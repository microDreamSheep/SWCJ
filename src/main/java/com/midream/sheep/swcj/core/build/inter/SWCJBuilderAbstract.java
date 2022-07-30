package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.classtool.compiler.javanative.DynamicCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.swc.all.ReptlileMiddle;

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
    public Object Builder(ReptlileMiddle middle) {
        //非空判断
        notNull();
        //开始拼接类信息
        return CacheCorn.addObject(middle.getRootReptile().getId(),buildObject(middle));
    }
    private void notNull(){
        if(swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        if(swcjCompiler == null){
            swcjCompiler = new DynamicCompiler();
        }
    }
    protected abstract Object buildObject(ReptlileMiddle middle);
}
