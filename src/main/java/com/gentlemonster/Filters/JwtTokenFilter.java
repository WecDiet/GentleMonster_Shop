// package com.gentlemonster.Filters;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.util.Pair;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.gentlemonster.Contants.Enpoint;
// import com.gentlemonster.Entities.User;
// import com.gentlemonster.Utils.JwtTokenUtils;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.validation.constraints.NotNull;
// import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
// public class JwtTokenFilter extends OncePerRequestFilter {

//     private final UserDetailsService userDetailsService;
//     @Autowired
//     private JwtTokenUtils jwtTokenUtils;

//     @Override
//     protected void doFilterInternal(@NotNull HttpServletRequest request,
//                                     @NotNull HttpServletResponse response, 
//                                     @NotNull FilterChain filterChain) throws ServletException, IOException {
//                     // filterChain.doFilter(request, response); // cho tất cả các request đi qua filter này

//         // Kiểm tra nếu request thuộc danh sách bypass
//         if (isBypassToken(request)) {
//             filterChain.doFilter(request, response); 
//             return;
//         }    

//         // Lấy header Authorization
//         final String authHeader =  request.getHeader("Authorization");
//         if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing or invalid Authorization header");
//             return;
//         }
//         String token = authHeader.substring(7).trim();

//         try {
//             // Kiểm tra token hợp lệ
//             if (!jwtTokenUtils.isValidToken(token)) {
//                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Invalid token");
//                 return;
//             }

//             // Lấy subject từ token
//             String subject = jwtTokenUtils.extractSubject(token);

//             // Tải thông tin người dùng
//             User userDetails =(User) userDetailsService.loadUserByUsername(subject);

//             // Xác thực token
//             if(jwtTokenUtils.validateToken(token, userDetails)){
//                 // Tạo đối tượng authenticationToken
//                 UsernamePasswordAuthenticationToken authenticationToken = 
//                     new UsernamePasswordAuthenticationToken(
//                         userDetails, 
//                         null, 
//                         userDetails.getAuthorities()
//                     );
                
//                 // Thiết lập vào SecurityContextHolder 
//                 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//             }else{
//                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token validation failed");
//                 return;
//             }

//             filterChain.doFilter(request, response); // Tiếp tục chuỗi filter nếu token hợp lệ
//         } catch (Exception e) {
//             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error: " + e.getMessage());
//         }
//     }

//     // Phương thức kiểm tra bypass tokens
//     private boolean isBypassToken(@NotNull HttpServletRequest request) {
//         final List<Pair<String, String>> byPassTokens = Arrays.asList(
//             Pair.of("/us", "GET"),
//             Pair.of( "/us/store/*", "GET"),
//             Pair.of(String.format("%s/list/**", Enpoint.API_PREFIX_SHOP), "GET"),
//             Pair.of(String.format("%s/item/**",Enpoint.API_PREFIX_SHOP), "GET"),
//             Pair.of(String.format("%s/stories/**", Enpoint.API_PREFIX_SHOP), "GET"),

//             Pair.of(String.format("%s/account_register", Enpoint.Auth.BASE), "POST"),
//             Pair.of(String.format("%s/login", Enpoint.Auth.BASE), "POST"),
//             Pair.of(String.format("%s/login", Enpoint.Auth.BASE_ADMIN), "POST"),
//             Pair.of(String.format("%s/new",Enpoint.Banner.BASE),"POST"),
//             Pair.of(String.format("%s/new", Enpoint.Slider.BASE), "POST"),
//             Pair.of(String.format("%s/users/*",Enpoint.API_PREFIX_ADMIN), "GET"),
//             Pair.of(String.format("%s/user_detail/*", Enpoint.User.BASE), "DELETE"),

//                 Pair.of("/swagger-ui/**", "GET"),
//                 Pair.of("/swagger-ui", "GET"),
//                 Pair.of("/swagger-ui.html", "GET"),
//                 Pair.of("/swagger-ui/index.html", "GET"),
//                 Pair.of("/swagger/**", "GET"),
//                 Pair.of("/v3/api-docs/**", "GET"),
//                 Pair.of("/api-docs/**", "GET"),
//                 Pair.of("/api-docs", "GET"),
//                 Pair.of("/swagger-resources/**", "GET"),
//                 Pair.of("/swagger-resources/", "GET"),
//                 Pair.of("/Configurations/ui", "GET"),
//                 Pair.of("/Configurations/security", "GET")
//         );
//         for(Pair<String, String> bypassToken: byPassTokens){
//             if (request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())) {
//                 return true; // Request matches a bypass token condition
//             }
//         }
//         return false;
//     }
// }
