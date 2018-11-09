package com.sdk4.sms.util;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sh
 */
public class SmsUtils {
    private SmsUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, String> renderTo(Map<String, String> data, Map<String, String> templateParam) {
        Map<String, String> res = new LinkedHashMap<>();

        if (templateParam == null) {
            templateParam = new LinkedHashMap<>(0);
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (templateParam.containsKey(key) && StringUtils.isNotEmpty(templateParam.get(key))) {
                res.put(templateParam.get(key), val);
            } else {
                res.put(key, val);
            }
        }

        return res;
    }
}
