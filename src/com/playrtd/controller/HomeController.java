package com.playrtd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gc.dto.UserInfoDto;
import com.nimbusds.jose.JWEObject.State;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.client.ClientInformation;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.playrtd.util.Credentials;
import com.nimbusds.openid.connect.*;;

/*
 * author: samuelbernardy
 * author: yasrodriguez
 * author: josephdgarza
 */

@Controller
public class HomeController {

	@RequestMapping("/")
	public String index() {

		return "";
	}

	@RequestMapping("/login")
	public String steamAuth() throws URISyntaxException, ParseException, IOException {
		// The client identifier provisioned by the server
		ClientID clientID = new ClientID(Credentials.STEAM_API_KEY);

		// The client callback URL
		URI callback = new URI(Credentials.STEAM_CALLBACK);

		// Generate random state string for pairing the response to the request
		// State state = new State();
		com.nimbusds.oauth2.sdk.id.State state = new com.nimbusds.oauth2.sdk.id.State(5);
		//
		// Generate nonce
		Nonce nonce = new Nonce();

		// Compose the request (in code flow)
		AuthenticationRequest req = new AuthenticationRequest(new URI("https://steamcommunity.com/openid/"),
				new ResponseType("code"), Scope.parse("openid email profile address"), clientID, callback,
				new com.nimbusds.oauth2.sdk.id.State(5), nonce);
		// AuthenticationRequest req = new AuthenticationRequest(
		// new URI("https://steamcommunity.com/openid/login"),
		// new ResponseType("code"),
		// Scope.parse("openid email profile address"),
		// clientID,
		// callback,
		// state,
		// nonce);

		HTTPResponse httpResponse = req.toHTTPRequest().send();
		System.out.println(httpResponse.getStatusCode());
		System.out.println(httpResponse.getStatusMessage());

		System.out.println(httpResponse.getHeaders());
		System.out.println(httpResponse.getContent());
		System.out.println(httpResponse.getLocation());

		AuthenticationResponse response = AuthenticationResponseParser.parse(httpResponse);

		if (response instanceof AuthenticationErrorResponse) {
			// process error
		}

		AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) response;

		// Retrieve the authorisation code
		AuthorizationCode code = successResponse.getAuthorizationCode();

		// Don't forget to check the state
		assert successResponse.getState().equals(state);
		System.out.println(response.toString());
		return "";
	}

	@RequestMapping("/return")
	public String loggedIn(Model model) throws IOException {

		// Strings from UserSummary
		//featherscs PUBLIC steamid : 76561198094590002
		//puffins PUBLIC steamid : 76561197992237092
		long steam_ID = 76561198094590002l;
		String personaName = "";
		String avatar = "";
		UserInfoDto userProfile = new UserInfoDto();

		// Strings from OwnedGames
		int ownedAppID = 0;

		// Strings from RecentGames
		int recentAppID = 0;

		// request urls and readers to parse json on repsonse.
		URL playerSummaryURL = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="
				+ Credentials.STEAM_API_KEY + "&steamids=" + steam_ID + "&format=json");
		URL playOwnedGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");
		URL playRecentGamesURL = new URL("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key="
				+ Credentials.STEAM_API_KEY + "&steamid=" + steam_ID + "&format=json");
		System.out.println(playRecentGamesURL);
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

		JSONObject playerJson = new JSONObject(playerJsonStr);
		JSONObject ownedJson = new JSONObject(ownedJsonStr);
		JSONObject recentJson = new JSONObject(recentJsonStr);
		
		String playerID = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("steamid"));
		String playerAvatar = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("avatarfull"));
		String playerPersona = (playerJson.getJSONObject("response").getJSONArray("players").getJSONObject(0).getString("personaname"));	
		ArrayList<Integer> ownedGamesArray = new ArrayList<Integer>();
		ArrayList<Integer> recentGamesArray = new ArrayList<Integer>();
		for (int i = 0; i < ownedJson.getJSONObject("response").getJSONArray("games").length(); i++) {
			Integer ownedString = ownedJson.getJSONObject("response").getJSONArray("games").getJSONObject(i).getInt("appid");
			ownedGamesArray.add(ownedString);
			}
		for (int i = 0; i < recentJson.getJSONObject("response").getJSONArray("games").length(); i++) {
			Integer recentString = recentJson.getJSONObject("response").getJSONArray("games").getJSONObject(i).getInt("appid");
			recentGamesArray.add(recentString);
			}
		
		userProfile.setSteam_ID(steam_ID);
		userProfile.setPersonaName(personaName);
		userProfile.setAvatar(playerAvatar);
		userProfile.setRecentGames(recentGamesArray);
		userProfile.setOwnedGames(ownedGamesArray);
		
//		System.out.println(steam_ID + " " + personaName + " " + " " + avatar);
//
//		for (int i = 0; i < ownedJson.length(); i++) {
//			ownedAppID = ownedJson.getJSONObject(i).getInt("appid");
//		}
//
//		System.out.println(ownedAppID);
//
//		for (int i = 0; i < recentJson.length(); i++) {
//			recentAppID = recentJson.getJSONObject(i).getInt("appid");
//		}


		
		 model.addAttribute("avatar", playerAvatar);
		 model.addAttribute("persona", playerPersona.toString());


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