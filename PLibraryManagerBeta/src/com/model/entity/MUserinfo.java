package com.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.model.tool.MTDataBaseTool;

public class MUserinfo {
	private String uid;
	private String uname;
	private String upwd;
	private String uright;
	
	
	/////
//	private int id ;
//	private String uid;
//	private String uname;
//	private String upwd ;
//	private String urole;
//	private String note;
//	private String img;
//	private String phone;
//	private String email;
	private MTDataBaseTool mtDBTool;
	//	含参构造函数;
	
	
	
	
//	public MUserinfo(String uid, String uname, String upwd,
//			String urole, String note, String img, String phone, String email) {
//		if(mtDBTool==null){
//			mtDBTool=new MTDataBaseTool();
//		}
//		this.uid = uid;
//		this.uname = uname;
//		this.upwd = upwd;
//		this.urole = urole;
//		this.note = note;
//		this.img = img;
//		this.phone = phone;
//		this.email = email;
//		
//	}
	//	构造函数;
	public MUserinfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	public MUserinfo(String uid, String uname, String upwd, String uright) {
		this.uid 	= uid;
		this.uname 	= uname;
		this.upwd 	= upwd;
		this.uright = uright;
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	插入用户信息的数据;
	public String insertUserinfo(){
		String sql=
		"insert into user_info (uid,uname,upwd,uright) values (" +
		"'"+this.uid+"','"+this.uname+"','"+this.upwd+"','"+this.uright+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//  修改用户信息
	public String updateUserinfo(String uid,String uname,String upwd,String uright){
		String sql=
		"update user_info set uname='"+uname+"',upwd='"+upwd+"',uright='"+uright+"' WHERE uid='"+uid+"'";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	public String checkUserinfo(String uid,String pwd){
		String 				sql	 = "select * from user_info where uid='"+uid+"' and upwd='"+pwd+"'";
		System.out.println(sql);
		ArrayList<String[]> list = mtDBTool.query(sql); 
		if(list!=null){			
			int 			nsize= list.size();
			if(nsize>0){
				String[] 	item = list.get(0);
				JSONArray   array= new JSONArray();
				JSONObject 	obj  = new JSONObject();
				try {
					obj.put("uid", item[0]);
					obj.put("uname", item[1]);
					obj.put("upwd", item[2]);
					obj.put("uright", item[3]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				array.add(obj);
				return array.toString();
			}
		}
		return "fail";
	}
	//	翻页显示信息;
	public String queryUserinfoByPageAndCondition(int nCurrentPage,int nCountLimit,String pkind,String value){
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String where=" where "+pkind+" like '%"+value+"%' limit "+nItem+","+nCountLimit;
		if(value.equals("null")){
			where=" limit "+nItem+","+nCountLimit;
		}
		String 				sql	 = "select * from user_info "+where;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("uid", items[0]);
						obj.put("uname", items[1]);
						obj.put("upwd", items[2]);
						obj.put("uright", items[3]);

					} catch (Exception e) {
						e.printStackTrace();
					}
					array.add(obj);
				}
				return array.toString();
			}
		}
		return "fail";
	}
	public String queryUserinfoItem(String uid){
		String 				sql	 = "select * from user_info where uid='"+uid+"'";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("uid", items[0]);
						obj.put("uname", items[1]);
						obj.put("upwd", items[2]);
						obj.put("uright", items[3]);

					} catch (Exception e) {
						e.printStackTrace();
					}
					array.add(obj);
				}
				return array.toString();
			}
		}
		return "fail";
	}
	public String queryUserinfoItem2(String uid){
		String 				sql	 = "select * from user_info where uid='"+uid+"'";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("uid", items[0]);
						obj.put("uname", items[1]);
						obj.put("upwd", items[2]);
						obj.put("uright", items[3]);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					array.add(obj);
				}
				return array.toString();
			}
		}
		return "fail";
	}
	public String delAll(){
		String sql="delete from user_info";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	
	public String delItem(String uid){
		String sql="delete from user_info where uid='"+uid+"'";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	/**
	 * 
	 * @return
	 */
	public String checkUserinfo(){
		String 				sql	 = "select * from user_info ";
		ArrayList<String[]> list = mtDBTool.query(sql); 
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("uid", items[0]);
						obj.put("uname", items[1]);
						obj.put("upwd", items[2]);
						obj.put("uright", items[3]);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					array.add(obj);
				}
				return array.toString();
			}
		}
		return "fail";
	}
	//数据的属性的抓取;

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpwd() {
		return upwd;
	}
	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}

	public String getUright() {
		return uright;
	}
	public void setUright(String uright) {
		this.uright = uright;
	}
//	public MTDataBaseTool getMtDBTool() {
//		return mtDBTool;
//	}
//	public void setMtDBTool(MTDataBaseTool mtDBTool) {
//		this.mtDBTool = mtDBTool;
//	}
	
}
