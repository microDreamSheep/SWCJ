package com.midream.sheep.swcj.pojo.buildup;

public class MethodHandler {
    String methodName;
    String methodType;

    @Override
    public String toString() {
        return "MethodHandler{" +
                "methodName='" + methodName + '\'' +
                ", methodType='" + methodType + '\'' +
                '}';
    }

    public String getMethodName() {
        return methodName;
    }

    public MethodHandler(String methodName, String methodType) {
        this.methodName = methodName;
        this.methodType = methodType;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }
}
