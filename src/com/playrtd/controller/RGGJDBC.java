package com.playrtd.controller;

import java.sql.DriverManager;

import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.Statement;

import java.sql.Connection;




public class RGGJDBC {

	public static void main(String[] args) {
		ResultSetImpl resultSet = null;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gametest","gcjdbc","gcjdbc");
			Statement stmt = (Statement) con.createStatement();
			String sql ="SELECT * FROM gametest.games\nwhere tag = 19\nORDER BY RAND()\nLIMIT 1";
			
			
			resultSet = (ResultSetImpl) stmt.executeQuery(sql);
			while(resultSet.next()){
			resultSet.getInt("tag");
			resultSet.getString("gameName");
			resultSet.getString("appID");
			
			System.out.println(resultSet.getInt("tag") + " " + resultSet.getString("gameName"));
		

			}
			con.close();
	} catch (Exception e) {
		e.printStackTrace();
		}
}
	}

