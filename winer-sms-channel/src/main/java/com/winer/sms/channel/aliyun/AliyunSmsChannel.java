package com.winer.sms.channel.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.autumn.exception.ExceptionUtils;
import com.winer.sms.channel.AbstractSmsChannel;
import com.winer.sms.channel.SmsChannelContext;
import com.winer.sms.channel.SmsChannelException;
import com.winer.sms.channel.SmsMessage;
import com.winer.sms.channel.aliyun.model.SendSmsRequest;
import com.winer.sms.channel.aliyun.model.SendSmsResponse;
import com.autumn.util.StringUtils;
import com.autumn.util.json.JsonUtils;

import java.util.List;

/**
 * 阿里云短信通道
 *
 * @author Jekin 2018-12-07 10:34:36
 */
public class AliyunSmsChannel extends AbstractSmsChannel<AliyunSmsProperties> {

    /**
     * 通道Id
     */
    public static final String CHANNEL_ID = "aliyunSMS";

    /**
     * 通道名称
     */
    public static final String CHANNEL_NAME = "阿里云短信";

    // 产品名称:云通信短信API产品,开发者无需替换
    private static final String PRODUCT = "Dysmsapi";
    // 产品域名,开发者无需替换
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     *
     */
    static {
        DefaultProfile.addEndpoint("cn-hangzhou", PRODUCT, DOMAIN);
    }

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
    public AliyunSmsChannel() {
        super(AliyunSmsProperties.class, null);
    }

    /**
     * 实例化
     *
     * @param channelDefaultProperties
     */
    public AliyunSmsChannel(AliyunSmsProperties channelDefaultProperties) {
        super(AliyunSmsProperties.class, null, channelDefaultProperties);
    }

    /**
     * 实例化
     *
     * @param channelContext 通道上下文
     */
    public AliyunSmsChannel(SmsChannelContext channelContext) {
        super(AliyunSmsProperties.class, channelContext);
    }

    /**
     * 实例化
     *
     * @param channelContext           通道上下文
     * @param channelDefaultProperties
     */
    public AliyunSmsChannel(SmsChannelContext channelContext, AliyunSmsProperties channelDefaultProperties) {
        super(AliyunSmsProperties.class, channelContext, channelDefaultProperties);
    }



    /**
     * 分页大小
     */
    private static final int MAX_SEND_PHONE_NUMBERS = 1000;

    @Override
    protected void internalSend(SmsMessage message, AliyunSmsProperties channelProperties) throws Exception {
        String signName = message.getSignName();
        if (StringUtils.isNullOrBlank(signName)) {
            signName = channelProperties.getDefaultSignName();
        }
        if (StringUtils.isNullOrBlank(signName)) {
            ExceptionUtils.throwValidationException("signName 为空。");
        }
        if (StringUtils.isNullOrBlank(channelProperties.getAccessKeyId())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 accessKeyId 的值或值为空");
        }
        if (StringUtils.isNullOrBlank(channelProperties.getAccessKeySecret())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 accessKeySecret 的值或值为空");
        }
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                channelProperties.getAccessKeyId().trim(),
                channelProperties.getAccessKeySecret().trim());
        IAcsClient acsClient = new DefaultAcsClient(profile);
        List<String> pages = this.phoneNumberPages(message.getPhoneNumbers(), message.getNationCode(), MAX_SEND_PHONE_NUMBERS);
        for (String page : pages) {
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(page);
            request.setSignName(signName);
            request.setTemplateCode(message.getTemplateCode());
            request.setTemplateParam(JsonUtils.toJSONString(message.getParams()));
            SendSmsResponse response;
            try {
                response = acsClient.getAcsResponse(request);
                if (!"OK".equalsIgnoreCase(response.getCode())) {
                    throw new SmsChannelException(response.getMessage());
                }
            } catch (ServerException e) {
                throw new SmsChannelException(e.getMessage(), e);
            } catch (ClientException e) {
                throw new SmsChannelException(e.getMessage(), e);
            }
        }
    }
}
