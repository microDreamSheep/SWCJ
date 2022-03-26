package com.midream.sheep.swcj.core;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public interface SWCJXmlFactory {
    void parse(File xmlFile) throws IOException, SAXException, ConfigException, ParserConfigurationException;
    Object getWebSpider(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
}
