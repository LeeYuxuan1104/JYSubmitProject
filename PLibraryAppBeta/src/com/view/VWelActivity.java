package com.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.plibraryappbeta.R;

import com.model.entity.MUserinfo;
import com.model.tool.common.MTConfiger;
import com.model.tool.common.MTGetOrPostHelper;
import com.model.tool.view.EditTextWithDel;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class VWelActivity extends Activity implements OnClickListener{
	private Context			mContext;
	private	Intent			mIntent;
	private EditTextWithDel vid,vpwd;     
	private Button 		 	vlogin;
	/*线程*/
	private ProgressDialog 	vDialog; // 对话方框;
	private MyThread   		myThread;
	private MUserinfo 		mUserinfo;
	
	//	控制线程;
	@SuppressLint("HandlerLeak") 
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Bundle bundle=msg.getData();
			int nFlag = bundle.getInt("flag");			
			vDialog.dismiss();
			Log.i("MyLog", "flag="+nFlag);
			switch (nFlag) {
			// 01.成功;
			case 1:
				//	成功提醒;
				Toast.makeText(mContext, R.string.success,Toast.LENGTH_SHORT).show();
				//	页面跳转;
				mIntent		=new Intent(mContext, VQueryActivity.class);
				
				String uid	=bundle.getString("uid");
				String uname=bundle.getString("uname");
				int    uright=bundle.getInt("uright");
				bundle		=new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("uname", uname);
				bundle.putInt("uright", uright);
				
				mIntent.putExtras(bundle);
				//	跳转页面;
				startActivity(mIntent);
				//	关闭本页面;
				finish();
				break;
			// 02.失败;
			case 2:
				//	失败显示;
				Toast.makeText(mContext, R.string.fail, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			if(myThread!=null){
				myThread.interrupt();
				myThread=null;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_wel);
		initView();
		initEvent();
	}

	private void initView(){
		vid=(EditTextWithDel) findViewById(R.id.etid);
		vpwd=(EditTextWithDel) findViewById(R.id.etpwd);     
		vlogin=(Button) findViewById(R.id.btnlogin);
	}
	private void initEvent(){
		mContext=VWelActivity.this;
		vlogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnlogin:
			if(myThread==null){
				if(mUserinfo==null){						
					mUserinfo=new MUserinfo();
				}
				mUserinfo=mUserinfo.getUserInfo(mContext, vid, vpwd);
				if(mUserinfo!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(mUserinfo);
					myThread.start();
				}
			}
			break;

		default:
			break;
		}
	}
	
	public class MyThread extends Thread {
		private MTGetOrPostHelper mGetOrPostHelper;
		private MUserinfo		  mUserinfo;
		public MyThread(MUserinfo mUserinfo) {
			this.mGetOrPostHelper=new MTGetOrPostHelper();
			this.mUserinfo	 =mUserinfo;
		}
		@Override
		public void run() {
			int nFlag = 1;
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String   url 	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/user_info";
			String   param;
			String response	 = "fail";
			Message  msg	 =	new Message();
			Bundle   bundle	 =	new Bundle();
			try {
				param = "opertype=2&uid="+URLEncoder.encode(mUserinfo.getUid(),"utf-8")+"&upwd="+URLEncoder.encode(mUserinfo.getUpwd(),"utf-8");
				response  = mGetOrPostHelper.sendGet(url, param);
			} catch (UnsupportedEncodingException e) {
				
			}
			
			if (response.trim().equals("fail")||response.trim().equals("")) {
				nFlag = 2;
			}else{
				try {
					JSONArray array = new JSONArray(response);
					JSONObject obj = null;

					// JsonObject的解析;
					obj 			 = array.getJSONObject(0);
					Log.i("MyLog", "obj="+obj);
					//	数据解析;
					String uid 		 = obj.getString("uid");
					String uname	 = obj.getString("uname");
					String uright 	 = obj.getString("uright");
					//	数据装包;
					bundle.putString("uid", uid);
					bundle.putString("uname", uname);
					bundle.putInt("uright", Integer.parseInt(uright));
					nFlag=1;
				} catch (JSONException e) {
					nFlag = 2;
				}
			}
			Log.i("MyLog", "flag="+nFlag);
			bundle.putInt("flag", nFlag);
			msg.setData(bundle);	
			mHandler.sendMessage(msg);
		}
	}
}
