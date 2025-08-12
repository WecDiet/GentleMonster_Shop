package com.gentlemonster.Configurations.OpenAPISwagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl
    ) {

        // http://localhost:9090/swagger-ui/index.html#/  Link truy cập web kiểm tra
        Server devServer = new Server();
        devServer.setUrl(serverUrl);
        devServer.setDescription("Server URL in Development environment");

        Info info = new Info();
        info.setTitle(title);
        info.setVersion(version);
        info.setDescription("Gentle Monster API Documentation");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));

    }
}
