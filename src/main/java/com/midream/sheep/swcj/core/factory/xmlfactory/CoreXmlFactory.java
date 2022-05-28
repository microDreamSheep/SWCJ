package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工厂类，读取配置文件，获取具体实现类
 * */
public class CoreXmlFactory implements SWCJXmlFactory {
    private final ReptileConfig rc = new ReptileConfig();
    private final Map<String,RootReptile> rootReptiles = new HashMap<>();
    private SWCJBuilder swcjBuilder = null;
    private static ReptilesBuilder rb;
    private static final ExecutorService execute = Executors.newFixedThreadPool(1);
    //xml工厂提供的构造器
    public CoreXmlFactory(String xmlFile) throws IOException, ParserConfigurationException, SAXException, ConfigException {
        parse(new File(xmlFile));
    }
    //xml工厂提供的构造器
    public CoreXmlFactory(){
    }
    //xml工厂提供的构造器
    public CoreXmlFactory(File xmlFile) throws IOException, ParserConfigurationException, SAXException, ConfigException {
        parse(xmlFile);
    }
    //解析文档
    @Override
    public void parse(File xmlFile) throws IOException, SAXException, ConfigException, ParserConfigurationException {
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(xmlFile);
        NodeList root = d.getElementsByTagName("SWCJ");
        if(root.getLength()!=0) {
            parseXml(root.item(0));
        }
    }
    //解析xml配置文件
    private void parseXml(Node root){
        NodeList nodes = root.getChildNodes();
        Thread thread = new Thread(() -> {
            List<RootReptile> list = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node item = nodes.item(i);
                switch (item.getNodeName()) {
                    case "config":
                        try {
                            CoreParseTool.parseConfig(item, rc);
                        } catch (ConfigException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "swc":
                        try {
                            RootReptile rootReptile = CoreParseTool.parseRoot(item);
                            rootReptile.setLoad(false);
                            this.rootReptiles.put(rootReptile.getId(), rootReptile);
                            list.add(rootReptile);
                        } catch (ConfigException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
            for (RootReptile reptile : list) {
                if (rb == null) {
                    rb = new ReptilesBuilder();
                }
                if (rootReptiles.get(reptile.getId()).isLoad()) {
                    continue;
                }
                try {
                    reptile.setLoad(true);
                    rb.Builder(reptile, rc);
                } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
                    e.printStackTrace();
                }
            }
        });
        execute.execute(thread);
    }
    @Override
    public Object getWebSpider(String id) throws ConfigException, EmptyMatchMethodException, InterfaceIllegal {
        //效验池中是否存在,如果存在直接返回
        Object object = null;
        int i = 0;
        while (true) {
            try {
                object = BuildTool.getObjectFromTool(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (object != null) {
                return object;
            }
            RootReptile rootReptile = rootReptiles.get(id);
            if(rootReptile==null){
                try {
                    Thread.sleep(100);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i==10){
                    throw new ConfigException("你的配置文件找不到id="+id);
                }
                continue;
            }
            if(!rootReptile.isLoad()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                rootReptiles.get(id).setLoad(true);
                return rb.Builder(rootReptiles.get(id),rc);
            }
        }
    }

    @Override
    public void parse(String File) {
        try {
            parse(new File(File));
        } catch (IOException | SAXException | ConfigException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCompiler(SWCJCompiler swcjCompiler) {
        if(swcjBuilder==null){
            swcjBuilder = new ReptilesBuilder();
        }
        this.swcjBuilder.setCompiler(swcjCompiler);
    }

    @Override
    public void setBuilder(SWCJBuilder swcjBuilder) {
        this.swcjBuilder = swcjBuilder;
    }

    /**
     *
     * @author midreamsheep
     */
    private static class CoreParseTool {
        public static RootReptile parseRoot(Node n) throws ConfigException {
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
        public static void parseConfig(Node nodes, ReptileConfig rc) throws ConfigException {
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
        private static void parseUrl(NodeList nl, ReptileUrl ru) throws ConfigException {
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
        private static String getProgram(Node node){
            Document document = node.getOwnerDocument();

            DOMImplementationLS domImplLS = (DOMImplementationLS) document.getImplementation();

            LSSerializer serializer = domImplLS.createLSSerializer();

            String str = serializer.writeToString(node);
            return str.replace("<?xml version=\"1.0\" encoding=\"UTF-16\"?>","");
        }
    }

}
