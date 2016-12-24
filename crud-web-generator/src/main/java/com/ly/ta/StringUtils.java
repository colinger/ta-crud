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

    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
}
