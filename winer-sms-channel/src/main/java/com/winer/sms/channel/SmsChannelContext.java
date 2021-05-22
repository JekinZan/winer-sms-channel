package com.winer.sms.channel;

import com.autumn.util.channel.ChannelContext;

import java.util.Collection;

/**
 * 短信通道上下文
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-03-02 00:08
 **/
public interface SmsChannelContext extends ChannelContext<SmsChannel> {

    /**
     * 注册监听
     *
     * @param channelListen
     */
    void registerChannelListen(SmsChannelListen channelListen);

    /**
     * 获取短信监听集合
     *
     * @return
     */
    Collection<SmsChannelListen> getSmsChannelListens();
}
