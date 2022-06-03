package com.midream.sheep.swcj.core.build.builds.javanative;

import com.midream.sheep.swcj.annotation.WebSpider;
import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * @author midreamsheep
 */
public class BuildTool {
    private static final String Template = "try {\n" +
            "SWCJExecute swcjExecute = new #[class]();\n" +
            "String corn = \"#[method]\";\n" +
            "ExecuteValue executeValue = new ExecuteValue();\n" +
            "executeValue.setHtml(#[isHtml]);\n" +
            "executeValue.setType(com.midream.sheep.swcj.core.executetool.execute.SRequest.#[type]);\n" +
            "executeValue.setUrl(\"#[url]\");\n" +
            "executeValue.setClassNameReturn(\"#[returntype]\");\n" +
            "executeValue.setUserAge(\"#[userage]\");\n" +
            "executeValue.setCookies(\"#[cookies]\");\n" +
            "executeValue.setValues(StringUtil.changeString(\"#[values]\"));\n" +
            "executeValue.setTimeout(\"#[timeout]\");\n" +
            "return (#[returntype])swcjExecute.execute(executeValue, corn).toArray(new #[returntype]{});\n" +
            "} catch (Exception e) {\n" +
            "e.printStackTrace();\n" +
            "}\n" +
            "return null;\n";
    public static Object getObjectFromTool(String className) {
        return CacheCorn.getObject(className);
    }
    public static SWCJClass getSWCJClass(RootReptile rr) throws ConfigException, EmptyMatchMethodException, ClassNotFoundException {
        SWCJClass sclass = new SWCJClass();
        //获取所有方法
        List<ReptileUrl> rus = rr.getRu();
        throwsException(rus);
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        sclass.setClassName(name);
        sclass.setItIterface(rr.getParentInter());
        //效验接口是否有方法,并返回方法名
        try {
            getFunction(sclass);
        } catch (ClassNotFoundException e) {
            throw new ConfigException("你的接口不存在：" + rr.getParentInter());
        }
        if (sclass.getMethods() == null || sclass.getMethods().size() == 0) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        return sclass;
    }
    private static void throwsException(List<ReptileUrl> rus) throws ConfigException {
        for (ReptileUrl url : rus) {
            if(url.getUrl()==null||url.getUrl().equals("")){
                throw new ConfigException("你的path未配置,在"+url.getName());
            }
            if(url.getParseProgram()==null||url.getParseProgram().equals("")){
                throw new ConfigException("你的策略未配置,在"+url.getName());
            }
        }
    }
    private static void getFunction(SWCJClass swcjClass) throws ClassNotFoundException {
        Class<?> ca = Class.forName(swcjClass.getItIterface());
        Method[] methods = ca.getMethods();
        for (Method method : methods) {
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
    public static String spliceMethod(ReptileUrl ru, RootReptile rr, SWCJMethod method,ReptileConfig rc) {
        StringBuilder sb = new StringBuilder();
        //方法体
        StringBuilder sbmethod = new StringBuilder();
        //获取参数传入列表
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
            }
        } else {
            System.err.println("SWCJ:警告：你的接口有部分参数没有用到,方法:" + ru.getName());
            for (int i = 0; i < len; i++) {
                add(vars, vars1.get(i), " ", split2[i], ",");
            }
            for (int i = len; i < vars1.size(); i++) {
                add(vars, vars1.get(i), " args", i, ",");
            }
        }
        String varString = "";
        if (vars.length() != 0) {
            varString = vars.substring(0, vars.lastIndexOf(","));
        }

        //方法头 定义被重写
        add(sbmethod, "\npublic ", method.getReturnType(), (" "), method.getMethodName(), "(", varString, "){");
        //开始拼接方法
        {
            String in = ru.getParseProgram().replace("\\","\\\\").replace("\"","\\\"").replace("\n","");
            String s = Template.replace("#[method]", in)
                    .replace("#[isHtml]", String.valueOf(ru.isHtml()))
                    .replace("#[type]", ru.getRequestType())
                    .replace("#[url]", ru.getUrl())
                    .replace("#[returntype]", method.getReturnType())
                    .replace("#[userage]", rc.getUserAgents().get(new Random().nextInt(rc.getUserAgents().size())))
                    .replace("#[cookies]", rr.getCookies())
                    .replace("#[values]", ru.getValues())
                    .replace("#[timeout]", rc.getTimeout() + "")
                    .replace("#[class]",ru.getExecutClassName());
            add(sbmethod,s);
        }
        add(sbmethod,"}");
        //注入
        if (len != 0) {
            for (int i = 0; i < len; i++) {
                //判断，使未使用的name不被注入
                if (sbmethod.indexOf("#{" + split2[i] + "}") != -1) {
                    sbmethod.insert(sbmethod.indexOf("#{" + split2[i] + "}") + 3 + split2[i].length(), "\".replace(\"#{" + split2[i] + "}\"," + split2[i] + "+\"\")+\"");
                } else {
                    System.err.println("SWCJ:你的" + split2[i] + "参数并未使用，请检查是否需要");
                }
            }
        }
        sb.append(sbmethod);
        return sb.toString();
    }
}
