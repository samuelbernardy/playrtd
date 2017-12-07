package com.playrtd.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gc.dto.ProductDto;

public class AppID {
	
	// regex to eliminate emojis😱 ™® foreign characters  ポイントサービス用シリアルナンバー" and other special characters so mysql will be happy
	public static String regexChecker(String str2Check) {
		Pattern checkRegex = Pattern.compile("[a-zA-Z0-9\\t\\n ./<>?;:\"'`!@#$%^&*()\\[\\]{}_+=|\\\\-]");
		Matcher regexMatcher = checkRegex.matcher(str2Check);
		str2Check = "";
		while (regexMatcher.find()) {
			if(regexMatcher.group().length() !=0){
				//System.out.print(regexMatcher.group());
				str2Check+=regexMatcher.group();
		
		}
		}
		return str2Check;
	}
	public static void main(String[] args) {
		String test = "KILL THE EMOJI 😱";
		// hibernate and SQL connections
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();

		////// various variables being used


//		int[] tags = { 19, 21, 492, 3859, 113, 1743, 3871, 7368, 1625, 1685, 4158, 3841, 3843, 4840, 128, 4182, 1662,
//				4085, 4736 }; // category tags of games
//
//		String[] gameNames = new String[25]; // names of games
//		String[] gameDesc = new String[25];// games descriptions
//		String[] images = new String[25];// games image urls
//		String[] ID = new String[25]; // holds game ID
		// String[] discordURLS = new String[25]; // holds URLS for discord server

		
//		int[] tags = {19, 21, 492, 3859, 113, 1743, 3871, 7368, 1625, 1685, 4158, 3841, 3843, 4840, 128, 4182, 1662,
//				4085, 4736 }; // category tags of games
		String[] tempHolder = new String[338]; // temp holder for parcing
		String[] gameTag = new String[338]; // tag numerical value
		String[] gameTagName = new String[338]; //tag names
		String[] gameNames = new String[25]; // names of games
		String[] gameDesc = new String[25];// games descriptions
		String[] images = new String[25];// games image urls
		String[] AppID = new String[25]; // holds game ID
//		String[] discordURLS = new String[25]; // holds URLS for discord server

		//////

	

			/////////////////////////////////// NAMES COLLECTED
			/////////////////////////////////// HERE///////////////////////////////////////////////////////////
			try {
				
				String div = "";
				String divs = "";
				Document doc = Jsoup.connect("http://store.steampowered.com/tag/browse/#global_6310").get();
				
				//Elements temp = doc.select("div#tag_browse_global.tag_browse_tags");
				Elements temp = doc.select("div.tag_browse_tag");
				int i=0;
				for(Element movieList: temp) {
					i++;
				//	System.out.println(i +  " " + movieList.getAllElements().first().toString());
					div = movieList.getAllElements().first().toString();
					divs = movieList.getAllElements().first().text();
					tempHolder[i-1] = div;
					gameTagName[i-1] = divs;
					System.out.println(gameTagName[i-1]);
					//System.out.println(games[i-1]);
				}
				
				//games[i-1] = div.split("</div>");
				
				// This code first splits the text to the number of values we need in the array.
				// Then further splices it with substrings to grab the APPID
				for (i = 0; i < 338; i++) {
					gameTag[i] = (tempHolder[i].substring(tempHolder[i].indexOf("data-tagid=\"") + 12,
							tempHolder[i].indexOf("\">"))); // ID takes the info from temp array
				
				System.out.println(gameTag[i]);
				}
				for (int j = 0; j < gameTag.length - 1; j++) {
					String gameCollector = "http://store.steampowered.com/search/?tags=" + gameTag[j] + "&page=1";
				doc = Jsoup.connect(gameCollector).get();
				// this grabs the div containing all of the game names


				temp = doc.select("div.col.search_name.ellipsis");
				i=0;  // used for iterations

				for (Element gameList : temp) {
					i++;
					// span is where the titles are held
					gameNames[i-1] = regexChecker(gameList.getElementsByTag("span").first().text());
					//gameNames[i - 1] = new String (gameList.getElementsByTag("span").first().text().getBytes(), "ISO-8859-1");
				}

				////////////////////////////////// APP IDS COLLECTED HERE - IMAGE IS DONE FROM
				////////////////////////////////// THE APP
				////////////////////////////////// ID//////////////////////////////////////////////

				doc = Jsoup.connect(gameCollector).get();

				temp = doc.select("div#search_result_container");

				

				String[] games; // temp array to hold game info from jSoup
				// This code grabs all of the information within the div of the 25 games
				// showing.
				for (Element gameList : temp) {
					div = gameList.getElementsByTag("div").first().toString();

				}
				games = div.split("<div style=\"clear:");

				// This code first splits the text to the number of values we need in the array.
				// Then further splices it with substrings to grab the APPID
				for (i = 0; i < games.length-2; i++) {
					AppID[i] = (games[i].substring(games[i].indexOf("appid=\"") + 7,
							games[i].indexOf("\" onmouseover=\""))); // ID takes the info from temp array

					///////////////// Images are gathered here ///////////////////
					images[i] = "<img src=\"http://cdn.edgecast.steamstatic.com/steam/apps/" + AppID[i] + "/header.jpg\">";

					String gameURL = "http://store.steampowered.com/app/" + AppID[i];
					doc = Jsoup.connect(gameURL).get();
					// this grabs the div containing game description
					Elements gamed = doc.select("div#game_area_description.game_area_description");

					////////////// Descriptions collected here/////////////////////
					gameDesc[i]=regexChecker(gamed.text());
					//gameDesc[i] = new String(gamed.text().getBytes(), "ISO-8859-1");

					/*
					 * discord server code. Doesn't work entirely due to not all games offering
					 * 
					 * 
					 * gameURL = "https://www.google.com/search?q=discord server for " +
					 * gameNames[i]; doc = Jsoup.connect(gameURL).get(); // this grabs the div
					 * containing all of the game names Elements discord = doc.select("cite._Rm");
					 * System.out.println(discord.get(0).text()); discordURLS[i] =
					 * discord.get(0).text();
					 */

					ProductDto Action = new ProductDto();

					
					Action.setTag(gameTag[j]);
					Action.setTagName(gameTagName[j]);

					Action.setGameName(gameNames[i]);
					Action.setAppID(AppID[i]);
					Action.setImage(images[i]);

					// created if statement to find games with no description due to age
					// confirmations
					if (gameDesc[i].length() < 5) {
						Action.setDescription(gameDesc[i] + "This game is for mature audiences only. Please: "
								+ "<a href=\"" + gameURL + "\">click here</a>" + " for a detailed description.");
					} else {
						Action.setDescription(gameDesc[i]);
					}
					// Action.setDiscord(discordURLS[i]);
					// System.out.println(discordURLS[i]);
					Session session = factory.openSession();
					Transaction t = (Transaction) session.beginTransaction();

					session.persist(Action);// problem child
					t.commit();
					session.close();

					System.out.println("data inserted");
				}

			} }catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		System.out.println("ALL DONE!!!!!!!");
	}
}