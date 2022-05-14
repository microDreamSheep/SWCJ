package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.factory.parse.IParseTool;
import com.midream.sheep.swcj.classtool.compiler.SWCJCompiler;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public interface SWCJXmlFactory {
    //解析文件
    void parse(File xmlFile) throws IOException, SAXException, ConfigException, ParserConfigurationException;
    //获取类
    Object getWebSpider(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal, ClassNotFoundException;
    //增加资源文件
    void addResource(String File);
    //设置编译器
    void setCompiler(SWCJCompiler swcjCompiler);
    //设置构建器
    void setBuilder(SWCJBuilder swcjBuilder);
    //设置解析器
    void setParseTool(IParseTool iParseTool);
}
