package com.playrtd.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gc.dto.DiscordDto;
import com.gc.dto.ProductDto;
import com.playrtd.util.Credentials;

@Controller
public class OtherLikes {
	
	@RequestMapping("/likedgame")
	public String getgame(Model model, @RequestParam(value = "gameName") String gameName) {
		
		
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sf = cfg.buildSessionFactory();
		Session s = sf.openSession();
		Transaction tx = s.beginTransaction();

		// System.out.println(tag);
		String id = "";
		String name = "";

		String img = "";
		String desc = "";
		String dscURL = "";
		String twitchID = "";
		String storeURL = "";
		Object[] obj = new Object[8];

		// String quer = "select g.appID,g.gameName,g.tag,g.image,g.description from
		// ProductDto g WHERE g.tag = "+tag+" ORDER BY RAND()";
	
		String quer = Q1(gameName);

		System.out.println(quer);
		Query q2 = s.createQuery(quer);

		q2.setFirstResult(1);
		q2.setMaxResults(1);
		List results = q2.list();
		Iterator i = results.iterator();
		List<ProductDto> list = new ArrayList<ProductDto>();
		while (i.hasNext()) {

			// Objects position is being correlated by the createQuery above. IE. g.appID is
			// the first, so that would be obj[0]
			obj = (Object[]) i.next();
			id = (String) obj[0];
			name = (String) obj[1];
			String tags = (String) obj[2];
			img = (String) obj[3];
			desc = (String) obj[4];
			dscURL = (String) obj[5];
			twitchID = (String) obj[6];
			storeURL = (String) obj[7];
			list.add(new ProductDto(id, name, img, desc, dscURL, twitchID, storeURL));
		}

		// get TwitchTV stream
		String twitchResponse = getTwitchFeed(twitchID);

		// get discord server page
		String discordJsonString = callCustomSearchEngineApi(name);

		// Call method to parse JSON
		ArrayList<DiscordDto> discords = parseDiscords(discordJsonString);
		if (discords == null) {
			model.addAttribute("topdiscord", "https://www.google.com/search?q=discord server AND " + name);
		} else {
			model.addAttribute("topdiscord", discords.get(0).getLink());
		}

		s.flush();
		s.close();
		model.addAttribute("gameID", id);
		try {
			// temp image to go through to favorites/recents
			model.addAttribute("passthroughImg", URLEncoder.encode(img, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("displayImage", img);
		model.addAttribute("gameDesc", desc);
		model.addAttribute("gameName", name);
		model.addAttribute("twitchWidget", twitchResponse);
		model.addAttribute("storeURL", storeURL);
		return "likedgame";
	}
	
	public static String Q1(String gameName) {
		String temp = "select appID,gameName,tagName,image,description,discord,twitchGameID,storeURL from ProductDto WHERE gameName = '"
				+ gameName + "'";
		return temp;

	}
	
	public static String getTwitchFeed(String twitchGameID) {
		// Twitch game ID for the game recommended
		// Counter-Strike
		String channelName = "";
		String twitchResponse = "";
		if (twitchGameID == null || twitchGameID.isEmpty()) {

			twitchResponse = "<img id=\"missingtwitch\" src=\"resources/images/static.gif\"></img>";
		} else {

			try {
				HttpClient http = HttpClientBuilder.create().build();
				HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
				HttpGet getPage = new HttpGet("/helix/streams?game_id=" + twitchGameID);
				getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);

				HttpResponse resp = http.execute(host, getPage);

				String jsonString = EntityUtils.toString(resp.getEntity());
				JSONObject json = new JSONObject(jsonString);
				System.out.println(json.toString());
				try {

					// Gets the first stream in the array (most views)
					JSONArray allStreams = json.getJSONArray("data");
					JSONObject firstStream = allStreams.getJSONObject(0);

					String thumbnailUrl = firstStream.getString("thumbnail_url");

					Pattern channelPattern = Pattern.compile("(?<=.+_user_).+(?=-.+)");
					Matcher m = channelPattern.matcher(thumbnailUrl.trim());
					m.find();

					channelName = m.group(0);

					twitchResponse = "<div id=\"twitch-embed\"></div>" + "		<script type=\"text/javascript\">"
							+ "      var embed = new Twitch.Embed(\"twitch-embed\", {" + "        width: 750,"
							+ "        height: 440," + "        channel: \"" + channelName + "\","
							+ "        layout: \"video\"," + "        muted: true" + "      });"
							+ "      embed.addEventListener(Twitch.Embed.VIDEO_READY, () => {"
							+ "        var player = embed.getPlayer();" + "        player.play();" + "      });"
							+ "    </script>";

				} catch (JSONException e) {
					twitchResponse = "<img id=\"missingtwitch\" src=\"resources/images/static.gif\"></img>";
					return twitchResponse;
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Gets all live streams for the game ID, then selects the first stream in the
		// array (most views)

		return twitchResponse;

	}

	private ArrayList<DiscordDto> parseDiscords(String jsonString) {
		if (jsonString == null) {
			return null;
		}
		
		JSONObject json = new JSONObject(jsonString);
		int totalResults = json.getJSONObject("searchInformation").getInt("totalResults");

		// Return null if there are no search results
		if (totalResults == 0) {
			return null;
		}

		// Search results from first page (up to 10 results)
		JSONArray searchResults = json.getJSONArray("items");
		ArrayList<DiscordDto> discordsList = new ArrayList<>();

		// Grabs the link for each result, creates a discord
		// object, and stores it in an array
		for (int i = 0; i < searchResults.length(); i++) {
			JSONObject result = searchResults.getJSONObject(i);
			String link = result.getString("link");
			DiscordDto discord = new DiscordDto();
			discord.setLink(link);
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
			
			int statusCode = resp.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				return null;
			}
			
		} catch (IOException e) {
			e.printStackTrace(); // remove
		}
		return response;
	}

}
	
	
	


