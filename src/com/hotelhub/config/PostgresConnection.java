package com.hotelhub.config;
import java.sql.*;

public class PostgresConnection {

	public static Connection Connector() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hotelhub", "nagarjunmallesh", "");
            return conn;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
	
}
