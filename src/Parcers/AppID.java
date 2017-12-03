package Parcers;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AppID {
public static void main(String[] args) {
	try {
		//TODO to grab as many games as we want, make a string based on the URL. 
		//create an int for tag and page to continue to dig through site for data base
		Document doc = Jsoup.connect("http://store.steampowered.com/search/?tags=19&page=1").get();
		
		Elements temp = doc.select("div#search_result_container");
		
		String div ="";
		int i=0;
		
		//This code grabs all of the information within the div of the 25 games showing.
		for(Element movieList: temp) {
			div = movieList.getElementsByTag("div").first().toString();
			i++;
		}
		
		//This code first splits the text to the number of values we need in the array. 
		//Then further splices it with substrings to grab the APPID
		for (i=0; i < 25; i++) {
			String[] games = div.split("<div style=\"clear:");
			games[i] = (games[i].substring(games[i].indexOf("d=\"")+3,games[i].indexOf("\" onmouseover=\""))); 
			
			System.out.println(i + " " + games[i]);
		}
		
		
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
