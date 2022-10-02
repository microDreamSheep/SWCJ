package com.midream.sheep.swcj.data;

import java.util.HashMap;
import java.util.Map;

public class XmlSpecialStrings {
    public static final Map<String,String> map = new HashMap<>();
    static{
        //添加特殊字符替换规则 key:源字符 value:替换字符
        map.put("&amp;","&");
        map.put("&lt;","<");
        map.put("&gt;",">");
        map.put("&quot;","\"");
    }
}
