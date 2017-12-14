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
<title>You rolled ${gameName}!</title>
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
		<div id="logged_head">
			<p>Rolling as ${persona}</p>
			<img class="avatar" src="${avatar}"></img>
		</div>
	</header>
	<main class="fadeIn">
	<div id="logoptions">
		<nav>
			<a href="favorites">My Likes</a> <a href="index">Log Out</a>
		</nav>
	</div>

	<div class="info_column">
		<div id="optionbar">
			<a href="gameon?tag1=${tag1}&tag2=${tag2}&tag3=${tag3}"><img
				id="reroll" src="resources/images/reroll.png"></img></a>
			<!--  <a href=""><img id="like" src="resources/images/likebutton.png"></img></a>-->
			<form method="GET"
				onsubmit="event.preventDefault(); return loadDoc(this);">
				<input type="hidden" id="gameName" name="gameName"
					value="${gameName}"> <input type="hidden" id="gameImg"
					name="gameImg" value="${passthroughImg}"> <input
					type="hidden" id="persona" name="persona" value="${persona}">
				<input type="hidden" id="steamID" name="steamID" value="${steamID}">
				<input type="hidden" id="avatar" name="avatar" value="${avatar}">
				<input type="hidden" id="gameID" name="gameID" value="${gameID}">
				<input type="hidden" id="storeURL" name="storeURL"
					value="${storeURL}"> <input id="like" type="image"
					src="resources/images/likebutton.png" alt="submit" value="like">
			</form>
		</div>
		<h1>You rolled ${gameName}!</h1>
		<div class="info_header">
			<div>
				<a href="${storeURL}" target="_blank">${displayImage}</a>
			</div>
			<p>${gameDesc}</p>
		</div>
		<form method="GET"
			onsubmit="event.preventDefault(); return loadDoc(this);">
			<input type="hidden" id="gameName" name="gameName"
				value="${gameName}" /> <input type="hidden" id="gameImg"
				name="gameImg" value="${gameImg}" /> <input type="hidden"
				id="persona" name="persona" value="${persona}" />
		</form>
		<a id="discordlink" href="${topdiscord}" target="_blank"><img
			class="discord_strip" src="resources/images/discord_strip.png"></img></a>
		${twitchWidget}

	</div>
	</main>
	<footer class="fadeIn">
		<div></div>
		<nav>
			<a href="index">Home</a> <a href="favorites">My Likes</a> <a
				href="about">About Us</a>
		</nav>
	</footer>
	<script>
		function loadDoc(form) {

			var gameName = document.getElementById("gameName").value;
			var gameImg = document.getElementById("gameImg").value;
			var steamID = document.getElementById("steamID").value;
			var persona = document.getElementById("persona").value;
			var gameID = document.getElementById("gameID").value;
			var storeURL = document.getElementById("storeURL").value;

			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {

					console.log(this.responseText);
				} else {
					console.log("error:" + this.responseText);
				}
			};
			xhttp.open("GET", "like?gameName=" + gameName + "&gameImg="
					+ gameImg + "&persona=" + persona + "&steamID=" + steamID
					+ "&gameID=" + gameID + "&storeURL=" + storeURL, true);
			xhttp.send();
		}
	</script>

</body>
</html>
