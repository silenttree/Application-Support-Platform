<%@page contentType="text/html;charset=UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="resources/ux/icardpay/RSA.js"></script>
		<script type="text/javascript" src="resources/ux/icardpay/BigInt.js"></script>
		<script type="text/javascript" src="resources/ux/icardpay/Barrett.js"></script>
		<script type="text/javascript" src="resources/ux/icardpay/RsaData.js"></script>
<script language=javascript>
function getRsaPassword(){
	var result = {};
	RSAinit();
	var pwd = document.getElementById('pwd').value.trim();;
	if(pwd.trim() == ''){
		result.successed = false;
		result.message = '支付密码为空！';
	}else{
		result.successed = true;
		result.message = encryptedString(key, encodeURIComponent(pwd));
	}
	return result;
}
</script>
	</head>
	<body>
		支付密码: <input type=password id=pwd>
	</body>
</html>