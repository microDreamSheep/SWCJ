package com.midream.sheep.SWCJ.util.xml;

import com.midream.sheep.SWCJ.Exception.EmptyMatchMethodException;
import com.midream.sheep.SWCJ.data.ReptileConfig;
import com.midream.sheep.SWCJ.data.swc.RootReptile;
/**
 * @author midreamSheep
 * 爬虫构造器，构造一个爬虫对象
 * */
public interface ReptilesBuilderInter {
    //构建对象
    Object Builder(RootReptile rr, ReptileConfig rc) throws EmptyMatchMethodException;
    //检查接口是否生效
    String checkFunction(String className, String id) throws ClassNotFoundException;
}
