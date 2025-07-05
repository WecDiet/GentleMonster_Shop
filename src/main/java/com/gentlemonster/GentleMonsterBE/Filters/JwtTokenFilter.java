// package com.gentlemonster.GentleMonsterBE.Filters;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;

// import org.springframework.data.util.Pair;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.validation.constraints.NotNull;

// @Component
// public class JwtTokenFilter extends OncePerRequestFilter {

//     @Override
//     protected void doFilterInternal(@NotNull HttpServletRequest request,
//                                     @NotNull HttpServletResponse response, 
//                                     @NotNull FilterChain filterChain) throws ServletException, IOException {
//                     // filterChain.doFilter(request, response); // cho tất cả các request đi qua filter này
//         final List<Pair<String, String>> bypassTokken = Arrays.asList(Pair.of(null, null));         
//     }
    
// }
