package com.winer.sms.channel;

import com.autumn.util.channel.Channel;

/**
 * 短信通道
 *
 * @author Jekin 2018-12-07 10:16:28
 */
public interface SmsChannel extends Channel {

    /**
     * 获取监听上下文
     *
     * @return
     */
    SmsChannelContext getChannelContext();

    /**
     * 设置监听上下文
     *
     * @param channelContext 监听上下文
     */
    void setChannelContext(SmsChannelContext channelContext);

    /**
     * 获取通道属性类型
     *
     * @return
     */
    Class<? extends AbstractSmsChannelProperties> getChannelPropertiesType();

    /**
     * 采用默认配置属性发送短信
     *
     * @param message 短信消息
     * @throws Exception
     */
    void send(SmsMessage message) throws Exception;

    /**
     * 发送短信
     *
     * @param message           短信消息
     * @param channelProperties 通道属性
     * @throws Exception
     */
    void send(SmsMessage message, AbstractSmsChannelProperties channelProperties) throws Exception;
}
