package com.playrtd.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import org.springframework.web.servlet.ModelAndView;

import com.gc.dao.GameIdMappingDao;
import com.gc.dao.GameIdMappingDaoTwitch;
import com.gc.dto.GameIdMappingDto;
import com.playrtd.util.Credentials;

@Controller
public class TwitchController {

	@RequestMapping("/twitch")
	public String getTwitchPage() {
		return "twitch";
	}

	@RequestMapping("/getstream")
	public String getTwitchStream(Model model) {

		// Twitch game ID for the game recommended
		String gameId = "32399"; // Counter-Strike
		String channelName = "";

		// Gets all live streams for the game ID, then selects the first stream in the
		// array (most views)
		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/streams?game_id=" + gameId);
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			
			// TODO Handle response exceptions
			HttpResponse resp = http.execute(host, getPage); 
			
			String jsonString = EntityUtils.toString(resp.getEntity());
			JSONObject json = new JSONObject(jsonString);
			JSONArray allStreams = json.getJSONArray("data");
			
			//Gets the first stream in the array (most views)
			JSONObject firstStream = allStreams.getJSONObject(0);
			
			String thumbnailUrl = firstStream.getString("thumbnail_url");
			System.out.println(thumbnailUrl); // Remove

			// TODO Parse channel name from thumbnail URL
			String pattern = "_user_channelNameHere-";
			channelName = "monstercat";
			model.addAttribute("channel", channelName);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "twitch";
	}

	@RequestMapping("/getgamename")
	public String getTwitchGameName(Model model, @RequestParam("twitchgameid") String gameId) {
		String gameName = "";

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/games?id=" + gameId);
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray dataArr = json.getJSONArray("data");
			
			//
			JSONObject game = dataArr.getJSONObject(0);
			gameName = game.getString("name");

			model.addAttribute("gamename", gameName);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "twitch";
	}

	@RequestMapping("/getgameid")
	public String getTwitchGameId(Model model, @RequestParam("twitchgamename") String gameName) {
		String gameId = "";

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");

			HttpGet getPage = new HttpGet("/helix/games?name=" + URLEncoder.encode(gameName, "UTF-8"));
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray dataArr = json.getJSONArray("data");
			System.out.println(dataArr);
			JSONObject game = dataArr.getJSONObject(0);
			gameId = game.getString("id");

			model.addAttribute("gameid", gameId);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "twitch";
	}

	@RequestMapping("/getmultiplegameids")
	public String getMultipleTwitchGameIds(Model model, ArrayList<GameIdMappingDto> steamGames) {
		StringBuilder sb = new StringBuilder();
		GameIdMappingDao dao = new GameIdMappingDaoTwitch();
		final int MAX_GAMES_PER_REQUEST = 100;
		int recordCount = 0;
		ArrayList<GameIdMappingDto> games = (ArrayList<GameIdMappingDto>) dao.list(recordCount + 1,
				MAX_GAMES_PER_REQUEST);
		System.out.println("Array has " + games.size()); //remove

		try {
			while (games.size() != 0) {
				// clears StringBuilder
				sb = new StringBuilder();

				System.out.println(games); // remove
				HttpClient http = HttpClientBuilder.create().build();
				HttpHost host = new HttpHost("api.twitch.tv", 443, "https");

				// Builds url with all the games on the ArrayList
				for (int i = 0; i < games.size(); i++) {
					sb.append("name=" + URLEncoder.encode(games.get(i).getGameName(), "UTF-8"));
					if (i != games.size() - 1) {
						sb.append("&");
					}
				}

				HttpGet getPage = new HttpGet("/helix/games?" + sb.toString());
				System.out.println(getPage); // remove

				getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
				HttpResponse resp = http.execute(host, getPage);
				
				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json); //
				JSONArray dataArr = json.getJSONArray("data");
				System.out.println(dataArr); //

				// parse each game in the json response and store the game id on the database
				// games are not returned in the same order requested, so we need to parse both
				// name and id

				for (int i = 0; i < dataArr.length(); i++) {
					JSONObject game = dataArr.getJSONObject(i);
					String gameName = game.getString("name");
					String gameId = game.getString("id");

					// Get object from database and set twitch ID
					ArrayList<GameIdMappingDto> dtoArr = dao.searchByName(gameName);

					for (GameIdMappingDto dto : dtoArr) {
						dto.setTwitchGameId(gameId);

						// Update object on database
						dao.update(dto);
					}
				}
				// Update count to grab next 100 records
				recordCount += MAX_GAMES_PER_REQUEST;
				games = (ArrayList<GameIdMappingDto>) dao.list(recordCount + 1, MAX_GAMES_PER_REQUEST);
				System.out.println("Array has " + games.size()); // remove
				
				//API has a limit of 2 requests per second; setting a timer for 3 seconds to avoid errors
				Thread.sleep(3000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "twitch";
	}

	@RequestMapping("/getsteamgames")
	public ModelAndView parseSteamGames(Model model) {

		// Parse all steam games
		//TODO Fix errors for names with emoticons
		int appId = 0;
		String appName = "";
		ArrayList<GameIdMappingDto> games = new ArrayList<>();

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.steampowered.com", 80, "http");
			HttpGet getPage = new HttpGet("/ISteamApps/GetAppList/v2");
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString).getJSONObject("applist");
			JSONArray apps = json.getJSONArray("apps");

			GameIdMappingDao dao = new GameIdMappingDaoTwitch();
			GameIdMappingDto dto;
			for (int i = 0; i < apps.length(); i++) {
				JSONObject app = apps.getJSONObject(i);
				appId = app.getInt("appid");
				appName = app.getString("name");
				games.add(new GameIdMappingDto(appId, appName, null));
			}

			// Store appid and name on a table
			dao.insert(games);

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
		return new ModelAndView("twitch", "steamgames", "");
	}
}
