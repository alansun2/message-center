package vip.tuoyang.push.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.tuoyang.base.codec.jackson.JacksonConfig;

/**
 * @author AlanSun
 * @date 2022/1/20 10:30
 **/
@Configuration(proxyBeanMethods = false)
public class RabbitMqConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter(JacksonConfig.getSingletonObjectMapper());
    }
}
