package id.my.hendisantika.webfluxawss3v2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.30
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableWebFlux
public class WebConfiguration implements WebFluxConfigurer {
    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/codec/multipart/DefaultPartHttpMessageReader.html#setMaxInMemorySize(int)
        var partReader = new DefaultPartHttpMessageReader();
        partReader.setMaxParts(3);
        // Configure the maximum amount of disk space allowed for file parts
        partReader.setMaxDiskUsagePerPart(30L * 10000L * 1024L); // 307,2 MO
        partReader.setEnableLoggingRequestDetails(true);
        MultipartHttpMessageReader multipartReader = new
                MultipartHttpMessageReader(partReader);
        multipartReader.setEnableLoggingRequestDetails(true);
        configurer.defaultCodecs().multipartReader(multipartReader);

        /*
        Configure the maximum amount of memory allowed per part. When the limit is exceeded:
        file parts are written to a temporary file.
        non-file parts are rejected with DataBufferLimitException.
        */
        configurer.defaultCodecs().maxInMemorySize(512 * 1024);
    }
}
