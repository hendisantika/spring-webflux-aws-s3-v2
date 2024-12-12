package id.my.hendisantika.webfluxawss3v2.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.26
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FileValidatorException.class)
    public ErrorResponse handleFileValidatorException(FileValidatorException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage())
                .title("File Validator Exception")
                .type(URI.create("https://api.error.code"))
                .property("timestamp", Instant.now())
                .build();
    }

    @ExceptionHandler(DataBufferLimitException.class)
    public ErrorResponse handleLimitException(DataBufferLimitException e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage())
                .title("File Limit Exception")
                .type(URI.create("https://api.error.code"))
                .property("timestamp", Instant.now())
                .build();
    }
}
