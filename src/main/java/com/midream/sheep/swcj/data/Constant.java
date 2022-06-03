package com.midream.sheep.swcj.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamSheep
 * 字符常量池
 * */
public class Constant {
    //默认生成的包名
    public static final String DEFAULT_PACKAGE_NAME = "com";
    //String class name
    public static final String STRING_CLASS_NAME = "java.lang.String[]";
    private static final Map<String,String> CLASS_KEY_VALUE=new HashMap<>();
    //实现的执行策略名
    public static final Map<String,String> EXECUTE_CLASS_NAME = new HashMap<>();
    static {
        CLASS_KEY_VALUE.put("java.lang.String","String");
        CLASS_KEY_VALUE.put("[Ljava.lang.String;","String[]");

        //策略
        EXECUTE_CLASS_NAME.put("reg","com.midream.sheep.swcj.core.executetool.execute.regularexpression.SWCJregular");
    }
    private Constant(){}
    public static String getClassName(String key) {
        String s = CLASS_KEY_VALUE.get(key);
        if(s!=null){
            return s;
        }
        return key;
    }
    public static void addTactics(String key,String className){
        EXECUTE_CLASS_NAME.put(key,className);
    }
}
