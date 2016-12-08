package com.Delete;


import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.Login.PMF;
import com.Data.Amedas;

public class DelAmedas extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        
        PersistenceManager pm = null;
        try {
            pm = PMF.get().getPersistenceManager();
            Query query = pm.newQuery(Amedas.class);
            List<Amedas> amedases = (List<Amedas>) query.execute();

            pm.deletePersistentAll(amedases);
       
        } finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }

        resp.sendRedirect("./");
        }
}

