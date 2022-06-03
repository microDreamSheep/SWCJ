package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.parse.CoreParseTool;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工厂类，读取配置文件，获取具体实现类
 */
public class CoreXmlFactory extends SWCJAbstractFactory {

    private static final ExecutorService execute = Executors.newFixedThreadPool(1);

    //xml工厂提供的构造器
    public CoreXmlFactory(String value) {
        parse(value);
    }

    //xml工厂提供的默认构造器
    public CoreXmlFactory() {
    }

    //xml工厂提供的构造器
    public CoreXmlFactory(File xmlFile) {
        parse(xmlFile);
    }

    //解析文档
    @Override
    public void parse(File xmlFile) {
        if (this.swcjParseI == null) {
            this.swcjParseI = new CoreParseTool();
        }
        Thread thread = new Thread(() -> {
            try {
                parse(swcjParseI.parseXmlFile(xmlFile, rc));
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        });
        execute.execute(thread);
    }

    @Override
    public void parse(String File) {
        if (this.swcjParseI == null) {
            this.swcjParseI = new CoreParseTool();
        }
        Thread thread = new Thread(() -> {
            try {
                parse(swcjParseI.parseStringXml(File, rc));
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        });
        execute.execute(thread);
    }

    private void parse(List<RootReptile> list){
        for (RootReptile reptile : list) {
            rootReptiles.put(reptile.getId(), reptile);
        }
        for (RootReptile reptile : list) {
            if (swcjBuilder == null) {
                swcjBuilder = new ReptilesBuilder();
            }
            if (rootReptiles.get(reptile.getId()).isLoad()) {
                continue;
            }
            try {
                reptile.setLoad(true);
                swcjBuilder.Builder(reptile, rc);
            } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
                e.printStackTrace();
            }
        }
    }
}
