package com.midream.sheep.swcj.pojo.buildup;

import com.midream.sheep.swcj.data.Constant;

/**
 * @author midreamsheep
 */
public class SWCJValue {
    private String type;
    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return Constant.getClassName(type) +" "+name+ " = " + value + ';';
    }
}
