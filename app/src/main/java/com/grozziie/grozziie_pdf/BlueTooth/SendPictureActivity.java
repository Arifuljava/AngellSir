package com.grozziie.grozziie_pdf.BlueTooth;;

import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import com.grozziie.grozziie_pdf.R;
//import com.example.administrator.firstbluetooth.BluetoothConnect;
//import com.example.administrator.firstbluetooth.MainActivity;

//import de.greenrobot.event.EventBus;
//import de.greenrobot.event.ThreadMode;

public class SendPictureActivity extends AppCompatActivity {
    private Button SendPicture;
    private Button SetGray;
    private Button SetGrayValue;
    private Button SetGrayImageOpen;
    private static final String TAG1 ="Send_P" ;
    private BluetoothSocket socket1=null;
    private Bitmap bitsrc;
    private ImageView SetGrayImage;
    private Bitmap grayPicture;
    private  int init=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_send_picture);
        SendPicture=findViewById(R.id.sendPicture);
        SetGray=findViewById(R.id.setGray);
        SetGrayValue=findViewById(R.id.setGrayValue);
        SetGrayImage=findViewById(R.id.grayImage);
        SetGrayImageOpen=findViewById(R.id.grayOpenImage);
        if(init==0)
        {
            init=1;
            bitsrc= BitmapFactory.decodeResource(getResources(), R.drawable.xiaoai);
            //bitsrc=getbitmap(bitsrc);
        }
        SendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String String_S="ABCDEFG";
                    Intent intent=getIntent();//(intent.getStringExtra("sss"));
                    String sss=intent.getStringExtra("s");
                    Log.e(TAG1,sss);
                    byte[] msgBuffer = String_S.getBytes();
                    //MainActivity My =new MainActivity();
                    //BluetoothConnect sss=new BluetoothConnect();

                   // socket=sss.getSocket();
                   // onEventMainTrend(Event);
                    if(socket1==null)
                        Log.e(TAG1,"null is now");
                    else
                    {OutputStream os = socket1.getOutputStream();

                    os.write(msgBuffer);
                    }
                }
                catch (IOException e)
                {
                    Log.e(TAG1,"s");
                }
            }
        });
        SetGray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitsrc = BitmapFactory.decodeResource(getResources(), R.drawable.xiaoai);
                //setG.setImageResource(R.drawable.xiaoai);
                SetGrayImage.setImageBitmap(bitmap2Gray(bitsrc));
                Log.e(TAG1,"6369");

            }
        });
        SetGrayValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] sssy=new byte[1];
                sssy[0]=0x01;
                //bitsrc=BitmapFactory.decodeResource(getResources(),R.drawable.xiaoai);

                //convertGreyImg(bitsrc);
                bitsrc=getbitmap(bitsrc);
                SetGrayImage.setImageBitmap(ChangeImage(bitsrc));
            }
        });
        SetGrayImageOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPic();
            }
        });

    }



    public Bitmap getbitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //放大為屏幕的1/2大小
        float screenWidth  = getWindowManager().getDefaultDisplay().getWidth();		// 屏幕宽（像素，如：480px）
        float screenHeight = getWindowManager().getDefaultDisplay().getHeight();		// 屏幕高（像素，如：800p）

        if(width<577) {
            screenHeight = width * 4 / 3;
            screenWidth = width;
        }
        else {
            screenHeight = 576*height*4/width/3;
            screenWidth = 576;
        }
        Log.d("screen",screenWidth+"");
       // float scaleWidth = screenWidth/2/width;
        //float scaleHeight = screenHeight/2/height;
        float scaleWidth = screenWidth/width;
        float scaleHeight = screenHeight/height;

        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的圖片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
        return newbm;
    }
    //===========================================
    /**
     * 打开本地相册选择图片
     */
    private void selectPic(){
        //intent可以应用于广播和发起意图，其中属性有：ComponentName,action,data等
        Intent intent=new Intent();
        intent.setType("image/*");
        //action表示intent的类型，可以是查看、删除、发布或其他情况；我们选择ACTION_GET_CONTENT，系统可以根据Type类型来调用系统程序选择Type
        //类型的内容给你选择
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果第二个参数大于或等于0，那么当用户操作完成后会返回到本程序的onActivityResult方法
        startActivityForResult(intent, 1);
    }
    /**
     *把用户选择的图片显示在imageview中
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //用户操作完成，结果码返回是-1，即RESULT_OK
        if(resultCode==RESULT_OK){
            //获取选中文件的定位符
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            //使用content的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //获取图片
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                SetGrayImage.setImageBitmap(bitmap);
                //grayPicture=bitmap;
                bitsrc=bitmap;
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }else{
            //操作错误或没有选择图片
            Log.i("MainActivtiy", "operation error");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
//=================NEW=========================
    public Bitmap ChangeImage(Bitmap img)
    {
        int width = img.getWidth();			//获取位图的宽
        if(width>576)
            width=576;
        int height = img.getHeight();		//获取位图的高
        //height=300;

        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        Byte[]Gray_Send;
        ArrayList<Byte> Gray_Arraylist;
        Gray_Arraylist=new ArrayList<Byte>();
        //List<byte> list=new ArrayList<byte>();
        int Gray_i=0;
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        //int []i_G=new int[7];
        int []i_G=new int[13];
        int Send_Gray=0x00;
//        String Start_S="@<"+hexStr2Str("0D0A0000")+"#<";
////        String String_S="";
////
////        String End_S=">#@>";
        int StartInt=0;
        char  StartWords='@';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='<';
        Gray_Arraylist.add((byte)StartWords);
        StartInt=0x0D;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x00;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x00;
        Gray_Arraylist.add((byte)StartInt);
        for(int i = 0; i < height; i++)
        {
            StartWords = '#';
            Gray_Arraylist.add((byte) StartWords);
            StartWords = '<';
            Gray_Arraylist.add((byte) StartWords);
            for (int j = 0; j < width; j++)
            {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                int  Send_i=0;
//                if(grey<63)
//                {
//                    Send_i=6;
//                }
//                else if(grey>62 && grey<96)
//                {
//                    Send_i=5;
//                }
//                else if(grey>95 && grey<128)
//                {
//                    Send_i=4;
//                }
//                else if(grey>127 && grey<160)
//                {
//                    Send_i=3;
//                }
//                else if(grey>159 && grey<192)
//                {
//                    Send_i=2;
//                }
//                else
//                {
//                    Send_i=1;
//                }

                if(grey<21)
                {
                    Send_i=12;
                }
                else if(grey>20 && grey<43)
                {
                    Send_i=11;
                }
                else if(grey>42 && grey<65)
                {
                    Send_i=10;
                }
                else if(grey>64 && grey<87)
                {
                    Send_i=9;
                }
                else if(grey>86 && grey<109)
                {
                    Send_i=8;
                }
                else if(grey>108 && grey<131)
                {
                    Send_i=7;
                }
                else if(grey>130 && grey<153)
                {
                    Send_i=6;
                }
                else if(grey>152 && grey<175)
                {
                    Send_i=5;
                }
                else if(grey>174 && grey<197)
                {
                    Send_i=4;
                }
                else if(grey>196 && grey<219)
                {
                    Send_i=3;
                }
                else if(grey>218 && grey<240)
                {
                    Send_i=2;
                }
                else
                {
                    Send_i=1;
                }



                if(i_G[Send_i]!=0)
                {
                    i_G[Send_i]++;
                    if (i_G[Send_i] > 255)
                    {
                        Send_Gray = Send_i | 0x80;
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_Arraylist.add((byte) (i_G[Send_i]-1));
                        i_G[Send_i] = 0;
                    }
                }
                else {
                        for (int s = 1; s < 13; s++)//for (int s = 1; s < 7; s++)
                        {
                                if (i_G[s]==0)
                                {
                                    i_G[s] =0;
                                }
                                else if (i_G[s]==1)
                                {
                                    Send_Gray =  s;
                                    Gray_Arraylist.add((byte)Send_Gray);
                                    i_G[s] = 0;

                                    break;

                                }
                                else
                                {
                                    Send_Gray =s;
                                    Send_Gray=Send_Gray|0x80;
                                    Gray_Arraylist.add((byte)Send_Gray);
                                    Gray_Arraylist.add((byte)(i_G[s]-1));
                                    i_G[s] = 0;
                                    break;
                                }
                        }
                        i_G[Send_i]++;
                }
                if(j==(width-1))
                {
                    for(int Last_i=1;Last_i<13;Last_i++)//for(int Last_i=1;Last_i<7;Last_i++)
                    {
                        if (i_G[Last_i]==1) {
                            Send_Gray = Last_i;
                            Gray_Arraylist.add((byte) Send_Gray);
                            i_G[Last_i] = 0;

                            break;

                        } else if(i_G[Last_i]>1)
                        {
                            Send_Gray = Last_i;
                            Send_Gray = Send_Gray | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_Arraylist.add((byte) (i_G[Last_i] - 1));
                            i_G[Last_i] = 0;
                            break;
                        }
                    }
                }
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
            int aBc=0;

            if(width<576) {
                aBc=576-width;
                if((576-width)>255)
                {
                    aBc=aBc/256;
                    aBc=576-width;
                    aBc=aBc%256;
                    for (int xy = 0; xy < (576 - width) / 256; xy++) {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_Arraylist.add((byte) (255));
                    }
                    if(576-width==1) {
                        Send_Gray=0X81;
                        Gray_Arraylist.add((byte) Send_Gray);
                    }
                    else
                    {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_Arraylist.add((byte) ((576 - width) % 256 - 1));

                    }
                }
                else {
                    if(576-width==1) {
                        Send_Gray=0X81;
                        Gray_Arraylist.add((byte) Send_Gray);

                    }
                    else
                    {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_Arraylist.add((byte) ((576 - width) % 256 - 1));
                    }
                }
            }
            StartWords='#';
            Gray_Arraylist.add((byte)StartWords);
            StartWords='>';
            Gray_Arraylist.add((byte)StartWords);

        }

        StartWords='@';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='>';
        Gray_Arraylist.add((byte)StartWords);
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        try {
            if (socket1 == null)
                Log.e(TAG1, "null is now");
            else {
                byte[] sss=new byte[Gray_Arraylist.size()];
                Gray_Send=new Byte[Gray_Arraylist.size()];
                Gray_Arraylist.toArray(Gray_Send);
                for(int xx=0;xx<Gray_Send.length;xx++){
                    sss[xx]=Gray_Send[xx];
                }
                OutputStream os = socket1.getOutputStream();
                byte[] sssy=new byte[1];
                sssy[0]=0x01;
                //sssy[1]=0x02;
                os.write(sss);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG1,"s");
        }
        return result;
    }

// ============================================



    //===========================================




    public Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();			//获取位图的宽
        if(width>576)
            width=576;
        int height = img.getHeight();		//获取位图的高
        //height=100;

        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        Byte[]Gray_Send;
        ArrayList<Byte> Gray_Arraylist;
        Gray_Arraylist=new ArrayList<Byte>();
        //List<byte> list=new ArrayList<byte>();
        int Gray_i=0;
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        int []i_G=new int[7];
        int Send_Gray=0x00;
//        String Start_S="@<"+hexStr2Str("0D0A0000")+"#<";
////        String String_S="";
////
////        String End_S=">#@>";
        int StartInt=0;
        char  StartWords='@';
        //StartInt=Integer.getInteger(StartWords);
        Gray_Arraylist.add((byte)StartWords);
        Gray_i++;

        StartWords='<';
        //StartInt=Integer.getInteger(StartWords);
        Gray_Arraylist.add((byte)StartWords);
        Gray_i++;

        StartInt=0x0D;
        Gray_Arraylist.add((byte)StartInt);
        Gray_i++;

        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);
        Gray_i++;


        StartInt=0x00;
        Gray_Arraylist.add((byte)StartInt);
        Gray_i++;

        StartInt=0x00;
        Gray_Arraylist.add((byte)StartInt);
        Gray_i++;

        for(int i = 0; i < height; i++)	{
            StartWords='#';
            //StartInt=Integer.getInteger(StartWords);
            Gray_Arraylist.add((byte)StartWords);
            Gray_i++;


            StartWords='<';
            //StartInt=Integer.getInteger(StartWords);
            Gray_Arraylist.add((byte)StartWords);
            Gray_i++;
            for(int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int red = ((grey  & 0x00FF0000 ) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);




                if(grey<63)
                {
                    if(i_G[6]!=0)
                    {
                        i_G[6]++;
                        if (i_G[6] > 255)
                        {
                            Send_Gray = 6 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[6]-1));
                            Gray_i++;
                            i_G[6] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0) {

                            }
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                               //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[6]++;


                    }
                }
                else if(grey>62 && grey<96)
                {
                    if(i_G[5]!=0)
                    {
                        i_G[5]++;
                        if (i_G[5] > 255)
                        {
                            Send_Gray = 5 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[5]-1));
                            Gray_i++;
                            i_G[5] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0) {
                                ;
                            }
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[5]++;
                    }
                }
                else if(grey>95 && grey<128)
                {
                    if(i_G[4]!=0)
                    {
                        i_G[4]++;
                        if (i_G[4] > 255)
                        {
                            Send_Gray = 4 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[4]-1));
                            Gray_i++;
                            i_G[4] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0)
                                ;
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[4]++;
                    }
                }
                else if(grey>127 && grey<160)
                {
                    if(i_G[3]!=0)
                    {
                        i_G[3]++;
                        if (i_G[3] > 255)
                        {
                            Send_Gray = 3 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[3]-1));
                            Gray_i++;
                            i_G[3] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0)
                                ;
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[3]++;
                    }
                }
                else if(grey>159 && grey<192)
                {
                    if(i_G[2]!=0)
                    {
                        i_G[2]++;
                        if (i_G[2] > 255)
                        {
                            Send_Gray = 2 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[2]-1));
                            Gray_i++;
                            i_G[2] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0)
                                ;
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[2]++;
                    }
                }
                else if(grey>191 && grey<256)//224
                {
                    if(i_G[1]!=0)
                    {
                        i_G[1]++;
                        if (i_G[1] > 255)
                        {
                            Send_Gray = 1 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[1]-1));
                            Gray_i++;
                            i_G[1] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0)
                                ;
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[1]++;
                    }
                }
                else
                {
                    if(i_G[0]!=0)
                    {
                        i_G[0]++;
                        if (i_G[0] > 255)
                        {
                            Send_Gray = 0 | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[0]-1));
                            Gray_i++;
                            i_G[0] = 0;
                        }
                    }
                    else {
                        for (int s = 0; s < 7; s++) {
                            if (i_G[s] == 0)
                                ;
                            else if (i_G[s] == 1) {
                                Send_Gray =  s;
                                //Gray_Send[Gray_i++]=(byte)Send_Gray;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                i_G[s] = 0;

                                break;

                            }
                            else
                            {
                                Send_Gray =s;
                                Send_Gray=Send_Gray|0x80;
                                Gray_Arraylist.add((byte)Send_Gray);
                                Gray_i++;
                                Gray_Arraylist.add((byte)(i_G[s]-1));
                                Gray_i++;
                                i_G[s] = 0;
                                break;
                            }

                        }
                        i_G[0]++;
                    }
                }
                if(j==width-1)
                {
                    for(int Last_i=1;Last_i<7;Last_i++)
                    {
                        if (i_G[Last_i] == 1) {
                            Send_Gray = Last_i;
                            //Gray_Send[Gray_i++]=(byte)Send_Gray;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            i_G[Last_i] = 0;

                            break;

                        } else {
                            Send_Gray = Last_i;
                            Send_Gray = Send_Gray | 0x80;
                            Gray_Arraylist.add((byte) Send_Gray);
                            Gray_i++;
                            Gray_Arraylist.add((byte) (i_G[Last_i] - 1));
                            Gray_i++;
                            i_G[Last_i] = 0;
                            break;
                        }
                    }
                }

                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
           }
           int aBc=0;

            if(width<576) {
                aBc=576-width;
                if((576-width)>255)
                {
                    aBc=aBc/256;
                    aBc=576-width;
                    aBc=aBc%256;
                    for (int xy = 0; xy < (576 - width) / 256; xy++) {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_i++;
                        Gray_Arraylist.add((byte) (255));
                        Gray_i++;
                    }
                    if(576-width==1) {
                        Send_Gray=0X81;
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_i++;
                    }
                    else
                    {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_i++;
                        Gray_Arraylist.add((byte) ((576 - width) % 256 - 1));
                        Gray_i++;
                    }
                }
                else {
                    if(576-width==1) {
                        Send_Gray=0X81;
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_i++;
                    }
                    else
                    {
                        Send_Gray = 0 | 0x81;//80
                        Gray_Arraylist.add((byte) Send_Gray);
                        Gray_i++;
                        Gray_Arraylist.add((byte) ((576 - width) % 256 - 1));
                        Gray_i++;
                    }
                }
            }
            StartWords='#';
            //StartInt=Integer.getInteger(StartWords);
            Gray_Arraylist.add((byte)StartWords);
            Gray_i++;
            StartWords='>';
            //StartInt=Integer.getInteger(StartWords);
            Gray_Arraylist.add((byte)StartWords);
            Gray_i++;

        }

        StartWords='@';
        //StartInt=Integer.getInteger(StartWords);
        Gray_Arraylist.add((byte)StartWords);
        Gray_i++;


        StartWords='>';
        //StartInt=Integer.getInteger(StartWords);
        Gray_Arraylist.add((byte)StartWords);
        Gray_i++;

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        try {
            if (socket1 == null)
                Log.e(TAG1, "null is now");
            else {
                byte[] sss=new byte[Gray_Arraylist.size()];
                Gray_Send=new Byte[Gray_Arraylist.size()];
                Gray_Arraylist.toArray(Gray_Send);
                for(int xx=0;xx<Gray_Send.length;xx++){
                    sss[xx]=Gray_Send[xx];
                }
                OutputStream os = socket1.getOutputStream();
                byte[] sssy=new byte[1];
                sssy[0]=0x01;
                //sssy[1]=0x02;
                os.write(sss);
            }
        }
        catch (IOException e)
        {
            Log.e(TAG1,"s");
        }
        return result;
    }

    /**
     * 图片转灰度
     *
     * @param bmSrc
     * @return
     */
    public Bitmap bitmap2Gray(Bitmap bmSrc)
    {
        int width, height;
        height = bmSrc.getHeight();
        width = bmSrc.getWidth();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);

        return bmpGray;
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public  void OpenPost(FirstEvent event){
         socket1=event.getSocket_M();
        Log.e(TAG1,"99999");

    }
    protected void onDestroy() {
        //setSocket(socket);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
