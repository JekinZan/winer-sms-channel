package com.winer.sms.channel.unicom;

import com.autumn.exception.ExceptionUtils;
import com.winer.sms.channel.AbstractSmsChannel;
import com.winer.sms.channel.SmsChannelContext;
import com.winer.sms.channel.SmsChannelException;
import com.winer.sms.channel.SmsMessage;
import com.autumn.util.StringUtils;
import com.autumn.util.http.HttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * 联通信使短信通道
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-03-02 10:09
 **/
public class UnicomSmsChannel extends AbstractSmsChannel<UnicomSmsProperties> {

    /**
     * 通道Id
     */
    public static final String CHANNEL_ID = "unicomSMS";

    /**
     * 通道名称
     */
    public static final String CHANNEL_NAME = "中国联通信使短信";

    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    /**
     * 实例化
     */
    public UnicomSmsChannel() {
        super(UnicomSmsProperties.class, null);
    }

    /**
     * 实例化
     *
     * @param channelDefaultProperties
     */
    public UnicomSmsChannel(UnicomSmsProperties channelDefaultProperties) {
        super(UnicomSmsProperties.class, null, channelDefaultProperties);
    }

    /**
     * 实例化
     *
     * @param channelContext 通道上下文
     */
    public UnicomSmsChannel(SmsChannelContext channelContext) {
        super(UnicomSmsProperties.class, channelContext);
    }

    /**
     * 实例化
     *
     * @param channelContext           通道上下文
     * @param channelDefaultProperties
     */
    public UnicomSmsChannel(SmsChannelContext channelContext, UnicomSmsProperties channelDefaultProperties) {
        super(UnicomSmsProperties.class, channelContext, channelDefaultProperties);
    }

    @Override
    protected void internalSend(SmsMessage message, UnicomSmsProperties channelProperties) throws Exception {
        if (StringUtils.isNullOrBlank(channelProperties.getAccount())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 account 的值或值为空");
        }
        if (StringUtils.isNullOrBlank(channelProperties.getPassword())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 password 的值或值为空");
        }
        if (StringUtils.isNullOrBlank(channelProperties.getAgent())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 agent 的值或值为空");
        }
        String signName = StringUtils.isNotNullOrBlank(message.getSignName()) ? message.getSignName() : channelProperties.getSign();
        String msgContent = message.getMessageContent();
        if (message.getParams() != null) {
            for (Map.Entry<String, String> entry : message.getParams().entrySet()) {
                msgContent = msgContent.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        this.sendSoapSms(channelProperties, signName, message.getPhoneNumbers(), msgContent);
    }

    private String checkNotArg(Object value, String name) {
        if (value == null || StringUtils.isNullOrBlank(value.toString())) {
            throw new SmsChannelException(this.getChannelName() + " 发送短信时，参数 " + name + " 为空或空白值。");
        }
        return value.toString();
    }

    private static final String RES_BEGIN_STRING = "<ns1:out>";
    private static final String RES_END_STRING = "</ns1:out>";

    /**
     * 发送 Soap 短信
     *
     * @param channelProperties
     * @param signName
     * @param phoneNumbers
     * @param message
     * @throws IOException
     */
    private void sendSoapSms(UnicomSmsProperties channelProperties, String signName, String phoneNumbers, String message) throws IOException {
        boolean isCustomSign = StringUtils.isNotNullOrBlank(signName);
        String methodName = isCustomSign ? "sendGWMsg" : "sendNormMsg";
        StringBuilder sendSoapString = new StringBuilder();
        sendSoapString.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tes=\"http://webservice.ocean.com\">");
        sendSoapString.append("<soapenv:Header/>");
        sendSoapString.append("<soapenv:Body>");
        sendSoapString.append("<tes:").append(methodName).append(">");
        addSoapArg(sendSoapString, 0, channelProperties.getAccount());
        addSoapArg(sendSoapString, 1, channelProperties.getPassword());
        addSoapArg(sendSoapString, 2, channelProperties.getAgent());
        addSoapArg(sendSoapString, 3, phoneNumbers);
        addSoapArg(sendSoapString, 4, isCustomSign ? (signName + message) : message);
        addSoapArg(sendSoapString, 5, "");
        addSoapArg(sendSoapString, 6, UUID.randomUUID().toString().replace("-", ""));
        sendSoapString.append("</tes:").append(methodName).append(">");
        sendSoapString.append("</soapenv:Body>");
        sendSoapString.append("</soapenv:Envelope>");
        String soap = sendSoapString.toString();
        HttpClient client = new HttpClient();
        client.setContentType("text/xml");
        client.setCharset(StandardCharsets.UTF_8);
        client.addHeader("msgNormWebService", methodName);
        String res = client.doPostForString(StringUtils.removeEnd(channelProperties.getServiceUrl(), '/') + "/ocservice/service/msgNormWebService", soap);
        if (res == null) {
            throw new SmsChannelException("发送短信响应为 null 。");
        }
        int beginIndex = res.indexOf(RES_BEGIN_STRING);
        if (beginIndex <= 0) {
            throw new SmsChannelException("发送短信响应错误：" + res);
        }
        int endIndex = res.indexOf(RES_END_STRING);
        if (endIndex <= 0) {
            throw new SmsChannelException("发送短信响应错误：" + res);
        }
        if (endIndex <= beginIndex + RES_BEGIN_STRING.length()) {
            throw new SmsChannelException("发送短信无响应消息。");
        }
        String resMsg = res.substring(beginIndex + RES_BEGIN_STRING.length(), endIndex);
        if (!resMsg.equals("1")) {
            throw new SmsChannelException("发送短信出错:" + resMsg);
        }
    }

    /**
     * 添加Soap参数
     *
     * @param sendSoapString
     * @param index
     * @param value
     */
    private void addSoapArg(StringBuilder sendSoapString, int index, String value) {
        sendSoapString
                .append("<arg").append(Integer.toString(index)).append(">")
                .append(value == null ? "" : value)
                .append("</arg").append(Integer.toString(index)).append(">");
    }


}
