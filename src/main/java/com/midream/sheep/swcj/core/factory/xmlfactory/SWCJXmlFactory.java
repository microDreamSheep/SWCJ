package com.midream.sheep.swcj.core.factory.xmlfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.bystr.SWCJParseI;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author midreamsheep
 */
public abstract class SWCJXmlFactory
        extends SWCJAbstractFactory
{
    //解析器
    protected SWCJParseI swcjParseI = null;

    public SWCJXmlFactory setParseTool(SWCJParseI swcjParseI) {
        this.swcjParseI = swcjParseI;
        return this;
    }
    /**
     * 通过文件流解析文件
     * @param xmlFile 文件流
     * */
    public abstract SWCJXmlFactory parse(File xmlFile) throws IOException, SAXException, ConfigException, ParserConfigurationException;
    /**
     * 通过文件流解析文件
     * @param xmlString 文件
     * */
    public abstract SWCJXmlFactory parse(String xmlString) throws IOException, SAXException, ConfigException, ParserConfigurationException;
}
