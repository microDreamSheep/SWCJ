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

    public Object loadClass(ReptlileMiddle middle) {
        try {
            DataInComplier data = swcjCompiler.compileAndLoad(middle.getSwcjClass(),middle);
            if(data.isIsload()){
                return data.getaClass().getDeclaredConstructor().newInstance();
            }
            return swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + middle.getSwcjClass().getClassName(), data.getDatas()).getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            Logger.getLogger(ReptilesBuilder.class.getName()).info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object buildObject(ReptlileMiddle middle) {
        return loadClass(middle);
    }
}