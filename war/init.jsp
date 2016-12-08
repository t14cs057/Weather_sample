<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>  
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="javax.jdo.PersistenceManager"%>  
<%@ page import="javax.jdo.Query"%>
<%@ page import="javax.servlet.http.*"%>
 
<% //自分たちで定義した package をimport %>
<%@ page import="com.Login.*"%>  
<%@ page import="com.Data.*"%>  
<%@ page import="com.System.*"%>  

<html>
			<%	
			int error = 0;
			// Initial_Servlet から
			// エラーがあれば str に何かしらの値が入る
			String str = request.getParameter("Error");
			if(str != null){
				error = Integer.parseInt(str);
			}
			
 	    	PersistenceManager pm = null;
	  		try {
  			pm = PMF.get().getPersistenceManager();
  			Query query = pm.newQuery(Amedas.class);
  			query.setFilter("fuken_name == " + "'甲府'");
  			List<Amedas> amedas = (List<Amedas>) query.execute();
				
			%>

	<head>
		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		<title>新規登録</title>
		
	</head>
	<body>
	<div id="container">
		<div id="header">
		<h1>ビニールハウス倒壊予測管理システム</h1>
		</div>
		
		<div id="content">
		<h2> 新規登録 </h2>
	
			<!-- アカウントの新規登録を行なう -->
			<form action="/init" method="post">
			<table>
				<tr>
					<td>
					<label>メールアドレス :
					<input class="left" type="text" name="mail" id="ID"></label>
					</td>
				</tr>
				<%	if((error & 1) == 1){ %>
				<tr>
					<td>
					<label class="rcolor">※ メールアドレスが未入力です</label>
					</td>
				</tr>
				<%	} %>	
				
				<%	if((error & 8) == 8){ %>
				<tr>
					<td>
					<label class="rcolor">※ 適切なメールアドレスを入力してください</label>
					</td>
				</tr>
				<%	} %>	
				
				<tr>
					<td>
					<label>パスワード(4桁) :  
					<input type="password" name="pass"> </label>
					</td>
				</tr>	
				<%	if((error & 2) == 2){ %>
				<tr>
					<td>
					<label class="rcolor">※ パスワード欄が未入力です</label>
					</td>
				</tr>
				<%	} %>
				<tr>
					<td>
					<label>パスワード(確認用) : 
					<input type="password" name="pass2" placeholder="上記と同じパスワード"></label>
					</td>
				</tr>
				<%	if((error & 4) == 4){ %>
				<tr>
					<td>
					<label class="rcolor">※ パスワード(確認用)欄が未入力です</label>
					</td>
				</tr>
				<%	} %>				
				<%	if((error & 16) == 16){ %>
				<tr>
					<td>
					<label class="rcolor">※ パスワードが一致しません</label>
					</td>
				</tr>
				<%	} %>				
				<%	if((error & 32) == 32){ %>
				<tr>
					<td>
					<label class="rcolor">※ 4桁のパスワードを入力してください</label>
					</td>
				</tr>
				<%	} %>			
				<%	if((error & 64) == 64){ %>
				<tr>
					<td>
					<label class="rcolor">※ パスワードに使用できるのは半角英数字のみです</label>
					</td>
				</tr>
				<%	
				}
	  			}finally{
	  				pm.close();
	  			}
				%>

			</table>				
				
				<p> <input class="bsize" type="submit" value="登録する"> </p>

			</form>
			
			<form action="/login.jsp">
				<input class="top" type="submit" value="ログイン画面へ" />
			</form>
			
			</div>
		<div id="footer">
		<address>Copyright (C) Team E, All rights reserved. </address>
		</div>
		</div>
	</body>
</html>