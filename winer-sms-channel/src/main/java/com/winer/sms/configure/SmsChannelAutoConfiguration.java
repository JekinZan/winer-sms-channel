package com.winer.sms.configure;

import com.winer.sms.channel.SmsChannel;
import com.winer.sms.channel.SmsChannelContext;
import com.winer.sms.channel.SmsChannelListen;
import com.winer.sms.channel.aliyun.AliyunSmsChannel;
import com.winer.sms.channel.aliyun.AliyunSmsProperties;
import com.winer.sms.channel.cloopen.CloopenChannelProperties;
import com.winer.sms.channel.cloopen.CloopenSmsChannel;
import com.winer.sms.channel.impl.SmsChannelContextImpl;
import com.winer.sms.channel.tencent.TencentSmsChannel;
import com.winer.sms.channel.tencent.TencentSmsProperties;
import com.winer.sms.channel.unicom.UnicomSmsChannel;
import com.winer.sms.channel.unicom.UnicomSmsProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信配置
 *
 * @author Jekin 2018-12-08 00:21:54
 */
@Configuration
@EnableConfigurationProperties({AutumnSmsChannelProperties.class})
public class SmsChannelAutoConfiguration {

    /**
     * 短信通道上下文
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsChannelContext.class)
    public SmsChannelContext smsChannelContext() {
        return new SmsChannelContextImpl();
    }

    /**
     * 阿里云
     *
     * @param channelContext 通道上下文
     * @param properties     属性
     * @return
     */
    @Bean(AliyunSmsProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = AliyunSmsProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(AliyunSmsChannel.class)
    public SmsChannel aliyunSmsChannel(SmsChannelContext channelContext, AutumnSmsChannelProperties properties) {
        return new AliyunSmsChannel(channelContext, properties.getAliyun());
    }

    /**
     * 腾讯云
     *
     * @param channelContext 通道上下文
     * @param properties     属性
     * @return
     */
    @Bean(TencentSmsProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = TencentSmsProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(TencentSmsChannel.class)
    public SmsChannel tencentSmsChannel(SmsChannelContext channelContext, AutumnSmsChannelProperties properties) {
        return new TencentSmsChannel(channelContext, properties.getTencent());
    }

    /**
     * 中国联通信使短信通道
     *
     * @param channelContext 通道上下文
     * @param properties     属性
     * @return
     */
    @Bean(UnicomSmsProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = UnicomSmsProperties.CONDITIONAL_ENABLE, havingValue = "true")
    @ConditionalOnMissingBean(UnicomSmsChannel.class)
    public SmsChannel unicomSmsChannel(SmsChannelContext channelContext, AutumnSmsChannelProperties properties) {
        return new UnicomSmsChannel(channelContext, properties.getUnicom());
    }

    /**
     * 北京容联易短信通道
     *
     * @param channelContext 通道上下文
     * @param properties     属性
     * @return
     */
    @Bean(CloopenChannelProperties.CHANNEL_BEAN_NAME)
    @ConditionalOnProperty(name = CloopenChannelProperties.BEAN_CONDITIONAL_PROPERTY, havingValue = "true")
    @ConditionalOnMissingBean(CloopenSmsChannel.class)
    public SmsChannel cloopenSmsChannel(SmsChannelContext channelContext, AutumnSmsChannelProperties properties) {
        return new CloopenSmsChannel(channelContext, properties.getCloopen());
    }


    /**
     * 拦截
     *
     * @param channelContext 通道上下文
     * @return
     */
    @Bean
    public BeanPostProcessor autumnSmsChannelBeanPostProcessor(SmsChannelContext channelContext) {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof SmsChannel) {
                    channelContext.register((SmsChannel) bean);
                }
                if (bean instanceof SmsChannelListen) {
                    channelContext.registerChannelListen((SmsChannelListen) bean);
                }
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        };
    }

}
