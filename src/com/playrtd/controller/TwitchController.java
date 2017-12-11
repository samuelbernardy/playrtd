package com.playrtd.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.gc.dao.ProductDao;
import com.gc.dao.ProductDaoHibernate;
import com.gc.dto.ProductDto;
import com.playrtd.util.Credentials;

@Controller
public class TwitchController {

	@RequestMapping("/twitch")
	public String getTwitchPage() {
		return "admintwitch";
	}

	@RequestMapping("/getstream")
	public String getTwitchStream(Model model, @RequestParam("twitchgameid") String gameId) {
		String errorMessage = "";
		String channelName = "";
		
		//Return an error message if an empty or null twitch game ID is provided
		if (gameId == null || gameId.isEmpty()) {
			errorMessage = "Sorry, twitch game ID can't be blank or null.";
			model.addAttribute("errormessage", errorMessage);
			return "admintwitch";
		}
		
		// Gets all live streams for the game ID, then selects the first stream in the
		// array (most views)
		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/streams?game_id=" + gameId);
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			
			String jsonString = EntityUtils.toString(resp.getEntity());
			JSONObject json = new JSONObject(jsonString);
			JSONArray allStreams = json.getJSONArray("data");
			
			//Return an error message if the response comes back blank: twitch game id provided
			// is not valid or the game has no live streams at the moment
			if (allStreams.length() == 0) {
				errorMessage = "Sorry, this game does not have a live stream or the twitch game id is incorrect.";
				model.addAttribute("errormessage", errorMessage);
				return "admintwitch";
			}
			
			// Gets the first stream in the array (most views)
			JSONObject firstStream = allStreams.getJSONObject(0);

			// Parse channel name from thumbnail URL
			String thumbnailUrl = firstStream.getString("thumbnail_url");
			Pattern channelPattern = Pattern.compile("(?<=.+_user_).+(?=-.+)");
			Matcher m = channelPattern.matcher(thumbnailUrl.trim());
			m.find();
			channelName = m.group(0);

			model.addAttribute("channel", channelName);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "admintwitch";
	}

	@RequestMapping("/getgamename")
	public String getTwitchGameName(Model model, @RequestParam("twitchgameid") String gameId) {
		String gameName = "";
		String errorMessage = "";
		
		//Return an error message if an empty or null twitch game ID is provided
		if (gameId == null || gameId.isEmpty()) {
			errorMessage = "Sorry, twitch game ID can't be blank or null.";
			model.addAttribute("errormessage", errorMessage);
			return "twitch";
		}

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/games?id=" + gameId);
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray dataArr = json.getJSONArray("data");
			
			//Return an error message if the response comes back blank: twitch game id provided
			// is not valid 
			if (dataArr.length() == 0) {
				errorMessage = "Sorry, this twitch game id is incorrect.";
				model.addAttribute("errormessage", errorMessage);
				return "admintwitch";
			}

			JSONObject game = dataArr.getJSONObject(0);
			gameName = game.getString("name");

			model.addAttribute("gamename", gameName);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "admintwitch";
	}

	@RequestMapping("/getgameid")
	public String getTwitchGameId(Model model, @RequestParam("twitchgamename") String gameName) {
		String gameId = "";
		String errorMessage = "";
		
		//Return an error message if an empty or null twitch game name is provided
				if (gameName == null || gameName.isEmpty()) {
					errorMessage = "Sorry, game name can't be blank or null.";
					model.addAttribute("errormessage", errorMessage);
					return "admintwitch";
				}

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");

			HttpGet getPage = new HttpGet("/helix/games?name=" + URLEncoder.encode(gameName, "UTF-8"));
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray dataArr = json.getJSONArray("data");
			
			//Return an error message if the response comes back blank: twitch game name provided
			// is not valid
			if (dataArr.length() == 0) {
				errorMessage = "Sorry, this game name does not have a Twitch Game ID.";
				model.addAttribute("errormessage", errorMessage);
				return "admintwitch";
			}
			
			JSONObject game = dataArr.getJSONObject(0);
			gameId = game.getString("id");

			model.addAttribute("gameid", gameId);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "admintwitch";
	}

	@RequestMapping("/uploadtwitchgameids")
	public String uploadTwitchGameIDs(ArrayList<ProductDto> steamGames) {
		StringBuilder sb = new StringBuilder();
		ProductDao dao = new ProductDaoHibernate();
		final int MAX_GAMES_PER_REQUEST = 100;
		int recordCount = 0;
		ArrayList<ProductDto> games = (ArrayList<ProductDto>) dao.list(recordCount + 1, MAX_GAMES_PER_REQUEST);

		try {
			while (games.size() != 0) {
				// clears StringBuilder
				sb = new StringBuilder();

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

				getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);
				HttpResponse resp = http.execute(host, getPage);

				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONObject json = new JSONObject(jsonString);
				
				JSONArray dataArr = json.getJSONArray("data");

				// parse each game in the json response and store the game id on the database
				// games are not returned in the same order requested, so we need to parse both
				// name and id

				for (int i = 0; i < dataArr.length(); i++) {
					JSONObject game = dataArr.getJSONObject(i);
					String gameName = game.getString("name");
					String gameId = game.getString("id");

					// Get object from database and set twitch ID
					ArrayList<ProductDto> dtoArr = dao.searchByName(gameName);

					for (ProductDto dto : dtoArr) {
						dto.setTwitchGameID(gameId);

						// Update object on database
						dao.update(dto);
					}
				}
				// Update count to grab next 100 records
				recordCount += MAX_GAMES_PER_REQUEST;
				games = (ArrayList<ProductDto>) dao.list(recordCount + 1, MAX_GAMES_PER_REQUEST);

				// API has a limit of 2 requests per second; setting a timer for 3 seconds to
				// avoid errors
				Thread.sleep(3000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "admintwitch";
	}

}
