package com.midream.sheep.swcj.core.classtool.classloader;

import com.midream.sheep.swcj.core.classtool.DataInComplier;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.classtool.compiler.javanative.DynamicCompiler;

/**
 * @author midreamSheep
 * 自定义加载器，暂不支持拓展
 */
public class SWCJClassLoader extends ClassLoader {
    private SWCJCompiler swcjCompiler = null;
    //通过文件加载一个class
    public void setSwcjCompiler(SWCJCompiler swcjCompiler){
        this.swcjCompiler = swcjCompiler;
    }
    public Class<?> loadData(String className, byte[] datas) {
        if (datas != null) {
            return super.defineClass(className, datas, 0, datas.length);
        }
        return null;
    }

    //字符串加载
    public Class<?> compileJavaFile(String className, SWCJClass sclass) throws ClassNotFoundException {
        //看传递的是字节码还是具体的类
        if(sclass.getCodes()!=null&&sclass.getCodes().length!=0){
            loadData(className,sclass.getCodes());
        }
        if (swcjCompiler==null){
            swcjCompiler = new DynamicCompiler();
        }
        DataInComplier dataInComplier = swcjCompiler.compileAndLoad(className, sclass);
        if(dataInComplier.isIsload()){
            return dataInComplier.getaClass();
        }
        return loadData(dataInComplier.getCalssName(),dataInComplier.getDatas());
    }
}
