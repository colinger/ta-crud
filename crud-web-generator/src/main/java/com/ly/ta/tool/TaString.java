/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta.tool;

/**
 * @author Colin
 * @version $Id: TaString.java, v0.1 24/12/2016 colingo Exp $$
 */
public enum TaString {
                      bigint, varchar, datetime, tinybit;
    public static TaString getType(String type) {
        return valueOf(type.toLowerCase());
    }
}
