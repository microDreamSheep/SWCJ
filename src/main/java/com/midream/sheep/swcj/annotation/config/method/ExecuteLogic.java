package com.midream.sheep.swcj.annotation.config.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//运行期有效
@Target({ElementType.METHOD})//只能作用到方法上
public @interface ExecuteLogic {
    String processProgram();
    String executorKey() default "reg";
    boolean IsHtml() default false;
}
