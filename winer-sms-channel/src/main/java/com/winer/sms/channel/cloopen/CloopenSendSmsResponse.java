package com.winer.sms.channel.cloopen;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 发送短信响应
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-10-30 00:41
 **/
@Getter
@Setter
public class CloopenSendSmsResponse implements Serializable {

    private static final long serialVersionUID = -7600311805614724060L;

    /**
     * 成功状态码
     */
    public static final String SUCCESS_STATUS_CODE = "000000";


    /**
     * 状态码
     */
    private String statusCode;

    /**
     * 状态消息
     */
    private String statusMsg;

    /**
     * 短信唯一标识(成功后返回)
     */
    private String smsMessageSid;

    /**
     * 创建日期 格式 yyyyMMddHHmmss
     */
    private String dateCreated;


}
