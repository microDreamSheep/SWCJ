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
    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        //开始拼接类信息
        SWCJClass sclass = null;
        try {
            sclass = BuildTool.getSWCJClass(rr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            getAllMethod(Objects.requireNonNull(sclass), rr,rc);
            return loadClass(rr, rc, sclass);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            System.err.println("类加载异常");
            e.printStackTrace();
        }
        return null;
    }

    private Object loadClass(RootReptile rr, ReptileConfig rc, SWCJClass sclass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        if(swcjcl==null){
            swcjcl = new SWCJClassLoader();
        }
        Class<?> aClass = swcjcl.compileJavaFile(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), sclass);
        if (rc.isCache()) {
            System.err.println("缓存功能暂未实现");
        }
        Object webc = aClass.getDeclaredConstructor().newInstance();
        CacheCorn.addObject(rr.getId(), webc);
        return webc;
    }

    private void getAllMethod(SWCJClass sclass, RootReptile rr,ReptileConfig rc) throws InterfaceIllegal {
        int count = 0;
        final List<ReptileUrl> rus = rr.getRu();
        Map<String, SWCJMethod> function = sclass.getMethods();
        for (ReptileUrl reptileUrl : rus) {
            SWCJMethod s = function.get(reptileUrl.getName());
            if (s != null && s.getAnnotation() != null && !s.getAnnotation().equals("")) {
                String s1 = BuildTool.spliceMethod(reptileUrl, rr, s,rc);
                s.setBody(s1);
                count++;
            }
        }
        if (function.size() != count) {
            throw new InterfaceIllegal("IllMethod(可能你的方法没有与配置文件对应)");
        }
    }
}