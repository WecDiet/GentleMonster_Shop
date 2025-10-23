package com.gentlemonster.Filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.Entities.User;
import com.gentlemonster.Exception.RateLimitExceededException;
import com.gentlemonster.Services.RateLimit.RateLimitService;
import com.gentlemonster.Utils.JwtTokenUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response, 
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Kiểm tra nếu request thuộc danh sách bypass
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response); 
                return;
            }    

            // Lấy header Authorization
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid Authorization header");
                return; // CRITICAL: Must return here
            }
            
            String token = authHeader.substring(7).trim();

            // Lấy subject từ token
            String subject = jwtTokenUtils.extractSubject(token);
            
            // Tải thông tin người dùng
            User userDetails = (User) userDetailsService.loadUserByUsername(subject);
            log.info("Authenticating user: {}", userDetails.getUsername());
            
            // Xác thực token
            if (!jwtTokenUtils.isValidToken(token, userDetails)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token validation failed");
                return; // CRITICAL: Must return here
            }

            // Token hợp lệ - Tạo authentication
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(
                    userDetails, 
                    null, 
                    userDetails.getAuthorities()
                );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // // Kiểm tra rate limit TRƯỚC KHI cho request đi tiếp
            // String action = request.getMethod() + ":" + request.getServletPath();
            // String type = determineType(request.getServletPath());
            // rateLimitService.isAllowed(userDetails.getUsername(), action, type);

            // Nếu không throw, tiếp tục chain
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
        }
    }

    private boolean isBypassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
            Pair.of("/us", "GET"),
            Pair.of("/us/store/", "GET"),
            Pair.of(String.format("%s/list/", Enpoint.API_PREFIX_SHOP), "GET"),
            Pair.of(String.format("%s/item/", Enpoint.API_PREFIX_SHOP), "GET"),
            Pair.of(String.format("%s/stories/", Enpoint.API_PREFIX_SHOP), "GET"),
            Pair.of(String.format("%s/account_register", Enpoint.Auth.BASE), "POST"),
            Pair.of(String.format("%s/login", Enpoint.Auth.BASE), "POST"),
            Pair.of(String.format("%s/login", Enpoint.Auth.BASE_ADMIN), "POST"),
            Pair.of(String.format("%s/new", Enpoint.Banner.BASE), "POST"),
            Pair.of(String.format("%s/new", Enpoint.Slider.BASE), "POST"),
            Pair.of(String.format("%s/users/", Enpoint.API_PREFIX_ADMIN), "GET"),
            Pair.of(String.format("%s/user_detail/", Enpoint.User.BASE), "DELETE"),
            
            // Swagger endpoints
            Pair.of("/swagger-ui/", "GET"),
            Pair.of("/swagger-ui", "GET"),
            Pair.of("/swagger-ui.html", "GET"),
            Pair.of("/swagger-ui/index.html", "GET"),
            Pair.of("/swagger/", "GET"),
            Pair.of("/v3/api-docs/", "GET"),
            Pair.of("/api-docs/", "GET"),
            Pair.of("/api-docs", "GET"),
            Pair.of("/swagger-resources/", "GET"),
            Pair.of("/configurations/ui", "GET"),
            Pair.of("/configurations/security", "GET")
        );
        
        String servletPath = request.getServletPath();
        String method = request.getMethod();
        
        for (Pair<String, String> bypassToken : byPassTokens) {
            String pattern = bypassToken.getFirst();
            String allowedMethod = bypassToken.getSecond();
            
            // Kiểm tra method trước
            if (!method.equals(allowedMethod)) {
                continue;
            }
            
            // Kiểm tra path - hỗ trợ wildcard
            if (pattern.endsWith("/")) {
                // Pattern dạng /path/ -> match prefix
                if (servletPath.startsWith(pattern)) {
                    return true;
                }
            } else {
                // Pattern exact match
                if (servletPath.equals(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String determineType(String path) {
        if (path.startsWith(Enpoint.API_PREFIX_ADMIN)) {
            return "admin";
        } else {
            return "public";
        }
    }
}