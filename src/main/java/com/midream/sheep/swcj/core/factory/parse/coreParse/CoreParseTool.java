package com.midream.sheep.swcj.core.factory.parse.coreParse;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.factory.xmlfactory.CoreXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.IParseTool;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

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
        for (int i = 0; i < nl.getLength(); i++) {
            Node no = nl.item(i);
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

    @Override
    public void parseConfig(Node nodes, ReptileConfig rc) throws ConfigException {
        NodeList nl = nodes.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
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

    private void parseUrl(NodeList nl, ReptileUrl ru) throws ConfigException {
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
                    ru.setExecutClassName(Constant.EXECUTE_CLASS_NAME.get(n.getAttributes().getNamedItem("type").getNodeValue().trim()));
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
