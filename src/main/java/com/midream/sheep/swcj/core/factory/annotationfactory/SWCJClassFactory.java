package com.midream.sheep.swcj.core.factory.annotationfactory;

import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;

import java.lang.reflect.InvocationTargetException;

public abstract class SWCJClassFactory
    extends SWCJAbstractFactory
{
    public SWCJClassFactory parse(Class<? extends SWCJConfigClassConfiguration> configClass){
        try {
            parseClass(configClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("some wrong about the config class,cause:can't get a instance \n wrong message:\n"+e.getMessage());
        }
        return this;
    }
    protected abstract void parseClass(SWCJConfigClassConfiguration swcjConfigClassConfiguration);
}
