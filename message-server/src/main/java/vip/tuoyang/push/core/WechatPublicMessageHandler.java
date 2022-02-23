package vip.tuoyang.push.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.tuoyang.base.util.AssertUtils;
import vip.tuoyang.base.util.StringUtils;
import vip.tuoyang.push.bean.AbstractPushMessage;
import vip.tuoyang.push.bean.MessageResult;
import vip.tuoyang.push.bean.WeChatPublicSendTemplateMessage;
import vip.tuoyang.push.bean.WechatPublicByAppletPushMessage;
import vip.tuoyang.push.bean.entity.WechatPublicFollow;
import vip.tuoyang.push.config.properties.ServiceSystemProperties;
import vip.tuoyang.push.repo.WechatPublicFollowRepository;
import vip.tuoyang.push.service.WechatPublicManager;
import vip.tuoyang.wechat.bean.request.WeChatPublicSendTemplateMessageRequest;
import vip.tuoyang.wechat.bean.response.WeChatPublicSendTemplateMessageResponse;

import java.util.HashMap;

/**
 * @author AlanSun
 * @date 2022/1/18 16:56
 */
@Service
public class WechatPublicMessageHandler implements MessageHandler {
    @Autowired
    private WechatPublicFollowRepository wechatPublicFollowRepository;
    @Autowired
    private ServiceSystemProperties serviceSystemProperties;

    @Override
    public MessageResult handle(AbstractPushMessage abstractPushMessage) {
        final WechatPublicByAppletPushMessage wechatPublicByAppletPushMessage = (WechatPublicByAppletPushMessage) abstractPushMessage;
        AssertUtils.notNull(wechatPublicByAppletPushMessage.getSystemAccount(), "systemAccount null error");
        MessageResult messageResult = new MessageResult();
        AssertUtils.notNull(wechatPublicByAppletPushMessage.getAccountType(), "accountType null error");
        if (StringUtils.isAllEmpty(wechatPublicByAppletPushMessage.getToUserName(), serviceSystemProperties.getDefaultToUserName())) {
            messageResult.setErrorMsg("未指定发送的公众号 param:" + wechatPublicByAppletPushMessage);
            return messageResult;
        }

        final String toUserName = StringUtils.getDefaultIfNull(wechatPublicByAppletPushMessage.getToUserName(), serviceSystemProperties.getDefaultToUserName());
        final WechatPublicFollow wechatPublicFollow = wechatPublicFollowRepository.findByApplet(wechatPublicByAppletPushMessage.getSystemAccount(), wechatPublicByAppletPushMessage.getAccountType(), toUserName);
        if (wechatPublicFollow != null) {
            final WeChatPublicSendTemplateMessage weChatPublicSendTemplateMessage = wechatPublicByAppletPushMessage.getRequest();

            final WeChatPublicSendTemplateMessageRequest.Builder builder = WeChatPublicSendTemplateMessageRequest.Companion.builder().touser(wechatPublicFollow.getOpenId())
                    .templateId(weChatPublicSendTemplateMessage.getTemplate_id())
                    .url(weChatPublicSendTemplateMessage.getUrl());
            final WeChatPublicSendTemplateMessage.Miniprogram miniprogram = weChatPublicSendTemplateMessage.getMiniprogram();
            if (miniprogram != null) {
                builder.miniprogram(miniprogram.getAppid(), miniprogram.getPagepath());
            }
            final HashMap<String, WeChatPublicSendTemplateMessage.Data> data = weChatPublicSendTemplateMessage.getData();
            if (data != null && !data.isEmpty()) {
                data.forEach((key, data1) -> builder.put(key, data1.getValue(), data1.getColor()));
            }

            final WeChatPublicSendTemplateMessageResponse messageResponse = WechatPublicManager.send(toUserName, builder.build());
            messageResult.setIsSuccess(messageResponse.isSuccess());
            messageResult.setErrorMsg(messageResponse.getErrmsg());
        } else {
            messageResult.setErrorMsg("没有找到对应的公众号 param:" + wechatPublicByAppletPushMessage);
        }

        return messageResult;
    }
}
