<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://embed.twitch.tv/embed/v1.js"></script>
<title>You rolled ${gameName}!</title>
</head>
<body>
	<header>
	<div id="headlogo">
		<img src="resources/images/playrtdlogo.png"></img>
	</div>
	<nav> <a href="https://www.steamcommunity.com/openid/login/"><img
		src="resources/images/loginbutton.png"></img></a> </nav> </header>
	<main>


	<div class="info_column">

		<h1>You rolled ${gameName}!</h1>
		<a href=""><img id="reroll" src="resources/images/reroll.png"></img></a>
		<div class="info_header">
			<div>${gameImg}</div>
			<p>${gameDesc}</p>
		</div>

		<a id="discordlink" href=""><img class="discord_strip"
			src="resources/images/discord_strip.png"></img></a>


		<div id="twitch-embed"></div>

		<!-- Load the Twitch embed script -->
		<!--
      Create a Twitch.Embed object that will render
      within the "twitch-embed" root element.
    -->
		<script type="text/javascript">
      var embed = new Twitch.Embed("twitch-embed", {
        width: 750,
        height: 440,
        channel: "${twitchChan}",
        layout: "video",
        autoplay: false
      });

      embed.addEventListener(Twitch.Embed.VIDEO_READY, () => {
        var player = embed.getPlayer();
        player.play();
      });
    </script>
	</div>
	</main>
	<footer> </footer>
</body>
</html>
