package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
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
            parse(swcjParseI.parseXmlFile(xmlFile, config));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
        }
        return this;
    }

    @Override
    public SWCJXmlFactory parse(String File) {
        notNull();
        try {
            parse(swcjParseI.parseStringXml(File, config));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
        }
        return this;
    }

    private void parse(List<RootReptile> rootReptiles) {
        for (RootReptile rootReptile : rootReptiles) {
            this.rootReptiles.put(rootReptile.getId(), rootReptile);
        }
        if(config.isCache()){
            //缓存
            try {
                cache();
            } catch (ConfigException e) {
                throw new RuntimeException(e);
            }
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
        Object builder = swcjBuilder.Builder(new ReptlileMiddle(rootReptiles.get(id), config));
        rootReptiles.remove(id);
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
