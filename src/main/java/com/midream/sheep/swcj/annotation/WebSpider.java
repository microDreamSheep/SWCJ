package com.midream.sheep.swcj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author midreamSheep
 * 描述是否是爬虫方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})//只能作用到方法上
public @interface WebSpider {
    String value();
}