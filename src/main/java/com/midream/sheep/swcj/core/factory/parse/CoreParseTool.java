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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CoreParseTool implements SWCJParseI {
    @Override
    public List<RootReptile> parseXmlFile(File xmlFile, ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException {
        long start = System.currentTimeMillis();
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        //3.解析文件
        NodeList rootNode = builder.parse(xmlFile).getElementsByTagName("SWCJ");
        long end = System.currentTimeMillis();
        System.out.println();
        if(rootNode.getLength()!=0) {
            return parseXml(rootNode.item(0),rc);
        }
        return null;
    }

    @Override
    public List<RootReptile> parseStringXml(String file, ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        NodeList swcj = factory.newDocumentBuilder().parse(new InputSource(new StringReader(file))).getElementsByTagName("SWCJ");
        if(swcj.getLength()!=0) {
            return parseXml(swcj.item(0), rc);
        }
        return null;
    }

    private List<RootReptile> parseXml(Node rootNode,ReptileConfig rc) {
        List<RootReptile> returnValues = new LinkedList<>();
        NodeList childNodes = rootNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            switch (item.getNodeName()) {
                //解析config节点
                case "config":
                    parseConfig(item, rc);
                    break;
                    //解析swc节点
                case "swc":
                        RootReptile rootReptile = parseRoot(item);
                        rootReptile.setLoad(false);
                        returnValues.add(rootReptile);
                    break;
                default:
                    break;
            }
        }
        return returnValues;
    }

    public RootReptile parseRoot(Node n) {
            //实例化类
            RootReptile rr = new RootReptile();
            //分析根节点
            NamedNodeMap nodeMap = n.getAttributes();
            //配置信息
            rr.setId(nodeMap.getNamedItem("id").getNodeValue().trim());
            //获取根节点子节点
            NodeList nl = n.getChildNodes();
            //分析配置并配置
            for (int i = 0; i < nl.getLength(); i++) {
                Node no = nl.item(i);
                if (no.getNodeName().equals("#comment")||no.getNodeName().equals("#text")){
                    continue;
                }
                switch (no.getNodeName()) {
                    case "cookies":
                        rr.setCookies(no.getTextContent().trim());
                        break;
                    case "parentInterface":
                        NamedNodeMap attributes = no.getAttributes();
                        rr.setParentInter(attributes.getNamedItem("class").getNodeValue().trim());
                        break;
                    case "url":
                        ReptileUrl ru = new ReptileUrl();
                        NamedNodeMap attributes1 = no.getAttributes();
                        ru.setName(attributes1.getNamedItem("name").getNodeValue());
                        ru.setInPutName(attributes1.getNamedItem("inPutName").getNodeValue());
                        parseUrl(no.getChildNodes(), ru);
                        //绑定两类-----组合设计模式
                        rr.addUrl(ru);
                        break;
                }
            }
            return rr;
        }
    public void parseConfig(Node node, ReptileConfig rc) {
            NodeList configs = node.getChildNodes();
            for (int i = 0; i < configs.getLength(); i++) {
                Node child = configs.item(i);
                if (child.getNodeName().equals("#comment")||child.getNodeName().equals("#text")){
                    continue;
                }
                switch (child.getNodeName()) {
                    //设置超时时间
                    case "timeout":
                        NamedNodeMap timeout = child.getAttributes();
                        try {
                            Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim());
                        } catch (NumberFormatException e) {
                            try {
                                throw new ConfigException("类型转换异常，配置文件time中" + timeout.getNamedItem("value").getNodeValue().trim() + "不是数字");
                            } catch (ConfigException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        rc.setTimeout(Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim()));
                        break;
                    case "constructionSpace":
                        NamedNodeMap constructionSpace = child.getAttributes();
                        if (Boolean.parseBoolean(constructionSpace.getNamedItem("isAbsolute").getNodeValue().trim())) {
                            try {
                                rc.setWorkplace(constructionSpace.getNamedItem("workSpace").getNodeValue().trim().replace("file:/", ""));
                            } catch (ConfigException e) {
                                throw new RuntimeException(e);
                            }
                            continue;
                        }
                        try {
                            rc.setWorkplace((Objects.requireNonNull(CoreXmlFactory.class.getClassLoader().getResource("")).getPath() + constructionSpace
                                    .getNamedItem("workSpace").getNodeValue().trim()).replace("file:/", ""));
                        } catch (ConfigException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "userAgent":
                        NodeList childNodes = child.getChildNodes();
                        for (int a = 0; a < childNodes.getLength(); a++) {
                            Node n = childNodes.item(a);
                            if (n.getNodeName().equals("value")) {
                                rc.addUserAgent(n.getTextContent().trim());
                            }
                        }
                        break;
                    case "createTactics":
                        NamedNodeMap attributes = child.getAttributes();
                        rc.setCache(Boolean.parseBoolean((attributes.getNamedItem("isCache").getNodeValue().equals("true") ? "true" : "false")));
                        break;
                    case "executes":
                        NodeList childs = child.getChildNodes();
                        for(int a = 0;a<childs.getLength();a++){
                            Node item = childs.item(a);
                            if(item.getNodeName().equals("executes")){
                                try {
                                    Class<?> aClass = Class.forName(item.getTextContent().trim());
                                    ExecuteConfigurationClass o = (ExecuteConfigurationClass)aClass.getDeclaredConstructor().newInstance();
                                    Constant.PutExecutesMap(o.getExecuteConfiguration());
                                } catch (ClassNotFoundException | NoSuchMethodException e) {
                                    try {
                                        throw new ConfigException("找不到类"+item.getTextContent().trim());
                                    } catch (ConfigException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                                    try {
                                        throw new ConfigException("配置类类实例化异常");
                                    } catch (ConfigException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                            if(!item.getNodeName().equals("execute")){
                                continue;
                            }
                            String key = "";
                            String value = "";
                            NodeList nodes1 = item.getChildNodes();
                            for(int b = 0;b<nodes1.getLength();b++){
                                Node it = nodes1.item(b);
                                if(it.getNodeName().equals("key")){
                                    key = it.getTextContent().trim();
                                    continue;
                                }
                                if(it.getNodeName().equals("value")){
                                    value = it.getTextContent().trim();
                                }
                            }
                            Constant.putExecute(key,value);
                        }
                        break;
                    case "chooseStrategy":
                        ChooseStrategy type = ChooseStrategy.getChooseStrategy(child.getAttributes().getNamedItem("type").getTextContent().trim());
                        rc.setChoice(type);
                        break;
                    default:
                        break;
                }
            }
        }

    private void parseUrl(NodeList nl, ReptileUrl ru) {
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                switch (n.getNodeName()) {
                    case "type":
                        ru.setRequestType(n.getAttributes().getNamedItem("type").getNodeValue().trim());
                        break;
                    case "path":
                        ru.setUrl(n.getAttributes().getNamedItem("path").getNodeValue().trim());
                        break;
                    case "value":
                        ru.setValues(n.getTextContent().trim());
                        break;
                    case "parseProgram":
                        ru.setHtml(Boolean.parseBoolean(n.getAttributes().getNamedItem("isHtml").getNodeValue().trim()));
                        ru.setExecutClassName(Constant.getExecute(n.getAttributes().getNamedItem("type").getNodeValue().trim()));
                        ru.setParseProgram(getProgram(n.getChildNodes().item(1)));
                        break;
                }
            }
        }

        private String getProgram(Node node){
            Document document = node.getOwnerDocument();

            DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();

            LSSerializer serializer = domImplLS.createLSSerializer();

            String str = serializer.writeToString(node);
            return str.replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>","");
        }
}