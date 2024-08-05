package team5.doghae.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "독해 사이트",
                description = "멋사5팀 백엔드 API 문서입니다.",
                version = "v1"),
        servers = {
                @io.swagger.v3.oas.annotations.servers.Server(url = "https://doghae.site", description = "운영 서버")
        }
)
@Configuration
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        Server server = new Server();
        server.setUrl("https://doghae.site");

        return new OpenAPI()
                .components(components)
                .info(apiInfo2())
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo2() {
        return new Info()
                .title("Doghae APIs")
                .description("경북대학교 멋쟁이 사자처럼 5팀 백엔드 API입니다.")
                .version("1.0.0");
    }
}
