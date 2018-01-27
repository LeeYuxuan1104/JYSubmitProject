package com.view;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



import com.example.plibraryappbeta.R;
import com.model.entity.MBookinfo;
import com.model.tool.common.MTConfiger;
import com.model.tool.common.MTGetOrPostHelper;
import com.model.tool.view.EditTextWithDel;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VBookAddActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private Builder	vBuilder;
	private TextView vTopic;
    private Button vBack,vFunction,vOther,vAdd;
    private EditTextWithDel vBid,vBname,vAuthor,vPress,vState,vNumber;
	private MBookinfo	mBookinfo;
	///
	//	使用的线程;
	private	ProgressDialog	vDialog;	// 	对话框;
	private MyThread		myThread;	// 线程;
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
		setContentView(R.layout.act_bookadd);
		initView();
		initEvent();
	}
	private void initView(){
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vBack=(Button) findViewById(R.id.btnBack);
		vFunction=(Button) findViewById(R.id.btnFunction);
		vOther= (Button) findViewById(R.id.btnOther);
		vAdd=(Button) findViewById(R.id.btnAdd);
		
		vBid=(EditTextWithDel) findViewById(R.id.etBid);
		vBname=(EditTextWithDel) findViewById(R.id.etBname);
		vAuthor=(EditTextWithDel) findViewById(R.id.etAuthor);
		vPress=(EditTextWithDel) findViewById(R.id.etPress);
		vState=(EditTextWithDel) findViewById(R.id.etState);
		vNumber=(EditTextWithDel) findViewById(R.id.etNumber);
		
		
	}
	private void initEvent(){
		mContext=VBookAddActivity.this;
		vTopic.setText("信息添加");
		vBack.setText("返回");
		vFunction.setVisibility(View.GONE);
		vOther.setVisibility(View.GONE);
		vAdd.setOnClickListener(this);
		vBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
			switch (nVid) {
			case R.id.btnBack:
				finish();
				break;
			case R.id.btnAdd:
				if(mBookinfo==null){
					mBookinfo=new MBookinfo();
				}
				mBookinfo=mBookinfo.getBookinfo(mContext, vBid, vBname, vAuthor, vPress, vState, vNumber);
				Log.i("MyLog", "object="+mBookinfo);
				if(mBookinfo!=null){	
					vBuilder=new Builder(mContext);
					vBuilder.setTitle("确认提交");
					String message=
							"[图书编号] "+mBookinfo.getBid()+"\r\n" +
							"[图书名称] "+mBookinfo.getBname()+"\r\n" +
							"[图书作者] "+mBookinfo.getAuthor()+"\r\n" +
							"[出版社名] "+mBookinfo.getPress()+"\r\n" +
							"[借出状态] "+mBookinfo.getState()+"\r\n" +
							"[同类序号] "+mBookinfo.getNumber()
							;
					vBuilder.setMessage(message);
					vBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if(myThread==null){								
								final CharSequence strDialogTitle = getString(R.string.wait);
								final CharSequence strDialogBody = getString(R.string.doing);
								vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
								myThread=new MyThread(mBookinfo);
								myThread.start();	
							}
						}
					});
					vBuilder.setNegativeButton(R.string.no, null);
					vBuilder.create();
					vBuilder.show();
				}
				break;
			default:
				break;
			}
		
	}
	
//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper mGetOrPostHelper;
		private MBookinfo		  mBookinfo;
		
		public MyThread(MBookinfo mBookinfo) {
			this.mGetOrPostHelper=  new MTGetOrPostHelper();
			this.mBookinfo		 =	mBookinfo;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			// 传值;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/book_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = 
						"opertype="+MTConfiger.ADD_ITEM+"&" +
						"bid="+URLEncoder.encode(mBookinfo.getBid(),"utf-8")+"&" +
						"bname="+URLEncoder.encode(mBookinfo.getBname(),"utf-8")+"&" +
						"author="+URLEncoder.encode(mBookinfo.getAuthor(),"utf-8")+"&" +
						"press="+URLEncoder.encode(mBookinfo.getPress(),"utf-8")+"&" +
						"state="+URLEncoder.encode(mBookinfo.getState(),"utf-8")+"&" +
						"number="+URLEncoder.encode(mBookinfo.getNumber(),"utf-8")
						;
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
