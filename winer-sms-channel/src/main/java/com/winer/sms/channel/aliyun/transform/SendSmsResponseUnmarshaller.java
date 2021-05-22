package com.winer.sms.channel.aliyun.transform;

import com.aliyuncs.transform.UnmarshallerContext;
import com.winer.sms.channel.aliyun.model.SendSmsResponse;

/**
 * 
 * 
 * @author Jekin 2018-12-07 12:20:36
 */
public class SendSmsResponseUnmarshaller {
	/**
	 * 
	 * @param sendSmsResponse
	 * @param context
	 * @return
	 */
	public static SendSmsResponse unmarshall(SendSmsResponse sendSmsResponse, UnmarshallerContext context) {
		sendSmsResponse.setRequestId(context.stringValue("SendSmsResponse.RequestId"));
		sendSmsResponse.setBizId(context.stringValue("SendSmsResponse.BizId"));
		sendSmsResponse.setCode(context.stringValue("SendSmsResponse.Code"));
		sendSmsResponse.setMessage(context.stringValue("SendSmsResponse.Message"));
		return sendSmsResponse;
	}
}
