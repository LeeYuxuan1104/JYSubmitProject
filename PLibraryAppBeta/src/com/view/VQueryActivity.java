package com.view;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class VQueryActivity extends Activity implements OnClickListener{
	//	上下文的事件内容;
	private Context	mContext;
	private Intent	mIntent;
	private Bundle	mBundle;
	//	对话框;
	private Builder	vBuilder;
	//	上方向内容;
	private TextView vTopic;
	private Button   vBack,vBookAdd,vUserAdd;
	//	下方向内容;
	private MTConfiger mtConfiger;
	private int 	nSwitcher;
	//	下方按钮控件;
	private Spinner  vPageCount;
	private Button   vUpPage,vDownPage;
	private EditText vPage;
	//	中间的按钮控件;
	private Spinner  vKind;
	private Button   vSearch;
	private EditTextWithDel vValue;
	//	样式形式内容;
	private TextView vNum,vContent;
	private ListView vlistView;

	//	进行线程控件;
	private ProgressDialog 	vDialog; // 对话方框;
	//	自定义类;
	private MyThread   myThread; 
	private int 	   nUpOrDown=1; 
	private ArrayList<Map<String, String>> listdata;
	//	适配器内容;
	//	参数值信息;
	private String[] names={"名称","作者","出版社","编号"},
					 kinds={"bname","author","press","bid"},
					 pages={"1","2","3","4","5","6","7","8","9","10"},
					 pages2={"01/页","02/页","03/页","04/页","05/页","06/页","07/页","08/页","09/页","10/页"};
	private ArrayAdapter<String> adapter,pAdapter;
	private SimpleAdapter	mAdapter;

	//	参数的初始化;
	//	当前页码;
	private int nCurrentPage=1,nCountLimit=4;
	//	参数码值;
	private String value=" ",pkind="";
	//	借书信息;
	private MBookinfo mBookinfo;
	
	//	控制线程;
	@SuppressLint("HandlerLeak") 
	Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle=msg.getData();
			int nFlag = bundle.getInt("flag");
			ArrayList<Map<String, String>> lresult=(ArrayList<Map<String, String>>) bundle.getSerializable("list");
			
			vDialog.dismiss();
			switch (nFlag) {
			// 01.成功;
			case 1:
				Toast.makeText(mContext, R.string.update,Toast.LENGTH_SHORT).show();
				break;
			// 02.失败;
			case 2:
				Toast.makeText(mContext, R.string.nodata, Toast.LENGTH_LONG).show();
				switch (nUpOrDown) {
				//	上一页;				
				case 1:
					nCurrentPage++;
					break;
				//	下一页;
				case 2:					
					nCurrentPage--;
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
			loadData(lresult);
			vPage.setText("第 "+nCurrentPage+" 页");
			if(myThread!=null){
				myThread.interrupt();
				myThread=null;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_query);
		initView();
		initEvent();
	}
	
	private void initView(){
		//	标题;
		vTopic=(TextView) findViewById(R.id.tvTopic);
		//	返回按钮;
		vBack=(Button) findViewById(R.id.btnBack);
		//	图书增加;
		vBookAdd=(Button) findViewById(R.id.btnFunction);
		//	用户增加;
		vUserAdd=(Button) findViewById(R.id.btnOther);

		//	下方按钮控件;
		vPageCount=(Spinner) findViewById(R.id.spPageCount);
		//	上一页;
		vUpPage	  =(Button) findViewById(R.id.btnUpPage);
		//	下一页;
		vDownPage =(Button) findViewById(R.id.btnDownPage);
		//	页码;
		vPage	  =(EditText) findViewById(R.id.etPage);
		//	中间的按钮控件;
		vKind	  =(Spinner) findViewById(R.id.spKind);
		//	搜索按钮;
		vSearch	  =(Button) findViewById(R.id.btnSearch);
		//	值按钮;
		vValue	  =(EditTextWithDel) findViewById(R.id.etValue);
		//	样式形式内容;
		vNum	  =(TextView) findViewById(R.id.num);
		//	内容值;
		vContent  =(TextView) findViewById(R.id.content);
		//	信息显示列表;
		vlistView =(ListView) findViewById(R.id.listView);
	
	}
	
	private void initEvent(){
		//	上下文内容的绑定;
		mContext	=VQueryActivity.this;
		mtConfiger	=new MTConfiger();
		vBack.setText(R.string.exit);
		vBack.setOnClickListener(this);
		vTopic.setText("借书查询");
		vBookAdd.setText("借书添加");
		vUserAdd.setText("人员添加");
		vBookAdd.setOnClickListener(this);
		vUserAdd.setOnClickListener(this);
		vUserAdd.setVisibility(View.GONE);
		mBookinfo=new MBookinfo();
		////
		//	搜索按钮;
		vSearch.setOnClickListener(this);
		//	上一页按钮;
		vUpPage.setOnClickListener(this);
		//	下一页按钮;
		vDownPage.setOnClickListener(this);
		vNum.setText("  ");
		vContent.setText("编号 · 名称 · 作者 · 出版社");
		initLoad();
		
		adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, names);
		vKind.setAdapter(adapter);
		//	信息的显示;
		showTip();
			
		
		
		//增加事件监听;
		vKind.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				pkind=kinds[position];
				vValue.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				pkind="";
				
			}
		});

		//	列表单点
		vlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				vBuilder				=new Builder(mContext);
				Map<String, String> obj	=listdata.get(position);
				vBuilder.setTitle("详情");
				String 				info=mBookinfo.getBookinfoItem(obj);
				vBuilder.setMessage(info);
				vBuilder.setNegativeButton(R.string.no, null);
				vBuilder.create();
				vBuilder.show();
			}
		});
		//	每页显示;
		pAdapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, pages2);
		vPageCount.setAdapter(pAdapter);
		vPageCount.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				nCountLimit=Integer.parseInt(pages[position]);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				nCountLimit=4;
			}
		});
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		initLoad();
	}
	private void showTip(){
		Intent i=getIntent();
		mBundle=i.getExtras();
		String uid=mBundle.getString("uid");
		String uname=mBundle.getString("uname");
		nSwitcher=mBundle.getInt("uright");
		vBuilder=new Builder(mContext);
		vBuilder.setTitle("提示");
		vBuilder.setMessage("欢迎！学号:"+uid+" 的 "+uname+" 同学");
		vBuilder.setNegativeButton(R.string.ok, null);
		vBuilder.create();
		vBuilder.show();
		//	控制按钮;
		switch (nSwitcher) {
		case 0:
			vBookAdd.setVisibility(View.GONE);
			break;
		case 1:
			vBookAdd.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	// 定义的线程——自定义的线程内容;
		public class MyThread extends Thread {
			private MTGetOrPostHelper mGetOrPostHelper;
			private int 	currentpage,countlimit;
			private String  pkind,value,order;
			private int 	id;
			public MyThread(int currentpage,int countlimit,String pkind,String value,String order,int id) {
				this.mGetOrPostHelper=new MTGetOrPostHelper();
				this.currentpage=currentpage;
				this.countlimit=countlimit;
				this.pkind=pkind;
				this.value=value;
				this.order=order;
				this.id=id;
			}
			@Override
			public void run() {
				int nFlag = 1;
				// 进行相应的登录操作的界面显示;
				// 01.Http 协议中的Get和Post方法;
				String 		url 	;
				String 		param	;
				String 		response= 	"fail";
				Message 	msg		=	new Message();
				Bundle	    bundle	=	new Bundle();
				try {
					url	  			= 	"http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/book_info";
					if(order.equals("delall")){
						param = 
						"opertype="+MTConfiger.DEL_ALL;
						response  = mGetOrPostHelper.sendGet(url, param);
						listdata.clear();
					}
					
					if(order.equals("delitem")){
						param = 
						"opertype="+MTConfiger.DEL_ITEM+"" +
						"&id="+id;
						response  = mGetOrPostHelper.sendGet(url, param);
						listdata.clear();
					}
					param = 
					"opertype="+MTConfiger.QUERY_PAGE_CONDITION+"&" +
					"currentpage="+currentpage+"&" +
					"countlimit="+countlimit+"&" +
					"pkind="+pkind+"&" +
					"value="+URLEncoder.encode(value,"utf-8");
					response  = mGetOrPostHelper.sendGet(url, param);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (response.trim().equals("fail")) {
					nFlag = 2;
				}else{
					try {
						JSONArray array = new JSONArray(response);
						listdata		= new ArrayList<Map<String,String>>();
						int i = 0;
						JSONObject obj = null;
						do {
							try {
								// JsonObject的解析;
								obj 			 = array.getJSONObject(i);

								int    order	 = (nCurrentPage-1)*nCountLimit+(i+1);
								String id 		 = obj.getString("id");
								String bid 		 = obj.getString("bid");
								String bname 	 = obj.getString("bname");
								String author 	 = obj.getString("author");
								
								String press 	 = obj.getString("press");
								String state 	 = obj.getString("state");
								String number  	 = obj.getString("number");
				
								Map<String, String> map=new HashMap<String, String>();
								map.put("order", order+"");
								map.put("id", id);
								map.put("bid", bid);
								map.put("bname", bname);
								map.put("author", author);
								map.put("press", press);
								map.put("state", state);
								map.put("number", number);
							
								map.put("content",bid+"  ·  "+bname+"  ·  "+author+"  ·  "+press);
								listdata.add(map);
								i++;
							} catch (Exception e) {
								obj = null;
							}
						} while (obj != null);
					} catch (JSONException e) {
						nFlag = 2;
					}
				}
				bundle.putSerializable("list", listdata);
				bundle.putInt("flag", nFlag);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			}
		}
		//	进行数据的加载;
		private void loadData(ArrayList<Map<String, String>> list){
			if(list!=null){
				mAdapter=new SimpleAdapter(mContext, list, R.layout.act_item, new String[]{"order","content","id"}, new int []{R.id.number,R.id.content,R.id.id});
				vlistView.setAdapter(mAdapter);
			}else nCurrentPage=0;
		}
		//	初始化加载;
		private void initLoad(){
			if(myThread==null){
				nUpOrDown=0;
				nCurrentPage=1;
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				myThread=new MyThread(nCurrentPage, nCountLimit, "uid", "null","null",0);
				myThread.start();
			}		
		}
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		value	= vValue.getText().toString().trim();
		switch (nVid) {
		case R.id.btnBack:
			mtConfiger.exitSystem(VQueryActivity.this);
			break;
		// 搜索键;
		case R.id.btnSearch:
			if (myThread == null) {
				nUpOrDown = 0;
				nCurrentPage = 1;
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,
						strDialogBody, true);
				myThread = new MyThread(nCurrentPage, nCountLimit, pkind,
						value, "null", 0);
				myThread.start();
			}
			break;

		// 上一页;
		case R.id.btnUpPage:
			if (listdata != null) {
				if (myThread == null) {
					nUpOrDown = 1;
					nCurrentPage--;
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,
							strDialogBody, true);
					myThread = new MyThread(nCurrentPage, nCountLimit, pkind,
							value, "null", 0);
					myThread.start();
				}
			}
			break;

		// 下一页;
		case R.id.btnDownPage:
			if (listdata != null) {
				if (myThread == null) {
					nUpOrDown = 2;
					nCurrentPage++;
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,
							strDialogBody, true);
					myThread = new MyThread(nCurrentPage, nCountLimit, pkind,
							value, "null", 0);
					myThread.start();
				}
			}
			break;
		case R.id.btnFunction:
			mIntent = new Intent(mContext, VBookAddActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
		
	}
	//	按下事件;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mtConfiger.exitSystem(VQueryActivity.this); 
	    }
		return super.onKeyDown(keyCode, event);
	}

}
