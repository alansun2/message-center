package vip.tuoyang.push.bean.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2022/1/18 22:59
 **/
@Getter
@Setter
@Entity
@Table(name = "wechat_applet_account", indexes = {
        @Index(name = "index_union_id", columnList = "union_id"),
        @Index(name = "index_system_account", columnList = "system_account")
})
public class WechatAppletAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 用户系统的账号
     */
    @Column(name = "system_account", nullable = false)
    private String systemAccount;

    /**
     * 账号类型，不同用户体系不同的类型
     */
    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "union_id", nullable = false)
    private String unionId;

    @Column(name = "open_id", nullable = false)
    private String openId;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "is_delete")
    private Byte isDelete;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "create_by", length = 50)
    private String createBy;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_by", length = 50)
    private String updateBy;
}