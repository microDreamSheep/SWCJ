package com.midream.sheep.swcj.pojo.buildup;

import java.util.List;

/**
 * @author midreamsheep
 */
public class SWCJMethod {
    private String name;
    private String methodName;
    private List<String> vars;
    private String returnType;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public void setVars(List<String> vars) {
        this.vars = vars;
    }

    public List<String> getVars() {
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
}
