package vip.tuoyang.push.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import vip.tuoyang.push.bean.entity.WechatPublicInfo;
import vip.tuoyang.push.repo.WechatPublicInfoRepository;
import vip.tuoyang.wechat.WeChatPublicApi;
import vip.tuoyang.wechat.bean.request.WeChatPublicSendTemplateMessageRequest;
import vip.tuoyang.wechat.bean.response.WeChatPublicSendTemplateMessageResponse;
import vip.tuoyang.wechat.config.WeChatConfigProperties;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author AlanSun
 * @date 2022/1/21 8:58
 */
@Slf4j
@Service
public class WechatPublicManager implements CommandLineRunner {
    @Autowired
    private WechatPublicInfoRepository wechatPublicInfoRepository;
    @Autowired
    private BeanFactory beanFactory;

    private static final Map<String, WeChatPublicApi> PUBLIC_ID_MAP = new ConcurrentHashMap<>(4);

    private static WeChatPublicApi defaultClient;

    @Override
    public void run(String... args) {
        final List<WechatPublicInfo> wechatPublicInfos = wechatPublicInfoRepository.findByIsDelete((byte) 0);
        wechatPublicInfos.forEach(wechatPublicInfo -> {
            WeChatConfigProperties weChatConfigProperties = new WeChatConfigProperties();
            weChatConfigProperties.setAppId(wechatPublicInfo.getAppId());
            weChatConfigProperties.setAppSecret(wechatPublicInfo.getAppSecret());
            weChatConfigProperties.setToken(wechatPublicInfo.getToken());
            weChatConfigProperties.setCacheKeyPrefix("WECHAT_PUBLIC_HS_TEACHER:" + wechatPublicInfo.getPublicId());
            final WeChatPublicApi bean = beanFactory.getBean(WeChatPublicApi.class, weChatConfigProperties);
            PUBLIC_ID_MAP.put(wechatPublicInfo.getPublicId(), bean);
            if (defaultClient == null) {
                defaultClient = bean;
            }
        });
    }

    /**
     * 发送模板消息
     *
     * @param toUserName toUserName
     * @param request    {@link WeChatPublicSendTemplateMessageRequest}
     * @return true: 成功;false: 失败
     */
    public static WeChatPublicSendTemplateMessageResponse send(String toUserName, WeChatPublicSendTemplateMessageRequest request) {
        WeChatPublicApi weChatPublicApi = PUBLIC_ID_MAP.get(toUserName);
        if (weChatPublicApi == null) {
            weChatPublicApi = defaultClient;
        }

        if (weChatPublicApi == null) {
            log.warn("没有对应的公众号 toUserName: [{}]", toUserName);
            return new WeChatPublicSendTemplateMessageResponse(null, 500, "没有对应的公众号 toUserName:" + toUserName);
        }

        return weChatPublicApi.sendTemplateMessage(request, "微信推送");
    }
}
