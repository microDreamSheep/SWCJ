package com.midream.sheep.SWCJ.util.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SIO implements ISIO{
    @Override
    public String inPutString(File file) throws IOException {
        //实例化输入流
        InputStream is = new FileInputStream(file);
        //建立1024大小的缓冲区
        int len = -1;
        byte[] datas = new byte[1024];
        //字符串
        StringBuffer sb = new StringBuffer();
        //输入
        while ((len=is.read(datas))!=-1){
            sb.append(new String(datas,0,len));
        }
        //关闭流
        is.close();
        //返回
        return sb.toString();
    }

    @Override
    public String inPutString(String file) throws IOException {
        return inPutString(new File(file));
    }

    @Override
    public byte[] inPutBytes(File file) throws IOException {
        return null;
    }

    @Override
    public byte[] inPutBytes(String file) throws IOException {
        return inPutBytes(new File(file));
    }

    @Override
    public void outPutString(String data, File tofile) throws IOException {
        OutputStream os = new FileOutputStream(tofile);
        os.write(data.getBytes());
        os.flush();
        os.close();
    }
}
