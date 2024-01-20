package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtils {
	Connection conn=null;
	public Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		 conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "admin", "password1");
		return conn;
	}

}
