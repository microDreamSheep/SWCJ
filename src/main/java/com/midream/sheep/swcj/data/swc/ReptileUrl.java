package com.midream.sheep.swcj.data.swc;

import java.util.ArrayList;
import java.util.List;
/**
 * @author midreamSheep
 * 爬虫的url配置类
 * */
public class ReptileUrl {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //注入名字
    private String inPutName="";
    //方法名
    private String name="";

    public String getInPutName() {
        return inPutName;
    }

    public void setInPutName(String inPutName) {
        this.inPutName = inPutName;
    }

    public ReptileCoreJsoup getJsoup() {
        return jsoup;
    }

    public void setJsoup(ReptileCoreJsoup jsoup) {
        this.jsoup = jsoup;
    }

    //访问链接
    private String url="";
    //请求类型
    private String requestType="";
    //正则表达式
    private String reg="";
    //jsoup策略
    private ReptileCoreJsoup jsoup;
    //是否是html返回
    private boolean isHtml;

    @Override
    public String toString() {
        return "ReptileUrl{" +
                ", inPutName='" + inPutName + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", requestType='" + requestType + '\'' +
                ", reg='" + reg + '\'' +
                ", jsoup=" + jsoup +
                ", isHtml=" + isHtml +
                '}';
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String program) {
        this.reg = program;
    }

    public ReptileUrl() {
    }
}
