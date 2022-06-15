package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

public abstract class SWCJBuilderAbstract implements SWCJBuilder{
    protected SWCJClassLoader swcjcl;
    @Override
    public void setCompiler(SWCJCompiler swcjCompiler) {
        this.swcjcl.setSwcjCompiler(swcjCompiler);
    }
    @Override
    public abstract Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal;
}
