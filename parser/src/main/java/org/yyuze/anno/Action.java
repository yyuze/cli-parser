package org.yyuze.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yanghanbo
 * @Date: 6/24/2020 10:43 AM
 */

/**
 * 用于标注动作 Handler 的注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Action {

    /**
     * @return 动作名字
     */
    String name();

    /**
     * @return 动作参数的缩写
     */
    String abbreviation() default "";

    /**
     * @return 动作参数的全称
     */
    String full() default "";

    /**
     * @return 动作参数的描述
     */
    String description() default "";

}
