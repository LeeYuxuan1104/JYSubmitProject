package com.model.entity;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plibraryappbeta.R;
import com.model.tool.common.MTConfiger;

public class MUserinfo {
	private String uid;
	private String uname;
	private String upwd;
	private String right;
	private MTConfiger mtConfiger;
	
	public MUserinfo() {
		if(mtConfiger==null){			
			mtConfiger=new MTConfiger();
		}
	}
	public MUserinfo(String uid, String uname, String upwd, String right) {
		super();
		this.uid = uid;
		this.uname = uname;
		this.upwd = upwd;
		this.right = right;
		if(mtConfiger==null){			
			mtConfiger=new MTConfiger();
		}
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

	
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	//	进行数据的填写;
	//	01.登录有关对象;
	public MUserinfo getUserInfo(Context context,EditText et1,EditText et2){
		MUserinfo	userinfo = null;
		String 		uid		 = mtConfiger.docheckEditView(et1);
		String 		upwd	 = mtConfiger.docheckEditView(et2);
		if(!uid.equals("null")&&!upwd.equals("null")){
			userinfo		 = new MUserinfo(uid, null, upwd, null);
		}else Toast.makeText(context, R.string.complete, Toast.LENGTH_SHORT).show();
		return userinfo;
	}
}
