package com.midream.sheep.swcj.core.build.function;

/**
 * @author midreamsheep
 */
public class StringUtil {
    public static String StringToUpperCase(String value){
        return value.substring(0,1).toUpperCase()+value.substring(1);
    }
    public static void add(StringBuilder sb,Object...vars){
        for (Object o : vars) {
            sb.append(o);
        }
    }
}
