package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.classtool.compiler.javanative.DynamicCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

public abstract class SWCJBuilderAbstract implements SWCJBuilder{
    protected SWCJClassLoaderInter swcjcl;
    protected SWCJCompiler swcjCompiler;
    @Override
    public void setCompiler(SWCJCompiler swcjCompiler) {
        this.swcjCompiler = swcjCompiler;
    }

    @Override
    public void setClassLoader(SWCJClassLoaderInter classLoader) {
        this.swcjcl = classLoader;
    }

    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) {
        notNull();
        //开始拼接类信息
        Object o =  buildObject(rr,rc);
        CacheCorn.addObject(rr.getId(), o);
        return o;
    }
    private void notNull(){
        if(swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        if(swcjCompiler == null){
            swcjCompiler = new DynamicCompiler();
        }
    }
    protected abstract Object buildObject(RootReptile rr, ReptileConfig rc);
}
