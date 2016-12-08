

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<% //自分たちで定義した package をimport %>
<%@ page import="java.util.*"%>  
<%@ page import="javax.jdo.*"%>  
<%@ page import="javax.servlet.*"%>  
<%@ page import="com.Login.*"%>  
<%@ page import="com.Data.*"%>  

<html>
<%
	int judge = 0, mailFlag = 0;

	// アカウントに重複がないかの判断
	String str = request.getParameter("Judge");
	// メールアドレスが送信出来たかどうかの判断
	String str2 = request.getParameter("mailFlag");
	if (str != null) {
		judge = Integer.parseInt(str);
	}
	if (str2 != null)
		mailFlag = Integer.parseInt(str2);
%>
<head>
		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		<title>確認画面</title>
		
	</head>
	<body>

	<div id="container">
		<div id="header">
			<h1>ビニールハウス倒壊予測管理システム</h1>
		</div>

		<div id="content">
			<%	if((judge & 1) == 1){ %>
			<label>そのメールアドレスはすでに使用されています．</label>

			<div>
				<a href="/init.jsp">新規登録画面へ</a>
			</div>
			<%	}else if((judge & 2) == 2){  %>
			<label>アカウントを登録しました．</label>
			<div>
				<a href="/login.jsp">ログイン画面へ</a>
			</div>
			<% }else if((judge & 4) == 4){%>
			<label>ログインしてください．</label>
			<div>
				<a href="/login.jsp">ログイン画面へ</a>
			</div>
			<%	} 
			if(( mailFlag & 2) == 2 ) { %>
			<label>不正なメールアドレスです．</label>
			<div>
				<a href="/init.jsp">新規登録画面へ</a>
			</div>
			<%} else if((mailFlag & 4) == 4 ) {%>
			<p> メールを送信できませんでした．</p>
			<label> メールアドレスを確認後再度新規登録をしてください</label>
			<div>
				<a href="/init.jsp">新規登録画面へ</a>
			</div>
			<%} %>

		</div>
		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved.</address>
		</div>
	</div>

</body>
</html>