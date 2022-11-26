package com.midream.sheep.swcj.core.factory.annotationfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.annotation.Param;
import com.midream.sheep.swcj.annotation.RequestType;
import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.annotation.config.method.*;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.MethodHandler;
import com.midream.sheep.swcj.pojo.buildup.MethodMeta;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.logging.Logger;

import static com.midream.sheep.swcj.core.build.builds.javanative.BuildTool.getMethodParametric;
import static com.midream.sheep.swcj.core.factory.FactoryData.count;

public class CoreFactoryByClass extends SWCJClassFactory{



    public CoreFactoryByClass(){

    }
    public CoreFactoryByClass(boolean isLoadCache,String workplace) throws ConfigException {
        if(!isLoadCache){
            return;
        }
        cache(workplace);
    }


    @Override
    protected SWCJClass parseClass(Class<?> interfaceClass) throws ConfigException, InterfaceIllegal {
        SWCJClass swcjClass = new SWCJClass();
        swcjClass.setItIterface(interfaceClass.getName());
        swcjClass.setClassName("swcj" + (count.addAndGet(1)));
        swcjClass.setMethods(parseMethod(interfaceClass.getMethods()));
        return swcjClass;
    }
    private Map<String, SWCJMethod> parseMethod(Method[] methods ) throws InterfaceIllegal, ConfigException {
        Map<String,SWCJMethod> swcjMethodMap = new LinkedHashMap<>();
        for (Method method : methods) {
            //实例化方法类
            SWCJMethod swcjMethod = new SWCJMethod();
            //设置方法名
            swcjMethod.setMethodName(method.getName());
            swcjMethod.setName(method.getName());
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
            MethodMeta meta = new MethodMeta();
            meta.setCookies(method.getAnnotation(MethodMetaConfig.class)==null?"":method.getAnnotation(MethodMetaConfig.class).cookies());
            parsePublicArea(swcjMethod, meta,config,method);
            swcjMethodMap.put(swcjMethod.getMethodName(), swcjMethod);
        }
        return swcjMethodMap;
    }
    private static void parsePublicArea(SWCJMethod swcjMethod, MethodMeta meta, ReptileConfig reptileConfig,Method method) throws ConfigException {
        ReptileUrl reptileUrl = new ReptileUrl();
        ExecuteLogic executeLogic = null;
        try {
            executeLogic =  method.getAnnotation(ExecuteLogic.class);
            reptileUrl.setExecutClassName(Constant.getExecute(executeLogic.executorKey()));
            reptileUrl.setParseProgram(executeLogic.processProgram());
            reptileUrl.setHtml(executeLogic.IsHtml());

            reptileUrl.setUrl(method.getAnnotation(UrlConfig.class).value());
        }catch (Exception e){
            throw new ConfigException("ConfigException(配置异常):must include @Executor,@UrlConfig,@ParseProgram"+e.getMessage());
        }
        reptileUrl.setValues(method.getAnnotation(ValueConfig.class)==null?"":method.getAnnotation(ValueConfig.class).value());

        List<String> vars = swcjMethod.getExecuteVars();
        swcjMethod.setParamIn(getMethodParametric(swcjMethod,vars).replace("class",Constant.nullString));
        swcjMethod.setExecuteStr(StringUtil.getExecuteCharacter(reptileUrl,vars,reptileConfig,meta,swcjMethod));
    }
}
