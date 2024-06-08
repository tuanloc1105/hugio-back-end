package vn.com.hugio.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${springdoc.version:1.0}") String appVersion) {
        final String bearerSecuritySchemeName = "BearerToken";
        //final String basicSecuritySchemeName = "BasicToken";
        return new OpenAPI()
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(bearerSecuritySchemeName)
                        //.addList(basicSecuritySchemeName)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        bearerSecuritySchemeName,
                                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                )
                        //.addSecuritySchemes(
                        //        basicSecuritySchemeName,
                        //        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
                        //)
                )
                .info(
                        new Info()
                                .title("HUGIO SERVICE API DOCUMENT")
                                .version(appVersion)
                                .license(
                                        //new License().name("Apache 2.0").url("http://springdoc.org")
                                        new License().name("Author: Loc Vo Tuan").url("https://www.facebook.com/hiiamlcx/")
                                )
                );
    }

}
