package com.midream.sheep.swcj.core.parse;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.IParseTool;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileCoreJsoup;
import com.midream.sheep.swcj.pojo.swc.ReptilePaJsoup;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Objects;

/**
 * @author midreamsheep
 */
public class CoreParseTool implements IParseTool {
    @Override
    public RootReptile parseRoot(Node n) throws ConfigException {
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
                    ru.setName(attributes1.getNamedItem("name").getNodeValue());
                    ru.setInPutName(attributes1.getNamedItem("inPutName").getNodeValue());
                    parseUrl(no.getChildNodes(),ru);
                    //绑定两类-----组合设计模式
                    rr.addUrl(ru);
                    break;
            }
        }
        return rr;
    }

    @Override
    public void parseConfig(Node nodes, ReptileConfig rc) throws ConfigException {
        NodeList nl = nodes.getChildNodes();
        for(int i = 0;i<nl.getLength();i++) {
            Node child = nl.item(i);
            switch (child.getNodeName()) {
                //设置超时时间
                case "timeout":
                    NamedNodeMap timeout = child.getAttributes();
                    try {
                        Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim());
                    } catch (NumberFormatException e) {
                        throw new ConfigException("类型转换异常，配置文件time中" + timeout.getNamedItem("value").getNodeValue().trim() + "不是数字");
                    }
                    rc.setTimeout(Integer.parseInt(timeout.getNamedItem("value").getNodeValue().trim()));
                    break;
                case "constructionSpace":
                    NamedNodeMap constructionSpace = child.getAttributes();
                    if (!Boolean.parseBoolean(constructionSpace.getNamedItem("isAbsolute").getNodeValue().trim())) {
                        rc.setWorkplace((Objects.requireNonNull(CoreXmlFactory.class.getClassLoader().getResource("")).getPath() + constructionSpace
                                .getNamedItem("workSpace").getNodeValue().trim()).replace("file:/", ""));
                    } else {
                        rc.setWorkplace(constructionSpace.getNamedItem("workSpace").getNodeValue().trim().replace("file:/", ""));
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
                default:
                    break;
            }
        }
    }

    private void parseUrl(NodeList nl,ReptileUrl ru) throws ConfigException{
        for(int i=0;i<nl.getLength();i++){
            Node n = nl.item(i);
            switch (n.getNodeName()){
                case "type":
                    ru.setRequestType(n.getAttributes().getNamedItem("type").getNodeValue().trim());
                    break;
                case "path":
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
                                    if(node.getAttributes().getNamedItem("step")!=null&&!node.getAttributes().getNamedItem("step").getNodeValue().equals("")){
                                        String step = node.getAttributes().getNamedItem("step").getNodeValue().trim();
                                        try {
                                            int s = Integer.parseInt(step);
                                            rp.setStep(s);
                                        }catch (NumberFormatException e){
                                            throw new ConfigException("类型转换异常，step"+node.getAttributes().getNamedItem("step").getNodeValue()+"不是数字");
                                        }
                                    }
                                    if(node.getAttributes().getNamedItem("allStep")!=null&&!node.getAttributes().getNamedItem("allStep").getNodeValue().equals("")){
                                        String step = node.getAttributes().getNamedItem("allStep").getNodeValue().trim();
                                        try {
                                            int s = Integer.parseInt(step);
                                            rp.setAllStep(s+1);
                                        }catch (NumberFormatException e){
                                            throw new ConfigException("类型转换异常，allStep"+node.getAttributes().getNamedItem("allStep").getNodeValue()+"不是数字");
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
}
