package com.System;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.Login.PMF;
import com.Data.House;
import com.Data.Amedas;

public class Calculation {
	
	private House house;
	private double wind; // 風速
	private double snow; // 降雪量
	// 風速と降雪量に関する危険度ポイント
	private int riskWind = 0, riskSnow = 0; 
	
	// コンストラクタ
	public Calculation(House house){
		this.house = house;
	}
	
	public void calcWeather() throws Exception{
		// 都道府県が北海道の場合は処理を変える
		int hokkaido = 0;
		// 天候データを取得するクラスを定義
		Weather weath = new Weather();
		// 現在のハウスの住所を取得
		String address = house.getAddress();
        /*
         * 住所を
         *  ~ [都,道,府,県] ~ [市,郡] ~ [町,村] ~ 
         *  に分割するための処理を行なう
         *  
         */
		// 住所を都道府県で分割
		// split[0] = ~[都道府県], split[1] = [都道府県]以外
       String[] split = address.split("県", 2);
       if( split.length == 1 )
    	   split = split[0].split("道", 2);
       if( split.length == 1 )
    	   split = split[0].split("府", 2);
       if( split.length == 1 )
    	   split = split[0].split("都", 2);
        // 都道府県が北海道の場合の処理
       if(split[0].equals("北海道"))
    	   hokkaido = 1;
        
       // 住所を市,郡で分割
       // split2[0] = ~[市,郡], split2[1] = [市, 郡]以外
       String[] split2 = split[1].split("市", 2);
       if( split2.length == 1 )
    	   split2 = split[1].split("郡", 2);
       	
       // 住所を町,村で分割
       // split3[0] = ~[町,村], split3[1] = [町, 村]以外
       String[] split3 = null;
       if( split2.length != 1 	){
    	   split3 = split2[1].split("町", 2);
    	   if( split3.length == 1 )
        	   split3 = split2[1].split("村", 2);
       }
       
       /*
        * 分割した住所に基づき Amedas データストアから該当する[府県番号]と[地点コード]を検索,取得する
        * [府県番号]と[地点コード]は天候データを取得するために必要となるため
        */
       PersistenceManager pm = PMF.get().getPersistenceManager();
 		try {
 			// Amedas のデータストアに関する Query
 			Query query = pm.newQuery(Amedas.class);
 			List<Amedas> amedases = null;
 			// 北海道の場合
 			if( hokkaido == 0 )
 				query.setFilter("fuken_name ==" + "'" + split[0] + "'");

 			// fuken_name または station_name に該当する[市,郡],[町,村]が存在するかに関するフィルターをかける
 			if( split3 != null ){
 				String str1 = split2[0], str2 = split3[0];
 				query.setFilter("station_name == s1 || station_name == s2");
 				query.declareParameters("String s1, String s2");
 				
 				amedases = (List<Amedas>) query.execute(str1, str2);
 			}else{
 				amedases = (List<Amedas>) query.execute();
 			}
 
 			// フィルターに該当するものがなくアメダス情報からデータを取得出来なかった場合 ----------- */
 			// 例 山梨県南都留郡富士河口湖町 とかがこれに該当します
 			if( amedases.size() == 0 ){
 				// Query を1一度初期化
 				// station_name に関するフィルターをまず掛け直す
 				query = pm.newQuery(Amedas.class);	
 				query.setFilter("fuken_name ==" + "'" + split[0] + "'");
 				List<Amedas> tmps = (List<Amedas>) query.execute();
 				Amedas ameTrue = null;
 				
 				// station_name に関するフィルターで[村, 市]に関して曖昧検索を行なう
 				for( Amedas ame : tmps ){
 					 String str = "[.\\s\\S]*?" + ame.getStationName() + "[.\\s\\S]*?";
 					 Pattern pattern = Pattern.compile(str, Pattern.MULTILINE);
 					 Matcher matcher = pattern.matcher(split3[0]);
 					if(matcher.find()){
 						ameTrue = ame;
 						break;
 					}
 				}
 				
 				/*
 				 * 上記2つの処理を終え,[府県番号]と[地点コード]から降雪量と風速を取得する
 				 */
 				if( ameTrue != null ){
 					// 天候データを取得する関数を呼び出す
 					weath.getWeather(ameTrue.getFukenId(), ameTrue.getStationId());
 				}else{
 					weath.getWeather(0, 0); 		
 				}
 				// 風速と降雪量を取得
 				this.wind = weath.getWind();
 				this.snow = weath.getSnow();
 			}/* -------------------------------------------------------------------------------------- */			
 			else{ 
 				for (Amedas amedas: amedases) {
 					weath.getWeather(amedas.getFukenId(), amedas.getStationId());
 					// 風速と降雪量を取得する
 					this.wind = weath.getWind();
 					this.snow = weath.getSnow();
 				}
 			}
 		} finally {
				if (pm != null && !pm.isClosed())
					pm.close();				
				}
	}
	
	// 各ハウスの危険度を計算する
	public int riskCalc( ) throws Exception{
		// 危険度の合計値
		int riskPoint = 0;
		// 風速
		double windSpeed = getWind();
		// 降雪量
		double snow = getSnow();
		
		// ハウスの柱,経過年数,ビニール,経過年数
		String pillar = house.getPillar();
       int pillarNum = house.getPillarNum();

       String vin = house.getVin();
       int vinNum = house.getVinNum();
        
       // 風速--------------------------------------------------------
       if(windSpeed >= 15 && windSpeed < 20 ){
    	   riskWind = 1;
       	} 
       else if(windSpeed >= 20 && windSpeed < 30 ){
    	   riskWind = 2;
          }      
       else if(windSpeed >= 30 && windSpeed < 40 ){
         	riskWind = 3;
          }       
       else if(windSpeed >= 40 ){
         	riskWind = 4;
          }
       riskPoint += riskWind;
       
       // 積雪量-----------------------------------------------------
       if(snow >= 5 && snow < 10 ){
    	   riskSnow = 1;
       } 
       else if(snow >= 10 && snow < 15 ){
    	   riskSnow = 2;
       }       
       else if(snow >= 15 && snow < 20 ){
    	   riskSnow = 4;      	
       }        
       else if(snow >= 20){
    	   riskSnow = 5;      	
       } 
       riskPoint += riskSnow;
       
       // 柱の種類と経過年数------------------------------------------
       if(pillar.equals("鉄") && pillarNum >= 14 ){
    	   riskPoint++;
        }
       else if(pillar.equals("木") && pillarNum >= 5 ){
    	   riskPoint++;
       }
       else if(pillar.equals("その他") && pillarNum >= 8 ){
    	   riskPoint++;   	
       }
       
       // ビニールの種類-----------------------------------------------
       if(vin.equals("その他")){
    	   riskPoint++;
       }
       
       return riskPoint;  
	}
	
	// 降雪量を取得する
	public double getSnow(){
		return snow;
	}
	// 風速を取得する
	public double getWind(){
		return wind;
	}
	// 降雪量の危険度を取得
	public int getRiskSnow(){
		return riskSnow;
	}
	// 風速の危険度を取得
	public int getRiskWind(){
		return riskWind;
	}
	
}
