package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
/**
 * 这是工厂类的模板，自定义工厂需要实现这些方法
 * @author midreamsheep
 * */
public interface SWCJXmlFactory {
    /**
     * 通过文件流解析文件
     * @param xmlFile 文件流
     * */
    void parse(File xmlFile);
    /**
     * 通过文件流解析文件
     * @param xmlString xml字符串
     * */
    void parse(String xmlString);
    /**
     * 获取具体的实现类
     * @param id 配置文件的具体id
     * @return 具体的实现对象
     * */
    Object getWebSpider(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
    /**
     * 设置编译器
     * @param swcjCompiler 编译器
     * */
    void setCompiler(SWCJCompiler swcjCompiler);
    /**
     * 设置构建器
     * @param swcjBuilder 构建器
     * */
    void setBuilder(SWCJBuilder swcjBuilder);
    /**
     * 设置解析器
     * @param swcjParseI 解析器
     * */
    void setParseTool(SWCJParseI swcjParseI);
}
