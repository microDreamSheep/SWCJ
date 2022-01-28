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
    //存储内存中已有的地址
    private static final Map<String,String> FILE_PATH_MAP;
    static{
        OBJECTS_MAP = new HashMap<>();
        FILE_PATH_MAP = new HashMap<>();
    }
    public static Object getObject(String key){
        return OBJECTS_MAP.get(key);
    }
    public static void addObject(String ket,Object object){
        OBJECTS_MAP.put(ket,object);
    }
    public static String getPath(String key){
        return FILE_PATH_MAP.get(key);
    }
    public static void addPath(String ket,String path){
        FILE_PATH_MAP.put(ket,path);
    }
}
