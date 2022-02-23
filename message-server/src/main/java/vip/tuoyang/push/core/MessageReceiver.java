package vip.tuoyang.push.core;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.tuoyang.push.bean.AbstractPushMessage;
import vip.tuoyang.push.bean.MessageResult;

import java.io.IOException;
import java.time.Instant;

/**
 * @author AlanSun
 * @date 2022/1/20 8:46
 */
@Slf4j
@Service
public class MessageReceiver {
    @Autowired
    private HandlerFactory handlerFactory;

    @RabbitListener(bindings = @QueueBinding(value = @Queue("message-push-queue"), exchange = @Exchange(value = "message-push"), key = "message-push"))
    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void push(AbstractPushMessage abstractPushMessage, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("推送消息: [{}]", abstractPushMessage);
        try {
            boolean pushFlag = true;
            if (abstractPushMessage.getPushTime() != null && abstractPushMessage.getTimeToLive() != null) {
                if (abstractPushMessage.getPushTime() + abstractPushMessage.getTimeToLive() < Instant.now().toEpochMilli()) {
                    pushFlag = false;
                }
            }
            if (pushFlag) {
                final MessageResult messageResult = handlerFactory.createMessageHandler(abstractPushMessage).handle(abstractPushMessage);
                log.info("push message result: [{}]", messageResult);
            } else {
                log.info("消息过期，取消发送。param: [{}]", abstractPushMessage);
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("消息发送异常，", e);
            log.error("消息发送异常，param: [{}]", abstractPushMessage);
            channel.basicNack(tag, false, true);
        }
    }
}
