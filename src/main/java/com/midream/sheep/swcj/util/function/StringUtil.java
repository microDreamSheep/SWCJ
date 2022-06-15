package com.midream.sheep.swcj.util.function;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static Map<String,String> changeString(String values){
        if("".equals(values)){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        String[] split = values.split(";");
        for (String s : split) {
            String[] split1 = s.split("=");
            map.put(split1[0],split1[1]);
        }
        return map;
    }
    public static String change(String in,String names,String values){
        if(names.contains("#[")&&names.contains("]")){
            return in;
        }
        String[] ns = names.split(",");
        String[] vs = values.split(",");
        for (int i = 0;i<ns.length;i++) {
            in = in.replaceAll(ns[i],vs[i]);
        }
        return in;
    }
    public static String add(String in){
        return in.replaceAll("\"","\\\"");
    }

    public static String getString(List<String> args){
        StringBuilder sb = new StringBuilder();
        if(args.size()!=0) {
            sb.append(",");
        }
        for(int i = 0;i<args.size();i++){
            sb.append(args.get(i));
            if(i != args.size()-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
