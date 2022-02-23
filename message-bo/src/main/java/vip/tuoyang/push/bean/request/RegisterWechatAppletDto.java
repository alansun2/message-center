package vip.tuoyang.push.bean.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author AlanSun
 * @date 2022/1/19 10:29
 */
@ToString
@Getter
@Setter
public class RegisterWechatAppletDto {
    @NotBlank(message = "systemAccount must trans")
    private String systemAccount;

    @NotNull(message = "accountType must trans")
    private String accountType;

    @NotBlank(message = "unionId must trans")
    public String unionId;

    @NotBlank(message = "openId must trans")
    public String openId;

    private String nickName;

    private String avatar;
}
