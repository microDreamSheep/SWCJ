package com.midream.sheep.swcj.core.classtool.classloader;

import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.classtool.compiler.javanative.DynamicCompiler;

/**
 * @author midreamSheep
 * 自定义加载器，暂不支持拓展
 */
public class SWCJClassLoader extends ClassLoader implements SWCJClassLoaderInter {
    //通过文件加载一个class
    public Class<?> loadData(String className, byte[] datas) {
        if (datas != null) {
            return super.defineClass(className, datas, 0, datas.length);
        }
        return null;
    }

}
