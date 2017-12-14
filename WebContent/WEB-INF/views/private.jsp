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
		<h1 id="sadface">:(</h1>
		<h1>We can't see you!</h1>

		<p>Unfortunately, your steam profile is set to private. Without
			seeing your info, we can't recommend you a game. PLease, set your
			steam profile to public, and we'll be happy to have you back.</p>
	</div>
	</main>
	<footer class="fadeIn">
	<div></div>
	<nav> <a href="index">Home</a> <a href="about">About Us</a></nav> </footer>
</body>
</html>