package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class ScrollViewTouchView extends ScrollView{
    public ScrollViewTouchView(Context context) {
        super(context);
    }
    public ScrollViewTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public ScrollViewTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int y1=getScrollY();
                int h1=getHeight();
                int s1=BarcodeActivity.scrollview_TouchEnd;
                int z1=y1+h1;
                if(z1>=s1) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    Log.d("kkk",z1+"");


                }else
                {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    //return false;


                }

                break;

            case MotionEvent.ACTION_MOVE:
                //getParent().getParent().requestDisallowInterceptTouchEvent(true);
                ///getParent().requestDisallowInterceptTouchEvent(true);
//                if (getScrollY() > 0 && getScrollY() < maxY)
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                else
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                if (getScrollY()==0)
//                    //Log.i("kkk","滑到头了");
//                if (getChildAt(0).getMeasuredHeight() == getScrollY() + getHeight())
                int y=getScrollY();
                int h=getHeight();
                int s=BarcodeActivity.scrollview_TouchEnd;
                int z=y+h;
                Log.d("kkk",getChildAt(0).getMeasuredHeight()+" y "+getScrollY()+" h "+getHeight()+" s "+s);
                if(z>=s) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    Log.d("kkk",z+"");
                    this.smoothScrollTo(0, s-getHeight());

                }else
                {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    //return false;


                }
//                if(y==0)
//                {
//                    this.smoothScrollTo(0, s);
//                }
                break;

            case MotionEvent.ACTION_UP:
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;

        }
        return super.dispatchTouchEvent(ev);


    }

}
