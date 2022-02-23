package vip.tuoyang.push.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.tuoyang.base.constants.BaseConstants;
import vip.tuoyang.base.util.StringUtils;
import vip.tuoyang.push.bean.entity.WechatPublicFollow;
import vip.tuoyang.push.bean.request.WeChatPublicUserInfoDto;
import vip.tuoyang.push.bean.request.WechatPublicEventDto;
import vip.tuoyang.push.cache.WeChatUserCache;
import vip.tuoyang.push.config.properties.ServiceSystemProperties;
import vip.tuoyang.push.repo.WechatPublicFollowRepository;
import vip.tuoyang.wechat.bean.WeChatPublicUserInfo;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2021/5/6 14:44
 */
@Slf4j
@Service
public class WeChatService {
    @Autowired
    private WeChatUserCache weChatUserCache;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WechatPublicFollowRepository wechatPublicFollowRepository;
    @Autowired
    private ServiceSystemProperties serviceSystemProperties;

    /**
     * 处理接收的微信消息
     */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "message-wechat-public-event-queue", durable = "true"), exchange = @Exchange(value = "message-wechat-public-event"), key = "message-wechat-public-event"))
    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void receiveMessageHandle(@Payload WechatPublicEventDto wechatPublicEventDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("微信关注 wechatPublicEventDto: [{}]", wechatPublicEventDto);
            final String fromUserName = wechatPublicEventDto.getFromUserName();
            // 开发者微信号
            final String toUserName = StringUtils.getDefaultIfNull(wechatPublicEventDto.getToUserName(), serviceSystemProperties.getDefaultToUserName());
            if (wechatPublicEventDto.getEventType() == 1) {
                // 获取微信用户信息
                final WeChatPublicUserInfoDto weChatPublicUserInfoDto = wechatPublicEventDto.getUserInfo();
                WeChatPublicUserInfo userInfo = new WeChatPublicUserInfo();
                BeanUtils.copyProperties(weChatPublicUserInfoDto, userInfo);
                assert fromUserName != null;
                if (userInfo.getUnionid() != null) {
                    // 获取公众号
                    WechatPublicFollow wechatPublicFollow = weChatUserCache.getByOpenIdAndToUserName(userInfo.getOpenid(), toUserName);
                    if (wechatPublicFollow == null) {
                        wechatPublicFollow = new WechatPublicFollow();
                        wechatPublicFollow.setAvatar(userInfo.getHeadimgurl());
                        wechatPublicFollow.setNickName(userInfo.getNickname());
                        wechatPublicFollow.setUnionId(userInfo.getUnionid());
                        wechatPublicFollow.setOpenId(userInfo.getOpenid());
                        wechatPublicFollow.setToUserName(toUserName);
                        wechatPublicFollow.setIsDelete(BaseConstants.IS_DELETE_0);
                        wechatPublicFollow.setCreateTime(LocalDateTime.now());
                        wechatPublicFollow.setCreateBy("系统");
                        wechatPublicFollow.setUserInfo(objectMapper.writeValueAsString(userInfo));
                        wechatPublicFollowRepository.save(wechatPublicFollow);
                    }
                } else {
                    log.warn("获取微信用户信息失败：[{}]", userInfo);
                }
            } else {
                //处理取消订阅事件
                final WechatPublicFollow wechatPublicFollow = weChatUserCache.getByOpenIdAndToUserName(fromUserName, toUserName);
                if (wechatPublicFollow != null) {
                    //取消订阅
                    wechatPublicFollowRepository.updateIsDelete(LocalDateTime.now(), fromUserName, toUserName);
                    weChatUserCache.delete(fromUserName, toUserName);
                }
            }

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("微信关注异常，", e);
            log.error("微信关注异常，param: [{}]", wechatPublicEventDto);
            channel.basicNack(tag, false, true);
        }
    }
}
