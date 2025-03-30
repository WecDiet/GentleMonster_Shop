package com.gentlemonster.GentleMonsterBE.Utils;

import com.gentlemonster.GentleMonsterBE.Exception.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import com.gentlemonster.GentleMonsterBE.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private Long expiration; // thời gian để kết thúc token

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.jwtRefreshExpiration}")
    private Long jwtRefreshExpiration;

    public String generateToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // setClaims: set các claims cho token
                    .setSubject(user.getEmail()) // setSubject: set chủ đề của token
                    .setIssuedAt(Date.from(Instant.now())) // setIssuedAt: set thời gian phát hành token
                    .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.HOURS))) // setExpiration: set thời gian hết hạn của token
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // signWith: ký token bằng thuật toán HS256
                    .compact(); // compact: tạo token
            return token;
        }catch (Exception e){
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }
    }

    private String generateRefreshToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plus(jwtRefreshExpiration, ChronoUnit.HOURS)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }

    }

    //
    private Key getSignInKey() {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    private String generateSecretKey(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return Encoders.BASE64.encode(key);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() // parserBuilder: tạo một builder để parse token
                .setSigningKey(getSignInKey())// setSigningKey: set key để ký token
                .build() // build: tạo parser
                .parseClaimsJws(token) // parseClaimsJws: parse token thành JWS
                .getBody(); // getBody: lấy các claims từ JWS
    }

    // Hàm này sẽ trích xuất claim từ token thông qua một function được truyền vào và trả về
    // giá trị của function đó sau khi áp dụng lên claims của token đó.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Hàm này sẽ kiểm tra xem token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration); // lấy thời gian hết hạn của token
        return expirationDate.before(new Date()); // trả về true nếu token hết hạn, ngược lại trả về false
    }

}
