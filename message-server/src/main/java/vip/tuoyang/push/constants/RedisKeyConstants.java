package vip.tuoyang.push.constants;

/**
 * @author AlanSun
 * @date 2021/5/10 19:20
 */
public class RedisKeyConstants {
    private static final String SERVICE_PREFIX = "PUSH:";
    /**
     * 微信公众号的关注信息,根据openId
     */
    public static final String WECHAT_PUBLIC_FOLLOW_INFO_BY_OPENID = SERVICE_PREFIX + "WECHAT_PUBLIC_FOLLOW_INFO_BY_OPENID:";
}
