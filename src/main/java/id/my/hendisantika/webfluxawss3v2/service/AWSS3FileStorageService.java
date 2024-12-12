package id.my.hendisantika.webfluxawss3v2.service;

import id.my.hendisantika.webfluxawss3v2.config.AwsProperties;
import id.my.hendisantika.webfluxawss3v2.domain.AWSS3Object;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Flux<AWSS3Object> getObjects() {
        LOGGER.info("Listing objects in S3 bucket: {}", s3ConfigProperties.getS3BucketName());
        return Flux.from(s3AsyncClient.listObjectsV2Paginator(ListObjectsV2Request.builder()
                        .bucket(s3ConfigProperties.getS3BucketName())
                        .build()))
                .flatMap(response -> Flux.fromIterable(response.contents()))
                .map(s3Object -> new AWSS3Object(s3Object.key(), s3Object.lastModified(), s3Object.eTag(), s3Object.size()));
    }

    public Mono<Void> deleteObject(@NotNull String objectKey) {
        LOGGER.info("Delete Object with key: {}", objectKey);
        return Mono.just(DeleteObjectRequest.builder().bucket(s3ConfigProperties.getS3BucketName()).key(objectKey).build())
                .map(s3AsyncClient::deleteObject)
                .flatMap(Mono::fromFuture)
                .then();
    }
}
