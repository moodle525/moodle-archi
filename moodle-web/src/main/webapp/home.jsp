<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<title>page</title>
<%@ include file="/page/common/common.jsp"%>
<%
	request.getRequestDispatcher("/index").forward(request, response);
%>
</head>
<body>
	<h2></h2>
</body>
</html>


