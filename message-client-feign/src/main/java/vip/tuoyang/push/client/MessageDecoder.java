package vip.tuoyang.push.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import vip.tuoyang.base.bean.response.CommonResponse;
import vip.tuoyang.base.codec.jackson.JacksonConfig;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author AlanSun
 * @date 2022/1/20 13:47
 */
public class MessageDecoder implements Decoder {
    private final ObjectMapper objectMapper;

    public MessageDecoder() {
        this.objectMapper = JacksonConfig.getSingletonObjectMapper();
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        return objectMapper.readValue(response.body().asInputStream(), CommonResponse.class);
    }
}
