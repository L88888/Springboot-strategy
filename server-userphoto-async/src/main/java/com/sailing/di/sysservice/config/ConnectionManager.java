package com.sailing.di.sysservice.config;

import lombok.Data;

import java.sql.*;

@Data
public class ConnectionManager {

    private static ConnectionManager instance;
    private  String url = "jdbc:oracle:thin:@10.162.121.144:1521:ORCL";
    private  String userName = "stdb";
    private  String password = "stdb_data123";
    private  String driverClass = "oracle.jdbc.driver.OracleDriver";

    public static ConnectionManager newInstance(){
        if(instance == null){
            instance = new ConnectionManager();
        }
        return instance;
    }

    public Connection connection(){
        Connection connection = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(this.url,this.userName,this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }


    public static void close(ResultSet rs, Statement stmt, Connection conn){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }


}
