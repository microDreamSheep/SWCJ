package com.midream.sheep.swcj.core.analyzer;

import java.util.List;
/**
 * @author midreamSheep
 * @date 2022-07-10
 *分析中间层
 * */
public interface IAnalyzer<T> {
    /**
     * 执行的分析器，分析成具体的执行字符串交给执行层执行
     * @param in   执行的字符串
     * @param args 携带的参数
     * @return 返回具体的结果类的集合，在生成字节码中墙砖成数组
     */
    List<T> execute(String in, Object... args);
}
