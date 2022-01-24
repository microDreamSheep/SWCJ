package com.midream.sheep.SWCJ.data.swc;

public class RootReptile {
    private ReptileUrl ru;
    private String cookies;
    private String parentInter;
    private String returnType = "java.lang.String";
    private String inPutType;
    private String inPutName;
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
