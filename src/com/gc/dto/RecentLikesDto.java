package com.gc.dto;

import java.io.Serializable;

public class RecentLikesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	int ID;
	String userID;
	String persona;
	String recentLikeIMG;
	String recentLikeName;
	String storeURL;
	String appID;

	public RecentLikesDto() {

	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

//	public RecentLikesDto(String recentLikeIMG, String recentLikeName, String appID) {
//		super();
//		this.recentLikeIMG = recentLikeIMG;
//		this.recentLikeName = recentLikeName;
//		this.appID = appID;
//
//	}

	public RecentLikesDto(String recentLikeIMG, String recentLikeName) {
		super();
		this.recentLikeIMG = recentLikeIMG;
		this.recentLikeName = recentLikeName;

	}
	public RecentLikesDto(String recentLikeIMG, String recentLikeName, String persona) {
		super();
		this.recentLikeIMG = recentLikeIMG;
		this.recentLikeName = recentLikeName;
		this.persona = persona;

	}

	public RecentLikesDto(String userID, String persona, String recentLikeIMG, String recentLikeName, String storeURL) {
		super();
		this.userID = userID;
		this.persona = persona;
		this.recentLikeIMG = recentLikeIMG;
		this.recentLikeName = recentLikeName;
		this.storeURL = storeURL;

	}

	public RecentLikesDto(int iD, String userID, String recentLikeIMG, String recentLikeName) {
		super();
		ID = iD;
		this.userID = userID;
		this.recentLikeIMG = recentLikeIMG;
		this.recentLikeName = recentLikeName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRecentLikeIMG() {
		return recentLikeIMG;
	}

	public void setRecentLikeIMG(String recentLikeIMG) {
		this.recentLikeIMG = recentLikeIMG;
	}

	public String getRecentLikeName() {
		return recentLikeName;
	}

	public void setRecentLikeName(String recentLikeName) {
		this.recentLikeName = recentLikeName;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public String getStoreURL() {
		return storeURL;
	}

	public void setStoreURL(String storeURL) {
		this.storeURL = storeURL;
	}

}
