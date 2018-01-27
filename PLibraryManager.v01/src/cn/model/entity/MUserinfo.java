package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.model.tool.MTDataBaseTool;

public class MUserinfo {
	private int id ;
	private String uid;
	private String uname;
	private String upwd ;
	private String urole;
	private String note;
	private String img;
	private String phone;
	private String email;
	private MTDataBaseTool mtDBTool;
	//	含参构造函数;
	public MUserinfo(String uid, String uname, String upwd,
			String urole, String note, String img, String phone, String email) {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
		this.uid = uid;
		this.uname = uname;
		this.upwd = upwd;
		this.urole = urole;
		this.note = note;
		this.img = img;
		this.phone = phone;
		this.email = email;
		
	}
	//	构造函数;
	public MUserinfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	插入用户信息的数据;
	public String insertUserinfo(){
		String sql=
		"insert into user_info (uid,uname,upwd,urole,note,img,phone,email) values (" +
		"'"+this.uid+"','"+this.uname+"','"+this.upwd+"','"+this.urole+"','"+this.note+"','"+this.img+"','"+this.phone+"','"+this.email+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//  修改用户信息
	public String updateUserinfo(String uid,String phone,String email,String note){
		String sql=
		"update user_info set phone='"+phone+"',email='"+email+"',note='"+note+"' WHERE uid='"+uid+"'";
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
					obj.put("uid", item[1]);
					obj.put("uname", item[2]);
					obj.put("upwd", item[3]);
					obj.put("urole", item[4]);
					obj.put("note", item[5]);
					obj.put("img", item[6]);
					obj.put("phone", item[7]);
					obj.put("email", item[8]);
					obj.put("id", item[0]);
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
						obj.put("id", items[0]);
						obj.put("uid", items[1]);
						obj.put("uname", items[2]);
						obj.put("upwd", items[3]);
						obj.put("urole", items[4]);
						obj.put("note", items[5]);
						obj.put("img", items[6]);
						obj.put("phone", items[7]);
						obj.put("email", items[8]);
						
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
	public String queryUserinfoItem(String id){
		String 				sql	 = "select * from user_info where id="+id;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("uid", items[1]);
						obj.put("uname", items[2]);
						obj.put("upwd", items[3]);
						obj.put("urole", items[4]);
						obj.put("note", items[5]);
						obj.put("img", items[6]);
						obj.put("phone", items[7]);
						obj.put("email", items[8]);
						
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
						obj.put("id", items[0]);
						obj.put("uid", items[1]);
						obj.put("uname", items[2]);
						obj.put("upwd", items[3]);
						obj.put("urole", items[4]);
						obj.put("note", items[5]);
						obj.put("img", items[6]);
						obj.put("phone", items[7]);
						obj.put("email", items[8]);
						
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
	
	public String delItem(String id){
		String sql="delete from user_info where id="+id;
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
						obj.put("id", items[0]);
						obj.put("uid", items[1]);
						obj.put("uname", items[2]);
						obj.put("upwd", items[3]);
						obj.put("urole", items[4]);
						obj.put("note", items[5]);
						obj.put("img", items[6]);
						obj.put("phone", items[7]);
						obj.put("email", items[8]);
						
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getUrole() {
		return urole;
	}
	public void setUrole(String urole) {
		this.urole = urole;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public MTDataBaseTool getMtDBTool() {
		return mtDBTool;
	}
	public void setMtDBTool(MTDataBaseTool mtDBTool) {
		this.mtDBTool = mtDBTool;
	}
	
}
