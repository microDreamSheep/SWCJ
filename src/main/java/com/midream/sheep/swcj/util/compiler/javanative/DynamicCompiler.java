package com.midream.sheep.swcj.util.compiler.javanative;

import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;
import com.midream.sheep.swcj.pojo.buildup.SWCJValue;
import com.midream.sheep.swcj.util.compiler.SWCJCompiler;

import javax.tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.midream.sheep.swcj.build.function.StringUtil.add;

/**
 * 运行时编译
 */
public class DynamicCompiler implements SWCJCompiler {
    private JavaFileManager fileManager;

    public DynamicCompiler() {
        this.fileManager = initManger();
    }

    private JavaFileManager initManger() {
        if (fileManager != null) {
            return fileManager;
        } else {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
            fileManager = new ClassFileManager(javaCompiler.getStandardFileManager(diagnosticCollector, null, null));
            return fileManager;
        }
    }

    /**
     * 编译源码并加载，获取Class对象
     *
     * @param fullName
     * @param sclass
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    public Class<?> compileAndLoad(String fullName, SWCJClass sclass) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        //增加包名
        add(sb, "package ", Constant.DEFAULT_PACKAGE_NAME, ";\n");
        //拼接类名
        add(sb, "public class ", sclass.getClassName(), " implements ",sclass.getItIterface(), " {");
        //拼接方法
        {
            //搭建全局静态属性
            {
                for (SWCJValue property : sclass.getValue()) {
                    add(sb,property.toString(),"\n");
                }
            }
            {
                Map<String, SWCJMethod> methods = sclass.getMethods();
                for (Map.Entry<String, SWCJMethod> entry : methods.entrySet()) {
                    sb.append(entry.getValue().getBody());
                }
            }
        }
        //类封口
        add(sb, "\n}");
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        List<JavaFileObject> javaFileObjectList = new ArrayList<JavaFileObject>();
        javaFileObjectList.add(new CharSequenceJavaFileObject(fullName, sb.toString()));
        boolean result = javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjectList).call();
        if (result) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.fileManager.getClassLoader(null).loadClass(fullName);
        } else {
            return Class.forName(fullName);
        }
    }

    /**
     * 关闭fileManager
     *
     * @throws IOException
     */
    public void close() throws IOException {
        this.fileManager.close();
    }
}