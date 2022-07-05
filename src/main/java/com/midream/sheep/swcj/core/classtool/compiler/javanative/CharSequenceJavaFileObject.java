package com.midream.sheep.swcj.core.classtool.compiler.javanative;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
 
/**
 * 字符串java源代码。JavaFileObject表示
 */
public class CharSequenceJavaFileObject extends SimpleJavaFileObject {
 
    //表示java源代码
    private final CharSequence content;
 
    protected CharSequenceJavaFileObject(String className, String content) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }
 
    /**
     * 获取需要编译的源代码
     * @param ignoreEncodingErrors 是否忽略编码错误
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }
}