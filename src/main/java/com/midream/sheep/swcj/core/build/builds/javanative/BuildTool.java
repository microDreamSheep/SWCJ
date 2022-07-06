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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public class BuildTool {
    private static final AtomicInteger t = new AtomicInteger(0);
    private static final String Template =
            "return new com.midream.sheep.swcj.core.analyzer.CornAnalyzer<#[fx]>().execute(\"#[execute]\",#[args]).toArray(new #[fx][0]);";

    public static Object getObjectFromTool(String className) {
        return CacheCorn.getObject(className);
    }

    public static SWCJClass getSWCJClass(RootReptile rootReptile, ReptileConfig config) throws ConfigException, EmptyMatchMethodException {
        //实例化类
        SWCJClass swcjClass = SWCJClass.buildClass();
        //设置接口，类名
        swcjClass.setClassName("swcj" + (t.addAndGet(1)));
        swcjClass.setItIterface(rootReptile.getParentInter());
        //效验接口是否有方法,并注入方法
        try {
            getFunction(swcjClass, rootReptile, config);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("你的接口不存在：" + rootReptile.getParentInter());
        } catch (InterfaceIllegal e) {
            throw new RuntimeException(e);
        }
        if (swcjClass.getMethods() == null || swcjClass.getMethods().size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        return swcjClass;
    }

    private static void getFunction(SWCJClass swcjClass, RootReptile rootReptile, ReptileConfig config) throws ClassNotFoundException, InterfaceIllegal {
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

            if (config.getChoice() == ChooseStrategy.ANNOTATION) {
                analysisMethodByAnnotation(swcjMethod, method, rootReptile, swcjClass);
            } else if (config.getChoice() == ChooseStrategy.METHOD_NAME) {
                analysisMethodByMethodName(swcjMethod, method, rootReptile, swcjClass);
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

    private static void analysisMethodByAnnotation(SWCJMethod swcjMethod, Method method, RootReptile rr, SWCJClass swcjClass) throws InterfaceIllegal {
        //获取方法上的注解
        WebSpider spider = method.getAnnotation(WebSpider.class);
        //放入所有有注解的方法
        if (spider == null || spider.value().equals("")) {
            throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
        }
        for (ReptileUrl url : rr.getRu()) {
            if (url.getName().equals(spider.value())) {
                swcjMethod.setName(url.getName());
                swcjClass.addMethod(spider.value(), swcjMethod);
                break;
            }
        }
    }

    private static void analysisMethodByMethodName(SWCJMethod swcjMethod, Method method, RootReptile rr, SWCJClass swcjClass) {
        for (ReptileUrl url : rr.getRu()) {
            if (url.getName().equals(method.getName())) {
                swcjMethod.setName(url.getName());
                swcjClass.addMethod(method.getName(), swcjMethod);
                break;
            }
        }
    }

    //
    public static String spliceMethod(ReptileUrl ru, RootReptile rr, SWCJMethod method, ReptileConfig rc) {
        //方法体
        StringBuilder sbmethod = new StringBuilder();
        //获取参数传入列表
        List<String> injection = new LinkedList<>();
        String varString = getMethodParametric(ru, method, injection);
        //方法头 定义被重写
        add(sbmethod, "\npublic ", method.getReturnType(), (" "), method.getMethodName(), "(", varString.replace("class ", ""), "){");
        //开始拼接方法
        {
            add(sbmethod, Template
                    .replace("#[execute]", StringUtil.getExecuteCharacter(ru, injection, rc, rr, method))
                    .replace("#[fx]", method.getReturnType().replace("[]", ""))
                    .replace(",#[args]", StringUtil.getStringByList(injection)));
        }
        add(sbmethod, "}");
        return sbmethod.toString();
    }

    public static String getMethodParametric(ReptileUrl ru, SWCJMethod method, List<String> injection) {
        //获取拼接对象
        StringBuilder sb = new StringBuilder();
        //获取方法参数和输入参数
        List<String> methodVars = method.getVars();
        String[] inPutVars = ru.getInPutName().split(",");

        //如果方法参数为空直接返回
        if (methodVars.size() == 0) {
            return "";
        }

        int len = inPutVars.length;
        if (ru.getInPutName().trim().equals("")) {
            len = 0;
        }

        if (len > methodVars.size()) {
            try {
                throw new InterfaceIllegal("方法参数不统一");
            } catch (InterfaceIllegal interfaceIllegal) {
                interfaceIllegal.printStackTrace();
            }
        } else if (len == methodVars.size()) {
            for (int i = 0; i < inPutVars.length; i++) {
                add(sb, methodVars.get(i), " ", inPutVars[i], ",");
                injection.add(inPutVars[i]);
            }
        } else {
            System.err.println("SWCJ:警告：你的接口有部分参数没有用到,方法:" + ru.getName());
            for (int i = 0; i < len; i++) {
                add(sb, methodVars.get(i), " ", inPutVars[i], ",");
                injection.add(inPutVars[i]);
            }
            for (int i = len; i < methodVars.size(); i++) {
                add(sb, methodVars.get(i), " args", i, ",");
            }
        }
        return sb.substring(0, sb.lastIndexOf(","));
    }
}
