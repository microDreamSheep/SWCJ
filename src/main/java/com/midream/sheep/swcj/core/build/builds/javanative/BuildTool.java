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
import java.util.logging.Logger;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * 构建工具类
 * @author midreamsheep
 */
public class BuildTool {
    /**原子性自增数*/
    private static final AtomicInteger t = new AtomicInteger(0);
    /**方法体模板*/
    private static final String Template =
            "return new com.midream.sheep.swcj.core.analyzer.CornAnalyzer<#[fx]>().execute(\"#[execute]\",#[args]).toArray(new #[fx][0]);";
    /**
     * 获取爬虫的具体类
     * @param className 类名
     * @return 爬虫对像
     * */
    public static Object getObjectFromTool(String className) {
        return CacheCorn.getObject(className);
    }
    /**
     * 获取爬虫实体类
     * @param rootReptile 爬虫实体数据
     * @param config 爬虫配置数据
     * @return 爬虫实体类
     * */
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
            Logger.getLogger(BuildTool.class.getName()).severe(e.getMessage());
            throw new ConfigException("你的接口不存在：" + rootReptile.getParentInter());
        } catch (InterfaceIllegal e) {
            Logger.getLogger(BuildTool.class.getName()).severe(e.getMessage());
            throw new RuntimeException(e);
        }
        if (swcjClass.getMethods() == null || swcjClass.getMethods().size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        return swcjClass;
    }
    /**
     * 为爬虫实体类注入方法数据
     * @param swcjClass 爬虫实体类
     * @param rootReptile 爬虫实体数据
     * @param config 爬虫配置数据
     * */
    private static void getFunction(SWCJClass swcjClass, RootReptile rootReptile, ReptileConfig config) throws ClassNotFoundException, InterfaceIllegal {
        Method[] methods = Class.forName(swcjClass.getItIterface()).getMethods();
        for (Method method : methods) {
            //实例化方法类
            SWCJMethod swcjMethod = new SWCJMethod();
            //设置方法名
            swcjMethod.setMethodName(method.getName());
            //设置方法属性
            List<String> methodType = new LinkedList<>();
            for (Parameter parameter : method.getParameters()) {
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
                    Logger.getLogger(BuildTool.class.getName()).severe(returnTypeIllegal.getMessage());
                }
            }
            swcjMethod.setReturnType(Constant.getClassName(method.getReturnType().toString()));
        }
    }
    /**
     * 通过注解解析方法
     * @param swcjMethod SWCJ方法实体类
     * @param method java反射方法对象
     * @param rootReptile 爬虫实体数据
     * @param swcjClass 爬虫实体类
     * */
    private static void analysisMethodByAnnotation(SWCJMethod swcjMethod, Method method, RootReptile rootReptile, SWCJClass swcjClass) throws InterfaceIllegal {
        //获取方法上的注解
        WebSpider spider = method.getAnnotation(WebSpider.class);
        //放入所有有注解的方法
        if (spider == null || spider.value().equals("")) {
            throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
        }
        for (ReptileUrl url : rootReptile.getRu()) {
            if (url.getName().equals(spider.value())) {
                swcjMethod.setName(url.getName());
                swcjClass.addMethod(spider.value(), swcjMethod);
                break;
            }
        }
    }
    /**
     * 通过方法名解析方法
     * @param swcjMethod SWCJ方法实体类
     * @param method java反射方法对象
     * @param rootReptile 爬虫实体数据
     * @param swcjClass 爬虫实体类
     * */
    private static void analysisMethodByMethodName(SWCJMethod swcjMethod, Method method, RootReptile rootReptile, SWCJClass swcjClass) {
        for (ReptileUrl url : rootReptile.getRu()) {
            if (url.getName().equals(method.getName())) {
                swcjMethod.setName(url.getName());
                swcjClass.addMethod(method.getName(), swcjMethod);
                break;
            }
        }
    }

    /**
     * 构建方法主体
     * @param method SWCJ方法实体类
     * @param config 爬虫配置数据
     * @param rootReptile 爬虫实体数据
     * @param ru 爬虫实体数据
     * @return 方法主体
     * */
    public static String spliceMethod(ReptileUrl ru, RootReptile rootReptile, SWCJMethod method, ReptileConfig config) {
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
                    .replace("#[execute]", StringUtil.getExecuteCharacter(ru, injection, config, rootReptile, method))
                    .replace("#[fx]", method.getReturnType().replace("[]", ""))
                    .replace(",#[args]", StringUtil.getStringByList(injection)));
        }
        add(sbmethod, "}");
        return sbmethod.toString();
    }
    /**
     * 获取方法参数列表
     * @param ru 爬虫方法实体数据
     * @param method 方法实体类
     * @param injection 参数列表
     * @return 参数列表字符串
     * */
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
                Logger.getLogger(BuildTool.class.getName()).severe(interfaceIllegal.getMessage());
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
