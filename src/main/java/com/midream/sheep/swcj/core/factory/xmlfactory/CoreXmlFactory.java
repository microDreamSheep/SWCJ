package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.bydom.CoreParseTool;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 工厂类，读取配置文件，获取具体实现类
 */
public class CoreXmlFactory extends SWCJAbstractFactory {
    //xml工厂提供的构造器
    public CoreXmlFactory(String value) {
        parse(value);
    }

    //xml工厂提供的默认构造器
    public CoreXmlFactory() {}

    //xml工厂提供的构造器
    public CoreXmlFactory(File xmlFile) {
        parse(xmlFile);
    }

    //解析文档
    @Override
    public SWCJXmlFactory parse(File xmlFile) {
        notNull();
        try {
                parse(swcjParseI.parseXmlFile(xmlFile, rc));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public SWCJXmlFactory parse(String File) {
        notNull();
        try {
            parse(swcjParseI.parseStringXml(File, rc));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void parse(List<RootReptile> rootReptiles) {
        for (RootReptile reptile : rootReptiles) {
            this.rootReptiles.put(reptile.getId(), reptile);
        }
    }

    @Override
    public Object getWebSpiderById(String id) {
        Object o = BuildTool.getObjectFromTool(id);
        if(o!=null){
            return o;
        }
        try {
            return swcjBuilder.Builder(rootReptiles.get(id), rc);
        } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
    }
    private void notNull(){
        if (swcjBuilder == null) {
            swcjBuilder = new ReptilesBuilder();
        }
        if (this.swcjParseI == null) {
            this.swcjParseI = new CoreParseTool();
        }
    }
}
