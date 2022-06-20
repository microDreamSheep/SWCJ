package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

import java.util.Objects;

public abstract class SWCJBuilderAbstract implements SWCJBuilder{
    protected SWCJClassLoader swcjcl;
    @Override
    public void setCompiler(SWCJCompiler swcjCompiler) {
        if(this.swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        this.swcjcl.setSwcjCompiler(swcjCompiler);
    }
    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal
    {
        try{
            //开始拼接类信息
            SWCJClass sclass = getSWCJClass(rr);
            getAllMethod(Objects.requireNonNull(sclass), rr,rc);
            return loadClass(rr, sclass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public abstract SWCJClass getSWCJClass(RootReptile rr) throws ClassNotFoundException, EmptyMatchMethodException, ConfigException;
    public abstract void getAllMethod(SWCJClass sclass, RootReptile rr, ReptileConfig rc);
    public abstract Object loadClass(RootReptile rr, SWCJClass sclass);
}
