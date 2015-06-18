<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring 4 MVC - HelloWorld Index Page</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/angularjs/angular.min.js"> </script>
</head>
<body> 
	<center>
		<form method="post" action="createUser">
		<a href="hello?page=message">back</a></br>
		<div ng-app="">
			<div class="alert-box">
			${message}
			</div>
		</div>
		User Create
		<br><br>
		First Name:
		<input type="text" name="firstName" /><br><br>
		Last Name:
		<input type="text" name="lastName" /><br><br> 
		User Name:
		<input type="text" name="userName" /><br><br>
		Password:
		<input type="password" name="password" /><br><br>
		
		<input class="button tiny round" type="submit" value="submit"/>
		</form>
	</center>
</body>
</html>