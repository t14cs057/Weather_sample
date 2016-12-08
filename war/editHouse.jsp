<!-- ハウス情報の編集を行なう画面 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.Date,java.text.SimpleDateFormat"%>
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

<html>
		<%
	     // ログインをしているかの判定
	     if( session.getAttribute("access") == null ){
		  	   response.sendRedirect("./error.jsp?Judge=4");
		   }
              
			// 現在ログインしているアカウントのメールアドレス 
		   String mailId = String.valueOf(session.getAttribute("mailId"));
			// ハウスの数，ハウスの番号
 			int size = 0, num = 0;
 			String houseId = null;

 			PersistenceManager pm = null;
  			try {
  			pm = PMF.get().getPersistenceManager();
  			Query query = pm.newQuery(House.class);
  			query.setOrdering("date asc");
	  		query.setFilter("mailId == id");
 			query.declareParameters("String id");
  			
  			List<House> houses = (List<House>) query.execute(mailId);
  			
  			size = houses.size();
  			
  			if( size == 0 ){
  				pm.close();
  	  	   		response.sendRedirect("./registerHouse.jsp");  				
  			}		
 		%>
	
	<head>
		<!-- 郵便番号 -> 住所の変換に使用 -->
		<script src="http://ajaxzip3.googlecode.com/svn/trunk/ajaxzip3/ajaxzip3.js" charset="UTF-8"></script>
	
		<!-- スタイルシートの読み込み -->
		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css" />
		<script type="text/javascript" src="./javascript/change.js"></script>
		
		<title>ハウス情報の変更</title>
	</head>
	
	<body>
	
	<div id="container">
		<div id="header">
		<h1>ビニールハウス倒壊予測管理システム</h1>
		こんにちは　<font color="#ffffff"><%=mailId%></font>　さん
       <a href="/logout">ログアウト</a> 
		</div>
		
		<div id="content">
		<h2>ハウス情報変更画面</h2>
		<div id="leftAlign">
		<% 
			for (House house: houses) { 
				houseId = house.getId();
		%>
		
		<form name="f" action="/del_house" method="post">
			<!-- JavaScriptで使用 -->
			<input type="hidden" name="houseId<%=num%>" value="<%=houseId%>" />
			<p>----------------------------------------------------------------</p>
			<h3 class="color1">
				ハウス<%=num+1%>
				<!-- 削除ボタンの表示 -->
				<!-- Submitでなければ定義できず... -->
				<input class="lmargin" type="submit" value="削除" name="num<%=num%>" />
			</h3>
			<p class="style">
				郵便番号 : 
				<!-- AjaxZip3.zip2addr( '〒上3桁', '〒下4桁', '都道府県', '市区町村', '町域大字', '丁目番地' );  -->
				<!--input type="text" name="zip01" size="10" maxlength="8" onkeyup="AjaxZip3.zip2addr(this,'','addr','addr');"-->
				<input class="left" type="text" name="zipCode1<%=num%>" size="4" maxlength="3" value="<%=house.getZip1()%>" autocomplete="off" / > 
				- <input class="left" type="text" name="zipCode2<%=num%>" size="5" maxlength="4" value="<%=house.getZip2()%>"
					onKeyUp="AjaxZip3.zip2addr('zipCode1<%=num%>','zipCode2<%=num%>','addr<%=num%>','addr<%=num%>');" autocomplete="off" />
			</p>
			<p class="style">
				住所 : <input class="left" type="text" name="addr<%=num%>" size="30" value="<%=house.getAddress()%>" />
			</p>
			<p class="style">
				使用している柱 : <select name="pillar<%=num%>">
					<% 
	      				String pillar = house.getPillar();
					   if(pillar.equals("鉄")) { %>
					<option value="pillar1" selected>鉄</option>
					<% }else{ %>
					<option value="pillar1">鉄</option>
					<% } %>
					<% if(pillar.equals("木")) { %>
					<option value="pillar2" selected>木</option>
					<% }else{ %>
					<option value="pillar2">木</option>
					<% } %>
					<% if(pillar.equals("その他")) { %>
					<option value="pillar3" selected>その他</option>
					<% }else{ %>
					<option value="pillar3">その他</option>
					<% } %>
				</select>
			</p>
	
			<p class="style">
				柱を建ててからの経過年数 :
				 <select name="pillarNum<%=num%>">
					<%
	  	  			int pillarNum = house.getPillarNum();
	  	  			for( int i = 0; i <= 15; i++ ){
						if( pillarNum == i ) {%>
					<option value="<%=i%>" selected><%= i %></option>
					<% }else {%>
					<option value="<%=i%>"><%=i%></option>
					<% 
						}
	  	  			}	%>
				</select>
			</p>
	
			<p class="style">
				ビニールハウスの種類 : 
				<select name="vin<%=num%>">
					<% 
					String vin = house.getVin();
					if(vin.equals("ポリオレフィン系フィルム")) { %>
					<option value="vin1" selected>ポリオレフィン系フィルム</option>
					<% } else {%>
					<option value="vin1">ポリオレフィン系フィルム</option>
					<% } %>
					<% if(vin.equals("その他")) { %>
					<option value="vin2" selected>その他</option>
					<% }else{ %>
					<option value="vin2">その他</option>
					<% } %>
				</select>
			</p>
	
			<p class="style">
				ビニールの経過年数 : 
				<select name="vinNum<%=num%>">
					<%
	  	  			int vinNum = house.getVinNum();
	  	  			for( int i = 0; i <= 15; i++ ){
						if( vinNum == i ) {%>
					<option value="<%=i%>" selected><%= i %></option>
					<% }else {%>
					<option value="<%=i%>"><%=i%></option>
					<% 
						}
	  	  			}
	  	  		%>
				</select>
			</p>
			<p>----------------------------------------------------------------</p>
	
			<% 	num++;
	  	 			}
	  			} finally {
	  				if (pm != null && !pm.isClosed())
	  					pm.close();				
	  				}
	  			%>
			<!-- JavaScriptで使用 -->
			<input type="hidden" name="Housesize" value="<%=size%>" />
			<!-- 編集による変更を適用 -->
			<p>
				<input class="bsize center" type="button" onClick="change(this.form)" value="変更" />
			</p>
		</form>
	</div>
		<!-- ハウスの編集を行なう -->
		<form action="/edit_house" name="form1" method="post">
			<% for( int i = 0; i < size; i++){ %>
			<input type="hidden" name="zipCode1<%=i%>" /> 
			<input type="hidden" name="zipCode2<%=i%>" /> 
			<input type="hidden" name="addr<%=i%>" />
			<input type="hidden" name="pillar<%=i%>" /> 
			<input type="hidden" name="pillarNum<%=i%>" /> 
			<input type="hidden" name="vin<%=i%>" />
			<input type="hidden" name="vinNum<%=i%>" />
			<% } %>
			<input type="hidden" name="mailId" value="<%=mailId%>" />
		</form>
	
		<!-- ハウスの削除を行なう -->
		<form action="/del_house" name="form2" method="post">
		<% for( int i = 0; i < size; i++){ %>
			<input type="hidden" name="houseId<%=i%>" /> 
			<% } %>
			<input type="hidden" name="num" /> 
			<input type="hidden" name="mailId" value="<%=mailId%>" />
			<input type="hidden" name="size" value="<%=size%>" />		
		</form>
	
			<form action="/HouseInfo.jsp" >
				<input type="submit" value="ハウス情報へ" />
			</form>
		
		</div>
		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved. </address>
		</div> 
		
		</div>  
	
	</body>
</html>