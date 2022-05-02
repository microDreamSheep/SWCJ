package com.midream.sheep.swcj.util.compiler;

import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import javassist.CannotCompileException;
import javassist.NotFoundException;

public interface SWCJCompiler {
    //编译类
    Class<?> compileAndLoad(String fullName, SWCJClass sclass) throws ClassNotFoundException, CannotCompileException, NotFoundException;
    //拼接类

}
