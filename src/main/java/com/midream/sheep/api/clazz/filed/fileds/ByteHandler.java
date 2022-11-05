package com.midream.sheep.api.clazz.filed.fileds;

import com.midream.sheep.api.clazz.filed.FiledHandler;

public class ByteHandler implements FiledHandler {
    private byte value;
    private static final Class<?> aClass = byte.class;

    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public ByteHandler(byte value) {
        this.value = value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (byte) value;
    }
}
