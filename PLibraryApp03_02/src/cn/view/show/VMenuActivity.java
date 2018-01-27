package cn.view.show;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.model.tool.common.MTConfiger;
import cn.model.tool.video.AbstructProvider;
import cn.model.tool.video.Video;
import cn.model.tool.video.VideoProvider;

import com.example.plibraryapp01.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VMenuActivity extends Activity implements OnClickListener{
	private Context		mContext;
	private Intent 		mIntent;
	private Button 		vBack,vUserinfo,vKindinfo,vIteminfo,vBorrowinfo,vStateinfo;
	private TextView 	vTopic;
	private MTConfiger	mtConfiger;
	////	视频部分;
	private Button bt_play;
	private Button bt_pause;
	private Button bt_stop;
	private int currentPosition;
	private Button bt_replay;
	private SurfaceView sv_vedio;
	private MediaPlayer mediaplayer;
	private SeekBar sb_progress;
	private boolean flag;
	private TextView tv_time;
	private LinearLayout vLayout;
	//////TODO
	private Handler handler;
	List<Video> listVideos = new ArrayList<Video>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_vmenu);
		initView();
		initEvent();
		initVideo();
	}
	
	private void initView(){
		vTopic=(TextView) findViewById(R.id.tvTopic);
		vBack=(Button) findViewById(R.id.btnBack);
		
		vUserinfo=(Button) findViewById(R.id.btnUserinfo);
		vKindinfo=(Button) findViewById(R.id.btnKindinfo);
		vIteminfo=(Button) findViewById(R.id.btnIteminfo);
		vBorrowinfo=(Button) findViewById(R.id.btnBorrowinfo);
		vStateinfo=(Button) findViewById(R.id.btnStateinfo);
		vLayout=(LinearLayout) findViewById(R.id.lt);
	}
	
	private void initEvent(){
		mContext	=	VMenuActivity.this;
		mtConfiger	=	new MTConfiger();
		vTopic.setText(R.string.menuinfo);
		vBack.setText(R.string.exit);
		vBack.setOnClickListener(this);
		
		vUserinfo.setOnClickListener(this);
		vKindinfo.setOnClickListener(this);
		vIteminfo.setOnClickListener(this);
		vBorrowinfo.setOnClickListener(this);
		vStateinfo.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("HandlerLeak")
	private void initVideo(){
		bt_play = (Button) findViewById(R.id.bt_play);
		bt_pause = (Button) findViewById(R.id.bt_pause);
		bt_stop = (Button) findViewById(R.id.bt_stop);
		bt_replay = (Button) findViewById(R.id.bt_replay);
		tv_time = (TextView) findViewById(R.id.tv_time);
		AbstructProvider provider = new VideoProvider(this);
		listVideos = provider.getList();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					int a = msg.getData().getInt("1");
					int b = msg.getData().getInt("2");
					set(b, a);
				}
			}

			private void set(int progress, int max) {
				// TODO 自动生成的方法存根
				tv_time.setText(toTime(progress) + "/" + toTime(max));
			}

			private String toTime(int progress) {
				// TODO 自动生成的方法存根
				StringBuffer sb = new StringBuffer();
				int s = (progress / 1000) % 60;
				int m = progress / 60000;
				sb.append(m).append(":");
				if (s < 10) {
					sb.append(0);
				}
				sb.append((progress / 1000) % 60);
				return sb.toString();
			}
		};
		bt_pause.setEnabled(false);
		bt_stop.setEnabled(false);
		sb_progress = (SeekBar) findViewById(R.id.sb_progress);
		sb_progress.setEnabled(false);
		sb_progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO 自动生成的方法存根
				if (mediaplayer != null) {
					int progress = seekBar.getProgress();
					mediaplayer.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO 自动生成的方法存根
			}
		});
		sv_vedio = (SurfaceView) findViewById(R.id.sv_video);
		sv_vedio.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		sv_vedio.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO 自动生成的方法存根
				System.out.println("销毁了");
				if (mediaplayer != null && mediaplayer.isPlaying()) {
					currentPosition = mediaplayer.getCurrentPosition();
				}
				stop();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO 自动生成的方法存根
				System.out.println("创建了");
				if (currentPosition > 0) {
					play(currentPosition);
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO 自动生成的方法存根
				System.out.println("大小改变了");
			}
		});
		bt_play.setOnClickListener(this);
		bt_pause.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		bt_replay.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View view) {
		int nVid=view.getId();
		switch (nVid) {
		case R.id.btnBack:
			mtConfiger.exitSystem(VMenuActivity.this);
			break;
		case R.id.btnUserinfo:
			mIntent=new Intent(mContext, VUserinfoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btnKindinfo:
			mIntent=new Intent(mContext, VKindinfoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btnIteminfo:
			mIntent=new Intent(mContext, VIteminfoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btnBorrowinfo:
			mIntent=new Intent(mContext, VBorrowinfoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btnStateinfo:
			mIntent=new Intent(mContext, VStateinfoActivity.class);
			startActivity(mIntent);
			break;
		case R.id.bt_play:
			vLayout.setVisibility(View.GONE);
			play(0);
			break;
		case R.id.bt_pause:
			pause();
			break;
		case R.id.bt_stop:
			vLayout.setVisibility(View.VISIBLE);
			stop();
			break;
		case R.id.bt_replay:
			vLayout.setVisibility(View.GONE);
			replay();
			break;
		default:
			break;
		}
		
	}
	private void replay() {
		// TODO 自动生成的方法存根
		if (mediaplayer != null && mediaplayer.isPlaying()) {
			mediaplayer.seekTo(0);
		} else {
			play(0);
		}
	}

	private void stop() {
		// TODO 自动生成的方法存根
		if (mediaplayer != null) {
			mediaplayer.stop();
			mediaplayer.release();
			mediaplayer = null;
			bt_play.setEnabled(true);
			bt_pause.setEnabled(false);
			bt_stop.setEnabled(false);
			flag = false;
			bt_pause.setText("暂停");
		} else {
			Toast.makeText(this, "请先播放音频！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 停止
	 */
	private void pause() {
		// 如果当前按钮显示为继续，点击之后开始播放
		if (bt_pause.getText().toString().trim().equals("继续")) {
			mediaplayer.start();
			bt_pause.setText("暂停");
			return;
		// 如果当前按钮显示为暂停，点击之后暂停
		} else {
			if (mediaplayer != null && mediaplayer.isPlaying()) {
				mediaplayer.pause();
				bt_pause.setText("继续");
			} else {
				Toast.makeText(this, "请先播放音频！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 *  播放视频
	 * @param currentPosition2
	 */
	private void play(final int currentPosition2) {

		String path = listVideos.get(0).getPath();
		File file = new File(path);
		try {
			mediaplayer = new MediaPlayer();
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDisplay(sv_vedio.getHolder());
			// 给播放器加载资源
			mediaplayer.setDataSource(path);
			mediaplayer.prepareAsync();
			bt_play.setEnabled(false);
			bt_pause.setEnabled(true);
			bt_stop.setEnabled(true);
			sb_progress.setEnabled(true);
			mediaplayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaplayer.start();
					final int max = mediaplayer.getDuration();
					sb_progress.setMax(max);
					mediaplayer.seekTo(currentPosition2);
					flag = true;

					new Thread() {
						public void run() {
							while (flag) {
								int progress = mediaplayer.getCurrentPosition();
								sb_progress.setProgress(progress);
								Message message = new Message();
								Bundle bundle = new Bundle();
								message.setData(bundle);
								bundle.putInt("1", max);
								bundle.putInt("2", progress);
								message.what = 0;
								handler.sendMessage(message);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							}
						}
					}.start();
				}
			});
			mediaplayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO 自动生成的方法存根
					bt_play.setEnabled(true);
				}
			});
			mediaplayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO 自动生成的方法存根
					bt_play.setEnabled(true);
					flag = false;
					return false;
				}
			});
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			Toast.makeText(this, "播放失败！",Toast.LENGTH_SHORT).show();
		}
	}
}
