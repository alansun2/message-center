package vip.tuoyang.push.bean.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wechat_public_info")
public class WechatPublicInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "public_id", nullable = false, length = 50)
    private String publicId;

    @Column(name = "app_id", nullable = false, length = 50)
    private String appId;

    @Column(name = "app_secret", nullable = false)
    private String appSecret;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "is_delete", nullable = false)
    private Byte isDelete;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "create_by", nullable = false, length = 50)
    private String createBy;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_by", length = 50)
    private String updateBy;

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}