package com.gc.dto;

import java.io.Serializable;

public class GameIdMappingDto implements Serializable {
	int appId;
	String gameName;
	String twitchGameId;
	
	public GameIdMappingDto() {
		appId = 0;
		gameName = "";
		twitchGameId = "";
	}
	
	public GameIdMappingDto(int appId, String gameName, String twitchGameId) {
		this.appId = appId;
		this.gameName = gameName;
		this.twitchGameId = twitchGameId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getTwitchGameID() {
		return twitchGameId;
	}

	public void setTwitchGameID(String twitchGameId) {
		this.twitchGameId = twitchGameId;
	}

	@Override
	public String toString() {
		return "GameIdsMappingDto [appId=" + appId + ", gameName=" + gameName + ", twitchGameId=" + twitchGameId + "]";
	}
}
