package com.winer.sms.configure;

import com.winer.sms.channel.AbstractSmsChannelProperties;
import com.winer.sms.channel.aliyun.AliyunSmsProperties;
import com.winer.sms.channel.cloopen.CloopenChannelProperties;
import com.winer.sms.channel.tencent.TencentSmsProperties;
import com.winer.sms.channel.unicom.UnicomSmsProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 短信属性
 *
 * @author Jekin 2018-12-07 23:58:35
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AbstractSmsChannelProperties.PREFIX)
public class AutumnSmsChannelProperties implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6200443879549297812L;


    /**
     * 阿里云属性
     */
    private AliyunSmsProperties aliyun = new AliyunSmsProperties();

    /**
     * 腾讯云属性
     */
    private TencentSmsProperties tencent = new TencentSmsProperties();

    /**
     * 联通信使属性
     */
    private UnicomSmsProperties unicom = new UnicomSmsProperties();

    /**
     * 京容联易通信息技术有限公司短信接口属性
     */
    private CloopenChannelProperties cloopen = new CloopenChannelProperties();

    /**
     *
     */
    public AutumnSmsChannelProperties() {

    }

}
