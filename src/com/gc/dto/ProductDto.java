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
	private int tag;
	private String gameName;
	private String appID;
	private String image;
	

	public ProductDto() {

		// productID = 0;
		// code = "";
		// description = "";
		// listPrice = 0;
	}

	
	
	
	public ProductDto(int id, int tag, String gameName, String appID, String image) {
		super();
		this.id = id;
		this.tag = tag;
		this.gameName = gameName;
		this.appID = appID;
		this.image = image;
	}




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
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




	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", tag=" + tag + ", gameName=" + gameName + ", appID=" + appID + ", image="
				+ image + "]";
	}




	
	
	
}
