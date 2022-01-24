package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.ReptilePaJsoup;
import com.midream.sheep.SWCJ.data.swc.ReptileUrl;
import com.midream.sheep.SWCJ.data.swc.RootReptile;
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
import java.util.HashMap;
import java.util.Map;

public class XmlFactory {
    private ReptileConfig rc = new ReptileConfig();
    private Map<String,RootReptile> rootReptiles = new HashMap<>();;
    private static ReptilesBuilder rb;
    static {
        rb = new ReptilesBuilder();
    }
    //xml工厂提供的构造器
    public XmlFactory(String xmlFile) throws IOException, ParserConfigurationException, SAXException {
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(xmlFile);
        NodeList root = d.getElementsByTagName("SWCL");
        parseXml(root.item(0));
    }
    //xml工厂提供的构造器
    public XmlFactory(File xmlFile) throws IOException, ParserConfigurationException, SAXException {
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(xmlFile);
        NodeList root = d.getElementsByTagName("SWCL");
        parseXml(root.item(0));
    }
    //解析xml配置文件
    private void parseXml(Node root){
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
    private void parseConfig(NodeList nl){
        for(int i = 0;i<nl.getLength();i++){
            Node child = nl.item(i);
            switch (child.getNodeName()){
                //设置超时时间
                case "timeout":
                    NamedNodeMap timeout = child.getAttributes();
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
    private void pareSWC(Node n){
        //实例化类
        RootReptile rr = new RootReptile();
        ReptileUrl ru = new ReptileUrl();
        //绑定两类-----组合设计模式
        rr.setRu(ru);
        //分析根节点
        NamedNodeMap nodeMap = n.getAttributes();
        //配置信息
        rr.setId(nodeMap.getNamedItem("id").getNodeValue().trim());
        rr.setInPutType(nodeMap.getNamedItem("inPutType").getNodeValue().trim());
        rr.setInPutName(nodeMap.getNamedItem("inPutName").getNodeValue().trim());
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
                case "returnType":
                    rr.setReturnType(no.getAttributes().getNamedItem("type").getNodeValue().trim());
                    break;
                case "url":
                    parseUrl(no.getChildNodes(),ru);
                    break;
            }
        }
        this.rootReptiles.put(rr.getId(),rr);
    }
    private void parseUrl(NodeList nl,ReptileUrl ru){
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
                            NodeList childNodes = item.getChildNodes();
                            for(int c=0;c<childNodes.getLength();c++){
                                if(childNodes.item(c).getNodeName().equals("pa")){
                                    //实例化一个ReptilePaJsoup
                                    ReptilePaJsoup rp = new ReptilePaJsoup();
                                    Node node = childNodes.item(c);
                                    rp.setPaText(node.getTextContent().trim());
                                    ru.addJsoup(rp);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
    public Object getWebSpider(String id) throws EmptyMatchMethodException {
        rc.setWorkplace(rc.getWorkplace().replace("file:/","").replace("file:\\",""));
        return rb.reptilesBuilder(rootReptiles.get(id),rc);
    }

}
