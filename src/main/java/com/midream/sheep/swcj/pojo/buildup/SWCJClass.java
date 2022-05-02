package com.midream.sheep.swcj.pojo.buildup;

import java.util.*;

/**
 * @author midreamsheep
 */
public final class SWCJClass {
    private String className;
    private String itIterface;
    private Map<String,SWCJMethod> methods;
    private List<SWCJValue> value;


    public String getItIterface() {
        return itIterface;
    }

    public void setItIterface(String itIterface) {
        this.itIterface = itIterface;
    }
    public String getClassName() {
        return className;
    }

    public List<SWCJValue> getValue() {
        return value;
    }

    public void setValue(List<SWCJValue> value) {
        this.value = value;
    }

    public void addValue(SWCJValue value) {
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
        if(methods==null){
            methods = new HashMap<>();
        }
        return methods;
    }

    public void setMethods(Map<String, SWCJMethod> methods) {
        this.methods = methods;
    }
}
