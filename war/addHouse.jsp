<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Date,java.text.SimpleDateFormat" %>
<%@ page import="java.util.List"%>  
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="javax.jdo.PersistenceManager"%>  
<%@ page import="javax.jdo.Query"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.Login.*"%>  
<%@ page import="com.Data.*"%>  
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- ハウスを登録後さらに登録するハウスの数を増やすJSP -->
 <html>         
       <%
	       // ログインをしているかの判定
	   	  if( session.getAttribute("access") == null ){
		  	   response.sendRedirect("./error.jsp?Judge=4");
		   }

       // 現在ログインしているアカウントのメールアドレス
       String mailId = String.valueOf(session.getAttribute("mailId"));
 
		
		PersistenceManager pm = null;
		try {
		pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(House.class);
		query.setOrdering("date asc");
		query.setFilter("mailId == " + "'" + mailId + "'");
		List<House> houses = (List<House>) query.execute();
		int housesize = houses.size();
		
		if( housesize == 0 ){
			pm.close();
 	   		response.sendRedirect("./registerHouse.jsp");  				
		}	
		}finally{
			pm.close();
		}
		
       // エラー内容，現在登録されているハウスの数，ハウスの数
       int error = 0, size = 1, count = 1;
       // 登録できるハウスの数
       final int MAX_HOUSE = 10;
       // 登録しようとしているハウスの数
       String str = request.getParameter("Count");
       // 現在登録されているハウスの数
		String s = request.getParameter("Size");

		if(s != null){
			size = Integer.parseInt(s); // 現在のハウスの個数を得る
		}
		
		if(str != null){
			count = Integer.parseInt(str); // ハウスの個数を得る			
		}
		%>
    	<head>
    	<!-- 郵便番号 => 住所の変換に使用 -->
       <script src="http://ajaxzip3.googlecode.com/svn/trunk/ajaxzip3/ajaxzip3.js" charset="UTF-8"></script>
  		
  		<!-- スタイルシートの読み込み -->
  		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		
		<title>ハウス情報追加登録画面</title>
       </head>
            		  
      <body>

	<div id="container">
		<div id="header">
			<h1>ビニールハウス倒壊予測管理システム</h1>
			こんにちは <font color="#ffffff"><%=mailId%></font> さん <a href="/logout">ログアウト</a>
		</div>

		<div id="content">
			<h2>ハウス情報追加登録画面</h2>
			<div id="leftAlign">
			<!-- ハウスの情報を登録する -->
			<form action="new_house" method="post">

				<% for(int i = size; i < size+count; i++){%>
				<p>----------------------------------------------------------------</p>

				<h2 class="color">
					ハウス<%=i+1%></h2>
				<p class="style">
					郵便番号 : 
					<!-- AjaxZip3.zip2addr( '〒上3桁', '〒下4桁', '都道府県', '市区町村', '町域大字', '丁目番地' );  -->
					<input class="left" type="text" name="zipCode1<%=i%>" size="4"
						maxlength="3" /> - <input class="left" type="text"
						name="zipCode2<%=i%>" size="5" maxlength="4"
						onKeyUp="AjaxZip3.zip2addr('zipCode1<%=i%>','zipCode2<%=i%>','addr<%=i%>','addr<%=i%>');" />
				</p>

				<p class="style">
					住所 : <input class="left" type="text" name="addr<%=i%>" size="30" />
				<p class="style">
					使用している柱 : <select name="pillar<%=i%>">
						<option value="">選択してください</option>
						<option value="pillar1">鉄</option>
						<option value="pillar2">木</option>
						<option value="pillar3">その他</option>
					</select>
				<p class="style">
					柱を建ててからの経過年数 : <select name="pillarNum<%=i%>">
						<option value="">選択してください</option>
						<%for(int j = 0; j <= 15; j++ ){ %>
						<option value="<%=j%>"><%=j%></option>
						<%} %>
					</select>
				<p class="style">
					ビニールハウスの種類 : <select name="vin<%=i%>">
						<option value="">選択してください</option>
						<option value="vin1">ポリオレフィン系フィルム</option>
						<option value="vin2">その他</option>
					</select>
				<p class="style">
					ビニールを貼ってからの経過年数 : <select name="vinNum<%=i%>">
						<option value="">選択してください</option>
						<%for(int j = 0; j <= 15; j ++ ){ %>
						<option value="<%=j%>"><%=j%></option>
						<%} %>
					</select>
				<p>----------------------------------------------------------------</p>
				<%} %>
				<!-- hidden属性でハウスの個数とメールIDを送る -->
				<input type="hidden" name="count" value="<%=count%>" /> <input
					type="hidden" name="mailId" value="<%=mailId%>" /> <input
					type="hidden" name="size" value="<%=size%>" />

				<h2>連絡先の追加</h2>
				<p class="style">
					メールアドレスの追加 <input class="left" type="text" name="addMail" size="40" />
				</p>

				<p>
					<input class="bsize center" type="submit" value="登録" />
				</p>
			</form>
			</div>
			<!-- 登録するハウスを増やす -->
			<form action="add_house" method="post">
				<input type="hidden" name="size" value="<%=size%>" /> <input
					type="hidden" name="count" value="<%=count%>" /> <input
					type="hidden" name="mailId" value="<%=mailId%>" /> <input
					type="hidden" name="add" value="add" />
				<p>
					<input class="bsize" type="submit" value="ビニールハウスの追加" /> 
					<font class="rcolor">※あと<%=MAX_HOUSE - count - size%>個まで追加できます</font>
				</p>
			</form>

			<form action="/HouseInfo.jsp" >
				<input type="submit" value="ハウス情報へ" />
			</form>

		</div>
		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved.</address>
		</div>

	</div>
   </body>
</html>
