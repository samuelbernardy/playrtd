
package com.playrtd.controller;

import com.gc.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
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
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.playrtd.resource.SteamOpenID;
import com.playrtd.util.Credentials;

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
			userProfile.setAvatar(playerAvatar);

			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
			SessionFactory factory = cfg.buildSessionFactory();
			Session session = factory.openSession();
			Transaction t = session.beginTransaction();
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
	public String getgame(Model model, @RequestParam(value = "opt1", required = false) String tag1,
			@RequestParam(value = "opt2", required = false) String tag2,
			@RequestParam(value = "opt3", required = false) String tag3) {
		System.out.println("1" + tag1 + "2" + tag2 + "3" + tag3);
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
		Object[] obj = new Object[7];

		// String quer = "select g.appID,g.gameName,g.tag,g.image,g.description from
		// ProductDto g WHERE g.tag = "+tag+" ORDER BY RAND()";
		String quer = Q1(tag1, tag2, tag3);

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
			list.add(new ProductDto(id, name, img, desc, dscURL, twitchID));
		}

		String twitchChannel = getTwitchFeed(twitchID);

		s.flush();
		s.close();
		model.addAttribute("gameID", id);
		model.addAttribute("gameImg", img);
		model.addAttribute("gameDesc", desc);
		model.addAttribute("gameName", name);
		model.addAttribute("discord", dscURL);
		model.addAttribute("twitchChan", twitchChannel);
		return "gameon";
	}

	public static String Q1(String query) {
		String temp = "select g.appID,g.gameName,g.tagName,g.image,g.description,g.discord,g.twitchGameID from ProductDto g WHERE g.tagName = '"
				+ query + "' ORDER BY RAND()";
		return temp;
	}

	public static String Q1(String query, String query2) {
		String temp = "select g.appID,g.gameName,g.tagName,g.image,g.description,g.discord,g.twitchGameID from ProductDto g WHERE g.tagName = '"
				+ query + "' or g.tagName = '" + query2 + "' ORDER BY RAND()";
		return temp;
	}

	public static String Q1(String query, String query2, String query3) {
		String temp = "select g.appID,g.gameName,g.tagName,g.image,g.description,g.discord,g.twitchGameID from ProductDto g WHERE g.tagName = '"
				+ query + "' or g.tagName = '" + query2 + "' or g.tagName = '" + query3 + "' ORDER BY RAND()";
		return temp;

	}

	public static String getTwitchFeed(String twitchGameID) {
		// Twitch game ID for the game recommended
		// Counter-Strike
		String channelName = "";

		// Gets all live streams for the game ID, then selects the first stream in the
		// array (most views)
		try {
			HttpClient http = HttpClientBuilder.create().build();
			HttpHost host = new HttpHost("api.twitch.tv", 443, "https");
			HttpGet getPage = new HttpGet("/helix/streams?game_id=" + twitchGameID);
			getPage.setHeader("Client-ID", Credentials.TWITCH_CLIENT_ID);

			// TODO Handle response exceptions
			HttpResponse resp = http.execute(host, getPage);

			String jsonString = EntityUtils.toString(resp.getEntity());
			JSONObject json = new JSONObject(jsonString);
			JSONArray allStreams = json.getJSONArray("data");

			// Gets the first stream in the array (most views)
			JSONObject firstStream = allStreams.getJSONObject(0);

			String thumbnailUrl = firstStream.getString("thumbnail_url");

			Pattern channelPattern = Pattern.compile("(?<=.+_user_).+(?=-.+)");
			Matcher m = channelPattern.matcher(thumbnailUrl.trim());
			m.find();
			channelName = m.group(0);

			// TODO Parse channel name from thumbnail URL

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return channelName;
	}
}
