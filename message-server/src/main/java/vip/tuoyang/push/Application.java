package vip.tuoyang.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import vip.tuoyang.base.cache.CacheUtils;
import vip.tuoyang.base.codec.jackson.JacksonConfig;
import vip.tuoyang.base.config.RedisConfig;

/**
 * @author AlanSun
 * @date 2022/1/21 16:02
 **/
@SpringBootApplication(scanBasePackages = "vip.tuoyang")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
