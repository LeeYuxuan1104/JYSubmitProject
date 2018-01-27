package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.model.entity.MKindinfo;

public class CCheckKindinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		MKindinfo		kindinfo=	null;
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   "fail";
		String 			id,kid,kname,note;
		/////
		switch (operType) {
		///	显示前10条信息;
		case 1:
		case 2:
		case 3:
			int nCurrentPage=Integer.parseInt(req.getParameter("currentpage"));
			int nCountLimit =Integer.parseInt(req.getParameter("countlimit"));
			String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			kindinfo=	new MKindinfo();
			sResult =	kindinfo.queryKindinfoByPageAndCondition(nCurrentPage, nCountLimit, pkind, value);
			break;
		///	清空操作;
		case 4:
			kindinfo=	new MKindinfo();
			sResult =	kindinfo.delAll();
			break;
		///	单条删除;
		case 5:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			kindinfo=	new MKindinfo();
			sResult =	kindinfo.delItem(id);
			break;
		///	 新增;
		case 6:
			kid		= 	new String(req.getParameter("kid").getBytes("ISO8859_1"),"utf-8");
			kname	=	new String(req.getParameter("kname").getBytes("ISO8859_1"),"utf-8");
			note	=	new String(req.getParameter("note").getBytes("ISO8859_1"),"utf-8");
			kindinfo=new MKindinfo(kid, kname, note);
			sResult = 	kindinfo.insertKindinfo();
			break;
		///	单条查询
		case 7:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			kindinfo=	new MKindinfo();
			sResult =	kindinfo.queryKindinfoItem(id);
			break;
		///	全部查询
		case 8:
			kindinfo=	new MKindinfo();
			sResult =	kindinfo.queryKindinfoAll();
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
