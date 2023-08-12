package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.core.content.ContextCompat;

public class DragViewX extends ImageView implements ImageView.OnTouchListener{
    protected int screenWidth;
    protected int screenHeight;
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private int dragDirection;
    private int nowGetLeft;
    private int nowGetTop;
    private int nowGetBottom;
    private int nowGetRight;
    private int nowGetWidth;
    private int nowGetHeight;
    private boolean nowFLag=false;
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
    //private int offset = 50; //可超出其父控件的偏移量
    protected Paint paint = new Paint();
    private static final int touchDistance = 50; //触摸边界的有效距离



    private int offset = 2;
    protected Paint borderPaint = new Paint();  //画边框用
    protected Paint fillPaint = new Paint();    //填充用
    //画四角图标
    private Rect leftTopRect;
    private Rect rightTopRect;
    private Drawable icLeftTop;
    private Drawable icRightTop;
    private Drawable icLeftDown;
    private Drawable icRightDown;
    private Rect leftBottomRect;
    private Rect rightBottomRect;
    private View view;


    // private  ScrollView scrollView;


    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;

    /**
     * 初始化获取屏幕宽高
     */
    protected void initScreenW_H() {
        //screenHeight = getResources().getDisplayMetrics().heightPixels - 40;
        //screenWidth = getResources().getDisplayMetrics().widthPixels;
//        screenHeight = getResources().getDisplayMetrics().heightPixels - 50;
//        screenWidth = getResources().getDisplayMetrics().widthPixels;

        //获取图标drawable
        icLeftTop = ContextCompat.getDrawable(getContext(), R.drawable.angenl_left_up);
        icRightTop = ContextCompat.getDrawable(getContext(), R.drawable.angenl_right_up);

        icLeftDown= ContextCompat.getDrawable(getContext(), R.drawable.angenl_left_down);
        icRightDown= ContextCompat.getDrawable(getContext(), R.drawable.angenl_right_down);
    }

    public DragViewX(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnTouchListener(this);
        initScreenW_H();
    }

    public DragViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        initScreenW_H();
    }

    public DragViewX(Context context) {
        super(context);
        setOnTouchListener(this);
        initScreenW_H();
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
//@Override
//protected void onDraw(Canvas canvas) {
//    super.onDraw(canvas);
//    borderPaint.setColor(Color.RED);
//    borderPaint.setStrokeWidth(4.0f);
//    borderPaint.setStyle(Paint.Style.STROKE);
//    //画矩形边框
//    canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
//            - offset, borderPaint);
//
////    //矩形填充
////    fillPaint.setColor(getResources().getColor(R.color.bacgroundColor));
////    fillPaint.setStrokeWidth(4.0f);
////    fillPaint.setStyle(Paint.Style.FILL);
////    canvas.drawRect(offset, offset, getWidth() - offset, getHeight()
////            - offset, fillPaint);
//
//    //绘制图标
//    //左上角
//    icLeftTop.setBounds(leftTopRect);
//    icLeftTop.draw(canvas);
//    //右上角
//    icRightTop.setBounds(rightTopRect);
//    icRightTop.draw(canvas);
//    //左下角
//    icLeftDown.setBounds(leftBottomRect);
//    icLeftDown.draw(canvas);
//    //右下角
//    icRightDown.setBounds(rightBottomRect);
//    icRightDown.draw(canvas);
//
//    //主要防止第一次进入没有触碰view就获取坐标位置报null问题
//    if (view == null) {
//        view = this;
//    }
//}

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        //左上角图标
//        leftTopRect = new Rect(0, 0, 25 * offset, 25 * offset);
//        //右上角图标
//        rightTopRect = new Rect(getWidth() - offset * 25, 0, getWidth(), 25 * offset);
//        //左下角
//        leftBottomRect = new Rect(0, getHeight() - offset * 25, offset * 25, getHeight());
//        //右下角
//        rightBottomRect = new Rect(getWidth() -offset * 25, getHeight() -offset * 25, getWidth(), getHeight());
//    }


//    public DragViewX(Context context) {
//        super(context);
//        setOnTouchListener(this);
//        initScreenW_H();
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
//        ScrollView scrollView=findViewById(R.id.scrollViewBarcode);
//        scrollView.setOnTouchListener (new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent motionEvent) {
//
//                    return true; // 只要这里return true，ScrollView就不会再滑动了。
//            }
//        });
        view = v;
        BarcodeActivity.getById=v.getId();
        //BarcodeActivity.DeleteFlag=0;
        BarcodeActivity.getIdFlag=2;
        BarcodeActivity.sonViewFlag=true;
       setBackgroundResource(R.drawable.bg_boarder_xu);
        int action = event.getAction()& MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            nowFLag=false;
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();

            v.bringToFront();
//            oriRight=screenWidth-oriLeft-v.getWidth();
//            oriBottom=screenHeight-oriTop-v.getHeight();


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
                        nowFLag=true;
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
                        nowFLag=false;
//                    ViewGroup.MarginLayoutParams params1=(ViewGroup.MarginLayoutParams) getLayoutParams();
//                    params1.leftMargin=oriLeft;
//                    params1.rightMargin=oriRight;
//                    params1.topMargin=oriTop;
//                    params1.bottomMargin=oriBottom;
//                    v.setLayoutParams(params1);



                v.layout(oriLeft, oriTop, oriRight, oriBottom);
                nowGetLeft=oriLeft;
                nowGetTop=oriTop;
                nowGetBottom=oriBottom;
                nowGetRight=oriRight;


//                    ViewGroup.LayoutParams params=getLayoutParams();
//                    params.height=oriBottom-oriTop;
//                    params.width=oriRight-oriLeft;
//                    v.setLayoutParams(params);

                   //v.layout(oriLeft, oriTop, oriRight, oriBottom);
//                    Log.d("NowView", oriLeft+"  "+oriTop+"  "+oriRight+"  "+oriBottom+"  "+dx);
//                    Log.d("NowViewSizeX", v.getHeight()+"  "+v.getWidth()+"  ");
                    nowGetHeight=v.getHeight();
                    nowGetWidth=v.getWidth();
//                    ViewGroup.LayoutParams params1=getLayoutParams();
//
//                    params1.height=v.getHeight();
//                    params1.width=v.getWidth();
//
//
//                    v.setLayoutParams(params1);

//                    ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();
//                    params.height=oriheight+dy;
//                    params.width=oriwidth+dx;
//                    params.leftMargin=oriLeft;
//
//                    params.topMargin=oriTop;
//                    params.rightMargin=oriRight;
//                    params.bottomMargin=oriBottom;
//
//
//
//                    v.setLayoutParams(params);
//                    Log.d("NowViewSize", v.getHeight()+"  "+v.getWidth()+"  ");

                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
//                    if(nowFLag)
//                    {
                        //v.layout(nowGetLeft, nowGetTop, nowGetRight, nowGetBottom);
                        int left=nowGetLeft;
                        int top=nowGetTop;
                        int right=0;
                        int bottom=0;
                        right=screenWidth-left-nowGetWidth;
                        bottom=screenHeight-top-nowGetHeight;

                        Log.d("NowView", left+"  "+top+"  "+right+"  "+bottom);
                        Log.d("NowViewSizeX", nowGetHeight+"  "+nowGetWidth+"  ");

                                    ViewGroup.LayoutParams params1=getLayoutParams();


                    params1.height=nowGetHeight;
                    params1.width=nowGetWidth;


                    v.setLayoutParams(params1);
                ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();

                params.leftMargin=left;


                params.topMargin=top;
                params.bottomMargin=bottom;
                params.rightMargin=right;


                v.setLayoutParams(params);
                BarcodeActivity.sonViewFlag=false;

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
//        int left = v.getLeft() + dx;
//        int top = v.getTop() + dy;
//        int right = v.getRight();
//        int bottom = v.getBottom();
//        right=screenWidth-left-v.getWidth();
//        bottom=screenHeight-top-v.getHeight();
//    right=left+v.getWidth();
//    bottom=top+v.getHeight();

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
        Log.d("raydrag", left+"  "+top+"  "+right+"  "+bottom+"  "+dx);
        v.layout(left, top, right, bottom);
        nowGetLeft=left;
        nowGetTop=top;
        nowGetBottom=bottom;
        nowGetRight=right;
        nowGetWidth=v.getWidth();
        nowGetHeight=v.getHeight();

       //v.layout(0, 0, 0, 0);

//         left=nowGetLeft;
//         top=nowGetTop;
//         int rightX=0;
//         int bottomX=0;
//        rightX=screenWidth-left-v.getWidth();
//        bottomX=screenHeight-top-v.getHeight();
//        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();
//
//        params.leftMargin=left;
//
//        params.topMargin=top;
////        params.bottomMargin=rightX;
////        params.rightMargin=bottomX;
//
//
//        v.setLayoutParams(params);


//        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();
//
//        params.leftMargin=left;
//
//        params.topMargin=top;
//        params.bottomMargin=bottom;
//        params.rightMargin=right;
//
//
//       v.setLayoutParams(params);

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

        //oriBottom=screenHeight-oriBottom-dy;

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
//        if (right - left - x < touchDistance*(v.getWidth())/100 && bottom - top - y < touchDistance*(v.getHeight())/100) {
//            return RIGHT_BOTTOM;
//        }
//        if (right - left - x < touchDistance*(v.getWidth())/100) {
//            return RIGHT;
//        }
//        if (bottom - top - y < touchDistance*v.getHeight()/100) {
//            return BOTTOM;
//        }
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
