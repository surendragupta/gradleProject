<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="app">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Spring 4 MVC</title>
	<!-- <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/foundation/5.5.2/css/foundation.min.css"> -->
	<!-- <link rel="stylesheet" href="resources/mytheme/css/foundation-min.css" type="text/css"> -->
	<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/resources/angularjs/angular.min.js"></script> --%>
	<link rel="stylesheet" href="css/jquery-ui.css">
	<link rel="stylesheet" href="css/style.css">
	 
	<script src="js/jquery-1.11.1.js"></script>
	<script src="js/jquery-ui.js"></script>
	
	
	<script>
		$(function() {		  
			$( "#tabs" ).tabs();	
		});
	</script>
</head>
<body>
	<div class="wrapper">
        <div class="container">
        <h4> Brain-Honey </h4>  
            <div id="tabs">
                <ul>
                    <li><a href="#login">Login</a></li>                   
                </ul>
                <div id="login">                	
                    <h4> ${message} </h4>                    
                    <form method="post" action="login">
                        <label for="email">Prefix Name:</label> <br /> 
                        <input type="text" name="prefixName" id="prefixName" /> <br />
                        <label for="email">User Name:</label> <br /> 
                        <input type="text" name="userName" id="userName" /> <br />
                        <label for="password">Password:</label> <br /> 
                        <input type="password" name="password" id="password" /> <br />
                        <br /> <input type="submit" value="Login">
                    </form>                    
                </div>
            </div>
        </div>
    </div>
</body>
</html>