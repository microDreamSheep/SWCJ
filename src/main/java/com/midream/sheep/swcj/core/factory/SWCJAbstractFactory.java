package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.swc.RootReptile;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author midreamsheep
 */
public abstract class  SWCJAbstractFactory implements SWCJXmlFactory{
    //核心配置文件
    protected ReptileConfig rc = new ReptileConfig();
    //爬虫文件
    protected Map<String, RootReptile> rootReptiles = new HashMap<>();
    //构造器
    protected SWCJBuilder swcjBuilder = null;
    //解析器
    protected SWCJParseI swcjParseI = null;
    @Override
    public Object getWebSpiderById(String id) throws ConfigException, EmptyMatchMethodException, InterfaceIllegal {
        //效验池中是否存在,如果存在直接返回
        Object object = null;
        int i = 0;
        while (true) {
            try {
                object = BuildTool.getObjectFromTool(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (object != null) {
                return object;
            }
            RootReptile rootReptile = rootReptiles.get(id);
            if(rootReptile==null){
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    i++;
                } catch (InterruptedException e) {
                    Logger.getLogger(SWCJAbstractFactory.class.getName()).warning("线程暂停失败");
                }
                if(i==10){
                    throw new ConfigException("你的配置文件找不到id="+id);
                }
                continue;
            }
            if(rootReptile.isLoad()){
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    Logger.getLogger(SWCJAbstractFactory.class.getName()).warning("线程暂停失败");
                }
            }else {
                rootReptiles.get(id).setLoad(true);
                return swcjBuilder.Builder(new ReptlileMiddle(rootReptiles.get(id),rc));
            }
        }
    }
    @Override
    public SWCJXmlFactory setCompiler(SWCJCompiler swcjCompiler) {
        if(swcjBuilder==null){
            swcjBuilder = new ReptilesBuilder();
        }
        this.swcjBuilder.setCompiler(swcjCompiler);
        return this;
    }
    @Override
    public SWCJXmlFactory setBuilder(SWCJBuilder swcjBuilder) {
        this.swcjBuilder = swcjBuilder;
        return this;
    }
    @Override
    public SWCJXmlFactory setParseTool(SWCJParseI swcjParseI) {
        this.swcjParseI = swcjParseI;
        return this;
    }

    @Override
    public SWCJXmlFactory invokeSpecialMethod(Object... args) {
        System.err.println("此工厂不存在特殊方法");
        return this;
    }

    @Override
    public SWCJXmlFactory setClassLoader(SWCJClassLoaderInter classLoader) {
        if(this.swcjBuilder==null){
            this.swcjBuilder = new ReptilesBuilder();
        }
        swcjBuilder.setClassLoader(classLoader);
        return null;
    }
}
