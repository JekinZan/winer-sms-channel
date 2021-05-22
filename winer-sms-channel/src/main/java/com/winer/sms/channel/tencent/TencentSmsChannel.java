package com.winer.sms.channel.tencent;

import com.autumn.exception.ExceptionUtils;
import com.winer.sms.channel.AbstractSmsChannel;
import com.winer.sms.channel.SmsChannelContext;
import com.winer.sms.channel.SmsChannelException;
import com.winer.sms.channel.SmsMessage;
import com.autumn.util.StringUtils;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯云短信通道
 *
 * @author Jekin 2018-12-07 22:35:07
 */
public class TencentSmsChannel extends AbstractSmsChannel<TencentSmsProperties> {

    /**
     * 通道Id
     */
    public static final String CHANNEL_ID = "tencentSMS";

    /**
     * 通道名称
     */
    public static final String CHANNEL_NAME = "腾讯云短信";

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
    public TencentSmsChannel() {
        super(TencentSmsProperties.class, null);
    }

    /**
     * 实例化
     *
     * @param channelDefaultProperties
     */
    public TencentSmsChannel(TencentSmsProperties channelDefaultProperties) {
        super(TencentSmsProperties.class, null, channelDefaultProperties);
    }

    /**
     * 实例化
     *
     * @param channelContext 通道上下文
     */
    public TencentSmsChannel(SmsChannelContext channelContext) {
        super(TencentSmsProperties.class, channelContext);
    }

    /**
     * 实例化
     *
     * @param channelContext           通道上下文
     * @param channelDefaultProperties
     */
    public TencentSmsChannel(SmsChannelContext channelContext, TencentSmsProperties channelDefaultProperties) {
        super(TencentSmsProperties.class, channelContext, channelDefaultProperties);
    }

    /**
     * 分页大小
     */
    private static final int MAX_SEND_PHONE_NUMBERS = 200;

    /**
     * 手机号分页
     *
     * @param phoneNumbers
     * @return
     */
    private List<ArrayList<String>> phoneNumberPageList(List<String> phoneNumbers) {
        List<ArrayList<String>> phonePages = new ArrayList<>();
        ArrayList<String> pageList = new ArrayList<>();
        for (String phone : phoneNumbers) {
            pageList.add(phone);
            if (pageList.size() >= MAX_SEND_PHONE_NUMBERS) {
                phonePages.add(pageList);
                pageList = new ArrayList<>();
            }
        }
        if (pageList.size() > 0) {
            phonePages.add(pageList);
        }
        return phonePages;
    }

    @Override
    protected void internalSend(SmsMessage message, TencentSmsProperties channelProperties) throws Exception {
        if (channelProperties.getAppId() == null) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 appId 的值或值为空");
        }
        if (StringUtils.isNullOrBlank(channelProperties.getAppKey())) {
            ExceptionUtils.throwValidationException("无法从配置属性中获取 appKey 的值或值为空");
        }
        String nationCode = this.getNationCode(message);
        int templateId = this.getTemplateId(message);
        ArrayList<String> phoneNumbers = this.toPhoneNumberList(message.getPhoneNumbers());
        ArrayList<String> params = this.getSendParams(message);
        if (phoneNumbers.size() == 1) {
            this.sendSingle(channelProperties.getAppId(), channelProperties.getAppKey().trim(), nationCode, phoneNumbers.get(0), templateId, params);
        } else {
            this.sendMultiple(channelProperties.getAppId(), channelProperties.getAppKey().trim(), nationCode, phoneNumbers, templateId, params);
        }
    }

    /**
     * 发送单条
     *
     * @param id
     * @param key
     * @param nationCode
     * @param phoneNumber
     * @param templateId
     * @param params
     * @throws Exception
     */
    private void sendSingle(int id, String key, String nationCode, String phoneNumber, int templateId,
                            ArrayList<String> params) throws Exception {
        SmsSingleSender ssender = new SmsSingleSender(id, key);
        // 签名参数未提供或者为空时，会使用默认签名发送短信
        SmsSingleSenderResult result = ssender.sendWithParam(nationCode, phoneNumber, templateId, params, "", "", "");
        if (result.result != 0) {
            throw new SmsChannelException(result.errMsg);
        }
    }

    /**
     * 批量发送
     *
     * @param id
     * @param key
     * @param nationCode
     * @param phoneNumbers
     * @param templateId
     * @param params
     * @throws Exception
     */
    private void sendMultiple(int id, String key, String nationCode, ArrayList<String> phoneNumbers, int templateId,
                              ArrayList<String> params) throws Exception {
        List<ArrayList<String>> phonePages = this.phoneNumberPageList(phoneNumbers);
        for (ArrayList<String> phonePage : phonePages) {
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            SmsMultiSender msender = new SmsMultiSender(id, key);
            SmsMultiSenderResult result = msender.sendWithParam(nationCode, phonePage, templateId, params, "", "", "");
            if (result.result != 0) {
                throw new SmsChannelException(result.errMsg);
            }
        }
    }

    private ArrayList<String> getSendParams(SmsMessage message) {
        ArrayList<String> params = new ArrayList<>();
        if (message.getParams() != null) {
            params.addAll(message.getParams().values());
        }
        return params;
    }

    private String getNationCode(SmsMessage message) {
        String nationCode = message.getNationCode();
        if (nationCode == null) {
            nationCode = "";
        } else {
            nationCode = nationCode.trim();
        }
        return nationCode;
    }

    /**
     * 获取短信模板Id
     *
     * @param message 消息
     * @return
     */
    private int getTemplateId(SmsMessage message) {
        try {
            return Integer.valueOf(message.getTemplateCode().trim());
        } catch (Exception e) {
            throw ExceptionUtils.throwValidationException("短信模板无法转换为整数。");
        }
    }
}
