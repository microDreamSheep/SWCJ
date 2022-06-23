package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ReptilesBuilder extends SWCJBuilderAbstract {

    public Object loadClass(RootReptile rr, SWCJClass sclass) {
        if(swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        Class<?> aClass = null;
        try {
            aClass = swcjcl.compileJavaFile(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), sclass);
            return aClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public SWCJClass getSWCJClass(RootReptile rr,ReptileConfig rc) throws ClassNotFoundException, EmptyMatchMethodException, ConfigException {
        return BuildTool.getSWCJClass(rr,rc);
    }

    public void getAllMethod(SWCJClass sclass, RootReptile rr,ReptileConfig rc) {
        int count = 0;
        final List<ReptileUrl> rus = rr.getRu();
        Map<String, SWCJMethod> function = sclass.getMethods();
        for (ReptileUrl reptileUrl : rus) {
            SWCJMethod s = function.get(reptileUrl.getName());
            if (s != null && s.getName() != null && !s.getName().equals("")) {
                String s1 = BuildTool.spliceMethod(reptileUrl, rr, s,rc);
                s.setBody(s1.replace("\n", ""));
                count++;
            }
        }
        if (function.size() != count) {
            try {
                throw new InterfaceIllegal("IllMethod(可能你的方法没有与配置文件对应)");
            } catch (InterfaceIllegal e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected Object buildObject(RootReptile rr, ReptileConfig rc) {
        try {
            SWCJClass swcjClass = getSWCJClass(rr, rc);
            getAllMethod(swcjClass,rr, rc);
            return loadClass(rr,swcjClass);
        } catch (ClassNotFoundException | EmptyMatchMethodException | ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}