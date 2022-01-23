package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.RootReptile;

public interface ReptilesBuilderInter {
    Object reptilesBuilder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException;
}
