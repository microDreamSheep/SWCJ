package com.midream.sheep.swcj.util.compiler;

import com.midream.sheep.swcj.pojo.buildup.SWCJClass;

public interface SWCJCompiler {
    //编译类
    Class<?> compileAndLoad(String fullName, SWCJClass sclass) throws ClassNotFoundException;
    //拼接类

}
