package com.midream.sheep.api.clazz.reflector;

import com.midream.sheep.swcj.util.function.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SWCJDefaultClazzBuilderReflection implements ClazzBuilderReflectionInter {
    @Override
    public Class<?> findClassByName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("你的类不存在"+e);
        }
    }

    @Override
    public Object newInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Method findSetMethod(Class<?> aClass, String methodName, Class<?>... parameterTypes) {
        try {
            return aClass.getMethod("set" + StringUtil.StringToUpperCase(methodName), parameterTypes);
        } catch (NoSuchMethodException e) {
            System.err.println("你的方法不存在"+e);
        }
        return null;
    }
}
