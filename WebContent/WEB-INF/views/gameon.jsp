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
	<div id="logged_head">
		<p>Rolling as ${persona}</p>
		<img class="avatar" src="${avatar}"></img>
	</div>
	</header>
	<main>
	<div id="logoptions">
		<nav><a href="">My Likes</a> <a href="">Log Out</a></nav>
	</div>


	<div class="info_column">
		<div id="optionbar">
			<a href=""><img id="reroll" src="resources/images/reroll.png"></img></a>
			<a href=""><img id="like" src="resources/images/likebutton.png"></img></a>
		</div>
		<h1>You rolled ${gameName}!</h1>
		<div class="info_header">
			<div>${gameImg}</div>
			<p>${gameDesc}</p>
		</div>

		<a id="discordlink" href="${discord}" target="_blank"><img class="discord_strip"
			src="resources/images/discord_strip.png"></img></a> ${twitchWidget}

	</div>
	</main>
	<footer>
	<div></div>
	<nav> <a href="">Home</a><a href="">My Likes</a><a href="about">About Us</a></nav> </footer>
</html>
