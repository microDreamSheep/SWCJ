package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class StringHandler implements FiledHandler {
    private String value;
    private static final Class<?> aClass = String.class;
    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public String getValue() {
        return value;
    }

    public StringHandler(String value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }
}
