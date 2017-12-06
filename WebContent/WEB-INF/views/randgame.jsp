<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rand Game</title>

<form>
  <input type="radio" name="Multiplayer" value="3859" checked> Male<br>
  <input type="radio" name="Action" value="19"> Female<br>
  <input type="radio" name="Adventure" value="21"> Other  
  <input type="submit" value="roll">
</form> 
</head>
<body>

${gameID} 
${gameImg}
${gameName}
${description}
</body>
</html>