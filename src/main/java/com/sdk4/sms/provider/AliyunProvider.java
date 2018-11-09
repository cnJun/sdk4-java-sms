package com.sdk4.sms.provider;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.sdk4.sms.Provider;
import com.sdk4.sms.SmsHelper;
import com.sdk4.sms.SmsResponse;
import com.sdk4.sms.enums.ProviderEnum;
import com.sdk4.sms.util.SmsUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 阿里云 - 云通信 - 短信服务
 *
 * https://dysms.console.aliyun.com/dysms.htm
 *
 * @author sh
 */
public class AliyunProvider implements Provider {
    /** 产品名称:云通信短信API产品,开发者无需替换 */
    static final String PRODUCT = "Dysmsapi";
    /** 产品域名,开发者无需替换 */
    static final String DOMAIN = "dysmsapi.aliyuncs.com";
    /** 暂不支持region化 */
    static final String REGION_ID = "cn-hangzhou";

    enum ParamKey {
        /**
         * id
         */
        accessKeyId,

        /**
         * 密钥
         */
        accessKeySecret
    }

    @Override
    public ProviderEnum provider() {
        return ProviderEnum.aliyun;
    }

    @Override
    public SmsResponse send(SmsHelper.Cfg cfg, String mobile, Map<String, String> params, String ext) {
        SmsResponse result = new SmsResponse();

        IAcsClient acsClient = null;

        try {
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID,
                    cfg.getProviderParam(ParamKey.accessKeyId.name()),
                    cfg.getProviderParam(ParamKey.accessKeySecret.name()));
            DefaultProfile.addEndpoint(REGION_ID, PRODUCT, DOMAIN);
            acsClient = new DefaultAcsClient(profile);
        } catch (Exception e) {
            result.setCode(5);
            result.setMessage("初始化短信通道失败:" + e.getMessage());

            result.setException(e);
        }

        if (result.success() && acsClient != null) {
            SendSmsRequest request = new SendSmsRequest();

            // 短信接收号码,支持以逗号分隔的形式进行批量调用,批量上限为1000个手机号码
            String phoneNumbers = mobile;
            request.setPhoneNumbers(phoneNumbers);
            request.setSignName(cfg.getTemplate().getSignName());
            request.setTemplateCode(cfg.getTemplate().getTemplateCode());

            Map<String, String> params_ = SmsUtils.renderTo(params, cfg.getTemplate().getTemplateParam());

            String paramString = new Gson().toJson(params_);

            request.setTemplateParam(paramString);

            // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            if (StringUtils.isNotEmpty(ext)) {
                request.setOutId(ext);
            }

            request.setMethod(MethodType.POST);

            try {
                SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
                String code = sendSmsResponse.getCode();
                String msg = sendSmsResponse.getMessage();
                String reqId = sendSmsResponse.getRequestId();
                String bizId = sendSmsResponse.getBizId();
                if ("OK".equals(sendSmsResponse.getCode())) {
                    result.setCode(0);
                    result.setMessage(StringUtils.isEmpty(msg) ? "发送成功" : msg);
                } else {
                    result.setCode(4);
                    result.setMessage(StringUtils.isEmpty(msg) ? "发送失败" : (StringUtils.isEmpty(code) ? "" : code + ":") + msg);
                }
                if (StringUtils.isNotEmpty(reqId)) {
                    result.put("requestId", reqId);
                }
                if (StringUtils.isNotEmpty(bizId)) {
                    result.setMsgId(bizId);
                }
            } catch (Exception e) {
                result.setCode(5);
                result.setMessage("发送错误:" + e.getMessage());

                result.setException(e);
            }
        }

        return result;
    }
}
