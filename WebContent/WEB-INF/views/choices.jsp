<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to PlayRTD, ${persona}</title>
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
	<script type="text/javascript">
		$(document).ready(function() {
			$('#dice').click(function() {
				checked = $("input[type=checkbox]:checked").length;
				if (!checked) {
					alert("You must check at least one checkbox.");
					return false;
				}
			});
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
	<div class="info_column">
		<h1>${choicemsg}</h1>
		<h4>Select the ones you want to roll for!</h4>

		<div>
			<form id="getrolling_form" action="gameon" method="POST">
				<div id="checkbox1" class="checkbox">
					<input id="check1" type="checkbox" class="check" name="tag1"
						value="${tag1}" /> <label for="check1" id="label1">
						${tag1} </label>
				</div>
				<div id="checkbox2" class="checkbox">
					<input id="check2" type="checkbox" class="check" name="tag2"
						value="${tag2}" /> <label for="check2" id="label2">
						${tag2} </label>
				</div>
				<div id="checkbox3" class="checkbox">
					<input id="check3" type="checkbox" class="check" name="tag3"
						value="${tag3}" /> <label for="check3" id="label3">
						${tag3} </label>
				</div>
				<br /> <input id="dice" type="image"
					src="resources/images/dieroll.png" alt="submit">
			</form>
		</div>
	</div>
	</main>
	<footer class="fadeIn">
	<div></div>
	<nav> <a href="index">Home</a> <a href="favorites">My Likes</a> <a
		href="about">About Us</a></nav> </footer>
</body>
</html>