package com.midream.sheep.swcj.core.build.inter;

import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

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
    public Object Builder(RootReptile rr, ReptileConfig rc)
    {
            //开始拼接类信息
            Object o =  buildObject(rr,rc);
            CacheCorn.addObject(rr.getId(), o);
            return o;
    }

    protected abstract Object buildObject(RootReptile rr, ReptileConfig rc);
}
