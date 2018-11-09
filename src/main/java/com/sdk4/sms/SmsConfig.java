package com.sdk4.sms;

import com.sdk4.sms.enums.ProviderEnum;
import lombok.Data;

import java.util.Map;

/**
 * 短信配置
 *
 * @author sh
 */
@Data
public class SmsConfig {
    /**
     * 连接超时
     */
    private int connectTimeout;

    /**
     * 读取超时
     */
    private int readTimeout;

    /**
     * 模板配置
     */
    private Map<String, TemplateConfig> templates;

    /**
     * 短信通道配置
     */
    private Map<String, ProviderConfig> channels;

    public boolean containTemplate(String tplId) {
        return this.templates != null && this.templates.containsKey(tplId);
    }

    @Data
    public static class TemplateConfig {
        /**
         * 短信通道
         */
        private String channel;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 模板代码
         */
        private String templateCode;

        /**
         * 模板内容
         */
        private String templateContent;

        /**
         * 模板内容(英文)
         */
        private String templateContent_en;

        /**
         * 模板短信参数对应关系
         */
        private Map<String, String> templateParam;

        /**
         * 短信通道扩展参数
         */
        private Map<String, String> extParam;
    }

    @Data
    public static class ProviderConfig {
        /**
         * 短信通道
         */
        private ProviderEnum provider;

        /**
         * 短信通道参数
         */
        private Map<String, String> params;
    }
}
