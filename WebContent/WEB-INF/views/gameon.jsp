<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
		<form method="GET"
			onsubmit="event.preventDefault(); return loadDoc(this);">
			<input type="hidden" id="gameName" name="gameName"
				value="${gameName}" /> <input type="hidden" id="gameImg"
				name="gameImg" value="${gameImg}" /> <input type="hidden"
				id="persona" name="persona" value="${persona}" /> <input
				type="submit" value="like" />
		</form>

		<a id="discordlink" href="${discord}" target="_blank"><img class="discord_strip"
			src="resources/images/discord_strip.png"></img></a> ${twitchWidget}

	</div>
	
	<footer>
	<div></div>
	<nav> <a href="">Home</a><a href="">My Likes</a><a href="about">About Us</a></nav> </footer>
	<script>
function loadDoc(form) {
	
	var gameName = document.getElementById("gameName").value;
	var gameImg = document.getElementById("gameImg").value;

	var persona = document.getElementById("persona").value;

  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      
      console.log(this.responseText);
    }else{
    	console.log("error:" + this.responseText);
    }
  };
  xhttp.open("GET", "like?gameName="+gameName+ "&gameImg="+gameImg + "&persona="+persona, true);
  xhttp.send();
}
</script>
	
	</body>
</html>
