package cn.view.show;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.model.entity.MBorrowerinfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;
import cn.model.tool.common.MTImgHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VBorrowBackActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private Button  vBack,vPhoto,vOk;
	private TextView vTopic;
	private String[] states={"正常","破损","遗失"};
	private Spinner	 vInstate;
	private ArrayAdapter<String> mAdapter;
	private MTImgHelper	mtImgHelper;
	private MTConfiger	mtConfiger;
	private String folderpath,tmppath,filepath,inimg=null,instate,iid,bid;
	private	ProgressDialog	vDialog;	// 对话框;
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
	private void closeThread(){
		if(myThread!=null){
			myThread.interrupt();
			myThread=null;
		}
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vborrow_back);
		initView();
		initEvent();
	}
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vInstate=(Spinner) findViewById(R.id.spinstate);
		vPhoto=(Button) findViewById(R.id.btnPhoto);
		vOk=(Button) findViewById(R.id.btnOk);
		
	}
	private void initEvent(){
		mContext	=	VBorrowBackActivity.this;
		mtImgHelper	=	new MTImgHelper();
		mtConfiger	=	new MTConfiger();
		vBack.setText(R.string.back);
		vBack.setOnClickListener(this);
		vOk.setOnClickListener(this);
		
		vTopic.setText(R.string.in);
		mAdapter	=	new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, states);
		vInstate.setAdapter(mAdapter);
		vPhoto.setOnClickListener(this);
		iid			=	getIntent().getExtras().getString("iid");
		bid			=	getIntent().getExtras().getString("bid");
		vInstate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				instate=states[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				instate=states[0];
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnPhoto:
			getPhotoInfo(iid);
			break;
		case R.id.btnOk:
			
			if(myThread==null){
				MBorrowerinfo	borrowerinfo=getInfo();
				if(borrowerinfo!=null){	
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(borrowerinfo);
					myThread.start();
				}
			}
			break;
		default:
			break;
		}
	}
	// 获取信息;
	private MBorrowerinfo getInfo(){
		MBorrowerinfo borrowerinfo=null;
		String state="归还";
		
		borrowerinfo=new MBorrowerinfo(bid, iid, null, null, null, null, state, null, instate, inimg);
		
		return borrowerinfo;
	}
	
	// 拍照功能;
	public void getPhotoInfo(String iid) {
		File file;
		
		if (mtConfiger.getfState().equals(Environment.MEDIA_MOUNTED)) {
			
				folderpath = mtConfiger.getfParentPath() + "borrow"+ File.separator +iid;
				inimg = java.lang.System.currentTimeMillis()+"";
				file = new File(folderpath);
				// 生成文件夹的方式;
				if (!file.exists()) {
					file.mkdirs();
				}
				// 生成2中文件路径:01.临时的 02.永久的
				tmppath = folderpath + File.separator + inimg + "_tmp.jpg";
				filepath = folderpath + File.separator + inimg + ".jpg";
				file = new File(tmppath);
				if (file.exists()) {
					file.delete();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						Toast.makeText(mContext, "照片创建失败!", Toast.LENGTH_LONG).show();
						return;
					}
				}
				Intent mIntent = new Intent("android.media.action.IMAGE_CAPTURE");
				mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(mIntent,1);
			
		} else Toast.makeText(mContext, "sdcard无效或没有插入!", Toast.LENGTH_SHORT).show();
	}
	//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper mGetOrPostHelper;
		private MBorrowerinfo	  mBorrowerinfo;
		
		public MyThread(MBorrowerinfo mBorrowerinfo) {
			this.mGetOrPostHelper=  new MTGetOrPostHelper();
			this.mBorrowerinfo	 =	mBorrowerinfo;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			// 传值;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/borrow_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = 
						"opertype=8&" +
						"bid="+URLEncoder.encode(mBorrowerinfo.getBid(),"utf-8")+"&" +
						"iid="+URLEncoder.encode(mBorrowerinfo.getIid(),"utf-8")+"&" +
						"state="+URLEncoder.encode(mBorrowerinfo.getState(),"utf-8")+"&" +
						"instate="+URLEncoder.encode(mBorrowerinfo.getInstate(),"utf-8")+"&" +
						"inimg="+mBorrowerinfo.getInimg()
						;
				response  = mGetOrPostHelper.sendGet(url, param);
				if(mBorrowerinfo.getInimg()!=null){					
					response	=	mGetOrPostHelper.uploadFile(url+"?opertype=9&bid="+mBorrowerinfo.getBid(),filepath,mBorrowerinfo.getInimg());
				}
			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")) {
				nFlag = 2;
			}
			
			mHandler.sendEmptyMessage(nFlag);
		}
	}
	
	// 返回键
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1&& resultCode == -1) {
			Toast.makeText(mContext, "拍照完成", Toast.LENGTH_SHORT).show();
			//	清空照片列表;
			mtImgHelper.compressPicture(tmppath, filepath);
			mtImgHelper.clearPicture(tmppath, null);
			vPhoto.setText(filepath);
		} 
	}
}
