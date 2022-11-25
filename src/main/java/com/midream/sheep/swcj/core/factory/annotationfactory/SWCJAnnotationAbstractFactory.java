package com.midream.sheep.swcj.core.factory.annotationfactory;

import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.xmlfactory.bystr.SWCJParseI;

public abstract class SWCJAnnotationAbstractFactory
    extends SWCJAbstractFactory
{
    @Override
    public SWCJAnnotationAbstractFactory setParseTool(SWCJParseI swcjParseI) {
        System.err.println("AnnotationFactory can't set parse tool");
        return this;
    }
    public abstract SWCJAnnotationAbstractFactory parse(Class<? extends SWCJConfigClassConfiguration> configClass);
}
