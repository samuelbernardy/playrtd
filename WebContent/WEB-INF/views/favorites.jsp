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
<title>${persona}'s Favorites</title>
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
	<div id="home_info" class="info_column">
		<div></div>
		<h1>${persona}'s Favorites</h1>
		
		<c:forEach var="myFavs" items="${list}">
			<tr>
				<td>${myFavs.recentLikeName}</td>
				<td><a href="${myFavs.storeURL}">${myFavs.recentLikeIMG}</a></td>
				
			</tr>

		</c:forEach>
	</div>
	</main>
	<footer class="footer">
	<div></div>
	<div></div>
	</footer>
</body>
</html>
