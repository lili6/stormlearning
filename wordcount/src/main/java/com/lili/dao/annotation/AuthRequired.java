/*
 * Copyright (c) 2013 Qunar.com. All Rights Reserved.
 */

package com.lili.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @author maolin.luo created on 2013-12-30 下午4:41:39
 * @version 1.0.0
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {

}
