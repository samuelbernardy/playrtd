<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="https://embed.twitch.tv/embed/v1.js"></script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<title>PlayRTD</title>
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
	<nav> <a href="login_page"><img
		src="resources/images/loginbutton.png"></img></a> </nav> </header>
	<main class="fadeIn">
	<div id="home_info" class="info_column">
		<h1>Welcome to PlayRTD!</h1>

		<p>We're taking the hassle out of the search for your next video
			game. Yeah yeah, you got options, but what you don't have is
			decisiveness. That's right Mr.Twenty-Browser-Tabs-And-Counting, we
			did it all for you. Log in with your Steam Account and get a tailored
			suggestion with the media you need to get involved quick. Don't
			believe us? Check out some of the suggestions below and tell us we're
			not the best match making service since $1 Valentines.</p>
	</div>
	<div id="recent_container">
		<c:forEach var="recLikes" items="${list}">
			<div class="recent_like">${recLikes.persona} liked ${recLikes.recentLikeName}
			
				<a href="likedgame?gameName=${recLikes.recentLikeName}" target="_blank" onClick="">${recLikes.recentLikeIMG}</a>
			</div>

		</c:forEach>
	</div>
	</main>
	<footer class="fadeIn">
	<div></div>
	<nav> <a href="index">Home</a> <a href="about">About Us</a></nav> </footer>
</body>
</html>
