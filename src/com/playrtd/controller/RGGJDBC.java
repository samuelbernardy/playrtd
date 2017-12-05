package com.playrtd.controller;

import java.sql.DriverManager;

import com.mysql.jdbc.ResultSetImpl;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import org.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.nimbusds.oauth2.sdk.client.ClientInformation;



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

