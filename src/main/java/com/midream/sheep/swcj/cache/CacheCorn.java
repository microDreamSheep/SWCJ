package com.midream.sheep.swcj.cache;

import com.midream.sheep.swcj.cache.configuration.ConfigXmlInjectionCache;
import com.midream.sheep.swcj.cache.object.SpiderCache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamsheep
 * 全局缓存管理
 */
public class CacheCorn {
    public static final SpiderCache SPIDER_CACHE = new SpiderCache();
    //缓存xml注入的xml
    public static final ConfigXmlInjectionCache INJECTION_CACHE = new ConfigXmlInjectionCache();

}
