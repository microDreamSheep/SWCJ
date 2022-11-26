package com.midream.sheep.swcj.core.factory.annotationfactory.byclass;

import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.buildup.SWCJMethod;

import java.util.Map;

import static com.midream.sheep.swcj.core.factory.FactoryData.count;

public class SWCJClassParserTool {
    public static SWCJClass parse(SWCJConfigClassConfiguration swcjConfigClassConfiguration){
        SWCJClass swcjClass = new SWCJClass();
        swcjClass.setClassName("swcj" + (count.addAndGet(1)));
        swcjClass.setId(swcjConfigClassConfiguration.getId());
        swcjClass.setItIterface(swcjClass.getItIterface());
        swcjClass.setLoad(false);
        swcjClass.setMethods(parseMethod(swcjConfigClassConfiguration));
        return swcjClass;
    }
    private static Map<String, SWCJMethod> parseMethod(SWCJConfigClassConfiguration swcjConfigClassConfiguration){
        SWCJMethod swcjMethod = new SWCJMethod();
        return null;
    }
}
