package com.midream.sheep.swcj.pojo;

import java.util.*;

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
    private List<String> value;
    public String getClassName() {
        return className;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public void addValue(String value) {
        if(this.value==null){
            this.value = new ArrayList<>();
        }
        this.value.add(value);
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
