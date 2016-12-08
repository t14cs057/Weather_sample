package com.NewServlet;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.Account;
import com.Data.House;

public class NewHouse extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		String s = req.getParameter("count");
		String str = req.getParameter("size");
		String mailId = req.getParameter("mailId");
		String addMail = req.getParameter("addMail");
		// ハウスの個数
		int count = Integer.parseInt(s);

		int size = 0;
		if( str != null )
			size = Integer.parseInt(str);

		long errCount = 1, error = 0;

		// ハウス情報の登録
		for(int i = size; i < size+count; i++){
			String zip1 = req.getParameter("zipCode1" + i);
			String zip2 = req.getParameter("zipCode2" + i);
			String address = req.getParameter("addr" + i);
			String pillar = req.getParameter("pillar" + i);
			String vin = req.getParameter("vin" + i);

			String pillarNum = req.getParameter("pillarNum" + i);
			String vinNum = req.getParameter("vinNum" + i);

			if(zip1.equals("") || zip2.equals("") || !zip1.matches("\\d\\d\\d") || !zip2.matches("\\d\\d\\d\\d"))
				error += 1 * (exponential2(i)) * errCount;
			if(!address.matches("[.\\s\\S]+?[都道府県][.\\s\\S]+?"))
				error += 2 * (exponential2(i)) * errCount;
			if(pillar.equals(""))
				error += 4 * (exponential2(i)) * errCount;
			if(vin.equals(""))
				error += 8 * (exponential2(i)) * errCount;
			if(pillarNum.equals(""))
				error += 16 * (exponential2(i)) * errCount;
			if(vinNum.equals(""))
				error += 32 * (exponential2(i)) * errCount;

			errCount = exponential32( i + 1 ); 
		}

		if(error != 0){
			resp.sendRedirect("./registerHouse.jsp?Count=" + String.valueOf(count) + "&Error=" + String.valueOf(error));
			return;
		}

		// ハウス情報の登録
		for(int i = size; i < size+count; i++){
			String zip1 = req.getParameter("zipCode1" + i);
			String zip2 = req.getParameter("zipCode2" + i);
			String address = req.getParameter("addr" + i);
			String pillar = req.getParameter("pillar" + i);
			String vin = req.getParameter("vin" + i);

			String pillarNum = req.getParameter("pillarNum" + i);
			String vinNum = req.getParameter("vinNum" + i);

			if(pillar.equals("pillar1"))
				pillar = "鉄";
			else if(pillar.equals("pillar2"))
				pillar = "木";
			else if(pillar.equals("pillar3"))
				pillar = "その他";

			if(vin.equals("vin1"))
				vin = "ポリオレフィン系フィルム";
			else if(vin.equals("vin2"))
				vin ="その他";

			int pNum = Integer.parseInt(pillarNum);
			int vNum = Integer.parseInt(vinNum);

			House house = new House(mailId, pillar, pNum, vin, vNum, zip1, zip2,address, new Date());

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.makePersistent(house); // House カインドにエンティティを追加
			} finally {
				pm.close();
			}
		}

		if( addMail != null && addMail.matches("[\\w\\-]+@(?:[\\w\\-]+\\.)+[\\w\\-]+")){
			PersistenceManager pm = null;
			pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(Account.class);
			query.setFilter("mailId == " + "'" + mailId + "'");

			List<Account> accounts = (List<Account>) query.execute();

			for( Account acc : accounts )
				acc.setMailList(addMail);
			pm.close();
		}

		resp.sendRedirect("/HouseInfo.jsp?Count=" + String.valueOf(count));
	}

	private long exponential32( int freq ){
		long number = 1;

		for( int i = 0; i < freq; i++ )
			number *= 32;

		return number;
	}

	private long exponential2( int freq ){
		long number = 1;

		for( int i = 0; i < freq; i++ )
			number *= 2;

		return number;
	}

}