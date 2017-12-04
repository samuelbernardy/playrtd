package com.playrtd.controller;

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


/*
 * author: samuelbernardy
 * author: yasrodriguez
 * author: josephdgarza
 */

@Controller
public class HomeController {

	private String steamAPIkey = "16F06098DE26F71F93EB595C82101042";

	@RequestMapping("/")
	public String index() {
		
		return "";
	}

	@RequestMapping("/return")
	public String loggedIn(Model model) throws IOException {

		// Strings from UserSummary
		long steam_ID = 0;
		String personaName = "";
		String avatar = "";

		// Strings from OwnedGames
		int ownedAppID = 0;

		// Strings from RecentGames
		int recentAppID = 0;

		// request urls and readers to parse json on repsonse.
		URL playerSummaryURL = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=065A9EB0620845303DD97F5D5F404659&steamids=76561198094590002&format=json");
		URL playOwnedGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=065A9EB0620845303DD97F5D5F404659&steamid=76561198094590002&format=json");
		URL playRecentGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=065A9EB0620845303DD97F5D5F404659&steamid=76561198094590002&format=json");
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

		JSONArray playerJson = new JSONArray(playerJsonStr);
		JSONArray ownedJson = new JSONArray(ownedJsonStr);
		JSONArray recentJson = new JSONArray(recentJsonStr);

		for (int i = 0; i < playerJson.length(); i++) {
			steam_ID = playerJson.getJSONObject(i).getLong("steamid");
			personaName = playerJson.getJSONObject(i).getString("personaName");
			avatar = playerJson.getJSONObject(i).getString("avatarfull");
		}
		System.out.println(steam_ID + " " + personaName + " " + " " + avatar);
		
		for (int i = 0; i < ownedJson.length(); i++) {
			ownedAppID = ownedJson.getJSONObject(i).getInt("appid");
		}
		
		System.out.println(ownedAppID);
		
		for (int i = 0; i < recentJson.length(); i++) {
			recentAppID = recentJson.getJSONObject(i).getInt("appid");
		}
//		
//		model.addAttribute("nasaData", forPrint);

		return "return";
	}

	@RequestMapping("/seerecent")
	public String seeRecent() {
		return "seerecent";
	}

	@RequestMapping("/rolled")
	public String rolled() {
		return "gameon";
	}
}