<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring 4 MVC - HelloWorld Index Page</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/angularjs/angular.min.js"></script>
</head>
<body> 
	<center>
		<form method="post" action="createDomain">
		<a href="hello?page=message">back</a></br>
		<div ng-app="">
			<div class="alert-box">
			${message}
			</div>
		</div>
		Domain Create
		<br><br>
		Reference id:
		<input type="text" name="reference" /><br><br>
		Domain Name:
		<input type="text" name="domainName" /><br><br> 
		User space:
		<input type="text" name="userspace" /><br><br>
		
		<input type="submit" value="submit"/>
		</form>
	</center>
</body>
</html>