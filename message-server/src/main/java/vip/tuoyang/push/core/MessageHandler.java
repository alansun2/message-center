package vip.tuoyang.push.core;

import vip.tuoyang.push.bean.AbstractPushMessage;
import vip.tuoyang.push.bean.MessageResult;

/**
 * @author AlanSun
 * @date 2022/1/18 16:55
 */
public interface MessageHandler {
    /**
     * 不同类型的消息处理
     *
     * @param abstractPushMessage {@link AbstractPushMessage}
     * @return {@link MessageResult}
     */
    MessageResult handle(AbstractPushMessage abstractPushMessage);
}
