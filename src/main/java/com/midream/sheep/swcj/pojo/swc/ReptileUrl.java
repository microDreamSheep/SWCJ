package com.midream.sheep.swcj.pojo.swc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author midreamSheep
 * 爬虫的url配置类
 */
public class ReptileUrl {

    //注入名字
    private String inPutName = "";
    //方法名
    private String name = "";
    //访问链接
    private String url = "";
    //请求类型
    private String requestType = "GET";
    //是否是html返回
    private boolean isHtml;
    //执行逻辑
    private String parseProgram;
    //执行器类名
    private String executClassName;
    //值
    private String values = "";

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getParseProgram() {
        return parseProgram;
    }

    public void setParseProgram(String parseProgram) {
        this.parseProgram = parseProgram;
    }

    public String getExecutClassName() {
        return executClassName;
    }

    public void setExecutClassName(String executClassName) {
        this.executClassName = executClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInPutName() {
        return inPutName;
    }

    public void setInPutName(String inPutName) {
        this.inPutName = inPutName;
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

    public ReptileUrl() {
    }

    @Override
    public String toString() {
        return "ReptileUrl{" +
                "inPutName='" + inPutName + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", requestType='" + requestType + '\'' +
                ", isHtml=" + isHtml +
                ", parseProgram='" + parseProgram + '\'' +
                ", executClassName='" + executClassName + '\'' +
                ", values='" + values + '\'' +
                '}';
    }
}
