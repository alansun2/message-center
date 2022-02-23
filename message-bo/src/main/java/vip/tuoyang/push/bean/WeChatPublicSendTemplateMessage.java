package vip.tuoyang.push.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * @author AlanSun
 * @date 2022/1/24 16:11
 **/
@Getter
@Setter
public final class WeChatPublicSendTemplateMessage {
    @NotNull
    public String touser;
    @NotNull
    public String template_id;
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    private String url;
    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private WeChatPublicSendTemplateMessage.Miniprogram miniprogram;
    @NotNull
    private HashMap<String, Data> data = new HashMap<>();

    public static Builder builder() {
        return new Builder();
    }

    @Getter
    @Setter
    public static final class Miniprogram {
        @NotNull
        private String appid;
        private String pagepath;

        public Miniprogram(@NotNull String appid, String pagepath) {
            this.appid = appid;
            this.pagepath = pagepath;
        }
    }

    @Getter
    @Setter
    public static final class Data {
        public String value;
        private String color;

        public Data() {
        }

        public Data(@NotNull String value, @NotNull String color) {
            this.value = value;
            this.color = color;
        }
    }

    public static final class Builder {
        private final WeChatPublicSendTemplateMessage weChatPublicSendTemplateMessage = new WeChatPublicSendTemplateMessage();

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder touser(@NotNull String touser) {
            this.weChatPublicSendTemplateMessage.setTouser(touser);
            return this;
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder templateId(@NotNull String templateId) {
            this.weChatPublicSendTemplateMessage.setTemplate_id(templateId);
            return this;
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder url(@NotNull String url) {
            this.weChatPublicSendTemplateMessage.setUrl(url);
            return this;
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder miniprogram(@NotNull String appid, String pagepath) {
            this.weChatPublicSendTemplateMessage.setMiniprogram(new WeChatPublicSendTemplateMessage.Miniprogram(appid, pagepath));
            return this;
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder first(@NotNull String value, @NotNull String color) {
            return this.put("first", value, color);
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder remark(@NotNull String value, @NotNull String color) {
            return this.put("remark", value, color);
        }

        @NotNull
        public WeChatPublicSendTemplateMessage.Builder put(@NotNull String key, @NotNull String value, @NotNull String color) {
            this.weChatPublicSendTemplateMessage.getData().put(key, new WeChatPublicSendTemplateMessage.Data(value, color));
            return this;
        }

        @NotNull
        public WeChatPublicSendTemplateMessage build() {
            return this.weChatPublicSendTemplateMessage;
        }
    }
}
