package com.sdk4.sms;

import com.sdk4.sms.enums.ProviderEnum;

import java.util.Map;

/**
 * 短信通道接口
 *
 * @author sh
 */
public interface Provider {

    /**
     * 短信通道
     *
     * @return 短信通道
     */
    ProviderEnum provider();

    /**
     * 发送短信
     *
     * @param cfg 短信发送通道配置
     * @param mobile 手机号码
     * @param params 发送参数
     * @param ext 扩展参数
     * @return 短信发送结果
     */
    SmsResponse send(SmsHelper.Cfg cfg, String mobile, Map<String, String> params, String ext);

}
