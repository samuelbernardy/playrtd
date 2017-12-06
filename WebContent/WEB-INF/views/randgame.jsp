<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rand Game</title>

<form>
<div id="checkboxes">
  <input type="radio" name="tag" value="3859"> Multiplayer <br>
  <input type="radio" name="tag" value="19"> Adventure <br>
  <input type="radio" name="tag" value="21"> Action <br>
  <input type="radio" name="tag" value="113"> Free to Play <br>
  <input type="radio" name="tag" value="1743"> Fighting <br>
  <input type="radio" name="tag" value="3871"> 2D <br>
  <input type="radio" name="tag" value="4736"> 2D Fighter <br>
  <input type="radio" name="tag" value="7368"> Local Multiplayer <br>
  <input type="radio" name="tag" value="1625"> Platformer <br>
  <input type="radio" name="tag" value="1685"> Co-op <br>
  <input type="radio" name="tag" value="4158"> Beat 'em up <br>
  <input type="radio" name="tag" value="3841"> Local Co-Op <br>
  <input type="radio" name="tag" value="3843"> Online Co-Op <br>
  <input type="radio" name="tag" value="492"> Indie <br>
  <input type="radio" name="tag" value="4840"> 4 Player Local <br>
  <input type="radio" name="tag" value="128"> Massively Multiplayer <br>
  <input type="radio" name="tag" value="4182"> Singleplayer <br>
  <input type="radio" name="tag" value="1662"> Survival <br>
  <input type="radio" name="tag" value="4085"> Anime <br>

  
  
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