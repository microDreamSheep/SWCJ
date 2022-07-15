package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Logger;

public class ReptilesBuilder extends SWCJBuilderAbstract {

    public Object loadClass(SWCJClass sclass) {
        if(swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        try {
            DataInComplier data = swcjCompiler.compileAndLoad(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), sclass);
            if(data.isIsload()){
                return data.getaClass().getDeclaredConstructor().newInstance();
            }
            return swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), data.getDatas()).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            Logger.getLogger(ReptilesBuilder.class.getName()).info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public SWCJClass getSWCJClass(RootReptile rr,ReptileConfig rc) throws ClassNotFoundException, EmptyMatchMethodException, ConfigException {
        return BuildTool.getSWCJClass(rr,rc);
    }

    public void getAllMethod(SWCJClass sclass, RootReptile rr,ReptileConfig rc) {
        Map<String, SWCJMethod> function = sclass.getMethods();
        for (ReptileUrl reptileUrl : rr.getRu()) {
            SWCJMethod method = function.get(reptileUrl.getName());
            if (method == null) {
                continue;
            }
            method.setBody(BuildTool.spliceMethod(reptileUrl, rr, method,rc));
        }
    }

    @Override
    protected Object buildObject(RootReptile rr, ReptileConfig rc) {
        try {
            SWCJClass swcjClass = getSWCJClass(rr, rc);
            getAllMethod(swcjClass,rr, rc);
            return loadClass(swcjClass);
        } catch (ClassNotFoundException | EmptyMatchMethodException | ConfigException e) {
            Logger.getLogger(ReptilesBuilder.class.getName()).info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}