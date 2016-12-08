// 倒壊の危険があるハウスがある場合，メールを送信する

package com.System;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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

import com.Data.*;
import com.Login.PMF;
import com.google.apphosting.api.ApiProxy;

@SuppressWarnings("serial")
public class SendMail extends HttpServlet {	
	
	// cron によって自動で呼び出される関数
	@Override
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException,ServletException {	

		resp.setContentType("text/html; charset=UTF-8");
		
		PersistenceManager pm = null;
		pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Account.class);
		// 登録されている全ユーザーの情報を保持		
		List<Account> accounts = (List<Account>) query.execute();
		
		// 登録されている各アカウントに次の処理を行なう
		for(Account account : accounts ){
			// アカウント情報を一つ取り出す
			String mailId = account.getMailId();			
			List<String> mailList = account.getMailList();
			query = pm.newQuery(House.class);
			// メールアドレスでハウス情報にフィルタをかける
			query.setFilter("mailId == " + "'" + mailId + "'");
			List<House> houses = (List<House>) query.execute();
			// 危険度小以上のハウスが存在するか
			int riskJudge = 0;
			List<Integer> risky = new ArrayList<Integer>();
			for(House house : houses ){	
				// ハウス情報を一つ取り出す
				Calculation cal = new Calculation(house);
				try {
					// そのハウスの危険度を計算する
					cal.calcWeather();
					// 危険度を取得
					int calRisk = cal.riskCalc();
					// 危険度をリストに登録
					risky.add(calRisk);
					if(calRisk >= 3)
						riskJudge = 1;						
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 危険度が小以上のハウスがあった場合
			if( riskJudge == 1 ){
				// メールを送信する
				sendMail(mailId, mailList, risky, resp);
			}
		}
	}
	// メールを送信する関数
	public void sendMail( String mail, List<String> mails, List<Integer> risks ,HttpServletResponse resp)throws IOException {

		// このシステムが稼働している appengine のプロジェクトIDを取得
		String appid = ApiProxy.getCurrentEnvironment().getAppId().toString();
		// 取得したID内に s~ が含まれていたため除去
		appid = appid.replaceAll("s~", "");
		// 送信元メールの @ マーク以降を定義
		String sender = "@" + appid + ".appspotmail.com";
		// 送信先メールアドレス兼現在のアカウントID
		String mailAddress = mail;
		// 各ハウスに関する危険度
		List<Integer> risky = risks;
		// 送信先メールアドレスのリスト
		List<String> mailList = mails;

		// 本文に記述する内容
		String[] text = new String[risky.size()];
		// ハウスの番号
		int number = 1;
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
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress, true));
			if( mailList.size() >= 1 ) {
				for( String address : mailList )
					msg.setRecipient(Message.RecipientType.CC, new InternetAddress(address, true));
			}
			// カレンダークラスにて現在の日付を取得(タイムゾーンは東京)
			Calendar calendar = Calendar.getInstance();
			TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
			calendar.setTimeZone(tz);
			// 改行を定義
			String newline = System.getProperty("line.separator");
			// 各種日時
			String year, month, day, hour, minute;
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

			// メールの Subject
			String subject = "ビニールハウス倒壊予測システム";
			// メールのタイトルを定義 - 文字エンコードを指定
			msg.setSubject(MimeUtility.encodeText(subject, "iso-2022-jp", "B"));
			// 危険度計算
			for( Integer risk1 : risky ){
				if( 5 <= risk1 ){
					text[number-1] = "ハウス" + number + " : 危険度 大" ;
				} else if( 4 <= risk1  ){
					text[number-1] = "ハウス" + number + " : 危険度 中" ;	        			
				}else if( 3 <= risk1  ){
					text[number-1] = "ハウス" + number + " : 危険度 小" ;	        			
				}else {
					text[number-1] = "";
 				}
				number++;
			}

			// メールの本文を保持	
			String contents = "警告発生日時 : " + date + newline;
			for( int i = 0; i < text.length; i++ ){
				if( !text[i].equals(""))
					contents += text[i] + newline; 
			}
			contents += "警告文はこちらサイトでご確認下さい : " + url + newline;
			// メールの本文を定義
			msg.setText(contents);
			// メールを送信
			Transport.send(msg);
		} catch (AddressException e) {
			// 例外処理1  
		} catch (MessagingException e) {
			// 例外処理2     	
		}
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