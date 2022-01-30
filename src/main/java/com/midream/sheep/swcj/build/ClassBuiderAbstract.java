package com.midream.sheep.swcj.build;

import com.midream.sheep.swcj.Annotation.WebSpider;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.ReptileUrl;
import com.midream.sheep.swcj.data.swc.RootReptile;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.util.classLoader.SWCJClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public abstract class ClassBuiderAbstract implements ClassBuilder {
    private static final SWCJClassLoader swcjcl;

    static {
        swcjcl = new SWCJClassLoader();
    }
    @Override
    public Object builderClass(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException {
        //拼接的主体
        StringBuilder sb = new StringBuilder("");
        //从池中查找，如果有直接返回
        Object o = checkpool(rr);
        if(o!=null){
            return o;
        }
        //拼接类头
        spliceClassHeader(sb,rr,rc);
        //拼接类的主要方法

        return null;
    }
    private boolean spliceGlobalVariable(StringBuilder sb, ReptileConfig rc){
        add(sb,"private static int timeout = ",rc.getTimeout(),";\n");
        //userAgent数组创建
        StringBuilder usreAgent = new StringBuilder();
        add(usreAgent,"private static String[] userAgent = new String[]{");
        for (int i = 0; i < rc.getUserAgents().size(); i++) {
            add(usreAgent,"\"",rc.getUserAgents().get(i),"\"",(i + 1 != rc.getUserAgents().size()) ? "," : "};");
        }
        add(sb,usreAgent,"\n");

    }
    private Object checkpool(RootReptile rr){
        Object object = null;
        try {
            object = getObject(rr.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
            return object;
    }
    private void spliceClassHeader(StringBuilder sb, RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException {
        //获取所有方法
        List<ReptileUrl> rus = rr.getRu();
        throwsException(rus);
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        //效验接口是否有方法,并返回方法名
        Map<String, SWCJMethod> function = new HashMap<>();
        try {
            getFunction(rr.getParentInter(), function);
        } catch (ClassNotFoundException | InterfaceIllegal e) {
            throw new ConfigException("你的接口不存在：" + rr.getParentInter());
        }
        if (function.size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        //增加包名
        add(sb, "package ", Constant.DEFAULT_PACKAGE_NAME, ";\n");
        //拼接类名
        add(sb, "public class ", name, " implements ", rr.getParentInter(), " {");
    }

    private void throwsException(List<ReptileUrl> rus) throws ConfigException {
        for (ReptileUrl url : rus) {
            if (url.getUrl() == null || url.getUrl().equals("")) {
                throw new ConfigException("你的path未配置,在" + url.getName());
            }
            if (url.getJsoup() == null && url.getReg().equals("")) {
                throw new ConfigException("你的策略未配置,在" + url.getName());
            }
            if (url.getName() == null || url.getName().equals("")) {
                throw new ConfigException("你的name未配置,在" + url.getName());
            }
            for (int i = 0; i < url.getJsoup().get(0).getJsoup().size() - 1; i++) {
                String element = url.getJsoup().get(0).getJsoup().get(i).getElement();
                if (element != null && !element.equals("")) {
                    throw new ConfigException("元素获取必须在最后一个pa里定义,在" + url.getName());
                }
            }
        }
    }

    public Object getObject(String Key) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //从缓存池中找
        Object ob = CacheCorn.getObject(Key);
        if (ob != null) {
            return ob;
        }
        //从加载器中找
        String path = CacheCorn.getPath(Key);
        if (path != null) {
            String name = Constant.DEFAULT_PACKAGE_NAME + "." + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
            return swcjcl.findClass(name).getDeclaredConstructor().newInstance();
        }
        return null;
    }
    public void getFunction(String className, Map<String,SWCJMethod> function) throws ClassNotFoundException, InterfaceIllegal {
        Class<?> ca = Class.forName(className);
        Method[] methods = ca.getMethods();
        for (Method method : methods) {
            //实例化方法类
            SWCJMethod swcjMethod = new SWCJMethod();
            //设置方法名
            swcjMethod.setMethodName(method.getName());
            //设置方法属性
            List<String> methodType = new ArrayList<>();
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                methodType.add(Constant.getClassName(parameter.getType().toString()));
            }
            swcjMethod.setVars(methodType);
            //放入所有有注解的方法
            if(method.getAnnotation(WebSpider.class)!=null&&!method.getAnnotation(WebSpider.class).value().equals("")){
                swcjMethod.setAnnotation(method.getAnnotation(WebSpider.class).value());
                if(!(Constant.getClassName(method.getReturnType().toString()).equals(""))) {
                    swcjMethod.setReturnType(Constant.getClassName(method.getReturnType().toString()));
                }else{
                    try {
                        throw new InterfaceIllegal("InterfaceReturnTypeIllegal(接口返回值不合法)");
                    } catch (InterfaceIllegal returnTypeIllegal) {
                        returnTypeIllegal.printStackTrace();
                    }
                }
                function.put(method.getAnnotation(WebSpider.class).value(),swcjMethod);
            }else{
                try {
                    throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
                } catch (InterfaceIllegal interfaceIllegal) {
                    interfaceIllegal.printStackTrace();
                }
            }
        }
    }
}
