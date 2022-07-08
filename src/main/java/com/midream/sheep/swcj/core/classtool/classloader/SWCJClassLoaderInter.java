package com.midream.sheep.swcj.core.classtool.classloader;

public interface SWCJClassLoaderInter {
    Class<?> loadData(String className, byte[] datas);
}
