package com.example.circleprogressdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class CircleProgress extends View{
	private Paint progressPaint,textPaint,centreSectionPaint,progressBgPaint;
	private RectF rangeRect;
	/**
	 * 默认进度条颜色
	 */
	private static final int DEFUALT_PROGRESS_COLOR = 0xff287737;
	/**
	 * 默认进度条宽度
	 */
	private static final int DEFUALT_PROGRESS_WIDTH = 10;
	/**
	 * 最大进度值
	 */
	private static final int DEFUALT_MAX_PROGRESS = 100;
	/**
	 * 默认字体颜色
	 */
	private static final int DEFUALT_TEXT_COLOR = Color.GRAY;
	/**
	 * 默认进度条内区域颜色
	 */
	private static final int DEFUALT_CENTRE_SECTION_COLOR = Color.DKGRAY;
	/**
	 * 默认进度条半径
	 */
	private static final float RADIUS = 50;
	private static final int DEFUALT_PROGRESS_BG_COLOR = 0x44000000;
	private float radius,progress,centreSectionRad;
	private int maxProgress;
	private int progressColor,progressBgColor,centreSectionColor,textColor;
	private float textSize,progressWidth;
	private float unitProgress;
	private int styleValue = 1;
	private boolean drawCentreSection;
	public CircleProgress(Context context) {
		super(context);
		radius = RADIUS;
		progressWidth = DEFUALT_PROGRESS_WIDTH;
		maxProgress = DEFUALT_MAX_PROGRESS;
		progressColor = DEFUALT_PROGRESS_COLOR;
		centreSectionColor = DEFUALT_CENTRE_SECTION_COLOR;
		textColor = DEFUALT_TEXT_COLOR;
		progressBgColor = DEFUALT_PROGRESS_BG_COLOR;
		init();
	}
	public CircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);
		radius = type.getDimension(R.styleable.CircleProgress_radius, RADIUS);
		progressWidth = type.getDimension(R.styleable.CircleProgress_progressWidth, DEFUALT_PROGRESS_WIDTH);
		maxProgress = type.getInt(R.styleable.CircleProgress_maxProgress, DEFUALT_MAX_PROGRESS);
		textSize = type.getDimension(R.styleable.CircleProgress_textSize, 0);
		progressColor = type.getColor(R.styleable.CircleProgress_progressColor, DEFUALT_PROGRESS_COLOR);
		centreSectionColor = type.getColor(R.styleable.CircleProgress_centreSectionColor, DEFUALT_CENTRE_SECTION_COLOR);
		textColor = type.getColor(R.styleable.CircleProgress_textColor, DEFUALT_TEXT_COLOR);
		styleValue = type.getInt(R.styleable.CircleProgress_style, 1);
		progressBgColor = type.getColor(R.styleable.CircleProgress_progressBgColor, DEFUALT_PROGRESS_BG_COLOR);
		init();
		type.recycle();
	}
	
	private void init() {
		//圆形所在范围
		rangeRect = new RectF();
		
		progressPaint = new Paint();
		progressPaint.setColor(progressColor);
		progressPaint.setAntiAlias(true);
		progressPaint.setStrokeWidth(progressWidth);
		
		progressBgPaint = new Paint(progressPaint);
		progressBgPaint.setColor(progressBgColor);
		
		textPaint = new Paint();
		textPaint.setColor(textColor);
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(textSize);
		
		centreSectionPaint = new Paint();
		centreSectionPaint.setColor(centreSectionColor);
		centreSectionPaint.setAntiAlias(true);
		
		setMaxProgress(maxProgress);
		setStyle(styleValue);
		initCentreSectionRad();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		if(drawCentreSection){
			canvas.drawCircle(xMid, yMid, centreSectionRad, centreSectionPaint);
		}
		drawArc(canvas, progress, !drawCentreSection);
		drawText(canvas, (int)progress + "%");
	}
	
	/**
	 * 中间区域半径
	 */
	private void initCentreSectionRad() {
		centreSectionRad = radius - progressWidth/2 + 1;
		setTextSize(centreSectionRad/2);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		xMid = getMeasuredWidth() / 2;
		yMid = getMeasuredHeight() / 2;
		rangeRect.set(xMid - radius, yMid - radius, xMid + radius, yMid + radius);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width,height);
	}
	
	/**
	 * 计算宽度
	 * @param measureSpec
	 * @return
	 */
	private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            result = (int)(2*radius + progressWidth) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }
	
	private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int)(2*radius + progressWidth) +  + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
	
	private int xMid;
	private int yMid;
	private void drawArc(Canvas canvas,float sweepAngle,boolean useCenter) {
		canvas.drawArc(rangeRect, 0, 360, useCenter, progressBgPaint);
		canvas.drawArc(rangeRect, 270, unitProgress*sweepAngle,useCenter, progressPaint);
	}
	
	/**
	 * 绘制进度文字
	 * @param canvas
	 * @param text
	 */
	private void drawText(Canvas canvas,String text) {
		float width = textPaint.measureText(text);
		//文字中心与视图中心重合xMid - width/2，yMid + textSize/2
		canvas.drawText(text, xMid - width/2, yMid + textSize/2, textPaint);
	}
	
	private void setStyle(int styleValue){
		Style style;
		if(CircleProgressStyle.Fill.value == styleValue){
			style = Style.FILL;
			drawCentreSection = false;
		}else{
			style = Style.STROKE;
			drawCentreSection = true;
		}
		progressPaint.setStyle(style);
		progressBgPaint.setStyle(style);
		invalidate();
	}
	
	public enum CircleProgressStyle{
		/**
		 * 圆环型进度条
		 */
		Stroke(1),
		/**
		 * 扇形进度条
		 */
		Fill(2);
		int value;
		CircleProgressStyle(int value){
			this.value = value;
		}
	}
	
	
	
	/**
	 * @param progress
	 */
	public void setProgress(float progress) {
		setProgress(progress, true);
	}
	
	/**
	 * @param progress 进度值
	 * @param fadeOut 当进度条满时，是否自动淡出
	 */
	public void setProgress(float progress,boolean fadeOut){
		if(fadeOut && progress == maxProgress){
			this.setVisibility(GONE, true);
		}else{
			if(progress > maxProgress){
				return;
			}
		}
		this.progress = progress;
		invalidate();
	}
	
	/**
	 * 获取进度值
	 * @return
	 */
	public float getProgress() {
		return progress;
	}
	/**
	 * 最大进度值，默认为100
	 * @param maxProgress
	 */
	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
		unitProgress = 360f/ maxProgress;
	}
	/**
	 * 获取最大进度值
	 * @return
	 */
	public int getMaxProgress() {
		return maxProgress;
	}
	/**
	 * 设置半径大小
	 * @param raduio
	 */
	public void setRaduio(float raduio) {
		this.radius = raduio;
		initCentreSectionRad();
		requestLayout();
	}
	
	/**
	 * 获取半径
	 * @return
	 */
	public float getRaduio() {
		return radius;
	}
	
	/**
	 * 设置进度条宽度
	 * @param progressWidth
	 */
	public void setProgressWidth(float progressWidth) {
		this.progressWidth = progressWidth;
		progressPaint.setStrokeWidth(progressWidth);
		progressBgPaint.setStrokeWidth(progressWidth);
		setRaduio(centreSectionRad + progressWidth/2 - 1);
	}
	/**
	 * 获取进度条宽度
	 * @return
	 */
	public float getProgressWidth() {
		return progressWidth;
	}
	
	
	/**
	 * 设置字体大小
	 * @param textSize
	 */
	public void setTextSize(float textSize) {
		this.textSize = textSize;
		textPaint.setTextSize(textSize);
	}
	public float getTextSize() {
		return textSize;
	}
	
	/**
	 * 设置进度条颜色
	 * @param progressColor
	 */
	public void setProgressColor(int progressColor) {
		this.progressColor = progressColor;
		progressPaint.setColor(progressColor);
		invalidate();
	}
	public int getProgressColor() {
		return progressColor;
	}
	/**
	 * 设置字体颜色
	 * @param textColor
	 */
	public void setTextColor(int textColor) {
		this.textColor = textColor;
		textPaint.setColor(textColor);
		invalidate();
	}
	public int getTextColor() {
		return textColor;
	}
	/**
	 * 设置进度条内的颜色
	 * @param centreSectionColor
	 */
	public void setCentreSectionColor(int centreSectionColor) {
		this.centreSectionColor = centreSectionColor;
		centreSectionPaint.setColor(centreSectionColor);
		invalidate();
	}
	public int getCentreSectionColor() {
		return centreSectionColor;
	}
	
	/**
	 * 设置进度条样式
	 * @param style CircleProgressStyle.Stroke环形进度条;CircleProgressStyle.Fill 扇形进度条
	 */
	public void setStyle(CircleProgressStyle style) {
		setStyle(style.value);
	}
	
	/**
	 * @param visibility
	 * @param fadeAnim 是否播放淡入/淡出动画 。如果设置为true，会有动画效果。否则，没有动画效果
	 */
	public void setVisibility(int visibility,boolean fadeAnim){
		if(visibility == this.getVisibility()){
			return;
		}
		if(fadeAnim){
			if(visibility == INVISIBLE || visibility == GONE){
				alphaAnim(1, 0, visibility);
			}else{
				alphaAnim(0, 1, visibility);
			}
		}else{
			super.setVisibility(visibility);
		}
	}
	
	@Override
	public void setVisibility(int visibility) {
		this.setVisibility(visibility, true);
	}
	/**
	 * 淡入淡出动画
	 * @param from
	 * @param to
	 * @param visibility
	 */
	private void alphaAnim(float from,float to,final int visibility){
		AlphaAnimation alpha = new AlphaAnimation(from, to);
		alpha.setDuration(500);
		alpha.setStartOffset(1000);
		alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				CircleProgress.super.setVisibility(visibility);
			}
		});
		this.startAnimation(alpha);
	}
}

