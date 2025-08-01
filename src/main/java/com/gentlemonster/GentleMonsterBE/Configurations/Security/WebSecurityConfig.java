package com.gentlemonster.GentleMonsterBE.Configurations.Security;

import com.gentlemonster.GentleMonsterBE.Contants.Enpoint;
import com.gentlemonster.GentleMonsterBE.Contants.Enpoint.User;
import com.gentlemonster.GentleMonsterBE.Filters.JwtTokenFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
        
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                    .requestMatchers(
                             "/swagger-ui/index.html",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/swagger/**",
                            "/v3/api-docs/**",
                            "/swagger-resources/**",
                            "/swagger-resources/",
                            "/Configurations/ui",
                            "/Configurations/security"
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
                    String.format("%s/stores/*",Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/store_detail/*",Enpoint.Store.BASE),
                    String.format("%s/stories/*", Enpoint.API_PREFIX_ADMIN),
                    String.format("%s/story_detail/*", Enpoint.Story.BASE),
                    String.format("%s/account/me", Enpoint.Auth.BASE_ADMIN),
                    // Shop API USER
                    String.format("%s/stores/*", Enpoint.API_PREFIX_SHOP),
                    "/us/store",
                    "/us", // Hiển thị toàn bộ banner ở trang chủ
                    String.format("%s/list/**", Enpoint.API_PREFIX_SHOP),
                    String.format("%s/item/**",Enpoint.API_PREFIX_SHOP),
                    String.format("%s/stories/**", Enpoint.API_PREFIX_SHOP),
                    String.format("%s/myaccount/me", Enpoint.Auth.BASE),
                    String.format("%s/myaccount/address", Enpoint.Auth.BASE)
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
                    String.format("%s/ware_product/new",Enpoint.Warehouse.BASE),
                    String.format("%s/new",Enpoint.Store.BASE),
                    String.format("%s/new", Enpoint.Story.BASE),
                    String.format("%s/product_detail/{productID}/upload", Enpoint.Product.BASE),
                    String.format("%s/{bannerID}/upload", Enpoint.Banner.BASE),
                    String.format("%s/slider_detail/{sliderID}/upload", Enpoint.Slider.BASE),
                    String.format("%s/store_detail/{storeID}/upload", Enpoint.Store.BASE),
                    String.format("%s/new/upload", Enpoint.Store.BASE),
                    String.format("%s/{cityID}/upload", Enpoint.City.BASE),
                    String.format("%s/story_detail/{storyID}/upload", Enpoint.Story.BASE),
                    String.format("%s/user_detail/{userID}/upload", Enpoint.User.BASE),
                    String.format("%s/ware_product/{productWarehouseID}/upload", Enpoint.Warehouse.BASE),


                    String.format("%s/login", Enpoint.Auth.BASE),
                    String.format("%s/login", Enpoint.Auth.BASE_ADMIN),
                    String.format("%s/change_password", Enpoint.Auth.BASE_ADMIN),
                    String.format("%s/change_password", Enpoint.Auth.BASE),
                    String.format("%s/myaccount/address", Enpoint.Auth.BASE),
                    String.format("%s/myaccount/me", Enpoint.Auth.BASE),
                    String.format("%s/myaccount/profile/password-verification", Enpoint.Auth.BASE_ADMIN),
                    String.format("%s/myaccount/profile/password-verification", Enpoint.Auth.BASE)

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
                    String.format("%s/story_detail/*", Enpoint.Story.BASE),
                    String.format("%s/myaccount/address", Enpoint.Auth.BASE),
                    String.format("%s/myaccount/profile/edit_profile", Enpoint.Auth.BASE)
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
                    String.format("%s/store_detail/*",Enpoint.Store.BASE),
                    String.format("%s/*", Enpoint.Banner.BASE),
                    String.format("%s/*", Enpoint.City.BASE),
                    String.format("%s/story_detail/*", Enpoint.Story.BASE),
                    String.format("%s/*", Enpoint.Story.BASE),


                    String.format("%s/myaccount/address", Enpoint.Auth.BASE)
                ).permitAll()
                .anyRequest()
                .authenticated()
        );

        // httpSecurity.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
        //     @Override
        //     public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
        //         CorsConfiguration configuration = new CorsConfiguration();
        //         configuration.setAllowedOrigins(List.of("*"));
        //         configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        //         configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        //         configuration.setExposedHeaders(List.of("x-auth-token"));
        //         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //         source.registerCorsConfiguration("/**", configuration);
        //         httpSecurityCorsConfigurer.configurationSource(source);
        //     }
        // });
        return httpSecurity.build();
    }
}
