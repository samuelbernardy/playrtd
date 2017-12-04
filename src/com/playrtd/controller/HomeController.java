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

/*
 * author: samuelbernardy
 * author: yasrodriguez
 * author: josephdgarza
 */

@Controller
public class HomeController {

	private String steamAPIkey = "16F06098DE26F71F93EB595C82101042";

	@RequestMapping("/")
	public String main() {
		return "";
	}

	@RequestMapping("/return")
	public String loggedIn(Model model) throws IOException {

		// Strings from UserSummary
		long steam_ID;
		String personaName;
		String avatar;

		// Strings from OwnedGames
		int appID;

		// Strings from RecentGames
		int recentAppID;

		// request urls and readers to parse json on repsonse.
		URL playerSummaryURL = new URL("");
		URL playOwnedGamesURL = new URL("");
		URL playRecentGamesURL = new URL("");
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

//		for (int i = 0; i < playerJson.length(); i++) {
//			steam_ID = playerJson.getJSONObject(i).getString("center");
//			city = json.getJSONObject(i).getString("city");
//			contact = json.getJSONObject(i).getString("contact");
//			forPrint += ("<h2>" + center + " " + city + " " + contact + "</h2>");
//		}
//		for (int i = 0; i < json.length(); i++) {
//			center = json.getJSONObject(i).getString("center");
//			city = json.getJSONObject(i).getString("city");
//			contact = json.getJSONObject(i).getString("contact");
//			forPrint += ("<h2>" + center + " " + city + " " + contact + "</h2>");
//		}
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