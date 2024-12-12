package id.my.hendisantika.webfluxawss3v2.service;

import id.my.hendisantika.webfluxawss3v2.common.FileUtils;
import id.my.hendisantika.webfluxawss3v2.config.AwsProperties;
import id.my.hendisantika.webfluxawss3v2.domain.AWSS3Object;
import id.my.hendisantika.webfluxawss3v2.domain.FileResponse;
import id.my.hendisantika.webfluxawss3v2.domain.UploadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

    public Mono<byte[]> getByteObject(@NotNull String key) {
        LOGGER.debug("Fetching object as byte array from S3 bucket: {}, key: {}", s3ConfigProperties.getS3BucketName(), key);
        return Mono.just(GetObjectRequest.builder().bucket(s3ConfigProperties.getS3BucketName()).key(key).build())
                .map(it -> s3AsyncClient.getObject(it, AsyncResponseTransformer.toBytes()))
                .flatMap(Mono::fromFuture)
                .map(BytesWrapper::asByteArray);
    }

    public Mono<FileResponse> uploadObject(FilePart filePart) {
        String filename = filePart.filename();

        Map<String, String> metadata = Map.of("filename", filename);
        // get media type
        MediaType mediaType = ObjectUtils.defaultIfNull(filePart.headers().getContentType(), MediaType.APPLICATION_OCTET_STREAM);

        CompletableFuture<CreateMultipartUploadResponse> s3AsyncClientMultipartUpload = s3AsyncClient
                .createMultipartUpload(CreateMultipartUploadRequest.builder()
                        .contentType(mediaType.toString())
                        .key(filename)
                        .metadata(metadata)
                        .bucket(s3ConfigProperties.getS3BucketName())
                        .build());

        UploadStatus uploadStatus = new UploadStatus(Objects.requireNonNull(filePart.headers().getContentType()).toString(), filename);

        return Mono.fromFuture(s3AsyncClientMultipartUpload)
                .flatMapMany(response -> {
                    FileUtils.checkSdkResponse(response);
                    uploadStatus.setUploadId(response.uploadId());
                    LOGGER.info("Upload object with ID={}", response.uploadId());
                    return filePart.content();
                })
                .bufferUntil(dataBuffer -> {
                    // Collect incoming values into multiple List buffers that will be emitted by the resulting Flux each time the given predicate returns true.
                    uploadStatus.addBuffered(dataBuffer.readableByteCount());

                    if (uploadStatus.getBuffered() >= s3ConfigProperties.getMultipartMinPartSize()) {
                        LOGGER.info("BufferUntil - returning true, bufferedBytes={}, partCounter={}, uploadId={}",
                                uploadStatus.getBuffered(), uploadStatus.getPartCounter(), uploadStatus.getUploadId());

                        // reset buffer
                        uploadStatus.setBuffered(0);
                        return true;
                    }

                    return false;
                })
                .map(FileUtils::dataBufferToByteBuffer)
                // upload part
                .flatMap(byteBuffer -> uploadPartObject(uploadStatus, byteBuffer))
                .onBackpressureBuffer()
                .reduce(uploadStatus, (status, completedPart) -> {
                    LOGGER.info("Completed: PartNumber={}, etag={}", completedPart.partNumber(), completedPart.eTag());
                    (status).getCompletedParts().put(completedPart.partNumber(), completedPart);
                    return status;
                })
                .flatMap(uploadStatus1 -> completeMultipartUpload(uploadStatus))
                .map(response -> {
                    FileUtils.checkSdkResponse(response);
                    LOGGER.info("upload result: {}", response.toString());
                    return new FileResponse(filename, uploadStatus.getUploadId(), response.location(), uploadStatus.getContentType(), response.eTag());
                });
    }

    private Mono<CompletedPart> uploadPartObject(UploadStatus uploadStatus, ByteBuffer buffer) {
        final int partNumber = uploadStatus.getAddedPartCounter();
        LOGGER.info("UploadPart - partNumber={}, contentLength={}", partNumber, buffer.capacity());

        CompletableFuture<UploadPartResponse> uploadPartResponseCompletableFuture = s3AsyncClient.uploadPart(UploadPartRequest.builder()
                        .bucket(s3ConfigProperties.getS3BucketName())
                        .key(uploadStatus.getFileKey())
                        .partNumber(partNumber)
                        .uploadId(uploadStatus.getUploadId())
                        .contentLength((long) buffer.capacity())
                        .build(),
                AsyncRequestBody.fromPublisher(Mono.just(buffer)));

        return Mono
                .fromFuture(uploadPartResponseCompletableFuture)
                .map(uploadPartResult -> {
                    FileUtils.checkSdkResponse(uploadPartResult);
                    LOGGER.info("UploadPart - complete: part={}, etag={}", partNumber, uploadPartResult.eTag());
                    return CompletedPart.builder()
                            .eTag(uploadPartResult.eTag())
                            .partNumber(partNumber)
                            .build();
                });
    }
}
