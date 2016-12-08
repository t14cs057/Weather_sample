package com.System;


import java.io.IOException;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.*;
import java.util.*;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.House;

public class EditHouse extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		String mailId = req.getParameter("mailId");
		int i = 0;
    	PersistenceManager pm = null;
  		try {
			pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(House.class);
			query.setOrdering("date asc");
			query.setFilter("mailId == id");
			query.declareParameters("String id");
			
			List<House> houses = (List<House>) query.execute(mailId);
  		
		// ハウス情報の登録
		for( House house : houses ){
			String zip1 = req.getParameter("zipCode1" + i);
			String zip2 = req.getParameter("zipCode2" + i);
			String address = req.getParameter("addr" + i);
			String pillar = req.getParameter("pillar" + i);
			String vin = req.getParameter("vin" + i);
			// 郵便番号が数字の3桁,4桁の組み合わせではなかった,住所が空白だったときの処理
			if( !zip1.matches("\\d{3}") || !zip2.matches("\\d{4}") || address.equals("")){
				resp.sendRedirect("/HouseInfo.jsp");
				return;
			}
			
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

			int pNum = Integer.parseInt(req.getParameter("pillarNum" + i));
			int vNum = Integer.parseInt(req.getParameter("vinNum" + i));
			
			house.setAllData(pillar, pNum, vin, vNum, zip1, zip2, address);
			
			i++;
		}
		
  		}finally {
			pm.close();
		}

		resp.sendRedirect("/HouseInfo.jsp");
	    
    }
}
