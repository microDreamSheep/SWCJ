package com.midream.sheep.swcj.core.classtool.compiler.javanative;

import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;

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
    public DataInComplier compileAndLoad(String fullName, SWCJClass sclass) throws ClassNotFoundException {

        DataInComplier dataInComplier = new DataInComplier();
        dataInComplier.setIsload(true);
        DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
        JavaFileManager fileManager = new ClassFileManager(javaCompiler.getStandardFileManager(diagnosticCollector, null, null));

        //加载
        List<JavaFileObject> javaFileObjectList = new ArrayList<JavaFileObject>();
        javaFileObjectList.add(new CharSequenceJavaFileObject(fullName,getWholeClassString(sclass)));
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
            sb.append(entry.getValue().getBody());
        }
        //类封口
        add(sb, "\n}");
        return sb.toString();
    }
}