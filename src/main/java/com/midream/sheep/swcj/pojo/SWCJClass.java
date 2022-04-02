package com.midream.sheep.swcj.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author midreamsheep
 */
public final class SWCJClass {
    private String className;

    public String getItIterface() {
        return itIterface;
    }

    public void setItIterface(String itIterface) {
        this.itIterface = itIterface;
    }

    private String itIterface;
    private Map<String,SWCJMethod> methods;
    private Map<String,String> value;
    public String getClassName() {
        return className;
    }

    public String getValue(String name) {
        return value.get(name);
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    public void addValue(String filed,String value) {
        if(this.value==null){
            this.value = new HashMap<>();
        }
        this.value.put(filed,"private static "+filed+(value.equals("")?"":(""+value))+";");
    }
    public void addMethod(String name,SWCJMethod swcjMethod) {
        if(this.methods==null){
            this.methods = new HashMap<>();
        }
        if(swcjMethod!=null){
            methods.put(name,swcjMethod);
        }
    }

        public void setClassName(String className) {
        if (className!=null&&!className.equals("")) {
            this.className = className;
        }else{
            this.className = "a" + UUID.randomUUID().toString().replace("-", "");
        }
    }

    public Map<String, SWCJMethod> getMethods() {
        return methods;
    }

    public void setMethods(Map<String, SWCJMethod> methods) {
        this.methods = methods;
    }
}
