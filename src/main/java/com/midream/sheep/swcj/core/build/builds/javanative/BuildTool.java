package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.midream.sheep.swcj.util.function.StringUtil.StringToUpperCase;
import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public class BuildTool {
    private static final Random r  = new Random();
    private static int t = 0;
    private static final String Template =
            "return new com.midream.sheep.swcj.core.analyzer.CornAnalyzer<#[fx]>().execute(\"#[execute]\",#[args]).toArray(new #[fx][0]);";

    public static Object getObjectFromTool(String className) {
        return CacheCorn.getObject(className);
    }

    public static SWCJClass getSWCJClass(RootReptile rr,ReptileConfig rc) throws ConfigException, EmptyMatchMethodException, ClassNotFoundException {
        SWCJClass sclass = SWCJClass.buildClass();
        //获取类名
        String name = "a" + (t++);
        sclass.setClassName(name);
        sclass.setItIterface(rr.getParentInter());
        //效验接口是否有方法,并返回方法名
        try {
            getFunction(sclass,rr,rc);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("你的接口不存在：" + rr.getParentInter());
        } catch (InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
        if (sclass.getMethods() == null || sclass.getMethods().size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        return sclass;
    }

    private static void getFunction(SWCJClass swcjClass,RootReptile rr,ReptileConfig rc) throws ClassNotFoundException, InterfaceIllegal {
        Method[] methods = Class.forName(swcjClass.getItIterface()).getMethods();
            for (Method method : methods) {
                method.setAccessible(true);
                //实例化方法类
                SWCJMethod swcjMethod = new SWCJMethod();
                //设置方法名
                swcjMethod.setMethodName(method.getName());
                //设置方法属性
                List<String> methodType = new LinkedList<>();
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    methodType.add(Constant.getClassName(parameter.getType().toString()));
                }
                swcjMethod.setVars(methodType);
                if(rc.getChoice()== ChooseStrategy.ANNOTATION) {
                    WebSpider spider = method.getAnnotation(WebSpider.class);
                    //放入所有有注解的方法
                    if (spider == null || spider.value().equals("")) {
                        throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
                    }
                    swcjMethod.setName(Objects.requireNonNull(spider).value());
                    swcjClass.addMethod(spider.value(), swcjMethod);

                }else if(rc.getChoice()== ChooseStrategy.METHOD_NAME){
                    for (ReptileUrl url : rr.getRu()) {
                        if(url.getName().equals(method.getName())){
                            swcjMethod.setName(url.getName());
                            break;
                        }
                    }
                    swcjClass.addMethod(method.getName(), swcjMethod);
                }
                if ((Constant.getClassName(method.getReturnType().toString()).equals(""))) {
                    try {
                        throw new InterfaceIllegal("InterfaceReturnTypeIllegal(接口返回值不合法)");
                    } catch (InterfaceIllegal returnTypeIllegal) {
                        returnTypeIllegal.printStackTrace();
                    }
                }
                swcjMethod.setReturnType(Constant.getClassName(method.getReturnType().toString()));
            }
    }
    //
    public static String spliceMethod(ReptileUrl ru, RootReptile rr, SWCJMethod method, ReptileConfig rc) {
        //方法体
        StringBuilder sbmethod = new StringBuilder();
        //获取参数传入列表
        List<String> injection = new LinkedList<>();
        String varString = getMethodParametric(ru,method,injection);
        //方法头 定义被重写
        add(sbmethod, "\npublic ", method.getReturnType(), (" "), method.getMethodName(), "(", varString.replace("class ",""), "){");
        //开始拼接方法
        {
            String executeCharacter = StringUtil.getExecuteCharacter(ru, injection, rc, rr, method);
            String s = Template.replace("#[execute]", executeCharacter).replace("#[fx]", method.getReturnType()
                            .replace("[]", ""))
                    .replace("\n", "")
                    .replace(",#[args]", StringUtil.getStringByList(injection));
            add(sbmethod, s);
        }
        add(sbmethod, "}");
        return sbmethod.toString();
    }
    private static String getMethodParametric(ReptileUrl ru,SWCJMethod method,List<String> injection){
        StringBuilder vars = new StringBuilder();
        List<String> vars1 = method.getVars();
        String[] split2 = ru.getInPutName().split(",");
        int len = split2.length;
        if (ru.getInPutName().trim().equals("")) {
            len = 0;
        }
        if (len > vars1.size()) {
            try {
                throw new InterfaceIllegal("方法参数不统一");
            } catch (InterfaceIllegal interfaceIllegal) {
                interfaceIllegal.printStackTrace();
            }
        } else if (len == vars1.size() && len != 0) {
            for (int i = 0; i < split2.length; i++) {
                add(vars, vars1.get(i), " ", split2[i], ",");
                injection.add(split2[i]);
            }
        } else if(len==0){
        }else{
            System.err.println("SWCJ:警告：你的接口有部分参数没有用到,方法:" + ru.getName());
            for (int i = 0; i < len; i++) {
                add(vars, vars1.get(i), " ", split2[i], ",");
                injection.add(split2[i]);
            }
            for (int i = len; i < vars1.size(); i++) {
                add(vars, vars1.get(i), " args", i, ",");
            }
        }
        String varString = "";
        if (vars.length() != 0) {
            varString = vars.substring(0, vars.lastIndexOf(","));
        }
        return varString;
    }
}
