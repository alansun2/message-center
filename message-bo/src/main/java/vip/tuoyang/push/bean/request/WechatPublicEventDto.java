package vip.tuoyang.push.bean.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AlanSun
 * @date 2022/1/20 17:01
 */
@ToString
@Getter
@Setter
public class WechatPublicEventDto {
    /**
     * 1：关注；2：取消关注
     */
    private Byte eventType;
    /**
     * 用户公众号
     */
    private String fromUserName;
    /**
     * 开发者微信号
     */
    private String toUserName;
    /**
     * 用户信息
     */
    private WeChatPublicUserInfoDto userInfo;
}
