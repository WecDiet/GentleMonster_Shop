package com.gentlemonster.GentleMonsterBE.Configurations.Security;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(
                        "/swagger-ui/index.html",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-resources/",
                        "/configuration/ui",
                        "/configuration/security"
                ).permitAll()

                .requestMatchers(HttpMethod.GET,
                    String.format("%s/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/users/*",Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/search/*", Enpoint.User.BASE),
                    String.format("%s/user_detail/*", Enpoint.User.BASE),
                    String.format("%s/roles/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/categories/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/category_detail/*", Enpoint.Category.BASE),
                    String.format("%s/sliders/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/slider_detail/*", Enpoint.Slider.BASE),
                    String.format("%s/product_types/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/product_type_detail/*", Enpoint.Product_Type.BASE),
                    String.format("%s/collaborations/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/collaboration_detail/*", Enpoint.Collaboration.BASEADMIN),
                    String.format("%s/banners/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/products/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/product_detail/*", Enpoint.Product.BASE),
                        String.format("%s/warehouses/*",Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/warehouse_detail/*", Enpoint.Warehouse.BASE),
                        String.format("%s/ware_product/*",Enpoint.Warehouse.BASE),
                        String.format("%s/cities/*",Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/subsidiaries/*",Enpoint.API_PREFIX_ADMIN),
                        String.format("%s/subsidiary_detail/*",Enpoint.Subsidiary.BASE),

                        // Shop API USER
                        "/us", // Hiển thị toàn bộ banner ở trang chủ
                    String.format("%s/list/**", Enpoint.API_PREFIX_SHOP),
                        String.format("%s/item/**",Enpoint.API_PREFIX_SHOP)
                ).permitAll()


                .requestMatchers(HttpMethod.POST,
                    String.format("%s/new", Enpoint.User.BASE),
                    String.format("%s/new", Enpoint.Role.BASE),
                    String.format("%s/new", Enpoint.Category.BASE),
                        String.format("%s/account_register", Enpoint.Auth.BASE),
                        String.format("%s/new", Enpoint.Slider.BASE),
                        String.format("%s/new", Enpoint.Product_Type.BASE),
                        String.format("%s/new", Enpoint.Product.BASE),
                        String.format("%s/new",Enpoint.Warehouse.BASE),
                        String.format("%s/new",Enpoint.Banner.BASE),
                        String.format("%s/new",Enpoint.City.BASE),
                        String.format("%s/new",Enpoint.Subsidiary.BASE),
                        String.format("%s/ware_product/new",Enpoint.Warehouse.BASE)
                ).permitAll()

                .requestMatchers(HttpMethod.PUT,
                    String.format("%s/user_detail/*", Enpoint.User.BASE),
                    String.format("%s/role_detail/*", Enpoint.Role.BASE),
                    String.format("%s/category_detail/*", Enpoint.Category.BASE),
                        String.format("%s/slider_detail/*", Enpoint.Slider.BASE),
                        String.format("%s/product_type_detail/*", Enpoint.Product_Type.BASE),
                        String.format("%s/collaboration_detail/*", Enpoint.Collaboration.BASEADMIN),
                        String.format("%s/warehouse_detail/*",Enpoint.Warehouse.BASE),
                        String.format("%s/ware_product/*",Enpoint.Warehouse.BASE),
                        String.format("%s/ware_product/*",Enpoint.Warehouse.BASE),
                        String.format("%s/product_detail/*",Enpoint.Product.BASE),
                        String.format("%s/*", Enpoint.Banner.BASE),
                        String.format("%s/*", Enpoint.City.BASE),
                        String.format("%s/subsidiary_detail/*",Enpoint.Subsidiary.BASE)
                ).permitAll()

                .requestMatchers(HttpMethod.DELETE,
                    String.format("%s/user_detail/*", Enpoint.User.BASE),
                    String.format("%s/role_detail/*", Enpoint.Role.BASE),
                    String.format("%s/category_detail/*", Enpoint.Category.BASE),
                        String.format("%s/slider_detail/*", Enpoint.Slider.BASE),
                        String.format("%s/product_type_detail/*", Enpoint.Product_Type.BASE),
                        String.format("%s/warehouse_detail/*",Enpoint.Warehouse.BASE),
                        String.format("%s/product_detail/*",Enpoint.Product.BASE),
                        String.format("%s/ware_product/*",Enpoint.Warehouse.BASE),
                        String.format("%s/*", Enpoint.Banner.BASE),
                        String.format("%s/*", Enpoint.City.BASE),
                        String.format("%s/subsidiary_detail/*",Enpoint.Subsidiary.BASE)
                ).permitAll()
                .anyRequest()
                .authenticated()
        ).csrf(AbstractHttpConfigurer::disable);

        httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
                configuration.setExposedHeaders(List.of("x-auth-token"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                httpSecurityCorsConfigurer.configurationSource(source);
            }
        });
        return httpSecurity.build();
    }
}
