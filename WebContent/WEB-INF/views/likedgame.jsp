<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://embed.twitch.tv/embed/v1.js"></script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<title>${gameName}!</title>
</head>
<body>
	<script>
		$(window).on("load", function() {
			console.log('Starting Anim')
			$(".fadeIn").animate({
				opacity : 1
			}, 1000);
			console.log('Ending Anim')
		});
	</script>
	<header class="fadeIn">
		<div id="headlogo">
			<img src="resources/images/playrtdlogo.png"></img>
		</div>
		<div></div>
	</header>
	<main class="fadeIn">
	<div id="logoptions">
		<nav></nav>
	</div>

	<div class="info_column">
		<div id="optionbar"></div>
		<h1>${gameName}!</h1>
		<div class="info_header">
			<div>
				<a href="${storeURL}" target="_blank">${displayImage}</a>
			</div>
			<p>${gameDesc}</p>
		</div>

		<a id="discordlink" href="${topdiscord}" target="_blank"><img
			class="discord_strip" src="resources/images/discord_strip.png"></img></a>
		${twitchWidget}

	</div>
	</main>
	<footer class="fadeIn">
		<div></div>
		<nav>

			<a href="index">Home</a> <a href="about">About Us</a>
		</nav>
	</footer>


</body>
</html>
