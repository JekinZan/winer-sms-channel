package com.winer.sms.channel.unicom;

import com.winer.sms.channel.AbstractSmsChannelProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 联通信使Soap短信通道
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-03-02 10:08
 **/
@Getter
@Setter
public class UnicomSmsProperties extends AbstractSmsChannelProperties {

    private static final long serialVersionUID = -7974525450062059823L;

    /**
     * 启用条件
     */
    public final static String CONDITIONAL_ENABLE = AbstractSmsChannelProperties.PREFIX + ".unicom.enable";

    /**
     * 通道 Bean 名称
     */
    public static final String CHANNEL_BEAN_NAME =  "unicom" + CHANNEL_BEAN_SUFFIX;

    /**
     * 账号
     */
    private String account = "";

    /**
     * 密码
     */
    private String password = "";

    /**
     * 代理商
     */
    private String agent = "";

    /**
     * 短信签名
     */
    private String sign = "";

    /**
     * 服务地址
     */
    private String serviceUrl = "";
}
