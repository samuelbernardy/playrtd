package com.playrtd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dto.*;

import com.playrtd.resource.SteamOpenID;
import com.playrtd.util.Credentials;

@Controller
@SessionAttributes("sessionId")
public class SteamAuthController {

	private SteamOpenID steamOpenID = new SteamOpenID();
	

	@RequestMapping(value = "/return", method = { RequestMethod.GET })
	public String postLoginPage(HttpServletResponse response, Model model, HttpServletRequest request) throws IOException {

		String userId = this.steamOpenID.verify(request.getRequestURL().toString(), request.getParameterMap());
		long steam_ID = Long.parseLong(userId);
		String personaName = "";
		String avatar = "";
		UserInfoDto userProfile = new UserInfoDto();

		// Strings from OwnedGames
		int ownedAppID = 0;

		// Strings from RecentGames
		int recentAppID = 0;

		// request urls and readers to parse json on repsonse.
		URL playerSummaryURL = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="
				+ Credentials.STEAM_API_KEY + "&steamids=" + steam_ID + "&format=json");
		URL playOwnedGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");
		URL playRecentGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");

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

		// get user data
		String playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
				.getString("avatarfull"));
		String playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
				.getString("personaname"));

		ArrayList<Integer> ownedGamesArray = new ArrayList<Integer>();
		ArrayList<Integer> recentGamesArray = new ArrayList<Integer>();
		for (int i = 0; i < ownedJson.getJSONObject("response").getJSONArray("games").length(); i++) {
			Integer ownedString = ownedJson.getJSONObject("response").getJSONArray("games").getJSONObject(i)
					.getInt("appid");
			ownedGamesArray.add(ownedString);
		}
		for (int i = 0; i < recentJson.getJSONObject("response").getJSONArray("games").length(); i++) {
			Integer recentString = recentJson.getJSONObject("response").getJSONArray("games").getJSONObject(i)
					.getInt("appid");
			recentGamesArray.add(recentString);
		}

		// set UserInfoDto object.
		userProfile.setSteam_ID(steam_ID);
		userProfile.setPersonaName(personaName);
		userProfile.setAvatar(playerAvatar);
		userProfile.setRecentGames(recentGamesArray);
		userProfile.setOwnedGames(ownedGamesArray);

		// open hibernation session and insert user info into userinfo table
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		Session hSession = factory.openSession();
		Transaction t = (Transaction) hSession.beginTransaction();
		hSession.persist(userProfile);
		t.commit();
		hSession.close();

		model.addAttribute("avatar", playerAvatar);
		model.addAttribute("persona", playerPersona.toString());

		return "return";
	}

	@RequestMapping(value = "/login_page", method = { RequestMethod.GET })
	public ModelAndView loginPage(HttpServletRequest request) {
		String openIdRedirectUrl = steamOpenID.login("http://localhost:8080/PlayRTD/return");
		return new ModelAndView("redirect:" + openIdRedirectUrl);
	}
}