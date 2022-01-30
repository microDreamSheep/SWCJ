package com.midream.sheep.swcj.build;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.data.swc.RootReptile;

public interface ClassBuilder {
    Object builderClass(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, ConfigException;
}
