package com.midream.sheep.swcj.cache.object;

import java.util.HashMap;
import java.util.Map;

/**
 * 换存生成的爬虫对象
 *  @author midreamsheep
 * */
public class SpiderCache {
    private final Map<String, Object> CACHE_OBJECTS_MAP = new HashMap<>();

    public Object getCacheSpider(String key) {
        return CACHE_OBJECTS_MAP.get(key);
    }

    public Object addCacheSpider(String ket, Object object) {
        CACHE_OBJECTS_MAP.put(ket, object);
        return object;
    }

    public void removeCacheSpider(String key) {
        CACHE_OBJECTS_MAP.remove(key);
    }
}
