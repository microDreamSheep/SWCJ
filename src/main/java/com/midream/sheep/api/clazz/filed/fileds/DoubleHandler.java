package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class DoubleHandler implements FiledHandler {

    private double value;
    private static final Class<?> aClass = double.class;
    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public DoubleHandler(double value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (double) value;
    }
}
