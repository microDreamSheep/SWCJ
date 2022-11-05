package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class LongHandler implements FiledHandler {
    private long value;
    private static final Class<?> aClass = long.class;
    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public LongHandler(long value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (long) value;

    }
}
