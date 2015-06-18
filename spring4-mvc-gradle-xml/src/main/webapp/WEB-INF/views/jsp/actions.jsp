<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
 
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="app">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Spring 4 MVC</title>
	<link rel="stylesheet" href="css/jquery-ui.css">
	<link rel="stylesheet" href="css/style.css" />
	
	<script src="js/angular/ui-grid-unstable.js"></script>
    <script src="cs/angular/ui-grid-unstable.css"></script>
	 
	<script src="js/jquery-1.11.1.js"></script>
	<script src="js/jquery-ui.js"></script>
	
	<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-touch.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.2.26/angular-animate.js"></script>
    <script src="http://ui-grid.info/docs/grunt-scripts/vfs_fonts.js"></script>
    <script src="js/angular/ui-grid-stable.js"></script>
    <link rel="stylesheet" href="css/angular/ui-grid-stable.css" type="text/css">
    <style type="text/css">
		.grid {
		  width: 800px;
		  height: 250px;
		}
	</style>
	
	<script>
		$(function() {		  
		  $( "#tabs" ).tabs({active: $('#currentTab').val()});		
		});
		
		var app = angular.module('app', ['ngTouch', 'ui.grid']);
		 
		app.controller('MainCtrl', ['$scope', function ($scope) {
		 
			
			
		  $scope.myDataUser = ${listUser};
		  $scope.myDataDomain = ${listDomain};
		}]);
		
		var RestPost = function() {
			
			 $.ajax({  
			     type : "Get",   
			     url : "/spring4-mvc-gradle-xml/hello",   
			     data : "page=createDomain",  
			     success : function(response) { 
			      
			     },  
			     error : function(e) {  
			      alert('Error: ' + e);   
			     }  
			    });
			}
	</script>
	</head>
	<body>
		<div class="wrapper">
	        <div class="container">
	        <h4> Brain-Honey </h4>
	        <input type="hidden" name="currentTab" id="currentTab" value="${tabIndex}" />
	            <div id="tabs">
	                <ul>               
	                    <li><a href="#domain">Domain</a></li>
	                    <li><a href="#user">User</a></li>
	                </ul>                
	                <div id="domain">
	                	<h4> ${message} </h4>                    
	                    <form method="post" action="createDomain">
	                        <label for="email">Reference id:</label> <br /> 
	                        <input type="text" name="reference" /> <br />
	                        <label for="email">Domain Name:</label> <br /> 
	                        <input type="text" name="domainName" /> <br />
	                        <label for="password">User space:</label> <br /> 
	                        <input type="text" name="userspace" /> <br />
	                        <br /> <input type="submit" value="Submit">
	                    </form>
	                    <div ng-controller="MainCtrl">
	                    	<i class="fa fa-spinner fa-spin text-center" ng-show="loading"></i>
					      <div id="grid2" ui-grid="{ data: myDataDomain }" class="grid"></div>
					    </div>
	                </div>
	                <div id="user">
	                    <h4> ${message} </h4>                    
	                    <form method="post" action="createUser">
	                        <label for="email">First Name:</label> <br /> 
	                        <input type="text" name="firstName" /> <br />
	                        <label for="email">Last Name:</label> <br /> 
	                        <input type="text" name="lastName" /> <br />
	                        <label for="password">User Name:</label> <br /> 
	                        <input type="text" name="userName" /> <br />
	                        <label for="password">Password:</label> <br /> 
	                        <input type="password" name="password" /> <br />
	                        <br /> <input type="submit" value="Submit">
	                    </form>
	                    <div ng-controller="MainCtrl">
					      <div id="grid1" ui-grid="{ data: myDataUser }" class="grid"></div>
					    </div>            
	                </div>
	            </div>
	        </div>
	    </div>
	</body>
</html>