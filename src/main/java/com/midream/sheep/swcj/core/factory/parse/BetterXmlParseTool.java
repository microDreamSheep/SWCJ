package com.midream.sheep.swcj.core.factory.parse;

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
    public List<RootReptile> parseStringXml(String xmlString, ReptileConfig rc){
        parseConfigFile(xmlString.substring(xmlString.indexOf("<config>") + 8, xmlString.indexOf("</config>")), rc);
        return parseClass(xmlString);
    }

    /**
     * 分析配置文件
     */
    private void parseConfigFile(String configString, ReptileConfig config) {
        //解析配置文件
        String[] strings = configString.split("};");
        //指针，指向元素
        int pointer = 0;
        //设置工作空间
        String[] workplaceConfig = strings[pointer].split("=");
        if (workplaceConfig[0].trim().equals("constructionSpace")) {
            String[] propertySet = workplaceConfig[1].substring(1).split(",");
            try {
                if (Boolean.parseBoolean(propertySet[0].trim())) {
                    config.setWorkplace(propertySet[1].trim());
                } else {
                    config.setWorkplace((Objects.requireNonNull(CoreXmlFactory.class.getClassLoader().getResource("")).getPath() + propertySet[1].trim()).replace("file:/", ""));
                }
            } catch (ConfigException e) {
                throw new RuntimeException(e);
            }
            pointer++;
        }
        //设置超时时间
        String[] timeoutConfig = strings[pointer].split("=");
        if (timeoutConfig[0].trim().equals("timeout")) {
            config.setTimeout(Integer.parseInt(timeoutConfig[1].substring(1).trim()));
            pointer++;
        }
        //设置userAgent
        String[] userAgentConfig = strings[pointer].split("=");
        if (userAgentConfig[0].trim().equals("userAgent")) {
            String[] split = userAgentConfig[1].substring(1).trim().split(",");
            for (String s : split) {
                config.addUserAgent(s.trim());
            }
            pointer++;
        }
        //设置注入配置
        String[] injectConfig = strings[pointer].split("=");
        if (injectConfig[0].trim().equals("executes")) {
            for (String s : injectConfig[1].substring(1).split(",")) {
                String[] split = s.split(":");
                if (split[0].trim().equals("execute")) {
                    String[] split1 = split[1].split("->");
                    Constant.putExecute(split1[0].substring(split1[0].indexOf("{")+1).trim(), split1[1].substring(0,split1[1].indexOf("}")-1).trim());
                } else if (split[0].trim().equals("executes")) {
                    try {
                        Constant.PutExecutesMap(((ExecuteConfigurationClass) Class.forName(split[1].trim().substring(1, split[1].trim().length() - 1)).getDeclaredConstructor().newInstance()).getExecuteConfiguration());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException | ClassNotFoundException e) {
                        try {
                            throw new ConfigException("你的配置文件有误，请检查配置文件");
                        } catch (ConfigException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }

            }
            pointer++;
        }
        //注入分析策略
        String[] methodChoose = strings[pointer].split("=");
        if (methodChoose[0].trim().equals("chooseStrategy")) {
            config.setChoice(ChooseStrategy.getChooseStrategy(methodChoose[1].substring(1).trim()));
        }
    }

    /**
     * 分析swcj
     * */
    private List<RootReptile> parseClass(String xmlString){
        List<RootReptile> rootReptiles = new LinkedList<>();
        String[] swcStrings = parseTag(xmlString,"<swc>","</swc>");
        for (String s : swcStrings) {
            RootReptile rootReptile = new RootReptile();
            rootReptile.setLoad(false);
            rootReptile.setId(s.substring(s.indexOf("<id>")+4,s.indexOf("</id>")));
            rootReptile.setCookies(s.substring(s.indexOf("<cookies>")+9,s.indexOf("</cookies>")));
            rootReptile.setParentInter(s.substring(s.indexOf("<parentInterface>")+"<parentInterface>".length(),s.indexOf("</parentInterface>")));
            String[] urls = parseTag(s, "<url>", "</url>");
            for (String url : urls) {
                ReptileUrl ru = new ReptileUrl();
                //分析urk标签的内容
                String[] properties = url.split("};");
                ru.setName(properties[0].split("=")[1].trim().substring(1));
                ru.setInPutName(properties[1].split("=")[1].trim().substring(1));
                ru.setRequestType(properties[2].split("=")[1].trim().substring(1));
                ru.setValues(properties[3].split("=")[1].trim().substring(1));
                ru.setUrl(properties[4].split("=")[1].trim().substring(1));
                String[] split = properties[5].split("-!>")[1].split(",");
                ru.setHtml(Boolean.parseBoolean(split[0].trim().substring(1)));
                ru.setParseProgram(split[2].trim().substring(split[2].trim().indexOf("->{")+3,split[2].trim().indexOf("}")-1));
                ru.setExecutClassName(Constant.getExecute(split[1].trim()));
                rootReptile.addUrl(ru);
            }
            rootReptiles.add(rootReptile);
        }
        return rootReptiles;
    }
    /**
     * 将字符串中的每个swc标签解析出来并返回数组
     * */
    private String[] parseTag(String xmlString, String startTag, String endTag){
        List<String> swcs = new LinkedList<>();
        int pointer = 0;
        while (xmlString.indexOf(startTag,pointer)!=-1){
            swcs.add(xmlString.trim().substring(xmlString.trim().indexOf(startTag,pointer)+5,xmlString.trim().indexOf(endTag,pointer)));
            pointer = xmlString.indexOf(endTag,pointer)+6;
        }
        return swcs.toArray(new String[0]);
    }
}
