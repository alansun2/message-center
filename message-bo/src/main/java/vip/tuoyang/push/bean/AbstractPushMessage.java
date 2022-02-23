package vip.tuoyang.push.bean;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author AlanSun
 * @date 2022/1/18 16:44
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageType")
@Getter
@Setter
public abstract class AbstractPushMessage {
    /**
     * 业务 id
     */
    private Long serviceId;
    /**
     * 业务类型
     */
    private String serviceType;
    /**
     * 回调时的 routingKey
     */
    private String callbackRoutingKey;
    /**
     * 是否需要将结果返回
     */
    private Boolean isNeedCallback = Boolean.FALSE;
    /**
     * 发送时间，毫秒时间戳
     */
    private Long pushTime;
    /**
     * 消息有效时间,单位：ms，pushTime + timeToLive >= 当前时间就发送，否则不发送
     */
    private Long timeToLive;
}
