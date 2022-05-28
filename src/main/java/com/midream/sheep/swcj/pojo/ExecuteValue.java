package com.midream.sheep.swcj.pojo;

import com.midream.sheep.swcj.core.executetool.execute.SRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author midreamsheep
 */
public class ExecuteValue {
    private boolean isHtml;
    private String url;
    private SRequest type;
    private String cookies;
    private String userAge;
    private String timeout;
    private Map<String,String> values;
    private String classNameReturn = "java.lang.String";

    public String getClassNameReturn() {
        return classNameReturn;
    }

    public void setClassNameReturn(String classNameReturn) {
        this.classNameReturn = classNameReturn;
    }

    public String getCookies() {
        if(cookies==null){
            return "";
        }
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getUserAge() {
        if(userAge==null){
            return "";
        }
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getTimeout() {
        if(timeout==null){
            return "10000";
        }
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getValues() {
        if(values==null){
            return new HashMap<>(0);
        }
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
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

    public SRequest getType() {
        if(type==null){
            return SRequest.GET;
        }
        return type;
    }

    public void setType(SRequest type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ExecuteValue{" +
                "isHtml=" + isHtml +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", cookies='" + cookies + '\'' +
                ", userAge='" + userAge + '\'' +
                ", timeout='" + timeout + '\'' +
                ", values=" + values +
                ", classNameReturn='" + classNameReturn + '\'' +
                '}';
    }
}
