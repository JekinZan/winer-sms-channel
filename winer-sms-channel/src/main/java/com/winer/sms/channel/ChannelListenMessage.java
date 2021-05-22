package com.winer.sms.channel;

import java.io.Serializable;
import java.util.Date;

/**
 * 监听消息
 *
 * @author Jekin 2018-12-07 11:08:05
 */
public class ChannelListenMessage implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8132573232557893024L;

    private final Date startTime;
    private final Date endTime;
    private final SmsMessage message;
    private final AbstractSmsChannelProperties channelProperties;

    /**
     * 实例化
     *
     * @param startTime         开始时间
     * @param endTime           结束时间
     * @param message           消息
     * @param channelProperties 通道属性
     */
    public ChannelListenMessage(Date startTime, Date endTime, SmsMessage message,
                                AbstractSmsChannelProperties channelProperties) {
        super();
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
        this.channelProperties = channelProperties;
    }

    /**
     * 获取开始时间
     *
     * @return
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 获取结束时间
     *
     * @return
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 获取消息
     *
     * @return
     */
    public SmsMessage getMessage() {
        return message;
    }

    /**
     * 获取通道属性
     *
     * @return
     */
    public AbstractSmsChannelProperties getChannelProperties() {
        return this.channelProperties;
    }
}
