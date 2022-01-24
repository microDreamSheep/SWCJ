package com.midream.sheep.SWCJ.data.swc;
/**
 * @author midreamSheep
 * 核心爬虫配置文件
 * */
public class RootReptile {
    //url策略
    private ReptileUrl ru;
    //携带cookies
    private String cookies;
    //父接口
    private String parentInter;
    //返回值类型 默认为String[]
    private String returnType = "java.lang.String[]";
    //注入类型
    private String inPutType;
    //注入名字
    private String inPutName;
    //方法名
    private String id;

    @Override
    public String toString() {
        return "RootReptile{" +
                "ru=" + ru +
                ", cookies='" + cookies + '\'' +
                ", parentInter='" + parentInter + '\'' +
                ", returnType='" + returnType + '\'' +
                ", inPutType='" + inPutType + '\'' +
                ", inPutName='" + inPutName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getInPutType() {
        return inPutType;
    }

    public void setInPutType(String inPutType) {
        this.inPutType = inPutType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInPutName() {
        return inPutName;
    }

    public void setInPutName(String inPutName) {
        this.inPutName = inPutName;
    }

    public RootReptile() {
    }

    public ReptileUrl getRu() {
        return ru;
    }

    public void setRu(ReptileUrl ru) {
        this.ru = ru;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getParentInter() {
        return parentInter;
    }

    public void setParentInter(String parentInter) {
        this.parentInter = parentInter;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
