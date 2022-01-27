package com.midream.sheep.swcj.util.xml;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.ReptileCoreJsoup;
import com.midream.sheep.swcj.data.swc.ReptilePaJsoup;
import com.midream.sheep.swcj.data.swc.ReptileUrl;
import com.midream.sheep.swcj.data.swc.RootReptile;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
/**
 * 工厂类，读取配置文件，获取具体实现类
 * */
public class XmlFactory {
    private ReptileConfig rc = new ReptileConfig();
    private Map<String,RootReptile> rootReptiles = new HashMap<>();;
    private static ReptilesBuilder rb;
    static {
        rb = new ReptilesBuilder();
    }
    //xml工厂提供的构造器
    public XmlFactory(String xmlFile) throws IOException, ParserConfigurationException, SAXException, ConfigException {
        parse(new File(xmlFile));
    }
    //xml工厂提供的构造器
    public XmlFactory(File xmlFile) throws IOException, ParserConfigurationException, SAXException, ConfigException {
        parse(xmlFile);
    }
    //解析文档
    private void parse(File xmlFile) throws ParserConfigurationException, IOException, SAXException, ConfigException {
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(xmlFile);
        NodeList root = d.getElementsByTagName("SWCL");
        parseXml(root.item(0));
    }
    //解析xml配置文件
    private void parseXml(Node root) throws ConfigException {
        NodeList nodes = root.getChildNodes();
        for(int i = 0;i< nodes.getLength();i++){
            Node item = nodes.item(i);
            switch (item.getNodeName()){
                case "config":
                    parseConfig(item.getChildNodes());
                    break;
                case "swc":
                    pareSWC(item);
                    break;
                default:
                    break;
            }
        }
    }
    //配置全局变量
    private void parseConfig(NodeList nl) throws ConfigException {
        for(int i = 0;i<nl.getLength();i++){
            Node child = nl.item(i);
            switch (child.getNodeName()){
                //设置超时时间
                case "timeout":
                    NamedNodeMap timeout = child.getAttributes();
                    try {
                        int time = Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim());
                    }catch (NumberFormatException e){
                        throw new ConfigException("类型转换异常，配置文件time中"+timeout.getNamedItem("value").getNodeValue().trim()+"不是数字");
                    }
                    rc.setTimeout(Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim()));
                    break;
                case "constructionSpace":
                    NamedNodeMap constructionSpace = child.getAttributes();
                    if(Boolean.parseBoolean(constructionSpace.getNamedItem("isAbsolute").getNodeValue().trim())){
                        rc.setWorkplace((XmlFactory.class.getClassLoader().getResource("").getPath()+constructionSpace
                        .getNamedItem("workSpace").getNodeValue().trim()).replace("file:/",""));
                    }else {
                        rc.setWorkplace(constructionSpace.getNamedItem("workSpace").getNodeValue().trim().replace("file:/",""));
                    }
                    break;
                case "userAgent":
                    NodeList childNodes = child.getChildNodes();
                    for(int a=0;a<childNodes.getLength();a++){
                        Node n = childNodes.item(a);
                        if(n.getNodeName().equals("value")){
                            rc.addUserAgent(n.getTextContent().trim());
                        }
                    }
                    break;
                case "createTactics":
                    NamedNodeMap attributes = child.getAttributes();
                    rc.setCache(Boolean.parseBoolean((attributes.getNamedItem("isCache").getNodeValue().equals("true")?"true":"false")));
                    break;
                default:
                    break;
            }
        }
    }
    //生成并配置爬虫实现类配置
    private void pareSWC(Node n) throws ConfigException {
        //实例化类
        RootReptile rr = new RootReptile();
        //分析根节点
        NamedNodeMap nodeMap = n.getAttributes();
        //配置信息
        rr.setId(nodeMap.getNamedItem("id").getNodeValue().trim());
        //获取根节点子节点
        NodeList nl = n.getChildNodes();
        //分析配置并配置
        for(int i=0;i<nl.getLength();i++){
            Node no = nl.item(i);
            switch (no.getNodeName()){
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
                    ru.setName(no.getAttributes().getNamedItem("name").getNodeValue());
                    ru.setInPutName(no.getAttributes().getNamedItem("inPutName").getNodeValue());
                    parseUrl(no.getChildNodes(),ru);
                    //绑定两类-----组合设计模式
                    rr.addUrl(ru);
                    break;
            }
        }
        this.rootReptiles.put(rr.getId(),rr);
    }
    private void parseUrl(NodeList nl,ReptileUrl ru) throws ConfigException {
        for(int i=0;i<nl.getLength();i++){
            Node n = nl.item(i);
            switch (n.getNodeName()){
                case "type":
                    ru.setRequestType(n.getAttributes().getNamedItem("type").getNodeValue().trim());
                    break;
                case "url":
                    ru.setUrl(n.getAttributes().getNamedItem("path").getNodeValue().trim());
                    break;
                case "parseProgram":
                    NodeList nodes = n.getChildNodes();
                    ru.setHtml(Boolean.parseBoolean(n.getAttributes().getNamedItem("isHtml").getNodeValue().trim()));
                    for(int a = 0;a<nodes.getLength();a++){
                        Node item = nodes.item(a);
                        if(item.getNodeName().equals("regular")){
                            ru.setReg(item.getAttributes().getNamedItem("reg").getNodeValue().trim());
                        }else if(item.getNodeName().equals("jsoup")){
                            //实例化jsoup核心
                            ReptileCoreJsoup rcj = new ReptileCoreJsoup();
                            if(item.getAttributes().getNamedItem("name")!=null&&!item.getAttributes().getNamedItem("name").getNodeValue().equals("")) {
                                rcj.setName(item.getAttributes().getNamedItem("name").getNodeValue());
                            }
                            //组合jsoup核心和ReptileUrl
                            ru.addJsoup(rcj);
                            NodeList childNodes = item.getChildNodes();
                            for(int c=0;c<childNodes.getLength();c++){
                                if(childNodes.item(c).getNodeName().equals("pa")){
                                    //实例化一个ReptilePaJsoup
                                    ReptilePaJsoup rp = new ReptilePaJsoup();
                                    Node node = childNodes.item(c);
                                    rp.setPaText(node.getTextContent().trim());
                                    if(node.getAttributes().getNamedItem("not")!=null){
                                        String not = node.getAttributes().getNamedItem("not").getNodeValue();
                                        String[] split = not.split(",");
                                        rp.setNot(split);
                                    }
                                    if(node.getAttributes().getNamedItem("step")!=null){
                                        String step = node.getAttributes().getNamedItem("step").getNodeValue().trim();
                                        try {
                                            int s = Integer.parseInt(step);
                                            rp.setStep(s);
                                        }catch (NumberFormatException e){
                                            throw new ConfigException("类型转换异常，pa"+node.getAttributes().getNamedItem("not").getNodeValue()+"不是数字");
                                        }
                                    }
                                    if(node.getAttributes().getNamedItem("element")!=null){
                                        rp.setElement(node.getAttributes().getNamedItem("element").getNodeValue());
                                    }
                                    rcj.addJsoup(rp);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
    public Object getWebSpider(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        RootReptile rootReptile = rootReptiles.get(id);
        if(rootReptile==null){
            throw new ConfigException("你的配置找不到 id为："+id);
        }
        return rb.Builder(rootReptile,rc);
    }

}
