package com.midream.sheep.swcj.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamsheep
 * 全局缓存管理
 */
public class CacheCorn {
    //缓存生成的爬虫对像
    private static final Map<String, Object> OBJECTS_MAP = new HashMap<>();
    //缓存xml注入的xml
    private static final Map<String, String> INJECTION_MAP = new HashMap<>();

    private static final Map<String, Object> CACHE_MAP = new HashMap<>();

    public static void put(String key, Object value) {
        CACHE_MAP.put(key, value);
    }

    public static Object get(String key) {
        return CACHE_MAP.get(key);
    }

    public static void putInjection(String key, String value) {
        INJECTION_MAP.put(key, value);
    }

    public static Map<String, String> getINJECTION_MAP() {
        return INJECTION_MAP;
    }

    public static Object getObject(String key) {
        return OBJECTS_MAP.get(key);
    }

    public static Object addObject(String ket, Object object) {
        OBJECTS_MAP.put(ket, object);
        return object;
    }
}
