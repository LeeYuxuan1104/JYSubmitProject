package com.model.entity;

import java.util.Map;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plibraryappbeta.R;
import com.model.tool.common.MTConfiger;

public class MBookinfo {
	private String 		id;
	private String 		bid;
	private String 		bname;
	private String 		author;
	private String 		press;
	private String 		state;
	private String 		number;
	private MTConfiger	mtConfiger;
	
	public MBookinfo(String bid, String bname, String author, String press,
			String state, String number) {
		super();
		this.bid = bid;
		this.bname = bname;
		this.author = author;
		this.press = press;
		this.state = state;
		this.number = number;
		if(mtConfiger==null){			
			mtConfiger=new MTConfiger();
		}
	}
	public MBookinfo() {
		if(mtConfiger==null){			
			mtConfiger=new MTConfiger();
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	//	进行数据的填写;
	//	01.登录有关对象;
	public MBookinfo getBookinfo(Context context,EditText et1,EditText et2,EditText et3,EditText et4,EditText et5,EditText et6){
		MBookinfo	bookinfo = null;
		String 		bid		 = mtConfiger.docheckEditView(et1);
		String 		bname	 = mtConfiger.docheckEditView(et2);
		String 		author	 = mtConfiger.docheckEditView(et3);
		String 		press	 = mtConfiger.docheckEditView(et4);
		String 		state	 = mtConfiger.docheckEditView(et5);
		String 		number	 = mtConfiger.docheckEditView(et6);
		if(!bid.equals("null")&&!bname.equals("null")&&!author.equals("null")&&!press.equals("null")&&!state.equals("null")&&!number.equals("null")){
			bookinfo		 = new MBookinfo(bid, bname, author, press, state, number);
		}else Toast.makeText(context, R.string.complete, Toast.LENGTH_SHORT).show();
		return bookinfo;
	}
	//	02.显示信息;
	// 02.获得信息的详情;
	public String getBookinfoItem(Map<String, String> obj) {
		String content = "";
		if (obj != null) {
			String id      = obj.get("id").toString();
			String bid     = obj.get("bid").toString();
			String bname   = obj.get("bname").toString();
			String author  = obj.get("author").toString();
			String press   = obj.get("press").toString();
			String state   = obj.get("state").toString();
			String number  = obj.get("number").toString();

			content =
					"[ID]   "  + id + "\r\n"+ 
					"[图书编号]   "+bid+"\r\n"+  
					"[图书名称]   "+bname + "\r\n"+ 
					"[图书作者]   " + author + "\r\n" +
					"[出版社名]   " +  press+ "\r\n" + 
					"[目前状态]   " + state+ "\r\n" + 
					"[图书序号]   " + number;
		} else
			return null;
		return content;
	}
}
