
package com.playrtd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dto.*;
import com.playrtd.resource.SteamOpenID;
import com.playrtd.util.Credentials;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Controller
public class SteamAuthController {

	private SteamOpenID steamOpenID = new SteamOpenID();

	@RequestMapping(value = "/return", method = { RequestMethod.GET })
	public String postLoginPage(HttpSession jSession, Model model, HttpServletRequest request) throws IOException {

		String userId = this.steamOpenID.verify(request.getRequestURL().toString(), request.getParameterMap());
		ModelAndView mav = new ModelAndView("post_login");
		mav.addObject("steamId", userId);
		System.out.println(userId);
		long steam_ID = Long.parseLong(userId);
		UserInfoDto userProfile = new UserInfoDto();

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

		int ownedCountCheck = ownedJson.getJSONObject("response").getInt("game_count");
		int recentCountCheck = recentJson.getJSONObject("response").getInt("total_count");

		if (ownedCountCheck >= 1 && recentCountCheck >= 1) {

			JSONArray ownedJSONArray = ownedJson.getJSONObject("response").getJSONArray("games");
			JSONArray recentJSONArray = recentJson.getJSONObject("response").getJSONArray("games");

			// find user data
			String playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("avatarfull"));
			String playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("personaname"));
			System.out.println(ownedJSONArray + "test1");
			ArrayList<Integer> ownedGamesArray = new ArrayList<Integer>();
			ArrayList<Integer> recentGamesArray = new ArrayList<Integer>();
			for (int i = 0; i < ownedJSONArray.length(); i++) {
				System.out.println(ownedJSONArray);
				// for (int i = 0; i <
				// ownedJson.getJSONObject("response").getJSONArray("games").length(); i++) {
				Integer ownedInt = ownedJSONArray.getJSONObject(i).getInt("appid");
				ownedGamesArray.add(ownedInt);
			}
			for (int i = 0; i < recentJSONArray.length(); i++) {
				System.out.println(recentJSONArray);
				Integer recentInt = recentJSONArray.getJSONObject(i).getInt("appid");
				recentGamesArray.add(recentInt);
			}

			userProfile.setSteam_ID(steam_ID);
			userProfile.setPersonaName(playerPersona);
			// System.out.println(userProfile.getPersonaName());
			userProfile.setAvatar(playerAvatar);
			System.out.println("Antonella1");
			// userProfile.setRecentGames(recentGamesArray);
			// userProfile.setOwnedGames(ownedGamesArray);

			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			System.out.println("Antonella 2");
			SessionFactory factory = cfg.buildSessionFactory();
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
			System.out.println("Antonella 3");
			session.save(userProfile);
			t.commit();
			session.close();
			System.out.println(recentGamesArray.size());
			
			ArrayList<String> selectTags = new ArrayList<String>();
			try {
				ArrayList<String> recentTagsArray = new ArrayList<String>();
				for (int i = 0; i < recentGamesArray.size(); i++) {
					Document doc = Jsoup.connect("http://store.steampowered.com/app/" + recentGamesArray.get(i)).get();

					Elements tempTag = doc.select("div.glance_tags.popular_tags");
					// System.out.println(temp.toString());
					String holder = "";
					String[] games;
					for (Element tagHolder : tempTag) {
						// System.out.println(i + " " + tagHolder.toString());
						holder = tagHolder.toString();
					}
					games = holder.split("a>");

					// This code first splits the text to the number of values we need in the array.
					// Then further splices it with substrings to grab the APPID
					for (int j = 0; j < 3; j++) {
						recentTagsArray
								.add(games[j].substring(games[j].indexOf("none;\">") + 8, games[j].indexOf(" </")));
					}
				}

				// create user's random options
				
				Random rand = new Random();
				String tempTag = "";
				while (selectTags.size() < 3) {
					tempTag = recentTagsArray.get(rand.nextInt(recentTagsArray.size())).toString();
					while (!(selectTags.contains(tempTag))) {
						selectTags.add(tempTag);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			model.addAttribute("avatar", playerAvatar);
			model.addAttribute("persona", playerPersona);
			model.addAttribute("opt1", selectTags.get(0).toString());
			model.addAttribute("opt2", selectTags.get(1).toString());
			model.addAttribute("opt3", selectTags.get(2).toString());

			return "return";
		} else

		{

			String nogamesnotif = "fgfdhgnfh";
			model.addAttribute("nogames", nogamesnotif);

			return "return";
		}

	}

	@RequestMapping(value = "/login_page", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request) {

		// log.debug("Trying to call Steam OpenID...");

		String openIdRedirectUrl = steamOpenID.login("http://localhost:8080/PlayRTD/return");

		// log.debug("Redirect URL : " + openIdRedirectUrl);

		return new ModelAndView("redirect:" + openIdRedirectUrl);
	}

	@RequestMapping(value = "/gameon", method = RequestMethod.GET)
	public String getgame() {

		return "gameon";
	}
}