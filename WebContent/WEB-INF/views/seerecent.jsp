<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recent Recommendations</title>
</head>
<body>
<h1> recent likes view</h1>

		<c:forEach var="recLikes" items="${list}">
			<tr>
				<td>${recLikes.recentLikeName}</td>
				<td>${recLikes.recentLikeIMG}</td>
				
			</tr>

		</c:forEach>
</body>
</html>