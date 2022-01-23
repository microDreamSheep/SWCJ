package com.midream.sheep.SWCJ.util.classLoader;

import java.io.*;

public class SWCJClassLoader extends ClassLoader{
    public Class<?> loadData(String className,String file){
        byte[] data = loderClassData(file);
        if(data!=null){
            return super.defineClass(className,data,0,data.length);
        }
        return null;
    }    public Class<?> loadData(String className,byte[] datas){
        if(datas!=null){
            return super.defineClass(className,datas,0,datas.length);
        }
        return null;
    }
    private byte[] loderClassData(String file)  {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        byte[] datas = null;
        try {
            bos = new ByteArrayOutputStream();
            is = new FileInputStream(new File(file));
            is.transferTo(bos);
            datas = bos.toByteArray();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
