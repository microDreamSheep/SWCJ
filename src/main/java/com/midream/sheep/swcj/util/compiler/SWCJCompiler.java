package com.midream.sheep.swcj.util.compiler;

public interface SWCJCompiler {
    //编译类
    Class<?> compileAndLoad(String fullName, String sourceCode) throws ClassNotFoundException;
}
