package com.sdk4.sms;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送结果
 *
 * @author sh
 */
@Data
public class SmsResponse {
    /**
     * 返回代码
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 短信通道返回扩展内容
     */
    private Map<String, String> extra = new HashMap<>();

    /**
     * 下发短信使用的配置
     */
    private SmsHelper.Cfg cfg;

    /**
     * 异常信息
     */
    private Exception exception;

    public boolean success() {
        return code == 0;
    }

    public SmsResponse put(String key, String value) {
        this.extra.put(key, value);
        return this;
    }

    public SmsResponse from(SmsHelper.Cfg cfg) {
        this.code = cfg.getCode();
        this.message = cfg.getMessage();

        return this;
    }
}
