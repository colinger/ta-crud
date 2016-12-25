/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta.tool;

import com.ly.ta.CrudGenerator;
import com.ly.ta.FieldInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gxy23996
 * @version $Id: JdbcUtils.java, v0.1 2016/12/23 gxy23996 Exp $$
 */
public class JdbcUtils {
    //数据库用户名
    private static final String USERNAME = "root";                            //"tcstpermission";
    //数据库密码
    private static final String PASSWORD = "admin";                           //"oTdXfRYDS3TDv2tRZ3PR";
    //驱动信息
    private static final String DRIVER   = "com.mysql.jdbc.Driver";
    //数据库地址
    private static final String URL      = "jdbc:mysql://localhost:3306/test";//"jdbc:mysql://10.100.158.93:3500/TCSmartTravelPermission";
    private Connection          connection;
    private PreparedStatement   pstmt;
    private ResultSet           resultSet;

    public JdbcUtils() {
        // TODO Auto-generated constructor stub
        try {
            Class.forName(DRIVER);

        } catch (Exception e) {

        }
    }

    /**
     * 获得数据库的连接
     * @return
     */
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("数据库连接成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public List<FieldInfo> test(String table) throws SQLException {
        List<FieldInfo> fieldInfoList = new ArrayList<>();
        this.getConnection();
        try {
            PreparedStatement pstm = connection
                .prepareStatement("SELECT a.COLUMN_NAME,a.DATA_TYPE, a.COLUMN_COMMENT FROM information_schema.`COLUMNS` a WHERE  a" + ".TABLE_NAME='" + table + "'");
            resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + ":" + resultSet.getString(2));
                FieldInfo fieldInfo = new FieldInfo(resultSet.getString(1), resultSet.getString(2), false, false);
                fieldInfoList.add(fieldInfo);
            }
        } finally {
            releaseConn();
        }
        return fieldInfoList;
    }

    /**
     * 释放数据库连接
     */
    public void releaseConn() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        // TODO Auto-generated method stub

        try {
            new CrudGenerator().generate("ta_admin_user", "/Users/colingo/Documents/data/", "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
