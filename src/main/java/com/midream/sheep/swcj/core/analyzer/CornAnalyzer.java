package com.midream.sheep.swcj.core.analyzer;

import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.executetool.SWCJExecute;
import com.midream.sheep.swcj.core.executetool.execute.SRequest;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.pojo.ExecuteValue;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * 分析中间层
 * @author Midream
 * @date 2022-03-01
 * @version 1.0
 * */
public class CornAnalyzer<T> implements IAnalyzer<T>{
    @Override
    public List<T> execute(String in, Object... args) {
        ExecuteValue executeValue = new ExecuteValue();
        String[] split = in.split("\\[swcj;]");
        //数据注入
        replaceString(split,args);
        //数据封装
        insertData(executeValue,split);
        try {
            //只要传递的执行类一定能够执行且是SWCJExecute类型的，那么就可以执行
            @SuppressWarnings("unchecked")
            SWCJExecute<T> execute = (SWCJExecute<T>) Class.forName(split[9]).newInstance();
            List<T> result = execute.execute(executeValue, split[10]);
            return result;
        } catch (Exception e) {
            Logger.getLogger(CornAnalyzer.class.getName()).severe(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private void replaceString(String[] split,Object[] args){
        if("".contains(split[0])) {
            return;
        }
        for (String s : split[0].split(",")) {
            String[] split1 = s.split(":");
            split[Integer.parseInt(split1[1]) - 1] = split[Integer.parseInt(split1[1]) - 1]
                    .replace("#{"+split1[2]+"}", args[Integer.parseInt(split1[0]) - 1] + "");
        }
    }
    private void insertData(ExecuteValue executeValue,String[] args){
        executeValue.setClassNameReturn(args[1]+"[]");
        executeValue.setHtml(Boolean.parseBoolean(args[2]));
        executeValue.setType(SRequest.get(args[3]));
        executeValue.setUrl(args[4]);
        executeValue.setUserAge(args[5]);
        executeValue.setCookies(args[6]);
        executeValue.setValues(StringUtil.changeStringToMaps(args[7]));
        executeValue.setTimeout(args[8]);
    }
}
