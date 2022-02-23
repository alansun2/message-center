package vip.tuoyang.push.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author AlanSun
 * @date 2021/5/8 15:19
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "message")
public class ServiceSystemProperties {
    /**
     * 默认的公众号，用于发送消息
     */
    private String defaultToUserName;
}
