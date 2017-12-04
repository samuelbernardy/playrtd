package com.playrtd.controller;

import java.io.IOException;
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
import com.playrtd.util.Credentials;

@Controller
public class TwitchController {

	@RequestMapping("/twitch")
	public String getTwitchStreamByGameID(Model model) {

		// This should be the Twitch game ID for the game recommended
		String gameId = "32399"; // Counter-Strike
		String channelName = "";

		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/streams?game_id=" + gameId);
			getPage.setHeader("Client-ID", Credentials.CLIENT_ID);
			HttpResponse resp = http.execute(host, getPage);
			String jsonString = EntityUtils.toString(resp.getEntity());

			JSONObject json = new JSONObject(jsonString);
			JSONArray allStreams = json.getJSONArray("data");
			JSONObject firstStream = allStreams.getJSONObject(0);
			
			String thumbnailUrl = firstStream.getString("thumbnail_url");
			System.out.println("Thumbnail: " + thumbnailUrl);
			//TODO Parse channel name from thumbnail URL
			String pattern = "_user_channelNameHere-";

		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		// This variable should have the name of the Twitch channel that has the highest
		// views for the video game recommended
		channelName = "monstercat";
		model.addAttribute("channel", channelName);
		return "twitch";
	}

}
