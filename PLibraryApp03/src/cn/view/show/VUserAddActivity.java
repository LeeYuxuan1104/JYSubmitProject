package cn.view.show;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cn.model.entity.MUserinfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VUserAddActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private Button vBack,vOk,vImage;
	private TextView vTopic;
	private EditText vUid,vUname,vUpwd,vNote,vPhone,vEmail;
	private Spinner	vUrole;
	private final int IMAGE=1;
	private MTConfiger mtConfiger;
	private String role,imagepath=null;
	private	ProgressDialog	vDialog;	// 	对话框; 
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
		setContentView(R.layout.act_vuser_add);
		//	初始化控件;
		initView();
		//	初始化事件;
		initEvent();
	}
	
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vOk	 =(Button) findViewById(R.id.btnOk);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vUrole=(Spinner) findViewById(R.id.sprole);
		vUid=(EditText) findViewById(R.id.etuid);
		vUname=(EditText) findViewById(R.id.etuname);
		vUpwd= (EditText) findViewById(R.id.etupwd);
		vNote=(EditText) findViewById(R.id.etnote);
		vPhone=(EditText) findViewById(R.id.etphone);
		vEmail=(EditText) findViewById(R.id.etemail);
		vImage=(Button) findViewById(R.id.ibimage);
	}
	private void initEvent(){
		mContext=VUserAddActivity.this;
		mtConfiger=new MTConfiger();
		vBack.setText(R.string.back);
		vTopic.setText(R.string.add);
		
		vBack.setOnClickListener(this);
		vImage.setOnClickListener(this);
		vOk.setOnClickListener(this);
		vUrole.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int position, long arg3) {
				switch (position) {
				case 0:
					role="普通用户";
					break;
				case 1:
					role="管理用户";
					break;
				default:
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				role="普通用户";
				
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
		case R.id.btnOk:
			if(myThread==null){
				MUserinfo mUserinfo=getInfo();
				if(mUserinfo!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(mUserinfo,imagepath);
					myThread.start();
				}
			}
			break;
		case R.id.ibimage:
			getPhoto();
			break;
		default:
			break;
		}
	}
	private MUserinfo getInfo(){
		MUserinfo	mUserinfo=null;
		String uid=mtConfiger.docheckEditView(vUid);
		String uname=mtConfiger.docheckEditView(vUname);
		String upwd=mtConfiger.docheckEditView(vUpwd);
		
		String note =mtConfiger.docheckEditView(vNote);
		String img	=mtConfiger.getImageName(imagepath);
		String phone=mtConfiger.docheckEditView(vPhone);
		String email=mtConfiger.docheckEditView(vEmail);
		if(!uid.equals("null")&&!uname.equals("null")&&!upwd.equals("null")&&!phone.equals("null")&&!email.equals("null")){							
			mUserinfo=new MUserinfo(uid, uname, upwd, role, note,img, phone, email);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		return mUserinfo;
	}
	
	private void getPhoto(){
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex  = c.getColumnIndex(filePathColumns[0]);
            imagepath = c.getString(columnIndex);
            showImage(imagepath); 
            c.close();
        }
    }
	@SuppressWarnings("deprecation")
	private void showImage(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        int width = bm.getWidth();  
        int height = bm.getHeight();  
  
        //放大為屏幕的1/2大小  
        float screenWidth  = getWindowManager().getDefaultDisplay().getWidth();     // 屏幕宽（像素，如：480px）  
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
        float scaleWidth = screenWidth/2/width;  
        float scaleHeight = screenHeight/2/height;  
  
        // 取得想要缩放的matrix參數  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
  
        vImage.setBackgroundDrawable(new BitmapDrawable(this.getResources(),newbm));
        vImage.setText("");
	}
	//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper mGetOrPostHelper;
		private MUserinfo		  mUserinfo;
		private String 			  path;
		
		public MyThread(MUserinfo mUserinfo,String path) {
			this.mGetOrPostHelper=new MTGetOrPostHelper();
			this.mUserinfo		 =mUserinfo;
			this.path			 =path;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			// 传值;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/user_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = "opertype="+MTConfiger.ADD_ITEM+"&uid="+URLEncoder.encode(mUserinfo.getUid(),"utf-8")+"&uname="+URLEncoder.encode(mUserinfo.getUname(),"utf-8")+"&upwd="+mUserinfo.getUpwd()+"&urole="+URLEncoder.encode(mUserinfo.getUrole(),"utf-8")+"&note="+URLEncoder.encode(mUserinfo.getNote(),"utf-8")+"&img="+mUserinfo.getImg()+"&phone="+mUserinfo.getPhone()+"&email="+mUserinfo.getEmail();
				response  = mGetOrPostHelper.sendGet(url, param);
				if(mUserinfo.getImg()!=null){					
					response	=	mGetOrPostHelper.uploadFile(url+"?opertype=8&uid="+mUserinfo.getUid(),path,mUserinfo.getImg());
				}
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
