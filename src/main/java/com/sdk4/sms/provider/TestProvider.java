package com.sdk4.sms.provider;

import com.sdk4.sms.Provider;
import com.sdk4.sms.SmsHelper;
import com.sdk4.sms.SmsResponse;
import com.sdk4.sms.enums.ProviderEnum;

import java.util.Map;
import java.util.UUID;

/**
 * 测试模拟发送通道
 *
 * @author sh
 */
public class TestProvider implements Provider {

    @Override
    public ProviderEnum provider() {
        return ProviderEnum.test;
    }

    @Override
    public SmsResponse send(SmsHelper.Cfg cfg, String mobile, Map<String, String> params, String ext) {
        SmsResponse result = new SmsResponse();

        result.setCode(0);
        result.setMessage("发送成功");
        result.setMsgId(UUID.randomUUID().toString());

        return result;
    }


}
