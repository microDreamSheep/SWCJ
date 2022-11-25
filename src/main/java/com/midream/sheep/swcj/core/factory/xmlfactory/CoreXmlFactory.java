package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * 工厂类，读取配置文件，获取具体实现类
 */
public class CoreXmlFactory extends SWCJAbstractXmlFactory {

    public CoreXmlFactory(){}

    public CoreXmlFactory(boolean isLoadCache,String workplace) throws ConfigException {
        if (isLoadCache) {
            cache(workplace);
        }
    }

    //解析文档
    @Override
    public SWCJAbstractXmlFactory parse(File xmlFile) {
        notNull();
        try {
            parse(swcjParseI.parseXmlFile(xmlFile, config));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
        } catch (ClassNotFoundException | InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public SWCJAbstractXmlFactory parse(String File) {
        notNull();
        try {
            parse(swcjParseI.parseStringXml(File, config));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
        } catch (ClassNotFoundException | InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    private void parse(List<SWCJClass> swcjClasses) {
        for (SWCJClass swcjClass : swcjClasses) {
            this.swcjClasses.put(swcjClass.getId(), swcjClass);
        }
    }

    @Override
    public Object getWebSpiderById(String id) {
        Object o = BuildTool.getObjectFromTool(id);
        try {
            return o!=null?o:build(id);
        } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
    }
    private Object build(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        Object builder = swcjBuilder.Builder(new ReptlileMiddle(swcjClasses.get(id), config));
        swcjClasses.remove(id);
        return builder;
    }
    private void notNull(){
        if (swcjBuilder == null) {
            swcjBuilder = new ReptilesBuilder();
        }
        if (this.swcjParseI == null) {
            this.swcjParseI = new BetterXmlParseTool();
        }
    }
}
