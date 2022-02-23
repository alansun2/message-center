package vip.tuoyang.push.bean.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2022/1/18 22:57
 **/
@Getter
@Setter
@Entity
@Table(name = "wechat_public_follow", indexes = {
        @Index(name = "index_union_id", columnList = "union_id"),
        @Index(name = "index_open_id_to_user_name", columnList = "open_id,to_user_name")
}
)
public class WechatPublicFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "union_id", nullable = false, length = 50)
    private String unionId;

    @Column(name = "open_id", nullable = false, length = 50)
    private String openId;

    @Column(name = "to_user_name", nullable = false, length = 50)
    private String toUserName;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "user_info")
    private String userInfo;

    @Column(name = "is_delete", nullable = false)
    private Byte isDelete;

    @Convert(disableConversion = true)
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "create_by", nullable = false, length = 50)
    private String createBy;

    @Convert(disableConversion = true)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_by", length = 50)
    private String updateBy;
}