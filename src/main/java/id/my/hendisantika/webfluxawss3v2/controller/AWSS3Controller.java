package id.my.hendisantika.webfluxawss3v2.controller;

import id.my.hendisantika.webfluxawss3v2.service.AWSS3FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
