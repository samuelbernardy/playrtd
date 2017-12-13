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
	
	// regex to eliminate emojisðŸ˜± â„¢Â® foreign characters  ãƒ�ã‚¤ãƒ³ãƒˆã‚µãƒ¼ãƒ“ã‚¹ç”¨ã‚·ãƒªã‚¢ãƒ«ãƒŠãƒ³ãƒ�ãƒ¼" and other special characters so mysql will be happy
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
		String test = "KILL THE EMOJI ðŸ˜±";
		// hibernate and SQL connections
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();

		////// various variables being used

		
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
		String tempImg = "";
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
				for(Element gameList: temp) {
					i++;
				//	System.out.println(i +  " " + movieList.getAllElements().first().toString());
					div = gameList.getAllElements().first().toString();
					divs = gameList.getAllElements().first().text();
					tempHolder[i-1] = div;
					gameTagName[i-1] = divs;
					System.out.println(gameTagName[i-1]);
					//System.out.println(games[i-1]);
				}
				
				
				
				// This code first splits the text to the number of values we need in the array.
				// Then further splices it with substrings to grab the APPID
				for (i = 0; i < 338; i++) {
					gameTag[i] = (tempHolder[i].substring(tempHolder[i].indexOf("data-tagid=\"") + 12,
							tempHolder[i].indexOf("\">"))); // ID takes the info from temp array
				
				System.out.println(gameTag[i]);
				}
				for (int j = 0; j < gameTag.length; j++) {
					
					String gameCollector = "http://store.steampowered.com/search/?tags=" + gameTag[j] + "&category1=998"; //category1=998 references games to filter out as much non-conforming information as possible
				doc = Jsoup.connect(gameCollector).get();
				// this grabs the div containing all of the game names


				temp = doc.select("div.col.search_name.ellipsis");
				i=0;  // used for iterations

				for (Element gameList : temp) {
					i++;
					// span is where the titles are held
					gameNames[i-1] = regexChecker(gameList.getElementsByTag("span").first().text());
					
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
							games[i].indexOf("\" onmouseover=\"")));// ID takes the info from temp array
					
					String descURL = (games[i].substring(games[i].indexOf("href=\"") + 6,
							games[i].indexOf("\" data-ds")));
					System.out.println(descURL);
					///////////////// Images are gathered here ///////////////////
					images[i] = "<img src=\"http://cdn.edgecast.steamstatic.com/steam/apps/" + AppID[i] + "/header.jpg\">";

					//String gameURL = "http://store.steampowered.com/app/" + AppID[i];
					doc = Jsoup.connect(descURL).get();
					// this grabs the div containing game description
					Elements gamed = doc.select("div#game_area_description.game_area_description");

					////////////// Descriptions collected here/////////////////////
					gameDesc[i]=regexChecker(gamed.text());
					// this grabs the div containing all of the game names
					System.out.println(gameNames[i]);
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

					Action.setStoreURL(descURL);
					Action.setTag(gameTag[j]);
					Action.setTagName(gameTagName[j]);
					if (gameNames[i].contains("PAYDAY 2: Ultimate")) {
						Action.setAppID("3756");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/bundles/3756/reabw92hwgdybyeh/header_586x192.jpg?t=1511791360\">");
					}
					else if (gameNames[i].contains("Resident Evil 6")) {
						Action.setAppID("50980");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/50980/header_586x192.jpg?t=1471550108\">");
					}							
					else if (gameNames[i].contains("Broken Sword Trilogy")){
						Action.setAppID("6447");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/6447/header_586x192.jpg?t=1447446814\">");
					}
					else if (gameNames[i].contains("Counter-Strike 1 Anthology")) {
						Action.setAppID("235");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/bundles/235/ht2e5g49jgepbgou/header_586x192.jpg?t=1465510801\">");
					}
					else if (gameNames[i].contains("Valve Complete Pack")) {
						Action.setAppID("232");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/bundles/232/552et9ppfs81wxtf/header_586x192.jpg?t=1456860295\">");
					}
					else if (gameNames[i].contains("Port Royale 3 Gold")) {
						Action.setAppID("29658");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/29658/header_586x192.jpg?t=1447451035\">");
					}
					else if (gameNames[i].contains("The Sims 3 Plus Pets")) {
						Action.setAppID("19071");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/19071/header_586x192.jpg?t=1447449986\">");
					}
					else if (gameNames[i].contains("Mount & Blade Full Collection")) {
						Action.setAppID("50292");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/50292/header_586x192.jpg?t=1447456183\">");
					}
					else if (gameNames[i].contains("Wildlife Park 2 - Ultimate Edition")) {
						Action.setAppID("46418");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/46418/header_586x192.jpg?t=1500499275\">");
					}
					else if (gameNames[i].contains("METAL GEAR SOLID V: The Definitive Experience")) {
						Action.setAppID("132479");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/132479/header_586x192.jpg?t=1480700517\">");
					}
					else if (gameNames[i].contains("The Typing of The Dead: Overkill Collection")) {
						Action.setAppID("48535");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/48535/header_586x192.jpg?t=1447455696\">");
					}
					else if (gameNames[i].contains("DEFCON + Soundtrack DLC")) {
						Action.setAppID("14325");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/14325/header_586x192.jpg?t=1447448492\">");
					}
					else if (gameNames[i].contains("Introversion Classics Pack")) {
						Action.setAppID("14002");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/14002/header_586x192.jpg?t=1478805414\">");
					}
					else if (gameNames[i].contains("Sins of a Solar Empire: Rebellion Game and Soundtrack Bundle")) {
						Action.setAppID("18821");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/18821/header_586x192.jpg?t=1484671018\">");
					}
					else if (gameNames[i].contains("Galactic Civilizations I and II Pack")) {
						Action.setAppID("18252");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/18252/header_586x192.jpg?t=1452696084\">");
					}
					else if (gameNames[i].contains("Red Faction Complete Bundle")) {
						Action.setAppID("15630");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/15630/header_586x192.jpg?t=1488921934\">");
					}
					else if (gameNames[i].contains("X-SuperBox")) {
						Action.setAppID("6330");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/6330/header_586x192.jpg?t=1447446764\">");
					}
					else if (gameNames[i].contains("Carpe Fulgur Collection")) {
						Action.setAppID("15400");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/15400/header_586x192.jpg?t=1447448831\">");
					}
					else if (gameNames[i].contains("Patrician IV Gold")) {
						Action.setAppID("11830");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/11830/header_586x192.jpg?t=1447447830\">");
					}
					else if (gameNames[i].contains("Port Royale 3 Gold and Patrician IV Gold - Double Pack")) {
						Action.setAppID("47967");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/47967/header_586x192.jpg?t=1447455496\">");
					}
					else if (gameNames[i].contains("X3 Terran War Pack")) {
						Action.setAppID("12588");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/12588/header_586x192.jpg?t=1447448036\">");
					}
					else if (gameNames[i].contains("X3: GoldBox")) {
						Action.setAppID("12985");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/12985/header_586x192.jpg?t=1447448044\">");
					}
					else if (gameNames[i].contains("Paths to Glory Sports Simulation Bundle")) {
						Action.setAppID("95106");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/95106/header_586x192.jpg?t=1457737962\">");
					}
					else if (gameNames[i].contains("Binary Domain")) {
						Action.setAppID("27824");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/27824/header_586x192.jpg?t=1447450583\">");
					}
					else if (gameNames[i].contains("Grand Theft Auto Collection")) {
						Action.setAppID("28988");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/28988/header_586x192.jpg?t=1454693854\">");
					}
					else if (gameNames[i].contains("Grand Theft Auto IV + Grand Theft Auto: San Andreas")) {
						Action.setAppID("36044");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/36044/header_586x192.jpg?t=1454693864\">");
					}
					else if (gameNames[i].contains("Street Fighter X Tekken: Complete Pack")) {
						Action.setAppID("19277");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/19277/header_586x192.jpg?t=1483565130\">");
					}
					else if (gameNames[i].contains("Farming Simulator 15 Gold Edition")) {
						Action.setAppID("83678");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/83678/header_586x192.jpg?t=1447462375\">");
					}
					else if (gameNames[i].contains("Men of War: Collector Pack")) {
						Action.setAppID("14299");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/14299/header_586x192.jpg?t=1447448479\">");
					}
					else if (gameNames[i].contains("Sniper Ghost Warrior 3 Season Pass Edition")) {
						Action.setAppID("166591");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/166591/header_586x192.jpg?t=1504764681\">");
					}
					else if (gameNames[i].contains("Disney Other-Worldly Adventure Pack")) {
						Action.setAppID("52411");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/52411/header_586x192.jpg?t=1447456700\">");
					}
					else if (gameNames[i].contains("Disney Mega Pack")) {
						Action.setAppID("52412");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/52412/header_586x192.jpg?t=1451949501\">");
					}
					else if (gameNames[i].contains("Pinball Thrills Triple Pack")) {
						Action.setAppID("71910");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/71910/header_586x192.jpg?t=1447460762\">");
					}
					else if (gameNames[i].contains("Sniper Ghost Warrior 3 Season Pass Edition")) {
						Action.setAppID("166591");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/166591/header_586x192.jpg?t=1504764681\">");
					}
					else if (gameNames[i].contains("Sniper Ghost Warrior Gold Edition")) {
						Action.setAppID("11762");
						Action.setImage("<img src=\"http://cdn.edgecast.steamstatic.com/steam/subs/11762/header_586x192.jpg?t=1447447817\">");
					}
					
					
					else {
					Action.setAppID(AppID[i]);
					Action.setImage(images[i]);
					}
					Action.setGameName(gameNames[i]);
					
					

					// created if statement to find games with no description due to age
					// confirmations
					if (gameDesc[i].length() < 5) {
						Action.setDescription(gameDesc[i] + "This game is either a bundle or for mature audiences only. Please: "
								+ "<a href=\"" + descURL + "\" target=\"_blank\">click here</a>" + " for a detailed description.");
					} else {
						Action.setDescription(gameDesc[i].substring(gameDesc[i].indexOf("About This Game ")+16));
					}
					// Action.setDiscord(discordURLS[i]);
					// System.out.println(discordURLS[i]);
					Session session = factory.openSession();
					Transaction t = (Transaction) session.beginTransaction();

					session.persist(Action);
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