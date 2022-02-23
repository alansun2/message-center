package vip.tuoyang.push.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.tuoyang.base.exception.BizException;
import vip.tuoyang.push.bean.AbstractPushMessage;
import vip.tuoyang.push.bean.WechatPublicByAppletPushMessage;

/**
 * @author AlanSun
 * @date 2022/1/20 0:16
 */
@Service
public class HandlerFactory {
    @Autowired
    private WechatPublicMessageHandler wechatPublicMessageHandler;

    public MessageHandler createMessageHandler(AbstractPushMessage abstractPushMessage) {
        if (abstractPushMessage instanceof WechatPublicByAppletPushMessage) {
            return wechatPublicMessageHandler;
        }

        throw new BizException("不支持的消息");
    }
}
