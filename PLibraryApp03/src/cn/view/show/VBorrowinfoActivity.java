package cn.view.show;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;

public class VBorrowinfoActivity extends Activity implements OnClickListener{
	private Context  mContext;
	private Button   vBack,vSearch,vUpPage,vDownPage,vDelAll,vOut;
	private Builder	 vBuilder;
	private TextView vTopic,vNum,vContent;
	private Spinner  vkindkind;
	private EditText vValue,vPage;
	private ListView vlistView;
	
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
	private String[] names={"借阅流水号","借阅人","借阅时间"};
	private String[] kinds={"bid","borrower","btime"};
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
				switch ( nUpOrDown) {
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
		setContentView(R.layout.act_vborrow);
		initView();
		initEvent();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		initLoad();
	}
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vkindkind=(Spinner) findViewById(R.id.spKind);
		vSearch=(Button) findViewById(R.id.btnSearch);
		vValue=(EditText) findViewById(R.id.etValue);
		vPage=(EditText) findViewById(R.id.etPage);
		vUpPage=(Button) findViewById(R.id.btnUpPage);
		vDownPage=(Button) findViewById(R.id.btnDownPage);
		vlistView=(ListView) findViewById(R.id.listView);
		vDelAll=(Button) findViewById(R.id.btnFunction);
		vOut=(Button) findViewById(R.id.btnOther);
		vPageCount=(Spinner) findViewById(R.id.spPageCount);
		vNum= (TextView) findViewById(R.id.num);
		vContent= (TextView) findViewById(R.id.content);
	}
	
	private void initEvent(){
		mContext	=	VBorrowinfoActivity.this;
		mtConfiger	=	new MTConfiger();
		
		
		vBack.setText(R.string.back);
		vTopic.setText(R.string.borrowinfo);
		vBack.setOnClickListener(this);
		vSearch.setOnClickListener(this);
		vUpPage.setOnClickListener(this);
		vDownPage.setOnClickListener(this);
		vDelAll.setVisibility(View.VISIBLE);
		vDelAll.setText(R.string.delall);
		vDelAll.setOnClickListener(this);
		vOut.setVisibility(View.VISIBLE);
		vOut.setText(R.string.borrow);
		vOut.setOnClickListener(this);
		
		vNum.setText("序号");
		vContent.setText("流水号 · 借书人  ·  借书日期  ·  借出状态");
		initLoad();
		
		adapter=new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, names);
		vkindkind.setAdapter(adapter);
		
		vkindkind.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				pkind=kinds[position];
				vValue.setText("");
				if(pkind.equals("btime")){
					setViewDate(mContext, vValue);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				pkind=" ";
				
			}
		});
		
		
		//	列表长按显示的内容;
		vlistView.setOnItemLongClickListener(new OnItemLongClickListener() {

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
		vlistView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				Intent	intent=new Intent(mContext, VBorrowDetailActivity.class);
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
			vlistView.setAdapter(mAdapter);
		}else nCurrentPage=0;
	}
	private void initLoad(){
		if(myThread==null){
			nUpOrDown=0;
			nCurrentPage=1;
			final CharSequence strDialogTitle = getString(R.string.wait);
			final CharSequence strDialogBody = getString(R.string.doing);
			vDialog = ProgressDialog.show(mContext, strDialogTitle,strDialogBody, true);
			myThread=new MyThread(nCurrentPage, nCountLimit, "iid", "null","back",0);
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
			nCurrentPage=1;
			nUpOrDown=0;
			if(myThread==null){
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
		//	借出;
		case R.id.btnOther:
			Intent	intent=new Intent(mContext, VBorrowOutActivity.class);
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
			String url 	  	;
			String param;
			String response	 = "fail";
			Message 	msg		=	new Message();
			Bundle	    bundle	=	new Bundle();
			try {
				url	  		 = "http://"+MTConfiger.IP+":"+MTConfiger.PORT+"/"+MTConfiger.PROGRAM+"/api/borrow_info";
				
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
					listdata	 = new ArrayList<Map<String,String>>();
					int i = 0;
					JSONObject obj = null;
					do {
						try {
							// JsonObject的解析;
							obj 			= array.getJSONObject(i);
							int    number	= (nCurrentPage-1)*nCountLimit+(i+1);
							String id		= 	obj.getString("id");
							String bid		= 	obj.getString("bid");
							String iid		=	obj.getString("iid");
							String iname	=	obj.getString("iname");
							String borrower=	obj.getString("borrower");
							String btime	=	obj.getString("btime");
							String deadline=	obj.getString("deadline");
							String state	=	obj.getString("state");
							String outstate =	obj.getString("outstate");
							String instate	=	obj.getString("instate");
							String inimg	=	obj.getString("inimg");
							
							Map<String, String> map=new HashMap<String, String>();
							map.put("number", number+"");

							map.put("id", id);
							map.put("bid", bid);
							map.put("iid", iid);
							map.put("iname", iname);
							map.put("borrower", borrower);
							map.put("btime", btime);
							map.put("deadline", deadline);
							map.put("state", state);
							map.put("outstate", outstate);
							map.put("instate", instate);
							map.put("inimg", inimg);
							
							map.put("content",bid+"  ·  "+borrower+"  ·  "+btime+"  ·  "+state);
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
