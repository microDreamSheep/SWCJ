package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class IntHandler implements FiledHandler {
    private int value;
    private static final Class<?> aClass = int.class;
    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public IntHandler(int value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (int) value;
    }
}
