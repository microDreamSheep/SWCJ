package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.annotation.Param;
import com.midream.sheep.swcj.annotation.RequestType;
import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.MethodHandler;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;
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
     * @param middle 传递配置包
     * @return 爬虫实体类
     * */
    public static SWCJClass getSWCJClass(ReptlileMiddle middle) throws ConfigException, EmptyMatchMethodException {
        RootReptile rootReptile = middle.getRootReptile();
        //实例化类
        SWCJClass swcjClass = SWCJClass.buildClass();
        //设置接口，类名
        swcjClass.setClassName("swcj" + (t.addAndGet(1)));
        swcjClass.setItIterface(rootReptile.getParentInter());
        //效验接口是否有方法,并注入方法
        try {
            getFunction(swcjClass, rootReptile, middle.getConfig());
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
            List<MethodHandler> methodType = new LinkedList<>();
            for (Parameter parameter : method.getParameters()) {
                String type = Constant.getClassName(parameter.getType().toString());
                String methodName = parameter.getAnnotation(Param.class)==null?parameter.getName():parameter.getAnnotation(Param.class).value();
                MethodHandler handler = new MethodHandler(methodName, type);
                methodType.add(handler);
            }
            swcjMethod.setVars(methodType);
            if ((Constant.getClassName(method.getReturnType().toString()).equals(""))) {
                try {
                    throw new InterfaceIllegal("InterfaceReturnTypeIllegal(接口返回值不合法)");
                } catch (InterfaceIllegal returnTypeIllegal) {
                    Logger.getLogger(BuildTool.class.getName()).severe(returnTypeIllegal.getMessage());
                }
            }
            swcjMethod.setRequestType(method.getAnnotation(RequestType.class) == null ? "GET" : method.getAnnotation(RequestType.class).value().getValue());
            swcjMethod.setReturnType(Constant.getClassName(method.getReturnType().toString()));
            if (config.getChoice() == ChooseStrategy.ANNOTATION) {
                analysisMethodByAnnotation(swcjMethod, method, rootReptile,config, swcjClass);
            } else if (config.getChoice() == ChooseStrategy.METHOD_NAME) {
                analysisMethodByMethodName(swcjMethod, method, rootReptile,config, swcjClass);
            }
        }
    }
    /**
     * 通过注解解析方法
     * @param swcjMethod SWCJ方法实体类
     * @param method java反射方法对象
     * @param rootReptile 爬虫实体数据
     * @param swcjClass 爬虫实体类
     * */
    private static void analysisMethodByAnnotation(SWCJMethod swcjMethod, Method method, RootReptile rootReptile,ReptileConfig reptileConfig, SWCJClass swcjClass) throws InterfaceIllegal {
        //获取方法上的注解
        WebSpider spider = method.getAnnotation(WebSpider.class);
        //放入所有有注解的方法
        if (spider == null || spider.value().equals(Constant.nullString)) {
            throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
        }
        for (ReptileUrl url : rootReptile.getRu()) {
            if(url.getName().equals(spider.value())){
                parsePublicArea(swcjMethod, url, rootReptile,reptileConfig);
                swcjClass.addMethod(spider.value(), swcjMethod);
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
    private static void analysisMethodByMethodName(SWCJMethod swcjMethod, Method method, RootReptile rootReptile,ReptileConfig reptileConfig ,SWCJClass swcjClass) {
        for (ReptileUrl url : rootReptile.getRu()) {
            if(url.getName().equals(method.getName())){
                parsePublicArea(swcjMethod, url, rootReptile,reptileConfig);
                swcjClass.addMethod(method.getName(), swcjMethod);
                return;
            }
        }
    }

    private static void parsePublicArea(SWCJMethod swcjMethod,ReptileUrl url, RootReptile rootReptile,ReptileConfig reptileConfig) {
        swcjMethod.setName(url.getName());
        List<String> vars = swcjMethod.getExecuteVars();
        swcjMethod.setParamIn(getMethodParametric(url, swcjMethod,vars).replace("class",Constant.nullString));
        swcjMethod.setExecuteStr(StringUtil.getExecuteCharacter(url,vars,reptileConfig,rootReptile,swcjMethod));

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
        List<MethodHandler> methodVars = method.getVars();
        for (MethodHandler var : methodVars) {
            injection.add(var.getMethodName());
            sb.append(var.getMethodType()).append(" ").append(var.getMethodName()).append(",");
        }
        return sb.substring(0, sb.lastIndexOf(","));
    }
}
