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
<title>${persona}'s Favorites</title>
</head>
<body>
	<header>
	<div id="headlogo">
		<img src="resources/images/playrtdlogo.png"></img>
	</div>
	<nav> <a href="login_page"><img
		src="resources/images/loginbutton.png"></img></a> </nav> </header>
	<main>
	<div id="home_info" class="info_column">
		<div></div>
		<h1>${persona}'s Favorites</h1>
		<c:foreach>
			<!-- Display Likes associated with current steamID -->
		</c:foreach>
	</div>
	</main>
	<footer class="footer">
	<div></div>
	<div></div>
	</footer>
</body>
</html>
