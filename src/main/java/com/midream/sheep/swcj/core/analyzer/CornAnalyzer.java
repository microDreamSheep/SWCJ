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
        String[] split = in.split("\\[swcj;\\]");
        for(int i = 0;i<split.length;i++){
            System.out.println(i+"---->"+split[i]);
        }
        //数据封装
        executeValue.setClassNameReturn(split[1]+"[]");
        executeValue.setHtml(Boolean.parseBoolean(split[2]));
        executeValue.setType(SRequest.get(split[3]));
        executeValue.setUrl(split[4]);
        executeValue.setUserAge(split[5]);
        executeValue.setCookies(split[6]);
        executeValue.setValues(StringUtil.changeString(split[7]));
        executeValue.setTimeout(split[8]);

        try {
            SWCJExecute<T> swcjExecute = (SWCJExecute<T>) Class.forName(split[9]).newInstance();
            List<T> ts = swcjExecute.execute(executeValue, split[10]);
            return ts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
