package com.midream.sheep.swcj.build.builds;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.build.function.BuildTool;
import com.midream.sheep.swcj.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.SWCJClass;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.util.classLoader.SWCJClassLoader;
import com.midream.sheep.swcj.util.compiler.SWCJCompiler;
import com.midream.sheep.swcj.util.io.ISIO;
import com.midream.sheep.swcj.util.io.SIO;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.midream.sheep.swcj.build.function.StringUtil.add;

public class ReptilesBuilder implements SWCJBuilder {
    private static final ISIO sio;
    private static final SWCJClassLoader swcjcl;

    static {
        //单例设计模式
        sio = new SIO();
        swcjcl = new SWCJClassLoader();
    }
    public void setCompiler(SWCJCompiler swcjCompiler){
        swcjcl.setSwcjCompiler(swcjCompiler);
    }
    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal, ClassNotFoundException {
        //开始拼接类信息
        SWCJClass sclass = BuildTool.getSWCJClass(rr, rc);
        try {
            getAllMethod(sclass, rr);
            return loadClass(rr, rc, sclass);
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            System.err.println("类加载异常");
        }
        return null;
    }

    private Object loadClass(RootReptile rr, ReptileConfig rc, SWCJClass sclass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException, InterfaceIllegal {
        Class<?> aClass = swcjcl.compileJavaFile(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), sclass);
        if (rc.isCache()) {
            System.err.println("缓存功能暂未实现");
        }
        Object webc = aClass.getDeclaredConstructor().newInstance();
        if (rc.isCache()) {
            //进入对象池
            CacheCorn.addObject(rr.getId(), webc);
        }
        //返回类
        return webc;
    }

    private void getAllMethod(SWCJClass sclass, RootReptile rr) throws InterfaceIllegal, ConfigException {
        int count = 0;
        final List<ReptileUrl> rus = rr.getRu();
        Map<String, SWCJMethod> function = sclass.getMethods();
        for (ReptileUrl reptileUrl : rus) {
            SWCJMethod s = function.get(reptileUrl.getName());
            if (s != null && s.getAnnotation() != null && !s.getAnnotation().equals("")) {
                String s1 = BuildTool.spliceMethod(reptileUrl, rr, s);
                s.setBody(s1);
                count++;
            }
        }
        if (function.size() != count) {
            throw new InterfaceIllegal("IllMethod(可能你的方法没有与配置文件对应)");
        }
    }
}