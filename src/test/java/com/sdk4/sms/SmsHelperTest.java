package com.sdk4.sms;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sh
 */
public class SmsHelperTest {

    {
        SmsHelper.loadConfig("sms.yml");
    }

    @Test
    public void testLoadConfig() throws Exception {
        SmsConfig smsConfig = new SmsConfig();
        SmsHelper.loadConfig(smsConfig);
        assertThat((SmsHelper.getConfig())).isEqualTo(smsConfig);
    }

    @Test
    public void testContainTemplate() throws Exception {
        assertThat(SmsHelper.containTemplate("test_send")).isTrue();
        assertThat(SmsHelper.containTemplate("test_send2")).isFalse();
    }

    @Test
    public void testSendSuccess() {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");
        SmsResponse sRes = SmsHelper.send("test_send", "13100000001", params);
        assertThat(sRes.success()).isEqualTo(true);
        assertThat(sRes.getCode()).isEqualTo(0);
        assertThat(sRes.getMessage()).isNotEmpty();
        assertThat(sRes.getMsgId()).isNotEmpty();
        assertThat(sRes.getCfg()).isNotNull();
        assertThat(sRes.getException()).isNull();
    }

    @Test
    public void testSendFail() {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");
        SmsResponse sRes = SmsHelper.send("prod_send", "13100000001", params);
        assertThat(sRes.success()).isEqualTo(false);
        assertThat(sRes.getCode()).isNotEqualTo(0);
    }

    @Test
    public void testSendFail_CfgNotExist() throws Exception {
        SmsConfig cfg = null;
        SmsHelper.loadConfig(cfg);

        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");

        SmsResponse sRes = SmsHelper.send("prod_send2", "13100000001", params);

        assertThat(sRes.success()).isFalse();
        assertThat(sRes.getMessage()).contains("未加载短信配置");
    }

    @Test
    public void testSendFail_ChannelIsEmpty() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");

        SmsResponse sRes = SmsHelper.send("ch_empty", "13100000001", params);

        assertThat(sRes.success()).isFalse();
        assertThat(sRes.getMessage()).contains("未指定短信通道");
    }

    @Test
    public void testSendFail_ChannelNotExist() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");

        SmsResponse sRes = SmsHelper.send("ch_notexist", "13100000001", params);

        assertThat(sRes.success()).isFalse();
        assertThat(sRes.getMessage()).contains("未配置短信通道");
    }

    @Test
    public void testSendFail_ProviderIsEmpty() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");

        SmsResponse sRes = SmsHelper.send("ch_provider_empty", "13100000001", params);

        assertThat(sRes.success()).isFalse();
        assertThat(sRes.getMessage()).contains("短信通道不能为空");
    }

    @Test
    public void testSendFail_ProviderParamsIsEmpty() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");

        SmsResponse sRes = SmsHelper.send("ch_provider_params_empty", "13100000001", params);

        assertThat(sRes.success()).isFalse();
        assertThat(sRes.getMessage()).contains("短信通道参数为空");
    }

    @Test
    public void testTemplateNotExist() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");
        SmsResponse sRes = SmsHelper.send("prod_send2", "13100000001", params);
        assertThat(sRes.success()).isFalse();
    }

    @Test
    public void testProviderNotExist() throws Exception {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");
        SmsResponse sRes = SmsHelper.send("prov_not_exist", "13100000001", params);
        assertThat(sRes.success()).isFalse();
    }

}
