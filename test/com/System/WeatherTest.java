package com.System;

import static org.junit.Assert.*;

import org.junit.Test;

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.TimeZone;

public class WeatherTest {

	@Test
	public void TestGetWeather1() throws IOException {
		
		Weather weath = new Weather();
		
		// 山梨県甲府市
		int preNo = 49, bloNo = 47638;
		
		// 降雪量と風速を取得する前は 0 
		assertEquals("0.0", String.valueOf(weath.getWind()));
		// 降雪量と風速を取得する
		weath.getWeather(preNo, bloNo);
		// 降雪量と風速取得後は 0 でなくなる
		assertNotSame("0.0", String.valueOf(weath.getWind()) );
	}

	@Test
	public void TestGetWeather2() throws IOException {
		
		Weather weath1 = new Weather();
		Weather weath2 = new Weather();
		
		// 山梨県甲府市
		int preNo1 = 49, bloNo1 = 47638;
		
		// 愛知県岡崎市
		int preNo2 = 51, bloNo2 = 467;
		
		// 降雪量と風速を取得する前は 0 
		assertEquals("0.0", String.valueOf(weath1.getWind()));
		// 降雪量と風速を取得する前は 0 
		assertEquals("0.0", String.valueOf(weath2.getWind()));
		// 降雪量と風速を取得する前は風速が一致する
		assertEquals(String.valueOf(weath1.getWind()), String.valueOf(weath2.getWind()));

		// 降雪量と風速を取得する1
		weath1.getWeather(preNo1, bloNo1);
		// 降雪量と風速を取得する2
		weath2.getWeather(preNo2, bloNo2);

		// 降雪量と風速を取得する前は 0 
		assertNotSame("0.0", String.valueOf(weath1.getWind()));
		// 降雪量と風速を取得する前は 0 
		assertNotSame("0.0", String.valueOf(weath2.getWind()));
		// 降雪量と風速を取得する前は風速が一致する
		assertNotSame(String.valueOf(weath1.getWind()), String.valueOf(weath2.getWind()));
	}
	
	@Test
	public void TestGetSourceText2() throws IOException {

		// 山梨県甲府市
		int preNo = 49, bloNo = 47638;
		
		// 各種データ，年，月，日，時
		String year = null;
		String month = null;
		String day = null;
		String time = null;
		
		TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
		// Calendarクラスで現在の年月日時を取得する
		Calendar calendar = Calendar.getInstance(tz);
	    calendar.setTimeZone(tz);
	    
		year = String.valueOf(calendar.get(Calendar.YEAR));
		month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

		// URLに合わせるため月の長さが1桁の場合0を付け足す．(1月->01月)
		if(month.length() == 1){
			StringBuilder sb = new StringBuilder();
			sb.append(month);
			sb.insert(0, "0");
			month = sb.toString();
		}
		
		String urlString = "http://www.data.jma.go.jp/obd/stats/etrn/view/hourly_s1.php?prec_no="+preNo+"&block_no="+bloNo+"&year=2015&month="+month+"&day="+day+"&view=p1";
		URL url = new URL(urlString);
		
		// URL からソースを取得する
		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String s;
			while (( s = bf.readLine()) != null ) {
				//System.out.println(s);
				sb.append(s + "\n");
			}
		} finally {
			in.close();
		}
		
		String str = sb.toString();
		assertEquals( str, Weather.getSourceText(url) );
	}

	@Test
	public void TestGetSnow() throws IOException {
		Weather weath = new Weather();
		
		// 山梨県甲府市
		int preNo = 49, bloNo = 47638;
		
		// 降雪量と風速を取得する前は 0 
		assertEquals("0.0", String.valueOf(weath.getWind()));
		// 降雪量と風速を取得する
		weath.getWeather(preNo, bloNo);
		// 降雪量取得後は 0 でなくなる
		assertNotSame("0.0", String.valueOf(weath.getSnow()) );
	}

	@Test
	public void TestGetWind() throws IOException {
		Weather weath = new Weather();
		
		// 山梨県甲府市
		int preNo = 49, bloNo = 47638;
		
		// 降雪量と風速を取得する前は 0 
		assertEquals("0.0", String.valueOf(weath.getWind()));
		// 降雪量と風速を取得する
		weath.getWeather(preNo, bloNo);
		// 風速取得後は 0 でなくなる
		assertNotSame("0.0", String.valueOf(weath.getWind()) );	}

}
