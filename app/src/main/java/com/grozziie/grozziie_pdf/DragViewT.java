package com.grozziie.grozziie_pdf;;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import androidx.annotation.NonNull;

public class DragViewT extends TextView implements TextView.OnTouchListener{
    protected int screenWidth;
    protected int screenHeight;
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int dragDirection;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int TOUCH_TWO = 0x21;
    private static final int CENTER = 0x19;
    private SeekBar textSizeseekBar;
    private int offset = 200; //可超出其父控件的偏移量
    protected Paint paint = new Paint();
    private static final int touchDistance = 50; //触摸边界的有效距离
    private Horizon scrollView;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layoutE1;
    private LinearLayout layoutE2;
    private LinearLayout layoutE3;
    private LinearLayout layoutE4;
    private ImageView imageE1;
    private ImageView imageE2;
    private ImageView imageE3;
    private ImageView imageE4;
    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;

    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        //screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        //screenWidth = getResources().getDisplayMetrics().widthPixels;
    }

//    public DragViewX(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        setOnTouchListener(this);
//        initScreenW_H();
//    }
//
//    public DragViewX(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        setOnTouchListener(this);
//        initScreenW_H();
//    }

    public DragViewT(Context context) {
        super(context);
        setOnTouchListener(this);
        initScreenW_H();
    }


//    @Override
//    public void setOnClickListener(View.OnClickListener l) {
//
//
//        super.setOnClickListener(l);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
//                if (getScrollY() > 0 && getScrollY() < maxY)
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                else
//                    getParent().requestDisallowInterceptTouchEvent(false);
                /*if (getScrollY()==0)
                    Log.i("kkk","滑到头了");
                if (getChildAt(0).getMeasuredHeight() == getScrollY() + getHeight())
                    Log.i("kkk","滑到底了");*/
                break;
            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4.0f);
        paint.setStyle(Paint.Style.STROKE);
        // canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
        //  - offset, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        BarcodeActivity.getById=v.getId();
        //BarcodeActivity.DeleteFlag=0;
        BarcodeActivity.getIdFlag=1;
        setBackgroundResource(R.drawable.bg_boarder_xu);

        int action = event.getAction()& MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            //BarcodeActivity.CountClick=0;
            TextView textView=findViewById(v.getId());

            BarcodeActivity.textSize=px2sp(getContext(),textView.getTextSize());
//            Message message = new Message();
//            message.what = 10;
//            Bundle bundle = new Bundle();
//            bundle.putFloat("msg", BarcodeActivity.textSize);
//            message.setData(bundle);
//            handlerM.sendMessage(message);

            BarcodeActivity.testString=textView.getText().toString();
            v.setStateListAnimator(null);
            v.bringToFront();

            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();
            dragDirection = getDirection(v, (int) event.getX(),
                    (int) event.getY());
        }
        if (action == MotionEvent.ACTION_POINTER_DOWN){
//            oriLeft = v.getLeft();
//            oriRight = v.getRight();
//            oriTop = v.getTop();
//            oriBottom = v.getBottom();
//            lastY = (int) event.getRawY();
//            lastX = (int) event.getRawX();
//            dragDirection = TOUCH_TWO;
//            oriDis = distance(event);
        }
        // 处理拖动事件
        delDrag(v, event, action);
        invalidate();
        return false;
    }
    /**
     * 处理拖动事件
     *
     * @param v
     * @param event
     * @param action
     */

    protected void delDrag(View v, MotionEvent event, int action) {
        switch (action) {
            case MotionEvent.ACTION_MOVE:

                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                if(dx>5||dy>5)//if(dx!=0||dy!=0)
                {
                    BarcodeActivity.CountClick=2;
                }
                switch (dragDirection) {
                    case LEFT: // 左边缘
                        //left(v, dx);
                        break;
                    case RIGHT: // 右边缘
                        right(v, dx);
                        break;
                    case BOTTOM: // 下边缘
                        bottom(v, dy);
                        break;
                    case TOP: // 上边缘
                        //top(v, dy);
                        break;
                    case CENTER: // 点击中心-->>移动
                        center(v, dx, dy);
                        break;
                    case LEFT_BOTTOM: // 左下
                        //left(v, dx);
                        // bottom(v, dy);
                        break;
                    case LEFT_TOP: // 左上
                        //left(v, dx);
                        //top(v, dy);
                        break;
                    case RIGHT_BOTTOM: // 右下
                        right(v, dx);
                        bottom(v, dy);
                        break;
                    case RIGHT_TOP: // 右上
                        // right(v, dx);
                        //top(v, dy);
                        break;
                    case TOUCH_TWO: //双指操控
//                        float newDist =distance(event);
//                        float scale = newDist / oriDis;
//                        //控制双指缩放的敏感度
//                        int distX = (int) (scale*(oriRight-oriLeft)-(oriRight-oriLeft))/50;
//                        int distY = (int) (scale*(oriBottom-oriTop)-(oriBottom-oriTop))/50;
//                        if (newDist>10f){//当双指的距离大于10时，开始相应处理
//                            left(v, -distX);
//                            top(v, -distY);
//                            right(v, distX);
//                            bottom(v, distY);
//                        }
                        break;

                }
                if (dragDirection != CENTER) {
//                    ViewGroup.MarginLayoutParams params1=(ViewGroup.MarginLayoutParams) getLayoutParams();
//                    params1.leftMargin=oriLeft;
//                    params1.rightMargin=oriRight;
//                    params1.topMargin=oriTop;
//                    params1.bottomMargin=oriBottom;
//                    v.setLayoutParams(params1);



//                    v.layout(oriLeft, oriTop, oriRight, oriBottom);
//                    ViewGroup.LayoutParams params=getLayoutParams();
//                    params.height=oriBottom-oriTop;
//                    params.width=oriRight-oriLeft;
//                    v.setLayoutParams(params);

                    v.layout(oriLeft, oriTop, oriRight, oriBottom);
                    ViewGroup.LayoutParams params=getLayoutParams();

                    //paint.setTextSize(BarcodeActivity.textSize);
                    paint.setTextSize(getContext().getResources().getDisplayMetrics().scaledDensity*(BarcodeActivity.textSize-0.5f));
                    params.height=v.getHeight();
                    int textMaxWidth=(int)(paint.measureText(BarcodeActivity.testString));

                    if(v.getWidth()>textMaxWidth)
                    {
                        params.width=textMaxWidth+12;
                    }
                    else
                    {
                        params.width = v.getWidth();
                    }
                    v.setLayoutParams(params);

                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundResource(0);
//                    }


                break;
            case MotionEvent.ACTION_POINTER_UP:
                //dragDirection = 0;
                break;
        }
    }
    /**
     * 触摸点为中心->>移动
     *
     * @param v
     * @param dx
     * @param dy
     */
    private void center(View v, int dx, int dy) {
        int left = v.getLeft() + dx;
        int top = v.getTop() + dy;
        int right = v.getRight() + dx;
        int bottom = v.getBottom() + dy;
//        if (left < -offset) {
//            left = -offset;
//            right = left + v.getWidth();
//        }
//        if (right > screenWidth + offset) {
//            right = screenWidth + offset;
//            left = right - v.getWidth();
//        }
//        if (top < -offset) {
//            top = -offset;
//            bottom = top + v.getHeight();
//        }
//        if (bottom > screenHeight + offset) {
//            bottom = screenHeight + offset;
//            top = bottom - v.getHeight();
//        }
        //Log.d("raydrag", left+"  "+top+"  "+right+"  "+bottom+"  "+dx);
        v.layout(left, top, right, bottom);

//                            ViewGroup.LayoutParams params=getLayoutParams();
//                    params.height=v.getHeight();
//                    params.width=v.getWidth();
//                    v.setLayoutParams(params);
        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();
        params.leftMargin=left;

        params.topMargin=top;

        v.setLayoutParams(params);

    }

    /**
     * 触摸点为上边缘
     *
     * @param v
     * @param dy
     */
    private void top(View v, int dy) {
        oriTop += dy;
//        if (oriTop < -offset) {
//            //对view边界的处理，如果子view达到父控件的边界，offset代表允许超出父控件多少
//            oriTop = -offset;
//        }
//        if (oriBottom - oriTop - 1 * offset < 1) {
//            oriTop = oriBottom - 1 * offset - 1;
//        }
    }

    /**
     * 触摸点为下边缘
     *
     * @param v
     * @param dy
     */
    private void bottom(View v, int dy) {
        oriBottom += dy;
//        if (oriBottom > screenHeight + offset) {
//            oriBottom = screenHeight + offset;
//        }
//        if (oriBottom - oriTop - 1 * offset < 1) {
//            oriBottom = 1 + oriTop + 1 * offset;
//        }
    }

    /**
     * 触摸点为右边缘
     *
     * @param v
     * @param dx
     */
    private void right(View v, int dx) {
        oriRight += dx;
//        if (oriRight > screenWidth + offset) {
//            oriRight = screenWidth + offset;
//        }
//        if (oriRight - oriLeft - 1 * offset < 1) {
//            oriRight = oriLeft + 1 * offset + 1;
//        }
    }

    /**
     * 触摸点为左边缘
     *
     * @param v
     * @param dx
     */
    private void left(View v, int dx) {
        oriLeft += dx;
//        if (oriLeft < -offset) {
//            oriLeft = -offset;
//        }
//        if (oriRight - oriLeft - 1 * offset < 1) {
//            oriLeft = oriRight - 1 * offset - 1;
//        }
    }

    /**
     * 获取触摸点flag
     *
     * @param v
     * @param x
     * @param y
     * @return
     */
    protected int getDirection(View v, int x, int y) {
        int left = v.getLeft();
        int right = v.getRight();
        int bottom = v.getBottom();
        int top = v.getTop();
        /*if (x < touchDistance && y < touchDistance) {
            return LEFT_TOP;
        }
        if (y < touchDistance && right - left - x < touchDistance) {
            return RIGHT_TOP;
        }
        if (x < touchDistance && bottom - top - y < touchDistance) {
            return LEFT_BOTTOM;
        }*/
        if (right - left - x < touchDistance && bottom - top - y < touchDistance) {
            return RIGHT_BOTTOM;
        }
//        if (right - left - x < touchDistance && bottom - top - y < touchDistance) {
//            return RIGHT_BOTTOM;
//        }
//        if (x < touchDistance) {
//            return LEFT;
//        }
//        if (y < touchDistance) {
//            return TOP;
//        }
        if (right - left - x < touchDistance) {
            return RIGHT;
        }
        if (bottom - top - y < touchDistance) {
            return BOTTOM;
        }
        return CENTER;
    }
    private android.os.Handler handlerM = new android.os.Handler(Looper.getMainLooper()) {


        float handlerSizeBar=1;
        @SuppressLint("HandlerM")
        @Override
        public void handleMessage(@NonNull Message msg) {
            //正常操作
            if (msg.what == 10) {
                handlerSizeBar=msg.getData().getFloat("msg");

                textSizeseekBar=findViewById(R.id.textSizeseekBar);

                textSizeseekBar.setProgress((int)handlerSizeBar);

                //saveEdit.setText(msg.getData().getString("msg"));
//                PDFImage.setImageBitmap(mBitmap);

//                imageView.setImageBitmap(mBitmap);
//                displayViewFlipperPDF.addView(imageView);

            }

        }
    };
    private float px2sp(Context context,float pxValue)
    {
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue/fontScale+0.5f);
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);//两点间距离公式
    }
    @Override
    protected  void onMeasure(int widthMeasureSpec,int heightMeasureSpect){
        super.onMeasure(widthMeasureSpec,heightMeasureSpect);
        ViewGroup mViewGroup=(ViewGroup) getParent();
        if(null!=mViewGroup)
        {
            screenHeight = mViewGroup.getHeight();
            screenWidth=mViewGroup.getWidth();
        }

    }
}
