package com.midream.sheep.swcj.pojo.swc.all;

import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;

public class ReptlileMiddle {
    private RootReptile rootReptile;
    private ReptileConfig config;

    public ReptlileMiddle(RootReptile rootReptile, ReptileConfig config) {
        this.rootReptile = rootReptile;
        this.config = config;
    }

    public RootReptile getRootReptile() {
        return rootReptile;
    }

    public void setRootReptile(RootReptile rootReptile) {
        this.rootReptile = rootReptile;
    }

    public ReptileConfig getConfig() {
        return config;
    }

    public void setConfig(ReptileConfig config) {
        this.config = config;
    }
}
