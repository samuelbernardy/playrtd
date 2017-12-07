package com.gc.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int userno;
	private long steam_ID;
	private String personaName;
	private String avatar;
	private ArrayList<Integer> ownedGames = new ArrayList<Integer>();
	private ArrayList<Integer> recentGames = new ArrayList<Integer>();

	
	public int getUserno() {
		return userno;
	}

	public void setUserno(int userno) {
		this.userno = userno;
	}

	public ArrayList<Integer> getOwnedGames() {
		return ownedGames;
	}

	public void setOwnedGames(ArrayList<Integer> ownedGames) {
		this.ownedGames = ownedGames;
	}

	public ArrayList<Integer> getRecentGames() {
		return recentGames;
	}

	public void setRecentGames(ArrayList<Integer> recentGames) {
		this.recentGames = recentGames;
	}

	public UserInfoDto () {
		
	}

	public long getSteam_ID() {
		return steam_ID;
	}

	public void setSteam_ID(long steam_ID) {
		this.steam_ID = steam_ID;
	}

	public String getPersonaName() {
		return personaName;
	}

	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
