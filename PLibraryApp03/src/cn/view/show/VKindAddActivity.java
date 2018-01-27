package cn.view.show;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.model.entity.MKindInfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VKindAddActivity extends Activity implements OnClickListener{
	private Context  mContext;
	private EditText vKid,vKname,vNote;
	private Button   vBack,vOk;
	private TextView vTopic;
	private	ProgressDialog	vDialog;	// 	对话框;
	private MTConfiger 		mtConfiger;
	private MyThread		myThread;	   // 线程;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int nFlag=msg.what;
			vDialog.dismiss();
			switch (nFlag) {
			case 1:				
				Toast.makeText(mContext, R.string.success,Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 2:
				Toast.makeText(mContext, R.string.fail,Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			closeThread();
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vkind_add);
		initView();
		initEvent();
	}
	private void initView(){
		vBack	=(Button) findViewById(R.id.btnBack);
		vTopic	=(TextView) findViewById(R.id.tvTopic);
		vOk		=(Button) findViewById(R.id.btnOk);
		
		vKid	=(EditText) findViewById(R.id.etkid);
		vKname	=(EditText) findViewById(R.id.etkname);
		vNote	=(EditText) findViewById(R.id.etnote);;
	}
	private void initEvent(){
		mContext=VKindAddActivity.this;
		mtConfiger=new MTConfiger();
		vBack.setText(R.string.back);
		vTopic.setText(R.string.kindinfo);
		vBack.setOnClickListener(this);
		vOk.setOnClickListener(this);
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
				MKindInfo mKindInfo=getInfo();
				if(mKindInfo!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(mKindInfo);
					myThread.start();
				}
			}
			break;
		default:
			break;
		}
		
	}
	private MKindInfo getInfo(){
		MKindInfo mKindInfo=null;
		String kid=mtConfiger.docheckEditView(vKid);
		String kname=mtConfiger.docheckEditView(vKname);
		String note=mtConfiger.docheckEditView(vNote);
		if(!kid.equals("null")&&!kname.equals("null")){
			mKindInfo=new MKindInfo(kid, kname, note);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		
		return mKindInfo;
	}
	
	//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper mGetOrPostHelper;
		private MKindInfo		mKindInfo;
		
		public MyThread(MKindInfo mKindInfo) {
			this.mGetOrPostHelper=new MTGetOrPostHelper();
			this.mKindInfo	=	mKindInfo;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			// 传值;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/kind_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = "opertype="+MTConfiger.ADD_ITEM+"&kid="+URLEncoder.encode(mKindInfo.getKid(),"utf-8")+"&kname="+URLEncoder.encode(mKindInfo.getKname(),"utf-8")+"&note="+URLEncoder.encode(mKindInfo.getNote(),"utf-8");
				response  = mGetOrPostHelper.sendGet(url, param);
			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")) {
				nFlag = 2;
			}
			
			mHandler.sendEmptyMessage(nFlag);
		}
	}
	private void closeThread(){
		if(myThread!=null){
			myThread.interrupt();
			myThread=null;
		}
	 }
}
