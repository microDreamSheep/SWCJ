package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.factory.xmlfactory.bystr.SWCJParseI;

/**
 * 这是工厂类的模板，自定义工厂需要实现这些方法
 * @author midreamsheep
 * */
public interface SWCJFactory {
    /**
     * 获取具体的实现类
     * @param id 配置文件的具体id
     * @return 具体的实现对象
     * */
    Object getWebSpiderById(String id) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
    /**
     * 设置编译器
     * @param swcjCompiler 编译器
     * */
    SWCJFactory setCompiler(SWCJCompiler swcjCompiler);
    /**
     * 设置构建器
     * @param swcjBuilder 构建器
     * */
    SWCJFactory setBuilder(SWCJBuilder swcjBuilder);
    /**
     * 设置类加载器
     * @param classLoader 加载器
     * */
    SWCJFactory setClassLoader(SWCJClassLoaderInter classLoader);
    /**
     * 设置解析器
     * @param swcjParseI 解析器
     * */
    SWCJFactory setParseTool(SWCJParseI swcjParseI);
    /**
     * 调用专属特殊方法
     * @param args 传递值
     * */
    SWCJFactory invokeSpecialMethod(Object... args);
}
