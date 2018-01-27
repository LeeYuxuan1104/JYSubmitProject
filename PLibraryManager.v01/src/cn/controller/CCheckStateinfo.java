package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.MStateinfo;

public class CCheckStateinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		///
		PrintWriter 	pWriter = 	resp.getWriter();
		MStateinfo		stateinfo=null;
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   "fail";
		String 			id,iid,count,ccount,ebids;
		///
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
			stateinfo		=	new MStateinfo();
			sResult =	stateinfo.queryStateinfoByPageAndCondition(nCurrentPage, nCountLimit, pkind, value);
			break;
			
		///	清空操作;
		case 4:
			stateinfo=new MStateinfo();
			sResult =	stateinfo.delAll();
			break;
			
		///	单条删除;
		case 5:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			stateinfo=new MStateinfo(); 
			sResult =	stateinfo.delItem(id);
			break;
			
		///	单条新增;
		case 6:
			iid		= 	new String(req.getParameter("iid").getBytes("ISO8859_1"),"utf-8");
			count	= 	new String(req.getParameter("count").getBytes("ISO8859_1"),"utf-8");
			ccount	= 	new String(req.getParameter("ccount").getBytes("ISO8859_1"),"utf-8");
			ebids	= 	new String(req.getParameter("ebids").getBytes("ISO8859_1"),"utf-8");
			stateinfo=new MStateinfo(iid, count, ccount, ebids);
			sResult =	stateinfo.insertStateinfo();
			break;
		///	单条查询;
		case 7:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			stateinfo=new MStateinfo(); 
			sResult =	stateinfo.queryStateinfoItem(id);
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
