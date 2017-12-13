<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to PlayRTD, ${persona}</title>
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
	<div class="info_column">
		<h1>${nogames}${hasGames}</h1>
		<h4>Select the ones you want to roll for!</h4>

			<div>
				<form id="getrolling_form" action="gameon" method="POST">
					<label><input type="checkbox" name="tag1" value="${tag1}" />
						${tag1}</label> <label><input type="checkbox" name="tag2"
						value="${tag2}" /> ${tag2}</label> <label><input type="checkbox"
						name="tag3" value="${tag3}" /> ${tag3}</label> <br />
						<input id="dice" type="image" src="resources/images/dieroll.png"
				alt="submit">
				</form>
			</div>
	</div>
	</main>
	<footer>
	<div></div>
	<nav> <a href="">Home</a> <a href="">My Likes</a> <a href="about">About
		Us</a></nav> </footer>
</body>
</html>
