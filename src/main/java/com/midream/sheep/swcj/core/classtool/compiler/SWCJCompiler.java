package com.midream.sheep.swcj.core.classtool.compiler;

import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.swc.all.ReptlileMiddle;

/**
 * 编译器有加载器调用
 * @author midreamsheep
 * */
public interface SWCJCompiler {
    /**
     * 编译
     * @param sclass 处理的class
     * @return 返回Class对象
     * */
    DataInComplier compileAndLoad(SWCJClass sclass, ReptlileMiddle middle) throws ClassNotFoundException;
}
