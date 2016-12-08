package com.Data;


import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

// Memoクラスでカインド（テーブル）を定義するための宣言
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class House {

	// メンバ id はエンティティ（タプル）のキーである
	 @PrimaryKey
	// メンバ id の値はシステムが自動的に重複しないようつける
	 @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	 @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	 private String id;
	
	@Persistent
	private String mailId;

	// メンバ pillar(柱の種類) をデータストアに書き込む
    @Persistent 
    private String pillar;
    
	// メンバ pillarNum(柱の経過年数) をデータストアに書き込む
    @Persistent 
    private int pillarNum;

    // メンバ vin(ビニールの種類) をデータストアに書き込む
    @Persistent 
    private String vin;    
    
    // メンバ vinNum(ビニールの経過年数) をデータストアに書き込む
    @Persistent 
    private int vinNum;

    // メンバ zipCode1(郵便番号) をデータストアに書き込む
    @Persistent 
    private String zipCode1;

    // メンバ zipCode2(郵便番号) をデータストアに書き込む
    @Persistent 
    private String zipCode2;
    
    // メンバ address(住所) をデータストアに書き込む
    @Persistent 
    private String address;
     
    // メンバ date をデータストアに書き込む
    @Persistent 
    private Date date;

    public House(String mailId, String pillar, int pillarNum, String vin, int vinNum, String zipCode1, String zipCode2, String address, Date date) {
        this.mailId = mailId;
    	 this.pillar = pillar;
        this.pillarNum = pillarNum;
        this.vin = vin;
        this.vinNum = vinNum;
        this.zipCode1 = zipCode1;
        this.zipCode2 = zipCode2;
        this.address = address;
        this.date = date;
    }

    // データストアから呼び出し用のゲッター
    public String getId(){
    	return id;
    }
    public String getMailId() {
        return mailId;
    }
    public String getPillar() { // 柱の構成物
        return pillar;
    }
    public String getVin(){ // ビニールの種類
    	return vin;
    }
    public int getPillarNum(){ // 柱の経過年数
    	return pillarNum;
    }
    public int getVinNum(){ // ビニールの経過年数
    	return vinNum;
    }
    public String getZip1() {
        return zipCode1;
    }
    public String getZip2() {
        return zipCode2;
    }
    public String getAddress(){
    	return address;
    }
    public Date getDate() {
        return date;
    }
    // データストア書き換え用のセッター
    public void setDate(Date date) {
        this.date = date;
    }
    public void setPillar(String pillar) {
        this.pillar = pillar;
    }
    public void setPillarNum(int pillarNum) {
    	this.pillarNum = pillarNum;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }
    public void setVinNum(int vinNum) {
    	this.vinNum = vinNum;
    }
    public void setZip1(String zipCode1) {
        this.zipCode1 = zipCode1;
    }
    public void setZip2(String zipCode2) {
    	this.zipCode2 = zipCode2;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setAllData(String pillar, int pillarNum, String vin, int vinNum, String zipCode1, String zipCode2, String address){
        this.pillar = pillar;
        this.pillarNum = pillarNum;
        this.vin = vin;
        this.vinNum = vinNum;
        this.zipCode1 = zipCode1;
        this.zipCode2 = zipCode2;
        this.address = address;   	
    }

}
