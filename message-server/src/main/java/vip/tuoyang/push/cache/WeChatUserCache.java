package vip.tuoyang.push.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.tuoyang.base.cache.CacheUtils;
import vip.tuoyang.base.constants.BaseConstants;
import vip.tuoyang.push.bean.entity.WechatPublicFollow;
import vip.tuoyang.push.constants.RedisKeyConstants;
import vip.tuoyang.push.repo.WechatPublicFollowRepository;

import java.util.concurrent.TimeUnit;

/**
 * @author AlanSun
 * @date 2021/7/22 8:34
 */
@Component
public class WeChatUserCache {
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private WechatPublicFollowRepository wechatPublicFollowRepository;

    /**
     * 根据 openid 获取用户信息
     *
     * @param openId openId
     * @return {@link WechatPublicFollow}
     */
    public WechatPublicFollow getByOpenIdAndToUserName(String openId, String toUserName) {
        return cacheUtils.getCache(() -> wechatPublicFollowRepository.findDistinctFirstByOpenIdAndToUserNameAndIsDelete(openId, toUserName, BaseConstants.IS_DELETE_0), RedisKeyConstants.WECHAT_PUBLIC_FOLLOW_INFO_BY_OPENID + openId + ":" + toUserName, 3600 * 24, 3600 * 24 * 7, TimeUnit.SECONDS);
    }

    public void delete(String openId, String toUserName) {
        cacheUtils.delete(RedisKeyConstants.WECHAT_PUBLIC_FOLLOW_INFO_BY_OPENID + openId + ":" + toUserName);
    }
}
