package com.midream.sheep.swcj.cache.configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigXmlInjectionCache {
    private final Map<String, String> INJECTION_MAP = new HashMap<>();

    public void putInjection(String key, String value) {
        INJECTION_MAP.put(key, value);
    }

    public Map<String, String> getINJECTION_MAP() {
        return INJECTION_MAP;
    }
}
