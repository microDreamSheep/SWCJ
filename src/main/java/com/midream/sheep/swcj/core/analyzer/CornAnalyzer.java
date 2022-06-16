package com.midream.sheep.swcj.core.analyzer;

import com.midream.sheep.swcj.core.executetool.SWCJExecute;
import com.midream.sheep.swcj.core.executetool.execute.SRequest;
import com.midream.sheep.swcj.pojo.ExecuteValue;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.util.List;

public class CornAnalyzer<T> implements IAnalyzer<T>{

    @Override
    public List<T> execute(String in, Object... args) {
        ExecuteValue executeValue = new ExecuteValue();
        String[] split = in.split("\\[swcj;]");
        //数据注入
        if(!("".contains(split[0]))) {
            String[] injections = split[0].split(",");
            for (String s : injections) {
                String[] split1 = s.split(":");
                split[Integer.parseInt(split1[1]) - 1] = split[Integer.parseInt(split1[1]) - 1]
                        .replace("#{"+split1[2]+"}", args[Integer.parseInt(split1[0]) - 1] + "");
            }
        }
        //数据封装
        executeValue.setClassNameReturn(split[1]+"[]");
        executeValue.setHtml(Boolean.parseBoolean(split[2]));
        executeValue.setType(SRequest.get(split[3]));
        executeValue.setUrl(split[4]);
        executeValue.setUserAge(split[5]);
        executeValue.setCookies(split[6]);
        executeValue.setValues(StringUtil.changeStringToMaps(split[7]));
        executeValue.setTimeout(split[8]);
        try {
            SWCJExecute<T> swcjExecute = (SWCJExecute) Class.forName(split[9]).newInstance();
            return swcjExecute.execute(executeValue, split[10]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
