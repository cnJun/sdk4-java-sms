package com.sdk4.sms.provider;

import com.sdk4.sms.Provider;
import com.sdk4.sms.SmsHelper;
import com.sdk4.sms.SmsResponse;
import com.sdk4.sms.enums.ProviderEnum;
import com.sdk4.sms.util.SmsUtils;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 云片
 * 
 * https://www.yunpian.com/
 * 
 * @author sh
 */
@Slf4j
public class YunpianProvider implements Provider {

    enum ParamKey {
        /**
         * api 密钥
         */
        apikey
    }

	@Override
	public ProviderEnum provider() {
		return ProviderEnum.yunpian;
	}

	@Override
    public SmsResponse send(SmsHelper.Cfg cfg, String mobile, Map<String, String> params, String ext) {
		SmsResponse result = new SmsResponse();

		String phoneArea = "+86";

		try {
			YunpianClient clnt = new YunpianClient(cfg.getProviderParam(ParamKey.apikey.name())).init();
			String msg = "";
			if ("+86".equals(phoneArea)) {
				msg = cfg.getTemplate().getTemplateContent();
			} else {
				msg = cfg.getTemplate().getTemplateContent_en();
			}

            Map<String, String> paramsRender = SmsUtils.renderTo(params, cfg.getTemplate().getTemplateParam());
			for (Map.Entry<String, String> entry : paramsRender.entrySet()) {
				msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
			}

			// 发送短信API
			Map<String, String> param = clnt.newParam(2);
			param.put(YunpianClient.MOBILE, phoneArea + mobile);
			param.put(YunpianClient.TEXT, msg);
			Result<SmsSingleSend> r = clnt.sms().single_send(param);
			if (r.isSucc()) {
				result.setCode(0);
				result.setMessage(StringUtils.isEmpty(r.getMsg()) ? "发送成功" : msg);
			} else {
				StringBuilder err = new StringBuilder();

				if (r.getCode() != null) {
					err.append(r.getCode());
				}
				if (StringUtils.isNotEmpty(r.getMsg())) {
					if (err.length() > 0) {
						err.append(':');
					}
					err.append(r.getMsg());
				}
				if (StringUtils.isNotEmpty(r.getDetail())) {
					if (err.length() > 0) {
						err.append(':');
					}
					err.append(r.getDetail());
				}

				result.setCode(4);
				result.setMessage(err.length() == 0 ? "发送失败" : err.toString());
			}

			SmsSingleSend sss = r.getData();
			if (sss != null) {
				if (sss.getSid() != null) {
					result.setMsgId("" + sss.getSid());
				}
				if (sss.getCount() != null) {
					result.put("count", "" + sss.getCount());
				}
				if (sss.getSid() != null) {
					result.put("sid", "" + sss.getSid());
				}
				if (sss.getFee() != null) {
					result.put("fee", "" + sss.getFee());
				}
				if (sss.getUnit() != null) {
					result.put("unit", "" + sss.getUnit());
				}
			}
			clnt.close();
		} catch (Exception e) {
            result.setCode(5);
            result.setMessage("发送错误:" + e.getMessage());

            result.setException(e);

            log.error("发送短信异常:{}", mobile, e);
		}

		return result;
	}
}
