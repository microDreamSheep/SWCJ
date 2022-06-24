package com.midream.sheep.swcj.core.factory.parse.bystr;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.APIClassInter.ExecuteConfigurationClass;
import com.midream.sheep.swcj.core.factory.SWCJParseI;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BetterXmlParseTool implements SWCJParseI {
    @Override
    public List<RootReptile> parseXmlFile(File xmlFile, ReptileConfig rc) throws ParserConfigurationException {
        //根据File获取xml文件文本
        StringBuilder sb = new StringBuilder();
        try (InputStream is = Files.newInputStream(xmlFile.toPath())) {
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseStringXml(sb.toString(), rc);
    }

    @Override
    public List<RootReptile> parseStringXml(String xmlString, ReptileConfig rc) {
        parseConfigFile(xmlString.substring(xmlString.indexOf("<config>") + 8, xmlString.indexOf("</config>")), rc);
        return parseAllClass(xmlString);
    }

    /**
     * 分析配置文件
     */
    private void parseConfigFile(String configString, ReptileConfig config) {
        //设置工作空间
        try {
            parseWorkPlace(configString.substring(configString.indexOf("<constructionSpace>") + "<constructionSpace>".length(), configString.indexOf("</constructionSpace>")), config);
        } catch (Exception ignored) {
        }
        //设置超时时间
        try {
            parseTimeOut(configString.substring(configString.indexOf("<timeout>") + "<timeout>".length(), configString.indexOf("</timeout>")), config);
        } catch (Exception ignored) {
        }
        try {
            //设置userAgent
            parseUserAgent(configString.substring(configString.indexOf("<userAgent>") + "<userAgent>".length(), configString.indexOf("</userAgent>")), config);
        } catch (Exception ignored) {
        }
        try {
            //设置注入配置
            parseExecutes(configString.substring(configString.indexOf("<executes>") + "<executes>".length(), configString.indexOf("</executes>")));
        } catch (Exception ignored) {
        }
        //注入分析策略
        try {
            parseChooseStrategy(configString.substring(configString.indexOf("<chooseStrategy>") + "<chooseStrategy>".length(), configString.indexOf("</chooseStrategy>")), config);
        }catch (Exception ignored) {
        }
    }

    private void parseChooseStrategy(String substring, ReptileConfig config) {
        config.setChoice(ChooseStrategy.getChooseStrategy(substring));
    }

    private void parseUserAgent(String substring, ReptileConfig config) {
        String[] strings = parseTag(substring, "<value>", "</value>");
        for (String s : strings) {
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
            if (Boolean.parseBoolean(workConfig.substring(workConfig.indexOf("<isAbsolute>") + "<isAbsolute>".length(), workConfig.indexOf("</isAbsolute>")).trim())) {
                config.setWorkplace(workConfig.substring(workConfig.indexOf("<workSpace>") + "<workSpace>".length(), workConfig.indexOf("</workSpace>")).trim());
            } else {
                config.setWorkplace((Objects.requireNonNull(CoreXmlFactory.class.getClassLoader().getResource("")).getPath() + workConfig.substring(workConfig.indexOf("<workSpace>") + "<workSpace>".length(), workConfig.indexOf("</workSpace>")).trim().trim()).replace("file:/", ""));
            }
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注入配置
     */
    private void parseExecutes(String executes) {
        String[] strings = parseTag(executes, "<execute>", "</execute>");
        for (String s : strings) {
            if (s.contains("<executeConfig>")) {
                String classes = s.substring(s.indexOf("<executeConfig>") + "<executeConfig>".length(), s.indexOf("</executeConfig>"));
                try {
                    Constant.PutExecutesMap(((ExecuteConfigurationClass) Class.forName(classes).getDeclaredConstructor().newInstance()).getExecuteConfiguration());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (s.contains("<key>")) {
                Constant.putExecute(s.substring(s.indexOf("<key>") + "<key>".length(), s.indexOf("</key>")).trim(), s.substring(s.indexOf("<value>") + "<value>".length(), s.indexOf("</value>")).trim());
            }
        }
    }

    /**
     * 分析swcj
     */
    private List<RootReptile> parseAllClass(String xmlString) {
        List<RootReptile> rootReptiles = new LinkedList<>();
        String[] swcStrings = parseTag(xmlString, "<swc>", "</swc>");
        for (String s : swcStrings) {
            RootReptile rootReptile = new RootReptile();
            parseClass(s, rootReptile);
            rootReptiles.add(rootReptile);
        }
        return rootReptiles;
    }

    private void parseClass(String s, RootReptile rootReptile) {
        rootReptile.setLoad(false);
        rootReptile.setId(s.substring(s.indexOf("<id>") + "<id>".length(), s.indexOf("</id>")).trim());
        rootReptile.setCookies(s.substring(s.indexOf("<cookies>") + "<cookies>".length(), s.indexOf("</cookies>")).trim());
        rootReptile.setParentInter(s.substring(s.indexOf("<parentInterface>") + "<parentInterface>".length(), s.indexOf("</parentInterface>")).trim());
        rootReptile.setRu(parseRu(parseTag(s, "<url>", "</url>")));
    }

    private List<ReptileUrl> parseRu(String[] RuStrings) {
        List<ReptileUrl> reptileUrls = new LinkedList<>();
        for (String ru : RuStrings) {
            ReptileUrl reptileUrl = new ReptileUrl();
            reptileUrl.setName(ru.substring(ru.indexOf("<name>") + "<name>".length(), ru.indexOf("</name>")).trim());
            reptileUrl.setInPutName(ru.substring(ru.indexOf("<inPutName>") + "<inPutName>".length(), ru.indexOf("</inPutName>")).trim());
            reptileUrl.setValues(ru.substring(ru.indexOf("<value>") + "<value>".length(), ru.indexOf("</value>")).trim());
            reptileUrl.setRequestType(ru.substring(ru.indexOf("<type>") + "<type>".length(), ru.indexOf("</type>")).trim());
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
