<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Date,java.text.SimpleDateFormat" %>
<%@ page import="java.util.List"%>  
<%@ page import="java.util.ArrayList"%>  
<%@ page import="javax.jdo.PersistenceManager"%>  
<%@ page import="javax.jdo.Query"%>
<%@ page import="javax.servlet.http.*"%>  
<%@ page import="com.Login.*"%>  
<%@ page import="com.Data.*"%>
<%@ page import="com.System.*"%> 
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 

 <html>
 		   <%
 	       // ログインをしているかの判定
 	       if( session.getAttribute("access") == null ){
 		  	   response.sendRedirect("./error.jsp?Judge=4");
 		   }
 		   
 		  // 現在ログインしているアカウントのメールアドレス 
 		   String mailId = String.valueOf(session.getAttribute("mailId"));
 		   int size = 0, number = 0;

 		   if( request.getParameter("Num") != null )
 			   number = Integer.parseInt(request.getParameter("Num"));
 		   
 		   // このJSPで用いる各変数の初期化
	    	PersistenceManager pm = null;
	  		try {
  			pm = PMF.get().getPersistenceManager();
  			Query query = pm.newQuery(House.class);
  			query.setOrdering("date asc");
	  		query.setFilter("mailId == id");
 			query.declareParameters("String id");
  			
  			List<House> houses = (List<House>) query.execute(mailId);
  			
  			size = houses.size();
  			int num = 1;
  			// 各ハウスの危険度を保持
  			List<Integer> risky = new ArrayList<Integer>();
  			List<Integer> riskySnow = new ArrayList<Integer>();
  			List<Integer> riskyWind = new ArrayList<Integer>();
  			// 危険度が小以上のハウスを管理
  			int flag = 0;
  			%>
    <head>
    	<!-- スタイルシートの適用 -->
	  	<meta http-equiv="Content-Style-Type" content="text/css">
		<link rel="stylesheet" href="/csslib/sheet_test.css" type="text/css"/>
		
		<title>ビニールハウス倒壊予測管理システム画面</title>
		
		<script type="text/javascript">
  		function detail(){
  			window.open("./detailHouse.jsp", "detail", "menubar=no, scrollbars=yes, resizable=no, width=540, height=450");
  		}

  		</script>
	</head>
    <body>
    
    <div id="container">
		<div id="header">
		<h1>ビニールハウス倒壊予測管理システム</h1>
		こんにちは　<font color="#ffffff"><%=mailId%></font>　さん
       <a href="/logout">ログアウト</a> 
		</div>
		<div id="content">
		
	    <h2> ビニールハウス倒壊予測管理システム画面 </h2>   

	<% 		
			// 各ハウスの危険度を計算
  			for (House house: houses) {
  				 Calculation cal = new Calculation(house);
  				 cal.calcWeather();
  				 int calRisk = cal.riskCalc();
				 risky.add(calRisk);
				 riskySnow.add(cal.getRiskSnow());
				 riskyWind.add(cal.getRiskWind());
  				 if(calRisk >= 3)
  					 flag = 1;
  			}
	
			/* 倒壊の危険のあるハウスを列挙する -------------------------------- */
  			if(flag == 0){ %>
  			<div class="frame">
 				※現在倒壊の危険のあるハウスはありません。
			</div> 
	   <% } else { %>
	 		<div class="frame rcolor">※倒壊の危険のあるハウスがあります！</div> 
	 		<div class="frame2 top">
			 <%
			num = 1;
			for(Integer risk : risky){		
			%>
	  			<h2 class="warning">
	  			<%
	  			if(risk >= 5){ %>
	  			※ ハウス<%=num %> : 危険度　<font class="rcolor">大</font>
	  			<%} 
	  			else if(risk >= 4){ %>
	  			※ ハウス<%=num %> : 危険度　<font class="ocolor">中</font>
	  			<%} 
	  			else if(risk >= 3){ %>
	  			※ ハウス<%=num %> : 危険度　<font class="ycolor">小</font>
	  			<%} %>
	  			</h2>	 

			<% 	num++;	
				} %>
			</div>	
			<% }	
  			 
  			/* 各ハウスの状態を表示 --------------------------------*/
  			num = 1;	
  			for (House house: houses) {
  				int riskSnow = riskySnow.get(num-1); // 雪に対する危険度
				int riskWind = riskyWind.get(num-1); // 風に対する危険度
  			%>
  			<div id="info">
	  			<font> ハウス<%=num%> : </font>
	  			<%if(risky.get(num-1) <= 2){ %>
	  			 <font color="green">正常</font> 
	  			<%} else if(risky.get(num-1) >= 3){ %>
	  			<label><font>危険度</font><font class="ycolor"> 小</font></label>
	  			<%
	  				/*-- 雪に対する対策を表示する------------------------------------------------------------*/ 
	  				if(riskSnow == 1){ %>
	  					<ul>
		  					<li>冬季期間使用しないビニールは除去する</li> 
		  					<li>被覆材の表面に雪の滑落を妨げるようなものがあれば除去する</li> 
			  				<li>積雪によりビニールが緩みパイプにひっかかり滑落を妨げる可能性もあるので
			  				人力で雪を除去するとともに釘やハウスバンドによる抑えを強化する</li> 
			  				<li>多くの融雪水が発生する場合に備え排水路の清掃や設備の確認をする</li>
		  				</ul> 
		  			 <% }else if(riskSnow >= 2){ %>
		  				<ul>
			  				<li>ビニールハウス内でストーブを使用して屋根に雪がたまらないようにする</li>
		  				</ul>
		  			<% } else if(riskSnow >= 4){ %>
		  				<ul>
			  				<li> ビニールハウスに積もっている雪を落とす </li>
		  				</ul>
		  			<% } 
	  				/*-- 風に対する対策を表示する------------------------------------------------------------*/
	  					if(riskWind == 1){%>
		  				<ul>
			  				<li>周囲を片づける</li> 
							<li>被覆資材の破れや穴の補修や柱の補強する</li> 
							<li>妻部など風当たりの強い箇所には寒冷紗などを張る</li> 
							<li>側面部分のビニールがめくれないように固定する </li>
		  				</ul>	
		  			<% } else if(riskWind == 2){ %>
		  				<ul>
			  				<li>暖房機や換気装置の電源を切る</li> 
							<li>ドアが外れないようにしっかりと固定する</li>
		  				</ul>
		  			<% } else if(riskWind == 3){ %>
		  				<ul>
			  				<li>風下から被覆資材を切る</li>
		  				</ul>
		  			<% } else if(riskWind >= 4){ %>
		  				<ul>
			  				<li>近づかない</li>
		  				</ul>
		  			<% }		  			
	  			}
	  			else if(risky.get(num-1) < 5){ %>
		  		<label><font>危険度</font> <font class="ocolor"> 中</font></label>
		  		<%  
		  			/*-- 雪に対する対策を表示する------------------------------------------------------------*/ 
		  			if(riskSnow == 1){ %>
	  					<ul>
		  					<li>冬季期間使用しないビニールは除去する</li> 
		  					<li>被覆材の表面に雪の滑落を妨げるようなものがあれば除去する</li> 
			  				<li>積雪によりビニールが緩みパイプにひっかかり滑落を妨げる可能性もある
			  				ので人力で雪を除去するとともに釘やハウスバンドによる抑えを強化する</li> 
			  				<li>多くの融雪水が発生する場合に備え排水路の清掃や設備の確認をする</li>
		  				</ul> 
		  				<% } else if(riskSnow == 2){ %>
		  				<ul>
		  					<li>ビニールハウス内でストーブを使用して屋根に雪がたまらないようにする</li>
		  				</ul>
		  			<% }else if(riskSnow >= 4){%>
		  				<ul>
		  					<li>ビニールハウスに積もっている雪を落とす </li>
		  				</ul>
		  			<% }
		  			/*-- 風に対する対策を表示する------------------------------------------------------------*/ 
		  			if(riskWind == 1){%>
		  				<ul>
			  				<li>周囲を片づける</li> 
							<li>被覆資材の破れや穴の補修や柱の補強する</li> 
							<li>妻部など風当たりの強い箇所には寒冷紗などを張る</li> 
							<li>側面部分のビニールがめくれないように固定する </li>
		  				</ul>
		  				
		  			<% }else if(riskWind == 2){%>
		  				<ul>
			  				<li>暖房機や換気装置の電源を切る</li> 
							<li>ドアが外れないようにしっかりと固定する</li>
		  				</ul>
		  			<% }else if(riskWind == 3){%>
		  				<ul>
			  				<li>風下から被覆資材を切る</li>
		  				</ul>
		  			<% }else if(riskWind >= 4){%>
		  				<ul>
		  					<li>近づかない</li>
		  				</ul>
		  			<% }
		  			} else if( risky.get(num-1) >= 5){ %>
	  				<label><font>危険度</font><font class="rcolor"> 大</font></label>
	  				<% 
	  				/*-- 雪に対する対策を表示する------------------------------------------------------------*/ 
	  				if(riskSnow == 1){%>
	  					<ul>
		  					<li>冬季期間使用しないビニールは除去する</li> 
		  					<li>被覆材の表面に雪の滑落を妨げるようなものがあれば除去する</li> 
			  				<li>積雪によりビニールが緩みパイプにひっかかり滑落を妨げる可能性もある
			  				ので人力で雪を除去するとともに釘やハウスバンドによる抑えを強化する</li> 
			  				<li>多くの融雪水が発生する場合に備え排水路の清掃や設備の確認をする</li>
		  				</ul> 
		  			 <%}else if(riskSnow == 2){%>
		  				<ul>
		  					<li>ビニールハウス内でストーブを使用して屋根に雪がたまらないようにする</li>
		  				</ul>
		  			<% }else if(riskSnow >= 4){%>
		  				<ul>
		  					<li>ビニールハウスに積もっている雪を落とす </li>
		  				</ul>
		  			<% }
	  				/*-- 風に対する対策を表示する------------------------------------------------------------*/ 
	  				if(riskWind == 1){%>
		  				<ul>
			  				<li>周囲を片づける</li> 
							<li>被覆資材の破れや穴の補修や柱の補強する</li> 
							<li>妻部など風当たりの強い箇所には寒冷紗などを張る</li> 
							<li>側面部分のビニールがめくれないように固定する </li>
		  				</ul>
		  				
		  			<% }else if(riskWind == 2){%>
		  				<ul>
			  				<li>暖房機や換気装置の電源を切る</li> 
							<li>ドアが外れないようにしっかりと固定する</li>
		  				</ul>
		  			<% }else if(riskWind == 3){%>
		  				<ul>
		  					<li>風下から被覆資材を切る</li>
		  				</ul>
		  			<% }else if(riskWind >= 4){%>
		  				<ul>
		  					<li>近づかない</li>
		  				</ul>
		  			<% }
	  			} %>
			</div>
			<!-- ハウスの概要を表形式で表示 -->
  			<table class="sample_01">
			<tbody>
				<tr>
					<th>ハウスの種類</th>
					<th>ハウスの経過年数</th>
					<th>ビニールの種類</th>
					<th>ビニールの経過年数</th>
				</tr>
				<tr>
					<td> <%= house.getPillar() %> </td>
					<td> <%= house.getPillarNum() %> </td>
					<td> <%= house.getVin() %> </td>
					<td> <%= house.getVinNum() %> </td>
				</tr>
			</tbody>
			</table>      
	 			<%
	  			num++;
	  			}
	  			} finally {
	  				if (pm != null && !pm.isClosed())
	  					pm.close();				
	  				}
	  		// ハウスの数が0以上の場合各ハウスの詳細画面に遷移することができる
	  		if( size > 0 ){%>		
		  		<form name="f">
					<input class="bsize" type="button" onClick="detail()" value="ハウスの詳細画面へ" />           						
			 	</form>
	  		<%} %>
	  		
				<form action="/editHouse.jsp">
					<p><input class="center bsize" type="submit" value="ハウス情報の変更" /></p>
				</form>
			<%if( size < 10 ){ %>
				<form action="/addHouse.jsp">
					<input type="hidden" name="Size" value="<%=size%>" />				
					<p><input class="center bsize" type="submit" value="ハウス情報の追加" /></p>
				</form>
			<%} %>
  		</div>
		<div id="footer">
			<address>Copyright (C) Team E, All rights reserved. </address>
		</div> 
		
		</div>  

   </body>
</html>
