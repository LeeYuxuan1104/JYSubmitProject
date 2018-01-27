package com.ctgu.horizontalprogressbarwithnotext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 水平渐变色拖动条
 * 
 */
public class MySeekBar extends View
{
	/**
	 * 进度条填充起始色
	 */
	protected int startFillColor;

	/**
	 * 进度条填充中间色
	 */
	protected int middleFillColor;

	/**
	 * 进度条填充结束颜色
	 */
	protected int endFillColor;

	/**
	 * 指示圆点颜色
	 */
	private int progressPointColor;

	/**
	 * 进度条未填充颜色
	 */
	protected int backgroundColor;

	/**
	 * 进度条长度
	 */
	protected float progressWidth;

	/**
	 * 进度条的高度，包括进度条的高度和上下的间隔
	 */
	private float progressHeight;

	/**
	 * 进度条的高度，不包括上下的间隔
	 */
	private float lineWidth;

	/**
	 * 进度条左侧间隔
	 */
	private float widthSpace;

	/**
	 * 进度条中心与上方的间隔
	 */
	private float heightSpace;

	/**
	 * 內圆的半径
	 */
	private float radius;

	/**
	 * 按比例计算各部分的值
	 */
	private float unit;

	/**
	 * 整个控件宽度
	 */
	protected int viewWidth;

	/**
	 * 整个控件高度
	 */
	protected int viewHeight;

	/**
	 * 渐变色数组
	 */
	private int colorArray[];

	/**
	 * 画笔
	 */
	protected Paint paint;

	/**
	 * 控件当前的进度
	 */
	private float progress = 0;

	/**
	 * 处理颜色渐变
	 */
	private LinearGradient shader;

	/**
	 * 自定义的监听器，负责监听控件的触摸事件
	 */
	private ProgressChangedListener listener;

	/**
	 * @param context
	 *            上下文
	 */
	public MySeekBar(Context context)
	{
		this(context, null);
	}

	/**
	 * @param context上下文
	 * @param attrs
	 *            自定义的属性
	 */
	public MySeekBar(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 *            上下文
	 * @param attrs
	 *            自定义的属性
	 * @param defStyleAttr
	 *            自定义的风格
	 */
	public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);

		colorArray = new int[3];
		paint = new Paint();

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.progressBar);
		int n = ta.getIndexCount();
		for (int i = 0; i < n; i++)
		{
			int attr = ta.getIndex(i);
			switch (attr)
			{
				case R.styleable.progressBar_startFillColor:
					colorArray[0] = ta.getColor(R.styleable.progressBar_startFillColor, 0xffff0000);
					break;
				case R.styleable.progressBar_middleFillColor:
					colorArray[1] = ta.getColor(R.styleable.progressBar_middleFillColor, 0xffff0000);
					break;
				case R.styleable.progressBar_endFillColor:
					colorArray[2] = ta.getColor(R.styleable.progressBar_endFillColor, 0xffff0000);
					break;
				case R.styleable.progressBar_backgroundColor:
					backgroundColor = ta.getColor(R.styleable.progressBar_backgroundColor, Color.GRAY);
					break;
				case R.styleable.progressBar_progressPointColor:
					progressPointColor = ta.getColor(R.styleable.progressBar_progressPointColor, Color.WHITE);
					break;
				case R.styleable.progressBar_lineWidth:
					lineWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
					break;
			}
		}
		ta.recycle();
	}

	/**
	 * 测量组件高度和宽度
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = 0;
		int height = 0;

		// 设置宽度
		// MeasureSpec.getMode()会得到三个int类型的值分别为:MeasureSpec.EXACTLY
		// MeasureSpec.AT_MOST,MeasureSpec.UNSPECIFIED。
		// MeasureSpec.UNSPECIFIED 未指定，所以可以设置任意大小。
		// MeasureSpec.AT_MOST MeasureExampleView可以为任意大小，但是有一个上限。
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec); // 会解析MeasureSpec值得到父容器width或者height。
		switch (specMode)
		{
			case MeasureSpec.EXACTLY: // 明确指定了
				width = specSize;
				break;
			case MeasureSpec.AT_MOST: // 一般为wrap_content
				width = getPaddingLeft() + getPaddingRight();
				break;
		}

		// 设置高度
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		switch (specMode)
		{
			case MeasureSpec.EXACTLY:
				height = specSize;
				break;
			case MeasureSpec.AT_MOST:
				height = width / 10;
				break;
		}
		// Logger.d("onMeasure():width=" + width + ",height=" + height);
		setMeasuredDimension(width, height);

		viewWidth = getMeasuredWidth(); // 获得视图的宽度
		viewHeight = getMeasuredHeight(); // 获得视图的高度
		unit = Math.min((float) viewWidth / 300, (float) viewHeight / 30); // 按比例计算各部分的值

		progressWidth = 280 * unit; // 进度条的长度
		progressHeight = 30 * unit; // 进度条的高度，包括上下间隔

		widthSpace = 8 * unit; // 进度条左侧间隔
		heightSpace = progressHeight / 2; // float heightSpace 进度条中心与上方的间隔

		radius = lineWidth / 2; // 內圆的半径
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		float percent = ((float) progress) / 100; // 当前进度的百分比
		if (percent > 1)
		{
			percent = 1;
		}

		// 画灰色的底线
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(backgroundColor); // 设置颜色为灰色
		paint.setStrokeWidth(lineWidth); // 设置线的宽度
		paint.setStrokeCap(Paint.Cap.ROUND); // 设置线为圆角
		canvas.drawLine(widthSpace, heightSpace, widthSpace + progressWidth, heightSpace, paint); // 画灰色的底线

		// 画渐变色横线
		shader = new LinearGradient(0, 0, widthSpace + progressWidth, 0, colorArray, null,
				Shader.TileMode.CLAMP);
		paint.setShader(shader);// 设置渐变色
		canvas.drawLine(widthSpace, heightSpace, widthSpace + percent * progressWidth, heightSpace, paint); // 画渐变色进度线条

		// 画圆
		paint.setShader(null);
		paint.setColor(progressPointColor);
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.FILL); // 设置绘画方式为填充
		canvas.drawCircle(widthSpace + percent * progressWidth, heightSpace, radius, paint); // 绘制圆
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				float x = event.getX();
				float y = event.getY();

				if ((x >= (0 - radius)) && (x <= (progressWidth + widthSpace + radius)))
				{
					if (y >= 0 && y <= progressHeight)
					{
						this.progress = x * 100.0f / (progressWidth + widthSpace); // 这里要重新计算进度值
						invalidate(); // 重绘界面
						if (listener != null)
						{
							listener.onProgressChanged(progress); // 在处理触摸事件时传入重新计算过的进度参数，
						}
					}
				}
				return true;
			case MotionEvent.ACTION_UP:
				invalidate(); // 手指抬起时刷新一下页面
				return true;
		}
		return super.onTouchEvent(event);
	}

	public void setOnProgressChangeListener(ProgressChangedListener listener2)
	{
		this.listener = listener2;
	}

	/**
	 * 设置进度条的进度值
	 * 
	 * @param pro
	 *            传入的进度值
	 */
	public void setProgress(float pro)
	{
		if (pro < 0)
		{
			this.progress = 0;
		}
		else if (pro > 100)
		{
			this.progress = 100;
		}
		else
		{
			this.progress = pro;
		}
		invalidate();
	}

	/**
	 * 设置渐变色数组
	 * 
	 * @param colorArray
	 */
	public void setColorArray(int[] colorArray)
	{
		this.colorArray = colorArray;
	}

	public interface ProgressChangedListener
	{
		public void onProgressChanged(float touchX);
	}
}
