package com.winer.sms.annotation;

import com.winer.sms.configure.SmsChannelAutoConfiguration;
import org.springframework.context.annotation.Import;


import java.lang.annotation.*;

/**
 * 启用短信
 * 
 * @author Jekin 2018-12-07 23:55:56
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ SmsChannelAutoConfiguration.class })
public @interface EnableAutumnSmsChannel {
    	
}
