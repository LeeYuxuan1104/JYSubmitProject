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
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class VWelcomeActivity extends Activity implements OnClickListener{
	////
	private Context  mContext;
	private Intent	 mIntent;
	////
	private EditText vUid,vUpwd;
	private Button 	 vOk,vResign;
	private ProgressDialog 	vDialog; // 对话方框;
	//	自定义类;
	private MyThread   myThread; 
	private MTConfiger mtConfiger;
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
				mIntent=new Intent(mContext, VMenuActivity.class);
				startActivity(mIntent);
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
        setContentView(R.layout.act_vwel);
        initView();
        initEvent();
    }
    
    private void initView(){
    	vUid=(EditText) findViewById(R.id.etUid);
    	vUpwd=(EditText) findViewById(R.id.etUpwd);
    	vOk=(Button) findViewById(R.id.btnOk);
    	vResign=(Button) findViewById(R.id.btnResign);
    	
    }

    private void initEvent(){
    	// 上下文进行衔接;
    	mContext	=	VWelcomeActivity.this;
    	mtConfiger	=	new MTConfiger();
    	//	进行事件监听的添加;
    	vOk.setOnClickListener(this);
    	vResign.setOnClickListener(this);
    }
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		//	登录按钮;
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
		//	注册按钮;
		case R.id.btnResign:
			mIntent=new Intent(mContext,VResignActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
		
	}
	private MUserinfo getInfo(){
		MUserinfo	userinfo = null;
		String 		uid		 = mtConfiger.docheckEditView(vUid);
		String 		upwd	 = mtConfiger.docheckEditView(vUpwd);
		if(!uid.equals("null")&&!upwd.equals("null")){
			userinfo=new MUserinfo(uid, null, upwd, null, null, null, null, null);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		
		return userinfo;
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
				param = "opertype="+MTConfiger.USER_LOGIN+"&uid="+URLEncoder.encode(mUserinfo.getUid(),"utf-8")+"&upwd="+URLEncoder.encode(mUserinfo.getUpwd(),"utf-8");
				response  = mGetOrPostHelper.sendGet(url, param);
			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")||response.trim().equals("")) {
				nFlag = 2;
			}
			mHandler.sendEmptyMessage(nFlag);
		}
	}
}
