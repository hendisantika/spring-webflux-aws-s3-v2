package id.my.hendisantika.webfluxawss3v2.controller;

import id.my.hendisantika.webfluxawss3v2.common.FileUtils;
import id.my.hendisantika.webfluxawss3v2.domain.SuccessResponse;
import id.my.hendisantika.webfluxawss3v2.service.AWSS3FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.36
 * To change this template use File | Settings | File Templates.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/object")
public class AWSS3Controller {

    private final AWSS3FileStorageService fileStorageService;

    @PostMapping("/upload")
    public Mono<SuccessResponse> upload(@RequestPart("file-data") Mono<FilePart> filePart) {
        return filePart
                .map(file -> {
                    FileUtils.filePartValidator(file);
                    return file;
                })
                .flatMap(fileStorageService::uploadObject)
                .map(fileResponse -> new SuccessResponse(fileResponse, "Upload successfully"));
    }

    @GetMapping(path = "/{fileKey}")
    public Mono<SuccessResponse> download(@PathVariable("fileKey") String fileKey) {
        return fileStorageService.getByteObject(fileKey)
                .map(objectKey -> new SuccessResponse(objectKey, "Object byte response"));
    }

    @DeleteMapping(path = "/{objectKey}")
    public Mono<SuccessResponse> deleteFile(@PathVariable("objectKey") String objectKey) {
        return fileStorageService.deleteObject(objectKey)
                .map(resp -> new SuccessResponse(null, MessageFormat.format("Object with key: {0} deleted successfully", objectKey)));
    }

    @GetMapping
    public Flux<SuccessResponse> getObject() {
        return fileStorageService.getObjects()
                .map(objectKey -> new SuccessResponse(objectKey, "Result found"));
    }
}
