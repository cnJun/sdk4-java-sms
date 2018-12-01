package com.sdk4.sms;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sh
 */
public class SmsConfigTest {

    @Test
    public void testConfig() throws Exception {
        SmsHelper.loadConfig("sms.yml");

        SmsConfig smsConfig = SmsHelper.getConfig();

        assertThat(smsConfig).isNotNull();
        assertThat(smsConfig.getReadTimeout()).isGreaterThan(0);
        assertThat(smsConfig.getConnectTimeout()).isGreaterThan(0);
        assertThat(smsConfig.getTemplates().size()).isGreaterThan(0);
        assertThat(smsConfig.getChannels().size()).isGreaterThan(0);
        assertThat(smsConfig.containTemplate("test_send")).isTrue();
        assertThat(smsConfig.containTemplate("test_send2")).isFalse();

        SmsConfig.TemplateConfig templateConfig = smsConfig.getTemplates().get("test_send");
        assertThat(templateConfig).isNotNull();
        assertThat(templateConfig.getTemplateContent()).isNullOrEmpty();
        assertThat(templateConfig.getTemplateContent_en()).isNullOrEmpty();
        assertThat(templateConfig.getExtParam()).isNullOrEmpty();

    }
}