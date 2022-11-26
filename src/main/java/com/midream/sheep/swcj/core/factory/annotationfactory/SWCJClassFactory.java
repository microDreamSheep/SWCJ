package com.midream.sheep.swcj.core.factory.annotationfactory;

import com.midream.sheep.swcj.Exception.ConfigException;
import com.midream.sheep.swcj.Exception.EmptyMatchMethodException;
import com.midream.sheep.swcj.Exception.InterfaceIllegal;
import com.midream.sheep.swcj.annotation.config.SWCJMetaConfig;
import com.midream.sheep.swcj.cache.CacheCorn;
import com.midream.sheep.swcj.core.APIClassInter.NullTag;
import com.midream.sheep.swcj.core.build.inter.SWCJBuilder;
import com.midream.sheep.swcj.core.classtool.classloader.SWCJClassLoaderInter;
import com.midream.sheep.swcj.core.classtool.compiler.SWCJCompiler;
import com.midream.sheep.swcj.core.factory.SWCJAbstractFactory;
import com.midream.sheep.swcj.core.factory.SWCJFactory;
import com.midream.sheep.swcj.pojo.buildup.SWCJClass;
import com.midream.sheep.swcj.pojo.swc.passvalue.ReptlileMiddle;

import java.lang.reflect.InvocationTargetException;

public abstract class SWCJClassFactory
    extends SWCJAbstractFactory
{
    @SuppressWarnings("unchecked")
    public <T> T parse(Class<T> interfaceClass) throws ConfigException, EmptyMatchMethodException, InterfaceIllegal {
        SWCJMetaConfig annotation = interfaceClass.getAnnotation(SWCJMetaConfig.class);
        if(annotation==null||annotation.id().trim().equals("")){
            throw new ConfigException("SWCJMetaConfig annotation is not found or id is empty");
        }
        if(CacheCorn.SPIDER_CACHE.getCacheSpider(annotation.id())!=null){
            return (T) CacheCorn.SPIDER_CACHE.getCacheSpider(annotation.id());
        }
        try {
            if(!annotation.className().equals(NullTag.class)){
                config.parse(annotation.className().getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ConfigException("some wrong in parseClass"+e.getMessage());
        }
        SWCJClass swcjClass = parseClass(interfaceClass);
        swcjClass.setId(annotation.id());
        return (T) CacheCorn.SPIDER_CACHE.addCacheSpider(annotation.id(),swcjBuilder.Builder(new ReptlileMiddle(swcjClass,config)));
    }
    protected abstract SWCJClass parseClass( Class<?> interfaceClass) throws ConfigException, InterfaceIllegal;


    @Override
    public SWCJClassFactory setCompiler(SWCJCompiler swcjCompiler) {
        return (SWCJClassFactory) super.setCompiler(swcjCompiler);
    }

    @Override
    public SWCJClassFactory setBuilder(SWCJBuilder swcjBuilder) {
        return (SWCJClassFactory) super.setBuilder(swcjBuilder);
    }

    @Override
    public SWCJClassFactory invokeSpecialMethod(Object... args) {
        return (SWCJClassFactory) super.invokeSpecialMethod(args);
    }

    @Override
    public SWCJClassFactory setClassLoader(SWCJClassLoaderInter classLoader) {
        return (SWCJClassFactory) super.setClassLoader(classLoader);
    }
}
