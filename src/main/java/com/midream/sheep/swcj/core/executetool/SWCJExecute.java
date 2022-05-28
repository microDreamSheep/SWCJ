package com.midream.sheep.swcj.core.executetool;

import com.midream.sheep.swcj.pojo.ExecuteValue;

import java.util.List;

public interface SWCJExecute<T> {
    /**
     * @param executeValue execute value
     * @param args Additional Required Implementation Information
     *             0 xml execute text
     *             1 reflection class
     * @return the returned instantiated object
     * */
    List<T> execute(ExecuteValue executeValue, String ...args) throws Exception;
}
