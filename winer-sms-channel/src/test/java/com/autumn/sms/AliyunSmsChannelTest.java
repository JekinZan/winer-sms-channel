package com.autumn.sms;

import com.winer.sms.channel.DefaultSmsMessage;
import com.winer.sms.channel.aliyun.AliyunSmsChannel;
import com.winer.sms.channel.aliyun.AliyunSmsProperties;
import org.junit.Test;

/**
 * 阿里云测试
 *
 * @author Jekin 2018-12-07 23:52:54
 */
public class AliyunSmsChannelTest extends AbstractSmsChannelTest {

    private String accessKeyId = "accessKeyId";
    private String accessKeySecret = "accessKeySecret";

    @Test
    public void sendTest() throws Exception {



        AliyunSmsProperties properties = new AliyunSmsProperties();

		properties.setAccessKeyId(accessKeyId);
		properties.setAccessKeySecret(accessKeySecret);

		AliyunSmsChannel channel = new AliyunSmsChannel(properties);

        DefaultSmsMessage msg = new DefaultSmsMessage();
        msg.setPhoneNumbers("130.....");
        msg.setSignName("妙常乐");
        msg.setTemplateCode("SMS_256666");
        msg.getParams().put("code", "123456");

        channel.send(msg);

        System.out.println("发送成功");

    }
}
