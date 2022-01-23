package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Annotation.WebSpider;
import com.midream.sheep.SWCJ.Exception.ConflictException;
import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.ReptilePaJsoup;
import com.midream.sheep.SWCJ.data.swc.ReptileUrl;
import com.midream.sheep.SWCJ.data.swc.RootReptile;
import com.midream.sheep.SWCJ.util.classLoader.SWCJClassLoader;
import com.midream.sheep.SWCJ.util.javaassist.Assist;
import com.midream.sheep.SWCJ.util.javaassist.IAssist;
import javassist.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReptilesBuilder implements ReptilesBuilderInter{
    private static IAssist assist;
    private static SWCJClassLoader swcjcl;
    static{
        assist = new Assist();
        swcjcl = new SWCJClassLoader();
    }
    @Override
    public Object reptilesBuilder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException {
        //效验接口是否有方法,并返回方法名
        String s1 = null;
        try {
            s1 = checkFunction(rr.getParentInter(), rr.getId());
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(s1==null||s1.equals("")){
                throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        CtClass ctClass = null;
        String name = UUID.randomUUID().toString().replace("-","");
        String className = "com.midream.sheep"
                +"."+ name;
        try{
            //获取类池
            ClassPool classPool = assist.getClassPool();
            //获取随机名，防止重复
            ctClass = classPool.makeClass(className
            );
            //实现接口
            ctClass.addInterface(classPool.get(rr.getParentInter()));
            //拼接方法体
            StringBuilder sb = new StringBuilder();
            boolean isOut = false;
            //拼接方法
            {
                //方法头 定义被重写
                sb.append("\npublic "+((rr.getReturnType().equals("String")||rr.getReturnType().equals("java.lang.String"))?"String":rr.getReturnType())+" "+s1+"("+(rr.getInPutType().equals("")?"":rr.getReturnType()+" "+rr.getInPutName())+"){"+"\n" +
                        "try{");
                //搭建全局静态属性
                {
                    //timeout
                    CtField timeout=CtField.make("private int timeout = "+rc.getTimeout()+";", ctClass);
                    ctClass.addField(timeout);
                    //userAgent数组创建
                    StringBuffer usreAgent = new StringBuffer();
                    usreAgent.append("private String[] userAgent = new String[]{");
                    for (int i = 0;i<rc.getUserAgents().size();i++) {
                        usreAgent.append("\""+rc.getUserAgents().get(i)+"\""+((i+1!=rc.getUserAgents().size())?",":"};"));
                    }
                    ctClass.addField(CtField.make(usreAgent.toString(),ctClass));
                }
                //搭建局部变量
                {
                    if(!(rr.getCookies().equals(""))&&!(rr.getCookies()==null)) {
                        //cookie字典
                        Map<String,String> map = new HashMap<>();
                        //取出cookies值
                        String cookie = rr.getCookies();
                        String[] split = cookie.split(";");
                        for (String s : split) {
                            String[] split1 = s.split("=");
                            map.put(split1[0],split1[1]);
                        }
                        //拼接cookie接受map
                        sb.append("java.util.Map/*<String,String>*/ map = new java.util.HashMap/*<>*/();");
                        //开始注入cookie值
                        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                            sb.append("map.put(\""+stringStringEntry.getKey()+"\",\""+stringStringEntry.getValue()+"\");\n");
                        }
                    }
                }
                //搭建主要的方法体
                {
                    //获取方法主体的类
                    ReptileUrl ru = rr.getRu();
                    String map = (!rr.getCookies().equals("")||rr.getCookies()!=null)?".cookies(map)":"";
                    sb.append("\norg.jsoup.nodes.Document document = org.jsoup.Jsoup.connect(\""+ru.getUrl()+"\").ignoreContentType(true).timeout(timeout)\n"
                            +map+".userAgent(userAgent[(int) (Math.random()*userAgent.length)])."
                            +(ru.getRequestType().equals("POST")?"post":"get")+"();");
                    if(ru.getReg()!=null&&!ru.getReg().equals("")){
                        //进入正则表达式方法
                        sb.append("String text = document."+(ru.isHtml()?"html":"text")+"();\n"+"java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(\""+ru.getReg()+"\");\n" +
                                "java.util.regex.Matcher matcher = pattern.matcher(text);\n");
                        if(rr.getReturnType().equals("String")||rr.getReturnType().equals("java.lang.String")||rr.getReturnType().equals("")){
                            sb.append("matcher.find();\n" +
                                    "String result =  matcher.group();\n");
                        }else if(rr.getReturnType().equals("String[]")||rr.getReturnType().equals("java.lang.String[]")){
                            sb.append("matcher.find();\n" +
                                    "int i = matcher.groupCount();\n" +
                                    "String[] result = new String[i];\n" +
                                    "for(int d = 0;d<i;d++){\n" +
                                    "result[d] = matcher.group(d);\n" +
                                    "}");
                        }
                    }else if(ru.getJsoup()!=null){
                        //拼接jsoup
                        {
                            //一级查询
                            ReptilePaJsoup rpj = ru.getJsoup().get(0);
                            sb.append("\njava.util.List/*<String>*/ list = new java.util.ArrayList/*<>*/();\n");
                            sb.append("org.jsoup.select.Elements select = document.select(\""+rpj.getPaText()+"\");\n" +
                                    "for (int i = 0;i<select.size();i++) {\norg.jsoup.nodes.Element element = select.get(i);");
                            //开始循环
                            String string = "element";//命名空间
                            String end = "element";
                            for(int i = 1;i<ru.getJsoup().size();i++){
                                sb.append("org.jsoup.select.Elements element"+i+" = "+string+".select(\""+ru.getJsoup().get(i).getPaText()+"\");\n");
                                sb.append("for(int a = 0;a<element"+i+".size();a++) {\norg.jsoup.nodes.Element element"+(i+1)+" = element"+i+".get(a);");
                                string = "element"+(i+1);
                                end = string;
                            }
                            sb.append("list.add("+end+(ru.isHtml()?".html":".text")+"());\n");
                            //添加括号
                            for(int i = 0;i<ru.getJsoup().size();i++){
                                sb.append("}\n");
                            }
                            //返回数据
                            sb.append("Object[] result = list.toArray();\n");
                        }
                    }
                    sb.append("return result;\n");
                }
                isOut = true;
            }
            sb.append("}catch (Exception e){\ne.printStackTrace();\n}\nreturn null;\n}");
            String method = sb.toString();
            CtMethod make = CtMethod.make(method, ctClass);
            ctClass.addMethod(make);
            Class<?> aClass = null;
            if(!isOut) {
                if (rc.isCache()) {
                    byte[] bytes = ctClass.toBytecode();
                    aClass = swcjcl.loadData(className, bytes);
                } else {
                    ctClass.writeFile(rc.getWorkplace());
                    aClass = swcjcl.loadData(className, rc.getWorkplace() + "/" + className.replace(".", "//") + ".class");
                }
                ctClass.detach();
                if (aClass != null) {
                    return aClass.getDeclaredConstructor().newInstance();
                }
            }else{
                if(rc.isCache()){
                    throw new ConflictException("There is no cache in the output mode(输出模式不存在缓存)");
                }
                ctClass.writeFile(rc.getWorkplace());
                ctClass.detach();
                return rc.getWorkplace();
            }
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }finally {
        }
        return null;
    }
    public String checkFunction(String className,String id) throws NotFoundException, ClassNotFoundException {
        Class<?> ca = Class.forName(className);
        Method[] methods = ca.getMethods();
        for (Method method : methods) {
            if(method.getAnnotation(WebSpider.class).value().equals(id)){
                return method.getName();
            }
        }
        return "";
    }
}