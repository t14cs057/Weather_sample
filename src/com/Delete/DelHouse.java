package com.Delete;


import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.House;

public class DelHouse extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
    	
         String houseId = null;
         String sizeStr = 	req.getParameter("Housesize");
         int size = Integer.parseInt(sizeStr);
         int i = 0;
         for( i = 0; i < size; i ++ ){
        	 houseId = req.getParameter("houseId" + i);
        	 String delHouse =  req.getParameter("num" + i);
        	 if( delHouse != null )
        		 break;
         }
        String mailId = req.getParameter("mailId");
        //int num = Integer.parseInt(req.getParameter("num"));
        int num = 0;
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(House.class);
            
            query.setFilter("id ==" + "'" + houseId + "'");
            
            List<House> houses = (List<House>) query.execute();

            pm.deletePersistentAll(houses);

        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }

        resp.sendRedirect("./HouseInfo.jsp?Num=" + String.valueOf(num));
        }
}

