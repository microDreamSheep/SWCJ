package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
/**
 * 具体类的构建中心
 * @author midreamsheep
 * */
public interface SWCJBuilder {
    /**
     * 构建一个类
     * @param rc 全局配置文件
     * @param rr 核心爬虫文件
     * @return 返回具体的实现类
     * @exception EmptyMatchMethodException 没有适配的方法
     * @exception ConfigException 配置文件异常
     * @exception InterfaceIllegal 接口异常
     * */
    Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
    /**
     * 设置编译器
     * @param swcjCompiler 编译器
     * */
    void setCompiler(SWCJCompiler swcjCompiler);
}
