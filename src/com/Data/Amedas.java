package com.Data;

import java.util.ArrayList;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Amedas {

	// メンバ id はエンティティ（タプル）のキーである
	@PrimaryKey
	// メンバ id の値はシステムが自動的に重複しないようつける
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String fuken_name;
	
	@Persistent
	private int fuken_id;
	
	@Persistent
	private String station_name;
	
	@Persistent
	private String station_name_kana;
	
	@Persistent
	private int station_id;
	
	public Amedas( String fuken_name, int fuken_id, String station_name, String station_name_kana, int station_id){
		this.fuken_name = fuken_name;
		this.fuken_id = fuken_id;
		this.station_name = station_name;
		this.station_name_kana = station_name_kana;
		this.station_id = station_id;
	}
	
	public String getFukenName(){
		return fuken_name;
	}
	public int getFukenId(){
		return fuken_id;
	}
	public String getStationName(){
		return station_name;
	}
	public String getStationNameKana(){
		return station_name_kana;
	}
	public int getStationId(){
		return station_id;
	}
	
}
