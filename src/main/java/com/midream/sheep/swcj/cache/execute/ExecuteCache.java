package com.midream.sheep.swcj.cache.execute;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行爬虫的缓存
 * @author midreamsheep
 * */
public class ExecuteCache {
    private final Map<String, Object> CACHE_MAP = new HashMap<>();
    public void put(String key, Object value) {
        CACHE_MAP.put(key, value);
    }

    public Object get(String key) {
        return CACHE_MAP.get(key);
    }
}
