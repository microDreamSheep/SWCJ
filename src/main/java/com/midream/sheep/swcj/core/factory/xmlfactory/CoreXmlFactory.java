package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.build.builds.ReptilesBuilder;
import com.midream.sheep.swcj.build.function.BuildTool;
import com.midream.sheep.swcj.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.IParseTool;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.parse.CoreParseTool;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.util.compiler.SWCJCompiler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工厂类，读取配置文件，获取具体实现类
 * */
public class CoreXmlFactory implements SWCJXmlFactory {
    private final ReptileConfig rc = new ReptileConfig();
    private final Map<String,RootReptile> rootReptiles = new HashMap<>();
    private SWCJBuilder swcjBuilder = null;
    private static ReptilesBuilder rb;
    private static IParseTool parseTool;
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
        if(parseTool==null){
            parseTool = new CoreParseTool();
        }
        NodeList nodes = root.getChildNodes();
        new Thread(()-> {
            List<RootReptile> list = new ArrayList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node item = nodes.item(i);
                switch (item.getNodeName()) {
                    case "config":
                        try {
                            parseTool.parseConfig(item, rc);
                        } catch (ConfigException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "swc":
                        try {
                            RootReptile rootReptile = parseTool.parseRoot(item);
                            rootReptile.setLoad(false);
                            this.rootReptiles.put(rootReptile.getId(),rootReptile);
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
                if(rb==null){
                    rb = new ReptilesBuilder();
                }
                if(rootReptiles.get(reptile.getId()).isLoad()){
                    continue;
                }
                try {
                    reptile.setLoad(true);
                    rb.Builder(reptile,rc);
                } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public Object getWebSpider(String id) throws ConfigException, EmptyMatchMethodException, InterfaceIllegal, ClassNotFoundException {
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
            if(rootReptile.isLoad()){
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
    public void addResource(String File) {
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

    @Override
    public void setParseTool(IParseTool iParseTool) {
        parseTool = iParseTool;
    }
}
