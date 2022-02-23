package vip.tuoyang.push.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vip.tuoyang.push.bean.entity.WechatPublicFollow;

import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2022/1/18 10:42
 **/
public interface WechatPublicFollowRepository extends JpaRepository<WechatPublicFollow, Long> {
    /**
     * 根据 openId 查询关注信息
     *
     * @param openId   openId
     * @param isDelete 是否删除
     * @return {@link WechatPublicFollow}
     */
    WechatPublicFollow findDistinctFirstByOpenIdAndToUserNameAndIsDelete(String openId, String toUserName, Byte isDelete);

    /**
     * 删除
     *
     * @param openId     openId
     * @param now        更新时间
     * @param toUserName 公众号
     * @return 影响行数
     */
    @Modifying
    @Query(value = "update WechatPublicFollow set isDelete = 1, updateTime = ?1, updateBy = 'SYSTEM' where openId = ?2 AND toUserName = ?3 AND isDelete = 0")
    int updateIsDelete(LocalDateTime now, String openId, String toUserName);

    /**
     * 根据以下条件查询公众号信息
     *
     * @param systemAccount 系统账号
     * @param accountType   账号类型
     * @param toUserName    指定的公众号
     * @return {@link WechatPublicFollow}
     */
    @Query(nativeQuery = true, value = "SELECT * FROM wechat_public_follow wpf " +
            "INNER JOIN wechat_applet_account waa ON wpf.union_id = waa.union_id AND wpf.is_delete = 0 AND waa.is_delete = 0 " +
            "WHERE waa.system_account = ?1 AND waa.account_type = ?2 AND wpf.to_user_name = ?3 LIMIT 1")
    WechatPublicFollow findByApplet(String systemAccount, String accountType, String toUserName);
}