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

 <html>
 	       <%
       // ログインをしているかの判定
       if( session.getAttribute("access") == null ){
	  	   response.sendRedirect("./error.jsp?Judge=4");
	   }
       // 現在ログインしているアカウントのメールアドレス       
       String mailId = String.valueOf(session.getAttribute("mailId"));
	
       PersistenceManager pm = null;
		int size = 0;
 		try {
			pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(House.class);
			query.setOrdering("date asc");
			// ハウスエンティティのメールアドレスに対してフィルターをかける
			query.setFilter("mailId == id");
			query.declareParameters("String id");		
			List<House> houses = (List<House>) query.execute(mailId);
			size = houses.size();
			if( size != 0 ){
				response.sendRedirect("./HouseInfo.jsp");		
			}
		}finally{
 			pm.close();
 		}
 		
       // エラー内容，エラーの番号
       long error = 0, errCount = 1;
       // 登録しようとしているハウスの数
       int count = 1;
       
       // 登録できるハウスの上限
       final int MAX_HOUSE = 10;
       // エラーを取得
		String str = request.getParameter("Error");
       // 登録しようとしているハウスの数を取得
		String s = request.getParameter("Count");
	
		if(str != null){
			error = Long.parseLong(str); // エラー内容を得る(2進数)
		}
		
		if(s != null){
			count = Integer.parseInt(s); // ハウスの個数を得る
		}
		%>
    	<head>
    	<!-- 郵便番号 -> 住所の変換に使用 -->
       <script src="http://ajaxzip3.googlecode.com/svn/trunk/ajaxzip3/ajaxzip3.js" charset="UTF-8"></script>
  		
  		<!-- スタイルシートの読み込み -->
  		<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		
		<title>ハウス情報登録画面</title>
       </head>
            		  
      <body>
	<div id="container">
		<div id="header">
			<h1>ビニールハウス倒壊予測管理システム</h1>
			こんにちは <font color="#ffffff"><%=mailId%></font> さん <a href="/logout">ログアウト</a>
		</div>

		<div id="content">

			<h2>ハウス情報登録画面</h2>
			<div id="leftAlign">
			
			<!-- ハウスの情報を登録する -->
			<form action="/new_house" method="post">
				<% for(int i = 0; i < count; i++){%>
				<p>----------------------------------------------------------------</p>

				<h3 class="color">ハウス<%=i+1%></h3>
					<p>
						郵便番号 :
						<!-- AjaxZip3.zip2addr( '〒上3桁', '〒下4桁', '都道府県', '市区町村', '町域大字', '丁目番地' );  -->
						<input class="left" type="text" name="zipCode1<%=i%>" size="4"
							maxlength="3" /> - <input class="left" type="text"
							name="zipCode2<%=i%>" size="5" maxlength="4"
							onKeyUp="AjaxZip3.zip2addr('zipCode1<%=i%>','zipCode2<%=i%>','addr<%=i%>','addr<%=i%>');" />
					</p>

					<!-- 郵便番号が空白のとき,半角数字以外が入力されたとき -->						
					<% if((error & ( 1*(exponential2(i))*errCount ) ) == (1*(exponential2(i)) * errCount) ){ %>
							<p class="rcolor">※ 郵便番号が正しく入力されていません</p> 
					<%	} %>
							<p> 住所 : <input class="left" type="text" name="addr<%=i%>" size="30" /> </p> 
					<!-- 住所が空白のとき,不適切な文字列が入力されたとき -->							
					<% if((error & ( 2*(exponential2(i))*errCount ) ) == 2 * (exponential2(i)) * errCount){ %>

							<p class="rcolor">※ 住所が正しく入力されていません</p> 
							<%	} %>
							<p>
								使用している柱 : <select name="pillar<%=i%>">
									<option value="">選択してください</option>
									<option value="pillar1">鉄</option>
									<option value="pillar2">木</option>
									<option value="pillar3">その他</option>
								</select>
							</p>
					<!-- 柱が選択されていないとき -->				 
					<% if((error & ( 4*(exponential2(i))*errCount) ) == 4 * (exponential2(i)) * errCount){ %>
							<p class="rcolor">※ 使用している柱が選択されていません</p>  
							<%	} %>
							<p> 柱を建ててからの経過年数 : 
								<select name="pillarNum<%=i%>">
									<option value="">選択してください</option>
									<%for(int j = 0; j <= 15; j ++ ){ %>
									<option value="<%=j%>"><%=j%></option>
									<%} %>
								</select>
							</p> 
					<!-- 柱の年数が選択されていないとき -->	
					<% if((error & ( 8 * (exponential2(i)) * errCount ) ) == 8 * (exponential2(i)) * errCount){ %>
							<p class="rcolor">※ 柱の経過年数が選択されていません</p>  
					<%	} %>
							<p> ビニールハウスの種類 :
								 <select name="vin<%=i%>">
									<option value="">選択してください</option>
									<option value="vin1">ポリオレフィン系フィルム</option>
									<option value="vin2">その他</option>
								</select>
							</p> 
					<!-- ビニールハウスが選択されていないとき -->
					<% if((error & ( 16 * (exponential2(i)) * errCount ) ) == 16 * (exponential2(i)) * errCount){ %>
							<p class="rcolor">※ ビニールハウスの種類が選択されていません</p>  
					<% } %>
							<p>
								ビニールを張ってからの経過年数 : <select name="vinNum<%=i%>">
									<option value="">選択してください</option>
									<%for(int j = 0; j <= 15; j ++ ){ %>
									<option value="<%=j%>"><%=j%></option>
									<%} %>
								</select>
							</p> 
					<!-- ビニールの年数が選択されていないとき -->
					<% if((error & ( 32 * (exponential2(i)) * errCount ) ) == 32 * (exponential2(i)) * errCount){ %>
							<p class="rcolor">※ ビニールの経過年数が選択されていません</p> 
					 <%	} %>				
					<p>----------------------------------------------------------------</p>
							<%
							errCount = exponential32( i + 1 );
					        } 
					        %> <!-- hidden属性でハウスの個数とメールIDを送る --> 
					        <input type="hidden" name="count" value="<%=count%>" /> 
					        <input type="hidden" name="mailId" value="<%=mailId%>" />
				
							<table>
								<tr>
									<td>
										<h2>連絡先の追加</h2>
										<label>
											メールアドレスの追加 : <input class="left" type="text" name="addMail" size="40" />
										</label>
									</td>
								</tr>
							</table>
							<p> <input class="bsize center" type="submit" value="登録" /> </p>
			</form>
			</div>
			<!-- 登録するハウスを増やす -->
			<form action="/add_house" method="post">
				<input type="hidden" name="count" value="<%=count%>" /> 
				<input type="hidden" name="mailId" value="<%=mailId%>" /> 
				<input type="hidden" name="size" value="<%=size%>" />
				<input class="bsize" type="submit" value="ビニールハウスの追加" /> 
				<label class="rcolor">※あと<%=MAX_HOUSE - count%>個まで追加できます </label>
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
   
   <%!/*-- 関数を定義する --*/
   
 	// 32の引数乗を計算する関数
	long exponential32(int freq) {
		long number = 1;
		for (int i = 0; i < freq; i++)
			number *= 32;
		return number;
	}

	// 2の引数乗を計算する関数
	long exponential2(int freq) {
		long number = 1;
		for (int i = 0; i < freq; i++)
			number *= 2;
		return number;
	}%>
   
</html>
