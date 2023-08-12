package com.grozziie.grozziie_pdf;;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;


public class QRDialog extends Dialog implements View.OnClickListener{
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;
    private TextView scannerCancel;
    private TextView scannerConfirm;
    private TextView scannerText;
    private TextView scannerOneCode;
    private TextView scnanerQRCode;
    private LinearLayout scannerContentLayout;
    private String cancel,confirm,content;
    private Bitmap bitmap;
    public QRDialog(@NonNull Context context) {
        super(context);
    }
    public QRDialog(@NonNull Context context,int themeId) {
        super(context,themeId);
    }
    public QRDialog(@NonNull Context context,String content) {
        super(context);
        this.content=content;
    }
    public QRDialog setCancel(String cancel,IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener =listener;
        return this;
    }

    public QRDialog setConfirm(String confirm,IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_dialog_layout);
        scannerCancel=findViewById(R.id.scannerCancel);
        scannerConfirm=findViewById(R.id.scannerConfirm);
        scannerContentLayout=findViewById(R.id.scannerContentLayout);
        scannerText=findViewById(R.id.scnanerText);
        scannerOneCode=findViewById(R.id.scannerOneCode);
        scnanerQRCode=findViewById(R.id.scannerQRCode);
        scannerCancel.setOnClickListener(this);
        scannerConfirm.setOnClickListener(this);
        scannerText.setBackgroundResource(R.drawable.bt_8_hui);
        scannerText.setTextColor(Color.parseColor("#377d84"));
        TextView textView =new TextView(getContext());
        textView.setText(content);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        //imageView.setImageBitmap();
        scannerContentLayout.setGravity(Gravity.CENTER);
        scannerContentLayout.addView(textView);
        scannerOneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scannerContentLayout.removeAllViews();
                scannerOneCode.setBackgroundResource(R.drawable.bt_8_hui);
                scannerOneCode.setTextColor(Color.parseColor("#377d84"));
                scannerText.setBackgroundResource(R.drawable.bt_8);
                scannerText.setTextColor(Color.BLACK);
                scnanerQRCode.setBackgroundResource(R.drawable.bt_8);
                scnanerQRCode.setTextColor(Color.BLACK);
                ImageView imageView =new ImageView(getContext());
                //imageView.setImageResource(R.drawable.xiaoai);
                if(checkcountname(content))
                {
                    BarcodeActivity.selectScannerFlag=0;
                    imageView.setImageBitmap(null);
                }
                 else
                     BarcodeActivity.selectScannerFlag=1;
                     imageView.setImageBitmap(encodeAsBitmap(content.toString(), BarcodeFormat.CODE_128, 150, 100));

                scannerContentLayout.setGravity(Gravity.CENTER);
                scannerContentLayout.addView(imageView);
            }
        });
        scnanerQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeActivity.selectScannerFlag=2;
                scannerContentLayout.removeAllViews();
                scnanerQRCode.setBackgroundResource(R.drawable.bt_8_hui);
                scnanerQRCode.setTextColor(Color.parseColor("#377d84"));
                scannerOneCode.setBackgroundResource(R.drawable.bt_8);
                scannerOneCode.setTextColor(Color.BLACK);
                scannerText.setBackgroundResource(R.drawable.bt_8);
                scannerText.setTextColor(Color.BLACK);
                ImageView imageView =new ImageView(getContext());
                makeQrCode(content.toString());
                imageView.setImageBitmap(bitmap);
                //imageView.setImageBitmap();
                scannerContentLayout.setGravity(Gravity.CENTER);
                scannerContentLayout.addView(imageView);
            }
        });
        scannerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeActivity.selectScannerFlag=0;
                scannerText.setBackgroundResource(R.drawable.bt_8_hui);
                scannerText.setTextColor(Color.parseColor("#377d84"));
                scannerOneCode.setBackgroundResource(R.drawable.bt_8);
                scannerOneCode.setTextColor(Color.BLACK);
                scnanerQRCode.setBackgroundResource(R.drawable.bt_8);
                scnanerQRCode.setTextColor(Color.BLACK);
                scannerContentLayout.removeAllViews();
                TextView textView =new TextView(getContext());
                textView.setText(content);
                textView.setTextSize(20);
                textView.setTextColor(Color.BLACK);
                //imageView.setImageBitmap();
                scannerContentLayout.setGravity(Gravity.CENTER);
                scannerContentLayout.addView(textView);
            }
        });

    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.scannerCancel:
                if(cancelListener!=null)
                {
                    cancelListener.onCancel(this);


                }
                dismiss();
                break;
            case R.id.scannerConfirm:
                if(confirmListener!=null)
                {
                    confirmListener.onConfirm(this);
                }
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener{
        void onCancel(QRDialog dialog);

    }
    public interface IOnConfirmListener{
        void onConfirm(QRDialog dialog);
    }
    public boolean checkcountname(String countname){
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private void makeQrCode(String makeText) {
        int widthPix = 200;
        int heightPix = 200;
        Bitmap bitmapX;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            bitMatrix = new QRCodeWriter().encode(makeText, BarcodeFormat.QR_CODE, 200, 200, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //                Bitmap bitmap= EncodingUtils.createQRCode("123415",500,500,
        //                        isLogo.isChecked()? BitmapFactory.decodeResource(getResources(),
        //                                R.mipmap.ic_launcher):null
        //                );

        int[] pixels = new int[widthPix * heightPix];
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for (int y = 0; y < heightPix; y++) {
            for (int x = 0; x < widthPix; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * widthPix + x] = 0xff000000;
                } else {
                    pixels[y * widthPix + x] = 0xffffffff;
                }
            }
        }

        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

        //                if (logoBm != null) {
        //                    bitmap = addLogo(bitmap, logoBm);
        //                }


        this.bitmap = bitmap;
    }
    private   Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {

        //BarcodeFormat barcodeFormat=BarcodeFormat.CODE_128;

        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


    private   Bitmap creatCodeBitmap(String contents, int width,
                                            int height, Context context) {
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setText(contents);
        tv.setHeight(height);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setWidth(width);
        tv.setDrawingCacheEnabled(true);
        tv.setTextColor(Color.BLACK);
        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.buildDrawingCache();
        Bitmap bitmapCode = tv.getDrawingCache();
        return bitmapCode;
    }


    private   Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                          PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = 20;
        Bitmap newBitmap = Bitmap.createBitmap(
                first.getWidth() + second.getWidth() + marginW,
                first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, marginW, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        //int x=Canvas.ALL_SAVE_FLAG;
        cv.save();
        cv.restore(

        );

        return newBitmap;
    }
}
