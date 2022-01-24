package com.midream.sheep.SWCJ.data.swc;

import java.util.ArrayList;
import java.util.List;
/**
 * @author midreamSheep
 * 爬虫的url配置类
 * */
public class ReptileUrl {
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
