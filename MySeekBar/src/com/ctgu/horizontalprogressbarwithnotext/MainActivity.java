package com.ctgu.horizontalprogressbarwithnotext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;

import com.ctgu.horizontalprogressbarwithnotext.MySeekBar.ProgressChangedListener;

/**
 * 测试界面
 */
public class MainActivity extends Activity
{
	private MySeekBar bar;
	private SeekBar seekbar;

	// private int colors[] = { Color.RED, Color.GREEN };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = (MySeekBar) findViewById(R.id.progressBar);
		// bar.setColorArray(colors); // 传入一个渐变色数组，更改默认的拖动条样式，是可选项
		bar.setOnProgressChangeListener(new ProgressChangedListener()
		{
			@Override
			public void onProgressChanged(float progress)
			{
				bar.setProgress(progress);
			}
		});

		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				bar.setProgress(progress);
			}
		});
	}
}
