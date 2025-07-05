package com.gentlemonster.GentleMonsterBE.Configurations.Security;


import com.gentlemonster.GentleMonsterBE.Repositories.IAuthRepository;
import com.gentlemonster.GentleMonsterBE.Utils.JwtTokenUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private IAuthRepository authRepository;
    // @Autowired
    // private JwtTokenUtils jwtTokenUtils;

    // UserDetail là đối tượng user theo chuẩn của java spring khi đăng nhập vào hệ thống thì nó sẽ tạo ra dối tượng đó 
    // và quản việc đăng nhập thông qua đối tượng user's detail object  
    // Quản lí đăng nhập dựa trên UserDetail
    @Bean
    public UserDetailsService userDetailsService(){
        return login -> {
            if (isAPIRequest()) {
                return authRepository.findByEmail(login)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + login));
            }
            return authRepository.findByUsername(login)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + login));
        };
    }


    // Kiểm tra xem request có phải là API request hay không
    // Nếu request là API request là /customer thì sẽ lấy thông tin người dùng theo email
    // Nếu request không phải là API request là /manager thì sẽ lấy thông tin người dùng theo username
    private boolean isAPIRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI().contains("/customer/");
        }
        return false;
    }

    // PasswordEncoder là đối tượng mã hoá mật khẩu
    @Bean
    public PasswordEncoder passwordEncode(){
        return new BCryptPasswordEncoder(10);
    }

    // AuthenticationProvider là đối tượng quản lí việc xác thực người dùng
    // Nó sẽ sử dụng UserDetailsService để lấy thông tin người dùng và PasswordEncoder để mã hoá mật khẩu
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncode());
        return authProvider;
    }


    // AuthenticationManager là đối tượng quản lí việc xác thực người dùng, 
    // nó sẽ sử dụng AuthenticationProvider để xác thực người dùng
    // AuthenticationManager sẽ được sử dụng trong quá trình xác thực người dùng

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    // @Bean
    // public JwtAuthenticationFilter jwtAuthenticationFilter() {
    //     return new JwtAuthenticationFilter(jwtTokenUtils, authRepository);
    // }
}