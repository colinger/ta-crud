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
 * 根据DO自动生成，相应的jsp页面
 * @author Colin
 * @version $Id: TaCrud.java, v0.1 25/12/2016 colingo Exp $$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TaCrudInfo {
    int order() default 0;

    String label(); //标题

    String path();//属性

    String idTag() default "";// html id属性

    boolean required() default true;//是否必填

    String desc() default "";//说明：限15个字
}
