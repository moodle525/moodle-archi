<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width,target-densitydpi=medium-dpi,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>用户简单操作</title>
<%@ include file="/page/common/common.jsp"%>
<script type="text/javascript">
	

	$(function() {
		$("#addUserInfo").hide();
		$("#addNew").hide();

		$("#beginSearch").bind(
				"click",
				function() {
					$.ajax({
						type : 'POST',
						url : _base + '/user/search',
						dataType : 'json',
						data : {
							key : $("#searchInfo").val()
						},
						success : function(data) {
							$("#searchResult").show();
							var content = "";
							var counter = 0;
							$.each(data.data, function(i) {
								counter++;
								content += this.name + " ";
							});
							$("#searchResult").html(
									"共" + counter + "个数据：" + content + "<br>");
						}
					});
				});
		
		$("#crossDomainButton").bind("click",function(){
			$.ajax({
				type:"POST",
				url:"http://www.baidu.com",
				dataType:"html",
				crossDomain: true,
				success:function(data){
					$("crossDomain").html(data);
				}
			});
		});
	});

	function addUser(btn) {
		$("#addUserInfo").show();
		$(btn).hide();
		$("#addNew").show();
	}

	function submit(user) {
		var userName = $("#userName").val();
		var password = $("#password").val();
		if (userName == "" || password == "") {
			alert("输点内容呗");
			return;
		}
		$("#userForm").submit();
	}
</script>
</head>
<body>
	<h3>我的页面水平很渣渣，效果做的很差</h3>
	<div align="center">
		<table style="BORDER-COLLAPSE: collapse" borderColor=#000000 height=40
			cellPadding=1 width=500 align=center border=1" >
			<tr>
				<td>id</td>
				<td>名称</td>
				<td>密码</td>
				<td>创建时间</td>
				<td>更新时间</td>
			</tr>
			<c:forEach var="user" items="${userList}">
				<tr>
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>${user.password}</td>
					<td>${user.createTime}</td>
					<td><fmt:formatDate value="${user.updateTime}" type="both"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</c:forEach>
		</table>
		<div id="addUserInfo">
			<form id="userForm" method="post" action="${_base}/user/addUser">
				名称：<input type="text" id="userName" name="userName" /><br> 密码：<input
					type="text" id="password" name="password" />
			</form>
		</div>
		<button type="button" onclick="addUser(this)">新增一个</button>
		<button id="addNew" type="button" onclick="submit(this)">新增</button>
		<br> <input type="text" id="searchInfo">---<input
			type="button" id="beginSearch" value="搜个关键字">
	</div>
	<div id="searchResult" style="display: none" align="center"></div>
	<input
			type="button" id="crossDomainButton" value="跨域">
	<div id="crossDomain" align="center"></div>
</body>
</html>