package com.midream.sheep.swcj.core.factory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.build.builds.javanative.BuildTool;
import com.midream.sheep.swcj.core.build.builds.javanative.ReptilesBuilder;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoader;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.factory.parse.bystr.BetterXmlParseTool;
import com.midream.sheep.swcj.core.factory.parse.bystr.SWCJParseI;
import com.midream.sheep.swcj.data.Constant;
import com.midream.sheep.swcj.data.ReptileConfig;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;
import com.midream.sheep.swcj.util.function.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author midreamsheep
 */
public abstract class  SWCJAbstractFactory implements SWCJXmlFactory{
    //核心配置文件
    public static volatile ReptileConfig config = new ReptileConfig();
    //爬虫文件
    protected Map<String, SWCJClass> swcjClasses = new HashMap<>();
    //构造器
    protected SWCJBuilder swcjBuilder = null;
    //解析器
    protected SWCJParseI swcjParseI = null;
    //加载器
    protected SWCJClassLoaderInter classLoader = null;
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
            SWCJClass rootReptile = swcjClasses.get(id);
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
                swcjClasses.get(id).setLoad(true);
                swcjClasses.remove(swcjClasses.get(id));
                return swcjBuilder.Builder(new ReptlileMiddle(swcjClasses.get(id), config));
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
        this.classLoader = classLoader;
        swcjBuilder.setClassLoader(classLoader);
        return null;
    }
    //读取配置文件中的对象
    protected void cache(String workplace) throws ConfigException {
        //通过io流读取文件
        File file = new File(workplace+"/ClassCatch.swcj");
        if(!file.exists()) {
            return;
        }
        String key_value = StringUtil.getStringByStream(file);
        if(key_value.trim().equals("")){
            return;
        }
        for (String s : key_value.split("\n")) {
            String[] split = s.split("=");
            String className = Constant.DEFAULT_PACKAGE_NAME+"."+ split[0];
            String id = split[1];
            if(CacheCorn.SPIDER_CACHE.getCacheSpider(id)!=null){
                continue;
            }
            try {
                byte[] bytesByStream = StringUtil.getBytesByStream(new FileInputStream(new File(workplace + "/class/" + className.replace(Constant.DEFAULT_PACKAGE_NAME+".","") + ".class")));
                //加载类
                if(classLoader==null){
                    classLoader = new SWCJClassLoader();
                    setClassLoader(classLoader);
                }
                //将类放入池中
                CacheCorn.SPIDER_CACHE.addCacheSpider(id, classLoader.loadData(className, bytesByStream).getDeclaredConstructor().newInstance());
                //删除id为id的的rootReptile
                swcjClasses.remove(id);
            } catch (FileNotFoundException e) {
                throw new ConfigException("找不到缓存文件");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException e) {
                throw new RuntimeException("加载类异常");
            }
        }
        SWCJParseI.count.addAndGet(key_value.split("\n").length);
    }
}
