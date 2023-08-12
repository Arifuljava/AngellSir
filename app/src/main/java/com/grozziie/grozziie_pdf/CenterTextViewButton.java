package com.grozziie.grozziie_pdf;;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class CenterTextViewButton extends TextView {

    public CenterTextViewButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas = getTopCanvas(canvas);
        super.onDraw(canvas);
    }

    private Canvas getTopCanvas(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables == null) {
            return canvas;
        }
        Drawable drawable = drawables[0];// 左面的drawable
        if (drawable == null) {
            drawable = drawables[2];// 右面的drawable
        }

        float textSize = getPaint().measureText(getText().toString()); //获取文本所占尺寸
        int drawWidth = drawable.getIntrinsicWidth(); //获取图标所占尺寸
        int drawPadding = getCompoundDrawablePadding(); //获取中间空余的尺寸
        float contentWidth = textSize + drawWidth + drawPadding; //计算当前所占尺寸
        int leftPadding = (int) (getWidth() - contentWidth);
        setPadding(0, 0, leftPadding, 0); // 直接贴到左边
        float dx = (getWidth() - contentWidth) / 2; //获取中心位置
        canvas.translate(dx, 0);// 整体往右移动
        return canvas;
    }

}