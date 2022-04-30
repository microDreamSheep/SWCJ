package com.midream.sheep.swcj.util.compiler;

import com.midream.sheep.swcj.pojo.SWCJClass;

public interface SWCJCompiler {
    //编译类
    Class<?> compileAndLoad(String fullName, String sourceCode) throws ClassNotFoundException;
    //拼接类

}
