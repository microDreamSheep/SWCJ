package com.midream.sheep.swcj.util.xml;

import com.midream.sheep.swcj.Annotation.WebSpider;
import com.midream.sheep.swcj.Exception.*;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.swc.*;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.util.classLoader.SWCJClassLoader;
import com.midream.sheep.swcj.util.function.StringUtil;
import com.midream.sheep.swcj.util.io.*;
import com.midream.sheep.swcj.data.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class ReptilesBuilder implements ReptilesBuilderInter {
    private static final ISIO sio;
    private static final SWCJClassLoader swcjcl;

    static {
        sio = new SIO();
        swcjcl = new SWCJClassLoader();
    }

    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        //java源文件
        File javaFile = null;
        //class 源文件
        File classFile = null;
        //获取所有方法
        List<ReptileUrl> rus = rr.getRu();
        throwsException(rus);
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        //效验池中是否存在,如果存在直接返回
        Object object = null;
        try {
            object = getObject(rr.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (object != null) {
            return object;
        }
        //效验接口是否有方法,并返回方法名
        Map<String,SWCJMethod> function = new HashMap<>();
        try {
            getFunction(rr.getParentInter(),function);
        } catch (ClassNotFoundException e) {
                throw new ConfigException("你的接口不存在："+rr.getParentInter());
        }
        if (function.size()==0) {
                throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        try {
            //拼接类
            StringBuilder sb = new StringBuilder();
            //增加包名
            sb.append("package ").append(Constant.DEFAULT_PACKAGE_NAME).append(";\n");
            //拼接类名
            sb.append("public class ").append(name).append(" implements ").append(rr.getParentInter()).append(" {");
            //拼接方法
            {
                //搭建全局静态属性
                {
                    //timeout
                    sb.append("private static int timeout = ").append(rc.getTimeout()).append(";\n");
                    //userAgent数组创建
                    StringBuilder usreAgent = new StringBuilder();
                    usreAgent.append("private static String[] userAgent = new String[]{");
                    for (int i = 0; i < rc.getUserAgents().size(); i++) {
                        usreAgent.append("\"").append(rc.getUserAgents().get(i)).append("\"").append((i + 1 != rc.getUserAgents().size()) ? "," : "};");
                    }
                    sb.append(usreAgent).append("\n");
                }
                {
                    for (ReptileUrl reptileUrl : rus) {
                        SWCJMethod s = function.get(reptileUrl.getName());
                        if(s==null){
                            continue;
                        }
                        if(s.getAnnotation()!=null&&!s.getAnnotation().equals("")) {
                            spliceMethod(sb, reptileUrl, rr, s);
                            function.remove(reptileUrl.getName());
                        }
                    }
                    if(function.size()!=0){
                        throw new InterfaceIllegal("IllMethod(可能你的方法没有与配置文件对应)");
                    }
                }
            }

            //类封口
            sb.append("\n}");
            //实例化文件类
            javaFile = new File(rc.getWorkplace() + "//" + name + ".java");
            //输出到工作空间
            sio.outPutString(sb.toString(), javaFile);
            //编译类
            String s = swcjcl.compileJavaFile(javaFile);
            //实例化文件类
            classFile = new File(s);
            //加载类
            Class<?> aClass = swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + name, s);
            Object webc = aClass.getDeclaredConstructor().newInstance();
            if (rc.isCache()) {
                //是缓存则进入对象池
                CacheCorn.addObject(rr.getId(), webc);
            } else {
                //非缓存则进入路径池
                CacheCorn.addPath(rr.getId(),name);
            }
            //返回类
            return webc;
        } catch (IOException|InvocationTargetException|InstantiationException|IllegalAccessException|NoSuchMethodException e) {
            System.err.println("类加载异常");
        }finally {
            if(javaFile!=null) {
                //删掉java原文件
                javaFile.delete();
            }
            if(classFile!=null) {
                classFile.delete();
            }
        }
        return null;
    }

    @Override
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

    @Override
    public Object getObject(String Key) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Object ob = CacheCorn.getObject(Key);
        if (ob != null) {
            return ob;
        }
        String path = CacheCorn.getPath(Key);
        if (path != null) {
            String name = Constant.DEFAULT_PACKAGE_NAME + "." + path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf("."));
            return swcjcl.findClass(name).getDeclaredConstructor().newInstance();
        }
        return null;
    }

    private void spliceMethod(StringBuilder sb, ReptileUrl ru, RootReptile rr,SWCJMethod method) throws ConfigException {
        //方法体
        StringBuilder sbmethod = new StringBuilder();
        String stringBody = "String";
        boolean isQuote = false;
        //获取参数传入列表
        StringBuilder vars = new StringBuilder();
        List<String> vars1 = method.getVars();
        String[] split2 = ru.getInPutName().split(",");
        int len = split2.length;
        if(ru.getInPutName().equals("")){
            len = 0;
        }
        if(len>vars1.size()){
            try {
                throw new InterfaceIllegal("方法参数不统一");
            } catch (InterfaceIllegal interfaceIllegal) {
                interfaceIllegal.printStackTrace();
            }
        }else if(len==vars1.size()&&len!=0){
            for(int i = 0;i< split2.length;i++){
                vars.append(vars1.get(i)).append(" ").append(split2[i]).append(",");
            }
        }else {
            System.err.println("SWCJ:警告：你的接口有部分参数没有用到,方法:"+ru.getName());
            for(int i = 0;i<len;i++){
                vars.append(vars1.get(i)).append(" ").append(split2[i]).append(",");
            }
            for(int i = len;i<vars1.size();i++){
                vars.append(vars1.get(i)).append(" args").append(i).append(",");
            }
        }
        String varString = "";
        if(vars.length()!=0) {
            varString = vars.substring(0, vars.lastIndexOf(","));
        }
        //方法头 定义被重写
        sbmethod.append("\npublic ").append(method.getReturnType()).append(" ").append(method.getMethodName()).append("(").append(varString).append("){").append("\n").append("try{");
        //搭建局部变量
        String StringMap = "A"+UUID.randomUUID().toString().replace("-","");
        {
            if (!(rr.getCookies().equals("")) && (rr.getCookies() != null)) {
                //cookie字典
                Map<String, String> map = new HashMap<>();
                //取出cookies值
                String cookie = rr.getCookies();
                String[] split = cookie.split(";");
                for (String s : split) {
                    String[] split1 = s.split("=");
                    map.put(split1[0], split1[1]);
                }
                //拼接cookie接受map
                sbmethod.append("java.util.Map<String,String> ").append(StringMap).append(" = new java.util.HashMap<>();");
                //开始注入cookie值
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    sbmethod.append(StringMap).append(".put(\"").append(stringStringEntry.getKey()).append("\",\"").append(stringStringEntry.getValue()).append("\");\n");
                }
            }
        }
        //搭建主要的方法体
        {
            //获取方法主体的类
            String map = (!Objects.requireNonNull(rr.getCookies()).equals("")) ? ".cookies("+StringMap+")" : "";
            sbmethod.append("\norg.jsoup.nodes.Document document = org.jsoup.Jsoup.connect(\"").append(ru.getUrl()).append("\").ignoreContentType(true).timeout(timeout)\n").append(map).append(".userAgent(userAgent[(int) (Math.random()*userAgent.length)]).").append(ru.getRequestType()!=null&&ru.getRequestType().equals("POST") ? "post" : "get").append("();");
            if (ru.getReg() != null && !ru.getReg().equals("")) {
                //进入正则表达式方法
                sbmethod.append("String text = document.").append(ru.isHtml() ? "html" : "text").append("();\n").append("java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(\"").append(ru.getReg()).append("\");\n").append("java.util.regex.Matcher matcher = pattern.matcher(text);\n");
                if (method.getReturnType().equals(stringBody) || method.getReturnType().equals("java.lang.String") || method.getReturnType().equals("")) {
                    sbmethod.append("matcher.find();\n" +
                            "String SWCJresult =  matcher.group();\n");
                } else if (method.getReturnType().equals("String[]") || method.getReturnType().equals("java.lang.String[]")) {
                    sbmethod.append("matcher.find();\n" +
                            "int i = matcher.groupCount();\n" +
                            "String[] SWCJresult = new String[i];\n" +
                            "for(int d = 0;d<i;d++){\n" +
                            "SWCJresult[d] = matcher.group(d);\n" +
                            "}");
                    sbmethod.append("return SWCJresult;\n");
                }
            } else if (ru.getJsoup().get(0).getJsoup() != null) {
                if(method.getReturnType().equals("String")||method.getReturnType().equals("String[]")) {
                    sbmethod.append("String[] SWCJresult = null;");
                }else {
                    isQuote = true;
                    for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                        if(jsoup.getName()!=null&&!jsoup.getName().equals("")){
                            sbmethod.append("java.util.List<String> ").append(jsoup.getName()).append("= new java.util.LinkedList<>();");
                        }else {
                            throw new ConfigException("jsoup name为空");
                        }
                    }
                }
                //拼接jsoup
                for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                    spliceMethodJsoup(sbmethod,jsoup,ru,isQuote);
                }
            }
        }
        if(!isQuote) {//不是引用类型返回字符串
            if (method.getReturnType().equals(stringBody) || method.getReturnType().equals("java.lang.String")) {
                sbmethod.append("return SWCJresult[0];");
            } else {
                sbmethod.append("return SWCJresult;");
            }
        }else {//是引用类型就注入数据
            sbmethod.append("int[] casdsad = {");
            for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                sbmethod.append(jsoup.getName()).append(".size(),");
            }
            sbmethod.substring(0, sbmethod.lastIndexOf(","));
            sbmethod.append("};");
            sbmethod.append("java.util.Arrays.sort(casdsad);\n" +
                    "int maxawdwa = casdsad[casdsad.length-1];");
            sbmethod.append("java.util.List<").append(method.getReturnType().replace("[]","")).append(">").append("lists").append("= new java.util.LinkedList<>();");
            sbmethod.append("for(int i = 0;i<maxawdwa;i++){");
            //开始执行反射生成数据
            sbmethod.append("Class<?> aClass = Class.forName(\"" + method.getReturnType().replace("[]","") + "\");");
            sbmethod.append("Object o = aClass.getDeclaredConstructor().newInstance();");
            for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                sbmethod.append("try{");
                sbmethod.append("aClass.getDeclaredMethod(\"set" + StringUtil.StringToUpperCase(jsoup.getName()) + "\", String.class).invoke(o," + jsoup.getName() + ".get(i));");
                sbmethod.append("}catch(Exception e){}");
            }
            sbmethod.append("lists.add((").append(method.getReturnType().replace("[]","")).append(")o").append(");");
            sbmethod.append("}");
            sbmethod.append("return lists.toArray(new "+method.getReturnType()+"{});");
        }
        sbmethod.append("}catch (Exception e){\ne.printStackTrace();\n}\nreturn null;\n}");
        if(len!=0) {
            for(int i = 0;i<len;i++) {
                //判断，使未使用的name不被注入
                if(sbmethod.indexOf("#{" + split2[i] + "}")!=-1) {
                    sbmethod.insert(sbmethod.indexOf("#{" + split2[i] + "}") + 3 + split2[i].length(), "\".replace(\"#{" + split2[i] + "}\"," + split2[i] + "+\"\")+\"");
                }else {
                    System.err.println("SWCJ:你的"+split2[i]+"参数并未使用，请检查是否需要");
                }
            }
        }
        sb.append(sbmethod);
    }
    private void throwsException(List<ReptileUrl> rus) throws ConfigException {
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
    private void spliceMethodJsoup(StringBuilder sbmethod,ReptileCoreJsoup rcj,ReptileUrl ru,boolean isQuote){
        sbmethod.append("{");
        List<ReptilePaJsoup> jsoup = rcj.getJsoup();
        int bigParanthesesCount = jsoup.size();
        //一级查询
        ReptilePaJsoup rpj = jsoup.get(0);
        sbmethod.append("\njava.util.List<String> SWCJlist = new java.util.ArrayList<>();\nboolean owdad = true;");
        sbmethod.append("org.jsoup.select.Elements select = document.select(\"").append(rpj.getPaText()).append("\");\n").append("for (int i = 0;i<select.size();i++) {\norg.jsoup.nodes.Element element = select.get(i);");
        if(jsoup.get(0).getStep()!=0){
            sbmethod.append("if(owdad){i+=").append(jsoup.get(0).getStep()).append(";owdad=false;}");
        }
        String element = "element";
        if(jsoup.get(0).getNot()!=null){
            sbmethod.append("if(1==1");
            for (String s : jsoup.get(0).getNot()) {
                if(!s.equals("")){
                    sbmethod.append("&&!element").append(".text().equals(\"").append(s).append("\")");
                }
            }
            sbmethod.append("){");
            bigParanthesesCount++;
        }
        //开始循环
        String string = element;//命名空间
        String end = element;
        for (int i = 1; i < jsoup.size(); i++) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            sbmethod.append("boolean asd = true;org.jsoup.select.Elements elementi").append(i).append(" = ").append(string).append(".select(\"").append(jsoup.get(i).getPaText()).append("\");\n");
            sbmethod.append("for(int " + "c").append(uuid).append(" = 0;c").append(uuid).append("<elementi").append(i).append(".size();c").append(uuid).append("++) {\n");
            if(jsoup.get(i).getStep()!=0){
                sbmethod.append("if(asd){c").append(uuid).append("+=").append(jsoup.get(i).getStep()).append(";asd=false;}");
            }
            sbmethod.append("org.jsoup.nodes.Element element").append(i + 2).append(" = elementi").append(i).append(".get(c").append(uuid).append(");");
            if(jsoup.get(i).getNot()!=null){
                sbmethod.append("if(1==1");
                for (String s : jsoup.get(i).getNot()) {
                    if(!s.equals("")){
                        sbmethod.append("&&!element").append(i+2).append(".text().equals(\"").append(s).append("\")");
                    }
                }
                bigParanthesesCount++;
                sbmethod.append("){");
            }
            string = element + (i + 2);
            end = string;
        }
        if(jsoup.get(jsoup.size()-1).getElement()!=null&&!jsoup.get(jsoup.size()-1).getElement().equals("")) {
            sbmethod.append("SWCJlist.add(").append(end).append(".attr(\"").append(jsoup.get(jsoup.size()-1).getElement()).append("\"));");
        }else {
            sbmethod.append("SWCJlist.add(").append(end).append(ru.isHtml() ? ".html" : ".text").append("());\n");
        }
        //添加括号
        for (int i = 0; i < bigParanthesesCount; i++) {
            sbmethod.append("}\n");
        }
        //返回数据
        if(!isQuote) {
            sbmethod.append("SWCJresult = SWCJlist.toArray(new String[]{});");
        }else{
            sbmethod.append(rcj.getName()).append("=SWCJlist;");
        }
        sbmethod.append("}");
    }
}