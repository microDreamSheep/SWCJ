package com.midream.sheep.swcj.core.classtool.compiler;

import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
/**
 * 编译器有加载器调用
 * @author midreamsheep
 * */
public interface SWCJCompiler {
    /**
     * 编译并调用加载器加载
     * @param fullName 全类名
     * @param sclass 处理的class
     * @return 返回Class对象
     * */
    Class<?> compileAndLoad(String fullName, SWCJClass sclass) throws ClassNotFoundException;
}
