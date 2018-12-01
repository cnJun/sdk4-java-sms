package com.sdk4.sms.util;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sh
 */
public class SmsUtilsTest {

    @Test
    public void testRenderTo() throws Exception {
        Map<String, String> data = Maps.newHashMap();
        data.put("code", "1234");
        data.put("name", "cnJun");

        Map<String, String> templateParam = Maps.newHashMap();
        templateParam.put("name", "nickname");

        Map<String, String> renderMap = SmsUtils.renderTo(data, templateParam);

        assertThat(renderMap).hasSize(2);
        assertThat(renderMap).containsKey("code");
        assertThat(renderMap).containsKey("nickname");
        assertThat(renderMap.get("code")).isEqualTo(data.get("code"));
        assertThat(renderMap.get("nickname")).isEqualTo(data.get("name"));
    }
}
