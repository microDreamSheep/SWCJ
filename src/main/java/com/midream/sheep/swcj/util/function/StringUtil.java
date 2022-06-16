package com.midream.sheep.swcj.util.function;

import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static Map<String,String> changeStringToMaps(String values){
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

    public static String add(String in){
        return in.replaceAll("\"","\\\"");
    }

    public static String getStringByList(List<String> args){
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
    public static String getExecuteCharacter(ReptileUrl ru, List<String> injection, ReptileConfig rc, RootReptile rr, SWCJMethod method){
        String Template2 = "#[in][swcj;]#[fx][swcj;]#[isHtml][swcj;]#[type][swcj;]#[url][swcj;]#[userage][swcj;]#[cookies][swcj;]#[values][swcj;]#[timeout][swcj;]#[class][swcj;]#[method]";
        StringBuilder inj = new StringBuilder();
        String in = ru.getParseProgram().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "");
        String templet = Template2.replace("#[method]", in)
                .replace("#[isHtml]", String.valueOf(ru.isHtml()))
                .replace("#[type]", ru.getRequestType())
                .replace("#[url]", ru.getUrl())
                .replace("#[userage]", rc.getUserAgents().get(new Random().nextInt(rc.getUserAgents().size())))
                .replace("#[cookies]", rr.getCookies())
                .replace("#[values]", ru.getValues())
                .replace("#[timeout]", rc.getTimeout() + "")
                .replace("#[class]", ru.getExecutClassName())
                .replace("#[fx]", method.getReturnType().replace("[]", ""));
        String[] split = templet.split("\\[swcj;]");
        for (int i = 0; i < split.length; i++) {
            for (int a = 0; a < injection.size(); a++) {
                if (split[i].contains("#{" + injection.get(a) + "}")) {
                    inj.append(a + 1).append(":").append(i + 1).append(":").append(injection.get(a)).append(",");
                }
            }
        }
        String injectCharacter = "";
        if (!("".contentEquals(inj))) {
            injectCharacter = inj.substring(0, inj.length() - 1);
        }
        return templet.replace("#[in]",injectCharacter);
    }
}
