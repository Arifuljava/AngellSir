package com.grozziie.grozziie_pdf;;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TextVewT extends TextView {
    private static final int DEFAULT_DEGREES = 0;
    private int mDegrees;




    public TextVewT(Context context) {
        super(context);
    }
    public TextVewT(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public TextVewT(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
//    }



    @Override
    protected void onDraw(Canvas canvas) {
if(mDegrees!=0){


            canvas.save();
            canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
            canvas.rotate(mDegrees, this.getWidth() / 2f, this.getHeight() / 2f);
            //canvas.rotate(mDegrees);
            super.onDraw(canvas);
            canvas.restore();
    }
    else
{
    super.onDraw(canvas);
}

    }

    public void setDegrees(int degrees) {
        mDegrees = degrees;
    }
}
