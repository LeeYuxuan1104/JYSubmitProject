package cn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.model.entity.MUserinfo;
import cn.model.tool.MTConfig;

public class CCheckUserinfo extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/html;charset=utf-8");
		/////
		PrintWriter 	pWriter = 	resp.getWriter();
		MUserinfo		userinfo=	null;
		MTConfig		mtConfig=	null;
		int 			operType=	Integer.parseInt(req.getParameter("opertype"));
		String  		sResult =   "fail";
		String 			id,uid,uname,upwd,urole,note,img=null,phone,email;
		System.out.println("oper="+operType);
		/////
		switch (operType) {
		///	注册;
		case 1:
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			uname	=	new String(req.getParameter("uname").getBytes("ISO8859_1"),"utf-8");
			upwd	=	new String(req.getParameter("upwd").getBytes("ISO8859_1"),"utf-8");
			
			urole	=	new String(req.getParameter("urole").getBytes("ISO8859_1"),"utf-8");
			note	=	new String(req.getParameter("note").getBytes("ISO8859_1"),"utf-8");
			phone	=	new String(req.getParameter("phone").getBytes("ISO8859_1"),"utf-8");
			email	=	new String(req.getParameter("email").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo(uid, uname, upwd, urole, note, img, phone, email);
			sResult = 	userinfo.insertUserinfo();
			
			break;
		///	登录;
		case 2:
			System.out.println("登录");
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			upwd	=	new String(req.getParameter("upwd").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult =   userinfo.checkUserinfo(uid, upwd);
			break;
		///	显示前10条信息;
		case 3:
			int nCurrentPage=Integer.parseInt(req.getParameter("currentpage"));
			int nCountLimit =Integer.parseInt(req.getParameter("countlimit"));
			String pkind	=new String(req.getParameter("pkind").getBytes("ISO8859_1"),"utf-8");
			String value	=new String(req.getParameter("value").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult =	userinfo.queryUserinfoByPageAndCondition(nCurrentPage, nCountLimit, pkind, value);
			break;
		///	清空操作;
		case 4:
			userinfo=	new MUserinfo();
			sResult =	userinfo.delAll();
			break;
			
		///	单条删除;
		case 5:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult =	userinfo.delItem(id);
			break;
		///	数据新增;
		case 6:
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			uname	=	new String(req.getParameter("uname").getBytes("ISO8859_1"),"utf-8");
			upwd	=	new String(req.getParameter("upwd").getBytes("ISO8859_1"),"utf-8");
			
			urole	=	new String(req.getParameter("urole").getBytes("ISO8859_1"),"utf-8");
			note	=	new String(req.getParameter("note").getBytes("ISO8859_1"),"utf-8");
			img		=	new String(req.getParameter("img").getBytes("ISO8859_1"),"utf-8");
			phone	=	new String(req.getParameter("phone").getBytes("ISO8859_1"),"utf-8");
			email	=	new String(req.getParameter("email").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo(uid, uname, upwd, urole, note, img, phone, email);
			sResult = 	userinfo.insertUserinfo();
			break;
			
		///	单条查询;
		case 7:
			id		=	new String(req.getParameter("id").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult =	userinfo.queryUserinfoItem(id);
			break;
			
		///	图片上传;
		case 8:
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			mtConfig=	new MTConfig();
			sResult	=	mtConfig.uploadMap(req, "user", uid);
			break;
		/// uid查询;
		case 9:
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult	=	userinfo.queryUserinfoItem2(uid);
			break;
		/// 信息修改
		case 10:
			uid		= 	new String(req.getParameter("uid").getBytes("ISO8859_1"),"utf-8");
			phone		= 	new String(req.getParameter("phone").getBytes("ISO8859_1"),"utf-8");
			email		= 	new String(req.getParameter("email").getBytes("ISO8859_1"),"utf-8");
			note		= 	new String(req.getParameter("note").getBytes("ISO8859_1"),"utf-8");
			userinfo=	new MUserinfo();
			sResult	=	userinfo.updateUserinfo(uid, phone, email, note);
			break;
		///	显示所有用户信息;
		case 11:
			userinfo=	new MUserinfo();
			sResult =	userinfo.checkUserinfo();
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
