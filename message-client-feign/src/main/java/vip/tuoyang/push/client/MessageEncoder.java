package vip.tuoyang.push.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import lombok.SneakyThrows;
import vip.tuoyang.base.codec.jackson.JacksonConfig;

import java.lang.reflect.Type;

/**
 * @author AlanSun
 * @date 2022/1/20 13:20
 */
public class MessageEncoder implements Encoder {

    private final ObjectMapper objectMapper;

    public MessageEncoder() {
        this.objectMapper = JacksonConfig.getSingletonObjectMapper();
    }

    @SneakyThrows
    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        template.body(objectMapper.writeValueAsString(object));
    }
}
