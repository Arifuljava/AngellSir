package com.grozziie.grozziie_pdf;;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;



//==============================
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

import com.google.zxing.common.BitMatrix;

import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.Decoder;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.grozziie.grozziie_pdf.BlueTooth.BlueToothMainActivity;
import com.skateboard.zxinglib.CaptureActivity;

//==============================

import com.jogamp.opengl.util.packrect.LevelSet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.observers.TestObserver;
import jogamp.graph.font.typecast.ot.table.CffTable;

public class ViewFlipperOnTouchActivity extends AppCompatActivity {
    private ViewFlipper displayViewFlipperPDF;
    private int images[] = new int[4];
    private Animation animation[] = new Animation[2];
    private GestureDetector detector;
    private Bitmap mBitmap;
    private int PDFCount = 1;
    private int PDFNumber = 1;
    private TextView PDFCountNumber;
    private String PDfPageString = "1/1";
    private TextView PDFPrintButton;
    private BluetoothSocket socket1 = null;
    private static final String TAG1 = "Send_P";
    private Bitmap bitsrc = null;
    private TextView PDFSetButton;
    private EditText PageInputEnd;
    private EditText PageInputStart;
    private PdfRenderer renderer = null;
    private float pagePercent = 1;
    private EditText PageInputWidth;
    private EditText PageInputHeight;
    private EditText PageInputCopies;
    private Boolean widthFlag = true;
    private Boolean heightFlag = true;
    private CheckBox CheckPaper;
    private int PrintSpeed = 2;
    private int PrintDensity = 9;
    private int PrintContrast = 180;
    private int PrintHalftoneNumber_int=50;
    private boolean PrintHalftone=true;
    private boolean PrintBarcodeProcess=false;
    private boolean PrintCutEmptyProcess=false;
    private String PrintHalftoneS="";
    private String[] PrintHalftone_String=new String[]{"Dither","Diffusion","Grayscale"};
    private TextView PageInputRotate;
    private TextView PageSizeH;
    private TextView PageSizeW;
    private int PageSizeW_Max = 104;
    private int PageSizeH_Max = 150;
    private int PageRotate = 0;
    private String TextHouZhui = "PDF";
    private Bitmap[] bitmapX = null;
    private int BlueToothName_Flag=0;
    private Bitmap[] bitmapY;
    private boolean doc_Flag=false;
    private  int [][]lineStartP=new int[7][832];//new int[5][832]
    //int lineAdd=0;
    private int [][]lineEndP=new int[7][832];////new int[5][832]
    private int []lineAddP=new int[832];

    private final int mDuraction = 2000; // 两次返回键之间的时间差
    private long mLastTime = 0; // 最后一次按back键的时刻
    private  int [][]lineStartPBig=new int[7][1080];////new int[5][1080]
    //int lineAdd=0;
    private int [][]lineEndPBig=new int[7][1080];//////new int[5][1080]
    private int []lineAddPBig=new int[1080];

    private  BluetoothSocket socketallViewFipper=null;
    private BluetoothServerSocket meServerSocket;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    private ProgressDialog progressDialog=null;
    private int delayTimes=5200;
    private boolean againMainConnect=false;
    private boolean printEnable=true;
    private boolean printEnableFlag=false;
    private int PaperNumberFlag=0;
    private int PaperNumber=1;
    private String  nullPath="";
    private char[][]BayerDispersion={
        { 4, 32, 8, 40, 2, 34, 10, 42}, /* 8x8 Bayer ordered dithering */
        {48, 16, 56, 24, 50, 18, 58, 26}, /* pattern. Each input pixel */
        {12, 44, 4, 36, 14, 46, 6, 38}, /* is scaled to the 0..63 range */
        {60, 28, 52, 20, 62, 30, 54, 22}, /* before looking in this table */
        { 3, 35, 11, 43, 4, 33, 9, 41}, /* to determine the action. */
        {51, 19, 59, 27, 49, 17, 57, 25},
        {15, 47, 7, 39, 13, 45, 5, 37},
        {63, 31, 55, 23, 61, 29, 53, 21}
    };

    //private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper_on_touch);
        EventBus.getDefault().register(this);

          pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        //对于newWakeLock的第一个参数,有以下选择:
        //PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
        //SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
        //SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
        //FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度


        //方法1：保持cpu一直运行，不管屏幕是否黑屏
         wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
        //方法2：在Window增加flag打开屏幕常亮：
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //在Window去除flag关闭屏幕常亮：
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //方法3：在你需要常亮的Activity对应的布局文件中，给需要屏幕常亮的UI组件设置:android:keepScreenOn="true"
        //只要Activity不被停止的话,那么屏幕会一直保持常亮
        //falsh 为关闭
        wakeLock.acquire();

        displayViewFlipperPDF = findViewById(R.id.displayViewFlipperPDF);
        PDFCountNumber = findViewById(R.id.PDFCountNumber);
        PDFPrintButton = findViewById(R.id.PDFPrintButton);
        PDFSetButton = findViewById(R.id.PDFSetButton);
        PageInputEnd = findViewById(R.id.PageInputEnd);
        PageInputStart = findViewById(R.id.PageInputStart);
        PageInputWidth = findViewById(R.id.PageInputWidth);
        PageInputHeight = findViewById(R.id.PageInputHeight);
        PageInputCopies = findViewById(R.id.PageInputCopies);
        CheckPaper = findViewById(R.id.CheckPaper);
        PageInputRotate = findViewById(R.id.PageInputRotate);
        PageSizeH = findViewById(R.id.PageSizeH);
        PageSizeW = findViewById(R.id.PageSizeW);
        progressDialog=new ProgressDialog(ViewFlipperOnTouchActivity.this);
//        images[0]=R.drawable.banner1;
//        images[1]=R.drawable.banner2;
//        images[2]=R.drawable.banner3;
//        images[3]=R.drawable.banner4;
//        imageView=new ImageView(ViewFlipperOnTouchActivity.this);
//        imageView.setImageBitmap(mBitmap);
        getState();
        Intent intent = getIntent();//(intent.getStringExtra("sss"));
        int x = intent.getIntExtra("intentFlag", 0);
        if (x == 2) {
            doc_Flag=false;
//            Bundle b=intent.getExtras();
//            Bitmap bitmap =(Bitmap) b.getParcelable("bitmapX");

        } else {
            if(x==3)
            {
                doc_Flag=true;
            }
            else{
                doc_Flag=false;
            }
            final String getPath = intent.getStringExtra("getPath");
//        for(int i=0;i<images.length;i++)
//        {
//            ImageView imageView=new ImageView(this);
//            imageView.setImageResource(images[i]);
//
//            displayViewFlipperPDF.addView(imageView);
//
//        }


//        new Thread(new Runnable() {
//            @Override
//            public void run() {


            String path = "/storage/emulated/0/Download/WeiXin/Air Waybill.pdf";
            path = getPath;
            nullPath=getPath;

            File file = new File(path);
            String fileNmae = file.getName();
            boolean PrintCutEmptyProcessNow=PrintCutEmptyProcess;
            TextHouZhui = fileNmae.substring(fileNmae.lastIndexOf(".") + 1).toLowerCase();
            if (TextHouZhui.equals("png") || TextHouZhui.equals("jpg") || TextHouZhui.equals("jpeg") || TextHouZhui.equals("bmp")) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                float heightX = bitmap.getHeight();
                float widthX = bitmap.getWidth();
                pagePercent = heightX / widthX;

                Message message = new Message();
                message.what = 10;
                Bundle bundle = new Bundle();
                bundle.putFloat("msg", pagePercent);
                message.setData(bundle);
                handler.sendMessage(message);

                WindowManager wm = (WindowManager) ViewFlipperOnTouchActivity.this.getSystemService(ViewFlipperOnTouchActivity.this.WINDOW_SERVICE);
                //获取屏幕的高和宽，以决定pdf的高和宽
                float width = 800;//wm.getDefaultDisplay().getWidth();
                double height = wm.getDefaultDisplay().getHeight() /2.5;//3
                int nowHeight = (int) height;
                int nowWidth = (int) wm.getDefaultDisplay().getWidth();

                width = bitmap.getWidth() * nowHeight / bitmap.getHeight();
//                        float heightX=bitmap.getHeight();
//                        float widthX=bitmap.getWidth();
//                        pagePercent=heightX/widthX;
                if (width > nowWidth) {
                    width = nowWidth;
                    int xHeight = (int) width * bitmap.getHeight() / bitmap.getWidth();
                    if (xHeight < nowHeight)
                        nowHeight = xHeight;
                }

                //mBitmap = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
                mBitmap = zoomBitmap(bitmap, (int) width, nowHeight, PageRotate);

                //final Bitmap[] bitmaps = new Bitmap[1];//新建一个bmp数组用于存放pdf页面
                bitmapX = new Bitmap[1];
                //bitmapX=bitmaps;
                bitmapX[0] = mBitmap;//将pdf的bmp图像存放进数组中。
                bitsrc = mBitmap;
                PDFCount = 1;
                for (int i = 0; i < bitmapX.length; i++) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmapX[i]);
                    displayViewFlipperPDF.addView(imageView);


                }

            } else if (TextHouZhui.equals("pdf")) {
                ParcelFileDescriptor pdfFile = null;
                try {
                    pdfFile = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY); //以只读的方式打开文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                //PdfRenderer renderer = null;
                try {
                    renderer = new PdfRenderer(pdfFile);//用上面的pdfFile新建PdfRenderer对象
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (renderer == null) {
                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "pdf null", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_pdfnull)+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                final int pageCount = renderer.getPageCount();//获取pdf的页码数
                PDFCount = pageCount;
                final Bitmap[] bitmaps = new Bitmap[pageCount];//新建一个bmp数组用于存放pdf页面

                WindowManager wm = (WindowManager) ViewFlipperOnTouchActivity.this.getSystemService(ViewFlipperOnTouchActivity.this.WINDOW_SERVICE);
                //获取屏幕的高和宽，以决定pdf的高和宽
                float width = 800;//wm.getDefaultDisplay().getWidth();
                double height = wm.getDefaultDisplay().getHeight() / 2.5;
                int nowHeight = (int) height;
                int nowWidth = (int) wm.getDefaultDisplay().getWidth();
                for (int i = 0; i < pageCount; i++) {//这里用循环把pdf所有的页面都写入bitmap数组，真正使用的时候最好不要这样，
//因为一本pdf的书会有很多页，一次性全部打开会非常消耗内存，我打开一本两百多页的书就消耗了1.8G的内存，而且打开速度很慢。
//真正使用的时候要采用动态加载，用户看到哪页才加载附近的几页。而且最好使用多线程在后台打开。

                    PdfRenderer.Page page = renderer.openPage(i);//根据i的变化打开每一页
                    width = page.getWidth() * nowHeight / page.getHeight();
                    float heightX = page.getHeight();
                    float widthX = page.getWidth();
                    pagePercent = heightX / widthX;
                    if(i==0)
                    {
                        Message message = new Message();
                        message.what = 10;
                        Bundle bundle = new Bundle();
                        bundle.putFloat("msg", pagePercent);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }

                    if (width > nowWidth) {
                        width = nowWidth;
                        int xHeight = (int) width * page.getHeight() / page.getWidth();
                        if (xHeight < nowHeight)
                            nowHeight = xHeight;
                    }
//                    if((int) (page.getHeight() * width / page.getWidth())>1080)
//                    {
//                        nowHeight=1080;
//                    }
//                    else
//                    {
//                        nowHeight=(int) (page.getHeight() * width / page.getWidth());
//                    }
                    Log.e(TAG1, i + "Width:" + page.getWidth() + "Height:" + page.getHeight() + "pWidth" + width);
                    mBitmap = Bitmap.createBitmap((int) (width), nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
                    Canvas canvas = new Canvas(mBitmap);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(mBitmap, 0, 0, null);
                    Rect r = new Rect(0, 0, (int) (width), nowHeight);
                    page.render(mBitmap, r, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);//将pdf的内容写入bmp中

                    if(x == 3) {//DOC  DOCX  格式需要 进行裁剪=======================
                        mBitmap = DocGryBitmap(mBitmap, i, 0);

                    }
                    //==================================================
                    //=================增加裁剪空白处理==============
                    int cutEmptyImageFlag=1;
                    if(PrintCutEmptyProcessNow)//增加裁剪空白处理
                    {
                        // Bitmap mBitmapX=GetBarcodeImage(page,nowHeight,1080);//PDF图片放大处理

                        mBitmap=CutEmptyImateDisplay(mBitmap,nowWidth,nowHeight);//
                       // bitsrc = mBitmapY;
                    }
                    //===================================================
                    bitmaps[i] = mBitmap;//将pdf的bmp图像存放进数组中。
                    if (i == 0) {
                        bitsrc = mBitmap;
                    }


//                    Message message = new Message();
//                    message.what = 10;
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg", "");
//                    message.setData(bundle);
//                    handler.sendMessage(message);
                    // close the page
                    page.close();
                }
                for (int i = 0; i < bitmaps.length; i++) {
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(bitmaps[i]);
                    displayViewFlipperPDF.addView(imageView);


                }

            } else {

                //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the file format", Toast.LENGTH_SHORT).show();
                Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_fileformat)+"", Toast.LENGTH_SHORT).show();

            }


            // close the renderer
            //renderer.close();


        }
        PDfPageString = String.valueOf(1) + "/" + String.valueOf(PDFCount);
        PDFCountNumber.setText(PDfPageString);
        PageInputEnd.setText(String.valueOf(PDFCount));

//            }
//
//            //saveEdit.setText(content);
//
//        }).start();


//        animation[0]= AnimationUtils.loadAnimation(this,R.anim.viewflipper_right_in);
//        animation[1]=AnimationUtils.loadAnimation(this,R.anim.viewflipper_left_in);
//        displayViewFlipperPDF.setInAnimation(animation[0]);
//        displayViewFlipperPDF.setOutAnimation(animation[1]);
        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            //按下
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            //按下 但是还未抬起
            @Override
            public void onShowPress(MotionEvent e) {

            }

            //轻按，按一下立刻抬起
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            //滚动
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            //长按
            @Override
            public void onLongPress(MotionEvent e) {

            }

            //拖动
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() > 200) {


                    //左进右出（实现特效）外部资源
                    displayViewFlipperPDF.setInAnimation(ViewFlipperOnTouchActivity.this, R.anim.viewflipper_left_in);
                    displayViewFlipperPDF.setOutAnimation(ViewFlipperOnTouchActivity.this, R.anim.viewflipper_right_out);
                    displayViewFlipperPDF.showPrevious();
                    if (PDFCount == 1) {
                        PDfPageString = String.valueOf(1) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    } else if (PDFNumber > 1) {
                        PDFNumber--;
                        PDfPageString = String.valueOf(PDFNumber) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    } else {
                        PDFNumber = PDFCount;
                        PDfPageString = String.valueOf(PDFNumber) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    }
                    PageInputStart.setText(String.valueOf(PDFNumber));


                }
                if (e1.getX() - e2.getX() > 200) {


                    //左出右进（实现特效）外部资源
                    displayViewFlipperPDF.setInAnimation(ViewFlipperOnTouchActivity.this, R.anim.viewflipper_right_in);
                    displayViewFlipperPDF.setOutAnimation(ViewFlipperOnTouchActivity.this, R.anim.viewflipper_left_out);
                    displayViewFlipperPDF.showNext();
                    if (PDFCount == 1) {
                        PDfPageString = String.valueOf(1) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    } else if (PDFNumber < PDFCount) {
                        PDFNumber++;
                        PDfPageString = String.valueOf(PDFNumber) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    } else {
                        PDFNumber = 1;
                        PDfPageString = String.valueOf(PDFNumber) + "/" + String.valueOf(PDFCount);
                        PDFCountNumber.setText(PDfPageString);
                    }
                    PageInputStart.setText(String.valueOf(PDFNumber));
                }
                return false;
            }
        });
        PageInputWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0) {
                    if (isNumer(editable.toString())) {
                        if (Integer.parseInt(editable.toString()) > PageSizeW_Max) {
                            //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please enter a value less than " + PageSizeW_Max, Toast.LENGTH_SHORT).show();
                            Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_valueless)+"" + PageSizeW_Max, Toast.LENGTH_SHORT).show();
                            PageInputWidth.setText(String.valueOf(PageSizeW_Max));
                        } else {
                            if (CheckPaper.isChecked()) {
                                if (widthFlag) {
                                    int X = (int) (pagePercent * Integer.parseInt(editable.toString()));
                                    widthFlag = false;
                                    PageInputHeight.setText(String.valueOf(X));

                                } else {
                                    widthFlag = true;
                                }
                            } else {

                            }

                        }
                    } else {

                    }
                }
            }
        });
        PageInputCopies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String X = editable.toString();
                if (X.length() != 0) {
                    if (isNumer(X))
                    {
                        if(Integer.parseInt(X)>80)
                        {
                            PageInputCopies.setText("80");
                        }
                    }
                    else
                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_enternumber)+"", Toast.LENGTH_SHORT).show();



                }

            }
        });
        PageInputHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() != 0) {
                    if (isNumer(editable.toString())) {
                        if (Integer.parseInt(editable.toString()) > PageSizeH_Max) {
                            //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please enter a value less than " + PageSizeH_Max, Toast.LENGTH_SHORT).show();
                            Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_valueless)+"" + PageSizeH_Max, Toast.LENGTH_SHORT).show();

                            PageInputHeight.setText(String.valueOf(PageSizeH_Max));
                        } else {
                            if (CheckPaper.isChecked()) {
                                if (widthFlag) {
                                    int X = (int) (Integer.parseInt(editable.toString()) / pagePercent);
                                    widthFlag = false;
                                    PageInputWidth.setText(String.valueOf(X));

                                } else {
                                    widthFlag = true;
                                }
                            } else {

                            }
                        }
                    } else {

                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_enternumber)+"", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        PageInputRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String R_Now = PageInputRotate.getText().toString();
                Drawable drawableW = getResources().getDrawable(
                        R.drawable.pdfwidth);
                // / 这一步必须要做,否则不会显示.
                drawableW.setBounds(0, 0, drawableW.getMinimumWidth(),
                        drawableW.getMinimumHeight());
                Drawable drawableH = getResources().getDrawable(
                        R.drawable.pdfheight);
                drawableH.setBounds(0, 0, drawableH.getMinimumWidth(),
                        drawableH.getMinimumHeight());
                if (R_Now.equals("0")) {
                    PageInputRotate.setText("90");







//                    PageSizeW.setText("Paper Size (H):");
//
//                    PageSizeH.setText("Paper Size (W):");
                    PageSizeW.setText(R.string.flipper_papersizeh);

                    PageSizeH.setText(R.string.flipper_papersizew);


                    PageSizeW.setCompoundDrawables(drawableH, null, null, null);
                    PageSizeH.setCompoundDrawables(drawableW, null, null, null);
                    PageSizeW_Max = 150;
                    PageSizeH_Max = 104;


                } else if (R_Now.equals("90")) {
                    PageInputRotate.setText("180");
//                    PageSizeW.setText("Paper Size (W):");
//                    PageSizeH.setText("Paper Size (H):");


                    PageSizeW.setText(R.string.flipper_papersizew);
                    PageSizeH.setText(R.string.flipper_papersizeh);
                    PageSizeW.setCompoundDrawables(drawableW, null, null, null);
                    PageSizeH.setCompoundDrawables(drawableH, null, null, null);


                    PageSizeW_Max = 104;
                    PageSizeH_Max = 150;
                } else if (R_Now.equals("180")) {
                    PageInputRotate.setText("270");

//                    PageSizeW.setText("Paper Size (H):");
//                    PageSizeH.setText("Paper Size (W):");

                    PageSizeW.setText(R.string.flipper_papersizeh);
                    PageSizeH.setText(R.string.flipper_papersizew);
                    PageSizeW.setCompoundDrawables(drawableH, null, null, null);
                    PageSizeH.setCompoundDrawables(drawableW, null, null, null);

                    PageSizeW_Max = 150;
                    PageSizeH_Max = 104;
                } else if (R_Now.equals("270")) {
                    PageInputRotate.setText("0");

//                    PageSizeW.setText("Paper Size (W):");
//                    PageSizeH.setText("Paper Size (H):");
                    PageSizeW.setText(R.string.flipper_papersizew);
                    PageSizeH.setText(R.string.flipper_papersizeh);
                    PageSizeW.setCompoundDrawables(drawableW, null, null, null);
                    PageSizeH.setCompoundDrawables(drawableH, null, null, null);

                    PageSizeW_Max = 104;
                    PageSizeH_Max = 150;
                }
                R_Now = PageInputRotate.getText().toString();
                PageRotate = Integer.parseInt(R_Now);
            }
        });
        PDFSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(ViewFlipperOnTouchActivity.this, BlueToothMainActivity.class);
//                intent.putExtra("setWhat",2);
//                startActivity(intent);
                Intent intent = new Intent(ViewFlipperOnTouchActivity.this, PDFSetActivity.class);
                //intent.putExtra("setWhat",1);
                startActivity(intent);

            }
        });
        PDFPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameBlue=BlueToothMainActivity.BlueName;
                againMainConnect=false;
                printEnableFlag=true;


                if(nameBlue.length()>0)
                {
                    String nameNow=nameBlue.substring(0,2);
                    if(nameNow.equals("PL")||nameNow.equals("AC"))
                    //if(nameNow.equals("PL"))
                    {
                        BlueToothName_Flag=1;
                    }
                    else
                    {
                        BlueToothName_Flag=0;//0
                    }
                }
                else
                {
                    BlueToothName_Flag=0;
                }
                if(socketallViewFipper==null)
                {
                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!printEnable)
                {
                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.bluetoothmain_printing)+"", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isBtConDeviceByMac(BlueToothMainActivity.BlueMac)) {
                    PrintSend();
                } else {
//                    BlueToothMainActivity.BlueState = false;
//                    BlueToothMainActivity.BlueName = "";
//                    BlueToothMainActivity.BlueMac = "";



                         Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                        hmessage.sendEmptyMessage(1024);


                                        againMainConnect=true;

                                        socketallViewFipper.connect();

//                                    try {



//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();
//                                        return;
//                                    }



                                } catch (IOException e) {

                                    e.printStackTrace();
                                    //Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();
                                    //return;
                                }

//
                            }
                        });
                        thread.start();




                    //String xxxx=getString(R.string.flipper_connectdevice);
                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please connect the device...", Toast.LENGTH_SHORT).show();

                }



//                if (BlueToothMainActivity.socketall != null && BlueToothMainActivity.BlueState && bitsrc != null) {
//                    final String PageStart = PageInputStart.getText().toString().replaceAll(" ", "");
//                    final String PageEnd = PageInputEnd.getText().toString().replace(" ", "");
//                    final String PageCopies = PageInputCopies.getText().toString().replace(" ", "");
//                    final String PageWidth = PageInputWidth.getText().toString().replace(" ", "");
//                    final String PageHeight = PageInputHeight.getText().toString().replace(" ", "");
//                    if (PageCopies.length() != 0 && PageWidth.length() != 0 && PageHeight.length() != 0) {
//                        if (Integer.parseInt(PageCopies) < 1 || Integer.parseInt(PageWidth) < 1 || Integer.parseInt(PageWidth) < 1) {
//                            //Toast.makeText(ViewFlipperOnTouchActivity.this, "Print parameter cannot be 0", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_notzero)+"", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            if (PageStart.length() != 0 && PageEnd.length() == 0) {
//                                if (Integer.parseInt(PageStart) < 1 || Integer.parseInt(PageStart) > PDFCount) {
//                                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
//                                    return;
//                                } else {
////                            bitsrc=bitmaps[Integer.parseInt(PageStart)-1];
////                            ChangeImagePDF(bitsrc,1);
//                                    if (TextHouZhui.equals("pdf"))
//                                        GetPDFImage(PageStart, PageStart);
//                                    else {
//                                        GetBMPImage(PageStart, PageStart, bitmapX);
//                                    }
//
//                                }
//
//                            } else if (PageStart.length() != 0 && PageEnd.length() != 0) {
//                                if (Integer.parseInt(PageStart) < 1) {
//                                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                if (Integer.parseInt(PageEnd) > PDFCount) {
//                                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if (Integer.parseInt(PageStart) > Integer.parseInt(PageEnd)) {
//                                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Thread thread = new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //GetPDFImage(PageStart,PageEnd);
//                                        if (TextHouZhui.equals("pdf"))
//                                            GetPDFImage(PageStart, PageEnd);
//                                        else {
//                                            GetBMPImage(PageStart, PageEnd, bitmapX);
//                                        }
////
//                                    }
//                                });
//                                thread.start();
//
//
//                            } else if (PageStart.length() == 0) {
//                                //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                    } else {
//                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Print parameter cannot be empty", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_notempty)+"", Toast.LENGTH_SHORT).show();
//
//                        return;
//
//                    }
//
//                } else
//                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please connect the device", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();

            }
        });


    }
//    private void releaseWakeLock()
//
//    { if (wakeLock != null
//
//            && wakeLock.isHeld())
//
//    { wakeLock.release(); wakeLock =
//
//            null; }
//
//    }
    @Override
    protected void onStart() {
        super.onStart();
        getState();
        socketallViewFipper=BlueToothMainActivity.socketall;
        try{
            BlueToothMainActivity.meServerSocketall.accept(1200);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {// 截获back事件

            exitApp();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void exitApp() {

        if(System.currentTimeMillis() - mLastTime > mDuraction) {
            if(numExistMainActivity(ViewFlipperOnTouchActivity.class)>1)
            {
                finish();
            }
            else {

                Toast.makeText(this, getString(R.string.flipper_backexit)+"", Toast.LENGTH_SHORT).show();
                mLastTime = System.currentTimeMillis();
            }
        } else {

            if(numExistMainActivity(ViewFlipperOnTouchActivity.class)>1)
            {
                finish();
            }
            else
                System.exit(0);
            //finish();
        }
    }
    @Override
    protected void onDestroy() {
        //=======屏蔽掉用于解决国外打印闪退==================
        //        if (renderer != null)
        //            renderer.close();
        //=========================================

        super.onDestroy();
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OpenPost(BitmapEventBus event) {
        // bitmapY[0]=null;
        bitmapY = event.getIntentBitmap();
        bitmapX = new Bitmap[bitmapY.length];
        for (int x = 0; x < bitmapY.length; x++) {


            Bitmap bitmap = bitmapY[x];
            float heightX = bitmap.getHeight();
            float widthX = bitmap.getWidth();
            if (x == 0) {
                pagePercent = heightX / widthX;


                Message message = new Message();
                message.what = 10;
                Bundle bundle = new Bundle();
                bundle.putFloat("msg", pagePercent);
                message.setData(bundle);
                handler.sendMessage(message);

            }
            WindowManager wm = (WindowManager) ViewFlipperOnTouchActivity.this.getSystemService(ViewFlipperOnTouchActivity.this.WINDOW_SERVICE);
            //获取屏幕的高和宽，以决定pdf的高和宽
            float width = 800;//wm.getDefaultDisplay().getWidth();
            double height = wm.getDefaultDisplay().getHeight() / 2.5;
            int nowHeight = (int) height;
            int nowWidth = (int) wm.getDefaultDisplay().getWidth();

            width = bitmap.getWidth() * nowHeight / bitmap.getHeight();
            //                        float heightX=bitmap.getHeight();
            //                        float widthX=bitmap.getWidth();
            //                        pagePercent=heightX/widthX;
            if (width > nowWidth) {
                width = nowWidth;
                int xHeight = (int) width * bitmap.getHeight() / bitmap.getWidth();
                if (xHeight < nowHeight)
                    nowHeight = xHeight;
            }

            //mBitmap = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
            mBitmap = zoomBitmap(bitmap, (int) width, nowHeight, PageRotate);

            //final Bitmap[] bitmaps = new Bitmap[1];//新建一个bmp数组用于存放pdf页面

            //bitmapX=bitmaps;
            bitmapX[x] = mBitmap;//将pdf的bmp图像存放进数组中。
            if (x == 0)
                bitsrc = mBitmap;
            PDFCount = bitmapY.length;
        }
        for (int i = 0; i < bitmapX.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmapX[i]);
            displayViewFlipperPDF.addView(imageView);


        }
        PDfPageString = String.valueOf(1) + "/" + String.valueOf(PDFCount);
        PDFCountNumber.setText(PDfPageString);


        Log.e(TAG1, "99999");

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return this.detector.onTouchEvent(event);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        int i = 0;
        int handlerPageHeight=135;
        int handlerPageWidth=104;
        float getHandlerPageHeight=135;
        float getHandlerPageWidth=104;
        float handlerPercent=1;
        @SuppressLint("Handler2")
        @Override
        public void handleMessage(@NonNull Message msg) {
            //正常操作
            if (msg.what == 10) {
                handlerPercent=msg.getData().getFloat("msg");
                int handlerPageWidthN=104;
                int handlerPageHeightN=135;
                getHandlerPageHeight=handlerPageWidth*handlerPercent;
                if(getHandlerPageHeight>handlerPageHeight)
                {
                    getHandlerPageWidth=handlerPageHeight/handlerPercent;
                    //handlerPageWidth=(int)getHandlerPageWidth;
                    handlerPageWidthN=(int)getHandlerPageWidth;

                }
                else
                {
                    //handlerPageHeight=(int)getHandlerPageHeight;
                    handlerPageHeightN=(int)getHandlerPageHeight;

                }
                PageInputWidth.setText(String.valueOf(handlerPageWidthN));//handlerPageWidth
                PageInputHeight.setText(String.valueOf(handlerPageHeightN));//handlerPageHeight


                //saveEdit.setText(msg.getData().getString("msg"));
//                PDFImage.setImageBitmap(mBitmap);

//                imageView.setImageBitmap(mBitmap);
//                displayViewFlipperPDF.addView(imageView);

            }

        }
    };
private void PrintSend(){
    if (bitsrc == null) {
        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please select a file to print", Toast.LENGTH_SHORT).show();
        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_selectfile)+"", Toast.LENGTH_SHORT).show();
        return;
    }
    if (BlueToothMainActivity.socketall != null && BlueToothMainActivity.BlueState && bitsrc != null) {
        final String PageStart = PageInputStart.getText().toString().replaceAll(" ", "");
        final String PageEnd = PageInputEnd.getText().toString().replace(" ", "");
        final String PageCopies = PageInputCopies.getText().toString().replace(" ", "");
        final String PageWidth = PageInputWidth.getText().toString().replace(" ", "");
        final String PageHeight = PageInputHeight.getText().toString().replace(" ", "");
        if (PageCopies.length() != 0 && PageWidth.length() != 0 && PageHeight.length() != 0) {
            if (Integer.parseInt(PageCopies) < 1 || Integer.parseInt(PageWidth) < 1 || Integer.parseInt(PageWidth) < 1) {
                //Toast.makeText(ViewFlipperOnTouchActivity.this, "Print parameter cannot be 0", Toast.LENGTH_SHORT).show();
                Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_notzero)+"", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (PageStart.length() != 0 && PageEnd.length() == 0) {
                    if (Integer.parseInt(PageStart) < 1 || Integer.parseInt(PageStart) > PDFCount) {
                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
//                            bitsrc=bitmaps[Integer.parseInt(PageStart)-1];
//                            ChangeImagePDF(bitsrc,1);
                        if (TextHouZhui.equals("pdf"))
                            GetPDFImage(PageStart, PageStart);
                        else {
                            GetBMPImage(PageStart, PageStart, bitmapX);
                        }

                    }

                } else if (PageStart.length() != 0 && PageEnd.length() != 0) {
                    if (Integer.parseInt(PageStart) < 1) {
                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Integer.parseInt(PageEnd) > PDFCount) {
                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Integer.parseInt(PageStart) > Integer.parseInt(PageEnd)) {
                        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //GetPDFImage(PageStart,PageEnd);
                            if (TextHouZhui.equals("pdf"))
                                GetPDFImage(PageStart, PageEnd);
                            else {
                                GetBMPImage(PageStart, PageEnd, bitmapX);
                            }
//
                        }
                    });
                    thread.start();


                } else if (PageStart.length() == 0) {
                    //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please confirm the number of pages", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_numberpages)+"", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            //Toast.makeText(ViewFlipperOnTouchActivity.this, "Print parameter cannot be empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_notempty)+"", Toast.LENGTH_SHORT).show();

            return;

        }

    } else
        //Toast.makeText(ViewFlipperOnTouchActivity.this, "Please connect the device", Toast.LENGTH_SHORT).show();
        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();
}
    // ============================================
    private boolean isNumer(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private void GetBMPImage(String PageStart, String PageEnd, Bitmap[] bitmapNew) {
        int PageX = Integer.parseInt(PageEnd) - Integer.parseInt(PageStart) + 1;

        //final Bitmap[] bitmapsNow = new Bitmap[PageX];//新建一个bmp数组用于存放pdf页面
        int nowHeight = Integer.parseInt(PageInputHeight.getText().toString()) * 8;
        int nowWidth = Integer.parseInt(PageInputWidth.getText().toString()) * 8;
        int PrintCopies = 1;
        int j = 0;
        if (PageInputCopies.getText().toString().length() != 0) {
            PrintCopies = Integer.parseInt(PageInputCopies.getText().toString());
            if (PrintCopies < 1) {
                PrintCopies = 1;
            }
        } else {
            PrintCopies = 1;
        }
        if (nowHeight > 1080) {
            nowHeight = 1080;
        }
        for (int i = Integer.parseInt(PageStart) - 1; i < Integer.parseInt(PageEnd); i++) {


//            //Log.e(TAG1, i+"Width:"+page.getWidth()+"Height:"+page.getHeight()+"pWidth"+nowWidth);
//            mBitmap = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
//            Canvas canvas = new Canvas(mBitmap);
//            canvas.drawColor(Color.WHITE);
//            canvas.drawBitmap(bitmapNew[i], 0, 0, null);
//            Rect r = new Rect(0, 0, nowWidth, nowHeight);
//            //page.render(mBitmap, r, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);//将pdf的内容写入bmp中
//            Matrix matrix = new Matrix();
//            matrix.postRotate(PageRotate); /*翻转180度*/
//            int width = mBitmap.getWidth();
//            int height = mBitmap.getHeight();
//            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
//            canvas.drawBitmap(bitmapNew[i], 0, 0, null);
            mBitmap = zoomBitmap(bitmapNew[i], nowWidth, nowHeight, PageRotate);
            bitsrc = mBitmap;
            //bitmapsNow[j++] = mBitmap;//将pdf的bmp图像存放进数组中。

            if(BlueToothName_Flag==1) {
                ChangeImagePDF(bitsrc, PrintCopies, i, 0);
            }
            else{
                ChangeImagePDF_ESC(bitsrc, PrintCopies, i, 0);
            }

        }
    }

    private void GetPDFImage(String PageStart, String PageEnd) {
        int PageX = Integer.parseInt(PageEnd) - Integer.parseInt(PageStart) + 1;
        PaperNumber=PageX;
        PaperNumberFlag=0;
        boolean PrintBarcodeProcessNow=PrintBarcodeProcess;
        boolean PrintCutEmptyProcessNow=PrintCutEmptyProcess;
        final Bitmap[] bitmapsNow = new Bitmap[PageX];//新建一个bmp数组用于存放pdf页面
        int nowHeight = Integer.parseInt(PageInputHeight.getText().toString()) * 8;
        int nowWidth = Integer.parseInt(PageInputWidth.getText().toString()) * 8;
        int PrintCopies = 1;
        int j = 0;
        if (PageInputCopies.getText().toString().length() != 0) {
            PrintCopies = Integer.parseInt(PageInputCopies.getText().toString());
            if (PrintCopies < 1) {
                PrintCopies = 1;
            }
        } else {
            PrintCopies = 1;
        }
        if (nowHeight > 1080) {
            nowHeight = 1080;
        }

        //=======用于国外打印闪退==================
//            File file = new File(nullPath);
//            ParcelFileDescriptor pdfFile = null;
//            try {
//                pdfFile = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY); //以只读的方式打开文件
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//
//
//            //PdfRenderer renderer = null;
//            try {
//                renderer = new PdfRenderer(pdfFile);//用上面的pdfFile新建PdfRenderer对象
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (renderer == null) {
//                //Toast.makeText(ViewFlipperOnTouchActivity.this, "pdf null", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_pdfnull)+"", Toast.LENGTH_SHORT).show();
//                return;
//            }
        //=========================================

        for(int l=0;l<PrintCopies;l++)
        {
            for (int i = Integer.parseInt(PageStart) - 1; i < Integer.parseInt(PageEnd); i++) {//这里用循环把pdf所有的页面都写入bitmap数组，真正使用的时候最好不要这样，
//因为一本pdf的书会有很多页，一次性全部打开会非常消耗内存，我打开一本两百多页的书就消耗了1.8G的内存，而且打开速度很慢。
//真正使用的时候要采用动态加载，用户看到哪页才加载附近的几页。而且最好使用多线程在后台打开。

                PdfRenderer.Page page = renderer.openPage(i);//根据i的变化打开每一页
                //width=page.getWidth()*nowHeight/page.getHeight();
                Log.e(TAG1, i + "Width:" + page.getWidth() + "Height:" + page.getHeight() + "pWidth" + nowWidth);
                mBitmap = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
                Canvas canvas = new Canvas(mBitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mBitmap, 0, 0, null);
                Rect r = new Rect(0, 0, nowWidth, nowHeight);
                page.render(mBitmap, r, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);//将pdf的内容写入bmp中
                Matrix matrix = new Matrix();
                matrix.postRotate(PageRotate); /*翻转180度*/
                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();
                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
                canvas.drawBitmap(mBitmap, 0, 0, null);

//            //调试==============================
//            Bitmap bmpRet = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);
//            canvas= new Canvas(bmpRet);
//            Paint paint = new Paint();
//            canvas.drawBitmap(mBitmap, matrix, paint);
////============================================
                int barcodeNeedFlag=1;
                int cutEmptyImageFlag=1;
                Bitmap BitmapKC;
                Bitmap BitmapPXC;
                //CutEmptyImateY
                if(PrintCutEmptyProcessNow)//增加裁剪空白处理X方向
                {
                    // Bitmap mBitmapX=GetBarcodeImage(page,nowHeight,1080);//PDF图片放大处理

                    Bitmap mBitmapY=CutEmptyImateY(mBitmap,nowWidth,nowHeight);//
                    BitmapPXC = mBitmapY;
                }
                else
                {
                    BitmapPXC = mBitmap;
                }


                if(PrintBarcodeProcessNow)//增加扣条码处理
                {
                    Bitmap mBitmapX;

                    if(PrintCutEmptyProcess)
                    {
                        Bitmap mBitmapXC=GetBarcodeImage(page,nowHeight,1080);//PDF图片放大处理
                        Bitmap mBitmapZ=CutEmptyImateY(mBitmapXC,1080,nowHeight);//
                        mBitmapX= zoomBitmap(mBitmapZ, 1080, nowHeight, 0);
                    }
                    else
                    {
                         mBitmapX=GetBarcodeImage(page,nowHeight,1080);//PDF图片放大处理
                    }

                    lineStartP=new int[7][mBitmapX.getWidth()];//int[5][mBitmapX.getWidth()]
                    //int lineAdd=0;
                    lineEndP=new int[7][mBitmapX.getWidth()];//int[5][mBitmapX.getWidth()]
                    lineAddP=new int[mBitmapX.getWidth()];
                    Bitmap mBitmapY=SelectBarcodeToImage(mBitmapX,BitmapPXC);//mBitmap
                    BitmapKC = mBitmapY;
                }
                else
                {
                    BitmapKC = BitmapPXC;//mBitmap
                }
                if(PrintCutEmptyProcessNow)//增加裁剪空白处理XY方向
                {
                   // Bitmap mBitmapX=GetBarcodeImage(page,nowHeight,1080);//PDF图片放大处理

                    Bitmap mBitmapY=CutEmptyImate(BitmapKC,nowWidth,nowHeight);//
                    bitsrc = mBitmapY;
                }
                else
                {
                    bitsrc = BitmapKC;
                }


                // bitsrc = mBitmap;

                //bitmapsNow[j++] = mBitmap;//将pdf的bmp图像存放进数组中。
                if(BlueToothName_Flag==1) {
                    ChangeImagePDF(bitsrc, PrintCopies, i, 1);
                }
                else
                {
                    ChangeImagePDF_ESC(bitsrc, PrintCopies,i,1);
                }
                page.close();
            }
        }


//        for(int i=0;i<bitmapsNow.length;i++)
//        {
//            bitsrc=bitmapsNow[i];
//            ChangeImagePDF(bitsrc,PrintCopies);
//
//
//        }
    }
    private Bitmap GetBarcodeImage(PdfRenderer.Page page ,int nowHeight,int nowWidth)
    {
        //PdfRenderer.Page page = renderer.openPage(i);//根据i的变化打开每一页
        //width=page.getWidth()*nowHeight/page.getHeight();
        //Log.e(TAG1, i + "Width:" + page.getWidth() + "Height:" + page.getHeight() + "pWidth" + nowWidth);
        Bitmap mBitmapN = Bitmap.createBitmap(nowWidth, nowHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
        Canvas canvas = new Canvas(mBitmapN);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBitmapN, 0, 0, null);
        Rect r = new Rect(0, 0, nowWidth, nowHeight);
        page.render(mBitmapN, r, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);//将pdf的内容写入bmp中
        Matrix matrix = new Matrix();
        matrix.postRotate(PageRotate); /*翻转180度*/
        int width = mBitmapN.getWidth();
        int height = mBitmapN.getHeight();
        mBitmapN = Bitmap.createBitmap(mBitmapN, 0, 0, width, height, matrix, true);
        canvas.drawBitmap(mBitmapN, 0, 0, null);
        return  mBitmapN;
    }

    //=====================检查低功耗蓝牙连接状态===========================
//    public static boolean isBluetoothHeadsetConnected() {
//// BLE方法1
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        int xxx=BluetoothHeadset.STATE_CONNECTED;
//        int yyy=mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET);
//        int zzz=BluetoothHeadset.HEADSET;
//
//        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
//                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
//
////============================================================
////BLE方法2
//        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
////adapter也有getState(), 可获取ON/OFF...其它状态
//        int a2dp = blueadapter.getProfileConnectionState(BluetoothProfile.A2DP);              //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
//        int headset = blueadapter.getProfileConnectionState(BluetoothProfile.HEADSET);        //蓝牙头戴式耳机，支持语音输入输出
//        int health = blueadapter.getProfileConnectionState(BluetoothProfile.HEALTH);
//        return blueadapter != null && (a2dp == BluetoothAdapter.STATE_CONNECTED ||
//                headset == BluetoothAdapter.STATE_CONNECTED ||
//                health == BluetoothAdapter.STATE_CONNECTED);
//
////================================================================================
//
//
//    }
//=============================================================================
    private boolean isBtConDeviceByMac(String strCurBtMac) {
        BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            return false;
        }
        Set<BluetoothDevice> set = mBtAdapter.getBondedDevices();
        BluetoothDevice device = null;
        for (BluetoothDevice dev : set) {
            if (dev.getAddress().equalsIgnoreCase(strCurBtMac)) {
                device = dev;
                break;
            }
        }
        if (device == null) {
            return false;
        }
        //得到BluetoothDevice的Class对象
        Class<BluetoothDevice> bluetoothDeviceClass = BluetoothDevice.class;
        try {//得到连接状态的方法
            Method method = bluetoothDeviceClass.getDeclaredMethod("isConnected", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            return (boolean) method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void getState() {
        SharedPreferences sp = getSharedPreferences("PDFPrintSet3693", Context.MODE_PRIVATE);
        PrintSpeed = sp.getInt("PrintSpeed", 2) + 1;
        PrintDensity = sp.getInt("PrintDensity", 9) + 1;
        PrintContrast = sp.getInt("PrintContrast", 60) + 120;
        PrintHalftone=sp.getBoolean("PrintHalftone",true);
        PrintBarcodeProcess=sp.getBoolean("PrintBarcodeProcess",false);
        PrintCutEmptyProcess=sp.getBoolean("PrintCutEmptyProcess",false);
        PrintHalftoneS=sp.getString("PrintHalftoneString",PrintHalftone_String[0]);
        PrintHalftoneNumber_int=sp.getInt("PrintHalftoneNumber_int",50);



    }

    private static Bitmap zoomBitmap(Bitmap bm, int targetWidth, int targetHeight, int PageRotate) {
        int srcWidth = bm.getWidth();
        int srcHeight = bm.getHeight();
        float widthScale = targetWidth * 1.0f / srcWidth;
        float heightScale = targetHeight * 1.0f / srcHeight;
        Matrix matrix = new Matrix();
        //matrix.postRotate(PageRotate);
        matrix.postScale(widthScale, heightScale, 0, 0);
        // 如需要可自行设置 Bitmap.Config.RGB_8888 等等
        Bitmap bmpRet = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpRet);
        Paint paint = new Paint();
        canvas.drawBitmap(bm, matrix, paint);


        if (PageRotate != 0) {
            Bitmap mBitmap1 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
            Canvas canvas1 = new Canvas(mBitmap1);
            canvas1.drawColor(Color.WHITE);
            canvas1.drawBitmap(bmpRet, 0, 0, null);
            Rect r = new Rect(0, 0, targetWidth, targetHeight);


            Matrix matrix1 = new Matrix();
            matrix1.postRotate(PageRotate); /*翻转180度*/
            int width = bmpRet.getWidth();
            int height = bmpRet.getHeight();
            bmpRet = Bitmap.createBitmap(bmpRet, 0, 0, width, height, matrix1, true);
            canvas1.drawBitmap(bmpRet, 0, 0, null);

        }


        return bmpRet;
    }
    //==================图片灰度化=====================================
    public Bitmap bitmap2Gray(Bitmap bmSrc) {

    int width = bmSrc.getWidth();
    int height = bmSrc.getHeight();
    // 创建⽬标灰度图像
    Bitmap bmpGray = null;
    bmpGray =Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);//RGB_565
    // 创建画布
    Canvas c = new Canvas(bmpGray);
    Paint paint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
    c.drawBitmap(bmSrc,0,0,paint);
    return bmpGray;
}
//=======================================================
//图片二值化成为黑白图片
    public Bitmap BitmapToGrayBitmap(Bitmap img) {
        int height=img.getHeight();
        int width=img.getWidth();
        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height; i++)
        {
            for (int j = 0; j <width; j++)
            {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                if(grey>=128)
                {
                    grey=255;
                }
                else
                {
                    grey=0;
                }

                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
            //int aBc=0;

        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        //result=result;
        return result;
    }



    //================图片半色调误差扩散处理======================
    //Floyd-Steinberg算法的原理。它是利用误差的扩散算法的Floyd-Steinberg抖动算法来对图像进行二值化处理，
    //例如，灰度如的灰度值为g，误差值为e。遍历每个像素值，灰度如果大于m（127，或者像素灰度平均值，看你喜欢）
    // 那么pixels【i】=#ffffffff，打白，e=g-255；否则，打黑，pixels【i】=#ff000000，e=g；
    // 然后，这个像素点的右边，下边，和右下方的像素点，对应的加上3e/8,3e/8,e/4。最后你的到的像素数组在转成bitmap，就是抖动的单色图了。
    public Bitmap convertGreyImgByFloyd(Bitmap img) {
        int PrintContrastNow=PrintContrast;
        //img=bitmap2Gray(img);
        int width = img.getWidth();         //获取位图的宽
        int height = img.getHeight();       //获取位图的高


        int[] pixels = new int[width * height]; //通过位图的大小创建像素点数组


        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] gray=new int[height*width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey  & 0x00FF0000 ) >> 16);
                gray[width*i+j]=red;
            }
        }


        int e=0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int g=gray[width*i+j];
                if (g>=128) {//PrintContrastNow
                    pixels[width*i+j]=0xffffffff;
                    e=g-255;


                }else {
                    pixels[width*i+j]=0xff000000;
                    e=g-0;
                }
                if (j<width-1&&i<height-1) {
                    //右边像素处理
                    gray[width*i+j+1]+=3*e/8;
                    //下
                    gray[width*(i+1)+j]+=3*e/8;
                    //右下
                    gray[width*(i+1)+j+1]+=e/4;
                }else if (j==width-1&&i<height-1) {//靠右或靠下边的像素的情况
                    //下方像素处理
                    gray[width*(i+1)+j]+=3*e/8;
                }else if (j<width-1&&i==height-1) {
                    //右边像素处理
                    gray[width*(i)+j+1]+=e/4;
                }
            }


        }


        Bitmap mBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);






        return mBitmap;
    }








//=====================================================
    private void DitherNumberSet(int DitherDensity){
        int ADJ=0;
        int i,j;
        if(DitherDensity>0&&DitherDensity<=100)
        {

            if(DitherDensity>=50)
            {
                ADJ=DitherDensity-50;
                //变浓
                for(j=0;j<8;j++)
                {
                    for(i=0;i<8;i++)
                    {
                        if((BayerDispersion[j][i]+ADJ)>63)
                        {
                            BayerDispersion[j][i]=63;
                        }
                        else
                        {
                            BayerDispersion[j][i]+=ADJ;
                        }
                    }
                }
            }
            else
            {
                ADJ=50-DitherDensity;
                //变浓
                for(j=0;j<8;j++)
                {
                    for(i=0;i<8;i++)
                    {
                        if(BayerDispersion[j][i]>(ADJ+8))
                        {
                            BayerDispersion[j][i]-=ADJ;
                        }
                        else
                        {
                            BayerDispersion[j][i]=8;
                        }
                    }
                }
            }
        }
    }
    //==================================================================


    //=================================================================
    //半色调抖动8*8

    /**
     * 将bitmap 转换为RGB数组（三通道）
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2RGBData(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] intValues = new int[width * height];
        bitmap.getPixels(intValues, 0, width, 0, 0, width,
                height);
        byte[] rgb = new byte[width * height * 3];
        for (int i = 0; i < intValues.length; ++i) {
             int val = intValues[i];
//            rgb[i * 3] = (byte) ((val >> 16) & 0xFF);//R
//            rgb[i * 3+1 ] = (byte) ((val >> 8) & 0xFF);//G
//            rgb[i * 3+2] = (byte) (val & 0xFF);//B
            rgb[i * 3] = (byte)((val & 0x00FF0000) >> 16);
            rgb[i * 3+1 ] =(byte) ((val & 0x0000FF00) >> 8);
            rgb[i * 3+2] = (byte)(val & 0x000000FF);
        }
        return rgb;
    }


    public static int[] bitmap2RGBDataX(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] intValues = new int[width * height];
        bitmap.getPixels(intValues, 0, width, 0, 0, width,
                height);
        int[] rgb = new int[width * height * 3];
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            rgb[i * 3+2] = ((val >> 16) & 0xFF);//R
            rgb[i * 3 + 1] =  ((val >> 8) & 0xFF);//G
            rgb[i * 3] = (val & 0xFF);//B
//            int red = ((grey & 0x00FF0000) >> 16);
//            int green = ((grey & 0x0000FF00) >> 8);
//            int blue = (grey & 0x000000FF);
        }
        return rgb;
    }
    /**
     * 三通道数组转3通道bitmap
     */
    public  Bitmap rgb2BitmapFor323(byte[] data, int width, int height) {
        long startTime = System.currentTimeMillis();
        try {

            int[] colors = convertByteToColor323(data);//取RGB值转换为int数组
            if (colors == null) {
                return null;
            }
            return Bitmap.createBitmap(colors, 0, width, width, height,
                    Bitmap.Config.ARGB_8888);

        } catch (Exception e) {

        } finally {

        }
        return null;
    }
    public  Bitmap rgb2BitmapFor323X(int[] data, int width, int height) {
        long startTime = System.currentTimeMillis();
        try {

            int[] colors = convertByteToColor323X(data);//取RGB值转换为int数组
            if (colors == null) {
                return null;
            }
            return Bitmap.createBitmap(colors, 0, width, width, height,
                    Bitmap.Config.ARGB_8888);

        } catch (Exception e) {

        } finally {

        }
        return null;
    }


    /**
     * 将纯RGB数据数组转化成int像素数组，转三通道
     *
     * @param data rgb数组 输入为三通道
     */
    private  int[] convertByteToColor323(byte[] data) {
        int size = -1;
        if (data != null) {
            size = data.length;
        }
        if (size == 0) {
            return null;
        }
        int arg = 0;
        if (size % 3 != 0) {
            arg = 1;
        }
        // 一般RGB字节数组的长度应该是3的倍数，
        // 不排除有特殊情况，多余的RGB数据用黑色0XFF000000填充
        int[] color = new int[size / 3 + arg];
        int red, green, blue;
        int colorLen = color.length;
        int alpha = 0xFF << 24;
        int grey=0;
        if (arg == 0) {
            for (int i = 0; i < colorLen; ++i) {
                red = convertByteToInt(data[i * 3+2]);
                green = convertByteToInt(data[i * 3 + 1]);
                blue = convertByteToInt(data[i * 3 + 0]);
                // 获取RGB分量值通过按位或生成int的像素值

                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
//                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
//                color[i] = alpha | (grey << 16) | (grey << 8) | grey;
            }
        } else {
            for (int i = 0; i < colorLen - 1; ++i) {
                red = convertByteToInt(data[i * 3+2]);
                green = convertByteToInt(data[i * 3 + 1]);
                blue = convertByteToInt(data[i * 3 + 0]);
                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
            }

            color[colorLen - 1] = 0xFF000000;
        }
        return color;
    }
    private  int[] convertByteToColor323X(int[] data) {
        int size = -1;
        if (data != null) {
            size = data.length;
        }
        if (size == 0) {
            return null;
        }
        int arg = 0;
        if (size % 3 != 0) {
            arg = 1;
        }
        // 一般RGB字节数组的长度应该是3的倍数，
        // 不排除有特殊情况，多余的RGB数据用黑色0XFF000000填充
        int[] color = new int[size / 3 + arg];
        int red, green, blue;
        int colorLen = color.length;
        int alpha = 0xFF << 24;
        int grey=0;
        if (arg == 0) {
            for (int i = 0; i < colorLen; ++i) {
                red = data[i * 3+2];
                green = data[i * 3 + 1];
                blue = data[i * 3 + 0];
                // 获取RGB分量值通过按位或生成int的像素值

                //color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                color[i] = alpha | (grey << 16) | (grey << 8) | grey;
            }
        } else {
            for (int i = 0; i < colorLen - 1; ++i) {
                red = data[i * 3+2];
                green = data[i * 3 + 1];
                blue = data[i * 3 + 0];
//                color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);
                color[i] = alpha | (grey << 16) | (grey << 8) | grey;
            }
            color[colorLen - 1] = 0xFF000000;
        }
        return color;
    }
    /**
     * 将一个byte数转成int
     * 实现这个函数的目的是为了将byte数当成无符号的变量去转化成int
     *
     * @param data byte字节
     */
    private static int convertByteToInt(byte data) {
        int heightBit = (int) ((data >> 4) & 0x0F);
        int lowBit = (int) (0x0F & data);
        return heightBit * 16 + lowBit;
    }






   private Bitmap DitherToBitmap(Bitmap img)
    {

        int height=img.getHeight();
        int width=img.getWidth();
        int []BMPData = bitmap2RGBDataX(img);	//通过位图的大小创建像素点数组
        //img.getPixels(BMPData, 0, width, 0, 0, width, height);


        int LineSize,x,y,recordLine,Gray;
        int offsetD,i,j;
        int  PrintHalftoneNumber_intNow=PrintHalftoneNumber_int;
        //int str[]=new int[1000];
        //int TT;

        DitherNumberSet(PrintHalftoneNumber_intNow);



        LineSize=(width*3+3)/4*4;//一行字节数
        //灰度比较 抖动
        for(y=0;y<height;y+=8)
        {
            recordLine=0;
            for(x=0;x<width*3;x+=3*8)//像素点
            {
                //BGR
                //RGB
                for(i=x;i<24+x&&i<width*3;i+=3)
                {
                    for(j=y;j<y+8&&j<height;j++)
                    {
                        offsetD=i+j*LineSize;
                        //(int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);

                        Gray=(int)((float)BMPData[offsetD+2]*0.3+(float)BMPData[offsetD+1]*0.59+(float)BMPData[offsetD]*0.11);
                        Gray++;
                        Gray/=4;
                        if(Gray>BayerDispersion[(i-x)/3][j-y])//
                        {

                            //黑色点
                            BMPData[offsetD]=255;
                            BMPData[offsetD+1]=255;
                            BMPData[offsetD+2]=255;
                        }
                        else
                        {


                            //白色点
                            BMPData[offsetD]=0;//(byte)0xff;
                            BMPData[offsetD+1]=0;//(byte)0xff;
                            BMPData[offsetD+2]=0;//(byte)0xff;
                        }
                    }
                }
            }
        }
        Bitmap result=rgb2BitmapFor323X(BMPData,width,height);
//        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        result.setPixels(BMPData, 0, width, 0, 0, width, height);
        //result=result;
        return result;
    }

//=====================================================



    //doc 裁剪去水印====================================================
    private Bitmap DocGryBitmap(Bitmap img,int iCut,int ClearBack)
    {
        int height=img.getHeight();
        int width=img.getWidth();
        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        double hightTopNow=0.195*height;//0.16
        double hightBottomNow=0.2*height;//0.125
        int hTop=(int)hightTopNow;
        int hBottom=(int)hightBottomNow;
        int color_Flag=0;
        int color_Y=999999;
        int color_Flag_X=0;
        int color_Y_X=99999;
        for(int i = 0; i < height; i++)
        {
            for (int j = 0; j <width; j++)
            {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);





            //==================================
            if(ClearBack==0)
            {
                if(iCut==0)
                {
                    if(i<hightTopNow)
                    {

                        grey=255;
                    }

                    if(i>height-hightBottomNow)
                    {
                        if ((red>250 && green < 10 && blue < 10) && color_Flag==0)//判断红色
                        {
                            color_Flag=1;
                            color_Y=i;
                        }
                        if(i>=color_Y)
                        {
                            grey=255;
                        }
                    }


                }
                else
                {
                    if(i>height-hightBottomNow)
                    {

                        if ((red>250 && green < 10 && blue < 10) && color_Flag_X==0)
                        {
                            color_Flag_X=1;
                            color_Y_X=i;
                        }
                        if(i>=color_Y_X)
                        {
                            grey=255;
                        }

                    }
                }
                //if(grey>200 && ClearBack==0)
                if(grey>200)
                    grey=255;
            }








                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
            //int aBc=0;

        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        //result=result;
        return result;

    }
    //==============================裁剪图片=========================================
    private Bitmap CutBitmap(Bitmap img)
    {
        Matrix matrix1 = new Matrix();
        Bitmap  x = Bitmap.createBitmap(img, 0, 150, img.getWidth(), img.getHeight()-350, matrix1, true);
        //Bitmap  x=Bitmap.createBitmap (img, 0, 10, img.getWidth(), img.getHeight()-30, matrix1)
        return  x;
    }


//    private void SelectBarcodeToImage(Bitmap CutImage)//扣取条码放大
//    {
//        int width = CutImage.getWidth();
//        //获取位图的宽
//        if(width>832)
//            width=832;
//        int height = CutImage.getHeight();
//        if(height>1080)//获取位图的高
//            height=1080;
//        int PrintSpeedNow=PrintSpeed;
//        int PrintDensityNow=PrintDensity;
//        int PrintContrastNow=PrintContrast;
//        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
//
//        Byte[]Gray_Send;
//        ArrayList<Byte> Gray_Arraylist;
//        Gray_Arraylist=new ArrayList<Byte>();
//        //List<byte> list=new ArrayList<byte>();
//        int Gray_i=0;
//        CutImage.getPixels(pixels, 0, width, 0, 0, width, height);
//        int alpha = 0xFF << 24;
//
//        int  lineY=0;
//        int mathFlag=0;
//        for(int i = 0; i < height; i++)
//        {
//            int lineX=0;
//            int barcodeDot=0;
//            int barcodeStartDot=0;
//            int barcodeNowDot[]=new int[832];
//            int barcodeFlag=0;
//            int barcodeLineWiethMax=10;
//            int barcodeLineWidthMin=20;
//            int barcodeLineHightMax=0;
//            int imageX0=0;
//            int imageX1=0;
//            int imageY0=0;
//            int imageY1=0;
//            int x=0;
//            for (int j = 0; j <width; j++)
//            {
//
//                int grey = pixels[width * i + j];
//                int red = ((grey & 0x00FF0000) >> 16);
//                int green = ((grey & 0x0000FF00) >> 8);
//                int blue = (grey & 0x000000FF);
//
//                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
//
//                if(lineAddP[j]>0)
//                {
//
//
//                    barcodeNowDot[j]=j;
//                    int lineHigth=lineEndP[lineAddP[j]][j]-lineStartP[lineAddP[j]][j];
//                    if(lineHigth>49 && lineHigth<150)
//                    {
//
//                        if(barcodeFlag==0)
//                        {
//                            barcodeFlag=1;
//                            imageX0=j;
//                            imageY0=lineStartP[lineAddP[j]][j];
//
//                        }
//                    }
//                    else
//                    {
//                        barcodeDot++;
//                        if(j-barcodeNowDot[j-1]<barcodeLineWidthMin)//判断条码间距
//                        {
//
//                        }
//                        else
//                        {
//                            if(barcodeDot>barcodeLineWiethMax)
//                            {
//                                imageX1=j;
//                                imageY1=lineEndP[lineAddP[j]][j];
//                                barcodeDot=0;
//                                barcodeFlag=0;
//
//                            }
//                            else
//                            {
//                                imageX0=0;
//                                imageY0=0;
//                                imageX1=0;
//                                imageY1=0;
//                                barcodeDot=0;
//
//                            }
//
//                        }
//
//
//
//
//                    }
//
//
//
//                }
//                else
//                {
//                    if(j>0)
//                    {
//                        if(j-barcodeNowDot[j-1]>barcodeLineWidthMin)//判断条码间距
//                        {
//
//                        }
//                        else
//                        {
//                            if(barcodeDot>barcodeLineWiethMax)
//                            {
//                                imageX1=j;
//                                imageY1=lineEndP[lineAddP[j]][j];
//                                barcodeDot=0;
//
//                            }
//                            else
//                            {
//                                imageX0=0;
//                                imageY0=0;
//                                imageX1=0;
//                                imageY1=0;
//                                barcodeDot=0;
//                                barcodeFlag=0;
//
//                            }
//
//                        }
//                    }
//
//
//
//                }
//
//
//
//            }
//            int aBc=0;
//
//        }
//    }
private Bitmap CutEmptyImateDisplay(Bitmap CutImage,int nowWidth, int nowHeight)
{
    int PrintContrastNow=PrintContrast;
    int width = CutImage.getWidth();
    //获取位图的宽
    if(width>832)
        width=1080;
    int height = CutImage.getHeight();
    int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
    CutImage.getPixels(pixels, 0, width, 0, 0, width, height);
    int widthMaxEmpty=0;
    int heightMaxEmpty=0;
    for(int i = 0; i < height; i++)
    {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
        int lineX=0;

        for (int j = 0; j <width; j++) {

            int grey = pixels[width * i + j];
            int red = ((grey & 0x00FF0000) >> 16);
            int green = ((grey & 0x0000FF00) >> 8);
            int blue = (grey & 0x000000FF);

            grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


            if (grey > PrintContrastNow) {

            }
            else
            {
                //widthMaxEmpty=j;
                if(j>widthMaxEmpty)
                {
                    widthMaxEmpty=j;
                }
                heightMaxEmpty=i;
            }
        }
    }
    if(widthMaxEmpty<width-16)
    {
        widthMaxEmpty=widthMaxEmpty+16;

    }
    else
    {

    }
    if(heightMaxEmpty<width-16)
    {
        heightMaxEmpty=heightMaxEmpty+16;

    }
    else
    {

    }
    Bitmap bitmapDot = CutBitmapGetDot(CutImage, 0, 0, widthMaxEmpty, heightMaxEmpty);//获取原有条码 左右两边各留两个mm=16个点



    float widthP=800;

    widthP = bitmapDot.getWidth() * nowHeight / bitmapDot.getHeight();
    float heightX = bitmapDot.getHeight();
    float widthX = bitmapDot.getWidth();
    pagePercent = heightX / widthX;


    Message message = new Message();
    message.what = 10;
    Bundle bundle = new Bundle();
    bundle.putFloat("msg", pagePercent);
    message.setData(bundle);
    handler.sendMessage(message);


    if (widthP > nowWidth) {
        widthP = nowWidth;
        int xHeight = (int) widthP * bitmapDot.getHeight() / bitmapDot.getWidth();
        if (xHeight < nowHeight)
            nowHeight = xHeight;


    }
    Bitmap mBitmapZ = zoomBitmap(bitmapDot, (int)widthP, nowHeight, 0);
    return mBitmapZ;
}
    private Bitmap CutEmptyImate(Bitmap CutImage,int nowWidth, int nowHeight)
    {
        int PrintContrastNow=PrintContrast;
        int width = CutImage.getWidth();
        //获取位图的宽
        if(width>832)
            width=1080;
        int height = CutImage.getHeight();
        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        CutImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int widthMaxEmpty=0;
        int heightMaxEmpty=0;
        for(int i = 0; i < height; i++)
        {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
            int lineX=0;

            for (int j = 0; j <width; j++) {

                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


                if (grey > PrintContrastNow) {

                }
                else
                {
                    //widthMaxEmpty=j;
                    if(j>widthMaxEmpty)
                    {
                        widthMaxEmpty=j;
                    }
                    heightMaxEmpty=i;
                }
            }
        }
        if(widthMaxEmpty<width-16)
        {
            widthMaxEmpty=widthMaxEmpty+16;

        }
        else
        {
            widthMaxEmpty=width;
        }
        if(heightMaxEmpty<height-16)
        {
            heightMaxEmpty=heightMaxEmpty+16;

        }
        else
        {
            heightMaxEmpty=height;
        }
        Bitmap bitmapDot = CutBitmapGetDot(CutImage, 0, 0, widthMaxEmpty, heightMaxEmpty);//获取原有条码 左右两边各留两个mm=16个点

        Bitmap mBitmapZ = zoomBitmap(bitmapDot, nowWidth, nowHeight, 0);
        return mBitmapZ;
    }
    private Bitmap CutEmptyImateY(Bitmap CutImage,int nowWidth, int nowHeight)
    {
        int PrintContrastNow=PrintContrast;
        int width = CutImage.getWidth();
        //获取位图的宽
        if(width>832)
            width=1080;
        int height = CutImage.getHeight();
        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组
        CutImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int widthMaxEmpty=0;
        int heightMaxEmpty=0;
        for(int i = 0; i < height; i++)
        {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
            int lineX=0;

            for (int j = 0; j <width; j++) {

                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


                if (grey > PrintContrastNow) {

                }
                else
                {
                    //widthMaxEmpty=j;
                    if(j>widthMaxEmpty)
                    {
                        widthMaxEmpty=j;
                    }
                    heightMaxEmpty=i;
                }
            }
        }
        if(widthMaxEmpty<width-16)
        {
            widthMaxEmpty=widthMaxEmpty+16;

        }
        else
        {
            widthMaxEmpty=width;
        }
        if(heightMaxEmpty<height-16)
        {
            heightMaxEmpty=height;

        }
        else
        {
            heightMaxEmpty=height;
        }
        Bitmap bitmapDot = CutBitmapGetDot(CutImage, 0, 0, widthMaxEmpty, heightMaxEmpty);//获取原有条码 左右两边各留两个mm=16个点

        //Bitmap mBitmapZ = zoomBitmap(bitmapDot, nowWidth, nowHeight, 0);
        //return mBitmapZ;
        return bitmapDot;
    }
private Bitmap  SelectBarcodeToImage(Bitmap CutImage,Bitmap CutImageMain)//利用放大的图进行扣取条码
{
    int width = CutImage.getWidth();
    //获取位图的宽
    if(width>832)
        width=1080;
    int height = CutImage.getHeight();
    if(height>1080)//获取位图的高
        height=1080;
    int PrintSpeedNow=PrintSpeed;
    int PrintDensityNow=PrintDensity;
    int PrintContrastNow=PrintContrast;
    int []pixels = new int[width * height];	//通过位图的大小创建像素点数组

    Byte[]Gray_Send;
    ArrayList<Byte> Gray_Arraylist;
    Gray_Arraylist=new ArrayList<Byte>();
    //List<byte> list=new ArrayList<byte>();
    int Gray_i=0;
    CutImage.getPixels(pixels, 0, width, 0, 0, width, height);
    int alpha = 0xFF << 24;
    //int []i_G=new int[7];
    int []i_G=new int[13];
    int lineFlag=1;
    int [][]lineTimes=new int[7][width];//01234 //new int[5][width]
    int lineMax=40;//50
    //int lineFor=3;
//        int [][]lineStart=new int[5][width];
//        //int lineAdd=0;
//        int [][]lineEnd=new int[5][width];
//        int []lineAdd=new int[width];
    int  lineY=0;
    int mathFlag=0;

            int barcodeDot=0;
            int barcodeStartDot=0;
            int barcodeNowDot[]=new int[1080];
            int barcodeNowHightWucha[]=new int[1080];
            int barcodeFlag=0;
            int barcodeLineWidthMax=10;
            int barcodeLineWidthMin=20;
            int barcodeLineWidthJianJu=40;
            int barcodeLineHightMax=150;
            int barcodeLineHightMin=40;
            int barcodeLine_i=0;
            int barcodeLine_j=0;
            int imageX0[]= new int[5];//定义最大5个条码
            int imageX1[]= new int[5];
            int imageY0[]= new int[5];
            int imageY1[]= new int[5];
            int imageBarcodeDot[]=new int[5];
            int barcodeTimes=0;
            int barcodeLineStartFlag=0;
            int barcodeLineStartTimes=0;
            int barcodeLineStartj=0;
            int barcodeWhiteDotHight=0;
            //int barcodeLastX0Flag=0;
    int beforeYStartTemp=0;
    //int barcodeLastX0[]=new int[width];
    for(int i = 0; i < height; i++)
    {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
        int lineX=0;

        for (int j = 0; j <width; j++)
        {

            int grey = pixels[width * i + j];
            int red = ((grey & 0x00FF0000) >> 16);
            int green = ((grey & 0x0000FF00) >> 8);
            int blue = (grey & 0x000000FF);

            grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);








            if(grey>PrintContrastNow)
            {
                mathFlag=0;
                if(lineAddP[j]>=7)
                {
                    i=i;
                }
                if(lineTimes[lineAddP[j]][j]>lineMax)
                {


                    lineEndP[lineAddP[j]][j]=i;

                    int x=i;
                    int y=j;
                    int z=lineAddP[j];
                    int a=lineEndP[lineAddP[j]][j];
                    int b=lineStartP[lineAddP[j]][j];
                    int c=lineTimes[lineAddP[j]][j];

//====================================判断是否为条码============================================================
                    if(barcodeLineHightMax>lineTimes[lineAddP[j]][j]&&lineTimes[lineAddP[j]][j]>barcodeLineHightMin)//判断条码高度是否在范围里
                    {

                        if(barcodeFlag==99)
                        {
                             imageX0[barcodeTimes]=j;
                             imageX1[barcodeTimes]=0;
                             imageY0[barcodeTimes]=i;
                             imageY1[barcodeTimes]=0;

                        }
                        else
                        {
                            int barcodeLineHightWuCha=8;
                            int i_Y=0;
                            int i_X=0;

                            if(barcodeLineStartFlag==0)//竖线开始标志
                            {
//                                barcodeLastX0Flag++;
//                                barcodeLastX0[barcodeLastX0Flag]=j;

                                barcodeLine_j=j;
                                barcodeLineStartFlag=1;
                                barcodeLineStartTimes=0;
                            }
                            barcodeLineStartTimes++;//竖线连续相加次数

                            i_X=j-barcodeLine_j+1;
                            if(i_X==barcodeLineStartTimes)//判断竖线连续是否中断
                            {
                                //如果竖线不中断赋值横向和纵向坐标
                                barcodeLineStartj=j;
                                beforeYStartTemp=b;
                                //=================判断条码最后一条竖线的位置========================
                                if(barcodeLineStartTimes>2 &&barcodeLineStartTimes<40)
                                {
                                    if(i-barcodeLine_i<barcodeLineHightWuCha)
                                    {
                                        if(barcodeDot>barcodeLineWidthMax) {
                                            imageX1[barcodeTimes] = j;
                                        }
                                    }
                                }
                                //==================================================
                                barcodeLine_i=i;

                            }
                            else
                            {
                                barcodeLineStartFlag=0;//如果竖线中断清除竖线开始标志
                                i_X=j-barcodeLineStartj;//赋值下一条竖线和上一条竖线的差值
                                barcodeLineStartTimes=0;
                                i_Y=i-barcodeLine_i;//赋值上一条竖线和当前竖线的底部坐标的差值
                                int beforeYStart=beforeYStartTemp;//赋值上一条竖线的y0值
                                beforeYStartTemp=0;
                                int nowYStart=lineStartP[lineAddP[j]][j];//赋值当前竖线的y0值
                                int beforeY=0;
                                if(nowYStart>beforeYStart)//赋值上一条竖线和当前竖线的顶部坐标的差值
                                {
                                     beforeY=nowYStart-beforeYStart;
                                }
                                else
                                {
                                    beforeY=beforeYStart-nowYStart;
                                }

                                if(i_Y<barcodeLineHightWuCha && beforeY<barcodeLineHightWuCha)//判断条码高度误差是否否在误差范围里面
                                {
                                    if(i_X>0 && i_X<barcodeLineWidthJianJu)//判断条码间距是否在范围里
                                    {
                                        if(imageX0[barcodeTimes]>barcodeLine_j)
                                        {
                                            barcodeFlag=0;
                                        }
                                        if(barcodeFlag==0)
                                        {
                                            //int geng=barcodeLastX0Flag-1;

                                            imageX0[barcodeTimes]=barcodeLine_j;//barcodeLine_j;//初始化条码左上角坐标 X0,Y0
                                            //barcodeLastX0Flag=0;
                                            imageX1[barcodeTimes]=0;
                                            imageY0[barcodeTimes]=lineStartP[lineAddP[j]][j];
                                            imageY1[barcodeTimes]=barcodeLine_i;
                                            barcodeFlag=1;
                                        }
                                        barcodeDot++;
                                        if(barcodeDot>barcodeLineWidthMax){
                                            imageX1[barcodeTimes]=j;
                                            imageBarcodeDot[barcodeTimes]=barcodeDot;
                                        }
                                    }
                                    else
                                    {
                                        if(barcodeDot>barcodeLineWidthMax)//判断是否是条码 大于10的判断为条码
                                        {
                                            barcodeTimes++;
                                            barcodeDot=0;
                                            barcodeFlag=0;
                                            //barcodeLastX0Flag=0;
                                        }
                                        else
                                        {
                                            imageX0[barcodeTimes]=0;//初始化条码左上角坐标 X0,Y0
                                            imageX1[barcodeTimes]=0;
                                            imageY0[barcodeTimes]=0;
                                            imageY1[barcodeTimes]=0;
                                            imageBarcodeDot[barcodeTimes]=0;
                                            barcodeFlag=0;
                                            //barcodeLastX0Flag=0;
                                        }

                                    }

                                }
                                else
                                {
                                    if(barcodeDot>barcodeLineWidthMax)//判断是否是条码 大于10的判断为条码
                                    {
                                        barcodeTimes++;
                                        barcodeDot=0;
                                        barcodeFlag=0;
                                        //barcodeLastX0Flag=0;
                                    }
                                    else
                                    {
                                        imageX0[barcodeTimes]=0;//初始化条码左上角坐标 X0,Y0
                                        imageX1[barcodeTimes]=0;
                                        imageY0[barcodeTimes]=0;
                                        imageY1[barcodeTimes]=0;
                                        imageBarcodeDot[barcodeTimes]=0;
                                        barcodeFlag=0;
                                        //barcodeLastX0Flag=0;
                                    }
                                }


                            }


                        }


                    }
//===================================================================================================================
                    lineAddP[j]++;
                }
                else
                {
                    int x=i;
                    int y=j;
                    int z=lineAddP[j];
                    int a=lineEndP[lineAddP[j]][j];
                    int b=lineStartP[lineAddP[j]][j];

                    lineEndP[lineAddP[j]][j]=0;
                    lineTimes[lineAddP[j]][j]=0;
                    lineStartP[lineAddP[j]][j]=0;
                }
//                //================判断纸张后面是否为空白纸
//                if(barcodeWhiteDotHight==9999)
//                    barcodeWhiteDotHight=i;
//                //===============================

            }
            else
            {
//                barcodeWhiteDotHight=9999;


                //================判断纸张后面是否为空白纸

                    barcodeWhiteDotHight=i;
                //===============================
                if(lineTimes[lineAddP[j]][j]==0)
                {

                    lineStartP[lineAddP[j]][j]=i;
                }

                mathFlag=1;
                lineTimes[lineAddP[j]][j]++;


                if(i>=height-1){
                    if(lineTimes[lineAddP[j]][j]>lineMax)
                    {
                        lineEndP[lineAddP[j]][j]=i;
                        lineAddP[j]++;
                    }
                    else
                    {
                        lineEndP[lineAddP[j]][j]=0;
                        lineTimes[lineAddP[j]][j]=0;
                        lineStartP[lineAddP[j]][j]=0;
                    }
                }

            }




//                grey = alpha | (grey << 16) | (grey << 8) | grey;
//                pixels[width * i + j] = grey;
        }
        int aBc=0;

    }
    Bitmap newCutImage=CutImageMain;
    int beforeWidth=CutImageMain.getWidth();//没有放大的原来的
    int bigWidth=CutImage.getWidth();//放大为1080
    double beforeBarcodeWidth=1;
    for(int i=0;i<barcodeTimes+1;i++)
    {
        if(imageBarcodeDot[i]>10)//判断是否为条码
        {
            int barcodeWidthNow = imageX1[i] - imageX0[i];
            int barcodeNeedBigWidth = 50 * 8;//设置需要放大的条码的最小宽度
            int barcodeNeddBigSeveral = 27;//设置需要放大的条码的最小条数
            if (imageX1[i] > imageX0[i] && imageY1[i] > imageY0[i]) {


                Bitmap bitmapDot = CutBitmapGetDot(CutImage, imageX0[i] - 3, imageY0[i], imageX1[i] + 3, imageY1[i]);//获取原有条码 左右两边各留两个mm=16个点
                String getGeng[] = new String[2];

                getGeng = getOneBarcode(bitmapDot);//识别一维条码为字符
                String geng = getGeng[0];
                String geng1 = getGeng[1];
                //code128计算条码宽度===========================
                beforeBarcodeWidth = bitmapDot.getWidth() * 1.0 * beforeWidth * 1.0 / bigWidth * 1.0;//获取原有条码的宽度
                double minWidth = (geng.length() * 5 + 35) * 0.01 * 25.4;//0.01639英寸 理论最小值为0.07英寸   现在设置最小线为0.3mm 相当于1.016个点
                barcodeNeedBigWidth = ((int) minWidth + 2) * 8+24;//((int) minWidth + 2) * 8 增加误差判断24个点
                Boolean gengOK = false;
                //if(barcodeWidthNow<barcodeNeedBigWidth && imageBarcodeDot[i]>barcodeNeddBigSeveral && barcodeWidthNow>0)
                if (geng.length() == 0) {
                    gengOK = false;
                } else {


                    if (geng1.indexOf("39", 1) != -1) {
                        barcodeNeddBigSeveral = geng.length() * 5 + 10;

                        barcodeNeedBigWidth = barcodeNeddBigSeveral * 2 * 2 + geng.length() * 3 * 4;//最小线为2个点
                        if ((beforeBarcodeWidth < barcodeNeedBigWidth && beforeBarcodeWidth > 0)) {
                            gengOK = true;
                        } else {
                            gengOK = false;
                        }

                    } else {
                        if ((beforeBarcodeWidth < barcodeNeedBigWidth && beforeBarcodeWidth > 0))//判断128码
                        {
                            gengOK = true;
                        } else {
                            gengOK = false;
                        }
                    }
                }
                //gengOK=false;
                if (gengOK)//判断该条码是否需要放大
                {
                    //获取原来条码宽度imageX1[i]-imageX0[i]
                    int barcodeSideWidth = 8 * 8;//生成条码两边的空白宽度
                    int barcodeBigDot = CutImageMain.getWidth() - barcodeSideWidth - barcodeSideWidth;//左右各去除8个mm，增大为新的标签宽度

    //                Bitmap bitmapDot=CutBitmapGetDot(CutImage,imageX0[i]-2,imageY0[i],imageX1[i]+2,imageY1[i]);//获取原有条码
    //                String ssss=getOneBarcode(bitmapDot);//识别一维条码为字符
    //                beforeBarcodeWidth=bitmapDot.getWidth()*1.0*beforeWidth*1.0/bigWidth*1.0;//获取原有条码的宽度

                    Bitmap bitmapEmpty = Bitmap.createBitmap((int) beforeBarcodeWidth + 6, bitmapDot.getHeight(), Bitmap.Config.ARGB_8888);//创建一个新的空白图片来替换原有无法识别的条码

                    //bitmapEmpty.eraseColor(Color.parseColor("#000000"));//填充颜色
                    bitmapEmpty.eraseColor(Color.WHITE);
    //                Canvas canvas =new Canvas();
    //                canvas.drawColor(Color.WHITE);
    //                canvas.drawBitmap(bitmapEmpty, 0,0, null);
                    //Bitmap bitmapBig=zoomBitmap(bitmapDot,barcodeBigDot,imageY1[i]-imageY0[i],0);//将条码放大
                    Bitmap bitmapBig = zoomBitmap(bitmapDot, barcodeBigDot, 40, 0);//将条码放大
                    String getGengX[] = new String[2];
                    getGengX = getOneBarcode(bitmapBig);//识别一维条码为字符
                    if(getGengX[0].length()>0)
                    {
                        getGeng[0]=getGengX[0];
                        getGeng[1]=getGengX[1];
                    }
                    else
                    {

                    }


                    String barcodeNumber = getGeng[0];
                    int startX0 = imageX0[i] - barcodeBigDot;

                    double xishu = beforeWidth * 1.0 / bigWidth * 1.0;
                    double nowX0 = xishu * imageX0[i];
                    if (nowX0 > 4) {
                        nowX0 = nowX0 - 4;
                    } else {
                        nowX0 = nowX0 - nowX0;
                    }
                    int beforeBarcodeX0 = (int) nowX0;
                    if (startX0 > 0) {

                    } else {
                        startX0 = 0;
                    }
                    int nowEmptyHight = CutImageMain.getHeight() - barcodeWhiteDotHight - 1;//取出剩余空白标签的宽度
                    int barcodeMakeHight = 10 * 8;//生成条码的高度5*8

                    Bitmap bitmapX = Bitmap.createBitmap(CutImageMain.getWidth(), CutImageMain.getHeight(), CutImageMain.getConfig());//创建一个新的图片
                    Canvas canvas = new Canvas(bitmapX);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(CutImageMain, new Matrix(), null);

                    canvas.drawBitmap(bitmapEmpty, beforeBarcodeX0, imageY0[i], null);


                    if (nowEmptyHight > barcodeMakeHight)//条码宽度赋值为5mm 小于5mm的增加图片原有高度
                    {
                        newCutImage = mergeBitmap(bitmapX, bitmapBig, bitmapEmpty, beforeBarcodeX0, imageY0[i], 0, barcodeSideWidth, barcodeNumber);//合并图片
                    } else {
                        barcodeMakeHight = barcodeMakeHight - nowEmptyHight;
                        newCutImage = mergeBitmap(bitmapX, bitmapBig, bitmapEmpty, beforeBarcodeX0, imageY0[i], barcodeMakeHight, barcodeSideWidth, barcodeNumber);
                    }
                    i = barcodeTimes;
                }

                i = i;
            }
        }

    }
    return  newCutImage;

}
private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap,Bitmap bitmapEmpty,int x0,int y0,int barcodeWhiteDotHight,int barcodeSideWidth,String barcodeString) {
        int nowHeight=1080;
        Bitmap bitmapNow=null;
        if(barcodeWhiteDotHight%8==0)
        {

        }else
        {
            int xy=8-barcodeWhiteDotHight%8;
            barcodeWhiteDotHight=xy+barcodeWhiteDotHight;
        }
        if(barcodeWhiteDotHight>0)
        {
            if(firstBitmap.getHeight()+barcodeWhiteDotHight>1080)
            {
                nowHeight=1080-barcodeWhiteDotHight;
                firstBitmap=zoomBitmap(firstBitmap,firstBitmap.getWidth(),nowHeight,0);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight()+barcodeWhiteDotHight,firstBitmap.getConfig());//创建一个新的图片
    Canvas canvas = new Canvas(bitmap);
    canvas.drawColor(Color.WHITE);
    canvas.drawBitmap(firstBitmap, new Matrix(), null);

    //canvas.drawBitmap(bitmapEmpty, x0, y0, null);
    //canvas.drawColor(Color.WHITE);

    canvas.drawBitmap(secondBitmap, barcodeSideWidth, bitmap.getHeight()-78, null);


    Paint paint=new Paint();
    paint.setColor(Color.BLACK);
    paint.setTextSize(18);

    paint.setStrokeWidth(4);
    int textMaxWidth=(int)(paint.measureText(barcodeString));
    int xyz=(bitmap.getWidth()-textMaxWidth)/2;
    canvas.drawText(barcodeString,xyz,bitmap.getHeight()-10,paint);




    //canvas.drawText(barcodeString,xyz,bitmap.getHeight()-40,null);

//    //================================创建空白 图片
//    Bitmap bitmapWhite =Bitmap.createBitmap(secondBitmap.getWidth(),secondBitmap.getHeight(),Bitmap.Config.ARGB_8888);
//    Canvas canvasWhite=new Canvas(bitmapWhite);
//    canvasWhite.drawColor(Color.WHITE);
//    canvasWhite.drawBitmap(bitmapWhite,new Matrix(),null);
////===============================================================



    return bitmap;
}
    private  String[] getOneBarcode(Bitmap img) {
        String onebarcode[] = new String[2];

//        //Bitmap img = BitmapUtils.getCompressedBitmap(filename);
//        MultiFormatReader reader = new MultiFormatReader();
//        Result result = reader.decode(img);
//
//
//        //Bitmap img = BitmapUtils.getCompressedBitmap(filename);
//        //BitmapDecoder decoder = new BitmapDecoder(context);
//        //Result result = decoder.getRawResult(img);
//        String resP = "";
//        if (result != null
//                && codeType(DecodeFormatManager.ONE_D_FORMATS,
//                result.getBarcodeFormat())) {
//            resP = "|" + result.getResultPoints()[1].getX() + "|"
//                    + result.getResultPoints()[1].getY() + "|";
//            onebarcode = result.getText() + resP;
//        }
//        return onebarcode;



        img=BitmapToGrayBitmap(img);
        //Bitmap bMap = Bitmap.createBitmap(mTwod.width(), mTwod.height(), Bitmap.Config.ARGB_8888);

        int[] intArray = new int[img.getWidth()*img.getHeight()];
//copy pixel data from the Bitmap into the 'intArray' array
        img.getPixels(intArray, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        LuminanceSource source = new RGBLuminanceSource(img.getWidth(), img.getHeight(),intArray);

        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));




//        Reader reader = new DataMatrixReader();
////....doing the actually reading
//        Result result = reader.decode(bitmap);


        MultiFormatReader reader = new MultiFormatReader();
        try {
//            Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
//            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

//            Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
//            // 能解析的编码类型 和 解析时使用的编码。
//            Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
//
////            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
////            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
//            hints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.CODE_39);
//            hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
//            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");








            Result result = reader.decode(bitmap);
            String strResult = "BarcodeFormat:"
                    + result.getBarcodeFormat().toString() + "  text:"
                    + result.getText();
            onebarcode[0]=result.getText();
            onebarcode[1]=result.getBarcodeFormat().toString();

        } catch (Exception e) {
            onebarcode[0]="";
            onebarcode[1]="";
        }
        return  onebarcode;


}

private Bitmap  CutBitmapGetDot(Bitmap img,int x0,int y0,int x1,int y1)
    {
        Matrix matrix1 = new Matrix();
        Bitmap  x = Bitmap.createBitmap(img, x0, y0, x1-x0, y1-y0, matrix1, true);
        //Bitmap  x=Bitmap.createBitmap (img, 0, 10, img.getWidth(), img.getHeight()-30, matrix1)
        return  x;
    }
    private void Dottedine(Bitmap dImage)
    {
        int width = dImage.getWidth();
        //获取位图的宽
        if(width>832)
            width=832;
        int height = dImage.getHeight();
        if(height>1080)//获取位图的高
            height=1080;
        int PrintSpeedNow=PrintSpeed;
        int PrintDensityNow=PrintDensity;
        int PrintContrastNow=PrintContrast;
        int []pixels = new int[width * height];	//通过位图的大小创建像素点数组

        Byte[]Gray_Send;
        ArrayList<Byte> Gray_Arraylist;
        Gray_Arraylist=new ArrayList<Byte>();
        //List<byte> list=new ArrayList<byte>();
        int Gray_i=0;
        dImage.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        //int []i_G=new int[7];
        int []i_G=new int[13];
        int lineFlag=1;
        int [][]lineTimes=new int[7][width];//01234 new int[5][width]
        int lineMax=50;
        //int lineFor=3;
//        int [][]lineStart=new int[5][width];
//        //int lineAdd=0;
//        int [][]lineEnd=new int[5][width];
//        int []lineAdd=new int[width];
        int  lineY=0;
        int mathFlag=0;
        for(int i = 0; i < height; i++)
        {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
            int lineX=0;

            for (int j = 0; j <width; j++)
            {

                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);








                    if(grey>PrintContrastNow)
                    {
                        mathFlag=0;
                        if(lineTimes[lineAddP[j]][j]>lineMax)
                        {
                            lineEndP[lineAddP[j]][j]=i;

                            int x=i;
                            int y=j;
                            int z=lineAddP[j];
                            int a=lineEndP[lineAddP[j]][j];
                            int b=lineStartP[lineAddP[j]][j];
                            lineAddP[j]++;
                        }
                        else
                        {
                            int x=i;
                            int y=j;
                            int z=lineAddP[j];
                            int a=lineEndP[lineAddP[j]][j];
                            int b=lineStartP[lineAddP[j]][j];

                            lineEndP[lineAddP[j]][j]=0;
                            lineTimes[lineAddP[j]][j]=0;
                            lineStartP[lineAddP[j]][j]=0;
                        }

                    }
                    else
                    {
                        if(lineTimes[lineAddP[j]][j]==0)
                        {

                            lineStartP[lineAddP[j]][j]=i;
                        }

                        mathFlag=1;
                        lineTimes[lineAddP[j]][j]++;


                        if(i>=height-1){
                            if(lineTimes[lineAddP[j]][j]>lineMax)
                            {
                                lineEndP[lineAddP[j]][j]=i;
                                lineAddP[j]++;
                            }
                            else
                            {
                                lineEndP[lineAddP[j]][j]=0;
                                lineTimes[lineAddP[j]][j]=0;
                                lineStartP[lineAddP[j]][j]=0;
                            }
                        }

                    }




//                grey = alpha | (grey << 16) | (grey << 8) | grey;
//                pixels[width * i + j] = grey;
            }
            int aBc=0;

        }
    }
    //======================================

    public  Bitmap ChangeImagePDF(Bitmap img,final int PrintCopies,int iCut,int addressFile)
    {
        boolean PrintHalftoneNow=PrintHalftone;
        String PrintHalftoneSNow=PrintHalftoneS;
        if(PrintHalftoneNow)
        {
            if(addressFile==1)//文档
            {
                //灰度打印
            }else//图片
            {
                img=convertGreyImgByFloyd(img);//误差扩散
            }
        }
        else
        {


                if(PrintHalftoneSNow.equals(PrintHalftone_String[0]))
                {
                    img = DitherToBitmap(img);//抖动
                }
                else if(PrintHalftoneSNow.equals(PrintHalftone_String[1]))
                {
                    img=convertGreyImgByFloyd(img);//误差扩散
                }
                else
                {
                    //灰度打印
                }

        }


//        if(addressFile==1)//if(PrintHalftoneNow)//
//        {
//
//
//            if(PrintHalftoneNow)
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//        }
//        else
//        {
//            if(PrintHalftoneNow)
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//            else {
//                img = DitherToBitmap(img);//抖动
//            }
//            //img=convertGreyImgByFloyd(img);
//        }
        if(doc_Flag)
        img=DocGryBitmap(img,iCut,0);

        lineStartP=new int[7][img.getWidth()];//int[5][img.getWidth()]
        //int lineAdd=0;
        lineEndP=new int[7][img.getWidth()];//int[5][img.getWidth()]
        lineAddP=new int[img.getWidth()];
        //Dottedine(img);//竖线虚线话算法

       // SelectBarcodeToImage(img);//扣取条码 后面改为先放大图片再扣取


        int width = img.getWidth();
        //获取位图的宽
        if(width>832)
            width=832;
        int height = img.getHeight();
        if(height>1080)//获取位图的高
            height=1080;
        int PrintSpeedNow=PrintSpeed;

        if(PrintSpeedNow==6)
        {
            delayTimes=5200;
        }
        else if(PrintSpeedNow==5)
        {
            delayTimes=5800;
        }
        else if(PrintSpeedNow==4)
        {
            delayTimes=6400;
        }
        else if(PrintSpeedNow==3)
        {
            delayTimes=7000;
        }
        else if(PrintSpeedNow==2)
        {
            delayTimes=7600;
        }
        else if(PrintSpeedNow==1)
        {
            delayTimes=8200;
        }
        else
        {
            delayTimes=5200;
        }
        int PrintDensityNow=PrintDensity;
        int PrintContrastNow=PrintContrast;
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
        int lineFlag=1;
        int []lineTimes=new int[7];//01234
        int lineMax=120;
        int lineFor=3;
        int []lineStart=new int[7];
        int []lineEnd=new int[7];


        int Send_Gray=0x00;
//        String Start_S="@<"+hexStr2Str("0D0A0000")+"#<";
////        String String_S="";
////
////        String End_S=">#@>";
        int StartInt=0;
        char  StartWords=' ';
//        StartWords='<';
//        Gray_Arraylist.add((byte)StartWords);
        //命令格式============================
        //! 0 200 200 240 1
        //PW 320
        //CG 40 240 0 0
        //=====================================

        //! 0 200 200 1000 1
        //21 20 30 20 32 30 30 20 32 30 30 20 31 30 30 30 20 31 20 0A
        StartInt=0x21;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x32;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x32;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

//        StartInt=0x31;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x38;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);


        int qw=height/1000%10;
        //求百位
        int bw=height/100%10;
        //求十位
        int sw=height/10%10;
        //求个位
        int gw=height%10;
            Log.e(TAG1, "千"+qw+"百"+bw+"十"+sw+"个"+gw);
        if(qw!=0)
        {
            StartInt=qw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=bw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=sw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=gw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
        else
        {
            if(bw!=0)
            {
                StartInt=bw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=sw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=gw+48;
                Gray_Arraylist.add((byte)StartInt);
            }
            else
            {
                StartInt=sw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=gw+48;
                Gray_Arraylist.add((byte)StartInt);
            }

        }

        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x31;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        //PW 800
        //50 57 20 38 30 30 0A

        StartInt=0x50;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x57;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);



//        StartInt=0x38;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);

//====================
        int WqwX=width/1000%10;
        //求百位
        int WbwX=width/100%10;
        //求十位
        int WswX=width/10%10;
        //求个位
        int WgwX=width%10;
       // Log.e(TAG1, "千"+Wqw+"百"+Wbw+"十"+Wsw+"个"+Wgw);

        if(WbwX!=0)
        {
            StartInt=WbwX+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=WswX+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=WgwX+48;
            Gray_Arraylist.add((byte)StartInt);
        }
        else
        {
            StartInt=WswX+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=WgwX+48;
            Gray_Arraylist.add((byte)StartInt);
        }
//        StartInt=0x38;//WbwX+48;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x33;//WswX+48;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x32;//WgwX+48;
//        Gray_Arraylist.add((byte)StartInt);

//        StartInt=0x20;
//        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        //=========DENSITY================
        //DENSITY
        StartWords='D';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='E';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='N';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='S';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='I';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='T';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='Y';
        Gray_Arraylist.add((byte)StartWords);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        //求十位
        int DensitySw=PrintDensityNow/10%10;
        //求个位
        int DensityGw=PrintDensityNow%10;
        if(DensitySw!=0)
        {

            StartInt=DensitySw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=DensityGw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
        else
        {

            StartInt=DensityGw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
//        StartInt=0x20;
//        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        //==========SPEED==============================================
        //SPEED
        StartWords='S';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='P';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='E';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='E';
        Gray_Arraylist.add((byte)StartWords);
        StartWords='D';
        Gray_Arraylist.add((byte)StartWords);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        //求十位
        int SpeedSw=PrintSpeedNow/10%10;
        //求个位
        int SpeedGw=PrintSpeedNow%10;
        if(SpeedSw!=0)
        {

            StartInt=SpeedSw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=SpeedGw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
        else
        {

            StartInt=SpeedGw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
//        StartInt=0x20;
//        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);
        //==============================================

        //CG 100 1000 0 0
        //43 47 20 31 30 30 20 31 30 30 30 20 30 20 30 20
        StartInt=0x43;//CG
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x47;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);



        //====================
        int Wqw=width/8/1000%10;
        //求百位
        int Wbw=width/8/100%10;
        //求十位
        int Wsw=width/8/10%10;
        //求个位
        int Wgw=width/8%10;
        Log.e(TAG1, "千"+Wqw+"百"+Wbw+"十"+Wsw+"个"+Wgw);

            if(Wbw!=0)
            {
                StartInt=Wbw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=Wsw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=Wgw+48;
                Gray_Arraylist.add((byte)StartInt);
            }
            else
            {
                StartInt=Wsw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=Wgw+48;
                Gray_Arraylist.add((byte)StartInt);
            }


        //=========================


        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);


//        StartInt=0x31;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x38;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
        if(qw!=0)
        {
            StartInt=qw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=bw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=sw+48;
            Gray_Arraylist.add((byte)StartInt);
            StartInt=gw+48;
            Gray_Arraylist.add((byte)StartInt);
        }
        else
        {
            if(bw!=0)
            {
                StartInt=bw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=sw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=gw+48;
                Gray_Arraylist.add((byte)StartInt);
            }
            else
            {
                StartInt=sw+48;
                Gray_Arraylist.add((byte)StartInt);
                StartInt=gw+48;
                Gray_Arraylist.add((byte)StartInt);
            }

        }

        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;//CG
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);

        //PR 0
        //FORM
        //PRINT
        int k=0;
        int Send_i=0;
        int mathFlag=0;
        int l_k=0;
        int l_n=0;
        int l_m=0;
        int lineFlagOK=0;
        int lineFlagOK_A=0;
        int lineFor_A=0;
        int j_x=0;
        int lineAdd=0;
        int lineAddMax=0;
        int line_i=0;
        int []lineNowAdd=new int[width];
        int []lineHang=new int[width];
        for(int i = 0; i < height; i++)
        {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
            k=0;
            Send_i=0;
            lineFlagOK_A=0;
            if(i==664)
            {
                i=i;
            }
            if(lineFlagOK==1)
            {
                lineFor_A++;
                l_m++;
                if(lineFor_A>lineFor)
                {
                    lineFor_A=0;
                    lineFlagOK_A=0;
                    lineFlagOK=0;
                    l_k=0;
                    l_m=0;
                    for(int m=0;m<7;m++)
                    {
                        lineEnd[m]=0;
                        lineStart[m]=0;
                        lineTimes[m]=0;
                    }

                }else
                {
                    lineFlagOK_A=1;


                }


            }
            l_n=0;
            if(lineAdd>lineAddMax) {
                lineAddMax = lineAdd;
                line_i=i;
            }
            if(i==height-1)
            {
                lineAddMax=lineAddMax;
                line_i=line_i;
            }
            lineAdd=0;
            for (int j = 0; j <width; j++)
            {

                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


//==================================

                if(lineFlagOK_A==1){
                    if(j==lineEnd[l_n])
                    {
                        //l_n++;
                        l_n=l_n+1;
                        j_x=0;

                    }
                    int l_x=l_n;
                    if(l_x<l_k)
                    {
// 1  5   4   5 是1/5的黑点 4/5的白点
// 2  5   3   5 是2/5的黑点 3/5的白点
// 2  4   2   4 是2/4的黑点 2/4的白点
                        if(j>=lineStart[l_n]+2 && j<lineEnd[l_n]-2)
                        {
                            if(l_m%2!=0){
                                if(j_x<2) {
                                    mathFlag = 1;
                                }
                                else{

                                    mathFlag = 0;
                                }
                                j_x++;
                                if(j_x>=4)
                                {
                                    j_x=0;
                                }
                            }
                            else
                            {
                                if(j_x<2) {
                                    mathFlag = 0;
                                }
                                else{

                                    mathFlag = 1;
                                }
                                j_x++;
                                if(j_x>=4)
                                {
                                    j_x=0;
                                }
                            }

                            //*mathFlag = 1;
                        }
                        else
                        {
                            if(grey>PrintContrastNow)
                            {
                                //bufImage[j]=0x00;
                                mathFlag=0;

                            }
                            else
                            {
                                //bufImage[j]=0x01;
                                mathFlag=1;
                            }
                        }


                    }else
                    {
                        if(grey>PrintContrastNow)
                        {
                            //bufImage[j]=0x00;
                            mathFlag=0;

                        }
                        else
                        {
                            //bufImage[j]=0x01;
                            mathFlag=1;
                        }
                    }

                }
                else
                {
                    if(grey>PrintContrastNow)
                    {
                        //bufImage[j]=0x00;
                        mathFlag=0;

                    }
                    else
                    {
                        //bufImage[j]=0x01;
                        mathFlag=1;
                    }
                }


//                int lineFlag=1;
//                int []lineTimes=new int[5];//01234
//                int lineMax=20;
//                int lineFor=2;
//                int []lineStart=new int[5];
//                int []lineEnd=new int[5];
//
                if(lineFlag>0 & lineFlagOK_A==0)
                {
                    if(mathFlag>0)
                    {

                        if(lineTimes[l_k]==0)
                        {
                            lineStart[l_k]=j;
                        }
                        lineTimes[l_k]++;
                        if(j>=width-1)
                        {
                            lineEnd[l_k]=j;
                            if(lineTimes[l_k]>lineMax)
                            {
                                l_k++;
                                lineTimes[l_k]=0;
                                lineEnd[l_k]=0;
                                lineStart[l_k]=0;
                                lineFlagOK=1;
                            }
                            else
                            {
                                lineEnd[l_k]=0;
                                lineStart[l_k]=0;
                                lineTimes[l_k]=0;
                            }

                        }
                    }
                    else
                    {
                        if(lineTimes[l_k]>lineMax)
                        {
                            if(lineTimes[l_k]>=558)
                            {
                                lineTimes[l_k]=lineTimes[l_k];
                            }
                            lineEnd[l_k]=j;
                            l_k++;
                            lineTimes[l_k]=0;
                            lineEnd[l_k]=0;
                            lineStart[l_k]=0;
                            lineFlagOK=1;

                        }
                        else
                        {
                            lineEnd[l_k]=0;
                            lineStart[l_k]=0;
                            lineTimes[l_k]=0;
                        }
                    }

                }
                if(lineAddP[j]>0)
                {
                    if(lineStartP[lineNowAdd[j]][j]<=i && lineEndP[lineNowAdd[j]][j]>=i)
                    {
                        if(lineHang[j]<1) {
                            mathFlag = 1;
                        }
                        else{

                            mathFlag = 0;
                        }
                        lineHang[j]++;
                        if(lineHang[j]>=2)
                        {
                            lineHang[j]=0;
                        }




                        if(lineEndP[lineNowAdd[j]][j]==i)
                        {
                            lineNowAdd[j]++;
                        }
                    }
                }
                if(mathFlag>0)
                {
                    lineAdd++;
                }

                k++;
                if(k==1)
                {
                    Send_i=0;
                    Send_i=Send_i+128*mathFlag;//mathFlag|0x80
                }
                else if(k==2)
                {
                    Send_i=Send_i+64*mathFlag;//mathFlag|0x40
                }
                else if(k==3)
                {
                    Send_i=Send_i+32*mathFlag;//mathFlag|0x20
                }
                else if(k==4)
                {
                    Send_i=Send_i+16*mathFlag;//mathFlag|0x10
                }
                else if(k==5)
                {
                    Send_i=Send_i+8*mathFlag;//mathFlag|0x08
                }
                else if(k==6)
                {
                    Send_i=Send_i+4*mathFlag;//mathFlag|0x04
                }
                else if(k==7)
                {
                    Send_i=Send_i+2*mathFlag;//mathFlag|0x02
                }
                else if(k==8)
                {
                    Send_i=Send_i+1*mathFlag;//mathFlag|0x01
                    Gray_Arraylist.add((byte)Send_i);

                    Send_i=0;
                    k=0;
                }
// =================================





                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
            int aBc=0;

        }

//        PR 0
//        FORM
//        PRINT
        //50 52 20 30 0A 46 4F 52 4D 0A 50 52 49 4E 54 0A
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x50;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x52;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x20;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x46;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x4F;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x52;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x4D;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x50;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x52;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x49;//CG
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x4E;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x54;//CG
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0A;
        Gray_Arraylist.add((byte)StartInt);

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        //result=result;
        //try {
            if (socket1 != null)
                Log.e(TAG1, "null is now");
            else {




                final byte[] sss=new byte[Gray_Arraylist.size()];
                Gray_Send=new Byte[Gray_Arraylist.size()];
                Gray_Arraylist.toArray(Gray_Send);
                for(int xx=0;xx<Gray_Send.length;xx++){
                    sss[xx]=Gray_Send[xx];
                }

                byte[] sssy=new byte[1];
                sssy[0]=0x01;


                new receiveThread().start();
                if(!printEnableFlag)
                {
                    return result;
                }
                //sssy[1]=0x02;
                //======================================
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {


                        for(int j=0;j<1;j++)
                        {
                            printEnable=false;

                            try
                            {
                                if(PaperNumberFlag==0)
                                {
                                    hmessage.sendEmptyMessage(1);
                                }

                                OutputStream os = socketallViewFipper.getOutputStream();

                                os.write(sss);
//                                os.flush();
//                                os.close();
                                PaperNumberFlag++;
                                Thread.sleep(delayTimes);//休眠3秒
                                if(PaperNumberFlag>=PaperNumber*PrintCopies-1)
                                {
                                    printEnable=true;
                                    hmessage.sendEmptyMessage(0);
                                }
                                else
                                {
                                    hmessage.sendEmptyMessage(PaperNumberFlag+1);
                                }



////
//                                //存数据 方法1============================
//                               // File file = new File(ViewFlipperOnTouchActivity.this.getFilesDir(), "angenlTestA.txt");//创建文件到app内部
//
//                                FileOutputStream fileOutputStream=openFileOutput("angenlTestA.txt",MODE_PRIVATE);
//                                //String content=editText.getText().toString().trim();
//                                fileOutputStream.write(sss);
//                                fileOutputStream.close();
//                                //==============================

//                                //存储数据方法2=============================================
//                                final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
//                                final String outputTXT = storageDir + "angenlTestB.txt";
//                                os = new FileOutputStream(outputTXT);
//                                os.write(sss, 0, sss.length);
//                                os.flush();
//                                //========================================






////                                //存储数据方法3==============================================================
//                                //context.getFilesDir()帮助我们返回一个路径/data/data/包名/files/
//                                final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
//                                File file=new File(storageDir, "angenlTestD.txt");
//
//                                FileOutputStream fos=new FileOutputStream(file);
//
//                                fos.write(sss);
//
//                                fos.close();
////                                //============================================================================
                            }
                            catch (Exception ex) {
//                                if(!printEnable) {
//                                    //hmessage.sendEmptyMessage(0);
//
//
//                                    printEnable = true;
//                                    j=1024;
//                                }
                                ex.printStackTrace();
                            }
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try
//                                        {
//                                        OutputStream os = socketallViewFipper.getOutputStream();
//
//                                        os.write(sss);
//                                            Thread.sleep(500);//休眠3秒
//                                    }
//                                        catch (Exception ex) {
//                                            ex.printStackTrace();
//                                        }
//                                    }
//                                },500);

                            //OutputStream os = BlueToothMainActivity.socketall.getOutputStream();


                            //os.close();
                        }
//
//                    }
//                });
//                thread.start();

                //===============================================

//                for(int j=0;j<PrintCopies;j++)
//                {
//                    //OutputStream os = BlueToothMainActivity.socketall.getOutputStream();
//                    OutputStream os = socketallViewFipper.getOutputStream();
//
//                    os.write(sss);
//                    //os.close();
//                }

            }
        //}
//        catch (IOException e)
//        {
//            Log.e(TAG1,"s");
//        }
        return result;
    }
    private int numExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        int numActivities=0;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList =am.getRunningTasks(10);//

            ActivityManager.RunningTaskInfo runningTaskInfo = taskInfoList.get(0);// 只是拿          当前运行的栈
             numActivities = taskInfoList.get(0).numActivities;

//            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
//                if (taskInfo.baseActivity.equals(cmpName)) {// 说明它已经启动了
//                    flag = true;
//                    break;//跳出循环，优化效率
//                }
//            }
        }
        return numActivities;
    }


    private void manageConnectedSocket(BluetoothSocket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() == 0) {
                inputStream = socket.getInputStream();
            }
            int available = inputStream.available();
            byte[] bytes = new byte[available];
            inputStream.read(bytes, 0, available);
            String string = new String(bytes);
            System.out.println(string);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class receiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(true){
                if(socketallViewFipper==null){
                    break;
                }
                if(BlueToothMainActivity.BlueState){

                    try {
                        byte[] buffer = new byte[1024];
                        int is;

                        if(socketallViewFipper.isConnected()) {
                            is = socketallViewFipper.getInputStream().read(buffer);
                            if (is!=-1) {
                                is=is;
                                if(againMainConnect)
                                {
                                    againMainConnect=false;
                                    hmessage.sendEmptyMessage(0);
                                    if (isBtConDeviceByMac(BlueToothMainActivity.BlueMac)) {
                                        //PrintSend();
                                    }
                                    else
                                    {
                                        BlueToothMainActivity.BlueState = false;
                                        BlueToothMainActivity.BlueName = "";
                                        BlueToothMainActivity.BlueMac = "";
                                        Toast.makeText(ViewFlipperOnTouchActivity.this, getString(R.string.flipper_connectdevice)+"", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;

                                //is = is;
                            }
                            else
                            {
                                break;
                            }
                        }
                        else
                        {
                            break;
                        }
                    } catch (IOException e) {
//                        try {
                            if(!printEnable)
                            {
                                printEnable=true;
                            }
                            printEnableFlag=false;
//                        try {
//                            socketallViewFipper.close();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }

                            socketallViewFipper=null;
                            socketallViewFipper=BlueToothMainActivity.socketall;
                            try {
                                socketallViewFipper.connect();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }

//                        if(socketallViewFipper.isConnected())
//                        {
//                            int a=0;
//                            a=a;
//                        }
                        e.printStackTrace();
                    }
////                    connectBlueTask.cancel(true);
//                    new readThread().start();
////                    new ReadTask(readCallBack,socket).execute();
//                    Message msg = new Message();
//                    msg.what=0;
//                    msg.obj = "启动蓝牙监听线程";
//                    handler.sendMessage(msg);
                }
            }
        }
    }




    Handler hmessage = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0 && msg.what != 1024) {
                dialog(String.valueOf(msg.what));
                //Toast.makeText(DialogActivity.this, "333", Toast.LENGTH_LONG).show();
            }
            if (msg.what == 1024) {
                //dialog(getString(R.string.bluetoothmain_connecting));
                //Toast.makeText(DialogActivity.this, "333", Toast.LENGTH_LONG).show();
            }
            else if(msg.what==0)
            {
                progressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    };
    private void dialog(String SX)
    {

        //progressDialog.setMessage(SX);

        progressDialog.setMessage(getString(R.string.bluetoothmain_printing)+" "+SX);//getString(R.string.file_wait)+""   +" "+SX
        progressDialog.show();
    }
    public  Bitmap ChangeImagePDF_ESC(Bitmap img,int PrintCopies,int iCut,int addressFile)
    {
        boolean PrintHalftoneNow=PrintHalftone;
        String PrintHalftoneSNow=PrintHalftoneS;
        if(PrintHalftoneNow)
        {
            if(addressFile==1)//文档
            {
                //灰度打印
            }else//图片
            {
                img=convertGreyImgByFloyd(img);//误差扩散
            }
        }
        else
        {


            if(PrintHalftoneSNow.equals(PrintHalftone_String[0]))
            {
                img = DitherToBitmap(img);//抖动
            }
            else if(PrintHalftoneSNow.equals(PrintHalftone_String[1]))
            {
                img=convertGreyImgByFloyd(img);//误差扩散
            }
            else
            {
                //灰度打印
            }

        }


//        if(addressFile==1)//if(PrintHalftoneNow)//
//        {
//
//
//            if(PrintHalftoneNow)
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//        }
//        else
//        {
//            if(PrintHalftoneNow)
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//            else {
//                img = DitherToBitmap(img);//抖动
//            }
//            //img=convertGreyImgByFloyd(img);
//        }
        if(doc_Flag)
            img=DocGryBitmap(img,iCut,0);







        int width = img.getWidth();
        //获取位图的宽
        if(width>832)
            width=832;
        int height = img.getHeight();
        if(height>1080)//获取位图的高
            height=1080;
        int PrintSpeedNow=PrintSpeed;
        int PrintDensityNow=PrintDensity;
        int PrintContrastNow=PrintContrast;
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
        char  StartWords=' ';
//        StartWords='<';
//        Gray_Arraylist.add((byte)StartWords);
        //命令格式============================
        //! 0 200 200 240 1
        //PW 320
        //CG 40 240 0 0
        //=====================================

        //! 0 200 200 1000 1
        //21 20 30 20 32 30 30 20 32 30 30 20 31 30 30 30 20 31 20 0A
        StartInt=0x1d;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x0e;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x1c;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x60;
        Gray_Arraylist.add((byte)StartInt);

        StartInt=0x4D;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x53;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=PrintSpeedNow+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);

//        //打印浓度
//        bufImage[7]=0x1c
//        bufImage[8]=0x60
//        bufImage[9]=0x7E
//        bufImage[10]=0x7E
//        bufImage[11]=blackSet

        StartInt=0x1c;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x60;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x7E;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x7E;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=PrintDensityNow+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);

//        bufImage[12]=0x1d;
//        bufImage[13]=0x76;
//        bufImage[14]=0x30;
//        bufImage[15]=0x00;
//        bufImage[16]=setXL;//0x30
//        bufImage[17]=setXH;//0x00
//        bufImage[18]=setYL;//0x80
//        bufImage[19]=setYH;//0x01
//        var canvasHeight=that.data.paperSizeHeight*8
//        var setXH=parseInt(that.data.paperSizeWidth/256)
//        var setXL=parseInt(that.data.paperSizeWidth%256)
//        var setYH=parseInt(canvasHeight/256)
//        var setYL=parseInt(canvasHeight%256)
        StartInt=0x1d;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x76;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x30;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x00;
        Gray_Arraylist.add((byte)StartInt);
        int widthH=width/8/256;
        int widthL=width/8%256;
        int heightH=height/256;
        int heightL=height%256;
        StartInt=widthL+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);
        StartInt=widthH+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);
        StartInt=heightL+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);
        StartInt=heightH+0;//PrintDensityNow
        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);

        //PR 0
        //FORM
        //PRINT
        int k=0;
        int Send_i=0;
        int mathFlag=0;
        for(int i = 0; i < height; i++)
        {
//            StartWords = '#';
//            Gray_Arraylist.add((byte) StartWords);
//            StartWords = '<';
//            Gray_Arraylist.add((byte) StartWords);
            k=0;
            Send_i=0;
            for (int j = 0; j <width; j++)
            {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);


//==================================
                if(grey>PrintContrastNow)
                {
                    //bufImage[j]=0x00;
                    mathFlag=0;

                }
                else
                {
                    //bufImage[j]=0x01;
                    mathFlag=1;
                }
                k++;
                if(k==1)
                {
                    Send_i=0;
                    Send_i=Send_i+128*mathFlag;//mathFlag|0x80
                }
                else if(k==2)
                {
                    Send_i=Send_i+64*mathFlag;//mathFlag|0x40
                }
                else if(k==3)
                {
                    Send_i=Send_i+32*mathFlag;//mathFlag|0x20
                }
                else if(k==4)
                {
                    Send_i=Send_i+16*mathFlag;//mathFlag|0x10
                }
                else if(k==5)
                {
                    Send_i=Send_i+8*mathFlag;//mathFlag|0x08
                }
                else if(k==6)
                {
                    Send_i=Send_i+4*mathFlag;//mathFlag|0x04
                }
                else if(k==7)
                {
                    Send_i=Send_i+2*mathFlag;//mathFlag|0x02
                }
                else if(k==8)
                {
                    Send_i=Send_i+1*mathFlag;//mathFlag|0x01
                    Gray_Arraylist.add((byte)Send_i);

                    Send_i=0;
                    k=0;
                }
// =================================





                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
            int aBc=0;

        }

//        PR 0
//        FORM
//        PRINT
        //50 52 20 30 0A 46 4F 52 4D 0A 50 52 49 4E 54 0A
        StartInt=0x1c;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x5e;
        Gray_Arraylist.add((byte)StartInt);

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        //result=result;
        try {
            if (socket1 != null)
                Log.e(TAG1, "null is now");
            else {
                byte[] sss=new byte[Gray_Arraylist.size()];
                Gray_Send=new Byte[Gray_Arraylist.size()];
                Gray_Arraylist.toArray(Gray_Send);
                for(int xx=0;xx<Gray_Send.length;xx++){
                    sss[xx]=Gray_Send[xx];
                }

                byte[] sssy=new byte[1];
                sssy[0]=0x01;
                //sssy[1]=0x02;

                for(int j=0;j<PrintCopies;j++)
                {
                    OutputStream os = BlueToothMainActivity.socketall.getOutputStream();
                    os.write(sss);
                    //os.close();
                }


            }
        }
        catch (IOException e)
        {
            Log.e(TAG1,"s");
        }
        return result;
    }
    //===========================================
}
