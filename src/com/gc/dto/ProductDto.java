package com.gc.dto;

import java.io.Serializable;

public class ProductDto implements Serializable {
	/**
	 * The serialization runtime associates with each serializable class a version
	 * number, called a serialVersionUID, which is used during deserialization to
	 * verify that the sender and receiver of a serialized object have loaded
	 * classes for that object that are compatible with respect to serialization.
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String tag;
	private String tagName;
	private String gameName;
	private String appID;
	private String image;
	private String description;
	private String discord;
	private String twitchGameID;
	private String storeURL;

	public ProductDto() {

	}



	public ProductDto(int id, String tag, String gameName, String appID, String image, String description,
			String discord) {
		super();
		this.id = id;
		this.tag = tag;
		this.gameName = gameName;
		this.appID = appID;
		this.image = image;
		this.description = description;
		this.discord = discord;
	}

	public ProductDto(String gameName, String appID, String image, String description, String discord, String twitchGameID, String storeURL) {
		super();

		this.gameName = gameName;
		this.appID = appID;
		this.image = image;
		this.description = description;
		this.discord = discord;
		this.twitchGameID = twitchGameID;
		this.storeURL = storeURL;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDiscord() {
		return discord;
	}

	public void setDiscord(String discord) {
		this.discord = discord;
	}

	public String getTwitchGameID() {
		return twitchGameID;
	}

	public void setTwitchGameID(String twitchGameId) {
		this.twitchGameID = twitchGameId;
	}
	

	public String getStoreURL() {
		return storeURL;
	}



	public void setStoreURL(String storeURL) {
		this.storeURL = storeURL;
	}



	@Override
	public String toString() {
		return gameName + appID + image + description;
	}

}
