package vip.tuoyang.push.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.tuoyang.push.bean.request.RegisterWechatAppletDto;
import vip.tuoyang.push.service.PushService;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author AlanSun
 * @date 2022/1/19 10:23
 */
@Validated
@RestController
public class PushController {
    @Autowired
    private PushService pushService;

    /**
     * 注册小程序
     *
     * @param request {@link RegisterWechatAppletDto}
     */
    @PostMapping(value = "/register-wechat-applet")
    public void registerWechatApplet(@Validated @RequestBody @Size(min = 1, max = 50, message = "一次至少一个，最多50个") List<RegisterWechatAppletDto> request) {
        pushService.registerWechatApplet(request);
    }
}
