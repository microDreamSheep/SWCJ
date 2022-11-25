package com.midream.sheep.swcj.pojo.swc.passvalue;

import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;

public class ReptlileMiddle {
    private SWCJClass swcjClass;
    private ReptileConfig config;

    public ReptlileMiddle(SWCJClass rootReptile, ReptileConfig config) {
        this.swcjClass = rootReptile;
        this.config = config;
    }

    public SWCJClass getSwcjClass() {
        return swcjClass;
    }

    public void setSwcjClass(SWCJClass swcjClass) {
        this.swcjClass = swcjClass;
    }

    public ReptileConfig getConfig() {
        return config;
    }

    public void setConfig(ReptileConfig config) {
        this.config = config;
    }
}
