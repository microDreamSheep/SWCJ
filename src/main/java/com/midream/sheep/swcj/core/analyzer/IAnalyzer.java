package com.midream.sheep.swcj.core.analyzer;

import java.util.List;

public interface IAnalyzer<T> {
    /**
     * 执行的分析器，分析成具体的执行分装类
     *
     * @param in   执行的字符串
     * @param args 携带的参数
     * @return 返回具体的结果类
     */
    List<T> execute(String in, Object... args);
}
