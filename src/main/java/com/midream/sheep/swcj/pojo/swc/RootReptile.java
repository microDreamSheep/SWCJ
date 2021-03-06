package com.midream.sheep.swcj.pojo.swc;

import java.util.LinkedList;
import java.util.List;

/**
 * @author midreamSheep
 * 核心爬虫配置文件
 * */
public class RootReptile {
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public void setRu(List<ReptileUrl> ru) {
        this.ru = ru;
    }

    //url策略
    private List<ReptileUrl> ru = new LinkedList<>();
    //携带cookies
    private String cookies="";
    //父接口
    private String parentInter;
    //方法名
    private String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RootReptile() {
    }

    public List<ReptileUrl> getRu() {
        return ru;
    }

    public void setRu(LinkedList<ReptileUrl> ru) {
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

    public void addUrl(ReptileUrl ru){
        this.ru.add(ru);
    }

    @Override
    public String toString() {
        return "RootReptile{" +
                "isLoad=" + isLoad +
                ", ru=" + ru +
                ", cookies='" + cookies + '\'' +
                ", parentInter='" + parentInter + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
