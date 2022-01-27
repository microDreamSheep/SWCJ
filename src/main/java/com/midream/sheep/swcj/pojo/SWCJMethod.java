package com.midream.sheep.swcj.pojo;

import java.util.List;

/**
 * @author midreamsheep
 */
public class SWCJMethod {
    private String annotation;
    private String methodName;
    private List<String> vars;
    private String returnType;

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
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
