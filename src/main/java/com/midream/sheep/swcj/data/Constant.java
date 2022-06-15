package com.midream.sheep.swcj.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamSheep
 * 字符常量池
 * */
public class Constant {
    //空字符
    public static final String nullString = "";
    //>字符替换
    public static final String ltTag = "&lt;";
    //<字符替换
    public static final String gtTag = "&gt;";
    //默认生成的包名
    public static final String DEFAULT_PACKAGE_NAME = "com";

    private static final Map<String,String> CLASS_KEY_VALUE=new HashMap<>();
    //实现的执行策略名
    private static final Map<String,String> EXECUTE_CLASS_NAME = new HashMap<>();

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

    public static String getExecute(String key){
        return EXECUTE_CLASS_NAME.get(key);
    }
    public static void putExecute(String key,String value){
        EXECUTE_CLASS_NAME.put(key,value);
    }
    public static void addTactics(String key,String className){
        EXECUTE_CLASS_NAME.put(key,className);
    }
}
