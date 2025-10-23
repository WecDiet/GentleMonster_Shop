package com.gentlemonster.Services.RateLimit;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.Exception.RateLimitExceededException;
import com.gentlemonster.Utils.LocalizationUtils;
import com.google.common.hash.Hashing;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateLimitService implements IRateLimitService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocalizationUtils localizationUtil;

    @Value("${spring.ratelimit.public.requests}")
    private int publicRateLimitRequests;
    @Value("${spring.ratelimit.public.duration}")
    private Duration publicRateLimitDuration;

    @Value("${spring.ratelimit.admin.requests}")
    private int adminRateLimitRequests;
    @Value("${spring.ratelimit.admin.duration}")
    private Duration adminRateLimitDuration;

    // Lua script cập nhật: Atomic get hash, check time, increment or reset
    private static final String LUA_SCRIPT = "local key = KEYS[1]\n" +
            "local max = tonumber(ARGV[1])\n" +
            "local expire = tonumber(ARGV[2])\n" +
            "local current_time = tonumber(ARGV[3])\n" + // Pass current timestamp
            "local hash = redis.call('HGETALL', key)\n" +
            "local count = 0\n" +
            "local iat = 0\n" +
            "if #hash > 0 then\n" +
            "    for i=1, #hash, 2 do\n" +
            "        if hash[i] == 'count' then count = tonumber(hash[i+1]) end\n" +
            "        if hash[i] == 'iat' then iat = tonumber(hash[i+1]) end\n" +
            "    end\n" +
            "    if current_time - iat > expire then\n" + // Vượt duration: reset
            "        count = 1\n" +
            "        redis.call('HSET', key, 'count', 1, 'iat', current_time)\n" +
            "        redis.call('EXPIRE', key, expire)\n" +
            "        return 1\n" +
            "    elseif count < max then\n" +
            "        count = count + 1\n" +
            "        redis.call('HINCRBY', key, 'count', 1)\n" + // Increment count
            "        return count\n" +
            "    else\n" +
            "        return 0  -- Vượt requests trong duration\n" +
            "    end\n" +
            "else\n" + // New: set count=1, iat=current
            "    count = 1\n" +
            "    redis.call('HSET', key, 'count', 1, 'iat', current_time)\n" +
            "    redis.call('EXPIRE', key, expire)\n" +
            "    return 1\n" +
            "end";

    private final RedisScript<Long> rateLimitScript = RedisScript.of(LUA_SCRIPT, Long.class);

    @Override
    public boolean isAllowed(HttpServletRequest request, String action, String type) throws RateLimitExceededException {
        log.info("Rate limit check: public {} - {}, admin {} - {}",
                publicRateLimitRequests, publicRateLimitDuration,
                adminRateLimitRequests, adminRateLimitDuration);

        log.info("request: " + request.getHeader("User-Agent"));
        String key = getClientKey(request);
        // Tích hợp action vào type nếu cần (type + ":" + action)
        String effectiveType = type + (action != null && !action.isEmpty() ? ":" + action : "");

        String redisKey = generateKeyRateLimit(key, effectiveType);
        int maxRequests = getMaxRequests(type);
        long expireSeconds = getExpireSeconds(type);
        long currentTime = System.currentTimeMillis() / 1000; // Current timestamp in seconds

        Long result = redisTemplate.execute(rateLimitScript,
                Collections.singletonList(redisKey),
                maxRequests,
                expireSeconds,
                currentTime);
        if (result > 0 && result <= maxRequests) {
            String uuid = UUID.randomUUID().toString();
            String bucketKey = "ratelimit:bucket:" + effectiveType + ":" + key;
            HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
            hashOps.put(bucketKey, uuid, System.currentTimeMillis());
            redisTemplate.expire(bucketKey, expireSeconds, TimeUnit.SECONDS);
            return true;
        } else if (result == null || result == 0) {
            throw new RateLimitExceededException(localizationUtil.getLocalizedMessage(MessageKey.RATE_LIMIT_EXCEEDED));
        } else {
            // Unexpected (rare)
            log.warn("Unexpected rate limit result: {} for key: {}", result, key);
            throw new RateLimitExceededException(localizationUtil.getLocalizedMessage(MessageKey.RATE_LIMIT_TRY_ERROR));
        }
    }

    @Override
    public void setValue(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, unit);
    }

    @Override
    public Object getValue(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public Boolean deleteKey(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        if (key == null || !key.equals(ops.get(key))) {
            return false;
        }
        return redisTemplate.delete(key);
    }

    private long getExpireSeconds(String type) {
        Duration duration;
        log.info(publicRateLimitRequests + " - " + publicRateLimitDuration);
        try {
            duration = switch (type) {
                case "public" -> Duration.parse(publicRateLimitDuration.toString());
                case "admin" -> Duration.parse(adminRateLimitDuration.toString());
                default -> throw new IllegalArgumentException("Unknown rate limit type: " + type);
            };
        } catch (DateTimeParseException e) {
            duration = Duration.ofSeconds(10); // Fallback nếu parse fail
            throw new RuntimeException("Invalid duration configuration for type: " + type, e);
        }
        return duration.getSeconds();

    }

    private Integer getMaxRequests(String type) {
        switch (type) {
            case "public":
                return publicRateLimitRequests;
            case "admin":
                return adminRateLimitRequests;
            default:
                throw new IllegalArgumentException("Unknown rate limit type: " + type);
        }
    }

    private String generateKeyRateLimit(String userName, String type) {
        Map<String, Object> keyMap = Map.of(
                "userName", userName,
                "type", type);
        try {
            String code = objectMapper.writeValueAsString(keyMap);
            String encode = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(code.getBytes(StandardCharsets.UTF_8));
            return "ratelimit:counter:" + encode;
        } catch (Exception e) {
            log.warn("Failed to encode rate limit key, using fallback", e);
            return "ratelimit:counter:" + type + ":" + userName;
        }
    }

    private String getClientKey(HttpServletRequest request) {
        String ip = getClientIP(request);
        String userAgent = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "unknown";
        log.info("User-Agent: " + userAgent);
        String combined = ip + "|" + userAgent;
        log.info("Combined key: " + combined);
        return Hashing.sha256().hashString(combined, StandardCharsets.UTF_8).toString();
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        log.info("X-Forwarded-For: " + ip);
        log.info("RemoteAddr: " + request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
