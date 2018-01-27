package cn.model.entity;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import cn.model.tool.MTDataBaseTool;

public class MBorrowinfo {
	private int id ;
	private String bid;
	private String iid;
	private String iname;
	private String borrower;
	private String btime;
	private String deadline;
	private String state;
	private String outstate;
	private String instate;
	private String inimg;
	
	private MTDataBaseTool mtDBTool;

	//	含参构造函数;
	public MBorrowinfo(String bid, String iid, String iname, String borrower,
			String btime, String deadline, String state, String outstate,
			String instate, String inimg) {
		if(mtDBTool==null){
			mtDBTool=new MTDataBaseTool();
		}

		this.bid = bid;
		this.iid = iid;
		this.iname = iname;
		this.borrower = borrower;
		this.btime = btime;
		this.deadline = deadline;
		this.state = state;
		this.outstate = outstate;
		this.instate = instate;
		this.inimg = inimg;
	}
	//	构造函数;
	public MBorrowinfo() {
		if(mtDBTool==null){			
			mtDBTool=new MTDataBaseTool();
		}
	}
	//	插入用户信息的数据;
	public String insertBorrowinfo(){
		String sql=
		"insert into borrow_info (bid,iid,iname,borrower,btime,deadline,state,outstate,instate,inimg) values (" +
		"'"+this.bid+"','"+this.iid+"','"+this.iname+"','"+this.borrower+"','"+this.btime+"','"+this.deadline+"','"+this.state+"','"+this.outstate+"','"+this.instate+"','"+this.inimg+"')";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	//	翻页显示信息;
	public String queryBorrowinfoByPageAndCondition(int nCurrentPage,int nCountLimit,String pkind,String value){
		int nCPage=nCurrentPage-1;
		int nItem =nCPage*nCountLimit;
		String where=" where "+pkind+" like '%"+value+"%' limit "+nItem+","+nCountLimit;
		if(value.equals("null")){
			where=" limit "+nItem+","+nCountLimit;
		}
		String 				sql	 = "select * from borrow_info "+where;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("bid", items[1]);
						obj.put("iid", items[2]);
						obj.put("iname", items[3]);
						obj.put("borrower", items[4]);
						obj.put("btime", items[5]);
						obj.put("deadline", items[6]);
						obj.put("state", items[7]);
						obj.put("outstate", items[8]);
						obj.put("instate", items[9]);
						obj.put("inimg", items[10]);
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
	public String queryBorrowinfoItem(String id){
		String 				sql	 = "select * from borrow_info where id="+id;
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("bid", items[1]);
						obj.put("iid", items[2]);
						obj.put("iname", items[3]);
						obj.put("borrower", items[4]);
						obj.put("btime", items[5]);
						obj.put("deadline", items[6]);
						obj.put("state", items[7]);
						obj.put("outstate", items[8]);
						obj.put("instate", items[9]);
						obj.put("inimg", items[10]);
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
	// 查找所有借书记录
	public String queryBorrowinfoItem(){
		String 				sql	 = "select * from borrow_info order by id desc";
		ArrayList<String[]> list = mtDBTool.query(sql);
		JSONArray   		array= new JSONArray();
		if(list!=null){
			int 	nSize	=	list.size();
			if(nSize!=0){				
				for(String[] items:list){
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", items[0]);
						obj.put("bid", items[1]);
						obj.put("iid", items[2]);
						obj.put("iname", items[3]);
						obj.put("borrower", items[4]);
						obj.put("btime", items[5]);
						obj.put("deadline", items[6]);
						obj.put("state", items[7]);
						obj.put("outstate", items[8]);
						obj.put("instate", items[9]);
						obj.put("inimg", items[10]);
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
	// 查找用户的所有借书
	public String queryBorrowinfoItemByU(String name){
		String 				sql	 = "select item_book_info.*,bid,btime,borrow_info.id as pid from borrow_info,item_book_info where borrower='"+name+"' AND borrow_info.iid=item_book_info.iid order by pid DESC;";
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
						obj.put("bid", items[10]);
						obj.put("btime", items[11]);
						obj.put("pid", items[12]);
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
		String sql="delete from borrow_info";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	public String borrowBack(String bid,String state,String instate,String inimg){
		String sql="update borrow_info set state='"+state+"',instate='"+instate+"',inimg='"+inimg+"' where bid='"+bid+"'";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	/**
	 * @author loh
	 * @param id
	 * @return
	 */
	public String borrowBackById(String id,String state,String instate,String inimg){
		String sql="update borrow_info set state='"+state+"',instate='"+instate+"',inimg='"+inimg+"' where id='"+id+"'";
		if(this.mtDBTool.doDBUpdate(sql)!=0){
			return "ok";
		}
		return "fail";
	}
	public String delItem(String id){
		String sql="delete from borrow_info where id="+id;
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
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
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
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getBtime() {
		return btime;
	}
	public void setBtime(String btime) {
		this.btime = btime;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOutstate() {
		return outstate;
	}
	public void setOutstate(String outstate) {
		this.outstate = outstate;
	}
	public String getInstate() {
		return instate;
	}
	public void setInstate(String instate) {
		this.instate = instate;
	}
	public String getInimg() {
		return inimg;
	}
	public void setInimg(String inimg) {
		this.inimg = inimg;
	}
	public void setMtDBTool(MTDataBaseTool mtDBTool) {
		this.mtDBTool = mtDBTool;
	}
	
}
