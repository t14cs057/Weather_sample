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
<%@ page import="com.Delete.*"%>  
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

 <html>
 			<% 
 			int error = 0, logger = 0;
			// Initial_Servlet から
			// エラーがあれば str に何かしらの値が入る
			String str = request.getParameter("Error");
			if(str != null){
				error = Integer.parseInt(str);
			}
 	    	PersistenceManager pm = null;
	  		try {
  			pm = PMF.get().getPersistenceManager();
  			Query query = pm.newQuery(Account.class);
  			query.setOrdering("date asc");
  			List<Account> accounts = (List<Account>) query.execute();
  			%>
   <head>
  		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		
		<title>ログイン</title>
	</head>
	<body>
		<div id="container">
		<div id="header">
		<h1>ビニールハウス倒壊予測管理システム</h1>
		</div>
		
		<div id="content">
        <h2>ログイン認証</h2>            		    

        <form action="/login" method="post">
        <table>
        	<tr>
        		<td>
	 	        <label>メールアドレス : 
	 	        <input class="left" type="text" name="mail" />
	 	        </label>
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
					<label class="rcolor">※ 適切なメールアドレスを入力して下さい</label>
					</td>
				</tr>
				<%	} %>
				<%	if((error & 16) == 16){ %>
				<tr>
					<td>
					<label class="rcolor">※ そのメールアドレスは登録されていません</label>
					</td>
				</tr>
				<%	} %>
				<tr>
		  	     <td>
			  	     <label>パスワード(4桁) :  
			  	     <input class="left" type="password" name="pass" />
			  	     </label>
				  </td>
			  </tr>	
			  	<%	if((error & 2) == 2){ %>
			  	<tr>
				  	<td>
					<label class="rcolor">※ パスワードが未入力です</label>
					</td>
				</tr>
				<%	} %>	
				<%	if((error & 32) == 32){ %>
				<tr>
					<td>
					<label class="rcolor">※ パスワードが間違っています</label>
					</td>
				</tr>
				<%	
				}
	  			} finally {
  				if (pm != null && !pm.isClosed())
  					pm.close();				
  				}
				%>
			</table> 
								  
			  <p>	<input class="bsize" type="submit" value="ログイン"> </p>
		 </form>
		 
		 <form action="/init.jsp">
		 	<input class="top" type="submit" value="新規登録はこちら" />
		 </form>
		 </div>

		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved. </address>
		</div> 
		
		</div>  
   </body>
</html>
