package com.midream.sheep.swcj.core.executetool;

import com.midream.sheep.swcj.pojo.ExecuteValue;

import java.util.List;

public interface SWCJExecute<T> {
    /**
     * @param executeValue execute value
     * @param type 类型数据
     * @param args Additional Required Implementation Information
     *             0 xml execute text
     * @return the returned instantiated object
     * */
    List<T> execute(ExecuteValue executeValue, String ...args) throws Exception;
}
