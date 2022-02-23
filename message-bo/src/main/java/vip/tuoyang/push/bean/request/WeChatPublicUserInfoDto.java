package vip.tuoyang.push.bean.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public final class WeChatPublicUserInfoDto {
    private Integer subscribe;
    private String openid;
    private String nickname;
    private Integer sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private Long subscribe_time;
    private String unionid;
    private String remark;
    private Integer groupid;
    private List<Integer> tagid_list;
    private String subscribe_scene;
}