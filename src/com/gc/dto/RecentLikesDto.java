package com.gc.dto;

import java.io.Serializable;

public class RecentLikesDto implements Serializable {
	private static final long serialVersionUID = 1L;
	int ID;
	String userID;
	String recentLikeIMG;
	String recentLikeName;
	
	public RecentLikesDto(){
		
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

	
}
