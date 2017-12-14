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

<title>${persona}'sFavorites</title>
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
	<header CLASS="fadeIn">
		<div id="headlogo">
			<img src="resources/images/playrtdlogo.png"></img>
		</div>
		<div id="logged_head">
			<p>Rolling as ${persona}</p>
			<img class="avatar" src="${avatar}"></img>
		</div>
	</header>
	<main class="fadeIn">
	<div id="home_info" class="info_column">
		<div></div>
		<h1>${persona}'sFavorites</h1>

		<c:forEach var="myFavs" items="${list}">
			<tr>
				<td>${myFavs.recentLikeName}</td>
				<td><a href="${myFavs.storeURL}" target="_blank">${myFavs.recentLikeIMG}</a></td>

			</tr>

		</c:forEach>
	</div>
	</main>
	<footer class="fadeIn">
		<div></div>
		<nav>
			<a href="index">Home</a> <a href="favorites">My Likes</a> <a
				href="about">About Us</a>
		</nav>
	</footer>
</body>
</html>
