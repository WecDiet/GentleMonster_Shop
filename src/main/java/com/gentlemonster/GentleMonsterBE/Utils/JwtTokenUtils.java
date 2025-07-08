package com.gentlemonster.GentleMonsterBE.Utils;

import com.gentlemonster.GentleMonsterBE.Exception.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private Long expiration; // thời gian để kết thúc token

    @Value("${jwt.secretKey}")
    private String secretKey; // khoá bí mật để ký token

    @Value("${jwt.jwtRefreshExpiration}")
    private Long jwtRefreshExpiration; // thời gian để kết thúc refresh token



    // Getter cho expiration
    public Long getExpiration() {
        if (expiration == null || expiration <= 0) {
            throw new IllegalStateException("JWT expiration time is not configured or invalid: " + expiration);
        }
        return expiration;
    }

    // Getter cho jwtRefreshExpiration
    public Long getJwtRefreshExpiration() {
        if (jwtRefreshExpiration == null || jwtRefreshExpiration <= 0) {
            throw new IllegalStateException("JWT refresh expiration time is not configured or invalid: " + jwtRefreshExpiration);
        }
        return jwtRefreshExpiration;
    }

    public String generateToken(User user) throws Exception {
        // thuộc tính => claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole() != null ? user.getRole().getName().replace("ROLE_", "") : null); // thêm role của user vào claims
        if (user.getRole() == null || user.getRole().getName() == null) {
            throw new InvalidParamException("User role is missing");
        }
        String subject;
        if (user.getRole() != null && Arrays.asList("EMPLOYEE", "ADMIN", "STORE_MANAGER", "STORAGE_MANAGER")
            .contains(user.getRole().getName().toUpperCase())) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new InvalidParamException("Username is required for EMPLOYEE, ADMIN, STORE_MANAGER, or STORAGE_MANAGER role");
            }
            subject = user.getUsername(); // nếu là nhân viên thì sử dụng username làm subject
        } else {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new InvalidParamException("Email is required for non-EMPLOYEE role");
            }
            subject = user.getEmail(); // nếu không phải nhân viên thì sử dụng email làm subject
        }
        System.out.println("Token Key: " + this.generateSecretKey()); // in ra khoá bí mật để kiểm tra
        try {
            String token = Jwts.builder()
                    .setClaims(claims) // setClaims: set các claims cho token
                    .setSubject(subject) // setSubject: set chủ đề của token
                    .setIssuedAt(Date.from(Instant.now())) // setIssuedAt: set thời gian phát hành token
                    .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.HOURS))) // setExpiration: set thời gian hết hạn của token
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // signWith: ký token bằng thuật toán HS256
                    .compact(); // compact: tạo token
            return token;
        }catch (Exception e){
            throw new InvalidParamException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    public String generateRefreshToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole() != null ? user.getRole().getName().replace("ROLE_", "") : null); // thêm role của user vào claims
        if (user.getRole() == null || user.getRole().getName() == null) {
            throw new InvalidParamException("User role is missing");
        } // thêm role của user vào claims
        String subject;
        if (user.getRole() != null && Arrays.asList("EMPLOYEE", "ADMIN", "STORE_MANAGER", "STORAGE_MANAGER")
            .contains(user.getRole().getName().toUpperCase())) {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                throw new InvalidParamException("Username is required for EMPLOYEE, ADMIN, STORE_MANAGER, or STORAGE_MANAGER role");
            }
            subject = user.getUsername(); // nếu là nhân viên thì sử dụng username làm subject
        } else {
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                throw new InvalidParamException("Email is required for non-EMPLOYEE role");
            }
            subject = user.getEmail(); // nếu không phải nhân viên thì sử dụng email làm subject
        }
        System.out.println("RefreshToken Key: " + this.generateSecretKey()); // in ra khoá bí mật để kiểm tra
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plus(jwtRefreshExpiration, ChronoUnit.HOURS)))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e){
            throw new InvalidParamException("Cannot create jwt RefreshToken , error: "+e.getMessage());
        }

    }

    // Hàm này sẽ lấy khoá bí mật để ký token
    private Key getSignInKey() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        try {
            byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
            if (secretKeyBytes.length < 32) { // HS256 yêu cầu tối thiểu 32 byte
                throw new IllegalStateException("JWT secret key is too short (minimum 32 bytes required)");
            }
            return Keys.hmacShaKeyFor(secretKeyBytes);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid JWT secret key format: " + e.getMessage());
        }
    }

    // Hàm này sẽ tạo một khoá bí mật mới để ký token
    private String generateSecretKey(){
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return Encoders.BASE64.encode(key);
    }

    // Hàm này sẽ trích xuất tất cả các claims từ token
    // Claims là các thông tin được lưu trữ trong token, ví dụ như email, thời gian phát hành, thời gian hết hạn, v.v.
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

    // Hàm này sẽ trích xuất email từ token
    // Email được lưu trữ trong claim subject của token
    // public String extractEmail(String token) {
    //     return extractClaim(token, Claims::getSubject);
    // }

    // Hàm này sẽ kiểm tra xem token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration); // lấy thời gian hết hạn của token
        return expirationDate.before(new Date()); // trả về true nếu token hết hạn, ngược lại trả về false
    }

    // Hàm lấy subject từ token
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject); // lấy subject từ token
    }


    public boolean isValidToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey()) // set khoá bí mật để ký token
                    .build() // build parser
                    .parseClaimsJws(token); // parse token thành JWS
            return true; // nếu token hợp lệ thì trả về true
        } catch (JwtException | IllegalArgumentException e) {
            // Nếu token không hợp lệ hoặc có lỗi khi parse, trả về false
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }
}
