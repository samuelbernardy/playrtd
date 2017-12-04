package com.gc.dto;

public class RecentGamesDto {
	
	private int appID;
	private String tags;
	private String gameName;
	
	public RecentGamesDto () {
		
	}

	public int getAppID() {
		return appID;
	}

	public void setAppID(int appID) {
		this.appID = appID;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
}
