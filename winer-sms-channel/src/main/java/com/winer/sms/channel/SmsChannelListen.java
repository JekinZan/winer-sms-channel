package com.winer.sms.channel;

/**
 * 短信通道监听
 * 
 * @author Jekin 2018-12-07 10:53:59
 */
public interface SmsChannelListen {

	/**
	 * 发送成功
	 * 
	 * @param channel       通道
	 * @param listenMessage 监听消息
	 */
	void sendSuccess(SmsChannel channel, ChannelListenMessage listenMessage);

	/**
	 * 发送错误
	 * 
	 * @param channel       通道
	 * @param listenMessage 监听消息
	 * @param e             异常
	 */
	void sendError(SmsChannel channel, ChannelListenMessage listenMessage, Throwable e);
}
