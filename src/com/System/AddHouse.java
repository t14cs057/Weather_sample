package com.System;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.House;

public class AddHouse extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String SIZE = req.getParameter("SIZE");

		if( SIZE != null ){
			int s = Integer.parseInt(SIZE);
			if( s != 0 ){
				resp.sendRedirect("/HouseINfo.jsp");
				return;
			}
		}

		String countString = req.getParameter("count");
		String mailId = req.getParameter("mailId");
		String str = req.getParameter("size");
		String add = req.getParameter("add");

		int count = 0, size = 0;
		if( countString != null )
			count = Integer.parseInt(countString) + 1;

		if( str != null )
			size = Integer.parseInt(str);

		if( count >= 11 ){
			count--;
			resp.sendRedirect("/registerHouse.jsp?Count=" + String.valueOf(count) );
			return;
		}

		if( str != null ){
			size = Integer.parseInt(str);
		}

		if( ( size + count ) >= 11 ){
			count--;
			resp.sendRedirect("/addHouse.jsp?Count=" + String.valueOf(count) + "&Size=" + String.valueOf(size) );
			return;
		}

		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(House.class);
			query.setOrdering("date asc");
			query.setFilter("mailId == id");
			query.declareParameters("String id");

			List<House> houses = (List<House>) query.execute(mailId);
			count -= houses.size();
		}finally{
			pm.close();
		}

		if( count != 0 && add == null ){
			resp.sendRedirect("./registerHouse.jsp?Count=" + String.valueOf(count));
			return;
		}else {
			count = Integer.parseInt(countString) + 1;
			resp.sendRedirect("./addHouse.jsp?Count=" + String.valueOf(count) + "&Size=" + String.valueOf(size) );
			return;
		}
	}
}