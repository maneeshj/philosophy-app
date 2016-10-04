package com.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.json.JSONArray;

import com.project.app.PathToWiki;

public class DatabaseService {
	
	public static Connection GetConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "philosophy";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "maneesh";
        String password = "maneesh";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + dbName, userName, password);
        return conn;
    }
	
	public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
    }
	
	public void InsertPath(PathToWiki pathToWiki) {
		Connection conn = null;
		String query = "INSERT INTO philosophy.path(source, path, hop_count, timestamp)" + "VALUES (?, ?, ?, ?)";
		try{
			conn = GetConnection();
			JSONArray jsArray = new JSONArray(pathToWiki.Path);
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString (1, pathToWiki.SourcePage);
			preparedStmt.setString (2, jsArray.toString());
			preparedStmt.setInt(3, pathToWiki.GetHopCount());
			preparedStmt.setTimestamp(4, new Timestamp(new Date().getTime()));
			preparedStmt.execute();
		} catch(Exception e){
			System.out.println(e.getMessage());
		} finally {
			closeConnection(conn);
		}
	}
}
