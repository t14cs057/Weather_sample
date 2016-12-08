

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
	// アカウントに重複がないかの判断
	String mailId = String.valueOf(session.getAttribute("mailId"));
%>
<head>
		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		<title>詳細画面</title>
		
	</head>
	<body>

	<div id="container">
		<div id="header">
			<h1>ビニールハウス倒壊予測管理システム</h1>
		</div>

		<div id="content">
			<% 
			int num = 1;
			//このJSPで用いる各変数の初期化
			PersistenceManager pm = null;
			try {
			pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(House.class);
			query.setFilter("mailId == " + "'" + mailId + "'");
			query.setOrdering("date asc");
			List<House> houses = (List<House>) query.execute();
			// ハウスの中身を表示する
  			for (House house: houses) {
  			%>
  			<div id="info">
	  		<font> ハウス<%=num%></font>
	  		 </div>
  			<div class="frame bottom">
  			<div> 郵便番号 :  <%= house.getZip1() %> - <%= house.getZip2() %> </div>
  			<div> 住所 :  <%= house.getAddress() %> </div>
  			<div> ハウスの種類 :  <%= house.getPillar() %> </div>
  			<div> ハウスの経過年数 :  <%= house.getPillarNum() %> </div>
  			<div> ビニールの種類 :  <%= house.getVin() %> </div>
  			<div> ビニールの経過年数 :  <%= house.getVinNum() %> </div>
  			
  			</div>

  			<%
  			num++;
  			}
  			} finally {
  				if (pm != null && !pm.isClosed())
  					pm.close();				
  				}
  			%>
		<input class="detail" type="button" value="詳細画面を閉じる" onClick="window.close()">
		</div>
		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved.</address>
		</div>
	</div>

</body>
</html>