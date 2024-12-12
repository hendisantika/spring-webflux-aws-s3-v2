package id.my.hendisantika.webfluxawss3v2.config;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.27
 * To change this template use File | Settings | File Templates.
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties specific to aws client.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@Data
@Component
@ConfigurationProperties(prefix = "aws", ignoreUnknownFields = false)
public class AwsProperties {

    /**
     * Aws access key ID
     */
    private String accessKey;


    /**
     * Aws secret access key
     */
    private String secretKey;

    /**
     * Aws region
     */
    private String region;
    /**
     * Aws S3 bucket name
     */
    private String s3BucketName;

    /**
     * AWS S3 requires that file parts must have at least 5MB, except for the last part.
     */
    private int multipartMinPartSize;

    /**
     * S3 endpoint url
     */
    private String endpoint;
}
