package id.my.hendisantika.webfluxawss3v2.service;

import id.my.hendisantika.webfluxawss3v2.config.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.33
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AWSS3FileStorageService {
    private final S3AsyncClient s3AsyncClient;
    private final AwsProperties s3ConfigProperties;
}
