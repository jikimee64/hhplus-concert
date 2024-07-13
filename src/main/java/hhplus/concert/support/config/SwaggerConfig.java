package hhplus.concert.support.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local server")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("콘서트 예약 서비스 API 문서")
                .description("콘서트 예약 서비스 API 문서")
                .version("1.0.0");
    }
}