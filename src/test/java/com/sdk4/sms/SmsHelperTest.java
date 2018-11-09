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
    }

    @Test
    public void testSendFail() {
        Map<String, String> params = Maps.newHashMap();
        params.put("code", "1234");
        SmsResponse sRes = SmsHelper.send("prod_send", "13100000001", params);
        assertThat(sRes.success()).isEqualTo(false);
    }
}
