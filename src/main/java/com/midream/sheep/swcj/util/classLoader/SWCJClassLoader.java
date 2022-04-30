package com.midream.sheep.swcj.util.classLoader;

import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.build.function.BuildTool;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.pojo.SWCJClass;
import com.midream.sheep.swcj.pojo.SWCJMethod;
import com.midream.sheep.swcj.pojo.swc.ReptileUrl;
import com.midream.sheep.swcj.util.compiler.SWCJCompiler;
import com.midream.sheep.swcj.util.compiler.javanative.DynamicCompiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.midream.sheep.swcj.build.function.StringUtil.add;

/**
 * @author midreamSheep
 * 自定义编译类
 */
public class SWCJClassLoader extends ClassLoader {
    private SWCJCompiler swcjCompiler = null;
    //通过文件加载一个class
    public Class<?> loadData(String className, String file) {
        byte[] data = loderClassData(file);
        if (data != null) {
            return super.defineClass(className, data, 0, data.length);
        }
        return null;
    }
    public void setSwcjCompiler(SWCJCompiler swcjCompiler){
        this.swcjCompiler = swcjCompiler;
    }
    public Class<?> loadData(String className, byte[] datas) {
        if (datas != null) {
            return super.defineClass(className, datas, 0, datas.length);
        }
        return null;
    }

    @Override
    public Class<?> findClass(String name) {
        return super.findLoadedClass(name);
    }

    //通过字节加载一个class
    private byte[] loderClassData(String file) {
        byte[] datas = null;
        try {
            datas = Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
    }
    //字符串加载
    public Class<?> compileJavaFile(String className, SWCJClass sclass) throws IOException, ClassNotFoundException, InterfaceIllegal {
        if (swcjCompiler==null){
            swcjCompiler = new DynamicCompiler();
        }
        return swcjCompiler.compileAndLoad(className,sclass);
    }
}
