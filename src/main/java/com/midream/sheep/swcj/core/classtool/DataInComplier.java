package com.midream.sheep.swcj.core.classtool;

/**
 * @author midreamsheep
 */
public class DataInComplier {
    private boolean isload;

    private byte[] datas;
    private String calssName;

    private Class<?> aClass;

    public boolean isIsload() {
        return isload;
    }

    public void setIsload(boolean isload) {
        this.isload = isload;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    public String getCalssName() {
        return calssName;
    }

    public void setCalssName(String calssName) {
        this.calssName = calssName;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }
}
