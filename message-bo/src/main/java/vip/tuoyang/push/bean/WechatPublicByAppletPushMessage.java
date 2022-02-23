package vip.tuoyang.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AlanSun
 * @date 2022/1/18 16:45
 * <p>
 * 公众号消息，通过小程序绑定的公众号
 */
@ToString
@Getter
@Setter
public class WechatPublicByAppletPushMessage extends AbstractPushMessage {
    /**
     * 接收人账号（存在于第三方本地）
     */
    private String systemAccount;
    /**
     * 接收人类型
     */
    private String accountType;
    /**
     * 公众号 id(使用哪个公众号发送消息)
     */
    private String toUserName;
    /**
     * 模板消息
     */
    private WeChatPublicSendTemplateMessage request;
}
