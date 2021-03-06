package cn.com.jy.view.need;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.com.jy.activity.R;
import cn.com.jy.model.helper.MTConfigHelper;
import cn.com.jy.model.helper.MTGetOrPostHelper;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResignActivity extends Activity {
	//	01.控件定义;
	private TextView tvTopic,
					 tvResult	//	备注信息;
					 ;
	private TextView 
					 btnFunction;//	返回按钮;
	private EditText etWname,	 //	用户名称;
					 etWcall,	 //	用户编号;
					 etWpwd,	 //	用户密码;
					 etEnsure	 //	确认密码;
					 ;	
	private String   wname,		 //	用户名称;
					 wcall,		 //	用户昵称;
					 wpwd,		 //	用户密码;
					 wensure	 //	用户确认密码;
					 ;
	private Context	 mContext;	 //	上下文信息;
	private MyThread mThread;	 //	控制线程;
	private ProgressDialog  mDialog;// 对话框;
	private MTGetOrPostHelper mGetOrPostHelper;//	网络数据请求的
	
	// 03.与交互相关的Hanlder内容;
	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			Bundle b = msg.getData();
			int nFlag = b.getInt("flag");
			String id = b.getString("id");
			//	关闭对话方框;
			mDialog.dismiss();
			//	判断标记内容;
			switch (nFlag) {
			//	结果正确信号;
			case MTConfigHelper.NTAG_SUCCESS:		
				//	提示符;
				Toast.makeText(mContext, R.string.tip_success,Toast.LENGTH_LONG).show();
				Log.i("MyLog", "id="+id);
				tvResult.setText("您的分配临时编号为:"+id);
//				tvResult.setTextColor(Color.BLACK);
				break;
			//	结果错误信号;
			case MTConfigHelper.NTAG_FAIL:
				//	提示框;
				Toast.makeText(mContext, R.string.tip_fail,Toast.LENGTH_LONG).show();
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
		setContentView(R.layout.resign);
		//	控件的初始化;
		initView();
		//	事件的初始化;
		initEvent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	//	初始化控件;
	public void initView(){
		tvTopic	=(TextView) findViewById(R.id.tvTopic);
		etWname	=(EditText) findViewById(R.id.etWname);
		etWcall	=(EditText) findViewById(R.id.etWcall);
		etWpwd	=(EditText) findViewById(R.id.etWpwd);
		etEnsure=(EditText) findViewById(R.id.etEnsure);
		tvResult=(TextView) findViewById(R.id.tvResult);
		btnFunction=(TextView) findViewById(R.id.btnFunction);
	}
	//	初始化事件;
	public void initEvent(){
		mContext		=ResignActivity.this;
		mGetOrPostHelper=new MTGetOrPostHelper();
		tvTopic.setText("注册信息");
		btnFunction.setVisibility(View.GONE);
	}
	//	退出按钮;
	public void onClickBack(View view){
		finish();
	}
	//	重置按钮;
	public void onClick_Reset(View view){
		etWname.setText(MTConfigHelper.SPACE);
		etWcall.setText(MTConfigHelper.SPACE);
		etWpwd.setText(MTConfigHelper.SPACE);
		etEnsure.setText(MTConfigHelper.SPACE);
	}
	//	上传按钮;
	public void onClick_Ok(View view){
		// 获得相应的;
		wname 	 = etWname.getText().toString().trim();
		wcall	 = etWcall.getText().toString().trim();
		wpwd 	 = etWpwd.getText().toString().trim();
		wensure	 = etEnsure.getText().toString().trim();
		if(!wname.equals("")&&!wcall.equals("")&&!wpwd.equals("")&&!wensure.equals("")){			
			if(wpwd.endsWith(wensure)){
				if(mThread==null){
					final CharSequence strDialogTitle = getString(R.string.tip_dialog_wait);
					final CharSequence strDialogBody  = getString(R.string.tip_dialog_done);
					mDialog = ProgressDialog.show(mContext, strDialogTitle, strDialogBody,true);
					mThread	= new MyThread();
					mThread.start();
				}
			}else{
				Toast.makeText(mContext, "两次密码不同", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(mContext, "信息不可为空", Toast.LENGTH_SHORT).show();
		}
	}
	
	public class MyThread extends Thread{
		private String url,
		 			   param,
		 			   response;
		@Override
		public void run() {
			url		 =	"http://"+MTConfigHelper.TAG_IP_ADDRESS+":"+MTConfigHelper.TAG_PORT+"/"+MTConfigHelper.TAG_PROGRAM+"/login";
			try {
				param	 =	"operType=2&wname="+URLEncoder.encode(wname,"utf-8") + "&wcall="+URLEncoder.encode(wcall,"utf-8")+"&wpwd=" + wpwd;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 结果集内容;
			response = 	mGetOrPostHelper.sendGet(url,param);
			// 控制Handler的部分内容;
			int nFlag= 	MTConfigHelper.NTAG_FAIL;
			// 判断种类;
			if(!response.equalsIgnoreCase("fail")){
				nFlag= MTConfigHelper.NTAG_SUCCESS;
			}
			
			Message msg 	= myHandler.obtainMessage();
			Bundle  bundle	= new Bundle();
			bundle.putInt("flag", nFlag);
			bundle.putString("id", response);
			msg.setData(bundle);
			msg.sendToTarget();
		}
	}
	private void closeThread(){
		if(mThread!=null){
			mThread.interrupt();
			mThread=null;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeThread();
	}
}
