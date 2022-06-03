package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SWCJParseI {
    /**
     * 解析文件
     * @param xmlFile 文件路径
     * */
    List<RootReptile> parseXmlFile(File xmlFile, ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException;
    /**
     * 解析具体字符串
     *
     * */
    List<RootReptile> parseStringXml(String file,ReptileConfig rc) throws ParserConfigurationException, IOException, SAXException;
}
