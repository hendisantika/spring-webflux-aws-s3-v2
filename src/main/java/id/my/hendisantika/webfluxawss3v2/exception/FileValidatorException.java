package id.my.hendisantika.webfluxawss3v2.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.25
 * To change this template use File | Settings | File Templates.
 */
public class FileValidatorException extends RuntimeException {

    public FileValidatorException(String msg) {
        super(msg);
    }
}
