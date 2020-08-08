package org.yyuze.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yanghanbo
 * @Date: 6/20/2020 4:07 PM
 */


/**
 * 用于定义参数的注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Argument {

    /**
     * @return 参数名缩写
     */
    String abbreviation() default "";

    /**
     * @return 参数名全名
     */
    String full() default "";

    /**
     * @return 参数是否必要
     */
    boolean required() default false;

    /**
     * @return 参数描述
     */
    String description() default "";

    /**
     * @return 参数是否有变量
     */
    boolean hasArgument() default false;

    /**
     * @return 参数的变量名称
     */
    String argumentName() default "";

    /**
     * @return 参数变量的数量
     */
    int numberOfArgs() default 1;

}
