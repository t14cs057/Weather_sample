package com.System;

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 正規表現メモ
 * \\s : スペース
 * \\S : スペース以外
 * \\d : 半角数字
 * \\D : 半角数字以外
 * .   : すべての文字(スペースを除く)
 * \"  : ダブルクォーテーション
 * [.\\s\\S] : すべての文字
 */

public class Weather{
	private double snow;
	private double wind;
	
	public Weather(){
		this.snow = 0;
		this.wind = 0;
	}
	
	public void getWeather(int preNo, int bloNo) throws IOException{
		// 各種データ，月，日，時
		String month = null;
		String day = null;
		String time = null;
		String pattern = null;
				
		String pre = String.valueOf(preNo);
		String blo = String.valueOf(bloNo);
				
		TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
		// Calendarクラスで現在の年月日時を取得する
		Calendar calendar = Calendar.getInstance(tz);
	    calendar.setTimeZone(tz);

		month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

		// URLに合わせるため月の長さが 1 の場合先頭に 0 を付け足す．(1月->01月)
		if(month.length() == 1){
			StringBuilder sb = new StringBuilder();
			sb.append(month);
			sb.insert(0, "0");
			month = sb.toString();
		}
		// URLに合わせるため blockNo の長さを 4 になるよう先頭から 0 を付け足す
		while( blo.length() < 4 ){
			StringBuilder sb = new StringBuilder();
			sb.append(blo);
			sb.insert(0, "0");
			blo = sb.toString();
		}

		// URLを保持
		// prec_no と block_no が大事
		String urlString = "http://www.data.jma.go.jp/obd/stats/etrn/view/hourly_s1.php?prec_no="+pre+"&block_no="+blo+"&year=2015&month="+month+"&day="+day+"&view=p1";
		URL url = new URL(urlString);

		String str = getSourceText(url);

		/* URL のページのソースから取得する Pattern を設定する */
		// 時間が1時，2時の場合
		if( time == "1" || time == "2" )
			pattern = "style=\"white-space:nowrap\">"+ time + "\\D[.\\s\\S]*?<\\/tr>";
		// 時間が上記以外の場合
		else
			pattern = "style=\"white-space:nowrap\">"+ time + "[.\\s\\S]*?<\\/tr>";
		Pattern p = Pattern.compile(pattern,Pattern.MULTILINE);
		Matcher matcher1 = p.matcher(str);
						
		String result1 = "";
		while(matcher1.find()){
			result1 = matcher1.group();
		}
				 
		pattern = ">[.\\s\\S]*?<";

		p = Pattern.compile(pattern,Pattern.MULTILINE);
		Matcher matcher2 = p.matcher(result1);
				
		List<String> test = new ArrayList<String>();
		// matcher.find()はマッチングする箇所を一つずつ見つける
		while( matcher2.find()){
			test.add(matcher2.group(0).replaceAll("[^\\d.]",""));
		}
		// 風速は 16 番目
		// 降雪量は 24 番目
		// 積雪量は 25 番目
		if( test.size() <= 17 ){
			this.wind = 0;
			this.snow = 0;
			return ;
		}
		
		if( test.get(16).equals("") || test.get(16).equals(null))
			this.wind = 0 + 5.0;
		else					 
			this.wind = Double.parseDouble(test.get(16)) + 1;
				 
		if( test.get(24).equals("") || test.get(24).equals(null))
			this.snow = 0 + 5.0;
		else					 
			this.snow = Double.parseDouble(test.get(24)) + 1;		
	}

	public static String getSourceText(URL url) throws IOException {
		// URL からソースを取得する
		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String s;
			while (( s = bf.readLine()) != null ) {
				sb.append(s + "\n");
			}
		} finally {
			in.close();
		}
		return sb.toString();
	}
	
	public Double getSnow(){
		return this.snow;	
	}
	
	public Double getWind(){
		return this.wind;
	}
}