package com.midream.sheep.swcj.util.function;

import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.MethodMeta;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author midreamsheep
 */
public class StringUtil {
    private static final String Template2 = "#[in][swcj;]#[fx][swcj;]#[isHtml][swcj;]#[type][swcj;]#[url][swcj;]#[userage][swcj;]#[cookies][swcj;]#[values][swcj;]#[timeout][swcj;]#[class][swcj;]#[method]";


    /**
     * @author midreamsheep
     * @param value 首字母转大写字符串
     * @return 返回首字母大写字符串
     * */
    public static String StringToUpperCase(String value){
        return value==null||value.equals(Constant.nullString)? Constant.nullString :value.substring(0,1).toUpperCase()+value.substring(1);
    }

    /**
     * @author midreamsheep
     * */
    public static void add(StringBuilder sb,Object...vars){
        for (Object o : vars) {
            sb.append(o);
        }
    }
    /**
     * 将字符串转map
     * */
    public static Map<String,String> changeStringToMaps(String values){
        if("".equals(values)){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        for (String s : values.split(";")) {
            String[] split = s.split("=");
            map.put(split[0],split[1]);
        }
        return map;
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
    public static String getExecuteCharacter(ReptileUrl ru, List<String> injection, ReptileConfig rc, MethodMeta meta, SWCJMethod method){
        StringBuilder inj = new StringBuilder();
        String templet = Template2.replace("#[method]", ru.getParseProgram().replace("\\", "\\\\").replace("\"", "\\\""))
                .replace("#[isHtml]", String.valueOf(ru.isHtml()))
                .replace("#[type]", method.getRequestType())
                .replace("#[url]", ru.getUrl())
                .replace("#[userage]", rc.getUserAgents().get(new Random().nextInt(rc.getUserAgents().size())))
                .replace("#[cookies]", meta.getCookies())
                .replace("#[values]", ru.getValues())
                .replace("#[timeout]", rc.getTimeout() + "")
                .replace("#[class]", ru.getExecutClassName())
                .replace("#[fx]", method.getReturnType().replace("[]", ""))
                .replaceAll("\\R","");
        String[] split = templet.split("\\[swcj;]");
        for (int i = 0; i < split.length; i++) {
            for (int a = 0; a < injection.size(); a++) {
                if (split[i].contains("#{" + injection.get(a) + "}")) {
                    inj.append(a + 1).append(":").append(i + 1).append(":").append(injection.get(a)).append(",");
                }
            }
        }
        return templet.replace("#[in]",(Constant.nullString.contentEquals(inj))?Constant.nullString:inj.substring(0, inj.length() - 1));
    }
    /**
     * 读取文件
     * */
    public static String getStringByStream(File xmlFile) {
        try {
            return getStringByStream(Files.newInputStream(xmlFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getStringByStream(InputStream is) {
        try (InputStream inputStream = is) {
            //根据File获取xml文件文本
            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static String getStringByStream(BufferedReader gbk) {
        StringBuilder sb;
        try (BufferedReader input = gbk) {
            String line;
            sb = new StringBuilder();
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] getBytesByStream(InputStream is) {
        try (InputStream inputStream = is) {
            //根据File获取xml文件文本
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return baos.toByteArray();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
