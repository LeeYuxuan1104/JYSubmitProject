package cn.view.show;

import java.util.Map;

import com.example.plibraryapp01.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VItemDetailActivity extends Activity implements OnClickListener{
	private Button	 vBack;
	private TextView vTopic,vDetail;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vuser_detail);
		initView();
		initEvent();
	}
	
	private void initView(){
		vBack=(Button) findViewById(R.id.btnBack);
		vTopic=(TextView) findViewById(R.id.tvTopic);
	}
	private void initEvent(){
		vBack.setText(R.string.back);
		vBack.setOnClickListener(this);
		vTopic.setText(R.string.detail);
		vDetail=(TextView) findViewById(R.id.detail);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		@SuppressWarnings("unchecked")
		Map<String, String> map=(Map<String, String>) bundle.getSerializable("item");

		String iid=map.get("iid");
		String iname=map.get("iname");
		String note=map.get("note");
		String author=map.get("author");
		String press=map.get("press");
		String ptime=map.get("ptime");
		String count=map.get("count");
		String kid=map.get("kid");
		String img=map.get("img");
		String item		 = "书目编号:"+iid+"\r\n" +
						   "书目名称:"+iname+"\r\n" +
						   "书目备注:\r\n"+note+"\r\n"+
						   "作者:"+author+"\r\n" +
						   "出版社:"+press+"\r\n" +
						   "出版时间:"+ptime+"\r\n" +
						   "总计:"+count+"本\r\n" +
						   "类别:"+kid+"\r\n" +
						   "图片:\r\n"+img
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
		default:
			break;
		}
		
	}
}
