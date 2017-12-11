package com.playrtd.controller;
import java.util.ArrayList;

public class HelperMethods {

	public static boolean isOwned(int recommendedAppID, ArrayList<Integer> ownedGames) {
		return ownedGames.contains(recommendedAppID);
	}
}
