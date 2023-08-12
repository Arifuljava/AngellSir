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
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;


public class SaveDialog extends Dialog implements View.OnClickListener{
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;
    private TextView saveTextCancel;
    private TextView saveTextConfirm;
    private EditText saveTextName;
    private TextView saveTextID;
    private LinearLayout scannerContentLayout;
    private String cancel,confirm,content;
    private Bitmap bitmap;
    public SaveDialog(@NonNull Context context) {
        super(context);
    }
    public SaveDialog(@NonNull Context context, int themeId) {
        super(context,themeId);
    }
    public SaveDialog(@NonNull Context context, String content) {
        super(context);
        this.content=content;
    }
    public SaveDialog setCancel(String cancel, IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener =listener;
        return this;
    }

    public SaveDialog setConfirm(String confirm, IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_dialog_layout);
        saveTextCancel=findViewById(R.id.saveTextCancel);
        saveTextConfirm=findViewById(R.id.saveTextConfirm);
        scannerContentLayout=findViewById(R.id.SaveDialogContentLayout);
        saveTextName=findViewById(R.id.saveTextName);
        saveTextID=findViewById(R.id.saveTextID);

        saveTextCancel.setOnClickListener(this);
        saveTextConfirm.setOnClickListener(this);
        saveTextName.setText(content);
        long timecurrentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");// HH:mm:ss

        String barcodeTime = simpleDateFormat.format(timecurrentTimeMillis);
        saveTextID.setText(barcodeTime);

    }




    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.saveTextCancel:
                if(cancelListener!=null)
                {
                    cancelListener.onCancel(this);


                }
                dismiss();
                break;
            case R.id.saveTextConfirm:
                if(confirmListener!=null)
                {
                    confirmListener.onConfirm(this);
                }
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener{
        void onCancel(SaveDialog dialog);

    }
    public interface IOnConfirmListener{
        void onConfirm(SaveDialog dialog);
    }


}
