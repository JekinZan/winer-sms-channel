package com.winer.sms.channel;

import com.autumn.exception.ExceptionUtils;
import com.autumn.util.StringUtils;
import com.autumn.validation.MatchesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

import java.util.*;

/**
 * 短信发送抽象
 *
 * @author Jekin 2018-12-07 10:37:51
 */
public abstract class AbstractSmsChannel<P extends AbstractSmsChannelProperties> implements SmsChannel, DisposableBean {

    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Class<P> channelPropertiesType;
    private SmsChannelContext channelContext;

    private P channelDefaultProperties;

    public AbstractSmsChannel(Class<P> channelPropertiesType, SmsChannelContext channelContext) {
        this.channelPropertiesType = channelPropertiesType;
        this.channelContext = channelContext;
        this.channelDefaultProperties = null;
    }

    public AbstractSmsChannel(Class<P> channelPropertiesType, SmsChannelContext channelContext, P channelDefaultProperties) {
        this.channelPropertiesType = channelPropertiesType;
        this.channelContext = channelContext;
        this.channelDefaultProperties = channelDefaultProperties;
    }

    @Override
    public final Class<P> getChannelPropertiesType() {
        return this.channelPropertiesType;
    }

    @Override
    public SmsChannelContext getChannelContext() {
        return this.channelContext;
    }

    @Override
    public void setChannelContext(SmsChannelContext channelContext) {
        this.channelContext = channelContext;
    }

    /**
     * 获取通道默认属性
     *
     * @return
     */
    public P getChannelDefaultProperties() {
        return this.channelDefaultProperties;
    }

    /**
     * 设置通道默认属性
     *
     * @param channelDefaultProperties 通道默认属性
     */
    public void setChannelDefaultProperties(P channelDefaultProperties) {
        this.channelDefaultProperties = channelDefaultProperties;
    }

    /**
     * 获取日志
     *
     * @return
     */
    public final Logger getLogger() {
        return this.logger;
    }

    /**
     * 手机号分隔
     *
     * @param phoneNumbers 手机号列表
     * @return
     */
    protected final String[] toPhoneNumberArray(String phoneNumbers) {
        return phoneNumbers.split(",");
    }

    /**
     * 转换为电话号码列表
     *
     * @param phoneNumbers 手机号列表
     * @return
     */
    protected final ArrayList<String> toPhoneNumberList(String phoneNumbers) {
        String[] phones = toPhoneNumberArray(phoneNumbers);
        ArrayList<String> numbers = new ArrayList<>();
        Collections.addAll(numbers, phones);
        return numbers;
    }

    /**
     * 手机号分页
     *
     * @param phoneNumbers 手机号码集合
     * @param nationCode   国家代码
     * @param pageSize     页大小
     * @return
     */
    protected final List<String> phoneNumberPages(String phoneNumbers, String nationCode, int pageSize) {
        if (nationCode == null) {
            nationCode = "";
        } else {
            nationCode = nationCode.trim();
        }
        List<String> phonePages = new ArrayList<>();
        String[] phones = this.toPhoneNumberArray(phoneNumbers);
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (String phone : phones) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(nationCode).append(phone);
            index++;
            if (index >= pageSize) {
                phonePages.add(sb.toString());
                index = 0;
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            phonePages.add(sb.toString());
        }
        return phonePages;
    }

    /**
     * @param phoneNumbers
     */
    private String checkPhoneNumbers(String phoneNumbers) {
        StringBuilder sb = new StringBuilder();
        String[] phones = this.toPhoneNumberArray(phoneNumbers);
        for (String phone : phones) {
            if (!StringUtils.isNullOrBlank(phone)) {
                if (!MatchesUtils.isMobilePhone(phone)) {
                    ExceptionUtils.throwValidationException("手机号码[" + phone + "]无效。");
                }
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(phone);
            }
        }
        return sb.toString();
    }

    /**
     * @param message
     */
    private void initMessage(SmsMessage message) {
        message.setPhoneNumbers(checkPhoneNumbers(message.getPhoneNumbers()));
        if (message.getCreate() == null) {
            message.setCreate(new Date());
        }
        if (message.getParams() == null) {
            message.setParams(new LinkedHashMap<>());
        }
    }

    /**
     *
     */
    @Override
    public final void send(SmsMessage message) throws Exception {
        this.send(message, this.getChannelDefaultProperties());
    }

    /**
     *
     */
    @Override
    public final void send(SmsMessage message, AbstractSmsChannelProperties channelProperties) throws Exception {
        ExceptionUtils.checkNotNull(message, "message");
        ExceptionUtils.checkNotNull(channelProperties, "channelProperties");
        if (!this.getChannelPropertiesType().isAssignableFrom(channelProperties.getClass())) {
            ExceptionUtils.throwValidationException("指定的通道属性类型[" + channelProperties.getClass().getName() + "]不是[" + this.getChannelPropertiesType().getName() + "]的类型或分配的类型。");
        }
        if (StringUtils.isNullOrBlank(message.getTemplateCode())) {
            ExceptionUtils.throwValidationException("模板代码不能为空。");
        }
        if (StringUtils.isNullOrBlank(message.getPhoneNumbers())) {
            ExceptionUtils.throwValidationException("至少需要一个以上手机号码。");
        }
        this.initMessage(message);
        String platform = String.format("[%s %s]", this.getChannelId(), this.getChannelName());
        SmsChannelContext context = this.getChannelContext();
        Date start = new Date();
        Date end;
        try {
            this.internalSend(message, (P) channelProperties);
            end = new Date();
            if (this.getLogger().isDebugEnabled()) {
                this.getLogger().debug(platform + "发送短信成功:" + message.toString());
            }
            if (context != null && context.getSmsChannelListens() != null) {
                for (SmsChannelListen smsChannelListen : context.getSmsChannelListens()) {
                    try {
                        smsChannelListen.sendSuccess(this, new ChannelListenMessage(start, end, message, channelProperties));
                    } catch (Exception listenError) {
                        this.getLogger().error(listenError.getMessage(), listenError);
                    }
                }
            }
        } catch (Exception e) {
            end = new Date();
            this.getLogger().error(platform + "发送短信出错:" + e.getMessage() + " " + message.toString(), e);
            if (context != null && context.getSmsChannelListens() != null) {
                for (SmsChannelListen smsChannelListen : context.getSmsChannelListens()) {
                    try {
                        smsChannelListen.sendError(this, new ChannelListenMessage(start, end, message, channelProperties), e);
                    } catch (Exception listenError) {
                        this.getLogger().error(listenError.getMessage(), listenError);
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 内部发送短信
     *
     * @param message           消息
     * @param channelProperties 通道属性
     * @throws Exception
     */
    protected abstract void internalSend(SmsMessage message, P channelProperties) throws Exception;

    @Override
    public void destroy() throws Exception {
        this.channelContext = null;
    }
}
