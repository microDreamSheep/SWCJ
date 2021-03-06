package com.midream.sheep.swcj.pojo.buildup;

import java.util.*;

/**
 * @author midreamsheep
 */
public class SWCJClass {
    private String className;
    private String itIterface;
    private Map<String,SWCJMethod> methods;

    private byte[] codes;

    public String getItIterface() {
        return itIterface;
    }

    public void setItIterface(String itIterface) {
        this.itIterface = itIterface;
    }
    public String getClassName() {
        return className;
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

    public static SWCJClass buildClass(){
        return new SWCJClass();
    }

    public byte[] getCodes() {
        return codes;
    }

    public void setCodes(byte[] codes) {
        this.codes = codes;
    }
}
