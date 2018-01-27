package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.model.tool.MTDataBaseTool;

public class MStateinfo {
	private int id ;
	private String iid;
	private String count;
	private String ccount;
	private String ebids;
	
	private MTDataBaseTool mtDBTool;
		
	//	含参构造函数;
	public MStateinfo(String iid, String count, String ccount, String ebids) {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}
		this.iid = iid;
		this.count = count;
		this.ccount = ccount;
		this.ebids = ebids;
	}

	//	构造函数;
	public MStateinfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	查询书目的总数;
	private int queryCount(String field,String iid){
		int result=0;
		String sql="select "+field+" from book_state_info where iid='"+iid+"'";
		ArrayList<String[]> list = mtDBTool.query(sql);
		for(String[] items:list){
			String item	=items[0];
			result		=Integer.parseInt(item);
		}
		return result;
	} 
	//	插入用户信息的数据;
	public String insertStateinfo(){
		String sql=
		"insert into book_state_info (iid,count,ccount,ebids) values (" +
		"'"+this.iid+"','"+this.count+"','"+this.ccount+"','"+ebids+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//	翻页显示信息;
	public String queryStateinfoByPageAndCondition(int nCurrentPage,int nCountLimit,String pkind,String value){
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String where=" where "+pkind+" like '%"+value+"%' limit "+nItem+","+nCountLimit;
		if(value.equals("null")){
			where=" limit "+nItem+","+nCountLimit;
		}
		String 				sql	 = "select * from book_state_info "+where;
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
						obj.put("count", items[2]);
						obj.put("ccount", items[3]);
						obj.put("ebids", items[4]);
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
	public String queryStateinfoItem(String id){
		String 				sql	 = "select * from book_state_info where id="+id;
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
						obj.put("count", items[2]);
						obj.put("ccount", items[3]);
						obj.put("ebids", items[4]);
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
		String sql="delete from book_state_info";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	
	public String delItem(String id){
		String sql="delete from book_state_info where id="+id;
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	public String updateState(String ccount,String iid){
		String sql="update book_state_info set ccount='"+ccount+"' where iid='"+iid+"'";
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

	public String getCount(String iid) {
		int n=queryCount("count",iid);
		return n+"";
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCcount(String iid) {
		int n=queryCount("ccount",iid);
		return n+"";
	}

	public void setCcount(String ccount) {
		this.ccount = ccount;
	}

	public String getEbids() {
		return ebids;
	}

	public void setEbids(String ebids) {
		this.ebids = ebids;
	}

	public void setMtDBTool(MTDataBaseTool mtDBTool) {
		this.mtDBTool = mtDBTool;
	}
	
}
