package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawView extends View {
     //    声明Paint对象
     private Paint mPaint=null;
     private Path mPath;
     private int StrokeWidth = 10;
     private Rect rect1 = new Rect(0,0,0,0);//手动绘制矩形
    private  Rect rect;
    private  Canvas canvasX;
    private  Bitmap bitmap;
    private  float mx,my;
    private  int selectGraphis=0;
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public void SelectDraw(int graphis)
    {
        selectGraphis=graphis;
        DrawInit();
    }





              public DrawView(Context context,final View view){
                 super(context);
                  view.post(new Runnable() {
                      @Override
                      public void run() {
                          int height = view.getHeight();
                          int width = view.getWidth();
                          //构建对象
                          mPaint = new Paint();
                          mPaint.setAntiAlias(true);
                          mPaint.setStyle(Paint.Style.STROKE);
                          mPaint.setStrokeWidth(StrokeWidth);
                          mPaint.setAlpha(100);
                          DrawInit();

                          // 绘制绿色实心矩形
                          // canvas.drawRect(100, 200, 400, 200 + 400, mPaint);

                          canvasX = new Canvas();

                          bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                          //bitmap.eraseColor(Color.argb(0,0,0,0));
                          canvasX.setBitmap(bitmap);
                          //canvasX.drawColor(Color.TRANSPARENT);
                        }
                      });
                 //开启线程
                // new Thread(this).start();
             }

    public Bitmap SaveBitmap()
    {

        return bitmap;
    }
     private void DrawInit(){
        if(selectGraphis==4)
        {
            //mPaint.setAlpha(0);
           // mPaint.setXfermode(new PorterDuffXfermode((PorterDuff.Mode.CLEAR)));
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(10);
            selectGraphis=0;
        }
        else
        {

            mPaint.setStrokeWidth(StrokeWidth);
            mPaint.setColor(Color.BLACK);
        }
     }


     @Override
     protected void onDraw(Canvas canvas) {
                Paint bmpPaint=new Paint();
                 super.onDraw(canvas);
                 //设置无锯齿

//                 canvas.drawARGB(50,255,227,0);
//                 mPaint.setStyle(Paint.Style.STROKE);
//                 mPaint.setStrokeWidth(StrokeWidth);
//                 mPaint.setColor(Color.GREEN);
//                 mPaint.setAlpha(100);
//                 // 绘制绿色实心矩形
//                // canvas.drawRect(100, 200, 400, 200 + 400, mPaint);
//                 mPaint.setColor(Color.BLACK);
         if(mPaint!=null && rect!=null) {


             if(selectGraphis==0||selectGraphis==2)
             {
                 //canvas.drawLine(startX, startY, x, y, paint); 画直线
                 //canvas.drawLine(rect.left,rect.top,rect.right,rect.bottom,mPaint);


                 canvas.drawBitmap(bitmap, 0, 0, mPaint);
                 canvas.drawPath(mPath,mPaint);
             }else if(selectGraphis==1)
             {
                 canvas.drawBitmap(bitmap, 0, 0, bmpPaint);
                 canvas.drawRect(rect, mPaint);
             }
             else if(selectGraphis==5)
             {
                 canvas.drawBitmap(bitmap, 0, 0, bmpPaint);
                 canvas.drawLine(rect.left,rect.top,rect.right,rect.bottom,mPaint);

             }
         }
    }

     @Override
     public boolean onTouchEvent(MotionEvent event) {
                 int x = (int)event.getX();
                 int y = (int)event.getY();

                 switch (event.getAction()){
                         case MotionEvent.ACTION_DOWN:
                             rect =new Rect(0,0,0,0);
                             mPath=new Path();
                             mPath.moveTo(x,y);
                             mx=x;
                             my=y;

                                 rect.right+=StrokeWidth;
                                 rect.bottom+=StrokeWidth;
                                 //invalidate(rect);
                                 rect.left = x;
                                 rect.top = y;
                                 rect.right =rect.left;
                                 rect.bottom = rect.top;


                             case MotionEvent.ACTION_MOVE:
//                                 Rect old =
//                                         new Rect(rect.left,rect.top,rect.right+StrokeWidth,rect.bottom+StrokeWidth);
                                 if(selectGraphis==1||selectGraphis==5)
                                 {
                                     rect.right = x;
                                     rect.bottom = y;


                                 }
                                 else if(selectGraphis==0)
                                 {
                                     mPath.lineTo(x,y);
                                     mx=x;
                                     my=y;
                                     rect.right = (int)event.getX();
                                     rect.bottom = (int)event.getY();



                                 }
                                 else if(selectGraphis==2)
                                 {
                                     mPath.reset();
                                     RectF rectF=new RectF(rect.left,rect.top,x,y);
                                     rect.right = x;
                                     rect.bottom = y;
                                     mPath.addOval(rectF,Path.Direction.CCW);

                                 }

                                 invalidate();
                                 //old.union(x,y);

                                 break;

                         case MotionEvent.ACTION_UP:
                             if(selectGraphis==0)
                             {

                                 mPath.lineTo(mx,my);
                                 canvasX.drawPath(mPath,mPaint);
                                 //canvasX.drawLine(rect.left,rect.top,rect.right,rect.bottom, mPaint);画直线
                             }else if(selectGraphis==1) {
                                 canvasX.drawRect(rect, mPaint);
                             }
                             else if(selectGraphis==2)
                             {
                                 canvasX.drawPath(mPath,mPaint);
                             }
                             else if(selectGraphis==5)
                             {
                                 canvasX.drawLine(rect.left,rect.top,rect.right,rect.bottom, mPaint);
                             }
                                break;

                         default:
                                 break;
                     }
                 return  true;//处理了触摸信息，消息不再传递
             }

         }
