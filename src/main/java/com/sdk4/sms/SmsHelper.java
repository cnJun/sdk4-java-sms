package com.sdk4.sms;

import com.sdk4.sms.enums.ProviderEnum;
import com.sdk4.sms.provider.AliyunProvider;
import com.sdk4.sms.provider.TestProvider;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 短信发送工具类
 *
 * @author sh
 */
public class SmsHelper {
    private SmsHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static ConcurrentMap<ProviderEnum, Provider> api = new ConcurrentHashMap<>();
    private static SmsConfig smsConfig;

    static {
        addProvider(new AliyunProvider());
        addProvider(new TestProvider());
    }

    public static void addProvider(Provider provider) {
        api.put(provider.provider(), provider);
    }

    public static Provider getProvider(ProviderEnum provider) {
        return api.get(provider);
    }

    public static SmsConfig getConfig() {
        return SmsHelper.smsConfig;
    }

    public static void loadConfig(SmsConfig smsConfig) {
        SmsHelper.smsConfig = smsConfig;
    }

    public static boolean loadConfig(String yamlFilename) {
        boolean result = false;
        InputStream in = SmsHelper.class.getResourceAsStream("/" + yamlFilename);
        if (in != null) {
            Yaml yaml = new Yaml();
            SmsConfig smsConfig = yaml.loadAs(in, SmsConfig.class);
            if (smsConfig != null) {
                SmsHelper.smsConfig = smsConfig;
                result = true;
            }
        }
        return result;
    }

    public static boolean containTemplate(String templateId) {
        return smsConfig != null && smsConfig.containTemplate(templateId);
    }

    public static SmsResponse send(String templateId, String mobile, Map<String, String> params) {
        SmsResponse result = new SmsResponse();

        Cfg cfg = loadTemplateConfig(templateId);

        if (!cfg.success()) {
            return result.from(cfg);
        }

        ProviderEnum provider = cfg.provider.getProvider();

        Provider p = getProvider(provider);

        if (p == null) {
            result.setCode(4);
            result.setMessage("未找到" + provider.name() + "短信通道");
        } else {
            result = p.send(cfg, mobile, params, "");
        }

        result.setCfg(cfg);

        return result;
    }

    private static SmsHelper.Cfg loadTemplateConfig(String templateId) {
        SmsHelper.Cfg cfg = new SmsHelper.Cfg();

        if (smsConfig == null) {
            cfg.code = 4;
            cfg.message = "未加载短信配置";
        } else {
            cfg.templateId = templateId;
            cfg.template = smsConfig.getTemplates().get(templateId);
            if (cfg.template == null) {
                cfg.code = 4;
                cfg.message = "模板id不存在";
            } else if (StringUtils.isEmpty(cfg.template.getChannel())) {
                cfg.code = 4;
                cfg.message = "未指定短信通道";
            } else if (!smsConfig.getChannels().containsKey(cfg.template.getChannel())) {
                cfg.code = 4;
                cfg.message = "未配置短信通道";
            } else {
                cfg.provider = smsConfig.getChannels().get(cfg.template.getChannel());
                if (cfg.provider.getProvider() == null) {
                    cfg.code = 4;
                    cfg.message = "短信通道不能为空";
                } else if (cfg.provider.getProvider() != ProviderEnum.test
                        && (cfg.provider.getParams() == null || cfg.provider.getParams().size() == 0)) {
                    cfg.code = 4;
                    cfg.message = "短信通道参数为空";
                } else {
                    cfg.code = 0;
                    cfg.message = "OK";
                }
            }
        }

        return cfg;
    }

    @Getter
    public static class Cfg {
        private int code;
        private String message;

        private String templateId;
        private SmsConfig.TemplateConfig template;
        private SmsConfig.ProviderConfig provider;

        public boolean success() {
            return code == 0;
        }

        public String getProviderParam(String key) {
            String val = provider.getParams().get(key);
            return val == null ? "" : val.trim();
        }
    }
}
