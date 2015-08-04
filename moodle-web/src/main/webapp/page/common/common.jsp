<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%
	String _base = request.getContextPath();
	request.setAttribute("_base", _base);
	
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	response.setHeader("Pragma", "No-cache");
%>

<link href="${_base }/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="${_base }/resources/css/css.css" rel="stylesheet">
  
<script src="${_base }/components/jquery/dist/jquery.min.js"></script>
<script src="${_base }/components/jquery/dist/jquery.flexslider-min.js"></script>
<%-- <script  src="${_base }/components/nav.js"></script>
<script src="${_base }/components/list.js"></script>
<script src="${_base }/components/table-js.js"></script>
<script src="${_base }/components/xialzs.js"></script>
<script src="${_base }/components/top_g.js"></script> --%>
<script src="${_base }/components/bootstrap/dist/js/bootstrap.min.js"></script>
<%-- <script src="${_base }/components/validate/jquery.validate.min.js"></script>
<script src="${_base }/components/validate/additional-methods.min.js"></script>
<script src="${_base}/components/jsview/jsviews.min.js"></script> --%>
<script>
var _base = "${_base}";
</script>