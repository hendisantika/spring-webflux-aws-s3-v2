package id.my.hendisantika.webfluxawss3v2.domain;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.31
 * To change this template use File | Settings | File Templates.
 */
public record FileResponse(String name, String uploadId, String path, String type, String eTag) {
}
