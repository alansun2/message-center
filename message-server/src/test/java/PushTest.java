import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vip.tuoyang.base.util.DateUtils;
import vip.tuoyang.push.Application;
import vip.tuoyang.push.bean.MessageResult;
import vip.tuoyang.push.bean.WeChatPublicSendTemplateMessage;
import vip.tuoyang.push.bean.WechatPublicByAppletPushMessage;
import vip.tuoyang.push.core.HandlerFactory;

import java.time.LocalDateTime;

/**
 * @author AlanSun
 * @date 2021/11/25 23:50
 */
@Slf4j
@SpringBootTest(classes = Application.class)
public class PushTest {
    @Autowired
    private HandlerFactory handlerFactory;

    @Test
    public void test() {
        WechatPublicByAppletPushMessage abstractPushMessage = new WechatPublicByAppletPushMessage();
        abstractPushMessage.setSystemAccount("2");
        abstractPushMessage.setAccountType("HS_PARENT");
        final WeChatPublicSendTemplateMessage request = WeChatPublicSendTemplateMessage.builder()
                .touser("")
                .templateId("ruDTDdZYjBU-qQpJUcToRhtRASU126OLYKkZtrfYVJw")
                .first("尊敬的教师" + "，您的人脸导入任务【" + "test" + "】" + "成功", "#173177")
                .put("keyword1", "人脸导入", "#173177")
                .put("keyword2", "成功", "#173177")
                .put("keyword3", LocalDateTime.now().format(DateUtils.DATE_FORMAT_FORMATTER), "#173177")
                .remark("祝您生活愉快！", "#173177").build();
        abstractPushMessage.setRequest(request);
        abstractPushMessage.setServiceId(1L);
        abstractPushMessage.setServiceType("test");
        final MessageResult messageResult = handlerFactory.createMessageHandler(abstractPushMessage).handle(abstractPushMessage);
        log.info("push message result: [{}]", messageResult);
    }
}
