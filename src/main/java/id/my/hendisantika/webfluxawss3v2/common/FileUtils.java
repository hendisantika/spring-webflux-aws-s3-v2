package id.my.hendisantika.webfluxawss3v2.common;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.21
 * To change this template use File | Settings | File Templates.
 */
@UtilityClass
@Slf4j
public class FileUtils {
    private final String[] contentTypes = {
            "image/png",
            "image/jpg",
            "image/jpeg",
            "image/bmp",
            "image/gif",
            "image/ief",
            "image/pipeg",
            "image/svg+xml",
            "image/tiff"
    };

    private boolean isValidType(final FilePart filePart) {
        return isSupportedContentType(Objects.requireNonNull(filePart.headers().getContentType()).toString());
    }
}
