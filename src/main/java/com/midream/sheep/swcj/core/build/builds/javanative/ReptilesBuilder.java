package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilderAbstract;
import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class ReptilesBuilder extends SWCJBuilderAbstract {

    public Object loadClass(SWCJClass sclass,ReptlileMiddle middle) {
        try {
            DataInComplier data = swcjCompiler.compileAndLoad(sclass,middle);
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

    public SWCJClass getSWCJClass(ReptlileMiddle middle) throws ClassNotFoundException, EmptyMatchMethodException, ConfigException {
        return BuildTool.getSWCJClass(middle);
    }

    @Override
    protected Object buildObject(ReptlileMiddle middle) {
        try {
            return loadClass(getSWCJClass(middle),middle);
        } catch (ClassNotFoundException | EmptyMatchMethodException | ConfigException e) {
            Logger.getLogger(ReptilesBuilder.class.getName()).info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}