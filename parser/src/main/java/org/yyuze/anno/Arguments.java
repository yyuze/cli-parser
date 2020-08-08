package org.yyuze.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: yanghanbo
 * @Date: 6/24/2020 11:10 AM
 */


/**
 * 用于标注 Options 定义类的注释
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Arguments {

    String action();

}
