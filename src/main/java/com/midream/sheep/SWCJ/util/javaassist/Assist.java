package com.midream.sheep.SWCJ.util.javaassist;

import com.midream.sheep.SWCJ.util.javaassist.IAssist;
import javassist.ClassPool;

public class Assist implements IAssist {
    private static ClassPool cp = null;
    static {
        cp = ClassPool.getDefault();
    }

    @Override
    public ClassPool getClassPool() {
        return cp;
    }
}
