<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="https://fonts.googleapis.com/css?family=Kanit"
	rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
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
	<header class="fadeIn">
	<div id="headlogo">
		<a href="test"><img src="resources/images/playrtdlogo.png"></img></a>
	</div>
	<div id="logged_head">
		<p>Rolling as ${persona}</p>
		<img class="avatar" src="${avatar}"></img>
	</div>
	</header>
	<main class="fadeIn">
	<div class="info_column">
		<h1>
			About Us<br> <br>
		</h1>

		<h2>Joseph Garza</h2>
		<div class="bio">
			<img src="resources/images/joebio.jpg"></img>
			<p>
				"The best way to describe the reason for my choice to become a
				Software Developer is best summed up through a story from my past.
				When I was a child, about the age of 5. I sat on the porch one day
				and I felt the wind blow and I seen the trees moving in the wind. It
				really got me thinking "What makes the wind?". I pondered maybe the
				trees are making it? But then I realized they were merely being
				moved by the wind. So I thought more and came to the only conclusion
				that could make sense at that time (I still think it's a good
				theory!). Maybe the wind is made by everyone on the earth breathing
				at the same time!!! Okay, so how does that story sum up why this is
				was my career choice? Well, this has always been my thought process.
				I have always been extremely curious how things work and have always
				dove deep into thought to figure out solutions. Which is exactly
				what is needed as a Software Developer!"<br /> <em><strong><a href="https://github.com/JosephDGarza" target="_blank">Github</a><br />
				<a href="https://www.linkedin.com/in/joseph-garza/" target="_blank">LinkedIn</a></strong></em>
			</p>

		</div>
		<h2>Yasmin Rodriguez</h2>
		<div class="bio">
			<p>"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed
				do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
				enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi
				ut aliquip ex ea commodo consequat. Duis aute irure dolor in
				reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
				pariatur. Excepteur sint occaecat cupidatat non proident, sunt in
				culpa qui officia deserunt mollit anim id est laborum."
				<br /> <em><strong><a href="https://github.com/yasrodriguez" target="_blank">Github</a><br />
				<a href="https://www.linkedin.com/in/yasrodriguez/" target="_blank">LinkedIn</a></strong></em></p>
			<img src="resources/images/yasminbio.jpg"></img>
		</div>
		<h2>Samuel Bernardy</h2>
		<div class="bio">
			<img src="resources/images/sambio.jpg"></img>
			<p>"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed
				do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut
				enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi
				ut aliquip ex ea commodo consequat. Duis aute irure dolor in
				reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
				pariatur. Excepteur sint occaecat cupidatat non proident, sunt in
				culpa qui officia deserunt mollit anim id est laborum." <br /> <em><strong><a href="https://github.com/samuelbernardy" target="_blank">Github</a><br />
				<a href="https://www.linkedin.com/in/samuelbernardy/" target="_blank">LinkedIn</a></strong></em></p>
		</div>
	</div>
	</main>
	<footer class="fadeIn">
	<div></div>
	<nav> <a href="index">Home</a> <a href="favorites">My Likes</a> <a href="about">About
		Us</a></nav> </footer>
</body>
</html>
