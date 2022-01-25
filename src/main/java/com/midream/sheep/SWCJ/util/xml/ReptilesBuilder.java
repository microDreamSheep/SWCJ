package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Annotation.WebSpider;
import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.cache.CacheCorn;
import com.midream.sheep.SWCJ.data.Constant;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.ReptilePaJsoup;
import com.midream.sheep.SWCJ.data.swc.ReptileUrl;
import com.midream.sheep.SWCJ.data.swc.RootReptile;
import com.midream.sheep.SWCJ.util.classLoader.SWCJClassLoader;
import com.midream.sheep.SWCJ.util.io.ISIO;
import com.midream.sheep.SWCJ.util.io.SIO;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("all")
public class ReptilesBuilder implements ReptilesBuilderInter {
    private static final ISIO sio;
    private static final SWCJClassLoader swcjcl;

    static {
        sio = new SIO();
        swcjcl = new SWCJClassLoader();
    }

    @Override
    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<ReptileUrl> rus = rr.getRu();
        //获取类名
        String name = "a" + UUID.randomUUID().toString().replace("-", "");
        //效验池中是否存在
        Object object = getObject(rr.getId());
        if (object != null) {
            return object;
        }
        //效验接口是否有方法,并返回方法名
        Map<String,String> function = null;
        try {
            function = getFunction(rr.getParentInter());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (function == null || function.size()==0) {
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
                    StringBuilder usreAgent = new StringBuilder("");
                    usreAgent.append("private static String[] userAgent = new String[]{");
                    for (int i = 0; i < rc.getUserAgents().size(); i++) {
                        usreAgent.append("\"").append(rc.getUserAgents().get(i)).append("\"").append((i + 1 != rc.getUserAgents().size()) ? "," : "};");
                    }
                    sb.append(usreAgent.toString()).append("\n");
                }
                {
                    for (ReptileUrl reptileUrl : rus) {
                        String s = function.get(reptileUrl.getName());
                        if(s==null){
                        continue;
                        }
                        spliceMethod(sb,reptileUrl,rr,s);
                    }
                }
            }

            //类封口
            sb.append("\n}");
            //实例化文件类
            File javaFile = new File(rc.getWorkplace() + "//" + name + ".java");
            //输出到工作空间
            sio.outPutString(sb.toString(), javaFile);
            //编译类
            String s = swcjcl.compileJavaFile(javaFile);
            //实例化文件类
            File classFile = new File(s);
            //加载类
            Class<?> aClass = swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + name, s);
            Object webc = aClass.getDeclaredConstructor().newInstance();
            if (rc.isCache()) {
                //是缓存则进入对象池
                CacheCorn.addObject(rr.getId(), webc);
                classFile.delete();
            } else {
                //非缓存则进入路径池
                CacheCorn.addPath(rr.getId(), s);
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
                sbmethod.append("java.util.Map<String,String> map = new java.util.HashMap<>();");
                //开始注入cookie值
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    sbmethod.append("map.put(\"").append(stringStringEntry.getKey()).append("\",\"").append(stringStringEntry.getValue()).append("\");\n");
                }
            }
        }
        //搭建主要的方法体
        {
            //获取方法主体的类
            String map = (rr.getCookies() != null&!rr.getCookies().equals("")) ? ".cookies(map)" : "";
            sbmethod.append("\norg.jsoup.nodes.Document document = org.jsoup.Jsoup.connect(\"").append(ru.getUrl()).append("\").ignoreContentType(true).timeout(timeout)\n").append(map).append(".userAgent(userAgent[(int) (Math.random()*userAgent.length)]).").append(ru.getRequestType().equals("POST") ? "post" : "get").append("();");
            if (ru.getReg() != null && !ru.getReg().equals("")) {
                //进入正则表达式方法
                sbmethod.append("String text = document.").append(ru.isHtml() ? "html" : "text").append("();\n").append("java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(\"").append(ru.getReg()).append("\");\n").append("java.util.regex.Matcher matcher = pattern.matcher(text);\n");
                if (ru.getReturnType().equals(stringBody) || ru.getReturnType().equals("java.lang.String") || ru.getReturnType().equals("")) {
                    sbmethod.append("matcher.find();\n" +
                            "String result =  matcher.group();\n");
                } else if (ru.getReturnType().equals("String[]") || ru.getReturnType().equals("java.lang.String[]")) {
                    sbmethod.append("matcher.find();\n" +
                            "int i = matcher.groupCount();\n" +
                            "String[] result = new String[i];\n" +
                            "for(int d = 0;d<i;d++){\n" +
                            "result[d] = matcher.group(d);\n" +
                            "}");
                    sbmethod.append("return result;\n");
                }
            } else if (ru.getJsoup() != null) {
                //拼接jsoup
                {
                    //一级查询
                    ReptilePaJsoup rpj = ru.getJsoup().get(0);
                    sbmethod.append("\njava.util.List<String> list = new java.util.ArrayList<>();\n");
                    sbmethod.append("org.jsoup.select.Elements select = document.select(\"").append(rpj.getPaText()).append("\");\n").append("for (int i = 0;i<select.size();i++) {\norg.jsoup.nodes.Element element = select.get(i);");

                    String element = "element";
                    //开始循环
                    String string = element;//命名空间
                    String end = element;
                    for (int i = 1; i < ru.getJsoup().size(); i++) {
                        String uuid = UUID.randomUUID().toString().replace("-", "");
                        sbmethod.append("org.jsoup.select.Elements elementi").append(i).append(" = ").append(string).append(".select(\"").append(ru.getJsoup().get(i).getPaText()).append("\");\n");
                        sbmethod.append("for(int " + "c").append(uuid).append(" = 0;c").append(uuid).append("<elementi").append(i).append(".size();c").append(uuid).append("++) {\norg.jsoup.nodes.Element element").append(i + 2).append(" = elementi").append(i).append(".get(c" + uuid + ");");
                        string = element + (i + 2);
                        end = string;
                    }
                    sbmethod.append("list.add(").append(end).append(ru.isHtml() ? ".html" : ".text").append("());\n");
                    //添加括号
                    for (int i = 0; i < ru.getJsoup().size(); i++) {
                        sbmethod.append("}\n");
                    }
                    //返回数据
                    sbmethod.append("String[] result = list.toArray(new String[]{});");
                    if (ru.getReturnType().equals(stringBody) || ru.getReturnType().equals("java.lang.String")) {
                        sbmethod.append("return result[0];");
                    } else {
                        sbmethod.append("return result;");
                    }
                }
            }
        }
        sbmethod.append("}catch (Exception e){\ne.printStackTrace();\n}\nreturn null;\n}");
        if(ru.getInPutName()!=null&!ru.getInPutName().equals("")) {
            sbmethod.insert(sbmethod.indexOf("#{" + ru.getInPutName() + "}") + 3 + ru.getInPutName().length(), "\".replace(\"#{" + ru.getInPutName() + "}\"," + ru.getInPutName() + "+\"\")+\"");
        }
        sb.append(sbmethod);
    }
}