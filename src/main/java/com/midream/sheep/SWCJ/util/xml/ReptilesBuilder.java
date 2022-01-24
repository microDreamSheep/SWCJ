package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Annotation.WebSpider;
import com.midream.sheep.SWCJ.Exception.ConflictException;
import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.Constant;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.ReptilePaJsoup;
import com.midream.sheep.SWCJ.data.swc.ReptileUrl;
import com.midream.sheep.SWCJ.data.swc.RootReptile;
import com.midream.sheep.SWCJ.util.classLoader.SWCJClassLoader;
import com.midream.sheep.SWCJ.util.io.ISIO;
import com.midream.sheep.SWCJ.util.io.SIO;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReptilesBuilder implements ReptilesBuilderInter {
    private static ISIO sio;
    private static SWCJClassLoader swcjcl;

    static {
        sio = new SIO();
        swcjcl = new SWCJClassLoader();
    }

    @Override
    public Object reptilesBuilder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException {
        //效验接口是否有方法,并返回方法名
        String s1 = null;
        try {
            s1 = checkFunction(rr.getParentInter(), rr.getId());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (s1 == null || s1.equals("")) {
            throw new EmptyMatchMethodException("EmptyMatchMethodException(空匹配方法异常)");
        }
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        try {
            //拼接类
            StringBuilder sb = new StringBuilder();
            //增加包名
            sb.append("package " + Constant.DEFAULT_PACKAGE_NAME + ";\n");
            //拼接类名
            sb.append("public class " + name + " implements " + rr.getParentInter() + " {");
            //拼接方法
            {
                //搭建全局静态属性
                {
                    //timeout
                    sb.append("private static int timeout = " + rc.getTimeout() + ";\n");
                    //userAgent数组创建
                    StringBuffer usreAgent = new StringBuffer();
                    usreAgent.append("private static String[] userAgent = new String[]{");
                    for (int i = 0; i < rc.getUserAgents().size(); i++) {
                        usreAgent.append("\"" + rc.getUserAgents().get(i) + "\"" + ((i + 1 != rc.getUserAgents().size()) ? "," : "};"));
                    }
                    sb.append(usreAgent.toString() + "\n");
                }
                //方法头 定义被重写
                sb.append("\npublic " + ((rr.getReturnType().equals("String") || rr.getReturnType().equals("java.lang.String")) ? "String" : rr.getReturnType()) + " " + s1 + "(" + (rr.getInPutType().equals("") ? "" : rr.getReturnType() + " " + rr.getInPutName()) + "){" + "\n" +
                        "try{");
                //搭建局部变量
                {
                    if (!(rr.getCookies().equals("")) && !(rr.getCookies() == null)) {
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
                        sb.append("java.util.Map<String,String> map = new java.util.HashMap<>();");
                        //开始注入cookie值
                        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                            sb.append("map.put(\"" + stringStringEntry.getKey() + "\",\"" + stringStringEntry.getValue() + "\");\n");
                        }
                    }
                }
                //搭建主要的方法体
                {
                    //获取方法主体的类
                    ReptileUrl ru = rr.getRu();
                    String map = (!rr.getCookies().equals("") || rr.getCookies() != null) ? ".cookies(map)" : "";
                    sb.append("\norg.jsoup.nodes.Document document = org.jsoup.Jsoup.connect(\"" + ru.getUrl() + "\").ignoreContentType(true).timeout(timeout)\n"
                            + map + ".userAgent(userAgent[(int) (Math.random()*userAgent.length)])."
                            + (ru.getRequestType().equals("POST") ? "post" : "get") + "();");
                    if (ru.getReg() != null && !ru.getReg().equals("")) {
                        //进入正则表达式方法
                        sb.append("String text = document." + (ru.isHtml() ? "html" : "text") + "();\n" + "java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(\"" + ru.getReg() + "\");\n" +
                                "java.util.regex.Matcher matcher = pattern.matcher(text);\n");
                        if (rr.getReturnType().equals("String") || rr.getReturnType().equals("java.lang.String") || rr.getReturnType().equals("")) {
                            sb.append("matcher.find();\n" +
                                    "String result =  matcher.group();\n");
                        } else if (rr.getReturnType().equals("String[]") || rr.getReturnType().equals("java.lang.String[]")) {
                            sb.append("matcher.find();\n" +
                                    "int i = matcher.groupCount();\n" +
                                    "String[] result = new String[i];\n" +
                                    "for(int d = 0;d<i;d++){\n" +
                                    "result[d] = matcher.group(d);\n" +
                                    "}");
                        }
                    } else if (ru.getJsoup() != null) {
                        //拼接jsoup
                        {
                            //一级查询
                            ReptilePaJsoup rpj = ru.getJsoup().get(0);
                            sb.append("\njava.util.List/*<String>*/ list = new java.util.ArrayList/*<>*/();\n");
                            sb.append("org.jsoup.select.Elements select = document.select(\"" + rpj.getPaText() + "\");\n" +
                                    "for (int i = 0;i<select.size();i++) {\norg.jsoup.nodes.Element element = select.get(i);");
                            //开始循环
                            String string = "element";//命名空间
                            String end = "element";
                            for (int i = 1; i < ru.getJsoup().size(); i++) {
                                sb.append("org.jsoup.select.Elements element" + i + " = " + string + ".select(\"" + ru.getJsoup().get(i).getPaText() + "\");\n");
                                sb.append("for(int a = 0;a<element" + i + ".size();a++) {\norg.jsoup.nodes.Element element" + (i + 1) + " = element" + i + ".get(a);");
                                string = "element" + (i + 1);
                                end = string;
                            }
                            sb.append("list.add(" + end + (ru.isHtml() ? ".html" : ".text") + "());\n");
                            //添加括号
                            for (int i = 0; i < ru.getJsoup().size(); i++) {
                                sb.append("}\n");
                            }
                            //返回数据
                            sb.append("Object[] result = list.toArray();\n");
                        }
                    }
                    sb.append("return result;\n");
                }
            }
            sb.append("}catch (Exception e){\ne.printStackTrace();\n}\nreturn null;\n}");
            //类封口
            sb.append("\n}");
            //输出到工作空间
            sio.outPutString(sb.toString(), new File(rc.getWorkplace() + "//" + name + ".java"));
            //编译类
            String s = swcjcl.compileJavaFile(new File(rc.getWorkplace() + "//" + name + ".java"));
            Class<?> aClass = swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + name, s);
            if (rc.isCache()) {
                new File(rc.getWorkplace() + "//" + name + ".java").exists();
                new File(s).exists();
            }
            if (aClass != null) {
                //返回类
                return aClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public String checkFunction(String className, String id) throws ClassNotFoundException {
        Class<?> ca = Class.forName(className);
        Method[] methods = ca.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(WebSpider.class).value().equals(id)) {
                return method.getName();
            }
        }
        return "";
    }
}