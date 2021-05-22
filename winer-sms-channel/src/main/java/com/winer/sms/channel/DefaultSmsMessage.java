package com.winer.sms.channel;

import com.autumn.util.DateUtils;
import com.autumn.util.StringUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认短信消息
 *
 * @author Jekin 2018-12-07 10:19:52
 */
public class DefaultSmsMessage implements SmsMessage {

    /**
     *
     */
    private static final long serialVersionUID = -1132335276289287707L;
    private Date create;
    private String signName;
    private String templateCode;
    private String nationCode;
    private String phoneNumbers;
    private String messageContent;
    private Map<String, String> params;

    /**
     * 实例化
     */
    public DefaultSmsMessage() {
        this.setCreate(new Date());
        this.setParams(new LinkedHashMap<>());
    }

    @Override
    public Date getCreate() {
        return create;
    }

    @Override
    public void setCreate(Date create) {
        this.create = create;
    }

    @Override
    public String getSignName() {
        return signName;
    }

    @Override
    public void setSignName(String signName) {
        this.signName = signName;
    }

    @Override
    public String getNationCode() {
        return nationCode;
    }

    @Override
    public void setNationCode(String nationCode) {
        this.nationCode = nationCode;
    }

    @Override
    public String getTemplateCode() {
        return this.templateCode;
    }

    @Override
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Override
    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String getMessageContent() {
        return this.messageContent;
    }

    @Override
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("create -> ").append(DateUtils.dateFormat(this.getCreate(), "yyyy-MM-dd HH:mm:ss"));
        sb.append(" templateCode -> ").append(this.getTemplateCode());
        sb.append(" phoneNumbers -> ").append(this.getPhoneNumbers());
        if (StringUtils.isNotNullOrBlank(this.getMessageContent())) {
            sb.append(" messagecontent -> ").append(this.getMessageContent());
        }
        sb.append(" params -> ").append(this.getParams().toString());
        return sb.toString();
    }
}
