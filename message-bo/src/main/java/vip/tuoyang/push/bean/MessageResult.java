package vip.tuoyang.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AlanSun
 * @date 2022/1/20 0:20
 */
@ToString
@Getter
@Setter
public class MessageResult {

    private Boolean isSuccess = Boolean.FALSE;

    private String errorMsg;
}
