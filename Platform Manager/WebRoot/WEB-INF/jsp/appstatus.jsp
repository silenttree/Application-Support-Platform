<%@page import="com.asc.framework.app.ApplicationRunMode"%>
<%@page import="com.asc.framework.app.ApplicationController"%>
<%@page import="com.asc.framework.app.AppConfiguration"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@page session="false"%>
<%
	String appId = AppConfiguration.instance().getAppId();
	String appTitle = AppConfiguration.instance().getAppTitle();
	String message = (String)request.getAttribute("message");
%>
<html>
	<head>
	<style type="text/css">
		*{
		font-size:13px;
		}
		h3{
		font-size:20px;
		}
	</style>
	<script type="text/javascript">
		function start() {
			appForm.action.value = "start";
			appForm.submit();
		}
		function stop() {
			appForm.action.value = "stop";
			appForm.submit();
		}
		function suspend() {
			appForm.action.value = "suspend";
			appForm.submit();
		}
		function resume() {
			appForm.action.value = "resume";
			appForm.submit();
		}
	</script>
	</head>
	<body>
		<h3><%=appTitle%>(<%=appId%>)</h3>
		<hr>
		<p>
			Application Environment:<br>
			OS<br>
			Jdk Home<br>
			Jvm Version<br>
			<br>
			Application Status: <%=ApplicationController.instance().getAppStatus().asChineseText()%><br>
			totalMemory: <%= Runtime.getRuntime().totalMemory()%><br>
			freeMemory: <%= Runtime.getRuntime().freeMemory()%><br>
			maxMemory: <%= Runtime.getRuntime().maxMemory()%><br>
		</p>
		<%
		if (AppConfiguration.instance().getAppMode().equals(ApplicationRunMode.Debug)) {
		%>
		<form id="appForm">
			<input type="button" value="启动" onclick="javascript:start()">&nbsp;
			<input type="button" value="停止" onclick="javascript:stop()">&nbsp;
			<input type="button" value="暂停" onclick="javascript:suspend()">&nbsp;
			<input type="button" value="恢复" onclick="javascript:resume()">
			<input type="hidden" name="action" value="">
		</form>
		<%
		if (message != null) {
		%>
		<p style="color:red"><%=message%></p>
		<%
		}
		%>
		<%
		}
		%>
	</body>
</html>