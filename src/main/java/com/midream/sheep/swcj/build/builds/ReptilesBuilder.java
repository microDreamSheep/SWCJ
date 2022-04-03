package com.midream.sheep.swcj.build.builds;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.build.function.AssistTool;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.ReptileCoreJsoup;
import com.midream.sheep.swcj.data.swc.ReptilePaJsoup;
import com.midream.sheep.swcj.data.swc.ReptileUrl;
import com.midream.sheep.swcj.data.swc.RootReptile;
import com.midream.sheep.swcj.pojo.SWCJClass;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.util.classLoader.SWCJClassLoader;
import com.midream.sheep.swcj.build.function.StringUtil;
import com.midream.sheep.swcj.util.io.ISIO;
import com.midream.sheep.swcj.util.io.SIO;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.midream.sheep.swcj.build.function.StringUtil.add;

public class ReptilesBuilder {
    private static final ISIO sio;
    private static final SWCJClassLoader swcjcl;

    static {
        //单例设计模式
        sio = new SIO();
        swcjcl = new SWCJClassLoader();
    }

    public Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal {
        //从池中寻找类
        {
            //效验池中是否存在,如果存在直接返回
            Object object = null;
            try {
                object = AssistTool.getObjectFromTool(rr.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (object != null) {
                return object;
            }
        }
        //开始拼接类信息
        SWCJClass sclass = AssistTool.getSWCJClass(rr, rc);
        try {
            StringBuilder sb = new StringBuilder();
            spliceClass(sclass, rr, sb);
            return loadClass(rr, rc, sclass, sb.toString());
        } catch (IOException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            System.err.println("类加载异常");
        }
        return null;
    }

    private Object loadClass(RootReptile rr, ReptileConfig rc, SWCJClass sclass, String sb) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {
        Class<?> aClass;
        if (rc.isCache()) {
            aClass = swcjcl.compileJavaFile(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), sb);
        } else {
            //实例化文件类
            File javaFile = new File(rc.getWorkplace() + "//" + sclass.getClassName() + ".java");
            //输出到工作空间
            sio.outPutString(sb, javaFile);
            //编译类
            String s = swcjcl.compileJavaFile(javaFile);
            //加载类
            aClass = swcjcl.loadData(Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName(), s);
            boolean delete = javaFile.delete();
            if(!delete){
                System.err.println("文件删除失败");
            }
        }
        Object webc = aClass.getDeclaredConstructor().newInstance();
        if (rc.isCache()) {
            //进入对象池
            CacheCorn.addObject(rr.getId(), webc);
        }
        //返回类
        return webc;
    }

    private void spliceClass(SWCJClass sclass, RootReptile rr, StringBuilder sb) throws InterfaceIllegal, ConfigException {
        //增加包名
        add(sb, "package ", Constant.DEFAULT_PACKAGE_NAME, ";\n");
        //拼接类名
        add(sb, "public class ", sclass.getClassName(), " implements ", rr.getParentInter(), " {");
        //拼接方法
        {
            //搭建全局静态属性
            {
                add(sb, sclass.getValue("int timeout ="));
                add(sb, sclass.getValue("String[] userAgent = new String[]{"), "\n");
            }
            {
                final List<ReptileUrl> rus = rr.getRu();
                Map<String, SWCJMethod> function = sclass.getMethods();
                for (ReptileUrl reptileUrl : rus) {
                    SWCJMethod s = function.get(reptileUrl.getName());
                    if (s != null && s.getAnnotation() != null && !s.getAnnotation().equals("")) {
                        spliceMethod(sb, reptileUrl, rr, s);
                        function.remove(reptileUrl.getName());
                    }
                }
                if (function.size() != 0) {
                    throw new InterfaceIllegal("IllMethod(可能你的方法没有与配置文件对应)");
                }
            }
        }

        //类封口
        add(sb, "\n}");
    }

    private void spliceMethod(StringBuilder sb, ReptileUrl ru, RootReptile rr, SWCJMethod method) throws ConfigException {
        //方法体
        StringBuilder sbmethod = new StringBuilder();
        String stringBody = "String";
        boolean isQuote = false;
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
        } else if (len != 0) {
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
        add(sbmethod, "\npublic ", method.getReturnType(), (" "), method.getMethodName(), "(", varString, "){", "\n", "try{");
        //搭建局部变量
        String StringMap = "A" + UUID.randomUUID().toString().replace("-", "");
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
                add(sbmethod, "java.util.Map<String,String> ", StringMap, " = new java.util.HashMap<>();");
                //开始注入cookie值
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    add(sbmethod, StringMap, ".put(\"", stringStringEntry.getKey(), "\",\"", stringStringEntry.getValue(), "\");\n");
                }
            }
        }
        //搭建主要的方法体
        {
            //获取方法主体的类
            String map = (!Objects.requireNonNull(rr.getCookies()).equals("")) ? ".cookies(" + StringMap + ")" : "";
            add(sbmethod, "\norg.jsoup.nodes.Document document = org.jsoup.Jsoup.connect(\"", ru.getUrl(), "\").ignoreContentType(true).timeout(timeout)\n", map, ".userAgent(userAgent[(int) (Math.random()*userAgent.length)]).", ru.getRequestType() != null && ru.getRequestType().equals("POST") ? "post" : "get", "();");
            if (ru.getReg() != null && !ru.getReg().equals("")) {
                //进入正则表达式方法
                add(sbmethod, "String text = document.", ru.isHtml() ? "html" : "text", "();\n", "java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(\"", ru.getReg(), "\");\n", "java.util.regex.Matcher matcher = pattern.matcher(text);\n");
                if (method.getReturnType().equals(stringBody) || method.getReturnType().equals("java.lang.String") || method.getReturnType().equals("")) {
                    add(sbmethod, "matcher.find();\n",
                            "String SWCJresult =  matcher.group();\n");
                } else if (method.getReturnType().equals("String[]") || method.getReturnType().equals("java.lang.String[]")) {
                    add(sbmethod, "matcher.find();\n",
                            "int i = matcher.groupCount();\n",
                            "String[] SWCJresult = new String[i];\n",
                            "for(int d = 0;d<i;d++){\n",
                            "SWCJresult[d] = matcher.group(d);\n",
                            "}");
                    add(sbmethod, "return SWCJresult;\n");
                }
            } else if (ru.getJsoup().get(0).getJsoup() != null) {
                if (method.getReturnType().equals("String") || method.getReturnType().equals("String[]")) {
                    add(sbmethod, "String[] SWCJresult = null;");
                } else {
                    isQuote = true;
                    for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                        if (jsoup.getName() != null && !jsoup.getName().equals("")) {
                            add(sbmethod, "java.util.List<String> ", jsoup.getName(), "= new java.util.LinkedList<>();");
                        } else {
                            throw new ConfigException("jsoup name为空");
                        }
                    }
                }
                //拼接jsoup
                for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                    spliceMethodJsoup(sbmethod, jsoup, ru, isQuote);
                }
            }
        }
        if (!isQuote) {//不是引用类型返回字符串
            if (method.getReturnType().equals(stringBody) || method.getReturnType().equals("java.lang.String")) {
                add(sbmethod, "return SWCJresult[0];");
            } else {
                add(sbmethod, "return SWCJresult;");
            }
        } else {//是引用类型就注入数据
            add(sbmethod, "int[] SWCJvars1 = {");
            for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                add(sbmethod, jsoup.getName(), ".size(),");
            }
            sbmethod.substring(0, sbmethod.lastIndexOf(","));
            add(sbmethod, "};");
            add(sbmethod, "java.util.Arrays.sort(SWCJvars1);\n",
                    "int maxawdwa = SWCJvars1[SWCJvars1.length-1];");
            add(sbmethod, "java.util.List<", method.getReturnType().replace("[]", ""), ">", "lists", "= new java.util.LinkedList<>();");
            add(sbmethod, "for(int i = 0;i<maxawdwa;i++){");
            //开始执行反射生成数据
            add(sbmethod, "Class<?> aClass = Class.forName(\"", method.getReturnType().replace("[]", ""), "\");");
            add(sbmethod, "Object o = aClass.getDeclaredConstructor().newInstance();");
            for (ReptileCoreJsoup jsoup : ru.getJsoup()) {
                add(sbmethod, "try{");
                add(sbmethod, "aClass.getDeclaredMethod(\"set", StringUtil.StringToUpperCase(jsoup.getName()), "\", String.class).invoke(o,", jsoup.getName(), ".get(i));");
                add(sbmethod, "}catch(Exception e){}");
            }
            add(sbmethod, "lists.add((", method.getReturnType().replace("[]", ""), ")o", ");");
            add(sbmethod, "}");
            add(sbmethod, "return lists.toArray(new ", method.getReturnType(), "{});");
        }
        add(sbmethod, "}catch (Exception e){\ne.printStackTrace();\n}\nreturn null;\n}");
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
    }

    private void spliceMethodJsoup(StringBuilder sbmethod, ReptileCoreJsoup rcj, ReptileUrl ru, boolean isQuote) {
        add(sbmethod, "{");
        List<ReptilePaJsoup> jsoup = rcj.getJsoup();
        int bigParanthesesCount = jsoup.size();
        //一级查询
        ReptilePaJsoup rpj = jsoup.get(0);
        for (int i = 1; i < jsoup.size(); i++) {
            if (jsoup.get(i).getAllStep() != 0) {
                add(sbmethod, "int Welementi", i, " = 0;");
            }
        }
        add(sbmethod, "\njava.util.List<String> SWCJlist = new java.util.ArrayList<>();\nboolean owdad = true;int SWCJasd = 0;");
        add(sbmethod, "org.jsoup.select.Elements select = document.select(\"", rpj.getPaText(), "\");\n", "for (int i = 0;i<select.size();i++) {\n");
        add(sbmethod, "SWCJasd++;");
        if (jsoup.get(0).getStep() != 0) {
            add(sbmethod, "if(owdad){i+=", jsoup.get(0).getStep(), ";owdad=false;if(i > select.size()){break;}}");
        }
        if (rpj.getAllStep() != 0) {
            add(sbmethod, "if((SWCJasd-1)%", rpj.getAllStep(), "!=0){continue;}");
        }
        add(sbmethod, "org.jsoup.nodes.Element element = select.get(i);");
        String element = "element";
        if (jsoup.get(0).getNot() != null) {
            add(sbmethod, "if(1==1");
            for (String s : jsoup.get(0).getNot()) {
                if (!s.equals("")) {
                    add(sbmethod, "&&!element", ".text().equals(\"", s, "\")");
                }
            }
            add(sbmethod, "){");
            bigParanthesesCount++;
        }
        //开始循环
        String string = element;//命名空间
        String end = element;
        for (int i = 1; i < jsoup.size(); i++) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            add(sbmethod, "boolean Belementi", i, " = true;org.jsoup.select.Elements elementi", i, " = ", string, ".select(\"", jsoup.get(i).getPaText(), "\");\n");
            add(sbmethod, "for(int " + "c", uuid, " = 0;c", uuid, "<elementi", i, ".size();c", uuid, "++) {\n");
            if (jsoup.get(i).getStep() != 0) {
                add(sbmethod, "if(Belementi", i, "){c", uuid, "+=", jsoup.get(i).getStep(), ";owdad=false;if(c", uuid, ">=elementi", i, ".size()){break;}}");
            }
            if (jsoup.get(i).getAllStep() != 0) {
                add(sbmethod, "Welementi", i, "++;if((Welementi", i, "-1)%", jsoup.get(i).getAllStep(), "!=0){continue;}");
            }
            add(sbmethod, "org.jsoup.nodes.Element element", i + 2, " = elementi", i, ".get(c", uuid, ");");
            if (jsoup.get(i).getNot() != null) {
                add(sbmethod, "if(1==1");
                for (String s : jsoup.get(i).getNot()) {
                    if (!s.equals("")) {
                        add(sbmethod, "&&!element", i + 2, ".text().equals(\"", s, "\")");
                    }
                }
                bigParanthesesCount++;
                add(sbmethod, "){");
            }
            string = element + (i + 2);
            end = string;
        }
        if (jsoup.get(jsoup.size() - 1).getElement() != null && !jsoup.get(jsoup.size() - 1).getElement().equals("")) {
            add(sbmethod, "SWCJlist.add(", end, ".attr(\"", jsoup.get(jsoup.size() - 1).getElement(), "\"));");
        } else {
            add(sbmethod, "SWCJlist.add(", end, ru.isHtml() ? ".html" : ".text", "());\n");
        }
        //添加括号
        for (int i = 0; i < bigParanthesesCount; i++) {
            add(sbmethod, "}\n");
        }
        //返回数据
        if (!isQuote) {
            add(sbmethod, "SWCJresult = SWCJlist.toArray(new String[]{});");
        } else {
            add(sbmethod, rcj.getName(), "=SWCJlist;");
        }
        add(sbmethod, "}");

    }
}