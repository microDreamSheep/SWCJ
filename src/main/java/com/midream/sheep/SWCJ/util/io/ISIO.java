package com.midream.sheep.SWCJ.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * @author midreamSheep
 * io流接口
 * */
public interface ISIO {
    //获取文件字符串
    String inPutString(File file) throws IOException;
    String inPutString(String file) throws IOException;
    //获取字节码
    byte[] inPutBytes(File file) throws IOException;
    byte[] inPutBytes(String file) throws IOException;
    //输出字符串
    void outPutString(String data,File tofile) throws IOException;
}
