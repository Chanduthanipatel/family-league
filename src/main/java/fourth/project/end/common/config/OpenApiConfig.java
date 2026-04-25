package fourth.project.end.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH_SCHEME = "bearerAuth";

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Family League API")
                .version("v1")
                .description("Backend APIs for Family League predictions, results, and leaderboards"))
            .components(new Components()
                .addSecuritySchemes(BEARER_AUTH_SCHEME, new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
            .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME));
    }
}
