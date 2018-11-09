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
