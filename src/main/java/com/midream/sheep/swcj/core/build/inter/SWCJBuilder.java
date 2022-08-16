package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;

/**
 * 具体类的构建中心
 * @author midreamsheep
 * */
public interface SWCJBuilder {
    /**
     * 构建一个类
     * @param middle 传递的配置文件
     * @return 返回具体的实现类
     * @exception EmptyMatchMethodException 没有适配的方法
     * @exception ConfigException 配置文件异常
     * @exception InterfaceIllegal 接口异常
     * */
    Object Builder(ReptlileMiddle middle) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
    /**
     * 设置编译器
     * @param swcjCompiler 编译器
     * */
    void setCompiler(SWCJCompiler swcjCompiler);
    /**
     * 设置加载器
     * */
    void setClassLoader(SWCJClassLoaderInter classLoader);
}
