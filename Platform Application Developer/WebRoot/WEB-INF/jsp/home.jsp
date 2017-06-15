<%@page import="com.asc.commons.organization.entity.User"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String appTitle = "应用模块快速开发工具";
	String userCaption = "";
	User user = (User)request.getAttribute("user");
	if (user != null) {
		userCaption = user.getF_caption();
	}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title><%=appTitle%></title>
		<link rel="shortcut icon" href="resources/icon/logo.png"/>
		<link rel="stylesheet" type="text/css" href="dependencies/ext-4.2.1/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css" href="resources/ux/window/Notification.css">	
		<link rel="stylesheet" type="text/css" href="runtime/css/asc.icon.css">
		<link rel="stylesheet" type="text/css" href="resources/css/desktop.css">
			<!-- Sets the basepath for the library if not in same directory -->
		<script type="text/javascript">
			mxBasePath = 'dependencies/mxgraph/src';
		</script>

		<!-- Loads and initiaizes the library -->
		<script type="text/javascript" src="dependencies/mxgraph/mxclient.min.js"></script>
		<script type="text/javascript" src="dependencies/ext-4.2.1/ext-all-debug.js"></script>
		<script type="text/javascript" src="dependencies/ext-4.2.1/locale/ext-lang-zh_CN.js"></script>
		<!-- ace -->
		<script type="text/javascript" src="dependencies/ace/ace.js"></script>
		<script type="text/javascript" src="dependencies/ace/ext-language_tools.js"></script>
		<!-- 平台js -->
		<script type="text/javascript" src="runtime/api.js"></script>
		<script type="text/javascript" src="app/home.js"></script>
		<!-- ace加载语言 -->
		<script type="text/javascript">
			ace.require("ace/ext/language_tools");
		</script>
		<!-- 改变滚动条样式 -->
		<style type="text/css" media="screen">
		    #editor { 
		        position: absolute;
		        top: 0;
		        right: 0;
		        bottom: 0;
		        left: 0;
		    }
		    ::-webkit-scrollbar{width:5px;}
			::-webkit-scrollbar-track{display:none;}/* background-color:#bee1eb;外层轨道。可以用display:none让其不显示，也可以添加背景图片，颜色改变显示效果。 */
			::-webkit-scrollbar-thumb{background-color:#E2E2E2;}
			::-webkit-scrollbar-thumb:hover {background-color:#A3A3A3}
			::-webkit-scrollbar-thumb:active {background-color:#A3A3A3}
		</style>
	</head>
</html>