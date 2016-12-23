/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta;

/**
 * @author gxy23996
 * @version $Id: StringUtils.java, v0.1 2016/12/23 gxy23996 Exp $$
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * @param o the object
     * @return null if o is null, else o.toString()
     */
    public static String toString(Object o) {
        return o == null ? null : o.toString();
    }

    /**
     * @param name the name (singular form)
     * @return the plural form of the name
     */
    public static String plural(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        String str = name.endsWith("y") ? name.substring(0, name.length() - 1) + "ies" : name.endsWith("s") ? name + "es" : name + "s";
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
