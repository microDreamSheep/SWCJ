package com.midream.sheep.api.clazz;

import com.midream.sheep.api.clazz.filed.FiledHandler;
import com.midream.sheep.api.clazz.filed.fileds.StringHandler;
import com.midream.sheep.api.clazz.reflector.ClazzBuilderReflectionInter;
import com.midream.sheep.api.clazz.reflector.SWCJDefaultClazzBuilderReflection;

import java.lang.reflect.Method;
import java.util.*;

public class ClazzBuilder {
    private ClazzBuilderReflectionInter clazzBuilderReflectionInter;
    private Class<?> aClass;
    private final Map<String, List<FiledHandler>> dataMap = new LinkedHashMap<>();

    public ClazzBuilder(ClazzBuilderReflectionInter clazzBuidlerReflectionInter) {
        this.clazzBuilderReflectionInter = clazzBuidlerReflectionInter;
    }

    public ClazzBuilder() {
        this.clazzBuilderReflectionInter = new SWCJDefaultClazzBuilderReflection();
    }

    public void setClazzBuilderReflectionInter(ClazzBuilderReflectionInter clazzBuidlerReflectionInter) {
        this.clazzBuilderReflectionInter = clazzBuidlerReflectionInter;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setClass(String className) {
        setClass(clazzBuilderReflectionInter.findClassByName(className));
    }

    public void setClass(Class<?> aClass) {
        this.aClass = aClass;
    }
    public void addFiled(String filedName, FiledHandler filedHandler){
        List<FiledHandler> handlers = dataMap.get(filedName);
        if(handlers!=null){
            handlers.add(filedHandler);
            return;
        }
        List<FiledHandler> new_handlers = new LinkedList<>();
        new_handlers.add(filedHandler);
        dataMap.put(filedName,new_handlers);
    }
    public boolean removeFiled(String filedName, FiledHandler filedHandler){
        List<FiledHandler> handlers = dataMap.get(filedName);
        return handlers != null && handlers.remove(filedHandler);
    }

    public Object buildAObject(){
        Object object = clazzBuilderReflectionInter.newInstance(aClass);
        for (Map.Entry<String, List<FiledHandler>> entry : dataMap.entrySet()) {
            //反射获取方法
            String filedName = entry.getKey();
            Method setMethod = clazzBuilderReflectionInter.findSetMethod(aClass, filedName, entry.getValue().get(0).getaClass());
            //反射调用方法
            try {
                setMethod.invoke(object,entry.getValue().get(0).getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object;
    }
    public List<Object> buildObjects(){
        List<Object> objects = new LinkedList<>();
        //获取map中的最大数量
        int max = maxCount();
        for (int i = 0; i < max; i++) {
            objects.add(clazzBuilderReflectionInter.newInstance(aClass));
        }
        for (Map.Entry<String, List<FiledHandler>> entry : dataMap.entrySet()) {
            //反射获取方法
            String filedName = entry.getKey();
            Method setMethod = clazzBuilderReflectionInter.findSetMethod(aClass, filedName, entry.getValue().get(0).getaClass());
            List<FiledHandler> handlers = entry.getValue();
            for (int i = 0; i < handlers.size(); i++) {
                //反射调用方法
                try {
                    setMethod.invoke(objects.get(i),handlers.get(i).getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return objects;
    }

    public List<?> buildByMap(Map<String,List<String>> fields){
        for (Map.Entry<String, List<String>> aFiled : fields.entrySet()) {
            String filedName = aFiled.getKey();
            for (String filedValue : aFiled.getValue()) {
                this.addFiled(filedName, new StringHandler(filedValue));
            }
        }
        return buildObjects();
    }
    private int maxCount(){
        int max = 0;
        for (List<FiledHandler> value : dataMap.values()) {
            if(value.size()>max){
                max = value.size();
            }
        }
        return max;
    }
}
