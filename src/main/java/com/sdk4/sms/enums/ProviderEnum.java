package com.sdk4.sms.enums;

import lombok.Getter;

/**
 * 短信提供商
 *
 * @author sh
 */
@Getter
public enum ProviderEnum {

    /**
     * 测试
     */
    test("测试"),

    /**
     * 阿里云
     */
    aliyun("阿里云"),

    ;

    private String text;

    ProviderEnum(String text) {
        this.text = text;
    }
}
