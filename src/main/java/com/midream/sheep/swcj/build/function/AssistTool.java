package com.midream.sheep.swcj.build.function;

import com.midream.sheep.swcj.Annotation.WebSpider;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.ReptileUrl;
import com.midream.sheep.swcj.data.swc.RootReptile;
import com.midream.sheep.swcj.pojo.SWCJClass;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.util.classLoader.SWCJClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.midream.sheep.swcj.build.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public class AssistTool {
    public static Object getObjectFromTool(String className, SWCJClassLoader swcjcl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object ob = CacheCorn.getObject(className);
        if (ob != null) {
            return ob;
        }
        String path = CacheCorn.getPath(className);
        if (path != null) {
            String name = Constant.DEFAULT_PACKAGE_NAME + "." + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
            return swcjcl.findClass(name).getDeclaredConstructor().newInstance();
        }
        return null;
    }

    public static Object getObjectFromTool(String className) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return getObjectFromTool(className,new SWCJClassLoader());
    }
    public static SWCJClass getSWCJClass(RootReptile rr, ReptileConfig rc) throws ConfigException, EmptyMatchMethodException,InterfaceIllegal {
        SWCJClass sclass = new SWCJClass();
        //获取所有方法
        List<ReptileUrl> rus = rr.getRu();
        throwsException(rus);
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        sclass.setClassName(name);
        //效验接口是否有方法,并返回方法名
        try {
            getFunction(rr.getParentInter(), sclass);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("你的接口不存在：" + rr.getParentInter());
        }
        if (sclass.getMethods() == null || sclass.getMethods().size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        //拼接超时时间
        sclass.addValue("int timeout =",rc.getTimeout()+"");
        //拼接浏览器标识
        StringBuilder usreAgent = new StringBuilder();
        for (int i = 0; i < rc.getUserAgents().size(); i++) {
            add(usreAgent,"\"",rc.getUserAgents().get(i),"\"",(i + 1 != rc.getUserAgents().size()) ? "," : "};");
        }
        sclass.addValue("String[] userAgent = new String[]{", usreAgent.toString());
        return sclass;
    }
    private static void throwsException(List<ReptileUrl> rus) throws ConfigException {
        for (ReptileUrl url : rus) {
            if(url.getUrl()==null||url.getUrl().equals("")){
                throw new ConfigException("你的path未配置,在"+url.getName());
            }
            if(url.getJsoup()==null&&url.getReg().equals("")){
                throw new ConfigException("你的策略未配置,在"+url.getName());
            }
            if(url.getName()==null||url.getName().equals("")){
                throw new ConfigException("你的name未配置,在"+url.getName());
            }
            for (int i = 0;i<url.getJsoup().get(0).getJsoup().size()-1;i++) {
                String element = url.getJsoup().get(0).getJsoup().get(i).getElement();
                if(element!=null&&!element.equals("")){
                    throw new ConfigException("元素获取必须在最后一个pa里定义,在"+url.getName());
                }
            }
        }
    }
    private static void getFunction(String className, SWCJClass swcjClass) throws ClassNotFoundException, InterfaceIllegal {
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
                swcjClass.addMethod(method.getAnnotation(WebSpider.class).value(),swcjMethod);
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
