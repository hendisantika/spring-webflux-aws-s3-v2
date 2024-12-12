package id.my.hendisantika.webfluxawss3v2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.28
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@RequiredArgsConstructor
public class AwsS3Config {

    private final AwsProperties s3ConfigProperties;

    @Bean
    public S3AsyncClient s3AsyncClient(AwsCredentialsProvider awsCredentialsProvider) {
        return S3AsyncClient.builder()
                .httpClient(sdkAsyncHttpClient())
                .region(Region.of(s3ConfigProperties.getRegion()))
                .credentialsProvider(awsCredentialsProvider)
                .endpointOverride(URI.create(s3ConfigProperties.getEndpoint()))
                .forcePathStyle(true)
                .serviceConfiguration(s3Configuration()).build();
    }
}
