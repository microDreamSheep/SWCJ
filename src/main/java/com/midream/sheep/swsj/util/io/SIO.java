package com.midream.sheep.swsj.util.io;

import java.io.*;

public class SIO implements ISIO{
    @Override
    public String inPutString(File file) throws IOException {
        //字符串
        StringBuffer sb = new StringBuffer();
        //实例化输入流
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            //建立1024大小的缓冲区
            int len = -1;
            byte[] datas = new byte[1024];
            //输入
            while ((len = is.read(datas)) != -1) {
                sb.append(new String(datas, 0, len));
            }
        }finally {
            if(is!=null) {
                //关闭流
                is.close();
            }
        }
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
        OutputStream os = null;
        try {
            os = new FileOutputStream(tofile);
            os.write(data.getBytes());
            os.flush();
        }finally {
            if(os!=null) {
                os.close();
            }
        }
    }
}
