package com.midream.sheep.swcj.core.classtool.compiler.javanative;

import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;
import com.midream.sheep.swcj.util.function.StringUtil;

import javax.tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.midream.sheep.swcj.util.function.StringUtil.add;

/**
 * 运行时编译
 */
public class DynamicCompiler implements SWCJCompiler {
    private final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

    @Override
    @SuppressWarnings("all")
    public DataInComplier compileAndLoad(SWCJClass sclass, ReptlileMiddle middle) throws ClassNotFoundException {
        String fullName = Constant.DEFAULT_PACKAGE_NAME + "." + sclass.getClassName();
        DataInComplier dataInComplier = new DataInComplier();
        dataInComplier.setIsload(true);
        DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
        JavaFileManager fileManager = new ClassFileManager(javaCompiler.getStandardFileManager(diagnosticCollector, null, null));

        //加载
        List<JavaFileObject> javaFileObjectList = new ArrayList<JavaFileObject>();
        javaFileObjectList.add(new CharSequenceJavaFileObject(fullName,getWholeClassString(getAllMethod(sclass,middle))));
        boolean result = javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjectList).call();
        if (result) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Class<?> aClass = fileManager.getClassLoader(null).loadClass(fullName);
            try {
                fileManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataInComplier.setaClass(aClass);
            return dataInComplier;
        } else {
            try {
                fileManager.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataInComplier.setaClass(Class.forName(fullName));
            return dataInComplier;
        }
    }
    private String getWholeClassString(SWCJClass swcjClass){
        StringBuilder sb = new StringBuilder();
        //增加包名
        add(sb, "package ", Constant.DEFAULT_PACKAGE_NAME, ";\n");
        //拼接类名
        add(sb, "public class ", swcjClass.getClassName(), " implements ",swcjClass.getItIterface(), " {");
        //拼接方法
        Map<String, SWCJMethod> methods = swcjClass.getMethods();
        for (Map.Entry<String, SWCJMethod> entry : methods.entrySet()) {
            sb.append(entry.getValue().getExecuteStr());
        }
        //类封口
        add(sb, "\n}");
        return sb.toString();
    }
    private SWCJClass getAllMethod(SWCJClass swcjClass, ReptlileMiddle middle) {
        Map<String, SWCJMethod> function = swcjClass.getMethods();
        for (ReptileUrl reptileUrl : middle.getRootReptile().getRu()) {
            SWCJMethod method = function.get(reptileUrl.getName());
            if (method == null) {
                continue;
            }
            method.setExecuteStr(BuildTool.spliceMethod(method));
        }
        return swcjClass;
    }
    private static class BuildTool{
        private static final String Template =
                "return new com.midream.sheep.swcj.core.analyzer.CornAnalyzer<#[fx]>().execute(\"#[execute]\",#[args]).toArray(new #[fx][0]);";

        /**
         * 构建方法主体
         * @param method SWCJ方法实体类
         * @return 方法主体
         * */
        public static String spliceMethod(SWCJMethod method) {
            //方法体
            StringBuilder sbmethod = new StringBuilder();
            //方法头 定义被重写
            add(sbmethod, "\npublic ", method.getReturnType(), (" "), method.getMethodName(), "(",method.getParamIn(), "){");
            //开始拼接方法
            add(sbmethod,
                    Template.replace("#[execute]", method.getExecuteStr())
                            .replace("#[fx]", method.getReturnType().replace("[]", Constant.nullString))
                            .replace(",#[args]", StringUtil.getStringByList(method.getExecuteVars()))
            );
            add(sbmethod, "}");
            return sbmethod.toString();
        }

    }
}