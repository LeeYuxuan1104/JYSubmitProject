package cn.view.show;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.model.tool.common.MTConfiger;

import com.example.plibraryapp01.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class VStateDetailActivity extends Activity implements OnClickListener{
	private Context				 mContext;
	private Button	 			 vBack;
	private TextView 			 vTopic,vDetail;
	private Gallery	 			 vGallery;		//	画廊按钮;
	private ImageAdaper			 mAdaper;
	private List<BitmapDrawable> listBD = null;	// 承装图片的列表;
	private MTConfiger			 mtConfiger;
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
		vGallery=(Gallery) findViewById(R.id.gallery);
	}
	private void initEvent(){
		mContext	=	VStateDetailActivity.this;
		mtConfiger	=	new MTConfiger();
		vBack.setText(R.string.back);
		vBack.setOnClickListener(this);
		vTopic.setText(R.string.detail);
		vDetail=(TextView) findViewById(R.id.detail);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		@SuppressWarnings("unchecked")
		Map<String, String> map=(Map<String, String>) bundle.getSerializable("item");
		
		String iid		= 	map.get("iid");
		String count	=	map.get("count");
		String ccount	=	map.get("ccount");
		String ebids	=	map.get("ebids");
		
		
		String item		 = "书目编号:"+iid+"\r\n" +
						   "书目总数:"+count+"\r\n"+
						   "当前数目:"+ccount+"\r\n" +
						   "异常件数:"+ebids+"\r\n" 
						   ;
		vDetail.setText(item);
		String folderpath= mtConfiger.getfParentPath() + "borrow"+ File.separator + iid;
		listBD			=	getBitmap01_2(folderpath);
		if(listBD!=null){			
			vGallery.setVisibility(View.VISIBLE);
			mAdaper			=	new ImageAdaper(mContext, listBD);
			vGallery.setAdapter(mAdaper); 
		}
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
	
	//	适配器的类;
	public class ImageAdaper extends BaseAdapter{  
        private Context mContext;  
        private int 	mGalBackgroundItem;
        private int 	nSize;
        private List<BitmapDrawable> listBD;
        
        public ImageAdaper(Context mContext,List<BitmapDrawable> list){  
            this.mContext = mContext;  
            this.listBD	  = list;
            this.nSize	  = list.size();
            TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);  
            mGalBackgroundItem 	  = typedArray.getResourceId( R.styleable.Gallery_android_galleryItemBackground, 0);  
            typedArray.recycle();
        }  

        public int getCount() {  
            return nSize;  
        }  
  
        public Object getItem(int position) {  
            return listBD.get(position);  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  

		public View getView(int position, View convertView, ViewGroup parent) {  
            
            ImageView imageview = new ImageView(mContext); 
            imageview.setImageDrawable(listBD.get(position));	            	
            
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);  
            imageview.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,400));  
            imageview.setBackgroundResource(mGalBackgroundItem);  
            notifyDataSetChanged();
            return imageview;   
        }            
    }
	
	public List<BitmapDrawable> getBitmap01_2(String folderpath){
		List<BitmapDrawable> list =	new ArrayList<BitmapDrawable>();
		File   file		=	null;
		File[] tempList = 	null;
		file			=	new File(folderpath);
		if(file!=null){			
			tempList		=	file.listFiles();
			if(tempList!=null){				
				for(File ifile:tempList){
					String	path=	ifile.getPath();
					
					Bitmap 	bm 	= 	BitmapFactory.decodeFile(path); 
					BitmapDrawable bd=new BitmapDrawable(bm);
					list.add(bd);
				}
			}
		}
		return list;
	}
}
