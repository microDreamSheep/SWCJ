package com.midream.sheep.swcj.build.builder;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.build.ClassBuiderAbstract;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.ReptileUrl;
import com.midream.sheep.swcj.data.swc.RootReptile;
import com.midream.sheep.swcj.pojo.SWCJClass;
import com.midream.sheep.swcj.pojo.SWCJMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public class JsoupBuilder extends ClassBuiderAbstract {
    @Override
    public Object builderClass(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException {
        SWCJClass sclass = new SWCJClass();
        //获取所有方法
        List<ReptileUrl> rus = rr.getRu();
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        sclass.setClassName(name);
        sclass.setItIterface(rr.getParentInter());

        return null;
    }

    @Override
    public Object getObject(String Key) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return super.getObject(Key);
    }

    @Override
    public void getFunction(String className, Map<String, SWCJMethod> function) throws ClassNotFoundException, InterfaceIllegal {
        super.getFunction(className, function);
    }
}
