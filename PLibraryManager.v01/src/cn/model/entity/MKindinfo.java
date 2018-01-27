package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.model.tool.MTDataBaseTool;

public class MKindinfo {
	private int id ;
	private String kid;
	private String kname;
	private String note;
	
	private MTDataBaseTool mtDBTool;
	//	含参构造函数;
	public MKindinfo(String kid, String kname,String note) {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
		this.kid = kid;
		this.kname = kname;
		this.note = note;

	}
	//	构造函数;
	public MKindinfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	插入用户信息的数据;
	public String insertKindinfo(){
		String sql=
		"insert into kind_book_info (kid,kname,note) values (" +
		"'"+this.kid+"','"+this.kname+"','"+this.note+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//	翻页显示信息;
	public String queryKindinfoByPageAndCondition(int nCurrentPage,int nCountLimit,String pkind,String value){
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String where=" where "+pkind+" like '%"+value+"%' limit "+nItem+","+nCountLimit;
		if(value.equals("null")){
			where=" limit "+nItem+","+nCountLimit;
		}
		String 				sql	 = "select * from kind_book_info "+where;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("kid", items[1]);
						obj.put("kname", items[2]);
						obj.put("note", items[3]);
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
	//	翻页显示信息;
	public String queryKindinfoItem(String id){
		String 				sql	 = "select * from kind_book_info where id="+id;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("kid", items[1]);
						obj.put("kname", items[2]);
						obj.put("note", items[3]);
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
	public String queryKindinfoAll(){
		String 				sql	 = "select * from kind_book_info";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("kid", items[1]);
						obj.put("kname", items[2]);
						obj.put("note", items[3]);
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
		String sql="delete from kind_book_info";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	
	public String delItem(String id){
		String sql="delete from kind_book_info where id="+id;
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
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
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getKname() {
		return kname;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public MTDataBaseTool getMtDBTool() {
		return mtDBTool;
	}
	public void setMtDBTool(MTDataBaseTool mtDBTool) {
		this.mtDBTool = mtDBTool;
	}
	
}
