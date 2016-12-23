/**
 * LY.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.ly.ta.tool;

import java.sql.*;

/**
 * @author gxy23996
 * @version $Id: JdbcUtils.java, v0.1 2016/12/23 gxy23996 Exp $$
 */
public class JdbcUtils {
    //数据库用户名
    private static final String USERNAME = "tcstpermission";
    //数据库密码
    private static final String PASSWORD = "oTdXfRYDS3TDv2tRZ3PR";
    //驱动信息
    private static final String DRIVER   = "com.mysql.jdbc.Driver";
    //数据库地址
    private static final String URL      = "jdbc:mysql://10.100.158.93:3500/TCSmartTravelPermission";
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

    public void test(String table) throws SQLException {
        try {
            PreparedStatement pstm = connection
                .prepareStatement("SELECT a.COLUMN_NAME,a.COLUMN_COMMENT FROM information_schema.`COLUMNS` a WHERE  a" + ".TABLE_NAME='" + table + "'");
            resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + ":" + resultSet.getString(2));
            }
        } finally {
            releaseConn();
        }
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
        JdbcUtils jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
        jdbcUtils.test("ta_admin_user");

    }
}