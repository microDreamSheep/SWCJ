package com.midream.sheep.swcj.core.factory.annotationfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.core.factory.annotationfactory.byclass.SWCJClassParserTool;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;

public class CoreFactoryByClass extends SWCJClassFactory{

    public CoreFactoryByClass(){

    }
    public CoreFactoryByClass(boolean isLoadCache,String workplace) throws ConfigException {
        if(!isLoadCache){
            return;
        }
        cache(workplace);
    }

    @Override
    protected void parseClass(SWCJConfigClassConfiguration swcjConfigClassConfiguration) {
        SWCJClass parse = SWCJClassParserTool.parse(swcjConfigClassConfiguration);
        this.swcjClasses.put(parse.getClassName(),parse);
    }
}
