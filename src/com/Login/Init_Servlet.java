// 初期登録処理を行うサーブレット

package com.Login;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.*;
import javax.servlet.http.*;

import com.Data.Account;
import com.google.apphosting.api.ApiProxy;

@SuppressWarnings("serial")
public class Init_Servlet extends HttpServlet {
	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException,ServletException {
		resp.setContentType("text/html;charset=UTF-8");
	
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    //Writer out = resp.getWriter();
	    
	    // エラー判定，アカウント作成判定
	    int error = 0, judge = 1;
	    
	    // init.jsp からのデータを受け取る 
	    String mail = req.getParameter("mail");
	    String pass = req.getParameter("pass");
	    String pass2 = req.getParameter("pass2");
	    
	    // error
	    if(mail.equals("")){
	    	error += 1;
	    }
	    // メールアドレスが適切かどうかの判定
	    if(!mail.matches("[\\w\\.\\-]+@(?:[\\w\\-]+\\.)+[\\w\\-]+")){
	    	error += 8;
	    }
	    if(pass.equals("")){
	    	error += 2;
	    }
	    if(pass2.equals("")){
	    	error += 4;
	    }
	    if(!pass.equals(pass2) && !pass.equals("") && !pass2.equals("")){
	    	error += 16;
	    }
	    if( pass.length() != 4 || pass2.length() != 4 ){
	    	error += 32;
	    }
	    if( !pass.matches("[\\d\\w]+") || !pass2.matches("[\\d\\w]+")){
	    	error += 64;
	    }
	    
	    // エラーを見つければリダイレクトする
	    if(error != 0){
	    	resp.sendRedirect("./init.jsp?Error=" + String.valueOf(error));
	    	return;
	    }

	    // クエリを作成 
		 List<Account> accounts = (List<Account>) pm.newQuery(Account.class).execute();

		 // アカウント作成の判定用フラグ
	    int flag = 0;

	    if( accounts.size() == 0 ){ // アカウントが一人もいなかった場合
	 	   flag = 1;
	    }
	    else{
		   for(Account acc : accounts){
			   if(mail.equals(acc.getMailId()) )			   // 既存のIDと一致した場合	   
				   flag = -1;
		   }
		   if( flag != -1 ) // 既存のIDと一致しなかった
			   flag = 1;  
	    }

	    if( flag == 1 ){
	    	// メール送信用関数の呼び出し
	    	// 返り値でこのあとの処理を判断
	    	int mailFlag = SendMail(mail, pass);
	    	if( mailFlag == 1 ){ // メールを送ることができた場合
		    	Account acc = new Account(mail, pass, new Date());	 	
		    	try {
		    		pm.makePersistent(acc); // Account エンティティを追加する
		    	} finally {
		   			pm.close();
		    	}
		    	judge = 2;
		    	resp.sendRedirect("/error.jsp?Judge=" + String.valueOf(judge));	
	    	}else { // なんらかの理由でメールが送信出来なかった場合
	   			pm.close();
		    	resp.sendRedirect("/error.jsp?mailFlag=" + String.valueOf(mailFlag));		    		
	    	}
    	}else{ // 既存のアカウントと一致した場合
	    	pm.close();
	    	resp.sendRedirect("/error.jsp?Judge=" + String.valueOf(judge));
	    	}
	}
	
	// 新規登録におけるメール送信を行なう関数
	public int SendMail( String mail, String password)throws IOException {
		
		// このシステムが稼働している appengine のプロジェクトIDを取得
		String appid = ApiProxy.getCurrentEnvironment().getAppId().toString();
		// 取得したID内に s~ が含まれていたため除去
		appid = appid.replaceAll("s~", "");
		// 送信元メールの @ マーク以降を定義
		String sender = "@" + appid + ".appspotmail.com";
		// 送信先メールアドレス兼ログインIDとパスワード
		String mailaddress = mail;
		String pass = password; 
		
		int flag = 1;
		Properties pt = new Properties();
		Session session = Session.getDefaultInstance(pt, null);
		try{
			Message msg = new MimeMessage(session);

			//発信元のアドレスを指定- GAE にデプロイ後でないと試せない...
			/**
			 * InternetAddress("送信元メールアドレス", "メールの名前的な", "文字コード")
			 */
			msg.setFrom(new InternetAddress("softeng-cs2015" + sender, "チームMJ", "ISO-2022-JP"));

			//送信先のアドレスを指定()
			/** Message.RecipientType.**でメールの送信形式を決定
			 * TO
			 * BCC
			 * CC
			 */
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail, true));
			// カレンダークラスにて現在の日付を取得(タイムゾーンは東京)
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
			calendar.setTimeZone(tz);
			// 改行を定義
			String newline = System.getProperty("line.separator");
			// 各種日時
			String year, month, day, time, hour, minute;
			// Calender クラスで取得
			year = String.valueOf(calendar.get(Calendar.YEAR));
			month = String.valueOf(calendar.get(Calendar.MONTH)+1);
			day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
			minute = String.valueOf(calendar.get(Calendar.MINUTE));
			
			// メール本文の体裁のため( 1月->01月 ) となるような処理をする
			month = append(month);
			day = append(day);
			hour = append(hour);
			minute = append(minute);

			// 日付情報をまとめる
			String date = year + " 年 " + month + " 月 " + day + " 日 " + hour + " 時 " + minute + " 分 ";
			URL url = new URL("http://"+ appid + ".appspot.com/login.jsp");			
 			//メールのタイトルと本文
			String subject = "新規登録";
			String content = "ユーザー登録しました" + newline +  
								"登録した日付 : " + date + newline +
								"登録したメールアドレス : " +  mailaddress + newline + 
								"登録したパスワード : " + pass + newline + 
								"URL : " + url + newline;
			
			msg.setSubject(MimeUtility.encodeText(subject, "iso-2022-jp", "B"));
			msg.setText(content); 
			// メールの送信
			Transport.send(msg);
		} catch (AddressException e) { // 送信元メールアドレスに問題あり
			flag = 2;
		} catch (MessagingException e) { // 送信先メールアドレスが見つからなかった場合
			flag = 4;
		}

		return flag;
	}
	
	// 引き数文字列が長さ 1 の時 0 を先頭に付け足す関数
	private String append(String str){
		if( str.length() == 1 ){
			StringBuilder sb = new StringBuilder();
			sb.append(str);
			sb.insert(0, "0");
			str = sb.toString();
		}
		return str;
	}


}