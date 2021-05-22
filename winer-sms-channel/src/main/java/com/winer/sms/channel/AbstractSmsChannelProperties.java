package com.winer.sms.channel;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 短信通道属性抽象
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-03-02 09:38
 **/
@Getter
@Setter
public class AbstractSmsChannelProperties implements Serializable {
    private static final long serialVersionUID = -7548555879403715539L;

    /**
     * 属性前缀
     */
    public final static String PREFIX = "winer.sms.channel";

    /**
     * 通道 Bean 后缀
     */
    public static final String CHANNEL_BEAN_SUFFIX = "SmsChannel";

    /**
     * 是否启用
     */
    private boolean enable = false;
}
