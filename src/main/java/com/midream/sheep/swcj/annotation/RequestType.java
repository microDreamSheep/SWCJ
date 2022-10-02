package com.midream.sheep.swcj.annotation;

import com.midream.sheep.swcj.core.executetool.execute.SRequest;

import java.lang.annotation.*;

/**
 * @author midreamSheep
 * 描述爬虫方法
 * 需要在配置文件中加入使用注解分析功能
 */
@Retention(RetentionPolicy.RUNTIME)//运行期有效
@Target({ElementType.METHOD})//只能作用到方法上
public @interface RequestType {
    SRequest value();
}
