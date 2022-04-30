package com.midream.sheep.swcj.build.inter;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.util.compiler.SWCJCompiler;

public interface SWCJBuilder {
    Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException, InterfaceIllegal, ClassNotFoundException;
    void setCompiler(SWCJCompiler swcjCompiler);
}
