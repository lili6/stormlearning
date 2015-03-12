package com.lili.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: maolin.luo
 * Date: 14-8-15
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IPWhiteList {

    String [] whiteList() default {};
}
