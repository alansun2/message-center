package vip.tuoyang.push.client;

import feign.Headers;
import feign.RequestLine;
import vip.tuoyang.base.bean.response.CommonResponse;
import vip.tuoyang.push.bean.request.RegisterWechatAppletDto;

import java.util.List;

/**
 * @author AlanSun
 * @date 2022/1/20 12:30
 */
public interface MessageFeignClient {
    /**
     * 注册小程序
     *
     * @param request {@link RegisterWechatAppletDto}
     */
    @RequestLine(value = "POST /register-wechat-applet")
    @Headers(value = "Content-Type: application/json")
    CommonResponse<?> registerWechatApplet(List<RegisterWechatAppletDto> request);
}
