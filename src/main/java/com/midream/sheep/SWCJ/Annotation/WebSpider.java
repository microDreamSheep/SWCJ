package com.midream.sheep.SWCJ.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})//只能作用到方法上
@Retention(RetentionPolicy.RUNTIME)//定义运行策略
public @interface WebSpider {
    public String value();
}