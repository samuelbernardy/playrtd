<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
  <body>
  <form action = "getgamename">
  Enter Twitch Game ID:
  <input type = "text" name="twitchgameid" value="32399">
  <input type = "submit" value="Submit">
  </form>
   <p> ${gamename}</p>
 
 <form action = "getgameid">
  Enter Twitch Game Name:
  <input type = "text" name="twitchgamename" value="Counter-Strike: Global Offensive">
  <input type = "submit" value="Submit">
  </form>
 <p> ${gameid}</p>
    
  <a href="getstream">Sample Stream</a><br>
    
    <!-- Add a placeholder for the Twitch embed -->
    <div id="twitch-embed"></div>

    <!-- Load the Twitch embed script -->
    <script src="https://embed.twitch.tv/embed/v1.js"></script>

    <!--
      Create a Twitch.Embed object that will render
      within the "twitch-embed" root element.
    -->
    <script type="text/javascript">
      var embed = new Twitch.Embed("twitch-embed", {
        width: 854,
        height: 480,
        channel: "${channel}",
        layout: "video",
        autoplay: false
      });

      embed.addEventListener(Twitch.Embed.VIDEO_READY, () => {
        var player = embed.getPlayer();
        player.play();
      });
    </script>
    </body>
</html>