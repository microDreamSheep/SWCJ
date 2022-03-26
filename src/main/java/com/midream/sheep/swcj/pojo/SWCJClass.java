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
    private ArrayList<SWCJMethod> methods;
    private Map<String,String> value;
    public String getClassName() {
        return className;
    }

    public Map<String, String> getValue() {
        return value;
    }

    public void setValue(Map<String, String> value) {
        this.value = value;
    }

    public void addValue(String filed,String value) {
        if(this.value==null){
            this.value = new HashMap<>();
        }
        this.value.put("private "+filed+(value.equals("")?"":"="),value);
    }
    public void addMethod(SWCJMethod swcjMethod) {
        if(this.methods==null){
            this.methods = new ArrayList<>();
        }
        if(swcjMethod!=null){
            methods.add(swcjMethod);
        }
    }

        public void setClassName(String className) {
        if (className!=null&&!className.equals("")) {
            this.className = className;
        }else{
            this.className = "a" + UUID.randomUUID().toString().replace("-", "");
        }
    }


    public ArrayList<SWCJMethod> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<SWCJMethod> methods) {
        this.methods = methods;
    }
}
