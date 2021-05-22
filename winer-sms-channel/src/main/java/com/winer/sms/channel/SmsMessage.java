package com.winer.sms.channel;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 短信消息
 *
 * @author Jekin 2018-12-07 10:29:23
 */
public interface SmsMessage extends Serializable {

    /**
     * 获取创建时间
     *
     * @return
     */
    Date getCreate();

    /**
     * 设置创建时间
     *
     * @param create
     */
    void setCreate(Date create);

    /**
     * 获取模板代码
     *
     * @return
     */
    String getTemplateCode();

    /**
     * 设置模板代码
     *
     * @param templateCode
     */
    void setTemplateCode(String templateCode);

    /**
     * 获取签名
     *
     * @return
     */
    String getSignName();

    /**
     * 设置签名
     *
     * @param signName 签名
     */
    void setSignName(String signName);

    /**
     * 获取国家代码
     *
     * @return
     */
    String getNationCode();

    /**
     * 设置国家代码
     *
     * @param nationCode 国家代码
     */
    void setNationCode(String nationCode);

    /**
     * 获取手机号码
     *
     * @return 多个号码使用英文逗号
     */
    String getPhoneNumbers();

    /**
     * 设置手机号码
     *
     * @param phoneNumbers 手机号码,多个号码使用英文逗号
     */
    void setPhoneNumbers(String phoneNumbers);

    /**
     * 获取消息内容
     *
     * @return
     */
    String getMessageContent();

    /**
     * 设置消息内容
     *
     * @param messageContent
     */
    void setMessageContent(String messageContent);

    /**
     * 获取发送参数
     *
     * @return
     */
    Map<String, String> getParams();

    /**
     * 设置发送参数
     *
     * @param params 参数集合
     */
    void setParams(Map<String, String> params);
}
