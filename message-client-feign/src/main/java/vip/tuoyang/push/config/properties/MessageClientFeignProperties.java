package vip.tuoyang.push.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlanSun
 * @date 2022/1/20 10:38
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "message")
@Configuration(proxyBeanMethods = false)
public class MessageClientFeignProperties {
    /**
     * 消息服务的地址
     */
    private String serverUrl;
}
