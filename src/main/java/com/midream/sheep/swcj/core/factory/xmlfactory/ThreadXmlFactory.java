package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.SWCJXmlFactory;
import com.midream.sheep.swcj.core.factory.parse.bydom.CoreParseTool;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工厂类，读取配置文件，获取具体实现类
 */
public class ThreadXmlFactory extends SWCJAbstractFactory {
    private static final ExecutorService execute = Executors.newFixedThreadPool(1);

    //xml工厂提供的构造器
    public ThreadXmlFactory(String value) {
        parse(value);
    }
    //xml工厂提供的默认构造器
    public ThreadXmlFactory() {
    }
    //xml工厂提供的构造器
    public ThreadXmlFactory(File xmlFile) {
        parse(xmlFile);
    }
    //解析文档
    @Override
    public SWCJXmlFactory parse(File xmlFile) {
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
        return this;
    }
    @Override
    public SWCJXmlFactory parse(String File) {
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
        return this;
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

    @Override
    public SWCJXmlFactory invokeSpecialMethod(Object... args) {
        execute.shutdown();
        return this;
    }
}
