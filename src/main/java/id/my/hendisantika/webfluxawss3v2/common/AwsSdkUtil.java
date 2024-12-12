package id.my.hendisantika.webfluxawss3v2.common;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.SdkResponse;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-webflux-aws-s3-v2
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 12/12/24
 * Time: 16.20
 * To change this template use File | Settings | File Templates.
 */
@UtilityClass
@Slf4j
public class AwsSdkUtil {
    public boolean isErrorSdkHttpResponse(SdkResponse sdkResponse) {
        return sdkResponse.sdkHttpResponse() == null || !sdkResponse.sdkHttpResponse().isSuccessful();
    }
}
