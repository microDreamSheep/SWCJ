package com.midream.sheep.SWCJ.data.swc;

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
    //注入类型
    private String inPutType;
    //注入名字
    private String inPutName;
    //方法名
    private String name="";

    public String getInPutType() {
        return inPutType;
    }

    public void setInPutType(String inPutType) {
        this.inPutType = inPutType;
    }

    public String getInPutName() {
        return inPutName;
    }

    public void setInPutName(String inPutName) {
        this.inPutName = inPutName;
    }

    //访问链接
    private String url;
    //请求类型
    private String requestType;
    //正则表达式
    private String reg;
    //jsoup策略
    private List<ReptilePaJsoup> jsoup;
    //是否是html返回
    private boolean isHtml;
    //返回值类型 默认为String[]
    private String returnType = "java.lang.String[]";

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "ReptileUrl{" +
                "url='" + url + '\'' +
                ", requestType='" + requestType + '\'' +
                ", reg='" + reg + '\'' +
                ", jsoup=" + jsoup +
                ", isHtml=" + isHtml +
                '}';
    }

    public List<ReptilePaJsoup> getJsoup() {
        return jsoup;
    }

    public void setJsoup(List<ReptilePaJsoup> jsoup) {
        this.jsoup = jsoup;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public void addJsoup(ReptilePaJsoup data){
        if(jsoup==null){
            jsoup = new ArrayList<>();
        }
        jsoup.add(data);
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
