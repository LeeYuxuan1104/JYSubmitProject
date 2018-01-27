package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.MIteminfo;
import cn.model.entity.MStateinfo;
import cn.model.tool.MTConfig;

public class CCheckIteminfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		MIteminfo		iteminfo=	null;
		MStateinfo		stateinfo=	null;
		MTConfig		mtConfig=	null;
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   "fail";
		String 			id,iid,iname,note,author,press,ptime,count,kid,img=null;
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
			iteminfo=	new MIteminfo();
			sResult =	iteminfo.queryIteminfoByPageAndCondition(nCurrentPage, nCountLimit, pkind, value);
			System.out.println("s="+sResult);
			break;
		///	清空操作;
		case 4:
			iteminfo=	new MIteminfo();
			sResult =	iteminfo.delAll();
			break;
		///	单条删除;
		case 5:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new	MIteminfo(); 
			sResult =	iteminfo.delItem(id);
			break;
		///	单条新增;
		case 6:
			iid		= 	new String(req.getParameter("iid").getBytes("ISO8859_1"),"utf-8");
			iname	=	new String(req.getParameter("iname").getBytes("ISO8859_1"),"utf-8");
			note	=	new String(req.getParameter("note").getBytes("ISO8859_1"),"utf-8");
			
			author	=	new String(req.getParameter("author").getBytes("ISO8859_1"),"utf-8");
			press	=	new String(req.getParameter("press").getBytes("ISO8859_1"),"utf-8");
			ptime	=	new String(req.getParameter("ptime").getBytes("ISO8859_1"),"utf-8");
			count	=	new String(req.getParameter("count").getBytes("ISO8859_1"),"utf-8");
			kid		=	new String(req.getParameter("kid").getBytes("ISO8859_1"),"utf-8");
			img		=	new String(req.getParameter("img").getBytes("ISO8859_1"),"utf-8");
			
			iteminfo=   new MIteminfo(iid, iname, note, author, press, ptime, count, kid, img);
			stateinfo=	new MStateinfo(iid, count, count, "null");
			stateinfo.insertStateinfo();
			sResult =	iteminfo.insertIteminfo();

			break;
		case 7:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new	MIteminfo(); 
			sResult =	iteminfo.queryIteminfoItem(id);
			break;
		//	照片;
		case 8:
			iid		= 	new String(req.getParameter("iid").getBytes("ISO8859_1"),"utf-8");
			mtConfig=	new MTConfig();
			sResult	=	mtConfig.uploadMap(req, "item", iid);
			break;
		//	根据iid查询;
		case 9:
			iid		=	new String(req.getParameter("iid").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new	MIteminfo(); 
			sResult =	iteminfo.queryIteminfoItem2(iid);
			break;
		case 10:
			///	显示前10条信息;
			nCurrentPage=Integer.parseInt(req.getParameter("currentpage"));
			nCountLimit =Integer.parseInt(req.getParameter("countlimit"));
			//String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			//String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new MIteminfo();
			sResult =	iteminfo.queryIteminfoItem(nCurrentPage,nCountLimit);
			break;
		case 11:
			///	显示书籍总数;
			//String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			//String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new MIteminfo();
			sResult =	iteminfo.queryItemNum();
			System.out.println("s="+sResult);
			break;
		case 12:
			///	显示书籍总数;
			pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			iteminfo=	new MIteminfo();
			sResult =	iteminfo.queryItemNum(pkind,value);
			System.out.println("s="+sResult);
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
