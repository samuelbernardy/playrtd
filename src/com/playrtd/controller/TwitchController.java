package com.playrtd.controller;

import java.io.IOException;
import java.net.URLEncoder;

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
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray allStreams = json.getJSONArray("data");
			JSONObject firstStream = allStreams.getJSONObject(0);

			String thumbnailUrl = firstStream.getString("thumbnail_url");
			System.out.println(thumbnailUrl);
			
			// TODO Parse channel name from thumbnail URL
			String pattern = "_user_channelNameHere-";

			// TODO Twitch channel to play
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
			// System.out.println(dataArr);
			JSONObject game = dataArr.getJSONObject(0);
			gameId = game.getString("id");
			// System.out.println("Game " + gameName);

			model.addAttribute("gameid", gameId);

		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return "twitch";
	}
}
