<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>  
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<title>登录页test BM</title>
<meta content="text/html;charset=utf-8" http-equiv="content-type">
</head>
<body>
 <form action="/my/jx1" method=post>
<input type=text name="mapVo['x']"><br/>
<!-- <input type=text name="mapVo['x'].fruit"><br/> -->
<input type=text name="mapVo['y']"><br/>
<!-- <input type=text name="mapVo['y'].fruit"><br/> -->
<input type="submit"/> 
</form>
</body>
</html>