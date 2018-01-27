package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.model.tool.MTDataBaseTool;

public class MIteminfo {
	private int id ;
	private String iid;
	private String iname;
	private String note;
	private String author;
	private String press;
	private String ptime;
	private String count;
	private String kid;
	private String img;
	private MTDataBaseTool mtDBTool;

	//	含参构造函数;
	public MIteminfo(String iid, String iname, String note, String author,
			String press, String ptime, String count, String kid, String img
			) {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
		this.iid = iid;
		this.iname = iname;
		this.note = note;
		this.author = author;
		this.press = press;
		this.ptime = ptime;
		this.count = count;
		this.kid = kid;
		this.img = img;
	}
	//	构造函数;
	public MIteminfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}	
	//	插入用户信息的数据;
	public String insertIteminfo(){
		String sql=
		"insert into item_book_info (iid,iname,note,author,press,ptime,count,kid,img) values (" +
		"'"+this.iid+"','"+this.iname+"','"+this.note+"','"+this.author+"','"+this.press+"','"+this.ptime+"','"+this.count+"','"+this.kid+"','"+this.img+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//	翻页显示信息;
	public String queryIteminfoByPageAndCondition(int nCurrentPage,int nCountLimit,String pkind,String value){
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String where=" where "+pkind+" like '%"+value+"%' limit "+nItem+","+nCountLimit;
		
		if(value.equals("null")){
			where=" limit "+nItem+","+nCountLimit;
		}
		String 				sql	 = "select * from item_book_info "+where;
		System.out.println(sql);
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		System.out.println(list.toString());
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("iid", items[1]);
						obj.put("iname", items[2]);
						obj.put("note", items[3]);
						obj.put("author", items[4]);
						obj.put("press", items[5]);
						obj.put("ptime", items[6]);
						obj.put("count", items[7]);
						obj.put("kid", items[8]);
						obj.put("img", items[9]);
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
	//  显示所有图书
	public String queryIteminfoItem(int nCurrentPage,int nCountLimit) {
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String 				sql	 = "select * from item_book_info limit "+nItem+","+nCountLimit;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("iid", items[1]);
						obj.put("iname", items[2]);
						obj.put("note", items[3]);
						obj.put("author", items[4]);
						obj.put("press", items[5]);
						obj.put("ptime", items[6]);
						obj.put("count", items[7]);
						obj.put("kid", items[8]);
						obj.put("img", items[9]);
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
	public String queryItemNum(){
		String 				sql	 = "select count(*) as number from item_book_info";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("number", items[0]);
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
	public String queryItemNum(String pkind,String value){
		String where=" where "+pkind+" like '%"+value+"%'";
		String 				sql	 = "select count(*) as number from item_book_info"+where;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("number", items[0]);
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
	public String queryIteminfoItem(String id){
		String 				sql	 = "select * from item_book_info where id="+id;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("iid", items[1]);
						obj.put("iname", items[2]);
						obj.put("note", items[3]);
						obj.put("author", items[4]);
						obj.put("press", items[5]);
						obj.put("ptime", items[6]);
						obj.put("count", items[7]);
						obj.put("kid", items[8]);
						obj.put("img", items[9]);
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
	public String queryIteminfoItem2(String iid){
		System.out.println(iid);
		
		String 				sql	 = "select item_book_info.*,ifnull(aa.state,'归还') as state from item_book_info left join (select state,iid from borrow_info where state='借出') aa on item_book_info.iid=aa.iid where item_book_info.iid='"+iid+"'";
		System.out.println(sql);
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("iid", items[1]);
						obj.put("iname", items[2]);
						obj.put("note", items[3]);
						obj.put("author", items[4]);
						obj.put("press", items[5]);
						obj.put("ptime", items[6]);
						obj.put("count", items[7]);
						obj.put("kid", items[8]);
						obj.put("img", items[9]);
						obj.put("state", items[10]);
					} catch (Exception e) {
						e.printStackTrace();
					} 
					array.add(obj);
					System.out.println(array.toString());
				}
				return array.toString();
			}
		}
		return "fail";
	}
	public String delAll(){
		String sql="delete from item_book_info";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	
	public String delItem(String id){
		String sql="delete from item_book_info where id="+id;
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//数据的属性的抓取;	
	public MTDataBaseTool getMtDBTool() {
		return mtDBTool;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIid() {
		return iid;
	}
	public void setIid(String iid) {
		this.iid = iid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getPtime() {
		return ptime;
	}
	public void setPtime(String ptime) {
		this.ptime = ptime;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public void setMtDBTool(MTDataBaseTool mtDBTool) {
		this.mtDBTool = mtDBTool;
	}
	
}
