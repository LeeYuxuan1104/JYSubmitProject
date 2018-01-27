package cn.view.show;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.model.tool.common.MTConfiger;
import cn.model.tool.common.MTGetOrPostHelper;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VUserinfoActivity extends Activity implements OnClickListener{
	private Context  mContext;
	private Button   vBack,vSearch,vUpPage,vDownPage,vDelAll,vAdd;
	private Builder	 vBuilder;
	private TextView vTopic,vNum,vContent;
	private Spinner  vuserkind;
	private EditText vValue,vPage;
	private ListView vListView;

	//	进行线程控件;
	private ProgressDialog 	vDialog; // 对话方框;
	//	自定义类;
	private MyThread   myThread; 
	private MTConfiger mtConfiger;
	private int 	   nUpOrDown=1; 
	private ArrayList<Map<String, String>> listdata;
	//	适配器内容;
	private ArrayAdapter<String> adapter;
	private SimpleAdapter	mAdapter;
	//	参数值信息;
	private String[] names={"用户编号","用户名称","用户权限"};
	private String[] kinds={"uid","uname","urole"};
	private String[] pages={"4","5","6","7","8","9"};
	private ArrayAdapter<String> pAdapter;
	private Spinner	 vPageCount;
	
	//	参数的初始化;
	private int nCurrentPage=1;
	private int nCountLimit=4;
	private String value=" ";
	private String pkind="";
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
			vPage.setText("第"+nCurrentPage+"页");
			if(myThread!=null){
				myThread.interrupt();
				myThread=null;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vuser);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vuserkind=(Spinner) findViewById(R.id.spKind);
		vSearch=(Button) findViewById(R.id.btnSearch);
		vValue=(EditText) findViewById(R.id.etValue);
		vPage=(EditText) findViewById(R.id.etPage);
		vUpPage=(Button) findViewById(R.id.btnUpPage);
		vDownPage=(Button) findViewById(R.id.btnDownPage);
		vListView=(ListView) findViewById(R.id.listView);
		vDelAll=(Button) findViewById(R.id.btnFunction);
		vAdd=(Button) findViewById(R.id.btnOther);
		vPageCount=(Spinner) findViewById(R.id.spPageCount);
		vNum= (TextView) findViewById(R.id.num);
		vContent= (TextView) findViewById(R.id.content);
		
		
	}
	private void initEvent(){
		mContext	=	VUserinfoActivity.this;
		mtConfiger	=	new MTConfiger();
			
		vBack.setText(R.string.back);
		vTopic.setText(R.string.userinfo);
		vBack.setOnClickListener(this);
		vSearch.setOnClickListener(this);
		vUpPage.setOnClickListener(this);
		vDownPage.setOnClickListener(this);
		vDelAll.setVisibility(View.VISIBLE);
		vDelAll.setText(R.string.delall);
		vDelAll.setOnClickListener(this);
		vAdd.setVisibility(View.VISIBLE);
		vAdd.setText(R.string.add);
		vAdd.setOnClickListener(this);
		vNum.setText("序号");
		vContent.setText("编号 · 姓名 · 角色");
		
		initLoad();
		
		adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, names);
		vuserkind.setAdapter(adapter);
		//增加事件监听;
		vuserkind.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				pkind=kinds[position];
				vValue.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				pkind=" ";
				
			}
		});
		
		//	输入框变化的内容;
		vValue.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				if(listdata!=null){					
////					listdata.clear();
//					listdata=null;
//					nCurrentPage=0;
//					vPage.setText("第"+nCurrentPage+"页");
//				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
		//	列表长按显示的内容;
		vListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				vBuilder=new Builder(mContext);
				vBuilder.setTitle(R.string.delitem);
				final int id=Integer.parseInt(listdata.get(position).get("id"));
				vBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						nCurrentPage=1;
						nUpOrDown=0;
						final CharSequence strDialogTitle = getString(R.string.wait);
						final CharSequence strDialogBody = getString(R.string.doing);
						vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
						myThread=new MyThread(nCurrentPage, nCountLimit, pkind, "null","delitem",id);
						myThread.start();
					}
				});
				vBuilder.setNegativeButton(R.string.no, null);
				vBuilder.create();
				vBuilder.show();
				return false;
			}
		});
		//	列表单点
		vListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				Intent	intent=new Intent(mContext, VUserDetailActivity.class);
				Bundle  bundle=new Bundle();
				Map<String, String> map= listdata.get(position);
				bundle.putSerializable("item", (Serializable) map);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		//	每页显示;
		pAdapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, pages);
		vPageCount.setAdapter(pAdapter);
		vPageCount.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				nCountLimit=Integer.parseInt(pages[position]);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				nCountLimit=2;
			}
		});
	}

	private void loadData(ArrayList<Map<String, String>> list){
		if(list!=null){
			mAdapter=new SimpleAdapter(mContext, list, R.layout.act_item, new String[]{"number","content","id"}, new int []{R.id.number,R.id.content,R.id.id});
			vListView.setAdapter(mAdapter);
		}else nCurrentPage=0;
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		initLoad();
	}
	
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
		value=mtConfiger.docheckEditView(vValue);
		switch (nVid) {
		//	返回键;
		case R.id.btnBack:
			finish();
			break;
			
		//	搜索键;
		case R.id.btnSearch:
			if(myThread==null){
				nUpOrDown=0;
				nCurrentPage=1;
				final CharSequence strDialogTitle = getString(R.string.wait);
				final CharSequence strDialogBody = getString(R.string.doing);
				vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
				myThread=new MyThread(nCurrentPage, nCountLimit, pkind, value,"null",0);
				myThread.start();
			}
			break;
			
		//	上一页;
		case R.id.btnUpPage:
			if(listdata!=null){				
				if(myThread==null){
					nUpOrDown=1;
					nCurrentPage--;
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread= new MyThread(nCurrentPage, nCountLimit, pkind, value,"null",0);
					myThread.start();
				}	
			}
			break;
			
		//	下一页;
		case R.id.btnDownPage:
			if(listdata!=null){				
				if(myThread==null){
					value=mtConfiger.docheckEditView(vValue);
					nUpOrDown=2;
					nCurrentPage++;
					final CharSequence strDialogTitle = getString(R.string.wait);
					final CharSequence strDialogBody = getString(R.string.doing);
					vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
					myThread=new MyThread(nCurrentPage, nCountLimit, pkind, value,"null",0);
					myThread.start();
				}
			}
			break;
			
		//	清空操作;
		case R.id.btnFunction:
			if(listdata!=null){				
				vBuilder=new Builder(mContext);
				vBuilder.setTitle(R.string.delall);
				vBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if(myThread==null){
							nCurrentPage=0;
							nUpOrDown=0;
							final CharSequence strDialogTitle = getString(R.string.wait);
							final CharSequence strDialogBody = getString(R.string.doing);
							vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
							myThread=new MyThread(nCurrentPage, nCountLimit, pkind, "null","delall",0);
							myThread.start();
						}
					}
				});
				vBuilder.setNegativeButton(R.string.no, null);
				vBuilder.create();
				vBuilder.show();
			}else Toast.makeText(mContext, R.string.isnull, Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnOther:
			Intent intent=new Intent(mContext, VUserAddActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	
	//	查询线程
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
				url	  			= 	"http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/user_info";
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
			Log.i("MyLog", "xx="+response);
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

							int    number	 = (nCurrentPage-1)*nCountLimit+(i+1);
							String id 		 = obj.getString("id");
							String uid 		 = obj.getString("uid");
							String uname 	 = obj.getString("uname");
							String upwd 	 = obj.getString("upwd");
							
							String urole 	 = obj.getString("urole");
							String note	 	 = obj.getString("note");
							String img  	 = obj.getString("img");
							String phone 	 = obj.getString("phone");
							String email 	 = obj.getString("email");
			
							Map<String, String> map=new HashMap<String, String>();
							map.put("number", number+"");
							map.put("id", id);
							map.put("uid", uid);
							map.put("uname", uname);
							map.put("upwd", upwd);
							map.put("urole", urole);
							map.put("note", note);
							map.put("img", img);
							map.put("phone", phone);
							map.put("email", email);
							map.put("content",uid+"  ·  "+uname+"  ·  "+urole);
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
}
