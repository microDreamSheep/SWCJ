package com.midream.sheep.swcj.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamsheep
 * 全局缓存管理
 */
public class CacheCorn {
    //存放缓存中对象
    private static final Map<String,Object> OBJECTS_MAP;
    static{
        OBJECTS_MAP = new HashMap<>();
    }
    public static Object getObject(String key){
        return OBJECTS_MAP.get(key);
    }
    public static void addObject(String ket,Object object){
        OBJECTS_MAP.put(ket,object);
    }
}
