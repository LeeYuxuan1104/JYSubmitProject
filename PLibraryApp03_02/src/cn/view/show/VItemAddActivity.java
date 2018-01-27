package cn.view.show;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.model.entity.MItemInfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;
import cn.model.tool.common.MTImgHelper;
import cn.model.tool.common.MTSQLiteHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;

public class VItemAddActivity extends Activity implements OnClickListener{
	


	private Context  mContext;
    private EditText vIid,vIname,vNote,vAuthor,vPress,vPtime,vCount;    
    private Spinner	 vKid;
	private Button   vBack,vOk,vImg;
	private TextView vTopic;
	
	private MTSQLiteHelper	  mSqLiteHelper;//01.数据库帮助类;
	private SQLiteDatabase 	  mDB;		    //02.数据库对象类;
	private Cursor 		   	  mCursor;  	//03.数据库遍历签;
	private	ProgressDialog	vDialog;	// 	对话框;
	private MTConfiger 		mtConfiger;
	private MyThread		myThread;	   // 线程;
	private ArrayList<Map<String, String>> mList;
	private SimpleAdapter	mAdapter;
	private MTImgHelper		mtImgHelper;
	private String folderpath,img,filepath,tmppath,
				   kid;
	
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
		setContentView(R.layout.act_vitem_add);
		initView();
		initEvent();
	}
	private void initView(){
		vIid=(EditText) findViewById(R.id.etiid);
		vIname=(EditText) findViewById(R.id.etiname);
		vNote=(EditText) findViewById(R.id.etnote);
		vAuthor=(EditText) findViewById(R.id.etauthor);
		vPress=(EditText) findViewById(R.id.etpress);
		vPtime=(EditText) findViewById(R.id.etptime);
		vCount=(EditText) findViewById(R.id.etcount);    
	    vKid=(Spinner) findViewById(R.id.spkid);
	    vBack=(Button) findViewById(R.id.btnBack);
		vOk=(Button) findViewById(R.id.btnOk);
		vImg=(Button) findViewById(R.id.btnimg);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		
	}
	private void initEvent(){
		mContext=VItemAddActivity.this;
		mSqLiteHelper	=	new MTSQLiteHelper(mContext);
		mDB 			= 	mSqLiteHelper.getmDB();
		mtConfiger		=	new MTConfiger();
		mtImgHelper		=	new MTImgHelper();
		vBack.setText(R.string.back);
		vTopic.setText(R.string.add);
		vBack.setOnClickListener(this);
		vOk.setOnClickListener(this);
		vImg.setOnClickListener(this);
		showData();
		
		vPtime.setOnFocusChangeListener(new OnFocusChangeListener() {
			  
			  @Override
			  public void onFocusChange(View v, boolean hasFocus) {
			   // TODO Auto-generated method stub
			   if (hasFocus) {
				   setViewDate(mContext, vPtime);
			   }else {
			    
			   }   
			  }
			 });
		vKid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				kid=mList.get(position).get("id");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				kid=mList.get(0).get("id");
				
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
				MItemInfo mItemInfo=getInfo();
				if(mItemInfo!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(mItemInfo);
					myThread.start();
				}
			}
			break;
		///	照相;
		case R.id.btnimg:
			String iid=mtConfiger.docheckEditView(vIid);
			if(!iid.equals("null")){				
				getPhotoInfo(iid, kid);
			}else Toast.makeText(mContext, "请填写编号信息", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}
	private MItemInfo getInfo(){
		MItemInfo mItemInfo=null;
		String iid=mtConfiger.docheckEditView(vIid);
		String iname=mtConfiger.docheckEditView(vIname);
		String author=mtConfiger.docheckEditView(vAuthor);
		String note=mtConfiger.docheckEditView(vNote);
		String press=mtConfiger.docheckEditView(vPress);
		String ptime=mtConfiger.docheckEditView(vPtime);
		String count=mtConfiger.docheckEditView(vCount);
		if(!iid.equals("null")&&!iname.equals("null")&&!author.equals("null")&&!press.equals("null")&&!ptime.equals("null")&&!count.equals("null")){
			mItemInfo=new MItemInfo(iid, iname, note, author, press, ptime, count, iid,img);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		return mItemInfo;
	}
	
	//	线程的自定义形式;
	class MyThread extends Thread{
		
		private MTGetOrPostHelper mGetOrPostHelper;
		private MItemInfo		  mItemInfo;
		
		public MyThread(MItemInfo mItemInfo) {
			this.mGetOrPostHelper=  new MTGetOrPostHelper();
			this.mItemInfo		 =	mItemInfo;
		}
		
		@Override
		public void run() {
			int nFlag = 1;
			// 传值;
			String url 	  	 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/item_info";
			String param;
			String response	 = "fail";
			try {
				param 	  = 
						"opertype="+MTConfiger.ADD_ITEM+"&" +
						"iid="+URLEncoder.encode(mItemInfo.getIid(),"utf-8")+"&" +
						"iname="+URLEncoder.encode(mItemInfo.getIname(),"utf-8")+"&" +
						"note="+URLEncoder.encode(mItemInfo.getNote(),"utf-8")+"&" +
						"author="+URLEncoder.encode(mItemInfo.getAuthor(),"utf-8")+"&" +
						"press="+URLEncoder.encode(mItemInfo.getPress(),"utf-8")+"&" +
						"ptime="+URLEncoder.encode(mItemInfo.getPtime(),"utf-8")+"&" +
						"count="+URLEncoder.encode(mItemInfo.getCount(),"utf-8")+"&" +
						"kid="+URLEncoder.encode(mItemInfo.getKid(),"utf-8")+"&" +
						"img="+mItemInfo.getImg()
						;
				response  = mGetOrPostHelper.sendGet(url, param);
				if(mItemInfo.getImg()!=null){					
					response	=	mGetOrPostHelper.uploadFile(url+"?opertype=8&iid="+mItemInfo.getIid(),filepath,mItemInfo.getImg());
				}
			} catch (UnsupportedEncodingException e) {
				
			}

			if (response.trim().equalsIgnoreCase("fail")) {
				nFlag = 2;
			}
			
			mHandler.sendEmptyMessage(nFlag);
		}
	}
	
	//	显示信息的内容;
	private void showData(){
		//	表信息的加载;
		mList	=loadData();
		//	适配器的添加;
		mAdapter=new SimpleAdapter(mContext, mList, R.layout.act_item, new  String[]{"content","id"}, new int[]{R.id.content,R.id.id});
		//	适配器列表的绑定;
		vKid.setAdapter(mAdapter);
	}
	//	加载数据信息法;
	private ArrayList<Map<String, String>> loadData(){
		//	set的清空;
		ArrayList<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sql		=	"select * from kind_book_info";
		mCursor	= 	mDB.rawQuery(sql, null);
		while (mCursor.moveToNext()) {	
			Map<String, String> map=new HashMap<String, String>();
			String kid		=	mCursor.getString(mCursor.getColumnIndex("kid")).toString();
			String kname	=	mCursor.getString(mCursor.getColumnIndex("kname")).toString();
			map.put("content", kname);
			map.put("id",kid);
			list.add(map);
		}
		
		if(mCursor!=null){
			mCursor.close();
		}	
		return list;
	}
	private void closeThread(){
		if(myThread!=null){
			myThread.interrupt();
			myThread=null;
		}
	 }
	
	///	时间查询的控件;
		private String date;
		private void setViewDate(Context mContext,final EditText etview){
			Builder    vBuilder   = new Builder(mContext);
			
			/*布局控件*/
			View 	   view 	  = getLayoutInflater().inflate(R.layout.act_datatimepicker, null);
			vBuilder.setTitle(R.string.choosetime);
			vBuilder.setView(view);
			/*时间日期有关控件*/
			DatePicker datePicker = (DatePicker) view.findViewById(R.id.dpPicker);
			if (datePicker != null) {
				((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0))
				.getChildAt(2).setVisibility(View.GONE);
			} 
			Calendar   calendar   = Calendar.getInstance();

			int 	   nYear 	  = calendar.get(Calendar.YEAR);
			int 	   nMonth 	  = calendar.get(Calendar.MONTH);
			int 	   nDay 	  = calendar.get(Calendar.DAY_OF_MONTH);
			
			date = nYear + "年" + (nMonth + 1) + "月";
			datePicker.init(nYear, nMonth, nDay, new OnDateChangedListener() {

				@Override
				public void onDateChanged(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					// 日历控件;
					date = year + "年" + (monthOfYear + 1) + "月";
				}
			});
			vBuilder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					String stime = date;
					etview.setText(stime);
				}
			});
			vBuilder.create();
			vBuilder.show();
		}
		
		// 拍照功能;
		public void getPhotoInfo(String iid,String kid) {
			File file;
			if (mtConfiger.getfState().equals(Environment.MEDIA_MOUNTED)) {
				
					folderpath = mtConfiger.getfParentPath() + "item"+ File.separator + kid+File.separator+iid;
					img = java.lang.System.currentTimeMillis()+"";
					file = new File(folderpath);
					// 生成文件夹的方式;
					if (!file.exists()) {
						file.mkdirs();
					}
					// 生成2中文件路径:01.临时的 02.永久的
					tmppath = folderpath + File.separator + img + "_tmp.jpg";
					filepath = folderpath + File.separator + img + ".jpg";
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
		
		// 返回键
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent intent) {
			if (requestCode == 1&& resultCode == -1) {
				Toast.makeText(mContext, "拍照完成", Toast.LENGTH_SHORT).show();
				//	清空照片列表;
				mtImgHelper.compressPicture(tmppath, filepath);
				mtImgHelper.clearPicture(tmppath, null);
				vImg.setText(filepath);
			} 
		}
}
