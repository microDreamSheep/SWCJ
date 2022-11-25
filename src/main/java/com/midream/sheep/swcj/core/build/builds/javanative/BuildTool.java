package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.annotation.Param;
import com.midream.sheep.swcj.annotation.RequestType;
import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.MethodHandler;
import com.midream.sheep.swcj.pojo.buildup.MethodMeta;
import com.midream.sheep.swcj.pojo.enums.ChooseStrategy;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * 构建工具类
 * @author midreamsheep
 */
public class BuildTool {
      /**
     * 获取爬虫的具体类
     * @param className 类名
     * @return 爬虫对像
     * */
    public static Object getObjectFromTool(String className) {
        return CacheCorn.SPIDER_CACHE.getCacheSpider(className);
    }
    /**
     * 为爬虫实体类注入方法数据
     * @param swcjClass 爬虫实体类
     * @param rus 爬虫配置
     * @param config 爬虫配置数据
     * */
    public static void getFunction(SWCJClass swcjClass, MethodMeta meta, ReptileConfig config,List<ReptileUrl> rus) throws ClassNotFoundException, InterfaceIllegal {
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
                analysisMethodByAnnotation(swcjMethod, method, rus,config, swcjClass,meta);
            } else if (config.getChoice() == ChooseStrategy.METHOD_NAME) {
                analysisMethodByMethodName(swcjMethod, method, rus,config, swcjClass,meta);
            }
        }
    }
    /**
     * 通过注解解析方法
     * @param swcjMethod SWCJ方法实体类
     * @param method java反射方法对象
     * @param rus 爬虫配置数据
     * @param swcjClass 爬虫实体类
     * */
    private static void analysisMethodByAnnotation(SWCJMethod swcjMethod, Method method, List<ReptileUrl> rus,ReptileConfig reptileConfig, SWCJClass swcjClass,MethodMeta meta) throws InterfaceIllegal {
        //获取方法上的注解
        WebSpider spider = method.getAnnotation(WebSpider.class);
        //放入所有有注解的方法
        if (spider == null || spider.value().equals(Constant.nullString)) {
            throw new InterfaceIllegal("InterfaceMethodIllegal(接口方法不合法，请定义注解)");
        }
        for (ReptileUrl url : rus) {
            if(url.getName().equals(spider.value())){
                parsePublicArea(swcjMethod, url, meta,reptileConfig);
                swcjClass.addMethod(spider.value(), swcjMethod);
            }
        }
    }
    /**
     * 通过方法名解析方法
     * @param swcjMethod SWCJ方法实体类
     * @param method java反射方法对象
     * @param meta 方法源数据
     * @param swcjClass 爬虫实体类
     * */
    private static void analysisMethodByMethodName(SWCJMethod swcjMethod, Method method,List<ReptileUrl> rus,ReptileConfig reptileConfig ,SWCJClass swcjClass,MethodMeta meta) {
        for (ReptileUrl url : rus) {
            if(url.getName().equals(method.getName())){
                parsePublicArea(swcjMethod, url, meta,reptileConfig);
                swcjClass.addMethod(method.getName(), swcjMethod);
                return;
            }
        }
    }

    private static void parsePublicArea(SWCJMethod swcjMethod,ReptileUrl url, MethodMeta meta,ReptileConfig reptileConfig) {
        swcjMethod.setName(url.getName());
        List<String> vars = swcjMethod.getExecuteVars();
        swcjMethod.setParamIn(getMethodParametric(swcjMethod,vars).replace("class",Constant.nullString));
        swcjMethod.setExecuteStr(StringUtil.getExecuteCharacter(url,vars,reptileConfig,meta,swcjMethod));

    }
    /**
     * 获取方法参数列表
     * @param method 方法实体类
     * @param injection 参数列表
     * @return 参数列表字符串
     * */
    public static String getMethodParametric(SWCJMethod method, List<String> injection) {
        //获取拼接对象
        StringBuilder sb = new StringBuilder();
        //获取方法参数和输入参数
        List<MethodHandler> methodVars = method.getVars();
        for (MethodHandler var : methodVars) {
            injection.add(var.getMethodName());
            sb.append(var.getMethodType()).append(" ").append(var.getMethodName()).append(",");
        }
        return sb.toString().trim().equals("")?"":sb.substring(0, sb.lastIndexOf(","));
    }
}
