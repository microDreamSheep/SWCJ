package com.midream.sheep.swcj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author midreamSheep
 * 参数配置类
 */
@Retention(RetentionPolicy.RUNTIME)//运行期有效
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})//只能作用到方法上
public @interface Param {
    String value();
}