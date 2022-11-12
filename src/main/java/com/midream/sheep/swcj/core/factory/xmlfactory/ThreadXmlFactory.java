package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
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
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * 工厂类，读取配置文件，获取具体实现类
 */
public class ThreadXmlFactory extends SWCJAbstractFactory {
    private static final ExecutorService execute = new ThreadPoolExecutor(1,1,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    ,Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());

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
            this.swcjParseI = new BetterXmlParseTool();
        }
        execute.execute(() -> {
            try {
                parse(swcjParseI.parseXmlFile(xmlFile, config));
            } catch (ParserConfigurationException | IOException | SAXException e) {
                Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
            }
        });
        return this;
    }
    @Override
    public SWCJXmlFactory parse(String File) {
        if (this.swcjParseI == null) {
            this.swcjParseI = new BetterXmlParseTool();
        }
        execute.execute(() -> {
            try {
                parse(swcjParseI.parseStringXml(File, config));
            } catch (ParserConfigurationException | IOException | SAXException e) {
                Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
            }
        });
        return this;
    }
    private void parse(List<RootReptile> list){
        for (RootReptile reptile : list) {
            rootReptiles.put(reptile.getId(), reptile);
        }
        if(config.isCache()) {
            try {
                cache();
            } catch (ConfigException e) {
                throw new RuntimeException(e);
            }
        }
        for (RootReptile reptile : rootReptiles.values()) {
            if (swcjBuilder == null) {
                swcjBuilder = new ReptilesBuilder();
            }
            if (rootReptiles.get(reptile.getId()).isLoad()) {
                continue;
            }
            try {
                reptile.setLoad(true);
                swcjBuilder.Builder(new ReptlileMiddle(reptile, config));
                //删除
                rootReptiles.remove(reptile.getId());
            } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
                Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
            }
        }
    }

    @Override
    public SWCJXmlFactory invokeSpecialMethod(Object... args) {
        execute.shutdown();
        return this;
    }
}
