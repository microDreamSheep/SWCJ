package com.midream.sheep.swcj.core.factory.xmlfactory.bystr;

import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface SWCJParseI {

    AtomicInteger count = new AtomicInteger(0);

    /**
     * 解析文件
     *
     * @param xmlFile 文件路径
     */
    List<SWCJClass> parseXmlFile(File xmlFile, ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, InterfaceIllegal;
    /**
     * 解析具体字符串
     */
    List<SWCJClass> parseStringXml(String file, ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException, InterfaceIllegal;
}
