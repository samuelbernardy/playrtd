<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<link href="https://fonts.googleapis.com/css?family=Kanit" rel="stylesheet">
<link href="resources/styles.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Rand Game</title>
</head>
<body>
	<header>
	<div id="headlogo">
		<img src="resources/images/playrtdlogo.png"></img>
	</div>
	<nav> <a href="http://localhost:8080/PlayRTD/return"><img src="resources/images/loginbutton.png"></img></a>
	</nav> </header>
	<main>
<style> 

.whatever{
  font-style: italic;
  font-family: "muli italic", sans-serif;
  font-weight:600;
 color:rgb(240,252,255);
  transition-duration: .5s;
    background-color: rgba(64,73,91,1) ;
    display: inline-block;
    width: 100px;
    height: 40px;
  
}

#checkboxes input[type=radio]{
  
  
    display: none;
}

#checkboxes input[type=radio]:checked + .whatever{
  
  
  transition-duration: .5s;
   background-color: rgba(64,73,91,0.2);
  

}


</style>


<form>
<div id="checkboxes">
<input type="checkbox" name="tag" value="1">testval1
<input type="checkbox" name="tag" value="2">testval2
 <input type="checkbox" name="tag" value="3">testval3<br>
 <input type="checkbox" name="tag" value="113">Free to Play<br>
 <input type="checkbox" name="tag" value="21">Action<br>
 
 
   <!--   <input type="radio" name="tag" value="3859" id="r1" />
    <label class="whatever" for="r1">Multiplayer</label><br><br>
    <input type="radio" class="whatever" name="tag" value="19" id="r2" />
    <label class="whatever" for="r2">Adventure</label><br><br>
    <input type="radio" name="tag" value="21" id="r3" />
    <label class="whatever" for="r3">Action</label><br><br>
  <input type="radio" name="tag" value="113" id="r4" />
    <label class="whatever" for="r4">Free to Play</label><br><br>
    <input type="radio" name="tag" value="1743" id="r5" />
    <label class="whatever" for="r5">Fighting</label><br><br>
    <input type="radio" name="tag" value="3871" id="r6" />
    <label class="whatever" for="r6">2D</label><br><br>
    <input type="radio" name="tag" value="4736" id="r7" />
    <label class="whatever" for="r7">2D Fighter</label><br><br>
    <input type="radio" name="tag" value="7368" id="r8" />
    <label class="whatever" for="r8">Local Multiplayer</label><br><br>
    <input type="radio" name="tag" value="1625" id="r9" />
    <label class="whatever" for="r9">Platformer</label><br><br>
    <input type="radio" name="tag" value="1685" id="r10" />
    <label class="whatever" for="r10">Co-op</label><br><br>
    <input type="radio" name="tag" value="4158" id="r11" />
    <label class="whatever" for="r11">Beat 'em up</label><br><br>
    <input type="radio" name="tag" value="3841" id="r12" />
    <label class="whatever" for="r12">Local Co-Op</label><br><br>
    <input type="radio" name="tag" value="3843" id="r13" />
    <label class="whatever" for="r13">Online Co-Op</label><br><br>
    <input type="radio" name="tag" value="492" id="r14" />
    <label class="whatever" for="r14">Indie</label><br><br>
    <input type="radio" name="tag" value="4840" id="r15" />
    <label class="whatever" for="r15">4 Player Local</label><br><br>
    <input type="radio" name="tag" value="128" id="r16" />
    <label class="whatever" for="r16">Massively Multiplayer</label><br><br>
  <input type="radio" name="tag" value="4182" id="r17" />
    <label class="whatever" for="r17">Singleplayer</label><br><br>
  <input type="radio" name="tag" value="1662" id="r18" />
    <label class="whatever" for="r18">Survival</label><br><br>
  <input type="radio" name="tag" value="4085" id="r19" />
    <label class="whatever" for="r19">Anime</label><br><br>
  -->
  
  



  
  

  <input type="submit">
</div>
</form> 
</head>
<body>

${gameID} 
${gameImg}
${gameName}
${description}
</main>

</body>
</html>