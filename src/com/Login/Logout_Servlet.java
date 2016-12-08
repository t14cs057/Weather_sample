// Login処理を行うサーブレット
package com.Login;

import java.io.*;
//import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.servlet.*;
import javax.servlet.http.*;
//import com.DataClasses.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.Data.Account;

@SuppressWarnings("serial")
public class Logout_Servlet extends HttpServlet {	
	@Override
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException,ServletException {
		resp.setContentType("text/html;charset=UTF-8");

		PrintWriter out = resp.getWriter();
		HttpSession session = req.getSession(true);
		session.invalidate();
		
		resp.sendRedirect("./login.jsp");		   
	}

}