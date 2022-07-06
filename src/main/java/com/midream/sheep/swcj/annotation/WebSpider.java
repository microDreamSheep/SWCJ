package com.midream.sheep.swcj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author midreamSheep
 * 描述爬虫方法
 */
@Retention(RetentionPolicy.RUNTIME)//运行期有效
@Target({ElementType.METHOD})//只能作用到方法上
public @interface WebSpider {
    /**
     * 定义的配置名称
     * */
    String value();
}