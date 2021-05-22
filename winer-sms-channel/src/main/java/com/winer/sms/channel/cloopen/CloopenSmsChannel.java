package com.winer.sms.channel.cloopen;

import com.winer.sms.channel.AbstractSmsChannel;
import com.winer.sms.channel.SmsChannelContext;
import com.winer.sms.channel.SmsChannelException;
import com.winer.sms.channel.SmsMessage;
import com.autumn.util.Base64Utils;
import com.autumn.util.StringUtils;
import com.autumn.util.http.HttpClient;
import com.autumn.util.json.JsonUtils;
import com.autumn.util.security.HashUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * 北京容联易通信息技术有限公司短信接口
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-10-29 23:48
 **/
public class CloopenSmsChannel extends AbstractSmsChannel<CloopenChannelProperties> {

    /**
     * 编码
     */
    public static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * 通道Id
     */
    public static final String CHANNEL_ID = "cloopenSMS";

    /**
     * 通道名称
     */
    public static final String CHANNEL_NAME = "北京容联易通短信";

    /**
     * 软件名及版本号
     */
    public static final String SOFT_VERSION = "2013-12-26";

    /**
     * 日期时间格式
     */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 分页大小
     */
    private static final int MAX_SEND_PHONE_NUMBERS = 200;

    @Override
    public String getChannelId() {
        return CHANNEL_ID;
    }

    @Override
    public String getChannelName() {
        return CHANNEL_NAME;
    }

    /**
     * 获取软件以及版本号
     *
     * @return
     */
    public String getSoftVersion() {
        return SOFT_VERSION;
    }

    /**
     * 实例化
     */
    public CloopenSmsChannel() {
        super(CloopenChannelProperties.class, null);
    }

    /**
     * 实例化
     *
     * @param channelDefaultProperties
     */
    public CloopenSmsChannel(CloopenChannelProperties channelDefaultProperties) {
        super(CloopenChannelProperties.class, null, channelDefaultProperties);
    }

    /**
     * 实例化
     *
     * @param channelContext 通道上下文
     */
    public CloopenSmsChannel(SmsChannelContext channelContext) {
        super(CloopenChannelProperties.class, channelContext);
    }

    /**
     * 实例化
     *
     * @param channelContext           通道上下文
     * @param channelDefaultProperties
     */
    public CloopenSmsChannel(SmsChannelContext channelContext, CloopenChannelProperties channelDefaultProperties) {
        super(CloopenChannelProperties.class, channelContext, channelDefaultProperties);
    }

    @Override
    protected void internalSend(SmsMessage message, CloopenChannelProperties channelProperties) throws Exception {
        LocalDateTime sendTime = LocalDateTime.now();
        String timestamp = sendTime.format(DATE_TIME_FORMAT);
        HttpClient client = new HttpClient();
        client.setContentType("application/json");
        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", this.createAuthorization(channelProperties, timestamp));
        StringBuilder urlBuilder = new StringBuilder(255);
        urlBuilder.append(StringUtils.removeEnd(channelProperties.getServerUrl().trim(), '/'))
                .append("/")
                .append(this.getSoftVersion())
                .append("/Accounts/")
                .append(channelProperties.getAccountSid())
                .append("/SMS/TemplateSMS?sig=")
                .append(this.createSigParameter(channelProperties, timestamp));
        List<String> pages = this.phoneNumberPages(message.getPhoneNumbers(), message.getNationCode(), MAX_SEND_PHONE_NUMBERS);
        for (String page : pages) {
            CloopenSendSmsRequest request = this.createRequest(message, page, channelProperties, timestamp);
            String requestStr = JsonUtils.toJSONString(request);
            String responseStr = client.doPostForString(urlBuilder.toString(), requestStr);
            String chennErr = "通道[" + this.getChannelId() + " " + this.getChannelName() + "]发送短信";
            if (StringUtils.isNullOrBlank(responseStr)) {
                throw new SmsChannelException(chennErr + requestStr + " 无任何响应。");
            }
            CloopenSendSmsResponse response = JsonUtils.parseObject(responseStr, CloopenSendSmsResponse.class);
            if (!CloopenSendSmsResponse.SUCCESS_STATUS_CODE.equalsIgnoreCase(response.getStatusCode())) {
                if (response.getStatusMsg() == null) {
                    response.setStatusMsg("发生未知错误。");
                }
                throw new SmsChannelException(chennErr + "出错:" + response.getStatusMsg());
            }
        }
    }

    /**
     * 创建请求
     *
     * @param message
     * @param to
     * @param channelProperties
     * @param timestamp
     * @return
     */
    protected CloopenSendSmsRequest createRequest(SmsMessage message, String to, CloopenChannelProperties channelProperties, String timestamp) {
        CloopenSendSmsRequest request = new CloopenSendSmsRequest();
        request.setAppId(channelProperties.getAppId());
        request.setTo(to);
        request.setTemplateId(message.getTemplateCode());
        String[] datas;
        if (message.getParams() != null) {
            datas = new String[message.getParams().size()];
            int i = 0;
            for (String value : message.getParams().values()) {
                datas[i] = value;
                i++;
            }
        } else {
            datas = new String[0];
        }
        request.setDatas(datas);
        return request;
    }


    /**
     * 创建签名参数
     *
     * @param channelProperties 通道属性
     * @param timestamp         时间戳 yyyyMMddHHmmss
     * @return
     */
    protected String createSigParameter(CloopenChannelProperties channelProperties, String timestamp) {
        String str = channelProperties.getAccountSid() + channelProperties.getAuthToken() + timestamp;
        return HashUtils.md5(str).replace("-", "").toUpperCase(Locale.ENGLISH);
    }

    /**
     * 创建认证请求头
     *
     * @param channelProperties 通道属性
     * @param timestamp         时间戳 yyyyMMddHHmmss
     * @return
     */
    protected String createAuthorization(CloopenChannelProperties channelProperties, String timestamp) {
        String str = channelProperties.getAccountSid() + ":" + timestamp;
        return Base64Utils.encodeToString(str, CHARSET);
    }


}
