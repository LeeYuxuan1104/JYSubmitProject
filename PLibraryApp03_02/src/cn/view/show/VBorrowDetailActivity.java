package cn.view.show;

import java.util.Map;

import com.example.plibraryapp01.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VBorrowDetailActivity extends Activity implements OnClickListener{
	private Button	 vBack,vIn;
	private TextView vTopic,vDetail;
	private Context	 mContext;
	private String   iid,bid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vuser_detail);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vIn=(Button) findViewById(R.id.btnFunction);
		vTopic=(TextView) findViewById(R.id.tvTopic);
	}
	private void initEvent(){
		mContext=VBorrowDetailActivity.this;
		vBack.setText(R.string.back);
		vIn.setText(R.string.in);
		vBack.setOnClickListener(this);
		vIn.setVisibility(View.VISIBLE);
		vIn.setOnClickListener(this);
		vTopic.setText(R.string.detail);
		vDetail=(TextView) findViewById(R.id.detail);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		@SuppressWarnings("unchecked")
		Map<String, String> map=(Map<String, String>) bundle.getSerializable("item");

		bid				= 	map.get("bid");
		iid				=	map.get("iid");
		String iname	=	map.get("iname");
		String borrower=	map.get("borrower");
		String btime	=	map.get("btime");
		String deadline=	map.get("deadline");
		String state	=	map.get("state");
		String outstate=	map.get("outstate");
		String instate	=	map.get("instate");
		String inimg	=	map.get("inimg");
		
		String item		 = "流水编号:"+bid+"\r\n" +
						   "书目编号:"+iid+"\r\n" +
						   "书目名称:"+iname+"\r\n"+
						   "借阅人:"+borrower+"\r\n" +
						   "借出时间:"+btime+"\r\n" +
						   "截止时间:"+deadline+"\r\n" +
						   "借阅状态:"+state+"\r\n" +
						   "借出状态:"+outstate+"\r\n" +
						   "归还状态:"+instate+"\r\n" +
						   "归还图片:"+inimg
						   ;
		vDetail.setText(item);
	}

	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			finish();
			break;
		case R.id.btnFunction:
			Intent	intent=new Intent(mContext, VBorrowBackActivity.class);
			Bundle	bundle=new Bundle();
			bundle.putString("iid", iid);
			bundle.putString("bid", bid);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
}
