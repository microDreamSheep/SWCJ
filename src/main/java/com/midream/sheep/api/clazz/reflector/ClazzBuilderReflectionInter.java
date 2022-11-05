package com.midream.sheep.api.clazz.reflector;

import java.lang.reflect.Method;

public interface ClazzBuilderReflectionInter {
    Class<?> findClassByName(String className);

    Object newInstance(Class<?> aClass);

    Method findSetMethod(Class<?> aClass, String methodName, Class<?>... parameterTypes);
}
