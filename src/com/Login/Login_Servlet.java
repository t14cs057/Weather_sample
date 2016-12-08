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
import com.System.ReadFile;
//一時的に
import com.Data.Amedas;

@SuppressWarnings("serial")
public class Login_Servlet extends HttpServlet {	
	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException,ServletException {
		resp.setContentType("text/html;charset=UTF-8");

		 String mail = req.getParameter("mail");
		 String pass = req.getParameter("pass");
	   
		 PersistenceManager pm = PMF.get().getPersistenceManager();
		 
		 int flag = 0, error = 0;

		 // error
		 if(mail.equals("")){
			 error += 1;
		 }
		 if(!mail.matches("[\\w\\.\\-]+@(?:[\\w\\-]+\\.)+[\\w\\-]+")){
			 error += 8;
		 }
		 if(pass.equals("")){
			 error += 2;
		 }
		    /*if(name.equals("")){
		    	error += 4;
		    }*/
		    
		 if(error != 0){
			 resp.sendRedirect("./login.jsp?Error=" + String.valueOf(error));
			 return;
		 }
		//flag = 3; //@ とりあえず 2013/12/5

	    // クエリを作成 
		 List<Account> accounts = (List<Account>) pm.newQuery(Account.class).execute();

	   HttpSession session = req.getSession(false);

	   for(Account acc : accounts){
		   // アカウントとパスワードが一致したならば
		   if(mail.equals(acc.getMailId())){
			   if( pass.equals(acc.getPassword()) ){
				   session = req.getSession(true);
				   flag = 1;
				   session.setAttribute("access", 		String.valueOf(flag));
				   session.setAttribute("mailId", 		acc.getMailId()); 
				   session.setAttribute("password", 	acc.getPassword());
				   // ファイルを読み込む(非常に重い処理)
				   new ReadFile();
				   break;
			   }else{
				  error += 32;
				  break;
			   }
		   	}else{
		   		flag = 3;
		   	}
	   }
	   
	   if( flag == 1 ){
		   resp.sendRedirect("./registerHouse.jsp");
	   }else{
		   if( flag == 3 && ( error & 32 ) != 32 )
			   error += 16;
		   resp.sendRedirect("./login.jsp?Error=" + String.valueOf(error));		   
	   }
	}

}