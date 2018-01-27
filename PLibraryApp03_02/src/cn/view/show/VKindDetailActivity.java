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

public class VKindDetailActivity extends Activity implements OnClickListener{
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
		String kid 		 = map.get("kid");
		String kname 	 = map.get("kname");
		String note	 	 = map.get("note");
		String item		 = "书类编号:"+kid+"\r\n" +
						   "书类名称:"+kname+"\r\n" +
						   "书类备注:\r\n"+note 
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
