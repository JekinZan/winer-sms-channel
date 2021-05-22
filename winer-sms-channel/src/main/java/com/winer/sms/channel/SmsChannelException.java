package com.winer.sms.channel;

import com.autumn.exception.AutumnException;

/**
 * 短信通道异常
 * 
 * @author Jekin 2018-12-07 12:43:25
 */
public class SmsChannelException extends AutumnException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 954486753822622609L;

	/**
	 * 实例化
	 * 
	 */
	public SmsChannelException() {
		super("短信通道异常");
	}

	/**
	 * 实例化
	 * 
	 * @param message 消息
	 */
	public SmsChannelException(String message) {
		super(message);
	}

	/**
	 * 实例化
	 * 
	 * @param message 消息
	 * @param cause   源异常
	 */
	public SmsChannelException(String message, Throwable cause) {
		super(message, cause);
	}
}
