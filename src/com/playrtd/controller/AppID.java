package com.playrtd.controller;
import java.io.IOException;
import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gc.dto.ProductDto;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class AppID {
public static void main(String[] args) {
	
	Configuration cfg = new Configuration();
	cfg.configure("hibernate.cfg.xml");
	SessionFactory factory = cfg.buildSessionFactory();
	
	
int[]tags = {19,21, 492, 3859, 113, 1743, 3871, 7368, 1625, 1685, 4158, 3841, 3843, 4840, 128, 4182, 1662, 4085};
String[] gameNames = new String[25];
	
	for (int j = 0; j < tags.length-1; j++) {
	String gameCollector = "http://store.steampowered.com/search/?tags=" + tags[j] + "&page=1";
	System.out.println(gameCollector);
	///////////////////////////////////NAMES COLLECTED HERE///////////////////////////////////////////////////////////
	try {
		Document doc = Jsoup.connect(gameCollector).get();
		// this grabs the div containing all of the game names
		Elements temp = doc.select("div.col.search_name.ellipsis");
		int i=0;
		for(Element gameList: temp) {
			i++;
			//span is where the titles are held
			System.out.println(i +  " " + gameList.getElementsByTag("span").first().text());
			gameNames[i-1] = gameList.getElementsByTag("span").first().text();
		}
		
		
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	//////////////////////////////////APP IDS COLLECTED HERE - IMAGE IS DONE FROM THE APP ID//////////////////////////////////////////////
	try {
		//TODO to grab as many games as we want, make a string based on the URL. 
		//create an int for tag and page to continue to dig through site for data base
		Document doc = Jsoup.connect(gameCollector).get();
		int tag = 19;
		
		Elements temp = doc.select("div#search_result_container");
		String[] images = new String[25];
		String div ="";
		int i=0;
		String[] ID = new String[25]; // holds game ID
		String[] games = new String[25]; // temp array to hold game info from jSoup
		//This code grabs all of the information within the div of the 25 games showing.
		for(Element gameList: temp) {
			div = gameList.getElementsByTag("div").first().toString();
			i++;
		}
		games = div.split("<div style=\"clear:");

		//This code first splits the text to the number of values we need in the array. 
		//Then further splices it with substrings to grab the APPID
		for (i=0; i < 25; i++) {
			ID[i] = (games[i].substring(games[i].indexOf("appid=\"")+7,games[i].indexOf("\" onmouseover=\""))); // ID takes the info from temp array 
			images[i] = "http://cdn.edgecast.steamstatic.com/steam/apps/" +ID[i] +"/header.jpg";
			System.out.println(i + " " + ID[i]);
		}
		System.out.println(tag);
		System.out.println(ID[1]);
		System.out.println(images[1]);
		System.out.println(gameNames[1]);
		for (i=0; i < 25; i++) {
		ProductDto Action = new ProductDto();
		
		Action.setTag(tags[j]);
		Action.setGameName(gameNames[i]);
		Action.setAppID(ID[i]);
		Action.setImage(images[i]);
		
		Session session = factory.openSession();
		Transaction t = (Transaction) session.beginTransaction();
		System.out.println(i);
		session.persist(Action);//problem child
		t.commit();
		session.close();

		System.out.println("data inserted");
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
}
}