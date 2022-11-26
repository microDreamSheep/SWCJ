package com.midream.sheep.swcj.annotation.config;

import com.midream.sheep.swcj.core.APIClassInter.NullTag;
import com.midream.sheep.swcj.core.APIClassInter.SWCJConfigClassConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})//只能作用到类上
public @interface SWCJMetaConfig {
    String id();
    Class<? extends SWCJConfigClassConfiguration> className() default NullTag.class;
}
