package com.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model.entity.MBookinfo;


public class CCheckBookinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		MBookinfo		bookinfo=	null;
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   "fail";
		String 			id,bid,bname,author,press,state,number=null;
		/////
		switch (operType) {
		///	注册;
		case 1:

			break;
		///	登录;
		case 2:

			break;
		///	显示前10条信息;
		case 3:
			int nCurrentPage=Integer.parseInt(req.getParameter("currentpage"));
			int nCountLimit =Integer.parseInt(req.getParameter("countlimit"));
			String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			bookinfo		=	new MBookinfo();
			sResult =	bookinfo.queryIteminfoByPageAndCondition(nCurrentPage, nCountLimit, pkind, value);
//			System.out.println("s1="+sResult);
			break;
		///	清空操作;
		case 4:
			bookinfo=	new MBookinfo();
			sResult =	bookinfo.delAll();
			break;
		///	单条删除;
		case 5:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new	MBookinfo(); 
			sResult =	bookinfo.delItem(id);
			break;
		///	单条新增;
		case 6:
			bid		= 	new String(req.getParameter("bid").getBytes("ISO8859_1"),"utf-8");
			bname	=	new String(req.getParameter("bname").getBytes("ISO8859_1"),"utf-8");
			
			author	=	new String(req.getParameter("author").getBytes("ISO8859_1"),"utf-8");
			press	=	new String(req.getParameter("press").getBytes("ISO8859_1"),"utf-8");
			state	=	new String(req.getParameter("state").getBytes("ISO8859_1"),"utf-8");
			number	=	new String(req.getParameter("number").getBytes("ISO8859_1"),"utf-8");
	
			bookinfo=	new MBookinfo(bid, bname, author, press, state, number);
		
			
			sResult =	bookinfo.insertIteminfo();

			break;
		case 7:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new	MBookinfo(); 
			sResult =	bookinfo.queryIteminfoItem(id);
			break;
		//	照片;
		case 8:
//			iid		= 	new String(req.getParameter("iid").getBytes("ISO8859_1"),"utf-8");
//			mtConfig=	new MTConfig();
//			sResult	=	mtConfig.uploadMap(req, "item", iid);
			break;
		//	根据iid查询;
		case 9:
			bid		=	new String(req.getParameter("bid").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new	MBookinfo(); 
			sResult =	bookinfo.queryIteminfoItem2(bid);
			break;
		case 10:
			///	显示前10条信息;
			nCurrentPage=Integer.parseInt(req.getParameter("currentpage"));
			nCountLimit =Integer.parseInt(req.getParameter("countlimit"));
			//String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			//String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new MBookinfo();
			sResult =	bookinfo.queryIteminfoItem(nCurrentPage,nCountLimit);
			break;
		case 11:
			///	显示书籍总数;
			//String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			//String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new MBookinfo();
			sResult =	bookinfo.queryItemNum();
//			System.out.println("s="+sResult);
			break;
		case 12:
			///	显示书籍总数;
			pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			bookinfo=	new MBookinfo();
			sResult =	bookinfo.queryItemNum(pkind,value);
//			System.out.println("s="+sResult);
			break;
		default:
			break;
		}

		pWriter.print(sResult)	;
		pWriter.flush();
		pWriter.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
