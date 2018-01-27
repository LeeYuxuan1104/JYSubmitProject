package cn.view.show;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.model.entity.MUserinfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;

import com.example.plibraryapp01.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class VResignActivity extends Activity implements OnClickListener{
	//	上下文的内容;
	private Context			mContext;
	//	空间按钮;
	private Button  		vBack,vOk,vImg;
	private EditText 		vUid,vUname,vUpwd,vUrole,vNote,vPhone,vEmail;
	private TextView		vTopic;
	//	进度条;
	private ProgressDialog 	vDialog; // 对话方框;
	private MyThread		myThread;
	private MTConfiger		mtConfiger;
	
	//	控制线程;
	@SuppressLint("HandlerLeak") 
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int nFlag = msg.what;
			vDialog.dismiss();
			switch (nFlag) {
			// 01.成功;
			case 1:
				Toast.makeText(mContext, R.string.success,Toast.LENGTH_SHORT).show();
				finish();
				break;
			// 02.失败;
			case 2:
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
        setContentView(R.layout.act_vresign);
        initView();
        initEvent();
    }
    //	空间的声明;
    private void initView(){
    	vBack=(Button) findViewById(R.id.btnBack);
    	vOk=(Button) findViewById(R.id.btnOk);
    	vImg=(Button) findViewById(R.id.ibImg);
    	vUid=(EditText) findViewById(R.id.etUid);
    	vUname=(EditText) findViewById(R.id.etUname);
    	vUpwd=(EditText) findViewById(R.id.etUpwd);
    	vUrole=(EditText) findViewById(R.id.etUrole);
    	vNote=(EditText) findViewById(R.id.etNote);
    	vPhone=(EditText) findViewById(R.id.etPhone);
    	vEmail=(EditText) findViewById(R.id.etEmail);
    	vTopic=(TextView) findViewById(R.id.tvTopic);    	
    }
    //	事件的声明;
    private void initEvent(){
    	mContext	=	VResignActivity.this;
    	//	声明工具类;
    	mtConfiger	=	new MTConfiger();
    	
    	//	初始化页面内容;
    	vTopic.setText(R.string.resign);
    	vBack.setText(R.string.back);
    	//	添加事件监听;
    	vBack.setOnClickListener(this);
    	vOk.setOnClickListener(this);
    	vImg.setOnClickListener(this);
    	vImg.setVisibility(View.GONE);
    	myThread=null;
    }
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnOk:
			if(myThread==null){
				MUserinfo mUserinfo=getInfo();
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
	//	获取信息;
	private MUserinfo getInfo(){
		MUserinfo	mUserinfo=null;
		String uid=mtConfiger.docheckEditView(vUid);
		String uname = mtConfiger.docheckEditView(vUname);
		String upwd = mtConfiger.docheckEditView(vUpwd);
		String urole =mtConfiger.docheckEditView(vUrole);
		String note  =mtConfiger.docheckEditView(vNote);
		String phone =mtConfiger.docheckEditView(vPhone);
		String email =mtConfiger.docheckEditView(vEmail);
	
		if(!uid.equals("null")&&!uname.equals("null")&&!upwd.equals("null")&&!urole.equals("null")&&!phone.equals("null")&&!email.equals("null")){
			mUserinfo=new MUserinfo(uid, uname, upwd, urole, note, "null", phone, email);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		return mUserinfo;
	}

	// 定义的线程——自定义的线程内容;
	public class MyThread extends Thread {
		private MTGetOrPostHelper mGetOrPostHelper;
		private MUserinfo		  mUserinfo;
		public MyThread(MUserinfo mUserinfo) {
			this.mGetOrPostHelper=new MTGetOrPostHelper();
			this.mUserinfo		 =mUserinfo;
		}
		@Override
		public void run() {
			int nFlag = 1;
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/user_info";
			String param;
			String response	 = "fail";
			try {
				param = "opertype="+MTConfiger.USER_RESIGN+"&uid="+URLEncoder.encode(mUserinfo.getUid(),"utf-8")+"&uname="+URLEncoder.encode(mUserinfo.getUname(),"utf-8")+"&upwd="+mUserinfo.getUpwd()+"&urole="+URLEncoder.encode(mUserinfo.getUrole(),"utf-8")+"&note="+URLEncoder.encode(mUserinfo.getNote(),"utf-8")+"&phone="+mUserinfo.getPhone()+"&email="+mUserinfo.getEmail();
				response  = mGetOrPostHelper.sendGet(url, param);
			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")) {
				nFlag = 2;
			}
			mHandler.sendEmptyMessage(nFlag);
		}
	}
}