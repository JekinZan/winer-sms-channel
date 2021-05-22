package com.winer.sms.channel.aliyun.model;

import com.aliyuncs.RpcAcsRequest;

/**
 * 发送短信请求
 * 
 * @author Jekin 2018-12-07 12:21:50
 */
public class SendSmsRequest extends RpcAcsRequest<SendSmsResponse> {
	public SendSmsRequest() {
		super("Dysmsapi", "2017-05-25", "SendSms");
	}

	private String templateCode;

	private String phoneNumbers;

	private String signName;

	private String resourceOwnerAccount;

	private String templateParam;

	private Long resourceOwnerId;

	private Long ownerId;

	private String smsUpExtendCode;

	private String outId;

	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
		if (templateCode != null) {
			putQueryParameter("TemplateCode", templateCode);
		}
	}

	public String getPhoneNumbers() {
		return this.phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
		if (phoneNumbers != null) {
			putQueryParameter("PhoneNumbers", phoneNumbers);
		}
	}

	public String getSignName() {
		return this.signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
		if (signName != null) {
			putQueryParameter("SignName", signName);
		}
	}

	public String getResourceOwnerAccount() {
		return this.resourceOwnerAccount;
	}

	public void setResourceOwnerAccount(String resourceOwnerAccount) {
		this.resourceOwnerAccount = resourceOwnerAccount;
		if (resourceOwnerAccount != null) {
			putQueryParameter("ResourceOwnerAccount", resourceOwnerAccount);
		}
	}

	public String getTemplateParam() {
		return this.templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
		if (templateParam != null) {
			putQueryParameter("TemplateParam", templateParam);
		}
	}

	public Long getResourceOwnerId() {
		return this.resourceOwnerId;
	}

	public void setResourceOwnerId(Long resourceOwnerId) {
		this.resourceOwnerId = resourceOwnerId;
		if (resourceOwnerId != null) {
			putQueryParameter("ResourceOwnerId", resourceOwnerId.toString());
		}
	}

	public Long getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		if (ownerId != null) {
			putQueryParameter("OwnerId", ownerId.toString());
		}
	}

	public String getSmsUpExtendCode() {
		return this.smsUpExtendCode;
	}

	public void setSmsUpExtendCode(String smsUpExtendCode) {
		this.smsUpExtendCode = smsUpExtendCode;
		if (smsUpExtendCode != null) {
			putQueryParameter("SmsUpExtendCode", smsUpExtendCode);
		}
	}

	public String getOutId() {
		return this.outId;
	}

	public void setOutId(String outId) {
		this.outId = outId;
		if (outId != null) {
			putQueryParameter("OutId", outId);
		}
	}

	@Override
	public Class<SendSmsResponse> getResponseClass() {
		return SendSmsResponse.class;
	}
}
