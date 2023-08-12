package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;


public class Horizon extends HorizontalScrollView{

    public Horizon(Context context) {
        super(context);
    }

    public Horizon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ( BarcodeActivity.touchFlag== -1) {
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }




}