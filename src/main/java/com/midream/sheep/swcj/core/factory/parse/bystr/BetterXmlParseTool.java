package com.midream.sheep.swcj.core.factory.parse.bystr;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.APIClassInter.ExecuteConfigurationClass;
import com.midream.sheep.swcj.core.factory.SWCJParseI;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

public class BetterXmlParseTool implements SWCJParseI {
    @Override
    public List<RootReptile> parseXmlFile(File xmlFile, ReptileConfig rc) {
        return parseStringXml(StringUtil.getStringByStream(xmlFile), rc);
    }

    @Override
    public List<RootReptile> parseStringXml(String xmlString, ReptileConfig rc) {
        //正则删除注释
        xmlString = xmlString.replaceAll("<!--[\\s\\S]+?-->","");
        if(xmlString.contains("<config>")) {
            parseConfigFile(xmlString.substring(xmlString.indexOf("<config>") + 8, xmlString.indexOf("</config>")), rc);
        }
        return xmlString.contains("<swc>")?parseAllClass(xmlString):new LinkedList<>();
    }
    /**
     * 分析配置文件
     */
    private void parseConfigFile(String configString, ReptileConfig config) {
        //设置工作空间
        if(configString.contains("<constructionSpace>")){
            parseWorkPlace(configString.substring(configString.indexOf("<constructionSpace>") + "<constructionSpace>".length(), configString.indexOf("</constructionSpace>")), config);
        }
        //设置超时时间
        if(configString.contains("<timeout>")){
            parseTimeOut(configString.substring(configString.indexOf("<timeout>") + "<timeout>".length(), configString.indexOf("</timeout>")), config);
        }
        //设置userAgent
        if(configString.contains("<userAgent>")){
            parseUserAgent(configString.substring(configString.indexOf("<userAgent>") + "<userAgent>".length(), configString.indexOf("</userAgent>")), config);
        }
        //设置注入配置
        if(configString.contains("<executes>")){
            parseExecutes(configString.substring(configString.indexOf("<executes>") + "<executes>".length(), configString.indexOf("</executes>")));
        }
        //注入分析策略
        if(configString.contains("<chooseStrategy>")){
            parseChooseStrategy(configString.substring(configString.indexOf("<chooseStrategy>") + "<chooseStrategy>".length(), configString.indexOf("</chooseStrategy>")), config);
        }
        //注入重用策略
        if(configString.contains("<injections>")){
            parseInjections(configString.substring(configString.indexOf("<injections>") + "<injections>".length(), configString.indexOf("</injections>")));
        }
        config.setCache(configString.contains("<cache/>")||configString.contains("<cache></cache>"));
    }

    private void parseInjections(String substring) {
        for (String s : parseTag(substring, "<injection>", "</injection>")) {
            CacheCorn.INJECTION_CACHE.putInjection(s.substring(s.indexOf("<key>") + "<key>".length(), s.indexOf("</key>")).trim(), s.substring(s.indexOf("<value>") + "<value>".length(), s.indexOf("</value>")).trim());
        }
    }

    private void parseChooseStrategy(String substring, ReptileConfig config) {
        config.setChoice(ChooseStrategy.getChooseStrategy(substring));
    }

    private void parseUserAgent(String substring, ReptileConfig config) {
        for (String s : parseTag(substring, "<value>", "</value>")) {
            config.addUserAgent(s.trim());
        }
    }

    /**
     * 分析超时时间
     */
    private void parseTimeOut(String substring, ReptileConfig config) {
        config.setTimeout(Integer.parseInt(substring));
    }

    /*
     * 分析工作空间
     **/
    private void parseWorkPlace(String workConfig, ReptileConfig config) {
        try {
            String trim = workConfig.substring(workConfig.indexOf("<workSpace>") + "<workSpace>".length(), workConfig.indexOf("</workSpace>")).trim();
            if (Boolean.parseBoolean(workConfig.substring(workConfig.indexOf("<isAbsolute>") + "<isAbsolute>".length(), workConfig.indexOf("</isAbsolute>")).trim())) {
                config.setWorkplace(trim);
            } else {
                config.setWorkplace((Objects.requireNonNull(CoreXmlFactory.class.getClassLoader().getResource("")).getPath() + trim.trim()).replace("file:/", ""));
            }
        } catch (ConfigException e) {
            Logger.getLogger(BetterXmlParseTool.class.getName()).warning(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 注入配置
     */
    private void parseExecutes(String executes) {
        for (String s : parseTag(executes, "<execute>", "</execute>")) {
            putExecute(s);
        }
    }
    private void putExecute(String s){
        if (s.contains("<executeConfig>")) {
            String classes = s.substring(s.indexOf("<executeConfig>") + "<executeConfig>".length(), s.indexOf("</executeConfig>"));
            try {
                Constant.PutExecutesMap(((ExecuteConfigurationClass) Class.forName(classes).getDeclaredConstructor().newInstance()).getExecuteConfiguration());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                Logger.getLogger(BetterXmlParseTool.class.getName()).warning(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        if (s.contains("<key>")) {
            Constant.putExecute(s.substring(s.indexOf("<key>") + "<key>".length(), s.indexOf("</key>")).trim(), s.substring(s.indexOf("<value>") + "<value>".length(), s.indexOf("</value>")).trim());
        }
    }
    /**
     * 分析swcj
     */
    private List<RootReptile> parseAllClass(String xmlString) {
        for (Map.Entry<String, String> entry : CacheCorn.INJECTION_CACHE.getINJECTION_MAP().entrySet()) {
            xmlString = xmlString.replace(entry.getKey(), entry.getValue());
        }
        List<RootReptile> rootReptiles = new LinkedList<>();
        for (String s : parseTag(xmlString, "<swc>", "</swc>")) {
                RootReptile rootReptile = new RootReptile();
                parseClass(s, rootReptile);
                rootReptiles.add(rootReptile);
        }
        return rootReptiles;
    }

    private void parseClass(String s, RootReptile rootReptile) {
        rootReptile.setLoad(false);
        rootReptile.setId(s.substring(s.indexOf("<id>") + "<id>".length(), s.indexOf("</id>")).trim());
        if(s.contains("<cookies>")) {
            rootReptile.setCookies(s.substring(s.indexOf("<cookies>") + "<cookies>".length(), s.indexOf("</cookies>")).trim());
        }
        rootReptile.setParentInter(s.substring(s.indexOf("<parentInterface>") + "<parentInterface>".length(), s.indexOf("</parentInterface>")).trim());
        rootReptile.setRu(parseRu(parseTag(s, "<url>", "</url>")));
    }

    private List<ReptileUrl> parseRu(String[] RuStrings) {
        List<ReptileUrl> reptileUrls = new LinkedList<>();
        for (String ru : RuStrings) {
            ReptileUrl reptileUrl = new ReptileUrl();
            reptileUrl.setName(ru.substring(ru.indexOf("<name>") + "<name>".length(), ru.indexOf("</name>")).trim());
            if(ru.contains("<value>")) {
                reptileUrl.setValues(ru.substring(ru.indexOf("<value>") + "<value>".length(), ru.indexOf("</value>")).trim());
            }
            reptileUrl.setUrl(ru.substring(ru.indexOf("<path>") + "<path>".length(), ru.indexOf("</path>")).trim());
            String parseProgram = ru.substring(ru.indexOf("<parseProgram>") + "<parseProgram>".length(), ru.indexOf("</parseProgram>")).trim();
            reptileUrl.setExecutClassName(Constant.getExecute(parseProgram.substring(parseProgram.indexOf("<type>") + "<type>".length(), parseProgram.indexOf("</type>")).trim()));
            reptileUrl.setHtml(Boolean.parseBoolean(parseProgram.substring(parseProgram.indexOf("<isHtml>") + "<isHtml>".length(), parseProgram.indexOf("</isHtml>")).trim()));
            reptileUrl.setParseProgram(parseProgram.substring(parseProgram.indexOf("<xml>") + "<xml>".length(), parseProgram.indexOf("</xml>")).trim());
            reptileUrls.add(reptileUrl);
        }
        return reptileUrls;
    }

    /**
     * 将字符串中的每个swc标签解析出来并返回数组
     */
    private String[] parseTag(String xmlString, String startTag, String endTag) {
        List<String> swcs = new LinkedList<>();
        int pointer = 0;
        while (xmlString.indexOf(startTag, pointer) != -1) {
            swcs.add(xmlString.trim().substring(xmlString.trim().indexOf(startTag, pointer) + 5, xmlString.trim().indexOf(endTag, pointer)));
            pointer = xmlString.indexOf(endTag, pointer) + 6;
        }
        return swcs.toArray(new String[0]);
    }

}
