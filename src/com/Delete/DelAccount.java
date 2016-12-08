package com.Delete;


import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.Account;
import com.Data.House;

public class DelAccount extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(Account.class);
            List<Account> accounts = (List<Account>) query.execute();

            pm.deletePersistentAll(accounts);
            
            query = pm.newQuery(House.class);  			
            List<House> houses = (List<House>) query.execute();
           
            pm.deletePersistentAll(houses);
       
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }

        resp.sendRedirect("./");
        }
}

