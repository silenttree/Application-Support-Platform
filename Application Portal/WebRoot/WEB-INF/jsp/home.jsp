<%@page import="com.asc.commons.organization.entity.User"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%
	String appTitle = "平台应用门户";
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
		<!-- 样式文件 -->
		<link rel="stylesheet" type="text/css" href="dependencies/ext-4.2.1/resources/css/ext-all.css">
		<link rel="stylesheet" type="text/css" href="resources/ux/window/Notification.css">	
		<link rel="stylesheet" type="text/css" href="resources/ux/portal/portal.css">
		<link rel="stylesheet" type="text/css" href="resources/ux/statusbar/css/statusbar.css">
		<link rel="stylesheet" type="text/css" href="runtime/css/asc.icon.css">
		<link rel="stylesheet" type="text/css" href="resources/css/desktop.css">
		<!-- Extjs4 -->
		<script type="text/javascript" src="dependencies/ext-4.2.1/ext-all-dev.js"></script>
		<script type="text/javascript" src="dependencies/ext-4.2.1/locale/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="runtime/api-debug.js"></script>
		<!-- 流程图组件 开始 -->
		<script type="text/javascript">
			mxBasePath = 'dependencies/mxgraph/src';
		</script>
		<!-- Loads and initiaizes the library -->
		<script type="text/javascript" src="dependencies/mxgraph/mxclient.min.js"></script>
		<!-- 文件上传组件 -->
		<script type="text/javascript" src="dependencies/plupload-2.1.2/js/moxie.js"></script>
		<script type="text/javascript" src="dependencies/plupload-2.1.2/js/plupload.dev.js"></script>
		<!-- 门户界面引擎 -->
		<script type="text/javascript" src="app/home.js"></script>
		<!-- 图形界面组件 -->
		<!-- 
		<script type="text/javascript" src="dependencies/echarts-2.1.8/build/source/echarts-all.js"></script>
		 -->
		 
		<!-- 富文本编辑器组建 -->
		<link rel="stylesheet" href="dependencies/kindeditor/themes/default/default.css" />
		<link rel="stylesheet" href="dependencies/kindeditor/plugins/code/prettify.css" />
		<script charset="utf-8" src="dependencies/kindeditor/kindeditor.js"></script>
		<script charset="utf-8" src="dependencies/kindeditor/lang/zh_CN.js"></script>
		<script charset="utf-8" src="dependencies/kindeditor/plugins/code/prettify.js"></script>
		<script type="text/javascript" src="resources/kindeditor/My97DatePicker/WdatePicker.js"></script>
	</head>
</html>