<%@page contentType="text/html;charset=UTF-8"%>
<%
	String appTitle = "";
	String message = "";
	Object obj = request.getAttribute("message");
	if (obj != null) {
		message = String.valueOf(obj);
	}
	
	String ru = "";
	obj = request.getParameter("ru"); 
	if (obj != null) {
		ru = (String)obj;
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="shortcut icon" type="image/ico"
	href="resources/login/favicon.ico">
<title>登录页面 - <%=appTitle%></title>
<link href="resources/login/styles.css" type="text/css"
	media="screen" rel="stylesheet">
<script type="text/javascript" src="app/jm.js"></script>
<style type="text/css">
img,div {
	behavior: url(iepngfix.htc)
}
</style>
<script type="text/javascript">
/* 	$(document).ready(function () {
		$("#validCodeImg a").on("click",function(){
			changeImage();
		});
	}); */
	window.onload = function () {
		document.getElementById("imc_verfication_code").onclick = function () {
			changeImage();
		}
	}
	function changeImage(){
		document.getElementById("imc_verfication_code").src="resources/VerificationCode?ts=" + new Date().getTime();
	}
	document.onkeydown = function(event_e){ 
	   if(window.event) { 
	 	     event_e = window.event;
	 	  }            
	 	  var int_keycode = event_e.charCode||event_e.keyCode;          
	 	  if( int_keycode == '13' ) {              
	 			 doLogin();              
	 	      return false;
	 	  }      
	}
	function doLogin() {
		if (validate()) {
			var pwd = document.getElementById("p").value;
			document.getElementById('p').value = encode(pwd);
			loginForm.submit();
		}
	}
	function validate() {
		if (loginForm.u.value == '') {
			return false;
		}
		if (loginForm.p.value == '') {
			return false;
		}
		return true;
	}
	function enterPress(e){
		var e = e||window.event;
		if(e.keyCode==13){
			doLogin();
		}
	}
</script>
</head>
<body id="login">
	<div id="wrappertop"></div>
	<div id="wrapper">
		<div id="content">
			<div id="header">
				<a href=""><img src="resources/login/logo.png" alt="CRM ASC 应用支撑平台"></a>
			</div>
			<div id="darkbanner" class="banner320">
				<h2 style="font-weight:normal;font-family:微软雅黑;">用户登录</h2>
			</div>
			<div id="darkbannerwrap"></div>
			<form name="loginForm" method="post" action="login">
				<fieldset class="form">
					<table>
						<tr>
							<td style="padding-left:30px">
								<label for="user_name">用 户 名 ： </label>
							</td>
							<td>
								<input name="u" id="u" type="text">
							</td>
						</tr>
						<tr>
							<td style="padding-left:30px">
								<label for="user_password">密 　 码 ：</label>
							</td>
							<td>
								<input name="p" id="p" type="password">
							</td>
						</tr>
						<tr>
							<td style="padding-left:30px">
								<label for="user_password">验证码 ：</label>
							</td>
							<td>
								<div>
									<input name="v" id="v" value='' type="text" style="width:144px;">
									<img src='resources/VerificationCode' onclick="changeImage();" id="imc_verfication_code" style="vertical-align: middle;"/>
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div><font color="red">${message}</font></div>
							</td>
						</tr>
						<tr>
							<td style="padding-left:30px;padding-top:15px;padding-bottom:10px" colspan=2>
								<button class="positive" onclick="doLogin();">
									<img src="resources/login/key.png" alt="Announcement">登 录
								</button>
								<ul id="forgottenpassword">
									<li class="boldtext">|</li>
									<li><a href="#">忘记密码?</a></li>
								</ul>
							</td>
						</tr>
					</table>
				</fieldset>
			</form>
		</div>
	</div>

	<div id="wrapperbottom_branding">
		<div id="wrapperbottom_branding_text">
			<a href="#" style="text-decoration: none"> </a>.
		</div>
	</div>
</body>
</html>