package com.winer.sms.channel.cloopen;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 发送短信请求
 * <p>
 * </p>
 *
 * @description TODO
 * @author: Jekin
 * @create: 2020-10-30 00:40
 **/
@Getter
@Setter
public class CloopenSendSmsRequest implements Serializable {

    private static final long serialVersionUID = 3849504989559496114L;

    /**
     * 短信接收端手机号码集合，用英文逗号分开，每批发送的手机号数量不得超过200个
     */
    @JSONField(ordinal = 1)
    private String to;

    /**
     * 应用id
     */
    @JSONField(ordinal = 2)
    private String appId;

    /**
     * 短信模板id
     */
    @JSONField(ordinal = 3)
    private String templateId;

    /**
     * 子扩展码 支持20位数字
     */
    @JSONField(ordinal = 4)
    private String subAppend;

    /**
     * 第三方自定义消息id，最大支持32位，同账号下24H内不允许重复。重复返回错误码160054=请求重复
     */
    @JSONField(ordinal = 5)
    private String reqId;

    /**
     * 数据集合
     */
    @JSONField(ordinal = 6)
    private String[] datas;

}
