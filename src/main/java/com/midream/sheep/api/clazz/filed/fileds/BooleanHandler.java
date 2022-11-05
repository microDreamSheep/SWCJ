package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class BooleanHandler implements FiledHandler {
    private boolean value;
    private static final Class<?> aClass = boolean.class;

    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (boolean) value;
    }

    public BooleanHandler(boolean value) {
        this.value = value;
    }
}
