<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<title>登录页</title>
<meta content="text/html;charset=utf-8" http-equiv="content-type">
</head>
<body>
1111111111111111111111+++ ${message}+++++++++++55555555555555555555555555
   hello ${jb1.foo } + ${jb2.fruit } + ${jb3.fruit } 
</body>
</html>