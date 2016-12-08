package com.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Account {
	// ------------------------------

	@PrimaryKey
	@Persistent
	private String mailId; 
	
	@Persistent
	private List<String> mailList = new ArrayList<String>(); 

	@Persistent
	private String password; // String

    // メンバ date をデータストアに書き込む
    @Persistent 
    private Date date;
	// ------------------------------
	// コンストラクタ
	// ------------------------------
	public Account(String mailId, String password, Date date) {
		this.mailId = mailId;
		this.password = password;
		this.date = date;
	}

	// ------------------------------
	// セッター
	// ------------------------------

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	
	public void setMailList( String mailaddress ) {
		this.mailList.add(mailaddress);
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	// ------------------------------
	// ゲッター
	// ------------------------------

	public String getMailId() {
		return this.mailId;
	}
	
	public List<String> getMailList() {
		return this.mailList;
	}

	public String getPassword() {
		return this.password;
	}
}
