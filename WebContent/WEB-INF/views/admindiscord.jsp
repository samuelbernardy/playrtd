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

<p>${errormessage}</p>
</body>
</html>