package id.my.hendisantika.webfluxawss3v2.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.51
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(@Value("${application-description}") String appDescription, @Value("${application" +
            "-version}") String appVersion) {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new io.swagger.v3.oas.models.info.Info()
                .title("AWS S3 v2 Service API")
                .description(appDescription)
                .version(appVersion)
                .contact(new io.swagger.v3.oas.models.info.Contact()
                        .name("Hendi Santika")
                        .url("https://s.id/hendisantika")
                        .email("hendisantika@yahoo.co.id"))
                .termsOfService("TOC")
                .license(new io.swagger.v3.oas.models.info.License().name("License").url("https://s.id/hendisantika")));
        return openAPI;
    }
}
