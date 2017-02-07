package com.hudawei.buttonsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by hudawei on 2017/2/7.
 * 1.点击的时候记录点击的坐标pointX、pointY,触发绘制方法
 * 2.根据点击坐标和控件的宽高viewWidth,viewHeight计算出此次圆的最大半径maxRadius
 * 3.绘制背景，画矩形，根据viewWidth,viewHeight
 * 4.绘制涟漪效果，点击时圆的半径为minRadius。
 * a.每隔单位时间REPEAT_TIME，半径增加INCREMENT_RADIUS,不断调用绘制方法
 * b.直到等于圆的半径等于maxRadius停止
 */

public class RipplesButton extends Button {
    private float touchPointX;
    private float touchPointY;
    private int viewWidth;
    private int viewHeight;
    private float maxRadius;
    private float minRadius;
    private float curRadius;
    private final long REPEAT_TIME=50;
    private final int INCREMENT_RADIUS=20;
    private Paint backPaint;
    private Paint forePaint;
    public RipplesButton(Context context) {
        super(context);
    }

    public RipplesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RipplesButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(){
        backPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(getResources().getColor(R.color.colorPrimary));

        forePaint=new Paint(backPaint);
        forePaint.setColor(getResources().getColor(R.color.colorAccent));
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击的时候，记录点击的位置,触发绘制流程
        int action=event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                touchPointX=event.getX();
                touchPointY=event.getY();
                calcMaxRadius();
                postInvalidateDelayed(REPEAT_TIME);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void calcMaxRadius(){
        if(viewWidth>viewHeight){
            minRadius=viewHeight/2;
            int tempX=viewWidth/2>touchPointX?(int)(viewWidth-touchPointX):(int)touchPointX;
            maxRadius=(float)Math.sqrt(Math.pow(minRadius,2)+Math.pow(tempX,2));
        }else{
            minRadius=viewWidth/2;
            int tempY=viewHeight/2>touchPointY?(int)(viewHeight-touchPointY):(int)touchPointY;
            maxRadius=(float)Math.sqrt(Math.pow(minRadius,2)+Math.pow(tempY,2));
        }
        curRadius=minRadius;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制背景色
        canvas.drawRect(0,0,viewWidth,viewHeight,backPaint);
        canvas.save();
        //绘制涟漪
        canvas.clipRect(0,0,viewWidth,viewHeight);
        canvas.drawCircle(touchPointX,touchPointY,curRadius,forePaint);
        if(curRadius==maxRadius)
            canvas.drawRect(0,0,viewWidth,viewHeight,backPaint);
        canvas.restore();
        if(curRadius<maxRadius){
            curRadius=curRadius+INCREMENT_RADIUS>maxRadius?maxRadius:curRadius+INCREMENT_RADIUS;
            postInvalidateDelayed(REPEAT_TIME);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
    }
}
