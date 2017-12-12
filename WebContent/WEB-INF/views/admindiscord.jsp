<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>Discord</title>

</head>
<body>

<form action="discordsearch">
Enter Game Name: 
<input type=text value="Counter-Strike: Global Offensive" name="gamename">
<input type=submit value="Search Discords">
</form>

<p>Top Result: </p>
<table border="1">
<tr>
<td><a href="${topdiscord.link}">${topdiscord.title}</a></td>
				<td>${topdiscord.description}</td>
</tr>
</table>

<p>Results from First Page: </p>
<table border="1">
		<c:forEach var="discord" items="${discords}">
			<tr>
				<td><a href="${discord.link}">${discord.title}</a></td>
				<td>${discord.description}</td>
				
			</tr>

		</c:forEach>

	</table>

<p>${errormessage}</p>
</body>
</html>