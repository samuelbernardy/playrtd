
package com.playrtd.controller;

import com.gc.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.playrtd.resource.SteamOpenID;
import com.playrtd.util.Credentials;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SessionAttributes({ "selectTags", "choicemsg" })
@Controller
public class welcomeController {

	private SteamOpenID steamOpenID = new SteamOpenID();
	ArrayList<String> selectTags = new ArrayList<String>();

	// @RequestMapping("/")
	// public String index(Model model) {
	//
	// Configuration cfg = new Configuration();
	// cfg.configure("hibernate.cfg.xml");
	// SessionFactory sf = cfg.buildSessionFactory();
	// Session s = sf.openSession();
	// Transaction tx = s.beginTransaction();
	//
	// Object[] obj = new Object[2];
	//
	// String persona = "";
	// String appID = "";
	// String recentLikeIMG = "";
	//
	// Query q2 = s.createQuery("select recentLikeIMG,g.persona,g.appID, from
	// RecentLikesDto g ORDER BY g.ID DESC");
	//
	// q2.setFirstResult(0);
	// q2.setMaxResults(5);
	// List results = q2.list();
	// Iterator i = results.iterator();
	// List<RecentLikesDto> list = new ArrayList<RecentLikesDto>();
	// while (i.hasNext()) {
	//
	// obj = (Object[]) i.next();
	// recentLikeIMG = (String) obj[0];
	// persona = (String) obj[1];
	// appID = (String) obj[2];
	//
	// list.add(new RecentLikesDto(recentLikeIMG, persona, appID));
	// }
	//
	// s.flush();
	// s.close();
	// model.addAttribute("list", list);
	//
	// return "index";
	// }

	@RequestMapping(value = "/login_page", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request) {

		// log.debug("Trying to call Steam OpenID...");

		String openIdRedirectUrl = steamOpenID.login("http://localhost:8080/PlayRTD/return");

		// log.debug("Redirect URL : " + openIdRedirectUrl);

		return new ModelAndView("redirect:" + openIdRedirectUrl);
	}

	// This mapping handles Steam Authentication and returns use data including
	// STEAMID, RECENTGAMES, and OWNEDGAMES
	@RequestMapping(value = "/return", method = RequestMethod.GET)
	public RedirectView postLoginPage(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Random rand = new Random();
		ArrayList<String> recentTagsArray = new ArrayList<String>();
		String playerAvatar = "";
		String playerPersona = "";

		Long steam_ID = 0l;
		String userId = this.steamOpenID.verify(request.getRequestURL().toString(), request.getParameterMap());
		System.out.println(userId);
		steam_ID = Long.parseLong(userId);
		UserInfoDto userProfile = new UserInfoDto();

		// establish session to keep user logged in for subsequent actions.

		// request urls and readers to parse json on repsonse.
		URL playerSummaryURL = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="
				+ Credentials.STEAM_API_KEY + "&steamids=" + steam_ID + "&format=json");
		URL playOwnedGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");
		URL playRecentGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");
		System.out.println(playRecentGamesURL);

		// parse lines from returned jSon
		BufferedReader playerReader = new BufferedReader(new InputStreamReader(playerSummaryURL.openStream()));
		BufferedReader ownedReader = new BufferedReader(new InputStreamReader(playOwnedGamesURL.openStream()));
		BufferedReader recentReader = new BufferedReader(new InputStreamReader(playRecentGamesURL.openStream()));
		String playerJsonStr = "";
		String ownedJsonStr = "";
		String recentJsonStr = "";
		String playerLine = playerReader.readLine();
		String ownedLine = ownedReader.readLine();
		String recentLine = recentReader.readLine();
		while (playerLine != null) {
			playerJsonStr += playerLine;
			playerLine = playerReader.readLine();
		}
		while (ownedLine != null) {
			ownedJsonStr += ownedLine;
			ownedLine = ownedReader.readLine();
		}
		while (recentLine != null) {
			recentJsonStr += recentLine;
			recentLine = recentReader.readLine();
		}

		// create jSon objects
		JSONObject playerJson = new JSONObject(playerJsonStr);
		JSONObject ownedJson = new JSONObject(ownedJsonStr);
		JSONObject recentJson = new JSONObject(recentJsonStr);

		// TODO check if private account

		if (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
				.getInt("communityvisibilitystate") == 1) {
			return new RedirectView("privateprofileerror");
		}

		// check owned and recent game count. Offer random apptags if empty.
		int ownedCountCheck = ownedJson.getJSONObject("response").getInt("game_count");
		int recentCountCheck = recentJson.getJSONObject("response").getInt("total_count");
		if (ownedCountCheck >= 1 && recentCountCheck >= 1) {
			JSONArray ownedJSONArray = ownedJson.getJSONObject("response").getJSONArray("games");
			JSONArray recentJSONArray = recentJson.getJSONObject("response").getJSONArray("games");

			// find user data and populate owned and recent games array
			playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("avatarfull"));
			playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("personaname"));
			ArrayList<Integer> ownedGamesArray = new ArrayList<Integer>();
			ArrayList<Integer> recentGamesArray = new ArrayList<Integer>();
			for (int i = 0; i < ownedJSONArray.length(); i++) {
				Integer ownedInt = ownedJSONArray.getJSONObject(i).getInt("appid");
				ownedGamesArray.add(ownedInt);
			}
			for (int i = 0; i < recentJSONArray.length(); i++) {
				System.out.println(recentJSONArray);
				Integer recentInt = recentJSONArray.getJSONObject(i).getInt("appid");
				recentGamesArray.add(recentInt);
			}

			// populate user Object
			userProfile.setSteam_ID(steam_ID);
			userProfile.setPersonaName(playerPersona);
			userProfile.setAvatar(playerAvatar);

			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			SessionFactory factory = cfg.buildSessionFactory();
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			session.save(userProfile);
			t.commit();
			session.close();
			// System.out.println(recentGamesArray.size());

			selectTags = new ArrayList<String>();
			try {

				// ma game with tags 578080 238960
				// ma game without tags 359550
				// reg game 268910
				// Check if the game has age check and grab app tags based on page layout
				for (int i = 0; i < recentGamesArray.size(); i++) {
					Document doc = Jsoup
							.connect("http://store.steampowered.com/app/" + recentGamesArray.get(i) + "/agecheck")
							.get();
					Elements tempTag;
					String holder = "";
					String[] gameTags = new String[3];

					// Non MA Game
					if (doc.toString().contains("glance_tags popular_tags")) {
						tempTag = doc.select("div.glance_tags.popular_tags");
						for (Element tagHolder : tempTag) {
							holder = tagHolder.toString();
						}
						gameTags = holder.split("a>");

					}
					// MA game that shows tags
					else if (doc.toString().contains("glance_tags_ctn popular_tags_ctn")) {
						tempTag = doc.select("div.glance_tags_ctn.popular_tags_ctn");
						for (Element tagHolder : tempTag) {
							holder = tagHolder.toString();
						}
						gameTags = holder.split("a>");

					}

					// MA Game that doesnt display tags
					else {

					}

					// This code first splits the text to the number of values we need in the array.
					// Then further splices it with substrings to grab the APPID
					for (int j = 0; j < 3; j++) {

						// Non MA Game
						if (doc.toString().contains("glance_tags popular_tags")) {

							recentTagsArray.add(gameTags[j].substring(gameTags[j].indexOf("none;\">") + 8,
									gameTags[j].indexOf(" </")));
						}

						// MA game that shows tags
						else if (doc.toString().contains("glance_tags_ctn popular_tags_ctn")) {

							recentTagsArray.add(gameTags[j].substring(gameTags[j].indexOf("\"app_tag\">") + 11,
									gameTags[j].indexOf(" </")));
						}

						// MA Game that doesnt display tags
						else {

						}
					}
					// recentTagsArray
					// .add(games[j].substring(games[j].indexOf("none;\">") + 8, games[j].indexOf("
					// </")));
					// }
				}

				// create user's random options

				String tempTag = "";
				while (selectTags.size() < 3) {
					tempTag = recentTagsArray.get(rand.nextInt(recentTagsArray.size())).toString();
					while (!(selectTags.contains(tempTag))) {
						selectTags.add(tempTag);
					}
				}
				System.out.println(selectTags.get(2).toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.addCookie(new Cookie("steamID", steam_ID.toString()));
			response.addCookie(new Cookie("avatar", playerAvatar));
			response.addCookie(new Cookie("persona", playerPersona));
			model.addAttribute("choicemsg", "It seems that you like " + selectTags.get(0).toString() + ", "
					+ selectTags.get(1).toString() + ", " + selectTags.get(2).toString() + " games.");
			model.addAttribute("tag1", selectTags.get(0).toString());
			model.addAttribute("tag2", selectTags.get(1).toString());
			model.addAttribute("tag3", selectTags.get(2).toString());
			model.addAttribute("dTag1", selectTags.get(0).toString());
			model.addAttribute("dTag2", selectTags.get(1).toString());
			model.addAttribute("dTag3", selectTags.get(2).toString());

			return new RedirectView("choices");
		} else {

			playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("avatarfull"));
			playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("personaname"));
			userProfile.setSteam_ID(steam_ID);
			userProfile.setPersonaName(playerPersona);
			userProfile.setAvatar(playerAvatar);

			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			SessionFactory factory = cfg.buildSessionFactory();
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			session.save(userProfile);
			t.commit();
			session.close();

			recentTagsArray.add("Action");
			recentTagsArray.add("Adventure");
			recentTagsArray.add("Casual");
			recentTagsArray.add("Indie");
			recentTagsArray.add("Massively Multiplayer");
			recentTagsArray.add("Racing");
			recentTagsArray.add("RPG");
			recentTagsArray.add("Simulation");
			recentTagsArray.add("Sports");
			recentTagsArray.add("Strategy");
			String tempTag = "";
			while (selectTags.size() < 3) {
				tempTag = recentTagsArray.get(rand.nextInt(recentTagsArray.size())).toString();
				while (!(selectTags.contains(tempTag))) {
					selectTags.add(tempTag);
				}
			}
			System.out.println(selectTags.get(2).toString());

			String nogamesnotif = "It looks like you haven't played for a little bit, let us recommend a couple categories!";
			model.addAttribute("choicemsg", nogamesnotif);

			response.addCookie(new Cookie("steamID", steam_ID.toString()));
			response.addCookie(new Cookie("avatar", playerAvatar));
			response.addCookie(new Cookie("persona", playerPersona));
			model.addAttribute("tag1", selectTags.get(0).toString());
			model.addAttribute("tag2", selectTags.get(1).toString());
			model.addAttribute("tag3", selectTags.get(2).toString());
			model.addAttribute("dTag1", selectTags.get(0).toString());
			model.addAttribute("dTag2", selectTags.get(1).toString());
			model.addAttribute("dTag3", selectTags.get(2).toString());

			return new RedirectView("choices");
		}
		

	}
	@RequestMapping("/privateprofileerror")
	public String privateProfile() {
		return "private"	;
	}
	

}
