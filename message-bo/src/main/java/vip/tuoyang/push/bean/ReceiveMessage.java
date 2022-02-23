package vip.tuoyang.push.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author AlanSun
 * @date 2022/1/19 16:30
 */
@ToString
@Getter
@Setter
public class ReceiveMessage {

    private Long serviceId;

    private String serviceType;

    private Boolean isSuccess;
}
