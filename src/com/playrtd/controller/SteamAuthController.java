
package com.playrtd.controller;

import com.gc.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.playrtd.resource.SteamOpenID;
import com.playrtd.util.Credentials;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
@SessionAttributes("selectTags")
@Controller
public class SteamAuthController {

	private SteamOpenID steamOpenID = new SteamOpenID();
	ArrayList<String> selectTags = new ArrayList<String>();
	

	@RequestMapping(value = "/login_page", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request) {

		// log.debug("Trying to call Steam OpenID...");

		String openIdRedirectUrl = steamOpenID.login("http://localhost:8080/PlayRTD/return");

		// log.debug("Redirect URL : " + openIdRedirectUrl);

		return new ModelAndView("redirect:" + openIdRedirectUrl);
	}

	// This mapping handles Steam Authentication and returns use data including
	// STEAMID, RECENTGAMES, and OWNEDGAMES
	@RequestMapping(value = "/return", method = RequestMethod.GET)
	public RedirectView postLoginPage(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Long steam_ID = 0l;
		String userId = this.steamOpenID.verify(request.getRequestURL().toString(), request.getParameterMap());
		System.out.println(userId);
		steam_ID = Long.parseLong(userId);
		UserInfoDto userProfile = new UserInfoDto();

		// establish session to keep user logged in for subsequent actions.

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

		// check owned and recent game count. Offer random apptags if empty.
		int ownedCountCheck = ownedJson.getJSONObject("response").getInt("game_count");
		int recentCountCheck = recentJson.getJSONObject("response").getInt("total_count");
		if (ownedCountCheck >= 1 && recentCountCheck >= 1) {
			JSONArray ownedJSONArray = ownedJson.getJSONObject("response").getJSONArray("games");
			JSONArray recentJSONArray = recentJson.getJSONObject("response").getJSONArray("games");

			// find user data and populate owned and recent games array
			String playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("avatarfull"));
			String playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0)
					.getString("personaname"));
			ArrayList<Integer> ownedGamesArray = new ArrayList<Integer>();
			ArrayList<Integer> recentGamesArray = new ArrayList<Integer>();
			for (int i = 0; i < ownedJSONArray.length(); i++) {
				Integer ownedInt = ownedJSONArray.getJSONObject(i).getInt("appid");
				ownedGamesArray.add(ownedInt);
			}
			for (int i = 0; i < recentJSONArray.length(); i++) {
				System.out.println(recentJSONArray);
				Integer recentInt = recentJSONArray.getJSONObject(i).getInt("appid");
				recentGamesArray.add(recentInt);
			}

			// populate user Object
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
			// System.out.println(recentGamesArray.size());

			selectTags = new ArrayList<String>();
			try {
				ArrayList<String> recentTagsArray = new ArrayList<String>();

				// Check if the game has age check and grab app tags based on page layout
				for (int i = 0; i < recentGamesArray.size(); i++) {
					Document doc = Jsoup
							.connect("http://store.steampowered.com/app/" + recentGamesArray.get(i) + "/agecheck")
							.get();
					Elements tempTag;
					String holder = "";
					String[] games;

					if (doc.toString().contains("glance_tags popular_tags")) {
						tempTag = doc.select("div.glance_tags.popular_tags");
					} else {
						tempTag = doc.select("div.glance_tags_ctn.popular_tags_ctn");
					}
					for (Element tagHolder : tempTag) {
						// System.out.println(i + " " + tagHolder.toString());
						holder = tagHolder.toString();
					}
					games = holder.split("a>");

					// This code first splits the text to the number of values we need in the array.
					// Then further splices it with substrings to grab the APPID
					for (int j = 0; j < 3; j++) {
						if (doc.toString().contains("glance_tags popular_tags")) {

							recentTagsArray
									.add(games[j].substring(games[j].indexOf("none;\">") + 8, games[j].indexOf(" </")));
						} else {

							recentTagsArray.add(
									games[j].substring(games[j].indexOf("\"app_tag\">") + 11, games[j].indexOf(" </")));
						}
					}
					// recentTagsArray
					// .add(games[j].substring(games[j].indexOf("none;\">") + 8, games[j].indexOf("
					// </")));
					// }
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
System.out.println(selectTags.get(2).toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.addCookie(new Cookie("steamID", steam_ID.toString()));
			response.addCookie(new Cookie("avatar", playerAvatar));
			response.addCookie(new Cookie("persona", playerPersona));
			model.addAttribute("tag1", selectTags.get(0).toString());
			model.addAttribute("tag2", selectTags.get(1).toString());
			model.addAttribute("tag3", selectTags.get(2).toString());
			model.addAttribute("dTag1", selectTags.get(0).toString());
			model.addAttribute("dTag2", selectTags.get(1).toString());
			model.addAttribute("dTag3", selectTags.get(2).toString());

			return new RedirectView("choices");
		} else {

			String nogamesnotif = "It looks like you haven't played for a little bit. Let us help!...";
			response.addCookie(new Cookie("nogames", nogamesnotif));

			return new RedirectView("choices");
		}

	}

	@RequestMapping(value = "/choices", method = RequestMethod.GET)
	public String choices(Model model, @CookieValue(value = "steamID", required = false) Long steamid,
			@CookieValue(value = "avatar", required = false) String avatar,
			@CookieValue(value = "persona", required = false) String persona,
			@ModelAttribute("tag1") String tag1,@ModelAttribute("tag2") String tag2, @ModelAttribute("tag3") String tag3,
			@CookieValue(value = "nogames", required = false) String nogames) {

		model.addAttribute("avatar", avatar);
		model.addAttribute("persona", persona);
//		model.addAttribute("opt1", tag1);
//		model.addAttribute("opt2", tag2);
//		model.addAttribute("opt3", tag3);
		model.addAttribute("hasGames", "It seems that you like " + tag1 + ", " + tag2 + ", and " + tag3 + " games.");
		model.addAttribute("nogames", nogames);

		return "choices";
	}

	@RequestMapping(value = "/gameon")
	public String getgame(@CookieValue("steamID") Long steam_ID, @CookieValue("avatar") String avatar,
			@CookieValue("persona") String persona, Model model,
			@RequestParam(value = "tag1", required = false) String tag1,
			@RequestParam(value = "tag2", required = false) String tag2,
			@RequestParam(value = "tag3", required = false) String tag3, HttpSession jSession) {
		System.out.println("1" + tag1 + "2" + tag2 + "3" + tag3);
		System.out.println(steam_ID + avatar + persona);
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
		if (tag1 == null && tag2 == null && tag3 == null) {
			//no selection recommendation
		}
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

		String discordResult = "https://www.google.com/search?q=discord server AND " + name;

		String twitchResponse = getTwitchFeed(twitchID);

		s.flush();
		s.close();
		model.addAttribute("avatar", avatar);
		model.addAttribute("persona", persona);
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
		model.addAttribute("discord", discordResult);
		model.addAttribute("twitchWidget", twitchResponse);
		model.addAttribute("steamID",  steam_ID);
		model.addAttribute("tag1", tag1);
		model.addAttribute("tag2", tag2);
		model.addAttribute("tag3", tag3);
		return "gameon";
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
}
