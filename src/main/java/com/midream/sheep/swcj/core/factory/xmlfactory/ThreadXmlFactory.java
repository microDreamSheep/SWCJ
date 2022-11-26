package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.factory.SWCJFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
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
public class ThreadXmlFactory extends SWCJXmlFactory {
    private static final ExecutorService execute = new ThreadPoolExecutor(1,1,
            5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    ,Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    public ThreadXmlFactory(){}
    public ThreadXmlFactory(boolean isLoadCache,String workplace) throws ConfigException {
        if (isLoadCache) {
            cache(workplace);
        }
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
            } catch (ClassNotFoundException | InterfaceIllegal e) {
                throw new RuntimeException(e);
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
            } catch (ClassNotFoundException | InterfaceIllegal e) {
                throw new RuntimeException(e);
            }
        });
        return this;
    }
    private void parse(List<SWCJClass> list){
        for (SWCJClass swcjClass : list) {
            swcjClasses.put(swcjClass.getId(), swcjClass);
        }
        for (SWCJClass reptile : swcjClasses.values()) {
            if (swcjBuilder == null) {
                swcjBuilder = new ReptilesBuilder();
            }
            if (swcjClasses.get(reptile.getId()).isLoad()) {
                continue;
            }
            try {
                reptile.setLoad(true);
                swcjBuilder.Builder(new ReptlileMiddle(reptile, config));
                //删除
                swcjClasses.remove(reptile.getId());
            } catch (EmptyMatchMethodException | ConfigException | InterfaceIllegal e) {
                Logger.getLogger(CoreXmlFactory.class.getName()).severe(e.getMessage());
            }
        }
    }

    @Override
    public SWCJFactory invokeSpecialMethod(Object... args) {
        execute.shutdown();
        return this;
    }
}
