package com.midream.sheep.swcj.util.function;

import java.io.*;

public class SIO{
    public static String inPutString(File file) throws IOException {
        //字符串
        StringBuilder sb = new StringBuilder();
        //实例化输入流
        try (InputStream is = new FileInputStream(file)) {
            //建立1024大小的缓冲区
            int len = -1;
            byte[] datas = new byte[1024];
            //输入
            while ((len = is.read(datas)) != -1) {
                sb.append(new String(datas, 0, len));
            }
        }
        //关闭流
        //返回
        return sb.toString();
    }


    public static String inPutString(String file) throws IOException {
        return inPutString(new File(file));
    }

    public static byte[] inPutBytes(File file) throws IOException {
        return null;
    }

    public static byte[] inPutBytes(String file) throws IOException {
        return inPutBytes(new File(file));
    }

    public static void outPutString(String data, File tofile) throws IOException {
        try (OutputStream os = new FileOutputStream(tofile)) {
            os.write(data.getBytes());
            os.flush();
        }
    }

    public static void outPutData(byte[] datas, File toFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(toFile);
        outputStream.write(datas);
        outputStream.close();
    }
}
