package com.chainprotocol.gateway.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FastJsonDefaultInitConfig {

    @PostConstruct
    public void postConstant() {
        // Don't skip transient fields
        JSON.DEFAULT_GENERATE_FEATURE &= ~SerializerFeature.SkipTransientField.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
//        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.BrowserCompatible, true);
    }

}
