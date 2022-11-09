package com.midream.sheep.swcj.pojo.buildup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author midreamsheep
 */
public class SWCJMethod {
    //方法额外的配置文件
    private String config;
    //请求类型
    private String requestType;
    //方法名
    private String name;
    private String methodName;
    private List<MethodHandler> vars;
    private String returnType;
    private String executeStr;

    private String paramIn;

    public String getParamIn() {
        return paramIn;
    }

    public void setParamIn(String paramIn) {
        this.paramIn = paramIn;
    }

    private List<String> executeVars = new LinkedList<>();

    public List<String> getExecuteVars() {
        return executeVars;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setExecuteVars(List<String> executeVars) {
        this.executeVars = executeVars;
    }

    public String getExecuteStr() {
        return executeStr;
    }

    public void setExecuteStr(String executeStr) {
        this.executeStr = executeStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String annotation) {
        this.name = annotation;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setVars(List<MethodHandler> vars) {
        this.vars = vars;
    }

    public List<MethodHandler> getVars() {
        return vars;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType.replace("class ","");
    }

    public String getReturnType() {
        if(returnType.startsWith("[L")){
            return returnType.substring(2,returnType.length()-1)+"[]";
        }
        return returnType;
    }

    @Override
    public String toString() {
        return "SWCJMethod{" +
                "name='" + name + '\'' +
                ", methodName='" + methodName + '\'' +
                ", vars=" + vars +
                ", returnType='" + returnType + '\'' +
                ", body='" + executeStr + '\'' +
                '}';
    }
}
