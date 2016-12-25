/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CRUD标识
 * @author Colin
 * @version $Id: TaCrud.java, v0.1 25/12/2016 colingo Exp $$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TaCrud {
    String pageTitle() default ""; //标题

    String commandName() default "";

    String viewName() default "";
}
