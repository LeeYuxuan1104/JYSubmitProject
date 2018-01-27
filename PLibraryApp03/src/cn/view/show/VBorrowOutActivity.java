package cn.view.show;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.model.entity.MBorrowerinfo;
import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.DatePicker.OnDateChangedListener;


public class VBorrowOutActivity extends Activity implements OnClickListener{
	private Context	mContext;
	private Intent	mIntent;
	private Button vBack,vOk,vCodeiid,vCodeuid,vSearchiid,vSearchuid;
	private TextView vTopic;
	private EditText vIid,vResultbook,vBorrower,vResultborrower,vBtime,vDeadline,vBid;
	/////
	private ProgressDialog 	vDialog; // 对话方框;
	private MyThread   myThread=null; 
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
				Toast.makeText(mContext, R.string.update,Toast.LENGTH_SHORT).show();
				break;
			// 02.失败;
			case 2:
				Toast.makeText(mContext, R.string.nodata, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			String iname=msg.getData().getString("iname");
			if(iname!=null) vResultbook.setText(iname);
			String uname=msg.getData().getString("uname");
			if(uname!=null) vResultborrower.setText(uname);
			String oper=msg.getData().getString("oper");
			if(oper!=null){
				finish();
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
		setContentView(R.layout.act_vborrow_out);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vOk=(Button) findViewById(R.id.btnOk);
		vCodeiid=(Button) findViewById(R.id.codeiid);
		vCodeuid=(Button) findViewById(R.id.codeuid);
		vSearchiid=(Button) findViewById(R.id.searchiid);
		vSearchuid=(Button) findViewById(R.id.searchuid);
		
		vIid=(EditText) findViewById(R.id.etiid);
		vResultbook=(EditText) findViewById(R.id.etresultbook);
		vBorrower=(EditText) findViewById(R.id.etborrower);
		vResultborrower=(EditText) findViewById(R.id.etresultborrower);
		vBtime=(EditText) findViewById(R.id.etbtime);
		vDeadline=(EditText) findViewById(R.id.etdeadline);
		vBid=(EditText) findViewById(R.id.etbid);
	}
	
	private void initEvent(){
		mContext=VBorrowOutActivity.this;
		vBack.setText(R.string.back);
		vBack.setOnClickListener(this);
		vTopic.setText(R.string.borrow);
		vOk.setOnClickListener(this);
		vCodeiid.setOnClickListener(this);
		vCodeuid.setOnClickListener(this);
		vSearchiid.setOnClickListener(this);
		vSearchuid.setOnClickListener(this);
		String m=System.currentTimeMillis()+"";
		vBid.setText(m);
		mtConfiger=new MTConfiger();
		vBtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {				
				setViewDate(mContext, vBtime);
			}
		});
		
		vDeadline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setViewDate(mContext, vDeadline);			
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
		case R.id.codeiid:
			// 跳转至专门的intent控件;
			mIntent = new Intent(mContext, VFlushActivity.class);
			// 有返回值的跳转;
			startActivityForResult(mIntent,1);
			break;
		case R.id.codeuid:
			// 跳转至专门的intent控件;
			mIntent = new Intent(mContext, VFlushActivity.class);
			// 有返回值的跳转;
			startActivityForResult(mIntent,2);
			break;
		case R.id.searchiid:
			if(myThread==null){
				String iid=mtConfiger.docheckEditView(vIid);
				if(!iid.equals("null")){	
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(1, iid, null, null);
					myThread.start();
				}
			}
			break;
		case R.id.searchuid:
			if(myThread==null){
				String uid=mtConfiger.docheckEditView(vBorrower);
				if(!uid.equals("null")){
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				myThread=new MyThread(2, null, uid, null);
				myThread.start();
				}
			}
			break;
		case R.id.btnOk:
			
			if(myThread==null){
				MBorrowerinfo borrowerinfo= getInfo();
				if(borrowerinfo!=null){					
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(3, null, null, borrowerinfo);
					myThread.start();
				}
			}
			break;
		default:
			break;
		}
		
	}
	private MBorrowerinfo getInfo(){
		MBorrowerinfo borrowerinfo=null;
		String bid=mtConfiger.docheckEditView(vBid);
		String iid=mtConfiger.docheckEditView(vIid);
		String iname=mtConfiger.docheckEditView(vResultbook);
		String borrower=mtConfiger.docheckEditView(vBorrower);
		String uname=mtConfiger.docheckEditView(vResultborrower);
		String btime=mtConfiger.docheckEditView(vBtime);
		String deadline=mtConfiger.docheckEditView(vDeadline);
		String state="借出";
		String outstate="完好";
		String instate="未还";
		String inimg=null;
		if(!bid.equals("null")&&!iname.equals("null")&&!uname.equals("null")&&!btime.equals("null")&&!deadline.equals("null")){			
			borrowerinfo=new MBorrowerinfo(bid, iid, iname, borrower, btime, deadline, state, outstate, instate, inimg);
		}else Toast.makeText(mContext, R.string.complete, Toast.LENGTH_SHORT).show();
		
		return borrowerinfo;
	}
	
	// 返回键
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 1
				&& resultCode == 1) {
			String iid = intent.getStringExtra("bid");
			vIid.setText(iid);
		}else if(requestCode == 2
				&& resultCode == 1){
			String uid = intent.getStringExtra("bid");
			vBorrower.setText(uid);
		} 
	}
	//	查询线程
	// 定义的线程——自定义的线程内容;
	public class MyThread extends Thread {
		private MTGetOrPostHelper mGetOrPostHelper;
		private MBorrowerinfo	  borrowerinfo;
		private String  iid,uid;
		private int 	oper;
		public MyThread(int oper,String iid,String uid,MBorrowerinfo borrowerinfo) {
			this.mGetOrPostHelper=new MTGetOrPostHelper();
			this.oper=oper;
			this.iid=iid;
			this.uid=uid;
			this.borrowerinfo=borrowerinfo;
		}
		@Override
		public void run() {
			int nFlag = 1;
			// 进行相应的登录操作的界面显示;
			// 01.Http 协议中的Get和Post方法;
			String url 	=null;
			String param=null;
			String response	 = "fail";
	
			switch (oper) {
			///	书目查询;
			case 1:
				url	  = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/item_info";
				param = "opertype="+MTConfiger.Query_ITEM+"&iid="+iid;
				break;
			///	人员查询;
			case 2:
				url	  = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/user_info";
				param = "opertype="+MTConfiger.Query_ITEM+"&uid="+uid;
				break;
			///	信息提交;
			case 3:
				url	  = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/borrow_info";
				try {
					param = "opertype="+MTConfiger.ADD_ITEM+"&" +
							"bid="+borrowerinfo.getBid()+"&" +
							"iid="+borrowerinfo.getIid()+"&"+
							"iname="+URLEncoder.encode(borrowerinfo.getIname(),"utf-8")+"&"+
							"borrower="+URLEncoder.encode(borrowerinfo.getBorrower(),"utf-8")+"&"+
							"btime="+URLEncoder.encode(borrowerinfo.getBtime(),"utf-8")+"&"+
							"deadline="+URLEncoder.encode(borrowerinfo.getDeadline(),"utf-8")+"&"+
							"state="+URLEncoder.encode(borrowerinfo.getState(),"utf-8")+"&"+
							"outstate="+URLEncoder.encode(borrowerinfo.getOutstate(),"utf-8")+"&"+
							"instate="+URLEncoder.encode(borrowerinfo.getInstate(),"utf-8")+"&"+
							"inimg="+borrowerinfo.getInimg()
							;
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			default:
				break;
			}
			
			response  = mGetOrPostHelper.sendGet(url, param);

			if (response.trim().equals("fail")) {
				nFlag = 2;
			}else{
				if(oper!=3){
					try {
						JSONArray array = new JSONArray(response);
						
						int i = 0;
						JSONObject obj = null;
						Message	message=new Message();
						Bundle	bundle=new Bundle();
						do {
							try {
								// JsonObject的解析;
								obj 			 = array.getJSONObject(i);
								switch (oper) {
								case 1:
									
									String iname 	 = obj.getString("iname");
									bundle.putString("iname", iname);
									break;
								case 2:
									String uname 	 = obj.getString("uname");
									bundle.putString("uname", uname);
									break;
								default:
									break;
								}	
								i++;
							} catch (Exception e) {
								obj = null;
							}
						} while (obj != null);
						message.setData(bundle);
						mHandler.sendMessage(message);
					} catch (JSONException e) {
						nFlag = 2;
					}
				}else{
					Message	message=new Message();
					Bundle	bundle=new Bundle();
					bundle.putString("oper", "1");
					message.setData(bundle);
					mHandler.sendMessage(message);
				}
			}
			mHandler.sendEmptyMessage(nFlag);
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
}
