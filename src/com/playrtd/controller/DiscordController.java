package com.playrtd.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gc.dto.DiscordDto;
import com.playrtd.util.Credentials;

@Controller
public class DiscordController {

	@RequestMapping("/discord")
	public String getDiscordAdminPage() {
		return "admindiscord";
	}

	@RequestMapping("/discordsearch")
	public String getDiscordSearchResults(Model model, @RequestParam("gamename") String gameName) {

		// Return an error message if an empty or null twitch game ID is provided
		if (gameName == null || gameName.isEmpty()) {
			String errorMessage = "Sorry, game name can't be blank or null.";
			model.addAttribute("errormessage", errorMessage);
			return "admindiscord";
		}

		// Call the Custom Search Engine API
		String jsonString = callCustomSearchEngineApi(gameName);
		System.out.println("JSON response: " + jsonString);

		// Call method to parse JSON
		ArrayList<DiscordDto> discords = parseDiscords(jsonString);
		System.out.println("ArrayList: " + discords);
		if (discords == null) {
			String errorMessage = "No results found.";
			model.addAttribute("errormessage", errorMessage);
			return "admindiscord";
		}

		model.addAttribute("topdiscord", discords.get(0));
		model.addAttribute("discords", discords);

		return "admindiscord";
	}

	private ArrayList<DiscordDto> parseDiscords(String jsonString) {
		JSONObject json = new JSONObject(jsonString);
		int totalResults = json.getJSONObject("searchInformation").getInt("totalResults");

		// Return null if there are no search results
		if (totalResults == 0) {
			return null;
		}

		// Search results from first page (up to 10 results)
		JSONArray searchResults = json.getJSONArray("items");
		ArrayList<DiscordDto> discordsList = new ArrayList<>();

		// Grabs the title, link, and description for each result, creates a discord
		// object, and stores it in an array
		for (int i = 0; i < searchResults.length(); i++) {
			JSONObject result = searchResults.getJSONObject(i);

			String title = result.getString("title");
			String link = result.getString("link");
			String description = result.getString("snippet");

			DiscordDto discord = new DiscordDto(title, link, description);

			discordsList.add(discord);
		}

		return discordsList;
	}

	private String callCustomSearchEngineApi(String gameName) {
		String response = "";

		HttpClient http = HttpClientBuilder.create().build();
		HttpHost host = new HttpHost("www.googleapis.com", 443, "https");

		try {
			HttpGet getPage = new HttpGet("/customsearch/v1?q=" + URLEncoder.encode(gameName, "UTF-8") + "&cx="
					+ Credentials.GOOGLE_CUSTOM_SEARCH_ENGINE_ID + "&key=" + Credentials.GOOGLE_SEARCH_API_KEY);
			HttpResponse resp = http.execute(host, getPage);
			response = EntityUtils.toString(resp.getEntity());
		} catch (IOException e) {
			e.printStackTrace(); // remove
		}
		return response;
	}

}
