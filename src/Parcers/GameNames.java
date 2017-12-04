package Parcers;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GameNames {
public static void main(String[] args) {
	try {
		Document doc = Jsoup.connect("http://store.steampowered.com/search/?tags=19&page=1").get();
		// this grabs the div containing all of the game names
		Elements temp = doc.select("div.col.search_name.ellipsis");
		int i=0;
		for(Element gameList: temp) {
			i++;
			//span is where the titles are held
			System.out.println(i +  " " + gameList.getElementsByTag("span").first().text());
		}
		
		
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}
