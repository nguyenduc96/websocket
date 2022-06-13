package com.chainprotocol.gateway.core.config;

import com.alibaba.fastjson.JSON;
import kong.unirest.GenericType;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Log4j2
@Configuration
public class UnirestJSONConfig {

    @PostConstruct
    public void init() {
        Unirest.config().setObjectMapper(objectMapper);
        log.info("[GW Parent] Unirest JSON object mapping initialized");
    }

    @SuppressWarnings("unchecked")
    public static final ObjectMapper objectMapper = new ObjectMapper() {
        @Override
        public <T> T readValue(String value, Class<T> valueType) {
            if (valueType == String.class) {
                return (T) value;
            }
            return JSON.parseObject(value, valueType);
        }

        @Override
        public String writeValue(Object value) {
            if (value instanceof String) {
                return (String) value;
            }
            return JSON.toJSONString(value);
        }

        @Override
        public <T> T readValue(String value, GenericType<T> genericType) {
            return JSON.parseObject(value, genericType.getType());
        }
    };

}
