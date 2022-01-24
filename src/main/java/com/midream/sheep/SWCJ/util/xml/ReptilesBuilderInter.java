package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.RootReptile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author midreamSheep
 * 爬虫构造器，构造一个爬虫对象
 * */
public interface ReptilesBuilderInter {
    //构建对象
    Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException;
    //获取接口所有方法
    Map<String,String> getFunction(String className) throws ClassNotFoundException;
    //获取池中对象
    Object getObject(String Key)  throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException;
}
