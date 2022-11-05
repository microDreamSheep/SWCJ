package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class ShortHandler implements FiledHandler {
    private static final Class<?> aClass = short.class;
    private short value;
    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public ShortHandler(short value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (short) value;
    }
}
