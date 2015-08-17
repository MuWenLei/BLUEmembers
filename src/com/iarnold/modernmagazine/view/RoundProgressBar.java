package com.iarnold.modernmagazine.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.iarnold.modernmagazine.R;

/**
 * 锟斤拷iphone锟斤拷锟斤拷锟饺的斤拷锟斤拷锟斤拷锟斤拷锟竭程帮拷全锟斤拷View锟斤拷锟斤拷直锟斤拷锟斤拷锟竭筹拷锟叫革拷锟铰斤拷锟斤拷
 * @author xiaanming
 *
 */
public class RoundProgressBar extends View {
	/**
	 * 锟斤拷锟绞讹拷锟斤拷锟斤拷锟斤拷锟�
	 */
	private Paint paint;
	
	/**
	 * 圆锟斤拷锟斤拷锟斤拷色
	 */
	private int roundColor;
	
	/**
	 * 圆锟斤拷锟斤拷锟饺碉拷锟斤拷色
	 */
	private int roundProgressColor;
	
	/**
	 * 锟叫硷拷锟斤拷劝俜直鹊锟斤拷址锟斤拷锟斤拷锟斤拷锟缴�
	 */
	private int textColor;
	
	/**
	 * 锟叫硷拷锟斤拷劝俜直鹊锟斤拷址锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 */
	private float textSize;
	
	/**
	 * 圆锟斤拷锟侥匡拷锟�
	 */
	private float roundWidth;
	
	/**
	 * 锟斤拷锟斤拷锟斤拷
	 */
	private int max;
	
	/**
	 * 锟斤拷前锟斤拷锟斤拷
	 */
	private int progress;
	/**
	 * 锟角凤拷锟斤拷示锟叫硷拷慕锟斤拷锟�
	 */
	private boolean textIsDisplayable;
	
	/**
	 * 锟斤拷锟饺的凤拷锟绞碉拷幕锟斤拷呖锟斤拷锟�
	 */
	private int style;
	
	public static final int STROKE = 0;
	public static final int FILL = 1;
	
	public RoundProgressBar(Context context) {
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();

		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);
		
		//锟斤拷取锟皆讹拷锟斤拷锟斤拷锟皆猴拷默锟斤拷值
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		
		mTypedArray.recycle();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/**
		 * 锟斤拷锟斤拷锟斤拷锟侥达拷圆锟斤拷
		 */
		int centre = getWidth()/2; //锟斤拷取圆锟侥碉拷x锟斤拷锟斤拷
		int radius = (int) (centre - roundWidth/2); //圆锟斤拷锟侥半径
		paint.setColor(roundColor); //锟斤拷锟斤拷圆锟斤拷锟斤拷锟斤拷色
		paint.setStyle(Paint.Style.STROKE); //锟斤拷锟矫匡拷锟斤拷
		paint.setStrokeWidth(roundWidth); //锟斤拷锟斤拷圆锟斤拷锟侥匡拷锟�
		paint.setAntiAlias(true);  //锟斤拷锟斤拷锟斤拷锟� 
		canvas.drawCircle(centre, centre, radius, paint); //锟斤拷锟斤拷圆锟斤拷
		
		Log.e("log", centre + "");
		
		/**
		 * 锟斤拷锟斤拷锟饺百分憋拷
		 */
		paint.setStrokeWidth(0); 
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); //锟斤拷锟斤拷锟斤拷锟斤拷
		int percent = (int)(((float)progress / (float)max) * 100);  //锟叫硷拷慕锟斤拷劝俜直龋锟斤拷锟阶拷锟斤拷锟絝loat锟节斤拷锟叫筹拷锟斤拷锟斤拷锟姐，锟斤拷然锟斤拷为0
		float textWidth = paint.measureText(percent + "%");   //锟斤拷锟斤拷锟斤拷锟斤拷锟饺ｏ拷锟斤拷锟斤拷锟斤拷要锟斤拷锟斤拷锟斤拷锟斤拷目锟斤拷锟斤拷锟斤拷锟斤拷圆锟斤拷锟叫硷拷
		
		if(textIsDisplayable && percent != 0 && style == STROKE){
			canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint); //锟斤拷锟斤拷锟斤拷锟饺百分憋拷
		}
		
		
		/**
		 * 锟斤拷圆锟斤拷 锟斤拷锟斤拷圆锟斤拷锟侥斤拷锟斤拷
		 */
		
		//锟斤拷锟矫斤拷锟斤拷锟斤拷实锟侥伙拷锟角匡拷锟斤拷
		paint.setStrokeWidth(roundWidth); //锟斤拷锟斤拷圆锟斤拷锟侥匡拷锟�
		paint.setColor(roundProgressColor);  //锟斤拷锟矫斤拷锟饺碉拷锟斤拷色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);  //锟斤拷锟节讹拷锟斤拷锟皆诧拷锟斤拷锟斤拷锟阶达拷痛锟叫★拷慕锟斤拷锟�
		
		switch (style) {
		case STROKE:{
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 0, 360 * progress / max, false, paint);  //锟斤拷锟捷斤拷锟饺伙拷圆锟斤拷
			break;
		}
		case FILL:{
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //锟斤拷锟捷斤拷锟饺伙拷圆锟斤拷
			break;
		}
		}
		
	}
	
	
	public synchronized int getMax() {
		return max;
	}

	/**
	 * 锟斤拷锟矫斤拷锟饺碉拷锟斤拷锟街�
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 锟斤拷取锟斤拷锟斤拷.锟斤拷要同锟斤拷
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 锟斤拷锟矫斤拷锟饺ｏ拷锟斤拷为锟竭程帮拷全锟截硷拷锟斤拷锟斤拷锟节匡拷锟角讹拷锟竭碉拷锟斤拷锟解，锟斤拷要同锟斤拷
	 * 刷锟铰斤拷锟斤拷锟斤拷锟絧ostInvalidate()锟斤拷锟节凤拷UI锟竭筹拷刷锟斤拷
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
		
	}
	
	
	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}



}
