package com.winer.sms.channel.cloopen;

import com.winer.sms.channel.AbstractSmsChannelProperties;
import com.autumn.validation.annotation.NotNullOrBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 北京容联易通信息技术有限公司短信接口属性
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-10-29 23:49
 **/
@Getter
@Setter
public class CloopenChannelProperties extends AbstractSmsChannelProperties {

    private static final long serialVersionUID = 3823660845718915536L;

    /**
     * bean条件属性
     */
    public static final String BEAN_CONDITIONAL_PROPERTY = AbstractSmsChannelProperties.PREFIX + ".cloopen.enable";

    /**
     * 通道 Bean 名称
     */
    public static final String CHANNEL_BEAN_NAME = "cloopen" + CHANNEL_BEAN_SUFFIX;

    /**
     * 服务器 Url
     */
    private String serverUrl = "https://app.cloopen.com:8883";

    /**
     * 主账户Sid
     */
    @NotNullOrBlank(message = "主账户Sid不能为空。")
    private String accountSid;

    /**
     * 授权令牌
     */
    @NotNullOrBlank(message = "授权令牌不能为空。")
    private String authToken;

    /**
     * 应用id
     */
    @NotNullOrBlank(message = "应用id不能为空。")
    private String appId;


}
