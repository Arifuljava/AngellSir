package com.grozziie.grozziie_pdf;;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.ViewGroup;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;

import com.google.zxing.common.BitMatrix;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.grozziie.grozziie_pdf.Adapter.IconLinearAdapter;
import com.grozziie.grozziie_pdf.Adapter.LoadIconAdapter;
import com.grozziie.grozziie_pdf.BlueTooth.BlueToothMainActivity;
import com.grozziie.grozziie_pdf.SqliteHistorySave.SQLiteHelper;
import com.skateboard.zxinglib.CaptureActivity;


//import com.google.zxing.integration.android.IntentIntegrator.getCaptureActivity;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

//public class BarcodeActivity extends AppCompatActivity implements MarketFragment.CallBackValue{//回调接口
    public class BarcodeActivity extends AppCompatActivity{
    private BluetoothSocket socket1 = null;
    private double barcodeHeigthSet=0;
    private int barcodeWidthSet=0;
    private ScrollViewTouchView scrollViewBarcode;
    private Button qrMake;
    private Bitmap qrImage;
    private ImageView rotateButton;
    private ImageView maxButton;
    private ImageView minButton;
    private ImageView removeButton;
    private Button test;
    private RecyclerView BarcodeRecycle;
    private PopupWindow popupWindow;
    private RelativeLayout texteditlayout;
    private LinearLayout LinearLayoutBarcode;
    private RecyclerView EmojiRecycle;
    private RecyclerView LoadImageRecycle;
    private RecyclerView loadIconRecycle;
    private RecyclerView IconTitleLayout;
    private SeekBar textSizeseekBar;
    private int mCursorPosition = -1;
    private static final int CURSOR_COUNT = 20; //每次查询数据库的数量
    private ArrayList<String> mPathList;
    private ArrayList<String> barcodeInformationList;
    private ArrayList<String> viewInformationList;
    private ArrayList<String> getAssetsPathList;
    private ArrayList<File>mPathNameList;
    private ArrayList<String> iconListTitle;
    private List<String> mImgs;
    private List<String> mImgsX;
    private boolean mIsScanning = false; // 正在扫描
    private boolean mIsFinishSearchImage = false; // 是否扫描完了所有图片
    public static final int WHAT_REFRESH_PATH_LIST = 1;
    public static boolean sonViewFlag=false;
    private int containerHeight;
    private int containerWidth;
    private int barcodeSizeWidth=12;
    private int barcodeSizeHeight=30;
    private int barcodeSizeCopies=1;
    private int barcodeMaxSizeWidth=104;
    private int barcodeMaxSizeHeight=150;
    private int PrintSpeed = 2;
    private int PrintDensity = 9;
    private int PrintContrast = 180;
    private int rotateAngle = 0;
    public static int getById = 0;
    private int id = 1;
    private int oldId=0;
    private int idFlag=0;
    public static int getIdFlag = 0;
    public static int touchFlag = 0;
    public static int scrollview_TouchEnd=780;
    float lastX, lastY;
    private Paint paint;
    private Canvas canvas;
    private float startX;
    private float startY;
    private Bitmap bitcopy;
    private int DrawWidth;
    private int DrawHeight;
    private double PhoneHeightH;
    private double PhoneHeight;
    private int PhoneWidth;
    private int PhoneHeightS;
    private Bitmap bitmap;
    public static int keybordFlag = 0;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layoutE1;
    private LinearLayout layoutE2;
    private LinearLayout layoutE3;
    private LinearLayout layoutE4;
    private ImageView imageE1;
    private ImageView imageE2;
    private ImageView imageE3;
    private ImageView imageE4;
    private ImageView QRimage1;
    private ImageView QRimage2;
    private Horizon scrollView;
    private HorizontalScrollView scrollViewE;
    private TextView textView;
    private ImageView backLastBarcode;
    private ImageView barcodeHistoryOther;
    private HomeFragment homeFragment;
    private Button barcodeSave;
    private Button barcodeSize;
    //private Button barcodePrintEsc;
    private Button barcodePrint;
    private int emojiType1 = 0x1F601;
    private int emojiType2 = 0x1f680;
    private int emojiType3 = 0x2122;
    private int emojiType4 = 0x1f300;
    protected final int PERMISSION_REQUEST = 42;
    private Handler mHandler = new Handler();
    private SQLiteDatabase dbN=null;
    private List barcodeInformationListNow;
    //private Handler nHandler = new Handler();
    private ImageView barcodeDevice;
    private ImageView barcodeDeviceState;
    public static  float textSize=70;
    public static  String testString="双击编辑";
    private String[] pickerString={"yyyy-MM-dd","yyyy-MM","MM-dd","yyyy/MM/dd","yyyy/MM","MM/dd","yyyy年MM月dd日","yyyy年MM月","MM月dd日"};
    private String[] pickerTimeString={"hh:mm","hh时mm分"};
    private int dateFlag;
    private int timeFlag;
    private boolean BlodFlag=false;
    private boolean ItalicFlag=false;
    private boolean UnderlineFlag=false;
    private Typeface BlodType=Typeface.SANS_SERIF;
    public static int FirstClick=0;
    public static int SecondClick=0;
    public static int CountClick=0;
    public static int selectScannerFlag=0;
    private LoadImageAdapter loadImageAdapter;
    private IconLinearAdapter iconLinearAdapter;
    private LoadIconAdapter loadIconAdapter;
    private LoadImageAdapter mAdapter;
    private HashSet<String> mDirPaths = new HashSet<String>();
    private ProgressDialog mProgressDialog;
    private Cursor mCursor;
    private File mImgDir;
    private int mPicsSize;
    private int imageFlag=0;
    private AssetManager mAssetManager;
    private int titlePos=0;
    private Bitmap backgroundBitmap=null;
    private int BlueToothName_Flag=0;
    private String Barcode_HengXiang="Horizontal";
    private String Barcode_ZongXiang="Vertical";
    private String BarcodeFangXiang="Horizontal";
    private String SaveDialogNumber="";
    private String SaveDialogName="";
    private String[] qrcodeData=new String[1000];

//    private Handler nHandler = new Handler()
//    {
//        public void handleMessage(android.os.Message msg)
//        {
//            mProgressDialog.dismiss();
//            mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
//            {
//                @Override
//                public boolean accept(File dir, String filename)
//                {
//                    if (filename.endsWith(".jpg")||filename.endsWith(".png"))
//                        return true;
//                    return false;
//                }
//            }));
//            /**
//             * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
//             */
//
//            loadImageAdapter.refreshPathList(mImgs, mImgs.size(),
//                    mImgDir.getAbsolutePath());
//
//            //LoadImageRecycle.setAdapter(mAdapter);
//        };
//    };
        private Handler nHandler = new Handler()
        {
        public void handleMessage(android.os.Message msg)
        {
            mProgressDialog.dismiss();
            String path;
            if(msg.what==WHAT_REFRESH_PATH_LIST) {
                    while (mCursor.moveToNext()) {
                        //i++;
                        // 获取图片的路径
                        path = mCursor.getString(mCursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));

                        if (new File(path).exists()) {
                            mPathList.add(path);
                        }
                    }
//                    mCursor=null;
                    mCursor.close();
                    loadImageAdapter.refreshPathList(mPathList, mPathList.size());
            }
            else if(msg.what==2)
            {
                Bundle bundle=msg.getData();
                int iconTitleColor=bundle.getInt("iconTitleColor");

                iconLinearAdapter.RefreshIconTitleList(iconListTitle, iconListTitle.size(),iconTitleColor);

            }
            else if(msg.what==3)
            {
                loadIconAdapter.refreshGetAssetsList(getAssetsPathList, getAssetsPathList.size(),mAssetManager);
            }
            else if(msg.what==4)
            {
                Toast.makeText(BarcodeActivity.this, "save successful", Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==5)
            {
                if(backgroundBitmap!=null)
                    texteditlayout.setBackground(new BitmapDrawable(getResources(),backgroundBitmap));
                Bundle bundle=msg.getData();
                String barcodeNumber=bundle.getString("BarcodeNumber");
                //SaveDialogName=bundle.getString("BarcodeName");
                SaveDialogNumber=barcodeNumber;
                Cursor cursor = dbN.rawQuery("select _barcodenumbers,_viewflag,_viewinformation,_viewtext,_viewimage from viewinformation where _barcodenumbers='"+barcodeNumber+"';", null);
                Gson gson = new Gson();
                id=1;
                while(cursor.moveToNext()){
                    //遍历出表名
//                    //viewInformationList.clear();
                    barcodeInformationListNow=new ArrayList();
                    int viewFlag=Integer.parseInt(cursor.getString(1));

                    if(viewFlag==1)
                    {
                        //============================================


                        String barcodeinformation=cursor.getString(2);
                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList informationArrayList=new ArrayList<String>();
                        informationArrayList = gson.fromJson((barcodeinformation), type);
                        int viewLeft=Integer.parseInt(informationArrayList.get(0).toString());
                        int viewTop=Integer.parseInt(informationArrayList.get(1).toString());
                        int viewRight=Integer.parseInt(informationArrayList.get(2).toString());
                        int viewBottom=Integer.parseInt(informationArrayList.get(3).toString());
                        int viewHeight=Integer.parseInt(informationArrayList.get(4).toString());
                        int viewWidth=Integer.parseInt(informationArrayList.get(5).toString());

                        //============================================
                        Bitmap viewBitmap=null;
                        byte[] photoByteB;
                        //Bitmap bitmapB=null;
                        if(cursor.getBlob(4)!=null) {
                            photoByteB = cursor.getBlob(4);
                            viewBitmap = BitmapFactory.decodeByteArray(photoByteB, 0, photoByteB.length);
                        }
                        final DragViewX imageView = new DragViewX(getApplicationContext());

                        getById = id;
                        getIdFlag = 1;

                        imageView.setId(id++);



                        imageView.setX(viewLeft);
                        imageView.setY(viewTop);



                        //imageView.setImageBitmap(qrImage);
                        imageView.setImageBitmap(viewBitmap);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                        int widthX=drawView.SaveBitmap().getWidth();
//                        int heightX=drawView.SaveBitmap().getHeight();
//                        int width=200;
//                        double heightPercent=heightX/widthX;
//                        double height=heightPercent*width;
                        //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);

                        imageView.setClickable(true);
                        //moveImage(imageView, 2);

                        texteditlayout.addView(imageView,viewWidth,viewHeight);






                    }else
                    {



                        String barcodeinformation=cursor.getString(2);
                        String viewText=cursor.getString(3);

                        Type type = new TypeToken<ArrayList<String>>() {}.getType();
                        ArrayList informationArrayList=new ArrayList<String>();
                        informationArrayList = gson.fromJson((barcodeinformation), type);
                        //String barcodeSize=informationArrayList.get(2)+"*"+informationArrayList.get(3)+"mm";
                        int viewLeft=Integer.parseInt(informationArrayList.get(0).toString());
                        int viewTop=Integer.parseInt(informationArrayList.get(1).toString());
                        int viewRight=Integer.parseInt(informationArrayList.get(2).toString());
                        int viewBottom=Integer.parseInt(informationArrayList.get(3).toString());
                        int viewHeight=Integer.parseInt(informationArrayList.get(4).toString());
                        int viewWidth=Integer.parseInt(informationArrayList.get(5).toString());
                        float viewSize=Float.parseFloat(informationArrayList.get(6).toString());
                        final DragViewT textView = new DragViewT(getApplicationContext());
                         viewSize=px2sp(getApplicationContext(),Float.parseFloat(informationArrayList.get(6).toString()));
                        getById = id;
                        getIdFlag = 1;
                        textView.setTextColor(Color.BLACK);
                        textView.setId(id++);
                        textView.setText(viewText);
                        //textView.setBackgroundColor(10);

                        textView.setTextSize(viewSize);
                        textView.setStateListAnimator(null);
                        textView.bringToFront();

                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DoubleClickView(textView);
                            }
                        });
//                        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
//                                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

//                        ViewGroup.LayoutParams params2=getLayoutParams();
//
//
//                        params1.height=nowGetHeight;
//                        params1.width=nowGetWidth;
//
//
//
//                        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) getLayoutParams();
//                        ViewGroup.MarginLayoutParams params1= (ViewGroup.MarginLayoutParams) getLayoutParams();
//                        ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                        params.leftMargin=viewLeft;
//
//
//                        params.topMargin=viewTop;
//                        params.bottomMargin=viewBottom;
//                        params.rightMargin=viewRight;
//                        //moveImage(textView, 1);
                        textView.setX(viewLeft);
                        textView.setY(viewTop);
                        texteditlayout.addView(textView);

                    }

//                    String barcodenumber = cursor.getString(0);
//                    String barcodename=cursor.getString(1);
//                    String barcodetime=cursor.getString(2);
//                    String barcodeinformation=cursor.getString(3);
//                    //cursor.getInt(cursor.getColumnIndex("_id"));
////                Type type = new TypeToken<ArrayList<String>>() {}.getType();
////
////                viewInformationList = gson.fromJson(barcodeinformation, type);
//
//                    byte[] photoByte=cursor.getBlob(4);
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
//
//                    byte[] photoByteB;
//                    //Bitmap bitmapB=null;
//                    if(cursor.getBlob(5)!=null) {
//                        photoByteB = cursor.getBlob(5);
//                        backgroundBitmap = BitmapFactory.decodeByteArray(photoByteB, 0, photoByteB.length);
//                    }

                }


            }
//            for(int i=msg.what;i<msg.what+1;i++)
//            {
//                mImgs =  Arrays.asList(mPathNameList.get(i).list(new FilenameFilter() {
//                    @Override
//                    public boolean accept(File dir, String filename) {
//                        if (filename.endsWith(".jpg") || filename.endsWith(".png")) {
//                            return true;
//                        }
//                        return false;
//                    }
//                }));
//                /**
//                 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
//                 */
//                mImgsX.addAll(mImgs);
//
//                loadImageAdapter.refreshPathList(mImgsX, mImgsX.size());
//            }
            /**
             * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
             */

            //loadImageAdapter.refreshPathList(mPathList, mPathList.size());

            //LoadImageRecycle.setAdapter(mAdapter);
        };
    };

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
////to do 逻辑…
//
//                break;
//            case MotionEvent.ACTION_UP: getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    //回调Fragment

//    @Override
//    public void SendMessageValue(List strValue) {
//        // TODO Auto-generated method stub
//        List xxx=strValue;
//
//    }
//=========================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        setContentView(R.layout.activity_barcode);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bacgroundColor));

//        nHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case WHAT_REFRESH_PATH_LIST:
//
//                        if (loadImageAdapter == null) {
//
//                        } else {
//                            loadImageAdapter.refreshPathList(mPathList,mPathList.size());
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
        //=========
        scrollViewBarcode=findViewById(R.id.scrollViewBarcode);
        BarcodeRecycle = findViewById(R.id.barcodeRecycler);
        texteditlayout = findViewById(R.id.textEditLayout);
        //LinearLayoutBarcode=findViewById(R.id.LinearLayoutBarcode);
        texteditlayout.setDrawingCacheEnabled(true);
        texteditlayout.buildDrawingCache();
        barcodePrint = findViewById(R.id.barcodePrint);
        barcodeSave = findViewById(R.id.barcodeSave);
        barcodeSize = findViewById(R.id.barcodeSize);
        //barcodePrintEsc=findViewById(R.id.barcodePrintEsc);
        maxButton = findViewById(R.id.maxButton);
        minButton = findViewById(R.id.minButton);
        removeButton = findViewById(R.id.trashButton);
        rotateButton = findViewById(R.id.rotateButton);
        backLastBarcode = findViewById(R.id.backLastBarcode);
        barcodeHistoryOther=findViewById(R.id.barcodeHistoryOther);
        barcodeDevice = findViewById(R.id.barcodeDevice);
        barcodeDeviceState = findViewById(R.id.barcodeDeviceState);
        textSizeseekBar=findViewById(R.id.textSizeseekBar);
        if (BlueToothMainActivity.socketall == null) {
            barcodeDeviceState.setVisibility(View.VISIBLE);
        } else {
            barcodeDeviceState.setVisibility(View.GONE);
        }
        getState();
        GetPhoneSize(barcodeSizeWidth,barcodeSizeHeight);
        ConnectDB();
        Bundle barcodeFragment=getIntent().getExtras();

//获取Bundle的信息
        if(barcodeFragment!=null) {
            int intentFlag = barcodeFragment.getInt("IntentFlag");
            if (intentFlag == 1) {
                barcodeSizeWidth = barcodeFragment.getInt("BarcodeWidth");
                barcodeSizeHeight = barcodeFragment.getInt("BarcodeHeight");
                String barcodeNumber = barcodeFragment.getString("BarcodeNumber");
                SaveDialogName=barcodeFragment.getString("BarcodeName");
                saveState();
                GetPhoneSize(barcodeSizeWidth, barcodeSizeHeight);
                mProgressDialog = ProgressDialog.show(BarcodeActivity.this, null, "loading...");
                FragmentGetBarcodeView(barcodeNumber);

            }
        }
        getMaxSize();


//        //在程序中加入Fragment
//        FragmentManager manager = getSupportFragmentManager();
//        //开启一个Fragment事务
//        FragmentTransaction transaction = manager.beginTransaction();
//        MarketFragment myFragment = new MarketFragment();
//        transaction.add(R.id.goodsDisplay, myFragment ,"000");
//        transaction.commit();
//        barcodePrintEsc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(backgroundBitmap==null)
//                    ;
//                else
//                    texteditlayout.setBackground(null);
//                texteditlayout.setBackgroundColor(Color.WHITE);
//                texteditlayout.setDrawingCacheEnabled(true);
//                texteditlayout.buildDrawingCache();
//
//
//
//                //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）
//
//                //获取缓存
//
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        final  Bitmap bmp=LayoutToBitmap();
//                        texteditlayout.setDrawingCacheEnabled(false);
//                        texteditlayout.buildDrawingCache(false);
//                        if(backgroundBitmap!=null)
//                            texteditlayout.setBackground(new BitmapDrawable(getResources(),backgroundBitmap));
//
//                        if (isBtConDeviceByMac(BlueToothMainActivity.BlueMac)) {
//
//                        } else {
//                            BlueToothMainActivity.BlueState = false;
//                            BlueToothMainActivity.BlueName = "";
//                            BlueToothMainActivity.BlueMac = "";
//                            Toast.makeText(BarcodeActivity.this, "Please connect the device...", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (bmp == null) {
//                            Toast.makeText(BarcodeActivity.this, "Please select a file to print", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                        if (BlueToothMainActivity.socketall != null && BlueToothMainActivity.BlueState && bmp != null) {
////                            final String PageStart = PageInputStart.getText().toString().replaceAll(" ", "");
////                            final String PageEnd = PageInputEnd.getText().toString().replace(" ", "");
////                            final String PageCopies = PageInputCopies.getText().toString().replace(" ", "");
////                            final String PageWidth = PageInputWidth.getText().toString().replace(" ", "");
////                            final String PageHeight = PageInputHeight.getText().toString().replace(" ", "");
//
//
//
//
//                            Thread thread = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //GetPDFImage(PageStart,PageEnd);
//                                    Bitmap bitmap=zoomBitmap(bmp,barcodeSizeWidth*8,barcodeSizeHeight*8,0);
//                                    ChangeImagePDF_ESC(bitmap,1,1,1);
////                                                if (TextHouZhui.equals("pdf"))
////                                                    GetPDFImage(PageStart, PageEnd);
////                                                else {
////                                                    GetBMPImage(PageStart, PageEnd, bitmapX);
////                                                }
////
//                                }
//                            });
//                            thread.start();
//
//
//
//
//
//                        } else
//                            Toast.makeText(BarcodeActivity.this, "Please connect the device", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                }, 100);
//            }
//        });

        barcodeSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.popupwindow_barcodesize, null);
                final TextView button = view.findViewById(R.id.makeBarcodeSizeButton);
                final EditText editTextWidth = view.findViewById(R.id.BarcodePageInputWidth);
                final EditText editTextHeight = view.findViewById(R.id.BarcodePageInputHeight);
                final EditText editTextCopies=view.findViewById(R.id.BarcodePageCopies);
                final TextView DirectionText=view.findViewById(R.id.DirectionText);
                editTextWidth.setText(String.valueOf(barcodeSizeWidth));
                editTextHeight.setText(String.valueOf(barcodeSizeHeight));
                DirectionText.setText(BarcodeFangXiang);
                DirectionText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DirectionText.getText().equals(Barcode_HengXiang))
                        {
                            DirectionText.setText(Barcode_ZongXiang);
                        }
                        else {
                            DirectionText.setText(Barcode_HengXiang);
                        }
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(editTextCopies.getText().toString().length()!=0&& Integer.parseInt(editTextCopies.getText().toString())>0)
                        {
                            barcodeSizeCopies=Integer.parseInt(editTextCopies.getText().toString());
                        }
                        else
                        {
                            barcodeSizeCopies=1;
                        }
                        if(editTextHeight.getText().toString().length()!=0 && editTextWidth.getText().toString().length()!=0 && Integer.parseInt(editTextWidth.getText().toString())>0 && Integer.parseInt(editTextHeight.getText().toString())>0)
                        {
                            if(barcodeMaxSizeWidth>Integer.parseInt(editTextWidth.getText().toString())&& barcodeMaxSizeHeight>Integer.parseInt(editTextHeight.getText().toString()))
                            {
                                barcodeSizeWidth=Integer.parseInt(editTextWidth.getText().toString());
                                barcodeSizeHeight=Integer.parseInt(editTextHeight.getText().toString());
                                BarcodeFangXiang=DirectionText.getText().toString();
                                SetTextLayoutSize(PhoneWidth,barcodeSizeWidth,barcodeSizeHeight);
                                saveState();
                                popupWindow.dismiss();
                            }
                            else{
                                Toast.makeText(BarcodeActivity.this, "纸张尺寸太大，请和机器型号确认", Toast.LENGTH_SHORT).show();
                            }

                        }



                    }
                });
                makePopupWiondo(view);
            }
        });
        texteditlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


//                if(oldId==0)
//                {
//
//
//                }
//                else
//                {
//
//                        if(getIdFlag==1)
//                        {
//                            DragViewT textView=findViewById(oldId);
//                            textView.setBackgroundResource(0);
//
//                        }
//                        else if(getIdFlag==2)
//                        {
//                            DragViewX imageView1=findViewById(oldId);
//                            imageView1.setBackgroundResource(0);
//                        }
//
//                }
                Log.d("texteditlayout",getIdFlag+"   "+oldId);
                BarcodeActivity.sonViewFlag=false;

                return false;
            }
        });
//        scrollViewBarcode.setOnTouchListener (new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent motionEvent) {
//
//                    return true ;
//
//            }
//        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getById==0)
                {
                    getIdFlag=0;
                    Toast.makeText(BarcodeActivity.this, "请选择对象", Toast.LENGTH_SHORT).show();
                }
                else {


                    int indexID=getById;
                    if(getIdFlag==1)
                    {
                        DragViewT textView=findViewById(getById);
                        texteditlayout.removeView(textView);
                    }
                    else if(getIdFlag==2)
                    {
                        DragViewX imageView=findViewById(getById);
                        texteditlayout.removeView(imageView);

                    }
                    idFlag=0;
                    getIdFlag=0;

                }
            }
        });
        barcodeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inent = new Intent(BarcodeActivity.this, PDFSetActivity.class);//BlueToothMainActivity
                startActivity(inent);
                //finish();
            }
        });
        barcodeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameSave="";
                if(SaveDialogName=="")
                {
                    nameSave="Unnamed";
                }
                else
                {
                    nameSave=SaveDialogName;

                }
                final SaveDialog saveDialog=new SaveDialog(BarcodeActivity.this,nameSave);

                saveDialog.setCancel("Cancel", new SaveDialog.IOnCancelListener() {
                    @Override
                    public void onCancel(SaveDialog dialog) {

                    }
                }).setConfirm("Save", new SaveDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(SaveDialog dialog) {

                        EditText editText=(EditText) saveDialog.findViewById(R.id.saveTextName);
                        final   String  saveDialogbarcodeName=editText.getText().toString();
                        TextView textView=saveDialog.findViewById(R.id.saveTextID);
                        final String saveDialogTime=textView.getText().toString();
                        //=====================遍历layoutview==========
                        texteditlayout.setDrawingCacheEnabled(true);
                        texteditlayout.buildDrawingCache();


                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                final  Bitmap bmp=LayoutToBitmap();
                                texteditlayout.setDrawingCacheEnabled(false);
                                texteditlayout.buildDrawingCache(false);
                                mProgressDialog = ProgressDialog.show(BarcodeActivity.this, null, "saving...");
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String  barcodeNumber="";
//                                        if(SaveDialogNumber=="")
//                                        {
//                                            long timecurrentTimeMillis = System.currentTimeMillis();
//                                            barcodeNumber=String.valueOf(timecurrentTimeMillis);
//                                        }
//                                        else
//                                        {
//                                            barcodeNumber=SaveDialogNumber;
//                                        }
                                        long timecurrentTimeMillis = System.currentTimeMillis();
                                        barcodeNumber=String.valueOf(timecurrentTimeMillis);
                                        SearchView(bmp,saveDialogbarcodeName,barcodeNumber,saveDialogTime);
                                    }
                                });
                                thread.start();


                            }
                        }, 100);

                    }
                }).show();


























//                //======================保存图片=============================
//                final String FileName = System.currentTimeMillis() + ".jpg";
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        final Bitmap bmp = texteditlayout.getDrawingCache();
//                        String picturePahtp = "/storage/emulated/0/DCIM/Doodle/";
//                        savePicture(bmp, FileName, picturePahtp);
//                        texteditlayout.destroyDrawingCache();
//                    }
//                }, 100);
////=====================================================
//                //============================获取assets里的图片==================================
//                try {
//
//                    mAssetManager = getAssets();
//                    String[] mAssetsImageList;
//                    mAssetsImageList=mAssetManager.list("images/0");
//                    for (int i = 0; i < mAssetsImageList.length; i++) {
//                        String imagePath = "images/0" + "/" + mAssetsImageList[i];
//                        Bitmap bookCover = loadImage(imagePath);
//                        Bitmap bmp=bookCover;
//
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }





            }
        });
        barcodePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    String String_S = "ABCDEFG";
//                    Intent intent = getIntent();//(intent.getStringExtra("sss"));
//                    String sss = intent.getStringExtra("s");
//                    byte[] msgBuffer1=new byte[]{0x43,0x4c,0x53,0x0a,0x42,0x41,0x52,0x43,
//                                                    0x4f,0x44,0x45,0x20,0x31,0x32,0x38,0x20,
//                                                    0x33,0x20,0x33,0x20,0x38,0x38,0x20,0x33,
//                                                    0x34,0x20,0x31,0x30,0x35,0x20,0x39,0x38,
//                                                    0x36,0x34,0x35,0x36,0x32,0x31,0x34,0x33,
//                                                    0x39,0x33,0x35,0x0a,0x50,0x52,0x49,0x4e,0x54,0x0a};
//                    int[] msgBuffer2=new int[]{0x21,
//                            0x20,0x30,0x20,0x32,0x30,0x30,0x20,0x32,0x30,0x30,
//                            0x20,0x31,0x30,0x30,0x30,0x20,0x31,0x0A,0x50,0x41,
//                            0x47,0x45,0x2D,0x57,0x49,0x44,0x54,0x48,0x20,0x36,
//                            0x30,0x38,0x0A,0x47,0x41,0x50,0x2D,0x53,0x45,0x4E,
//                            0x53,0x45,0x0A,0x42,0x4F,0x58,0x20,0x30,0x20,0x39,
//                            0x36,0x20,0x35,0x36,0x30,0x20,0x38,0x39,0x36,0x20,
//                            0x32,0x0A,0x4C,0x49,0x4E,0x45,0x20,0x30,0x20,0x32,
//                            0x32,0x34,0x20,0x35,0x36,0x30,0x20,0x32,0x32,0x34,
//                            0x20,0x32,0x0A,0x4C,0x49,0x4E,0x45,0x20,0x30,0x20,
//                            0x32,0x38,0x38,0x20,0x35,0x36,0x30,0x20,0x32,0x38,
//                            0x38,0x20,0x32,0x0A,0x4C,0x49,0x4E,0x45,0x20,0x30,
//                            0x20,0x33,0x33,0x36,0x20,0x34,0x34,0x38,0x20,0x33,
//                            0x33,0x36,0x20,0x32,0x0A,0x4C,0x49,0x4E,0x45,0x20,
//                            0x30,0x20,0x35,0x30,0x34,0x20,0x35,0x36,0x30,0x20,
//                            0x35,0x30,0x34,0x20,0x32,0x0A,0x4C,0x49,0x4E,0x45,
//                            0x20,0x30,0x20,0x35,0x35,0x32,0x20,0x35,0x36,0x30,
//                            0x20,0x35,0x35,0x32,0x20,0x32,0x0A,0x4C,0x49,0x4E,
//                            0x45,0x20,0x30,0x20,0x36,0x33,0x32,0x20,0x35,0x36,
//                            0x30,0x20,0x36,0x33,0x32,0x20,0x32,0x0A,0x4C,0x49,
//                            0x4E,0x45,0x20,0x34,0x34,0x38,0x20,0x32,0x38,0x38,
//                            0x20,0x34,0x34,0x38,0x20,0x35,0x35,0x32,0x20,0x32,
//                            0x0A,0x4C,0x49,0x4E,0x45,0x20,0x34,0x34,0x38,0x20,
//                            0x34,0x30,0x30,0x20,0x35,0x36,0x30,0x20,0x34,0x30,
//                            0x30,0x20,0x32,0x0A,0x4C,0x49,0x4E,0x45,0x20,0x31,
//                            0x36,0x38,0x20,0x35,0x35,0x32,0x20,0x31,0x36,0x38,
//                            0x20,0x36,0x33,0x32,0x20,0x32,0x0A,0x54,0x45,0x58,
//                            0x54,0x20,0x35,0x35,0x20,0x30,0x20,0x31,0x36,0x20,
//                            0x37,0x32,0x20,0x32,0x30,0x32,0x31,0x2D,0x30,0x33,
//                            0x2D,0x31,0x35,0x20,0x20,0x31,0x37,0x3A,0x30,0x30,
//                            0x3A,0x35,0x37,0x20,0x20,0xB5,0xDA,0x31,0x2F,0x31,
//                            0x0A,0x53,0x45,0x54,0x42,0x4F,0x4C,0x44,0x20,0x31,
//                            0x0A,0x54,0x45,0x58,0x54,0x20,0x34,0x20,0x30,0x20,
//                            0x34,0x38,0x38,0x20,0x35,0x20,0xB1,0xEA,0x0A,0x53,
//                            0x45,0x54,0x42,0x4F,0x4C,0x44,0x20,0x30,0x0A,0x53,
//                            0x45,0x54,0x42,0x4F,0x4C,0x44,0x20,0x31,0x0A,0x54,
//                            0x45,0x58,0x54,0x20,0x34,0x20,0x30,0x20,0x34,0x38,
//                            0x38,0x20,0x34,0x32,0x20,0xBF,0xEC,0x0A,0x53,0x45,
//                            0x54,0x42,0x4F,0x4C,0x44,0x20,0x30,0x0A,0x53,0x45,
//                            0x54,0x42,0x4F,0x4C,0x44,0x20,0x31,0x0A,0x54,0x45,
//                            0x58,0x54,0x20,0x34,0x20,0x30,0x20,0x35,0x32,0x30,
//                            0x20,0x35,0x20,0xD7,0xBC,0x0A,0x53,0x45,0x54,0x42,
//                            0x4F,0x4C,0x44,0x20,0x30,0x0A,0x53,0x45,0x54,0x42,
//                            0x4F,0x4C,0x44,0x20,0x31,0x0A,0x54,0x45,0x58,0x54,
//                            0x20,0x34,0x20,0x30,0x20,0x35,0x32,0x30,0x20,0x34,
//                            0x32,0x20,0xB5,0xDD,0x0A,0x53,0x45,0x54,0x42,0x4F,
//                            0x4C,0x44,0x20,0x30,0x0A,0x42,0x41,0x52,0x43,0x4F,
//                            0x44,0x45,0x20,0x31,0x32,0x38,0x20,0x33,0x20,0x33,
//                            0x20,0x38,0x38,0x20,0x33,0x34,0x20,0x31,0x30,0x35,
//                            0x20,0x39,0x38,0x36,0x34,0x35,0x36,0x32,0x31,0x34,
//                            0x33,0x39,0x33,0x35,0x0A,0x54,0x45,0x58,0x54,0x20,
//                            0x32,0x20,0x30,0x20,0x31,0x33,0x30,0x20,0x31,0x39,
//                            0x39,0x20,0x39,0x20,0x38,0x20,0x36,0x20,0x34,0x20,
//                            0x35,0x20,0x36,0x20,0x32,0x20,0x31,0x20,0x34,0x20,
//                            0x33,0x20,0x39,0x20,0x33,0x20,0x35,0x0A,0x53,0x45,
//                            0x54,0x4D,0x41,0x47,0x20,0x32,0x20,0x32,0x0A,0x54,
//                            0x45,0x58,0x54,0x20,0x32,0x20,0x30,0x20,0x32,0x31,
//                            0x32,0x20,0x32,0x33,0x32,0x20,0x30,0x37,0x30,0x2D,
//                            0xBC,0xAA,0x4A,0x0A,0x53,0x45,0x54,0x4D,0x41,0x47,
//                            0x20,0x31,0x20,0x31,0x0A,0x54,0x45,0x58,0x54,0x20,
//                            0x34,0x20,0x30,0x20,0x31,0x36,0x20,0x32,0x39,0x36,
//                            0x20,0xBC,0xAF,0x0A,0x54,0x45,0x58,0x54,0x20,0x34,
//                            0x20,0x30,0x20,0x36,0x34,0x20,0x32,0x39,0x36,0x20,
//                            0x30,0x37,0x30,0x2D,0xBC,0xAA,0x4A,0x0A,0x54,0x45,
//                            0x58,0x54,0x20,0x34,0x20,0x30,0x20,0x31,0x36,0x20,
//                            0x33,0x35,0x32,0x20,0xCA,0xD5,0x0A,0x54,0x45,0x58,
//                            0x54,0x20,0x32,0x20,0x30,0x20,0x36,0x34,0x20,0x33,
//                            0x34,0x34,0x20,0xB3,0xC2,0x20,0x20,0x20,0x20,0x31,
//                            0x35,0x35,0x35,0x37,0x32,0x35,0x35,0x35,0x38,0x35,
//                            0x20,0x20,0x0A,0x54,0x45,0x58,0x54,0x20,0x32,0x20,
//                            0x30,0x20,0x36,0x34,0x20,0x33,0x38,0x34,0x20,0xBC,
//                            0xAA,0xC1,0xD6,0xCA,0xA1,0xCB,0xC9,0xD4,0xAD,0xCA,
//                            0xD0,0xC6,0xE4,0xCB,0xFC,0xC7,0xF8,0xB9,0xE3,0xB8,
//                            0xE6,0xB7,0xD1,0xB7,0xB5,0xBB,0xD8,0x0A,0x54,0x45,
//                            0x58,0x54,0x20,0x34,0x20,0x30,0x20,0x31,0x36,0x20,
//                            0x35,0x31,0x32,0x20,0xBC,0xC4,0x0A,0x54,0x45,0x58,
//                            0x54,0x20,0x35,0x35,0x20,0x30,0x20,0x36,0x34,0x20,
//                            0x35,0x31,0x32,0x20,0xD3,0xF6,0xBC,0xFB,0x20,0x20,
//                            0x31,0x35,0x35,0x35,0x37,0x33,0x38,0x38,0x38,0x30,
//                            0x36,0x20,0x20,0xD5,0xE3,0xBD,0xAD,0xCA,0xA1,0xBC,
//                            0xCE,0xD0,0xCB,0xCA,0xD0,0xBC,0xCE,0xC9,0xC6,0xCF,
//                            0xD8,0xD2,0xA6,0xD7,0xAF,0x0A,0x54,0x45,0x58,0x54,
//                            0x20,0x35,0x35,0x20,0x30,0x20,0x36,0x34,0x20,0x35,
//                            0x33,0x30,0x20,0xD5,0xF2,0xB1,0xA6,0xC8,0xBA,0xB6,
//                            0xAB,0xC2,0xB7,0x31,0x35,0x39,0x0A,0x42,0x41,0x52,
//                            0x43,0x4F,0x44,0x45,0x20,0x31,0x32,0x38,0x20,0x31,
//                            0x20,0x31,0x20,0x34,0x38,0x20,0x32,0x34,0x30,0x20,
//                            0x35,0x35,0x38,0x20,0x39,0x38,0x36,0x34,0x35,0x36,
//                            0x32,0x31,0x34,0x33,0x39,0x33,0x35,0x0A,0x54,0x45,
//                            0x58,0x54,0x20,0x35,0x35,0x20,0x30,0x20,0x32,0x36,
//                            0x34,0x20,0x36,0x31,0x30,0x20,0x39,0x20,0x38,0x20,
//                            0x36,0x20,0x34,0x20,0x35,0x20,0x36,0x20,0x32,0x20,
//                            0x31,0x20,0x34,0x20,0x33,0x20,0x39,0x20,0x33,0x20,
//                            0x35,0x0A,0x54,0x45,0x58,0x54,0x20,0x32,0x20,0x30,
//                            0x20,0x33,0x36,0x38,0x20,0x38,0x33,0x36,0x20,0xC6,
//                            0xB7,0xC0,0xE0,0x3A,0xB7,0xFE,0xCA,0xCE,0x0A,0x54,
//                            0x45,0x58,0x54,0x20,0x32,0x20,0x30,0x20,0x34,0x36,
//                            0x34,0x20,0x38,0x36,0x34,0x20,0xD2,0xD1,0xD1,0xE9,
//                            0xCA,0xD3,0x0A,0x46,0x4F,0x52,0x4D,0x0A,0x50,0x52,
//                            0x49,0x4E,0x54,0x0A,0x04,0x83,0x01,0x04,0xF0,0xAF,
//                            0x04,0x81,0x01,0x01,0x91,0x6C};
//
//
//
//                    byte[] msgBuffer=new byte[msgBuffer2.length];
//
//                    for(int xx=0;xx<msgBuffer2.length;xx++){
//                        msgBuffer[xx]=(byte)msgBuffer2[xx];
//                    }
//
//
//                    //MainActivity My =new MainActivity();
//                    //BluetoothConnect sss=new BluetoothConnect();
//
//                    // socket=sss.getSocket();
//                    // onEventMainTrend(Event);
//                    if (BlueToothMainActivity.socketall == null)
//                        //Log.e(TAG1,"null is now");
//                        Toast.makeText(BarcodeActivity.this, "请先连接设备", Toast.LENGTH_SHORT).show();
//                    else {
//                        OutputStream os = BlueToothMainActivity.socketall.getOutputStream();
//
//                        os.write(msgBuffer);
//                    }
//                } catch (IOException e) {
//                    //Log.e(TAG1,"s");

//                }
                String nameBlue=BlueToothMainActivity.BlueName;
                if(nameBlue.length()>0)
                {
                    String nameNow=nameBlue.substring(0,2);
                    if(nameNow.equals("PL"))
                    {
                        BlueToothName_Flag=1;
                    }
                    else
                    {
                        BlueToothName_Flag=0;
                    }
                }
                else
                {
                    BlueToothName_Flag=0;
                }
                int barcodeFlagAdd=0;
                for(int x=0;x<1000;x++)
                {
                    if(qrcodeData[x]!=null&&qrcodeData[x].length()>1)
                    {
                        if(qrcodeData[x].substring(1,2).equals("1"))
                        {
                            barcodeFlagAdd++;
                        }
                    }
                }
                final int barcodeFlagC=barcodeFlagAdd;
               if(barcodeFlagAdd>0)
               {
//                   for (int i = 0; i < texteditlayout.getChildCount(); i++) {
//                       viewInformationList.clear();
//                       View child = texteditlayout.getChildAt(i);
//                       if(child instanceof ImageView){
////                        //==========imageview转bitmap======================
////                        child.setDrawingCacheEnabled(true);
////
////                        Bitmap bitmap = child.getDrawingCache();
////
////                        child.setDrawingCacheEnabled(false);
//
////                        bitmap = Bitmap.createBitmap(child.getWidth(), child.getHeight(), Bitmap.Config.ARGB_8888);
////                        Canvas canvas = new Canvas(bitmap);
////                        canvas.drawColor(Color.BLACK);
////                        child.draw(canvas);
////                        //=================================================
//                           bitmap =((BitmapDrawable) ((ImageView) child).getDrawable()).getBitmap();
//
//                           //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                           int a =child.getLeft();
//                           int b=child.getTop();
////                float c=child.getRight();
////                float d=child.getBottom();
//                           int e=child.getHeight();
//                           int f=child.getWidth();
//                           int g=Integer.parseInt(barcodeWidth);
//                           int h=Integer.parseInt(barcodeHeight);
//                           int c=g-a-f;
//                           int d=h-b-e;
//                           viewInformationList.add(String.valueOf(a));
//                           viewInformationList.add(String.valueOf(b));
//                           viewInformationList.add(String.valueOf(c));
//                           viewInformationList.add(String.valueOf(d));
//                           viewInformationList.add(String.valueOf(e));
//                           viewInformationList.add(String.valueOf(f));
//
//                           String viewInformation= gson.toJson(viewInformationList);
//                           ContentValues viewBitmapValues = new ContentValues();
//
//                           final ByteArrayOutputStream os = new ByteArrayOutputStream();
//
//                           bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//                           viewBitmapValues.put("_barcodenumbers", barcodeNumber);
//                           viewBitmapValues.put("_viewflag", "1");
//                           viewBitmapValues.put("_viewinformation", viewInformation);
//                           viewBitmapValues.put("_viewtext", "115200");
//                           viewBitmapValues.put("_viewimage", os.toByteArray());
//                           dbN.insert("viewinformation", null, viewBitmapValues);
//
////                String sql = "insert into viewinformation (_barcodenumbers,_viewflag,_viewinformation,_viewtext,_viewimage) values ('" + barcodeNumber +
////                        "','" + "1" + "','" + viewInformation + "','" + "" + "','" + viewBitmapValues + "')";
////                dbN.execSQL(sql);
//
//
//
////                screenHeight-top-nowGetHeight;
////               _barcodenumbers varchar(50) not null,_viewflag varchar(50) not null,_viewinformation varchar(100) not null,_viewtext varchar(100) not null,_viewimage BLOB
//
//                           // Log.d("NowViewSizeB", a+"  "+b+"  "+c+" "+d+"  "+e+"  "+f+"  ");
//
//                       }
//                       else if(child instanceof TextView)
//                       {
//
//
////                float c=child.getLeft();
////                float d=child.getTop();
////                float e=child.getRight();
////                float f=child.getBottom();
////                float g=child.getHeight();
////                float h=child.getWidth();
//                           String viewText=((TextView) child).getText().toString();
//                           float k=((TextView) child).getTextSize();
//
//                           int a =child.getLeft();
//                           int b=child.getTop();
////                float c=child.getRight();
////                float d=child.getBottom();
//                           int e=child.getHeight();
//                           int f=child.getWidth();
//                           int g=Integer.parseInt(barcodeWidth);
//                           int h=Integer.parseInt(barcodeHeight);
//                           int c=g-a-f;//c=g-a-e;
//                           int d=h-b-e;//d=h-b-f;
//                           viewInformationList.add(String.valueOf(a));
//                           viewInformationList.add(String.valueOf(b));
//                           viewInformationList.add(String.valueOf(c));
//                           viewInformationList.add(String.valueOf(d));
//                           viewInformationList.add(String.valueOf(e));
//                           viewInformationList.add(String.valueOf(f));
//                           viewInformationList.add(String.valueOf(k));
//                           String viewInformation= gson.toJson(viewInformationList);
//
//
//                           //Log.d("NowViewSizeT", c+" "+d+"  "+e+"  "+f+"  "+g+"   "+h+"  "+a+"   "+b+"  ");
//
//                           String sql = "insert into viewinformation (_barcodenumbers,_viewflag,_viewinformation,_viewtext) values ('" + barcodeNumber +
//                                   "','" + "2" + "','" + viewInformation + "','" + viewText + "')";
//                           dbN.execSQL(sql);
//
//
//
//
//                       }
               }
                Thread threadN=new Thread(new Runnable() {
                    @Override
                    public void run() {

                            for(int i=0;i<barcodeSizeCopies;i++)
                            {
                                BarcodePrintCopies(barcodeFlagC);
                            }


                    }
                });
                threadN.start();







            }
        });

        backLastBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(homeFragment==null)
//                    homeFragment=new HomeFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.goodsDisplay,homeFragment).commitAllowingStateLoss();
//                Intent inent = new Intent(BarcodeActivity.this, AngenlActivity.class);
//                startActivity(inent);
                finish();
            }
        });
        barcodeHistoryOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent = new Intent(BarcodeActivity.this, AngenlActivity.class);
                startActivity(inent);
            }
        });

        //MarketText=view.findViewById(R.id.ET_1);

        BarcodeRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));
        BarcodeRecycle.setAdapter(new MakeBarcodeAdapter(BarcodeActivity.this, new MakeBarcodeAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int pos) {
                //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();
                //hintKeyboard(getActivity());
                if (pos == 0) {
                    BlodType=Typeface.SANS_SERIF;
                    showPopupWindow();
                } else if (pos == 1) {
                    LoadImagePopouWindow();
//                    final DragViewX imageView=new DragViewX(texteditlayout.getContext());
//                    getById=id;
//                    getIdFlag = 2;
//                    imageView.setId(id++);
////                    imageView.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            Toast.makeText(BarcodeActivity.this, "sss" + v.getId(), Toast.LENGTH_SHORT).show();
////                        }
////                    });
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if(oldId==0)
//                            {
//
//
//                            }
//                            else
//                            {
//
//                                    if (oldId!=getById) {
//                                        if (idFlag == 1) {
//                                            DragViewT textView = findViewById(oldId);
//                                            textView.setBackgroundResource(0);
//
//                                        } else if (idFlag == 2) {
//                                            DragViewX imageView1 = findViewById(oldId);
//                                            imageView1.setBackgroundResource(0);
//                                        }
//                                    }
//
//                            }
//                            idFlag=2;
//                            oldId=v.getId();
//                            //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    });
//                    imageView.setX(50);
//                    imageView.setY(50);
////                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
////                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
//
//                    //moveImage(imageView, 2);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imageView.setImageResource(R.drawable.xiaoai);
//                    //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);
//
//                    imageView.setClickable(true);
//                    //idPositionX=idPositionX+
//                    texteditlayout.addView(imageView);
                } else if (pos == 2) {
                    final View view = getLayoutInflater().inflate(R.layout.popupwindow_date, null);
                    final TextView button = view.findViewById(R.id.makeDateButton);
                    dateFlag=0;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //=====日期=================================
                            int themeResId = 2;//样式
                            Calendar calendar = Calendar.getInstance();
                            new DatePickerDialog(BarcodeActivity.this, themeResId, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                    //String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                                    //Toast.makeText(BarcodeActivity.this,text,Toast.LENGTH_SHORT).show();
                                    int m=month+1;
                                    String MM="";
                                    if(m<10)
                                    {
                                        MM="0"+""+m;
                                    }
                                    else
                                    {
                                        MM=String.valueOf(m);
                                    }
                                    String text=year+""+MM+""+dayOfMonth;
                                    //text="20200319";
                                    String getDate="";
                                    try{
                                        SimpleDateFormat DateFormat=new SimpleDateFormat("yyyyMMdd");
                                        SimpleDateFormat GetDateFormat=new SimpleDateFormat(pickerString[dateFlag]);
                                        Date newDate=DateFormat.parse(text.toString());
                                        getDate=GetDateFormat.format(newDate);
                                        text=getDate.toString();

                                    }
                                    catch (Exception e)
                                    {

                                    }

                                    //Toast.makeText(BarcodeActivity.this,text,Toast.LENGTH_SHORT).show();
                                    final DragViewT textView = new DragViewT(getApplicationContext());
                                    getById = id;
                                    getIdFlag = 1;
                                    textView.setTextColor(Color.BLACK);
                                    textView.setId(id++);
                                    textView.setText(text);
                                    //textView.setBackgroundColor(10);
                                    textSize=60;
                                    textView.setTextSize(60);
                                    textView.setStateListAnimator(null);
                                    textView.bringToFront();

                                    textView.setClickable(true);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DoubleClickView(textView);
//                                            if(oldId==0)
//                                            {
//
//
//                                            }
//                                            else
//                                            {
//
//                                                    if (oldId != getById) {
//                                                        if (idFlag == 1) {
//                                                            DragViewT textView = findViewById(oldId);
//                                                            textView.setBackgroundResource(0);
//
//                                                        } else if (idFlag == 2) {
//                                                            DragViewX imageView1 = findViewById(oldId);
//                                                            imageView1.setBackgroundResource(0);
////                                                            imageView1.setBackground(null);
//                                                        }
//                                                    }
//
//                                            }
//
//                                            idFlag=1;
//                                            oldId=v.getId();
//                                            //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                            return;
                                        }
                                    });
                                    //moveImage(textView, 1);
                                    texteditlayout.addView(textView);
                                }
                            }
                                    , calendar.get(Calendar.YEAR)
                                    , calendar.get(Calendar.MONTH)
                                    , calendar.get(Calendar.DAY_OF_MONTH)).show();
                            //======================================================
                            popupWindow.dismiss();
                        }
                    });
                    // ImageView imageViewXXX=findViewById(getById);
                    //moveImage(imageViewXXX);

                    final com.shawnlin.numberpicker.NumberPicker mNumberPicker=view.findViewById(R.id.makeDateNumberPicker);

                    mNumberPicker.setDisplayedValues(pickerString);//设置需要显示的数组
                    mNumberPicker.setMinValue(0);
                    mNumberPicker.setWrapSelectorWheel(false);
                    mNumberPicker.setMaxValue(pickerString.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法
//                    setPickerDividerColor(mNumberPicker);
//                    setNumberPickerTextColor(mNumberPicker,Color.BLACK);
//                    mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                        @Override
//                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                            dateFlag=newVal;
//                            //得到选择结果
//                            //Toast.makeText(BarcodeActivity.this,""+pickerString[newVal]+newVal,Toast.LENGTH_SHORT).show();
//                        }
//                    });


                    mNumberPicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                            dateFlag=newVal;
                        }
                    });
                    makePopupWiondo(view);





                } else if (pos == 3) {
                    final View view = getLayoutInflater().inflate(R.layout.popupwindow_time, null);
                    final TextView button = view.findViewById(R.id.makeTimeButton);
                    timeFlag=0;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //=====日期=================================
                            int themeResId = 2;
                            Calendar calendar = Calendar.getInstance();
                            new TimePickerDialog(BarcodeActivity.this, themeResId, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    //String text = hourOfDay + "时" + minute + "分";
                                    //Toast.makeText(BarcodeActivity.this,text,Toast.LENGTH_SHORT).show();
                                    int h=hourOfDay;
                                    String hh="";
                                    String mm="";
                                    if(h<10)
                                    {
                                        hh="0"+""+h;
                                    }
                                    else
                                    {
                                        hh=String.valueOf(h);
                                    }
                                    h=minute;
                                    if(h<10)
                                    {
                                        mm="0"+""+h;
                                    }
                                    else
                                    {
                                        mm=String.valueOf(h);
                                    }

                                    String text=""+hh+""+mm;
                                    //text="20200319";
                                    String getDate="";
                                    try{
                                        SimpleDateFormat TimeFormat=new SimpleDateFormat("hhmm");
                                        SimpleDateFormat GetDateFormat=new SimpleDateFormat(pickerTimeString[timeFlag]);
                                        Date newDate=TimeFormat.parse(text.toString());
                                        getDate=GetDateFormat.format(newDate);
                                        text=getDate.toString();

                                    }
                                    catch (Exception e)
                                    {

                                    }
                                    final DragViewT textView = new DragViewT(getApplicationContext());
                                    getById = id;
                                    getIdFlag = 1;
                                    textView.setTextColor(Color.BLACK);
                                    textView.setId(id++);
                                    textView.setText(text);
                                    //textView.setBackgroundColor(10);
                                    textSize=60;
                                    textView.setTextSize(60);
                                    textView.setStateListAnimator(null);
                                    textView.bringToFront();

                                    textView.setClickable(true);
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            DoubleClickView(textView);
//                                            if(oldId==0)
//                                            {
//
//
//                                            }
//                                            else
//                                            {
//
//                                                    if (oldId != getById) {
//                                                        if (idFlag == 1) {
//                                                            DragViewT textView = findViewById(oldId);
//                                                            textView.setBackgroundResource(0);
//
//                                                        } else if (idFlag == 2) {
//                                                            DragViewX imageView1 = findViewById(oldId);
//                                                            imageView1.setBackgroundResource(0);
////                                                            imageView1.setBackground(null);
//                                                        }
//                                                    }
//
//                                            }
//                                            idFlag=1;
//                                            oldId=v.getId();
//                                            //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                            return;
                                        }
                                    });

                                    //moveImage(textView, 1);
                                    texteditlayout.addView(textView);
                                }
                            }
                                    , calendar.get(Calendar.HOUR_OF_DAY)
                                    , calendar.get(Calendar.MINUTE)
                                    , true).show();
                            //======================================================
                            popupWindow.dismiss();
                        }
                    });


                    final com.shawnlin.numberpicker.NumberPicker mNumberPicker=view.findViewById(R.id.makeTimeNumberPicker);

                    mNumberPicker.setDisplayedValues(pickerTimeString);//设置需要显示的数组
                    mNumberPicker.setMinValue(0);
                    mNumberPicker.setWrapSelectorWheel(false);
                    mNumberPicker.setMaxValue(pickerTimeString.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法

//                    setPickerDividerColor(mNumberPicker);
//                    setNumberPickerTextColor(mNumberPicker,Color.BLACK);
//                    mNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                        @Override
//                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                            timeFlag=newVal;
//                            //得到选择结果
//                            //Toast.makeText(BarcodeActivity.this,""+pickerString[newVal]+newVal,Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    mNumberPicker.setOnValueChangedListener(new com.shawnlin.numberpicker.NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(com.shawnlin.numberpicker.NumberPicker picker, int oldVal, int newVal) {
                            timeFlag=newVal;
                        }
                    });
                    makePopupWiondo(view);

                }else if(pos==4)
                {
                    LoadIconTitlePopuWindow();
                }
                else if(pos==8)
                {
                    LoadTradeMarkPopuWindwo();
                }
                else if (pos == 400) {

                    final View view = getLayoutInflater().inflate(R.layout.popupwindow_inputicon, null);
                    TextView emojiMakesure=view.findViewById(R.id.emojiMakesure);
                    emojiMakesure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    scrollViewE = view.findViewById(R.id.emojiscrollViewT1);
                    layoutE1 = view.findViewById(R.id.layoutE1);
                    layoutE2 = view.findViewById(R.id.layoutE2);
                    layoutE3 = view.findViewById(R.id.layoutE3);
                    layoutE4 = view.findViewById(R.id.layoutE4);
                    imageE1 = view.findViewById(R.id.emojiImage1);
                    imageE2 = view.findViewById(R.id.emojiImage2);
                    imageE3 = view.findViewById(R.id.emojiImage3);
                    imageE4 = view.findViewById(R.id.emojiImage4);
                    //textView=wh.findViewById(R.id.Xx);
                    //textView.setText("ssssssss");
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    final int width = getWidthInPx(BarcodeActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                    ViewGroup.LayoutParams params1 = layoutE1.getLayoutParams();
                    ViewGroup.LayoutParams params2 = layoutE2.getLayoutParams();
                    ViewGroup.LayoutParams params3 = layoutE3.getLayoutParams();
                    ViewGroup.LayoutParams params4 = layoutE4.getLayoutParams();
                    params1.width = width;
                    params2.width = width;
                    params3.width = width;
                    params4.width = width;

                    layoutE1.setLayoutParams(layoutParams);
                    layoutE2.setLayoutParams(layoutParams);
                    layoutE3.setLayoutParams(layoutParams);
                    layoutE4.setLayoutParams(layoutParams);

                    EmojiRecycle = view.findViewById(R.id.emojiRecycle1);
                    EmojiRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));
                    EmojiRecycle.setAdapter(new EmojiAdapter(BarcodeActivity.this, 0x1f601, 80, new EmojiAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();

                            String emojiString = getEmojiStringByUnicode(emojiType1 + pos);
                            final DragViewT textView = new DragViewT(getApplicationContext());
                            getById = id;
                            getIdFlag = 1;
                            textView.setTextColor(Color.BLACK);
                            textView.setId(id++);
                            textView.setText(emojiString);
                            //textView.setBackgroundColor(10);
                            textSize=72;
                            textView.setTextSize(72);
                            textView.setStateListAnimator(null);
                            textView.bringToFront();

                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DoubleClickView(textView);
//                                    if(oldId==0)
//                                    {
//
//
//                                    }
//                                    else
//                                    {
//
//                                            if (oldId != getById) {
//                                                if (idFlag == 1) {
//                                                    DragViewT textView = findViewById(oldId);
//                                                    textView.setBackgroundResource(0);
//
//                                                } else if (idFlag == 2) {
//                                                    DragViewX imageView1 = findViewById(oldId);
//                                                    imageView1.setBackgroundResource(0);
////                                                    imageView1.setBackground(null);
//                                                }
//                                            }
//
//                                    }
//                                    idFlag=1;
//                                    oldId=v.getId();
//                                    //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                    return;
                                }
                            });
                            //moveImage(textView, 1);
                            texteditlayout.addView(textView);
                        }
                    }));
                    EmojiRecycle = view.findViewById(R.id.emojiRecycle2);
                    EmojiRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));
                    EmojiRecycle.setAdapter(new EmojiAdapter(BarcodeActivity.this, 0x1f680, 64, new EmojiAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();

                            String emojiString = getEmojiStringByUnicode(emojiType2 + pos);
                            final DragViewT textView = new DragViewT(getApplicationContext());
                            getById = id;
                            getIdFlag = 1;
                            textView.setTextColor(Color.BLACK);
                            textView.setId(id++);
                            textView.setText(emojiString);
                            //textView.setBackgroundColor(10);
                            textSize=72;
                            textView.setTextSize(72);
                            textView.setStateListAnimator(null);
                            textView.bringToFront();
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DoubleClickView(textView);
//                                    if(oldId==0)
//                                    {
//
//
//                                    }
//                                    else
//                                    {
//
//                                            if (oldId != getById) {
//                                                if (idFlag == 1) {
//                                                    DragViewT textView = findViewById(oldId);
//                                                    textView.setBackgroundResource(0);
//
//                                                } else if (idFlag == 2) {
//                                                    DragViewX imageView1 = findViewById(oldId);
//                                                    imageView1.setBackgroundResource(0);
////                                                    imageView1.setBackground(null);
//                                                }
//                                            }
//
//                                    }
//                                    idFlag=1;
//                                    oldId=v.getId();
//                                    //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                    return;
                                }
                            });
                            //moveImage(textView, 1);
                            texteditlayout.addView(textView);
                        }
                    }));
                    EmojiRecycle = view.findViewById(R.id.emojiRecycle3);
                    EmojiRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));
                    EmojiRecycle.setAdapter(new EmojiAdapter(BarcodeActivity.this, 0x2122, 1000, new EmojiAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();

                            String emojiString = getEmojiStringByUnicode(emojiType3 + pos);
                            final DragViewT textView = new DragViewT(getApplicationContext());
                            getById = id;
                            getIdFlag = 1;
                            textView.setTextColor(Color.BLACK);
                            textView.setId(id++);
                            textView.setText(emojiString);
                            //textView.setBackgroundColor(10);
                            textSize=72;
                            textView.setTextSize(72);
                            textView.setStateListAnimator(null);
                            textView.bringToFront();
                            textView.setClickable(true);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DoubleClickView(textView);
//                                    if(oldId==0)
//                                    {
//
//
//                                    }
//                                    else
//                                    {
//
//                                            if (oldId != getById) {
//                                                if (idFlag == 1) {
//                                                    DragViewT textView = findViewById(oldId);
//                                                    textView.setBackgroundResource(0);
//
//                                                } else if (idFlag == 2) {
//                                                    DragViewX imageView1 = findViewById(oldId);
//                                                    imageView1.setBackgroundResource(0);
////                                                    imageView1.setBackground(null);
//                                                }
//                                            }
//
//                                    }
//                                    idFlag=1;
//                                    oldId=v.getId();
//                                    //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                    return;
                                }
                            });
                            //moveImage(textView, 1);
                            texteditlayout.addView(textView);
                        }
                    }));
                    EmojiRecycle = view.findViewById(R.id.emojiRecycle4);
                    EmojiRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));
                    EmojiRecycle.setAdapter(new EmojiAdapter(BarcodeActivity.this, 0x1f300, 615, new EmojiAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(int pos) {
                            //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();

                            String emojiString = getEmojiStringByUnicode(emojiType4 + pos);
                            final DragViewT textView = new DragViewT(getApplicationContext());
                            getById = id;
                            getIdFlag = 1;
                            textView.setTextColor(Color.BLACK);
                            textView.setId(id++);
                            textView.setText(emojiString);
                            //textView.setBackgroundColor(10);
                            textSize=72;
                            textView.setTextSize(72);
                            textView.setStateListAnimator(null);
                            textView.bringToFront();
                            textView.setClickable(true);
                            //moveImage(textView, 1);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DoubleClickView(textView);
//                                    if(oldId==0)
//                                    {
//
//
//                                    }
//                                    else
//                                    {
//
//                                            if (oldId != getById) {
//                                                if (idFlag == 1) {
//                                                    DragViewT textView = findViewById(oldId);
//                                                    textView.setBackgroundResource(0);
//
//                                                } else if (idFlag == 2) {
//                                                    DragViewX imageView1 = findViewById(oldId);
//                                                    imageView1.setBackgroundResource(0);
////                                                    imageView1.setBackground(null);
//                                                }
//                                            }
//
//                                    }
//                                    idFlag=1;
//                                    oldId=v.getId();
//                                    //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();

                                    return;
                                }
                            });
                            texteditlayout.addView(textView);
                        }
                    }));
                    makePopupWiondo(view);
                    scrollViewE.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return true;

                        }

                    });
                    view.findViewById(R.id.emojiImage1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            keybordFlag=0;
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//
//                            ViewGroup.LayoutParams params1 = layoutE1.getLayoutParams();
//                            ViewGroup.LayoutParams params2 = layoutE2.getLayoutParams();
//                            params1.width = width;
//                            params2.width = width;
//                            layoutE1.setLayoutParams(layoutParams);
//                            layoutE2.setLayoutParams(layoutParams);


                            imageE1.setImageResource(R.drawable.emoji12);
                            imageE2.setImageResource(R.drawable.zgemoji21);
                            imageE3.setImageResource(R.drawable.emoji31);
                            imageE4.setImageResource(R.drawable.emoji41);
                            scrollViewE.scrollTo(0, 0);

                        }
                    });
                    view.findViewById(R.id.emojiImage2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            keybordFlag=0;
//                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//
//                            ViewGroup.LayoutParams params1 = layoutE1.getLayoutParams();
//                            ViewGroup.LayoutParams params2 = layoutE2.getLayoutParams();
//                            params1.width = width;
//                            params2.width = width;
//                            layoutE1.setLayoutParams(layoutParams);
//                            layoutE2.setLayoutParams(layoutParams);

                            imageE1.setImageResource(R.drawable.emoji11);
                            imageE2.setImageResource(R.drawable.emoji22);
                            imageE3.setImageResource(R.drawable.emoji31);
                            imageE4.setImageResource(R.drawable.emoji41);
                            scrollViewE.scrollTo(width, 0);

                        }
                    });
                    view.findViewById(R.id.emojiImage3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageE1.setImageResource(R.drawable.emoji11);
                            imageE2.setImageResource(R.drawable.zgemoji21);
                            imageE3.setImageResource(R.drawable.emoji32);
                            imageE4.setImageResource(R.drawable.emoji41);
                            scrollViewE.scrollTo(2 * width, 0);

                        }
                    });
                    view.findViewById(R.id.emojiImage4).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageE1.setImageResource(R.drawable.emoji11);
                            imageE2.setImageResource(R.drawable.zgemoji21);
                            imageE3.setImageResource(R.drawable.emoji31);
                            imageE4.setImageResource(R.drawable.emoji42);
                            scrollViewE.scrollTo(3 * width, 0);

                        }
                    });
//                    BarcodeRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this,5));
//                    BarcodeRecycle.setAdapter(new MakeBarcodeAdapter(BarcodeActivity.this, new MakeBarcodeAdapter.OnItemClickListener() {
//                        @Override
//                        public void onClick(int pos) {


                } else if (pos == 5 || pos == 6) {
                    View view = getLayoutInflater().inflate(R.layout.popupwindow_inputbarcode, null);
                    final EditText editText = view.findViewById(R.id.popupWindowBarcodeInput);
                    final LinearLayout linearLayout=view.findViewById(R.id.QRinputLayout);
                    final LinearLayout linearLayout1=view.findViewById(R.id.QRSetLayout);
                    final Button buttonR=view.findViewById(R.id.QRSetReduce);
                    final Button buttonA=view.findViewById(R.id.QRSetAdd);
                    final TextView textViewD=view.findViewById(R.id.QRSetNumber);
                    final CheckBox checkBox=view.findViewById(R.id.QRCheckS);
                    linearLayout1.setVisibility(View.INVISIBLE);
                    QRimage1  = view.findViewById(R.id.barcodeInput);
                    QRimage2  = view.findViewById(R.id.barcodeXulie);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //params.width =200;

                    params.height =150;
                    linearLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params1.height =0;
                    linearLayout1.setLayoutParams(params1);



                    view.findViewById(R.id.barcodeInput).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            QRimage1.setImageResource(R.drawable.zgtextinput);
                            QRimage2.setImageResource(R.drawable.xulieblack);
                            linearLayout.setVisibility(View.VISIBLE);
                            linearLayout1.setVisibility(View.INVISIBLE);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //params.width =200;

                            params.height =150;
                            linearLayout.setLayoutParams(params);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params1.height =0;
                            linearLayout1.setLayoutParams(params1);
                            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(editText, 0);


                        }
                    });
                    view.findViewById(R.id.barcodeXulie).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            QRimage1.setImageResource(R.drawable.zgtextinput2);
                            QRimage2.setImageResource(R.drawable.xulie);
                            linearLayout.setVisibility(View.INVISIBLE);
                            linearLayout1.setVisibility(View.VISIBLE);


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //params.width =200;

                            params.height =0;
                            linearLayout.setLayoutParams(params);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            //params.width =200;
                            params1.height =700;

                            linearLayout1.setLayoutParams(params1);
                            final int width = getWidthInPx(BarcodeActivity.this);
                            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(editText, 0);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            buttonR.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int X=Integer.parseInt(textViewD.getText().toString());
                                    X--;

                                    textViewD.setText(String.valueOf(X));
                                }
                            });
                            buttonA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int X=Integer.parseInt(textViewD.getText().toString());
                                    X++;

                                    textViewD.setText(String.valueOf(X));
                                }
                            });
//                            Timer timer = new Timer();
//                            timer.schedule(new TimerTask() {
//                                public void run() {
//                                    scrollView.scrollTo(width, 0);
//
//
//                                }
//                            }, 200);
//
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            boolean isOpen = imm.isActive();
//                            if (isOpen) {
//                                // imm.toggleSoftInput(0,
//                                // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//                                imm.hideSoftInputFromWindow(edt_pass.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                            }
                        }
                    });




                    TextView textMakesure=view.findViewById(R.id.textMakesure);
                    textMakesure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String xx=qrcodeData[getById];//2000+
                            String[] xyz=xx.split("[+]");
                            int l1=xyz[0].length()+1;
                            String s1=xx.substring(l1,xx.length());
                            int xy=Integer.parseInt(textViewD.getText().toString());
                            if(checkBox.isChecked())
                            {
                                String s2="";
                                if(xy>0)
                                {
                                     s2=xyz[0].substring(0,1).toString()+"1"+"1"+xy+"+";

                                }
                                else if(xy==0)
                                {
                                     s2=xyz[0].substring(0,1).toString()+"0"+"1"+xy+"+";
                                }
                                else
                                {
                                    String s3=String.valueOf(xy);
                                    s3=s3.substring(1,s3.length());
                                     s2=xyz[0].substring(0,1).toString()+"1"+"0"+s3+"+";
                                }
                                qrcodeData[getById]=s2+s1;
                            }
                            else
                            {
                                String s2="";
                                s2=xyz[0].substring(0,1).toString()+"0"+"0"+"0"+"+";
                                qrcodeData[getById]=s2+s1;
                            }


                            popupWindow.dismiss();
                        }
                    });
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            ImageView imageView = findViewById(getById);

                            String[] xx=qrcodeData[getById].split("[+]");
                            if (!s.toString().equals("")) {
                                if (pos == 6) {

                                    qrcodeData[getById]=xx[0]+"+"+s.toString();//2
                                    makeQrCode(s.toString());

                                    //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                                } else if (pos == 5) {
                                    //qrImage = encodeAsBitmap(s.toString(), BarcodeFormat.CODE_128, 350, 150);
                                    qrcodeData[getById]=xx[0]+"+"+s.toString();//1
                                    qrImage=creatBarcode(getApplicationContext(), s.toString(), 350, 150, true);

                                }

                                //makeQrCode(s.toString());

                                imageView.setImageBitmap(qrImage);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    makePopupWiondo(view);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(editText, 0);
                        }
                    }, 200);


                    //final ImageView imageView = new ImageView(getApplicationContext());
                    final DragViewX imageView = new DragViewX(getApplicationContext());
                    getById = id;
                    getIdFlag = 2;
                    imageView.setId(id++);
                    if (pos == 6) {
                        //第一位代表一维码还是二维码，1代表一维码2代表二维码
                        //第二位代表是否有序列，0代表没有序列1代表有序列
                        //第三位代表是递增还是递减，0代表递减1代表递增
                        //第四位->+的前面代表递增或递减的幅度
                        qrcodeData[getById]="2000+"+"123456";
                        makeQrCode("123456");
                        //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                    } else if (pos == 5) {
                       // qrImage = encodeAsBitmap("123456", BarcodeFormat.CODE_128, 350, 150);
                        qrcodeData[getById]="1000+"+"123456";
                        qrImage=creatBarcode(getApplicationContext(), "123456", 350, 150, true);
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DoubleClickImageView(imageView);

//                            if(oldId==0)
//                            {
//
//
//                            }
//                            else
//                            {
//
//                                    if (oldId != getById) {
//                                        if (idFlag == 1) {
//                                            DragViewT textView = findViewById(oldId);
//                                            textView.setBackgroundResource(0);
//
//                                        } else if (idFlag == 2) {
//                                            DragViewX imageView1 = findViewById(oldId);
//                                            imageView1.setBackgroundResource(0);
////                                            imageView1.setBackground(null);
//                                        }
//                                    }
//
//                            }
//                            idFlag=2;
//                            oldId=v.getId();
//                            Toast.makeText(BarcodeActivity.this,"V:"+v.getId()+"D:"+getById,Toast.LENGTH_SHORT).show();
//                            return;
                        }
                    });
                    imageView.setImageBitmap(qrImage);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                    //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);

                    imageView.setClickable(true);
                    //imageView.setBackgroundColor(10);

                    //moveImage(imageView, 2);

                    //texteditlayout.addView(textView);


                    texteditlayout.addView(imageView);
                } else if (pos == 7) {
                    //=============================================================================================
                    final View view = getLayoutInflater().inflate(R.layout.popupwindow_inputgraffiti, null);

                    final RelativeLayout graffitiLayout=view.findViewById(R.id.graffitiLayout);
                   // getViewHeight(graffitiLayout);
                    final TextView button = view.findViewById(R.id.makeGraffitiButton);
                    final ImageView imageViewLine=view.findViewById(R.id.graffitiInput);
                    final ImageView imageViewBlock=view.findViewById(R.id.graffitiBlock);
                    final ImageView imageViewCircle=view.findViewById(R.id.graffitiCircle);
                    final ImageView imageViewEraser=view.findViewById(R.id.graffitiEraser);
                    final ImageView imageViewDline=view.findViewById(R.id.graffitiDline);


                    makePopupWiondo(view);
                    final DrawView drawView=new DrawView(BarcodeActivity.this,graffitiLayout);
                    graffitiLayout.addView(drawView);
                    imageViewLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewLine.setImageResource(R.drawable.line11);
                            imageViewBlock.setImageResource(R.drawable.block42);
                            imageViewCircle.setImageResource(R.drawable.circle32);
                            imageViewDline.setImageResource(R.drawable.dline22);
                            imageViewEraser.setImageResource(R.drawable.eraser52);
                            drawView.SelectDraw(0);
                        }
                    });
                    imageViewBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewLine.setImageResource(R.drawable.line12);
                            imageViewBlock.setImageResource(R.drawable.block41);
                            imageViewCircle.setImageResource(R.drawable.circle32);
                            imageViewDline.setImageResource(R.drawable.dline22);
                            imageViewEraser.setImageResource(R.drawable.eraser52);
                            drawView.SelectDraw(1);
                        }
                    });
                    imageViewCircle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewLine.setImageResource(R.drawable.line12);
                            imageViewBlock.setImageResource(R.drawable.block42);
                            imageViewCircle.setImageResource(R.drawable.circle31);
                            imageViewDline.setImageResource(R.drawable.dline22);
                            imageViewEraser.setImageResource(R.drawable.eraser52);
                            drawView.SelectDraw(2);
                        }
                    });
                    imageViewEraser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewLine.setImageResource(R.drawable.line12);
                            imageViewBlock.setImageResource(R.drawable.block42);
                            imageViewCircle.setImageResource(R.drawable.circle32);
                            imageViewDline.setImageResource(R.drawable.dline22);
                            imageViewEraser.setImageResource(R.drawable.eraser51);
                            drawView.SelectDraw(4);
                        }
                    });
                    imageViewDline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imageViewLine.setImageResource(R.drawable.line12);
                            imageViewBlock.setImageResource(R.drawable.block42);
                            imageViewCircle.setImageResource(R.drawable.circle32);
                            imageViewDline.setImageResource(R.drawable.dline21);
                            imageViewEraser.setImageResource(R.drawable.eraser52);
                            drawView.SelectDraw(5);
                        }
                    });


                    //DrawView imageView=view.findViewById(R.id.imageGraffiti);
                    //DrawView drawView=new DrawView(BarcodeActivity.this);
                    //DrawView drawView=new DrawView(BarcodeActivity.this);

                   //imageView=drawView;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //final ImageView imageView = new ImageView((getApplicationContext()));
                            final DragViewX imageView = new DragViewX((getApplicationContext()));
                            getById = id;
                            getIdFlag = 2;
                            imageView.setId(id++);


//                            imageView.setX(100);
//                            imageView.setY(100);
//
//                            Matrix matrix = new Matrix();
//                            matrix.postScale((float) 0.2, (float) 0.2);
//                            bitmap = Bitmap.createBitmap(drawView.SaveBitmap(), 0, 0, drawView.SaveBitmap().getWidth(), drawView.SaveBitmap().getHeight(), matrix, true);
//                            imageView.setImageBitmap(drawView.SaveBitmap());
//                            imageView.setScaleX((float) 0.4);
//                            imageView.setScaleY((float) 0.4);


                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    if(oldId==0)
//                                    {
//
//
//                                    }
//                                    else
//                                    {
//
//                                            if (oldId != getById) {
//                                                if (idFlag == 1) {
//                                                    DragViewT textView = findViewById(oldId);
//                                                    textView.setBackgroundResource(0);
//
//                                                } else if (idFlag == 2) {
//                                                    DragViewX imageView1 = findViewById(oldId);
//                                                    imageView1.setBackgroundResource(0);
////                                                    imageView1.setBackground(null);
//                                                }
//                                            }
//
//                                    }
//                                    idFlag=2;
//                                    oldId=v.getId();
//                                    //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                                    return;
                                }
                            });
                            //imageView.setImageBitmap(qrImage);
                            imageView.setImageBitmap(drawView.SaveBitmap());
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            double widthX=drawView.SaveBitmap().getWidth();
                            double heightX=drawView.SaveBitmap().getHeight();
                            int width=200;
                            double heightPercent=heightX/widthX;
                            double height=heightPercent*width;
                            //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);

                            imageView.setClickable(true);
                            //moveImage(imageView, 2);

                            texteditlayout.addView(imageView,width,(int)height);
                            popupWindow.dismiss();
                        }
                    });
                    // ImageView imageViewXXX=findViewById(getById);
                    //moveImage(imageViewXXX);


//                    final DrawView imageView = view.findViewById(R.id.imageGraffiti);
//                    getViewHeight(imageView);

//                    imageView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            int action = event.getAction();
//                            switch (action) {
//                                // 用户手指摸到屏幕
//                                case MotionEvent.ACTION_DOWN:
//                                    startX = (int) event.getX();
//                                    startY = (int) event.getY();
//                                    break;
//                                // 用户手指正在滑动
//                                case MotionEvent.ACTION_MOVE:
//                                    float x = event.getX();
//                                    float y = event.getY();
////                                    canvas.drawLine(startX, startY, x, y, paint);
////                                    canvas.save();
////                                    canvas.restore();
////                                    // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
////                                    startX = x;
////                                    startY = y;
////                                    imageView.setImageBitmap(bitcopy);
////
//                                    canvas.drawRect(startX,startY,x,y,paint);
//                                    canvas.save();
//                                    canvas.restore();
//                                    // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
//                                    //startX = x;
//                                    //startY = y;
//                                    //imageView.postInvalidate();
//                                    imageView.setImageBitmap(bitcopy);
//                                    break;
//                                // 用户手指离开屏幕
//                                case MotionEvent.ACTION_UP:
//                                    break;
//
//                            }
//                            // true：告诉系统，这个触摸事件由我来处理
//                            // false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
//                            return true;
//                        }
//                    });



//===============================================
                } else if (pos == 9) {
//                    if (ContextCompat.checkSelfPermission(BarcodeActivity.this,
//                            Manifest.permission.CAMERA)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(BarcodeActivity.this,
//                                Manifest.permission.CAMERA)) {
//                            Toast.makeText(BarcodeActivity.this, "二维码扫码需要相机权限", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            ActivityCompat.requestPermissions(BarcodeActivity.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    0);
//                        }
//                    }else{
//                        IntentIntegrator intentIntegrator=new IntentIntegrator(BarcodeActivity.this);
//                        intentIntegrator.initiateScan();
//                    }

                    selectScannerFlag=0;
                    if (ContextCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(BarcodeActivity.this, Manifest.permission.CAMERA)) {
                            Toast.makeText(BarcodeActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                        }
                        // 申请权限
                        ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                        return;
                    }
                    // 二维码扫码
                    Intent intent = new Intent(BarcodeActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1001);



                }
                else if(pos==8)
                {
//                    Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.supermaket128);
//                    final String content="你好小艾";
//
//                    QRDialog qrDialog=new QRDialog(BarcodeActivity.this,content);
//
//                    qrDialog.setCancel("取消", new QRDialog.IOnCancelListener() {
//                        @Override
//                        public void onCancel(QRDialog dialog) {
//
//                        }
//                    }).setConfirm("确认", new QRDialog.IOnConfirmListener() {
//                        @Override
//                        public void onConfirm(QRDialog dialog) {
//
//
//
//
//                        }
//                    }).show();
                }

            }
        }));


/*

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(BarcodeActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        BarcodeRecycle.setLayoutManager(linearLayoutManager);
        BarcodeRecycle.setAdapter(new LinearHorAdapter(BarcodeActivity.this, new LinearHorAdapter.OnItemClickListener() {
            @Override
            public void onClick(final int pos) {
                Toast.makeText(BarcodeActivity.this,"Click"+pos,Toast.LENGTH_SHORT).show();
                if(pos==0) {
                    View view = getLayoutInflater().inflate(R.layout.popupwindow_inputtext, null);
                    final EditText editText=view.findViewById(R.id.popupWindowET_1);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            TextView textView=findViewById(getById);
                            textView.setText(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    makePopupWiondo(view);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        public void run()
                        {
                            InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(editText, 0);
                        }
                    },200);

                    final TextView textView =new TextView(getApplicationContext());
                    getById=id;
                    textView.setTextColor(Color.BLACK);
                    textView.setId(id++);
                    textView.setText("hahhah");
                    textView.setBackgroundColor(10);
                    getIdFlag=1;
                    moveImage(textView,1);
                    texteditlayout.addView(textView);
                }
                else if(pos==1)
                {
                    final ImageView imageView=new ImageView((getApplicationContext()));

                    imageView.setId(id++);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageView.setX(50);
                    imageView.setY(50);
//                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
//                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
                    getIdFlag=2;
                    moveImage(imageView,2);
                    imageView.setImageResource(R.drawable.xiaoai);


                    //idPositionX=idPositionX+
                    texteditlayout.addView(imageView);
                }
                else if(pos==2){
                    //final View view =getLayoutInflater().inflate(R.layout.popupwindow_inputgraffiti,null);




                    showPopupWindow();

//=============================================================================================
//                    final View view =getLayoutInflater().inflate(R.layout.popupwindow_inputgraffiti,null);
//                    final Button button=view.findViewById(R.id.makeGraffitiButton);
//
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            final ImageView imageView=new ImageView((getApplicationContext()));
//
//                            getById=id;
//                            imageView.setId(id++);
//                            imageView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                            imageView.setX(100);
//                            imageView.setY(100);
//                            //bitcopy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//                            //bitmap=Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888);
//                            //bitmap=Bitmap.createBitmap(bitcopy);
//                            Matrix matrix=new Matrix();
//                            matrix.postScale((float) 0.2,(float) 0.2);
//
//                            bitmap=Bitmap.createBitmap(bitcopy,0,0,bitcopy.getWidth(),bitcopy.getHeight(),matrix,true);
//                            imageView.setImageBitmap(bitmap);
//                            //imageView.setScaleX((float) 0.2);
//                            //imageView.setScaleY((float) 0.2);
//                            //imageView.setMaxHeight(1);
//                            //imageView.setMaxWidth(1);
//                            getIdFlag=2;
//                            moveImage(imageView,2);
//
//                            texteditlayout.addView(imageView);
//                            popupWindow.dismiss();
//                        }
//                    });
//                    // ImageView imageViewXXX=findViewById(getById);
//                    //moveImage(imageViewXXX);
//
//
//
//                    final ImageView imageView=view.findViewById(R.id.imageGraffiti);
//                    getViewHeight(imageView);
//                    imageView.setOnTouchListener(new View.OnTouchListener() {
//                        @Override
//                        public boolean onTouch(View v, MotionEvent event) {
//                            int action = event.getAction();
//                            switch (action) {
//                                // 用户手指摸到屏幕
//                                case MotionEvent.ACTION_DOWN:
//                                    startX = (int) event.getX();
//                                    startY = (int) event.getY();
//                                    break;
//                                // 用户手指正在滑动
//                                case MotionEvent.ACTION_MOVE:
//                                    float x = event.getX();
//                                    float y = event.getY();
//                                    canvas.drawLine(startX, startY, x, y, paint);
//                                    canvas.save();
//                                    canvas.restore();
//                                    // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
//                                    startX = x;
//                                    startY = y;
//                                    imageView.setImageBitmap(bitcopy);
//                                    break;
//                                // 用户手指离开屏幕
//                                case MotionEvent.ACTION_UP:
//                                    break;
//
//                            }
//                            // true：告诉系统，这个触摸事件由我来处理
//                            // false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
//                            return true;
//                        }
//                    });
//                    makePopupWiondo(view);
//


//===============================================
//                    popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    popupWindow.setFocusable(true);
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
//                    popupWindow.setAnimationStyle(R.style.anim_bottom_pop);
//
//                    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                    popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
                }
                else if(pos==4||pos==3)
                {
                    View view = getLayoutInflater().inflate(R.layout.popupwindow_inputtext, null);
                    final EditText editText=view.findViewById(R.id.popupWindowET_1);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            ImageView imageView=findViewById(getById);
                            if(!s.toString().equals(""))
                            {
                                if(pos==4) {
                                    makeQrCode(s.toString());
                                    //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                                }
                                else if(pos==3) {
                                    qrImage = encodeAsBitmap(s.toString(), BarcodeFormat.CODE_128, 150, 100);
                                }

                                    //makeQrCode(s.toString());

                                imageView.setImageBitmap(qrImage);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    makePopupWiondo(view);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        public void run()
                        {
                            InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.showSoftInput(editText, 0);
                        }
                    },200);


                    final ImageView imageView =new ImageView(getApplicationContext());
                    getById=id;

                    imageView.setId(id++);
                    if(pos==4) {
                        makeQrCode("123456");
                        //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                    }
                    else if(pos==3) {
                        qrImage = encodeAsBitmap("123456", BarcodeFormat.CODE_128, 100, 50);
                    }
                    imageView.setImageBitmap(qrImage);
                    //imageView.setBackgroundColor(10);
                    getIdFlag=2;
                    moveImage(imageView,2);

                    //texteditlayout.addView(textView);


                    texteditlayout.addView(imageView);
                }

//                popupWindow=new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setFocusable(true);
//                popupWindow.setOutsideTouchable(false);
//                popupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
            }
        }));*/

        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TextView textView=findViewById(getById);
//                textView.setRotation(90);
                if (rotateAngle > 360)
                    rotateAngle = 0;
                rotateAngle = rotateAngle + 90;

                if (getIdFlag == 1) {
                    DragViewT textView = findViewById(getById);
                    int textViewWidth=textView.getWidth();
                    int textViewHeight=textView.getHeight();
                    int l= textView.getLeft();
                    int t=textView.getTop();
                    int r=textView.getRight();
                    int b=textView.getBottom();
//                    textView.setPivotX(l);
//                    textView.setPivotY(t);
                    textView.setRotation(rotateAngle);

                    //textView.setDegrees(rotateAngle);
                    String xx=textView.getText().toString();
                    textView.setText(xx);
                    //textView.setGravity(Gravity.BOTTOM);
//                    textView.setDegrees(rotateAngle);
//                    textView.performClick();

                    //textView.setText("");
                    //textView.setRotation(90);

//                    ViewGroup.MarginLayoutParams paramsX=(ViewGroup.MarginLayoutParams) textView.getLayoutParams();
//
//                    paramsX.leftMargin=textView.getLeft();
//                    int right=0;
//                    int bottom=0;
//                    int screenWidth=texteditlayout.getHeight();
//                    int screenHeight=texteditlayout.getWidth();
//                    textViewWidth=textView.getWidth();
//                    textViewHeight=textView.getHeight();
//                    l= textView.getLeft();
//                    t=textView.getTop();
//                    r=textView.getRight();
//                    b=textView.getBottom();
//
//
//                    right=screenWidth-textView.getLeft()-textViewHeight;
//                    bottom=screenHeight-textView.getTop()-textViewWidth;
//                    paramsX.leftMargin=textView.getLeft();
//                    paramsX.topMargin=textView.getTop();
//                    paramsX.bottomMargin=bottom;
//                    paramsX.rightMargin=right;
//
//
//                    textView.setLayoutParams(paramsX);
//
//                    ViewGroup.LayoutParams params=textView.getLayoutParams();
//
//                    //paint.setTextSize(BarcodeActivity.textSize);
//
//                    //int textMaxWidth=(int)(paint.measureText(BarcodeActivity.testString));
//
//
//                        params.width=textViewHeight;
//
//                        params.height = textViewWidth;
//
//                    textView.setLayoutParams(params);
                //textView.setEms(4);


                    //textView.setRotation(90);

////=========================旋转后新坐标==============================
//                    double x0=l+textViewWidth*1.0/2;
//                    double y0=t0+textViewHeight*1.0/2;
////                    double a=Math.PI/180*rotateAngle;
//////                    newX=x0+(x1-x0)*Math.cos(a)-(y1-y0)*Math.sin(a);
//////
//////                    newY=y1+(x1-x0)*Math.sin(a)+(y1-y0)*Math.cos(a);
////                    double newL=x0+(l-x0)*Math.cos(a)-(t-y0)*Math.sin(a);
////                    double newT=t+(l-x0)*Math.sin(a)+(t-y0)*Math.cos(a);
////
////                    double newR=x0+(r-x0)*Math.cos(a)-(b-y0)*Math.sin(a);
////                    double newB=b+(r-x0)*Math.sin(a)+(b-y0)*Math.cos(a);
//
//
//
//                    double rotation = rotateAngle * Math.PI / 180;
//                    double newL =156;// (l - x0) * Math.cos(rotation) - (t - y0)*Math.sin(rotation) + x0;
//                    double newT = -156;//(l - x0) * Math.sin(rotation) + (t - y0)*Math.cos(rotation) + y0;
//
//                    double newR =468+156;// (r - x0) * Math.cos(rotation) - (b - y0)*Math.sin(rotation) + x0;
//                    double newB =468+156;// (r - x0) * Math.sin(rotation) + (b - y0)*Math.cos(rotation) + y0;
//
//
//                    int screenWidth=texteditlayout.getHeight();
//                    int screenHeight=texteditlayout.getWidth();
//                    newR=screenWidth-newR;
//                    newB=screenHeight-newB;
////===================================================================
//
//
//
//
//                    ViewGroup.MarginLayoutParams paramsT=(ViewGroup.MarginLayoutParams) textView.getLayoutParams();
//
//
//
//                    paramsT.leftMargin=(int)newL;
//                    paramsT.topMargin=(int)newT;
//                    paramsT.bottomMargin=(int)newB;
//                    paramsT.rightMargin=(int)newR;
//
//                    textView.setLayoutParams(paramsT);
////                    textView.setGravity(Gravity.LEFT|Gravity.TOP);



                } else if (getIdFlag == 2) {
                   // DrawView imageView = findViewById(getById);
                    ImageView view=findViewById(getById);
                    Bitmap bitmapM =((BitmapDrawable) ((ImageView) view).getDrawable()).getBitmap();
                    getPosition(bitmapM,view);
//                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//                    layoutParams.width = view.getHeight();
//                    layoutParams.height = view.getWidth();
//                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
//                    margin.leftMargin=0;
//                    margin.topMargin=0;
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
//                    layoutParams.width = view.getHeight();
//                    layoutParams.height = view.getWidth();
//                    view.setRotation(rotateAngle);
//
//                    view.setLayoutParams(layoutParams);


//                    float xxx=imageView.getPivotX();
//                    float yyy=imageView.getPivotY();
//                     imageView.setPivotX(yyy);
//                    imageView.setPivotY(xxx);

                }


//              final ImageView imageView=findViewById(getById);
//              imageView.setRotation(rotateAngle);


//                mAnimation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_angle);
//                mAnimation.setFillAfter(true);
//                imageView.setAnimation(mAnimation);
//                imageView.refreshDrawableState();


//                RotateAnimation rotate= (RotateAnimation) AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_angle);
//                //rotate.setFillAfter(true);
//                imageView.setAnimation(rotate);


            }
        });
//        textSizeseekBar.setProgress((int)textSize-10);
        textSizeseekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int p = 1;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub


                if (p < 1) {
                    p = 1;
                    textSizeseekBar.setProgress(p);

                }
                if (p > 100) {
                    p = 100;
                    textSizeseekBar.setProgress(p);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                touchFlag = 0;
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                touchFlag = 0;
                p = progress;
                if(getIdFlag==1) {
                    textSize = p + 10;
                    DragViewT textView = findViewById(getById);
                    textView.setTextSize(textSize);
                }
                //t1.setTextSize(p);
            }
        });
        maxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIdFlag==1){
                    DragViewT textView=findViewById(getById);
                    //float x=textView.getTextSize()/2;
                    float x=px2sp(getApplicationContext(),textView.getTextSize());
                    textSize=x+1;
                    textView.setTextSize(textSize);

                    Log.d("width", textView.getWidth()+"  "+textView.getHeight());

                    //maxView(textView);


                }
                else if(getIdFlag==2) {
                    ImageView imageView = findViewById(getById);
                    maxView(imageView);
                }
            }
        });

        minButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getIdFlag==1){
                    DragViewT textView=findViewById(getById);
                    //minView(textView);
                    //float getSize=textView.getTextSize();
                    //float fontScale=BarcodeActivity.this.getResources().getDisplayMetrics().scaledDensity;
                    float x=px2sp(getApplicationContext(),textView.getTextSize());


                    textSize=x-1;
                    textView.setTextSize(textSize);
                }
                else if(getIdFlag==2) {
                    ImageView imageView = findViewById(getById);
                    minView(imageView);
                }


//                TextView textView=findViewById(getById);
//                float x=textView.getScaleX();
//                float y=textView.getScaleY();
//                if(x<1||y<1)
//                {
//                    textView.setScaleX(1);
//                    textView.setScaleY(1);
//                }
//                else
//                {
//                    textView.setScaleX(x - 1);
//                    textView.setScaleY(y - 1);
//                }
            }
        });

        //===========


//        qrMake=findViewById(R.id.makeBarcode);
//        qrImage=findViewById(R.id.barcodeImage);
////        test=findViewById(R.id.test);
////        test.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent=new Intent(BarcodeActivity.this, RecyclerViewActivity.class);
////                startActivity(intent);
////            }
////        });
//        qrMake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makeQrCode();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getState();
        if (BlueToothMainActivity.socketall == null) {
            barcodeDeviceState.setVisibility(View.VISIBLE);
        } else {
            barcodeDeviceState.setVisibility(View.GONE);
        }
    }

    private float px2sp(Context context, float pxValue)
    {
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue/fontScale+0.5f);
    }

    private String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private void getPosition(Bitmap model,ImageView view){
//        float l = model.getLeft();
//        float  t = model.getTop();
//        float r = model.getLeft() + model.getWidth();
//        float b = model.getTop() + model.getHeight();
        int bitmapWidth=model.getWidth();
        int bitmapHeight=model.getHeight();
//        float[][] dst = new float[][]{{l,t},{r,t},{l,b},{r,b}};
//        float dst = float ArrayOf(
//                l, t,
//                r, t,
//                l, b,
//                r, b);
        Matrix matrix = new Matrix();
//        float cx = l + (r - l) / 2;
//        float cy = t + (b - t) / 2;
//       // matrix.postTranslate(model.translationX, model.translationY)
//        matrix.postRotate(90, cx, cy); // 以view的中心点旋转


        matrix.setRotate(90); //设置旋转
        //按照matrix的旋转构建新的Bitmap
        Bitmap bitmapcute = Bitmap.createBitmap(model, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        //绘制旋转之后的图像
        //Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmapcute);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int viewWidth=view.getHeight();
        int viewHeight=view.getWidth();
        layoutParams.width = view.getHeight();
        layoutParams.height = view.getWidth();
        view.setLayoutParams(layoutParams);

        view.setImageDrawable(bitmapDrawable);


        ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) view.getLayoutParams();

        params.leftMargin=view.getLeft();
        int right=0;
        int bottom=0;
        int screenWidth=texteditlayout.getHeight();
        int screenHeight=texteditlayout.getWidth();
        right=screenWidth-view.getLeft()-viewWidth;
        bottom=screenHeight-view.getTop()-viewHeight;
        params.leftMargin=view.getLeft();
        params.topMargin=view.getTop();
        params.bottomMargin=bottom;
        params.rightMargin=right;


        view.setLayoutParams(params);


        //matrix.postScale(model.scaleX, model.scaleY, cx, cy)
        //matrix.mapPoints(dst);
        //return dst;
    }



    public void maxView(View view) {
        float x = view.getScaleX();
        float y = view.getScaleY();
        if (x > 10 || y > 10) {
            view.setScaleX(10);
            view.setScaleY(10);
        } else {
            view.setScaleX((float) (x + 0.1));
            view.setScaleY((float) (y + 0.1));
        }
    }

    public void minView(View view) {
        float x = view.getScaleX();
        float y = view.getScaleY();
        if (x < 0.2 || y < 0.2) {
            view.setScaleX((float) 0.1);
            view.setScaleY((float) 0.1);
        } else {
            view.setScaleX((float) (x - 0.1));
            view.setScaleY((float) (y - 0.1));
        }
    }
    private void saveState()//存储数据到手机中
    {
        SharedPreferences sharedPref=this.getSharedPreferences("BarcodePrintSet3693", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putInt("BarcodeSizeWidth",barcodeSizeWidth);
        editor.putInt("BarcodeSizeHeight",barcodeSizeHeight);
        editor.putString("BarcodeFangXiang",BarcodeFangXiang);

        editor.commit();
    }
    private void getState()
    {
        SharedPreferences sp=getSharedPreferences("BarcodePrintSet3693", Context.MODE_PRIVATE);
        barcodeSizeWidth=sp.getInt("BarcodeSizeWidth",12);
        barcodeSizeHeight=sp.getInt("BarcodeSizeHeight",30);

        BarcodeFangXiang=sp.getString("BarcodeFangXiang","Horizontal");
         sp = getSharedPreferences("PDFPrintSet3693", Context.MODE_PRIVATE);
        PrintSpeed = sp.getInt("PrintSpeed", 2) + 1;
        PrintDensity = sp.getInt("PrintDensity", 9) + 1;
        PrintContrast = sp.getInt("PrintContrast", 60) + 120;
//        PrintHalftone=sp.getBoolean("PrintHalftone",true);
//        PrintHalftoneS=sp.getString("PrintHalftoneString",PrintHalftone_String[0]);
//        PrintHalftoneNumber_int=sp.getInt("PrintHalftoneNumber_int",50);

    }
    private void getMaxSize(){
        String nameBlue=BlueToothMainActivity.BlueName;
        if(nameBlue.length()>0)
        {
            String nameNow=nameBlue.substring(0,2);
            if(nameNow.equals("AC"))
            {
                //barcodeMaxSizeHeight=30;
                barcodeSizeWidth=12;
                barcodeSizeHeight=30;
                BarcodeFangXiang="Horizontal";//Vertical

                saveState();
            }
            else
            {

            }
        }
    }
    private void DoubleClickImageView(final ImageView  imageView)
    {
        SecondClick=(int)System.currentTimeMillis();
        if(popupWindow != null && popupWindow.isShowing())
        {
            return;
        }
        if((SecondClick-FirstClick<800)&& CountClick==0) {
            if (oldId == 0) {


            } else {
                if (dateFlag == 0) {

                    if (idFlag == 1) {

                        imageView.setBackgroundResource(0);

                    } else if (idFlag == 2) {
                        DragViewX imageView1 = findViewById(oldId);
                        imageView.setBackgroundResource(0);
//                                imageView1.setBackground(null);
                    }

                }
            }
            idFlag=2;
            oldId=imageView.getId();
            int posNow=5;
            String abc=qrcodeData[getById];
            String xx=qrcodeData[getById];
            xx=qrcodeData[getById].substring(0,1);
            xx=qrcodeData[getById].substring(1,1);

            if(qrcodeData[getById].substring(0,1).equals("1"))
            {
                posNow=5;
            }
            else
            {
                posNow=6;
            }



            final int pos =posNow;
            View view = getLayoutInflater().inflate(R.layout.popupwindow_inputbarcode, null);
            final EditText editText = view.findViewById(R.id.popupWindowBarcodeInput);
            TextView textMakesure=view.findViewById(R.id.textMakesure);

            final Button buttonR=view.findViewById(R.id.QRSetReduce);
            final Button buttonA=view.findViewById(R.id.QRSetAdd);
            final TextView textViewD=view.findViewById(R.id.QRSetNumber);
            final CheckBox checkBox=view.findViewById(R.id.QRCheckS);

            final LinearLayout linearLayout=view.findViewById(R.id.QRinputLayout);
            final LinearLayout linearLayout1=view.findViewById(R.id.QRSetLayout);

            if(qrcodeData[getById].substring(1,2).equals("1"))
            {
                checkBox.setChecked(true);
            }
            else
            {
                checkBox.setChecked(false);
            }
            //1000
            String[] s1=qrcodeData[getById].split("[+]");

            if(qrcodeData[getById].substring(2,3).equals("1"))
            {
                textViewD.setText(s1[0].substring(3,s1[0].length()));
            }
            else
            {
                textViewD.setText("-"+s1[0].substring(3,s1[0].length()));
            }

            linearLayout1.setVisibility(View.INVISIBLE);
            QRimage1  = view.findViewById(R.id.barcodeInput);
            QRimage2  = view.findViewById(R.id.barcodeXulie);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //params.width =200;

            params.height =150;
            linearLayout.setLayoutParams(params);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params1.height =0;
            linearLayout1.setLayoutParams(params1);
            view.findViewById(R.id.barcodeInput).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    QRimage1.setImageResource(R.drawable.zgtextinput);
                    QRimage2.setImageResource(R.drawable.xulieblack);
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //params.width =200;

                    params.height =150;
                    linearLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params1.height =0;
                    linearLayout1.setLayoutParams(params1);
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);


                }
            });
            view.findViewById(R.id.barcodeXulie).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    QRimage1.setImageResource(R.drawable.zgtextinput2);
                    QRimage2.setImageResource(R.drawable.xulie);
                    linearLayout.setVisibility(View.INVISIBLE);
                    linearLayout1.setVisibility(View.VISIBLE);


                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //params.width =200;

                    params.height =0;
                    linearLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    //params.width =200;
                    params1.height =700;

                    linearLayout1.setLayoutParams(params1);
                    final int width = getWidthInPx(BarcodeActivity.this);
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);


                    buttonR.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int X=Integer.parseInt(textViewD.getText().toString());
                            X--;

                            textViewD.setText(String.valueOf(X));
                        }
                    });
                    buttonA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int X=Integer.parseInt(textViewD.getText().toString());
                            X++;

                            textViewD.setText(String.valueOf(X));
                        }
                    });
//                            Timer timer = new Timer();
//                            timer.schedule(new TimerTask() {
//                                public void run() {
//                                    scrollView.scrollTo(width, 0);
//
//
//                                }
//                            }, 200);
//
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            boolean isOpen = imm.isActive();
//                            if (isOpen) {
//                                // imm.toggleSoftInput(0,
//                                // InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
//                                imm.hideSoftInputFromWindow(edt_pass.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                            }
                }
            });
            textMakesure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String xx=qrcodeData[getById];//2000+
                    String[] xyz=xx.split("[+]");
                    int l1=xyz[0].length()+1;
                    String s1=xx.substring(l1,xx.length());
                    int xy=Integer.parseInt(textViewD.getText().toString());
                    String s4="0";
                    if(checkBox.isChecked())
                    {
                        s4="1";
                    }
                    else
                    {
                        s4="0";
                    }

                    String s2="";
                    if(xy>0)
                    {
                        s2=xyz[0].substring(0,1).toString()+s4+"1"+xy+"+";

                    }
                    else if(xy==0)
                    {
                        s2=xyz[0].substring(0,1).toString()+s4+"1"+xy+"+";
                    }
                    else
                    {
                        String s3=String.valueOf(xy);
                        s3=s3.substring(1,s3.length());
                        s2=xyz[0].substring(0,1).toString()+s4+"0"+s3+"+";
                    }
                    qrcodeData[getById]=s2+s1;



                    popupWindow.dismiss();
                }
            });
            String[] s2=qrcodeData[getById].split("[+]");
            int s3=s2[0].length()+1;
            editText.setText(qrcodeData[getById].substring(s3,qrcodeData[getById].length()));
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);

            editText.setSelection(editText.length());
            editText.requestFocus();




            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    ImageView imageView = findViewById(getById);

                    String[] xx=qrcodeData[getById].split("[+]");




                    if (!s.toString().equals("")) {
                        if (pos == 6) {
                            //qrcodeData[getById]="2"+s.toString();
                            qrcodeData[getById]=xx[0]+"+"+s.toString();//2
                            makeQrCode(s.toString());

                            //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                        } else if (pos == 5) {
                            //qrImage = encodeAsBitmap(s.toString(), BarcodeFormat.CODE_128, 350, 150);
                            //qrcodeData[getById]="1"+s.toString();
                            qrcodeData[getById]=xx[0]+"+"+s.toString();//1
                            qrImage=creatBarcode(getApplicationContext(), s.toString(), 350, 150, true);

                        }

                        //makeQrCode(s.toString());

                        imageView.setImageBitmap(qrImage);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
            makePopupWiondo(view);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                }
            }, 200);

        }
        CountClick--;
        if(CountClick<0)
            CountClick=0;
        FirstClick=(int)System.currentTimeMillis();

    }
    private void DoubleClickView(final TextView textView){
        SecondClick=(int)System.currentTimeMillis();
        if(popupWindow != null && popupWindow.isShowing())
        {
            return;
        }
        if((SecondClick-FirstClick<800)&& CountClick==0)
        {
            if(oldId==0)
            {


            }
            else
            {
                if(dateFlag==0) {

                    if (idFlag == 1) {

                        textView.setBackgroundResource(0);

                    } else if (idFlag == 2) {
                        DragViewX imageView1 = findViewById(oldId);
                        imageView1.setBackgroundResource(0);
//                                imageView1.setBackground(null);
                    }

                }
            }
            idFlag=1;
            oldId=textView.getId();
            final View wh = LayoutInflater.from(this).inflate(R.layout.popupwindow_inputtext, null);
            final EditText editText = wh.findViewById(R.id.popupWindowET_1);
            scrollView = wh.findViewById(R.id.textscrollViewT1);
            layout1 = wh.findViewById(R.id.layoutT1);
            layout2 = wh.findViewById(R.id.layoutT2);
            imageE1 = wh.findViewById(R.id.textKeyboard);
            imageE2 = wh.findViewById(R.id.textStyle);
            TextView confirmTextView=wh.findViewById(R.id.textMakesure1);
            if(BarcodeActivity.keybordFlag==0) {
                editText.setText(textView.getText().toString());
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);

                editText.setSelection(editText.length());
                editText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
                //makePopupWiondo(view);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    }
                }, 300);
            }
//                    if(keybordFlag==0)
//                        scrollView.scrollTo(0, 0);
//                    else
//                        scrollView.scrollTo(width+width, 0);
//            final View wh = LayoutInflater.from(this).inflate(R.layout.popupwindow_inputtext, null);
//
//            scrollView = wh.findViewById(R.id.textscrollViewT1);
//            layout1 = wh.findViewById(R.id.layoutT1);
//            layout2 = wh.findViewById(R.id.layoutT2);
//            imageE1 = wh.findViewById(R.id.textKeyboard);
//            imageE2 = wh.findViewById(R.id.textStyle);
            //TextView confirmTextView=wh.findViewById(R.id.textMakesure1);
            confirmTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            //textView=wh.findViewById(R.id.Xx);
            //textView.setText("ssssssss");
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            final int width = getWidthInPx(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, 150);
            //LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

            ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
            ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
            params1.width = width;
            params2.width = width;
            layout1.setLayoutParams(layoutParams);
            layout2.setLayoutParams(layoutParams);

            //final ImageView imageView=wh.findViewById(R.id.imageGraffitiT2);

            //=============

             //editText = wh.findViewById(R.id.popupWindowET_1);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    TextView textView = findViewById(getById);
                    //DragViewT textView=findViewById(getById);
                    testString=s.toString();
                    textView.setText(s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);

            editText.requestFocus();
            // if (keybordFlag == 0) {
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
            //makePopupWiondo(view);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    //editText.setFocusableInTouchMode(true);
                    //editText.setFocusable(true);
                    //editText.requestFocus();
                    //InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputManager.showSoftInput(editText, 0);
                    InputMethodManager imm=(InputMethodManager)wh.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);


                }
            }, 200);
            // }




           // makePopupWiondo(wh);
            //===================

            //final SeekBar sk = wh.findViewById(R.id.seekBarT1);
            final TextView t1 = wh.findViewById(R.id.textT21);
            final ImageView i1=wh.findViewById(R.id.textCu);
            final TextView t2 = wh.findViewById(R.id.textT22);
            final ImageView i2=wh.findViewById(R.id.textxie);
            final TextView t3 = wh.findViewById(R.id.textT23);
            final ImageView i3=wh.findViewById(R.id.texthua);

            final LinearLayout textT21layout=wh.findViewById(R.id.textT21layout);
            final LinearLayout textT22layout=wh.findViewById(R.id.textT22layout);
            final LinearLayout textT23layout=wh.findViewById(R.id.textT23layout);
            final TextView textView1 = wh.findViewById(R.id.textT1);
            final TextView textView3 = wh.findViewById(R.id.textIT3);
            final TextView textView4 = wh.findViewById(R.id.textIT4);
            final TextView textView5 = wh.findViewById(R.id.textIT5);
            final TextView textView6 = wh.findViewById(R.id.textIT6);
            final TextView textView7 = wh.findViewById(R.id.textIT7);
            textView1.setTextColor(Color.parseColor("#fbb80e"));
            textT21layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BlodFlag=!BlodFlag;
                    if(BlodFlag==true &&ItalicFlag==false)
                    {
                        t1.setTextColor(Color.parseColor("#fbb80e"));
                        i1.setImageResource(R.drawable.zgtextcu2);
                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {
                        t1.setTextColor(Color.BLACK);
                        i1.setImageResource(R.drawable.zgtextcu1);
                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {
                        t1.setTextColor(Color.parseColor("#fbb80e"));
                        i1.setImageResource(R.drawable.zgtextcu1);
                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {
                        t1.setTextColor(Color.BLACK);
                        i1.setImageResource(R.drawable.zgtextcu1);
                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }



                }
            });
            textT22layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItalicFlag=!ItalicFlag;
                    if(BlodFlag==true &&ItalicFlag==false)
                    {
                        t2.setTextColor(Color.BLACK);
                        i2.setImageResource(R.drawable.zgtextxie1);
                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {
                        t2.setTextColor(Color.parseColor("#fbb80e"));
                        i2.setImageResource(R.drawable.zgtextxie2);
                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {
                        t2.setTextColor(Color.parseColor("#fbb80e"));
                        i2.setImageResource(R.drawable.zgtextxie2);
                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {
                        t2.setTextColor(Color.BLACK);
                        i2.setImageResource(R.drawable.zgtextxie1);
                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });
            textT23layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UnderlineFlag=!UnderlineFlag;
                    if(UnderlineFlag)
                    {
                        t3.setTextColor(Color.parseColor("#fbb80e"));
                        i3.setImageResource(R.drawable.zgtexthua2);
                        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                        textView.getPaint().setAntiAlias(true);

                        ;

//                    textView.setTypeface(BlodType,Typeface.BOLD);
//                    textView.invalidate();
                    }
                    else
                    {
                        t3.setTextColor(Color.BLACK);
                        i3.setImageResource(R.drawable.zgtexthua1);
                        textView.getPaint().setFlags(0);
//                    textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                    textView.invalidate();

                    if(BlodFlag==true &&ItalicFlag==false )
                    {
                        t2.setTextColor(Color.BLACK);
                        i2.setImageResource(R.drawable.zgtextxie1);
                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {
                        t2.setTextColor(Color.parseColor("#fbb80e"));
                        i2.setImageResource(R.drawable.zgtextxie2);
                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {
                        t2.setTextColor(Color.parseColor("#fbb80e"));
                        i2.setImageResource(R.drawable.zgtextxie2);
                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {
                        t2.setTextColor(Color.BLACK);
                        i2.setImageResource(R.drawable.zgtextxie1);
                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }


                }
            });

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView1.setTextColor(Color.parseColor("#fbb80e"));
                    textView3.setTextColor(Color.BLACK);
                    textView4.setTextColor(Color.BLACK);
                    textView5.setTextColor(Color.BLACK);
                    textView6.setTextColor(Color.BLACK);
                    textView7.setTextColor(Color.BLACK);

                    //DragViewT textview=findViewById(getById);
                    BlodType=Typeface.SANS_SERIF;
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }


                }
            });


            textView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView3.setTextColor(Color.parseColor("#fbb80e"));
                    textView1.setTextColor(Color.BLACK);
                    textView4.setTextColor(Color.BLACK);
                    textView5.setTextColor(Color.BLACK);
                    textView6.setTextColor(Color.BLACK);
                    textView7.setTextColor(Color.BLACK);
                    //DragViewT textview=findViewById(getById);

                    BlodType=Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });

            textView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView4.setTextColor(Color.parseColor("#fbb80e"));
                    textView3.setTextColor(Color.BLACK);
                    textView1.setTextColor(Color.BLACK);
                    textView5.setTextColor(Color.BLACK);
                    textView6.setTextColor(Color.BLACK);
                    textView7.setTextColor(Color.BLACK);
                    //DragViewT textview=findViewById(getById);
                    BlodType=Typeface.createFromAsset(getAssets(), "fonts/hkwwt.ttf");
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });

            textView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView5.setTextColor(Color.parseColor("#fbb80e"));
                    textView3.setTextColor(Color.BLACK);
                    textView4.setTextColor(Color.BLACK);
                    textView1.setTextColor(Color.BLACK);
                    textView6.setTextColor(Color.BLACK);
                    textView7.setTextColor(Color.BLACK);
                    //DragViewT textview=findViewById(getById);
                    BlodType=Typeface.createFromAsset(getAssets(), "fonts/simhei.ttf");
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });

            textView6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView6.setTextColor(getResources().getColor(R.color.bacgroundColor));
                    textView3.setTextColor(Color.BLACK);
                    textView4.setTextColor(Color.BLACK);
                    textView5.setTextColor(Color.BLACK);
                    textView1.setTextColor(Color.BLACK);
                    textView7.setTextColor(Color.BLACK);
                    //DragViewT textview=findViewById(getById);
                    BlodType=Typeface.createFromAsset(getAssets(), "fonts/stliti.ttf");
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });

            textView7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView7.setTextColor(Color.parseColor("#fbb80e"));
                    textView3.setTextColor(Color.BLACK);
                    textView4.setTextColor(Color.BLACK);
                    textView5.setTextColor(Color.BLACK);
                    textView6.setTextColor(Color.BLACK);
                    textView1.setTextColor(Color.BLACK);
                    //DragViewT textview=findViewById(getById);
                    BlodType=Typeface.createFromAsset(getAssets(), "fonts/ygyxsziti.ttf");
                    if(BlodFlag==true &&ItalicFlag==false)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD);
                        textView.invalidate();
                    }
                    else if(BlodFlag==false &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.ITALIC);
                    }
                    else if(BlodFlag==true &&ItalicFlag==true)
                    {

                        textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                    }
                    else
                    {

                        textView.setTypeface(BlodType,Typeface.NORMAL);
                    }
                }
            });
            textView1.setTypeface(Typeface.SANS_SERIF);
            textView3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf"));
            textView4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/hkwwt.ttf"));
            textView5.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/simhei.ttf"));
            textView6.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/stliti.ttf"));
            textView7.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ygyxsziti.ttf"));
//            sk.setProgress((int)textSize-10);
//            sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                int p = 1;
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//                    // TODO Auto-generated method stub
//
//
//                    if (p < 1) {
//                        p = 1;
//                        sk.setProgress(p);
//
//                    }
//                    if (p > 100) {
//                        p = 100;
//                        sk.setProgress(p);
//
//                    }
//
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//                    touchFlag = 0;
//                    // TODO Auto-generated method stub
//                }
//
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                    // TODO Auto-generated method stub
//                    touchFlag = 0;
//                    p = progress;
//                    textSize=p+10;
//                    textView.setTextSize(textSize);
//                    //t1.setTextSize(p);
//                }
//            });



            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;

                }

            });

            wh.findViewById(R.id.textKeyboard).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    keybordFlag = 0;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, 150);


                    ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                    ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                    params1.width = width;
                    params2.width = width;
                    layout1.setLayoutParams(layoutParams);
                    layout2.setLayoutParams(layoutParams);
                    imageE1.setImageResource(R.drawable.zgtextinput);
                    imageE2.setImageResource(R.drawable.zgtextstyle2);
                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);


                    scrollView.scrollTo(0, 0);

                }
            });
            wh.findViewById(R.id.textStyle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keybordFlag = 1;
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(imm.isActive()&&getCurrentFocus()!=null){
//                    if (getCurrentFocus().getWindowToken()!=null) {
//                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }

                    //EditText edit=(EditText)findViewById(R.id.edit);


                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);


                    //ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                    ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                    //params1.width = width;
                    params2.width = width;
                    // layout1.setLayoutParams(layoutParams);
                    layout2.setLayoutParams(layoutParams);
                    imageE1.setImageResource(R.drawable.zgtextinput2);
                    imageE2.setImageResource(R.drawable.zgtextstyle);



                    InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(editText, 0);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            scrollView.scrollTo(width, 0);


                        }
                    }, 200);


                }
            });
            makePopupWiondo(wh);
        }
        CountClick--;
        if(CountClick<0)
            CountClick=0;
        FirstClick=(int)System.currentTimeMillis();






    }
    public void makePopupWiondo(View view) {
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true );
          popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.anim_bottom_pop);


//        popupWindow.getContentView().setFocusable(true);
//        popupWindow.getContentView().setFocusableInTouchMode(true);
//        popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    popupWindow.dismiss();
//
//                    return true;
//                }
//                return false;
//            }
//        });
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                final int x = (int) event.getX();
//                final int y = (int) event.getY();
//
//                if ((event.getAction() == MotionEvent.ACTION_DOWN)
//                        && ((x < 0) || (x >= 100) || (y < 0) || (y >= 200))) {
//                    // donothing
//                    // 消费事件
//                    return true;
//                } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    //Log.e(TAG,"out side ...");
//                    return true;
//                }
//                return false;
//            }
//        });
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(v instanceof TextView)
                {
                    String xxx=((TextView) v).getText().toString();
                    int yyy=v.getId();
                    xxx=xxx;
                }
                else if(v instanceof ScrollViewTouchView)
                {
                    int yyy=v.getId();
                    yyy=yyy;
                }
                else if(v instanceof RelativeLayout)
                {
                    int yyy=v.getId();
                    yyy=yyy;
                }
                else
                {
                    String yyy=String.valueOf(v.getId());
                    yyy=yyy;
                }

                int xxxx=event.getAction();
                long xx=v.getId();
                float yyy=event.getY();
                if(event.getY()<0)
                {
                    return true;
                }
                else
                {return false;

                }


            }
        });



    }

    public void makeQrCode(String makeText) {
        int xWidth=barcodeSizeWidth;
        int xHeight=barcodeSizeHeight;

        int bWidth= texteditlayout.getWidth();
        int bHeigth= texteditlayout.getHeight();
        double xishu=bWidth*1.0/xWidth*1.0;
        double nowWidht=10*xishu;
        int widthPix = (int)nowWidht;
        int heightPix = (int)nowWidht;
        Bitmap bitmapX;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            bitMatrix = new QRCodeWriter().encode(makeText, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
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
                    pixels[y * widthPix + x] = 0x00ffffff;
                }
            }
        }

        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

        //                if (logoBm != null) {
        //                    bitmap = addLogo(bitmap, logoBm);
        //                }


        qrImage = bitmap;
    }
    public void makeQrCodeC(String makeText,int widthPix,int heightPix ) {
        int xWidth=barcodeSizeWidth;
        int xHeight=barcodeSizeHeight;

//        int bWidth= texteditlayout.getWidth();
//        int bHeigth= texteditlayout.getHeight();
//        double xishu=bWidth*1.0/xWidth*1.0;
//        double nowWidht=10*xishu;
//        int widthPix = (int)nowWidht;
//        int heightPix = (int)nowWidht;
        Bitmap bitmapX;
        BitMatrix bitMatrix = null;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 0);
            bitMatrix = new QRCodeWriter().encode(makeText, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
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
                    pixels[y * widthPix + x] = 0x00ffffff;
                }
            }
        }

        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

        //                if (logoBm != null) {
        //                    bitmap = addLogo(bitmap, logoBm);
        //                }


        qrImage = bitmap;
    }
    public void moveImage(final View view, final int idFlag) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        getById = v.getId();
                        getIdFlag = idFlag;
                        if (idFlag == 1) {
                            view.setStateListAnimator(null);
                            view.bringToFront();
                        }
                        //Toast.makeText(BarcodeActivity.this, v.getWidth() + "x" + v.getHeight() + "y", Toast.LENGTH_SHORT).show();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
                        float distanceX = lastX - event.getRawX();
                        float distanceY = lastY - event.getRawY();

                        float nextY = view.getY() - distanceY;
                        float nextX = view.getX() - distanceX;

                        // 不能移出屏幕
                        if (nextY < 0) {
                            nextY = 0;
                        } else if (nextY > containerHeight - view.getHeight()) {
                            nextY = containerHeight - view.getHeight();
                        }
                        if (nextX < 0)
                            nextX = 0;
                        else if (nextX > containerWidth - view.getWidth())
                            nextX = containerWidth - view.getWidth();

                        // 属性动画移动
                        ObjectAnimator y = ObjectAnimator.ofFloat(view, "y", view.getY(), nextY);
                        ObjectAnimator x = ObjectAnimator.ofFloat(view, "x", view.getX(), nextX);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(x, y);
                        animatorSet.setDuration(0);
                        animatorSet.start();

                        lastX = event.getRawX();
                        lastY = event.getRawY();
//                                imageView.setY(nextY);
//
//                                imageView.setX(nextX);
                }

                return false;
            }
        });
    }

    public void getViewHeight(final View view) {
        int height = view.getHeight();

        Log.d("Debug", "oncreat获取高度：" + height);//获取的高度为0 ，所以不能直接去获取高度
        view.post(new Runnable() {
            @Override
            public void run() {
                int height = view.getHeight();
                int width = view.getWidth();
                DrawHeight=height;
                DrawWidth=width;
                //ViewHight=view.getHeight();
                bitcopy = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                //bitcopy= Bitmap.createBitmap(width,height,bitsrc.getConfig());
                //bitcopy=Bitmap.createBitmap(bitsrc);
                //bitcopy=bitsrc;
                paint = new Paint();
                // 3.创建画板对象，把白纸铺在画板上
                paint.setStrokeWidth(15);
                canvas = new Canvas(bitcopy);
                //paint.setTextSize(50);
                canvas.drawBitmap(bitcopy, new Matrix(), paint);
                Log.d("Debug", "post中获取高度：" + height);

            }
        });

        //return  ViewHight;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 这里来获取容器的宽和高
        if (hasFocus) {
            containerHeight = texteditlayout.getHeight();
            containerWidth = texteditlayout.getWidth();
        }
    }


    //========================================
    public static Bitmap creatBarcode(Context context, String contents,
                                      int desiredWidth, int desiredHeight, boolean displayCode) {
        Bitmap ruseltBitmap = null;
        int marginW = 20;
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        if (displayCode) {
            Bitmap barcodeBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
//            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth + 2
//                    * marginW, desiredHeight, context);
            Bitmap codeBitmap = creatCodeBitmap(contents, desiredWidth , desiredHeight, context);
            ruseltBitmap = mixtureBitmap(barcodeBitmap, codeBitmap, new PointF(
                    0, desiredHeight));
        } else {
            ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                    desiredWidth, desiredHeight);
        }

        return ruseltBitmap;
    }


    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {

        //BarcodeFormat barcodeFormat=BarcodeFormat.CODE_128;

        final int WHITE = 0x00FFFFFF;
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


    protected static Bitmap creatCodeBitmap(String contents, int width,
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


    protected static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
                                          PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        int marginW = 20;
        int width=100;
        if(first.getWidth()>second.getWidth())
            width=first.getWidth();
        else
            width=second.getWidth();

        Bitmap newBitmap = Bitmap.createBitmap(
                width,
                first.getHeight() + second.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        //int x=Canvas.ALL_SAVE_FLAG;
        cv.save();
        cv.restore(

        );

        return newBitmap;
    }

    private synchronized void scanImageData(){
        if (mIsFinishSearchImage || mIsScanning) {
            return;
        }
        mIsScanning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = BarcodeActivity.this.getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC"); // 日期降序排序

                mCursor.moveToPosition(mCursorPosition); // 从上一次的扫描位置继续扫描
                int i = 0;
                String path;
                while (mCursor.moveToNext() && i < CURSOR_COUNT) {
                    i++;
                    // 获取图片的路径
                    path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    if (new File(path).exists()) {
                        mPathList.add(path);
                    }
                }
                mCursor.close();
                mCursorPosition += i;
                mIsScanning = false;
                if (i < CURSOR_COUNT) { // 扫描完了所有图片
                    mIsFinishSearchImage = true;
                }
                //loadImageAdapter.refreshPathList(mPathList,mPathList.size());
                nHandler.sendEmptyMessage(WHAT_REFRESH_PATH_LIST);

            }
        }).start();
    }
    private void getImages()
    {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = BarcodeActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                 mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");
                int imagecount=mCursor.getColumnCount();
                int i=0;
                String path;
                nHandler.sendEmptyMessage(WHAT_REFRESH_PATH_LIST);
                //mCursor.moveToPosition(mCursorPosition); // 从上一次的扫描位置继续扫描
//                while (mCursor.moveToNext())
//                {
//                    i++;
//                    // 获取图片的路径
//                     path = mCursor.getString(mCursor
//                            .getColumnIndex(MediaStore.Images.Media.DATA));
//
//                    if (new File(path).exists()) {
//                        mPathList.add(path);
//                    }
//
//
//                    // 获取该图片的父路径名
//                    File parentFile = new File(path).getParentFile();
//                    String dirPath = parentFile.getAbsolutePath();
//
//                    //利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
//                    if(mDirPaths.contains(dirPath))
//                    {
//                        continue;
//                    }
//                    else
//                    {
//                        mDirPaths.add(dirPath);
//                        mPathNameList.add(parentFile);
//
//                    }
//
//
//
//
////                    int picSize = parentFile.list(new FilenameFilter()
////                    {
////                        @Override
////                        public boolean accept(File dir, String filename)
////                        {
////                            if (filename.endsWith(".jpg")||filename.endsWith(".png"))
////                                return true;
////                            return false;
////                        }
////                    }).length;
////                    if (picSize > mPicsSize)
////                    {
////                        mPicsSize = picSize;
////                        mImgDir = parentFile;
////                    }
//                }
//                mCursor.close();
//                mCursorPosition += i;
//                //扫描完成，辅助的HashSet也就可以释放内存了
//                mDirPaths = new HashSet<String>() ;
//                // 通知Handler扫描图片完成
//                for(int j=0;j<mPathNameList.size()-1;j++) {
//
//                    nHandler.sendEmptyMessage(j);
//                }

            }
        }).start();

    }
    private void LoadTradeMarkPopuWindwo()
    {
        final View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_loadcion, null);

        iconListTitle = new ArrayList<String>();
        iconListTitle.clear();
        iconListTitle.add("50-27");
        iconListTitle.add("50-35");
        iconListTitle.add("50-40");
        iconListTitle.add("35-19");
        iconListTitle.add("40-20");
//        iconListTitle.add("交通");
//        iconListTitle.add("节庆");
//        iconListTitle.add("美食");
//        iconListTitle.add("美妆");
//        iconListTitle.add("天气");
//        iconListTitle.add("玩具");
//        iconListTitle.add("网络社交");
//        iconListTitle.add("文具");
//        iconListTitle.add("文字符号");
//        iconListTitle.add("运动");
//        iconListTitle.add("植物");
//        iconListTitle.add("边框");

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        TextView loadImageMakeSure=view.findViewById(R.id.loadIconMakeSure);
        loadImageMakeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        IconTitleLayout = view.findViewById(R.id.IconTitleLayout);
        IconTitleLayout.setLayoutManager(new LinearLayoutManager(BarcodeActivity.this,RecyclerView.HORIZONTAL,false));
        //IconTitleLayout.addItemDecoration(new MyDecoration());
        iconLinearAdapter=new IconLinearAdapter(BarcodeActivity.this,0, new IconLinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                //Toast.makeText(BarcodeActivity.this,"You Click"+pos,Toast.LENGTH_SHORT).show();
                //iconLinearAdapter.RefreshIconTitleList(iconListTitle, iconListTitle.size(),pos);
                Bundle bundle=new Bundle();
                bundle.putInt("iconTitleColor",pos);
                titlePos=pos;
                Message message=new Message();
                message.setData(bundle);
                message.what=2;
                nHandler.sendMessage(message);

                getAssetsPathList.clear();
                //===================================
                try {

                    mAssetManager = getAssets();

                    String[] mAssetsImageList;
                    mAssetsImageList=mAssetManager.list("trademark/"+String.valueOf(pos));
                    for (int i = 0; i < mAssetsImageList.length; i++) {
                        String imagePath = "trademark/"+String.valueOf(pos) + "/" + mAssetsImageList[i];
                        getAssetsPathList.add(imagePath);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                nHandler.sendEmptyMessage(3);



            }
        });
        IconTitleLayout.setAdapter(iconLinearAdapter);

        nHandler.sendEmptyMessage(2);
        getAssetsPathList = new ArrayList<String>();
        getAssetsPathList.clear();

        //===================================
        try {

            mAssetManager = getAssets();
            String[] mAssetsImageList;
            mAssetsImageList=mAssetManager.list("trademark/"+String.valueOf(0));
            for (int i = 0; i < mAssetsImageList.length; i++) {
                String imagePath = "trademark/"+String.valueOf(0) + "/" + mAssetsImageList[i];
                getAssetsPathList.add(imagePath);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadIconRecycle = view.findViewById(R.id.loadIconRecycle);
        loadIconRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));

        loadIconAdapter = new LoadIconAdapter(BarcodeActivity.this,getAssetsPathList,getAssetsPathList.size(),mAssetManager, new LoadIconAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
               //==========================
                //=================================================
                Bitmap bitmapX=loadImage(getAssetsPathList.get(pos));
                String getFrameSize[];

                getFrameSize=iconListTitle.get(titlePos).split("-");


                texteditlayout.setBackground(null);

                barcodeSizeWidth=Integer.parseInt(getFrameSize[0]);
                barcodeSizeHeight=Integer.parseInt(getFrameSize[1]);

                int x=scrollViewBarcode.getHeight();
                int y=scrollViewBarcode.getWidth();
                int a=texteditlayout.getHeight();
                int b=texteditlayout.getWidth();

                Log.d("TextLayout", x+"  "+y+"  "+a+"  "+b);
                SetTextLayoutSize(PhoneWidth,barcodeSizeWidth,barcodeSizeHeight);
                saveState();
                texteditlayout.setBackground(new BitmapDrawable(getResources(),bitmapX));
                backgroundBitmap=bitmapX;
                 x=scrollViewBarcode.getHeight();
                 y=scrollViewBarcode.getWidth();
                 a=texteditlayout.getHeight();
                 b=texteditlayout.getWidth();
                Log.d("TextLayout", x+"  "+y+"  "+a+"  "+b);


                //=============================





//                //==============
//                final DragViewX imageView=new DragViewX(texteditlayout.getContext());
//                getById=id;
//                getIdFlag = 2;
//                imageView.setId(id++);
//
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if(oldId==0)
//                        {
//
//
//                        }
//                        else
//                        {
//
//                            if (oldId!=getById) {
//                                if (idFlag == 1) {
//                                    DragViewT textView = findViewById(oldId);
//                                    textView.setBackgroundResource(0);
//
//                                } else if (idFlag == 2) {
//                                    DragViewX imageView1 = findViewById(oldId);
//                                    imageView1.setBackgroundResource(0);
////                                                            imageView1.setBackground(null);
//                                }
//                            }
//
//                        }
//                        idFlag=2;
//                        oldId=v.getId();
//                        //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                });
//
////                                    imageView.setX(0);
////                                    imageView.setY(0);
////
////                                    imageView.setScaleX((float) 0.5);
////                                    imageView.setScaleY((float) 0.5);
//////                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
//////                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
////
////                    //moveImage(imageView, 2);
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                //imageView.setImageURI(Uri.fromFile(new File(mPathList.get(pos))));
//
//
////                        //==================只获取图片的宽高================================================
////                        BitmapFactory.Options options = new BitmapFactory.Options();
////
////                        options.inJustDecodeBounds = true;
////
////                       Bitmap bitmapX = BitmapFactory.decodeFile(getAssetsPathList.get(pos), options);
////
////                       // Bitmap bitmapX=loadImage(getAssetsPathList.get(pos));
////
////
////                        options.inSampleSize = 1;
////
////                        options.inJustDecodeBounds = false;
////
////
////
////                        int widthX = options.outWidth;
////
////                        int heightX = options.outHeight;
////
//                Bitmap bitmapX=loadImage(getAssetsPathList.get(pos));
//                int widthX = bitmapX.getWidth();
//
//                int heightX = bitmapX.getHeight();
//
//
//
////                Bitmap mBitmap1 = Bitmap.createBitmap(widthX, heightX, Bitmap.Config.ARGB_8888);//根据屏幕的高宽缩放生成bmp对象
////                Canvas canvas1 = new Canvas(mBitmap1);
////                canvas1.drawColor(Color.WHITE);
////                canvas1.drawBitmap(bitmapX, 0, 0, null);
//
//
//                imageView.setImageBitmap(bitmapX);
////==================添加图片================================================
////                                    Bitmap bitmap = BitmapFactory.decodeFile(mPathList.get(pos));
////
////                                    int width = bitmap.getWidth();
////
////                                    int height = bitmap.getHeight();
////                                    imageView.setImageBitmap(bitmap);
////===========================================================================
//
//                int width=200;
//                // float height=heightX/widthX*width;
//                double heightPercent=1.0*heightX/widthX;
//                double height=heightPercent*width;
//                //imageView.setImageURI(Uri.fromFile(new File(mList.get(position))))
////                    //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);
////
//                imageView.setClickable(true);
////                    //idPositionX=idPositionX+
//
//                texteditlayout.addView(imageView,width,(int)height);
//                int xxx=imageView.getHeight();
//                int yyy=imageView.getWidth();
//
//                Log.d("imageView", xxx+"   "+yyy+"   "+widthX+"    "+heightX);
//                //===========
            }
        });
        loadIconRecycle.setAdapter(loadIconAdapter);
        //=========================================

        //iconLinearAdapter.RefreshIconTitleList(iconListTitle, iconListTitle.size());
        // LoadImagePopouWindow();
        makePopupWiondo(view);
    }
    private void LoadIconTitlePopuWindow()
    {
        final View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_loadcion, null);
        iconListTitle = new ArrayList<String>();
        iconListTitle.clear();
        iconListTitle.add("包装");
        iconListTitle.add("表情");
        iconListTitle.add("动物");
        iconListTitle.add("方向");
        iconListTitle.add("家具");
        iconListTitle.add("交通");
        iconListTitle.add("节庆");
        iconListTitle.add("美食");
        iconListTitle.add("美妆");
        iconListTitle.add("天气");
        iconListTitle.add("玩具");
        iconListTitle.add("网络社交");
        iconListTitle.add("文具");
        iconListTitle.add("文字符号");
        iconListTitle.add("运动");
        iconListTitle.add("植物");
        iconListTitle.add("边框");
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        TextView loadImageMakeSure=view.findViewById(R.id.loadIconMakeSure);
        loadImageMakeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        IconTitleLayout = view.findViewById(R.id.IconTitleLayout);
        IconTitleLayout.setLayoutManager(new LinearLayoutManager(BarcodeActivity.this,RecyclerView.HORIZONTAL,false));
        //IconTitleLayout.addItemDecoration(new MyDecoration());
        iconLinearAdapter=new IconLinearAdapter(BarcodeActivity.this,0, new IconLinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                //Toast.makeText(BarcodeActivity.this,"You Click"+pos,Toast.LENGTH_SHORT).show();
                //iconLinearAdapter.RefreshIconTitleList(iconListTitle, iconListTitle.size(),pos);
                Bundle bundle=new Bundle();
                bundle.putInt("iconTitleColor",pos);
                Message message=new Message();
                message.setData(bundle);
                message.what=2;
                nHandler.sendMessage(message);

                getAssetsPathList.clear();
                //===================================
                try {

                    mAssetManager = getAssets();

                    String[] mAssetsImageList;
                    mAssetsImageList=mAssetManager.list("images/"+String.valueOf(pos));
                    for (int i = 0; i < mAssetsImageList.length; i++) {
                        String imagePath = "images/"+String.valueOf(pos) + "/" + mAssetsImageList[i];
                        getAssetsPathList.add(imagePath);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                nHandler.sendEmptyMessage(3);



            }
        });
        IconTitleLayout.setAdapter(iconLinearAdapter);

        nHandler.sendEmptyMessage(2);
        getAssetsPathList = new ArrayList<String>();
        getAssetsPathList.clear();

        //===================================
        try {

            mAssetManager = getAssets();
            String[] mAssetsImageList;
            mAssetsImageList=mAssetManager.list("images/"+String.valueOf(0));
            for (int i = 0; i < mAssetsImageList.length; i++) {
                String imagePath = "images/"+String.valueOf(0) + "/" + mAssetsImageList[i];
                getAssetsPathList.add(imagePath);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadIconRecycle = view.findViewById(R.id.loadIconRecycle);
        loadIconRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 5));

        loadIconAdapter = new LoadIconAdapter(BarcodeActivity.this,getAssetsPathList,getAssetsPathList.size(),mAssetManager, new LoadIconAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();
                //==============
                final DragViewX imageView=new DragViewX(texteditlayout.getContext());
                getById=id;
                getIdFlag = 2;
                imageView.setId(id++);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        if(oldId==0)
//                        {
//
//
//                        }
//                        else
//                        {
//
//                            if (oldId!=getById) {
//                                if (idFlag == 1) {
//                                    DragViewT textView = findViewById(oldId);
//                                    textView.setBackgroundResource(0);
//
//                                } else if (idFlag == 2) {
//                                    DragViewX imageView1 = findViewById(oldId);
//                                    imageView1.setBackgroundResource(0);
////                                                            imageView1.setBackground(null);
//                                }
//                            }
//
//                        }
//                        idFlag=2;
//                        oldId=v.getId();
//                        //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                        return;
                    }
                });

//                                    imageView.setX(0);
//                                    imageView.setY(0);
//
//                                    imageView.setScaleX((float) 0.5);
//                                    imageView.setScaleY((float) 0.5);
////                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
////                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
//
//                    //moveImage(imageView, 2);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //imageView.setImageURI(Uri.fromFile(new File(mPathList.get(pos))));


//                        //==================只获取图片的宽高================================================
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//
//                        options.inJustDecodeBounds = true;
//
//                       Bitmap bitmapX = BitmapFactory.decodeFile(getAssetsPathList.get(pos), options);
//
//                       // Bitmap bitmapX=loadImage(getAssetsPathList.get(pos));
//
//
//                        options.inSampleSize = 1;
//
//                        options.inJustDecodeBounds = false;
//
//
//
//                        int widthX = options.outWidth;
//
//                        int heightX = options.outHeight;
//                        //=================================================
                Bitmap bitmapX=loadImage(getAssetsPathList.get(pos));
                imageView.setImageBitmap(bitmapX);
                int widthX = bitmapX.getWidth();

                int heightX = bitmapX.getHeight();
//==================添加图片================================================
//                                    Bitmap bitmap = BitmapFactory.decodeFile(mPathList.get(pos));
//
//                                    int width = bitmap.getWidth();
//
//                                    int height = bitmap.getHeight();
//                                    imageView.setImageBitmap(bitmap);
//===========================================================================

                int xWidth=barcodeSizeWidth;
                int xHeight=barcodeSizeHeight;

                int bWidth= texteditlayout.getWidth();
                int bHeigth= texteditlayout.getHeight();
                double xishu=bWidth*1.0/xWidth*1.0;
                double nowWidht=10*xishu;


                int width=(int)nowWidht;//200;
                // float height=heightX/widthX*width;
                double heightPercent=1.0*heightX/widthX;
                double height=heightPercent*width;
                //imageView.setImageURI(Uri.fromFile(new File(mList.get(position))))
//                    //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);
//
                imageView.setClickable(true);
//                    //idPositionX=idPositionX+

                texteditlayout.addView(imageView,width,(int)height);
                int xxx=imageView.getHeight();
                int yyy=imageView.getWidth();

                Log.d("imageView", xxx+"   "+yyy+"   "+widthX+"    "+heightX);
                //===========
            }
        });
        loadIconRecycle.setAdapter(loadIconAdapter);
        //=========================================

        //iconLinearAdapter.RefreshIconTitleList(iconListTitle, iconListTitle.size());
       // LoadImagePopouWindow();
        makePopupWiondo(view);


    }
    private void LoadImagePopouWindow()
    {
        final View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_loadimage, null);
        mPathList = new ArrayList<String>();
        mImgsX = new ArrayList<String>();
        mPathNameList = new ArrayList<File>();

        mDirPaths = new HashSet<String>() ;
        TextView loadImageMakeSure=view.findViewById(R.id.loadImageMakeSure);
        loadImageMakeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        LoadImageRecycle = view.findViewById(R.id.loadImageRecycle);
        LoadImageRecycle.setLayoutManager(new GridLayoutManager(BarcodeActivity.this, 4));

        loadImageAdapter = new LoadImageAdapter(BarcodeActivity.this,0, new LoadImageAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int pos) {
                                    //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();
                                    //==============
                                    final DragViewX imageView=new DragViewX(texteditlayout.getContext());
                                    getById=id;
                                    getIdFlag = 2;
                                    imageView.setId(id++);

                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

//                                            if(oldId==0)
//                                            {
//
//
//                                            }
//                                            else
//                                            {
//
//                                                    if (oldId!=getById) {
//                                                        if (idFlag == 1) {
//                                                            DragViewT textView = findViewById(oldId);
//                                                            textView.setBackgroundResource(0);
//
//                                                        } else if (idFlag == 2) {
//                                                            DragViewX imageView1 = findViewById(oldId);
//                                                            imageView1.setBackgroundResource(0);
////                                                            imageView1.setBackground(null);
//                                                        }
//                                                    }
//
//                                            }
//                                            idFlag=2;
//                                            oldId=v.getId();
//                                            //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                                            return;
                                        }
                                    });

//                                    imageView.setX(0);
//                                    imageView.setY(0);
//
//                                    imageView.setScaleX((float) 0.5);
//                                    imageView.setScaleY((float) 0.5);
////                ImageView.setX(getInt(getApplicationContext(),TYPE_X,0));
////                ImageView.setY(getInt(getApplicationContext(),TYPE_Y,0));
//
//                    //moveImage(imageView, 2);
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    imageView.setImageURI(Uri.fromFile(new File(mPathList.get(pos))));

                                    BitmapFactory.Options options = new BitmapFactory.Options();

                                     options.inJustDecodeBounds = true;

                                     Bitmap bitmapX = BitmapFactory.decodeFile(mPathList.get(pos), options);

                                     options.inSampleSize = 1;

                                     options.inJustDecodeBounds = false;



                                     int widthX = options.outWidth;

                                     int heightX = options.outHeight;
//==================添加图片================================================
//                                    Bitmap bitmap = BitmapFactory.decodeFile(mPathList.get(pos));
//
//                                    int width = bitmap.getWidth();
//
//                                    int height = bitmap.getHeight();
//                                    imageView.setImageBitmap(bitmap);
//===========================================================================

                                    int width=200;
                                   // float height=heightX/widthX*width;
                                    double heightPercent=1.0*heightX/widthX;
                                    double height=heightPercent*width;
                                    //imageView.setImageURI(Uri.fromFile(new File(mList.get(position))))
//                    //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);
//
                                    imageView.setClickable(true);
//                    //idPositionX=idPositionX+

                                    texteditlayout.addView(imageView,width,(int)height);
                                    int xxx=imageView.getHeight();
                                    int yyy=imageView.getWidth();

                                    Log.d("imageView", xxx+"   "+yyy+"   "+widthX+"    "+heightX);
                                    //===========
                                }
                            });
                            LoadImageRecycle.setAdapter(loadImageAdapter);


//        LoadImageRecycle.setAdapter(new LoadImageAdapter(BarcodeActivity.this,80, new LoadImageAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                //Toast.makeText(BarcodeActivity.this, "ClickYour" + pos, Toast.LENGTH_SHORT).show();
//            }
//        }));
        getImages();
// 监听滚动状态





//        LoadImageRecycle.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(v.posit)
//            }
//        });
//        LoadImageRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView view, int scrollState) {
//                    if(scrollState==RecyclerView.SCROLL_STATE_IDLE&&!mIsFinishSearchImage && !mIsScanning)
//                    {
//                        scanImageData();
//                    }
//            }
//
////            public void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                // 滑动到倒数第10个，继续扫描图片
////                if (!mIsFinishSearchImage && !mIsScanning
////                        && firstVisibleItem + visibleItemCount + 10 >= mPathList.size()) {
////
////                }
////            }
//        });
        makePopupWiondo(view);
    }

    public void showPopupWindow() {


         final View wh = LayoutInflater.from(this).inflate(R.layout.popupwindow_inputtext, null);

        scrollView = wh.findViewById(R.id.textscrollViewT1);
        layout1 = wh.findViewById(R.id.layoutT1);
        layout2 = wh.findViewById(R.id.layoutT2);
        imageE1 = wh.findViewById(R.id.textKeyboard);
        imageE2 = wh.findViewById(R.id.textStyle);




        TextView confirmTextView=wh.findViewById(R.id.textMakesure1);
        confirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //textView=wh.findViewById(R.id.Xx);
        //textView.setText("ssssssss");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int width = getWidthInPx(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, 150);
        //LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
        ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
        params1.width = width;
        params2.width = width;
        layout1.setLayoutParams(layoutParams);
        layout2.setLayoutParams(layoutParams);

        //final ImageView imageView=wh.findViewById(R.id.imageGraffitiT2);

        //=============

        final EditText editText = wh.findViewById(R.id.popupWindowET_1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView textView = findViewById(getById);
                //DragViewT textView=findViewById(getById);
                testString=s.toString();
                textView.setText(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);

        editText.requestFocus();
       // if (keybordFlag == 0) {
            InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
            //makePopupWiondo(view);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    //editText.setFocusableInTouchMode(true);
                    //editText.setFocusable(true);
                    //editText.requestFocus();
                    //InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    //inputManager.showSoftInput(editText, 0);
                    InputMethodManager imm=(InputMethodManager)wh.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);


                }
            }, 200);
       // }

        final DragViewT textView = new DragViewT(getApplicationContext());
        getById = id;
        textView.setTextColor(Color.BLACK);
        textView.setId(id++);
        //textView.setText("hahhah");
        //textView.setBackgroundColor(10);
        getIdFlag = 1;
        textSize=68;
        textView.setTextSize(68);
        testString="双击编辑";
        textView.setText(testString);
        //textView.setBackgroundColor(Color.argb(255,0,255,0));
        //getIdFlag=1;
        //moveImage(textView, 1);
        textView.setStateListAnimator(null);
        textView.bringToFront();

        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSizeseekBar.setProgress((int)textSize-10);
                DoubleClickView(textView);
//                if(oldId==0)
//                {
//
//
//                }
//                else
//                {
//                    if(dateFlag==0) {
//
//                            if (idFlag == 1) {
//                                DragViewT textView = findViewById(oldId);
//                                textView.setBackgroundResource(0);
//
//                            } else if (idFlag == 2) {
//                                DragViewX imageView1 = findViewById(oldId);
//                                imageView1.setBackgroundResource(0);
////                                imageView1.setBackground(null);
//                            }
//
//                    }
//                }
//                idFlag=1;
//                oldId=v.getId();
//                SecondClick=(int)System.currentTimeMillis();
//                if((SecondClick-FirstClick<300)&& CountClick==0)
//                {
//
//                    if(keybordFlag==0) {
//                        editText.setText(textView.getText().toString());
//                        editText.setFocusable(true);
//                        editText.setFocusableInTouchMode(true);
//
//                        editText.setSelection(editText.length());
//                        editText.requestFocus();
//                        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputManager.showSoftInput(editText, 0);
//                        //makePopupWiondo(view);
//                        Timer timer = new Timer();
//                        timer.schedule(new TimerTask() {
//                            public void run() {
//                                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                                inputManager.showSoftInput(editText, 0);
//                            }
//                        }, 300);
//                    }
////                    if(keybordFlag==0)
////                        scrollView.scrollTo(0, 0);
////                    else
////                        scrollView.scrollTo(width+width, 0);
//                    makePopupWiondo(wh);
//                }
//                CountClick--;
//                if(CountClick<0)
//                    CountClick=0;
//                FirstClick=(int)System.currentTimeMillis();


                return;
            }
        });
        qrcodeData[getById]="3000+"+testString;//1开头代表一维码，2开头代表二维码，3开头代表数字
        texteditlayout.addView(textView);
        makePopupWiondo(wh);
        //===================

        //final SeekBar sk = wh.findViewById(R.id.seekBarT1);
        final TextView t1 = wh.findViewById(R.id.textT21);
        final ImageView i1=wh.findViewById(R.id.textCu);
        final TextView t2 = wh.findViewById(R.id.textT22);
        final ImageView i2=wh.findViewById(R.id.textxie);
        final TextView t3 = wh.findViewById(R.id.textT23);
        final ImageView i3=wh.findViewById(R.id.texthua);

        final LinearLayout textT21layout=wh.findViewById(R.id.textT21layout);
        final LinearLayout textT22layout=wh.findViewById(R.id.textT22layout);
        final LinearLayout textT23layout=wh.findViewById(R.id.textT23layout);
        final TextView textView1 = wh.findViewById(R.id.textT1);
        final TextView textView3 = wh.findViewById(R.id.textIT3);
        final TextView textView4 = wh.findViewById(R.id.textIT4);
        final TextView textView5 = wh.findViewById(R.id.textIT5);
        final TextView textView6 = wh.findViewById(R.id.textIT6);
        final TextView textView7 = wh.findViewById(R.id.textIT7);

        final Button buttonR=wh.findViewById(R.id.TextSetReduce);
        final Button buttonA=wh.findViewById(R.id.TextSetAdd);
        final TextView textViewD=wh.findViewById(R.id.TextSetNumber);
        final CheckBox checkBox=wh.findViewById(R.id.TextCheckS);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int X=Integer.parseInt(textViewD.getText().toString());
                X--;

                textViewD.setText(String.valueOf(X));
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int X=Integer.parseInt(textViewD.getText().toString());
                X++;

                textViewD.setText(String.valueOf(X));
            }
        });


        textView1.setTextColor(Color.parseColor("#fbb80e"));
        textT21layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlodFlag=!BlodFlag;
                if(BlodFlag==true &&ItalicFlag==false)
                {
                    t1.setTextColor(Color.parseColor("#fbb80e"));
                    i1.setImageResource(R.drawable.zgtextcu2);
                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {
                    t1.setTextColor(Color.BLACK);
                    i1.setImageResource(R.drawable.zgtextcu1);
                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {
                    t1.setTextColor(Color.parseColor("#fbb80e"));
                    i1.setImageResource(R.drawable.zgtextcu1);
                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {
                    t1.setTextColor(Color.BLACK);
                    i1.setImageResource(R.drawable.zgtextcu1);
                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }



            }
        });
        textT22layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItalicFlag=!ItalicFlag;
                if(BlodFlag==true &&ItalicFlag==false)
                {
                    t2.setTextColor(Color.BLACK);
                    i2.setImageResource(R.drawable.zgtextxie1);
                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {
                    t2.setTextColor(Color.parseColor("#fbb80e"));
                    i2.setImageResource(R.drawable.zgtextxie2);
                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {
                    t2.setTextColor(Color.parseColor("#fbb80e"));
                    i2.setImageResource(R.drawable.zgtextxie2);
                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {
                    t2.setTextColor(Color.BLACK);
                    i2.setImageResource(R.drawable.zgtextxie1);
                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });
        textT23layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnderlineFlag=!UnderlineFlag;
                if(UnderlineFlag)
                {
                    t3.setTextColor(Color.parseColor("#fbb80e"));
                    i3.setImageResource(R.drawable.zgtexthua2);
                    textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                    textView.getPaint().setAntiAlias(true);

                        ;

//                    textView.setTypeface(BlodType,Typeface.BOLD);
//                    textView.invalidate();
                }
                else
                {
                    t3.setTextColor(Color.BLACK);
                    i3.setImageResource(R.drawable.zgtexthua1);
                    textView.getPaint().setFlags(0);
//                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
                textView.invalidate();

                if(BlodFlag==true &&ItalicFlag==false )
                {
                    t2.setTextColor(Color.BLACK);
                    i2.setImageResource(R.drawable.zgtextxie1);
                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {
                    t2.setTextColor(Color.parseColor("#fbb80e"));
                    i2.setImageResource(R.drawable.zgtextxie2);
                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {
                    t2.setTextColor(Color.parseColor("#fbb80e"));
                    i2.setImageResource(R.drawable.zgtextxie2);
                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {
                    t2.setTextColor(Color.BLACK);
                    i2.setImageResource(R.drawable.zgtextxie1);
                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }


            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView1.setTextColor(Color.parseColor("#fbb80e"));
                textView3.setTextColor(Color.BLACK);
                textView4.setTextColor(Color.BLACK);
                textView5.setTextColor(Color.BLACK);
                textView6.setTextColor(Color.BLACK);
                textView7.setTextColor(Color.BLACK);

                //DragViewT textview=findViewById(getById);
                BlodType=Typeface.SANS_SERIF;
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }


            }
        });


        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView3.setTextColor(Color.parseColor("#fbb80e"));
                textView1.setTextColor(Color.BLACK);
                textView4.setTextColor(Color.BLACK);
                textView5.setTextColor(Color.BLACK);
                textView6.setTextColor(Color.BLACK);
                textView7.setTextColor(Color.BLACK);
                //DragViewT textview=findViewById(getById);

                BlodType=Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf");
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });

        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView4.setTextColor(Color.parseColor("#fbb80e"));
                textView3.setTextColor(Color.BLACK);
                textView1.setTextColor(Color.BLACK);
                textView5.setTextColor(Color.BLACK);
                textView6.setTextColor(Color.BLACK);
                textView7.setTextColor(Color.BLACK);
                //DragViewT textview=findViewById(getById);
                BlodType=Typeface.createFromAsset(getAssets(), "fonts/hkwwt.ttf");
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView5.setTextColor(Color.parseColor("#fbb80e"));
                textView3.setTextColor(Color.BLACK);
                textView4.setTextColor(Color.BLACK);
                textView1.setTextColor(Color.BLACK);
                textView6.setTextColor(Color.BLACK);
                textView7.setTextColor(Color.BLACK);
                //DragViewT textview=findViewById(getById);
                BlodType=Typeface.createFromAsset(getAssets(), "fonts/simhei.ttf");
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });

        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView6.setTextColor(getResources().getColor(R.color.bacgroundColor));
                textView3.setTextColor(Color.BLACK);
                textView4.setTextColor(Color.BLACK);
                textView5.setTextColor(Color.BLACK);
                textView1.setTextColor(Color.BLACK);
                textView7.setTextColor(Color.BLACK);
                //DragViewT textview=findViewById(getById);
                BlodType=Typeface.createFromAsset(getAssets(), "fonts/stliti.ttf");
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });

        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView7.setTextColor(Color.parseColor("#fbb80e"));
                textView3.setTextColor(Color.BLACK);
                textView4.setTextColor(Color.BLACK);
                textView5.setTextColor(Color.BLACK);
                textView6.setTextColor(Color.BLACK);
                textView1.setTextColor(Color.BLACK);
                //DragViewT textview=findViewById(getById);
                BlodType=Typeface.createFromAsset(getAssets(), "fonts/ygyxsziti.ttf");
                if(BlodFlag==true &&ItalicFlag==false)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD);
                    textView.invalidate();
                }
                else if(BlodFlag==false &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.ITALIC);
                }
                else if(BlodFlag==true &&ItalicFlag==true)
                {

                    textView.setTypeface(BlodType,Typeface.BOLD_ITALIC);
                }
                else
                {

                    textView.setTypeface(BlodType,Typeface.NORMAL);
                }
            }
        });
        textView1.setTypeface(Typeface.SANS_SERIF);
        textView3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/fzstk.ttf"));
        textView4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/hkwwt.ttf"));
        textView5.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/simhei.ttf"));
        textView6.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/stliti.ttf"));
        textView7.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ygyxsziti.ttf"));
//        sk.setProgress((int)textSize-10);
//        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int p = 1;
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                // TODO Auto-generated method stub
//
//
//                if (p < 1) {
//                    p = 1;
//                    sk.setProgress(p);
//
//                }
//                if (p > 100) {
//                    p = 100;
//                    sk.setProgress(p);
//
//                }
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                touchFlag = 0;
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                // TODO Auto-generated method stub
//                touchFlag = 0;
//                p = progress;
//                textSize=p+10;
//                textView.setTextSize(textSize);
//                //t1.setTextSize(p);
//            }
//        });


        //final TextView textView=wh.findViewById(R.id.Xx);
        /*getViewHeight(imageView);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    // 用户手指摸到屏幕
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        //textView.setText(String.valueOf(startX));
                        touchFlag=0;
                        break;
                    // 用户手指正在滑动
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getX();
                        float y = event.getY();
                        canvas.drawLine(startX, startY, x, y, paint);
                        canvas.save();
                        canvas.restore();
                        // 每次绘制完毕之后，本次绘制的结束坐标变成下一次绘制的初始坐标
                        startX = x;
                        startY = y;
                        textView.setText(String.valueOf(startY));

                        imageView.setImageBitmap(bitcopy);
                        break;
                    // 用户手指离开屏幕
                    case MotionEvent.ACTION_UP:
                        touchFlag=0;
                        break;

                }
                // true：告诉系统，这个触摸事件由我来处理
                // false：告诉系统，这个触摸事件我不处理，这时系统会把触摸事件传递给imageview的父节点
                return true;
            }
        });
*/

        //final WPopupWindow popupWindow = new WPopupWindow(wh);
        //popupWindow.showAtLocation(getContentView(BarcodeActivity.this), Gravity.BOTTOM, 0, 0);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;

            }

        });

        wh.findViewById(R.id.textKeyboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                keybordFlag = 0;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, 150);


                ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                params1.width = width;
                params2.width = width;
                layout1.setLayoutParams(layoutParams);
                layout2.setLayoutParams(layoutParams);
                imageE1.setImageResource(R.drawable.zgtextinput);
                imageE2.setImageResource(R.drawable.zgtextstyle2);
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);


                scrollView.scrollTo(0, 0);

            }
        });
        wh.findViewById(R.id.textStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keybordFlag = 1;
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if(imm.isActive()&&getCurrentFocus()!=null){
//                    if (getCurrentFocus().getWindowToken()!=null) {
//                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }

                //EditText edit=(EditText)findViewById(R.id.edit);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);


                //ViewGroup.LayoutParams params1 = layout1.getLayoutParams();
                ViewGroup.LayoutParams params2 = layout2.getLayoutParams();
                //params1.width = width;
                params2.width = width;
                // layout1.setLayoutParams(layoutParams);
                layout2.setLayoutParams(layoutParams);
                imageE1.setImageResource(R.drawable.zgtextinput2);
                imageE2.setImageResource(R.drawable.zgtextstyle);



                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        scrollView.scrollTo(width, 0);


                    }
                }, 200);


            }
        });

    }

    //    private void saveBitmapPicture(String savePath)
//    {
//        File doodleFile = null;
//        File file = null;
//       // String savePath = mDoodleParams.mSavePath;
//        boolean isDir = mDoodleParams.mSavePathIsDir;
//        if (TextUtils.isEmpty(savePath)) {
//            File dcimFile = new File(Environment.getExternalStorageDirectory(), "DCIM");
//            doodleFile = new File(dcimFile, "Doodle");
//            //　保存的路径
//            //file = new File(doodleFile, System.currentTimeMillis() + ".jpg");
//            file = new File(doodleFile,  "123.jpg");
//        } else {
//            if (isDir) {
//                doodleFile = new File(savePath);
//                //　保存的路径
//                //file = new File(doodleFile, System.currentTimeMillis() + ".jpg");
//                file = new File(doodleFile,  "123.jpg");
//            } else {
//                file = new File(savePath);
//                doodleFile = file.getParentFile();
//            }
//        }
//        doodleFile.mkdirs();
//
//        FileOutputStream outputStream = null;
//        try {
//            if (ContextCompat.checkSelfPermission(DoodleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
//                ActivityCompat.requestPermissions(DoodleActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//            //file=new File(mDoodleParams.mImagePath);
//            outputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
//            ImageUtils.addImage(getContentResolver(), file.getAbsolutePath());
//            Intent intent = new Intent();
//            intent.putExtra(KEY_IMAGE_PATH, file.getAbsolutePath());
//            setResult(Activity.RESULT_OK, intent);
//            finish();
//
////                    OkHttp sss = new OkHttp();
////                    File file = new File(path);
////                    sss.uploadFile(file, new SetActivity.VolleyCallback() {
////                        @Override
////                        public void onSuccess(String result) {
////                            //TextDisplay.setText(result);
////                            Message message = new Message();
////                            message.what = 10;
////                            Bundle bundle = new Bundle();
////                            bundle.putString("msg", result);
////                            message.setData(bundle);
////                            String url = result;
////
//////                            //============地址转图片==================================
//////                            try {
//////                                InputStream is = new java.net.URL(url).openStream();
//////                                tmpBitmap = BitmapFactory.decodeStream(is);
//////                                //displayword.setImageBitmap(tmpBitmap);
//////                                is.close();
//////                            } catch (Exception e) {
//////                                e.printStackTrace();
//////                                // Log.i( "KK下载图片" , e.getMessage());
//////
//////                            }
//////                            //========================================================
////                            //handler.sendMessage(message);
////                        }
////                    });
//
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//           // onError(DoodleView.ERROR_SAVE, e.getMessage());
//        } finally {
//           //Util.closeQuietly(outputStream);
//            //callback.run();
//        }
//    }
    private void savePicture(Bitmap bm, String fileName, String filePath) {
        File doodleFile = null;
        Log.i("xing", "savePicture: ------------------------");
        if (null == bm) {
            Log.i("xing", "savePicture: ------------------图片为空------");
            return;
        }
        File foder = new File(Environment.getExternalStorageDirectory(), "DCIM");
        doodleFile = new File(foder, "Doodle");

        //File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");
        if (!doodleFile.exists()) {
            doodleFile.mkdirs();
        }
        File myCaptureFile = new File(doodleFile, fileName);
        try {
            if (ContextCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
            if (!myCaptureFile.exists()) {

                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 95, bos);

            bos.flush();
            //BarcodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse(doodleFile.getParent())));

            Context context = BarcodeActivity.this;
            MediaStore.Images.Media.insertImage(context.getContentResolver(), myCaptureFile.getAbsolutePath(), myCaptureFile.getName(), null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(filePath));
            intent.setData(uri);
            context.sendBroadcast(intent);


        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();

    }

    public int getWidthInPx(Context context) {
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    public   boolean isChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            //是否是Unicode编码,除了" "这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')||((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                continue;
            }
            else{
                return false;
            }
        }
        return true;
    }

    public   boolean isSpecialCharacter(String str){
        //是" "这个特殊字符的乱码情况
        if(str.contains("ï¿½")){
            return true;
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    IntentIntegrator intentIntegrator=new IntentIntegrator(BarcodeActivity.this);
//                    intentIntegrator.initiateScan();
                    //调用扫描
//                    Intent intent = new Intent(BarcodeActivity.this, CaptureActivity.class);
//                    startActivityForResult(intent, 1002);
                }
                return;
            }

        }
    }


    // @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
//        // 跳转扫描页面返回扫描数据
//        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        //  判断返回值是否为空
//        if (scanResult != null) {
//            //返回条形码数据
//            String result = scanResult.getContents();
//            Log.d("code", result);
//            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//        }
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            return true;
//        }
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001 && resultCode== Activity.RESULT_OK)
        {
            // String result=data.getStringExtra(CaptureActivity.KEY_DATA);
            String resultData=data.getStringExtra(CaptureActivity.KEY_DATA);
            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;
            try {
                Boolean SSSS= Charset.forName("ISO-8859-1").newEncoder().canEncode(resultData);
                Boolean yyyy= Charset.forName("UTF-8").newEncoder().canEncode(resultData);
                if(!SSSS)
                {
                    resultData=resultData;
                }
                else {
                    UTF_Str = new String(resultData.getBytes("ISO-8859-1"), "UTF-8");
                    is_cN = isChineseCharacter(UTF_Str);
                    //防止有人特意使用乱码来生成二维码来判断的情况
                    boolean b = isSpecialCharacter(resultData);
                    if (b) {
                        is_cN = true;
                    }
//                            System.out.println("是为:"+is_cN);
                    if (!is_cN) {
                        GB_Str = new String(resultData.getBytes("ISO-8859-1"), "GB2312");
//                                System.out.println("这是转了GB2312的"+GB_Str);
                    }
                    if(is_cN){
                        resultData=UTF_Str;
                    }else{
                        resultData=GB_Str;
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Toast.makeText(this,resultData, Toast.LENGTH_SHORT).show();
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.supermaket128);
            final String content=resultData;

            QRDialog qrDialog=new QRDialog(BarcodeActivity.this,content);

            qrDialog.setCancel("取消", new QRDialog.IOnCancelListener() {
                @Override
                public void onCancel(QRDialog dialog) {

                }
            }).setConfirm("确认", new QRDialog.IOnConfirmListener() {
                @Override
                public void onConfirm(QRDialog dialog) {
                    if(selectScannerFlag==0)
                    {
                        DragViewT  textview=new DragViewT(getApplicationContext());
                        final DragViewT textView = new DragViewT(getApplicationContext());
                        getById = id;
                        getIdFlag = 1;
                        textView.setTextColor(Color.BLACK);
                        textView.setId(id++);
                        textView.setText(content);
                        //textView.setBackgroundColor(10);
                        textSize=30;
                        textView.setTextSize(30);
                        textView.setStateListAnimator(null);
                        textView.bringToFront();

                        textView.setClickable(true);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DoubleClickView(textView);
//                                if(oldId==0)
//                                {
//
//
//                                }
//                                else
//                                {
//
//                                    if (oldId != getById) {
//                                        if (idFlag == 1) {
//                                            DragViewT textView = findViewById(oldId);
//                                            textView.setBackgroundResource(0);
//
//                                        } else if (idFlag == 2) {
//                                            DragViewX imageView1 = findViewById(oldId);
//                                            imageView1.setBackgroundResource(0);
////                                            imageView1.setBackground(null);
//                                        }
//                                    }
//
//                                }
//                                idFlag=1;
//                                oldId=v.getId();
//

                                return;
                            }
                        });
                        //moveImage(textView, 1);
                        texteditlayout.addView(textView);

                    }
                    else
                    {
                        DragViewX imageView=new DragViewX(getApplicationContext());
                        getById = id;
                        getIdFlag = 2;
                        imageView.setId(id++);
                        if (selectScannerFlag == 2) {
                            makeQrCode(content);
                            //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                        } else if (selectScannerFlag == 1) {
                            qrImage = encodeAsBitmap(content, BarcodeFormat.CODE_128, 350, 150);
                        }


                        imageView.setImageBitmap(qrImage);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        //imageView.getBackground().setAlpha(10);

                        //imageView.setBackgroundResource(R.drawable.bg_boarder_xu);

                        imageView.setClickable(true);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if(oldId==0)
//                                {
//
//
//                                }
//                                else
//                                {
//
//                                    if (oldId != getById) {
//                                        if (idFlag == 1) {
//                                            DragViewT textView = findViewById(oldId);
//                                            textView.setBackgroundResource(0);
//
//                                        } else if (idFlag == 2) {
//                                            DragViewX imageView1 = findViewById(oldId);
//                                            imageView1.setBackgroundResource(0);
////                                            imageView1.setBackground(null);
//                                        }
//                                    }
//
//                                }
//                                idFlag=2;
//                                oldId=v.getId();
//                                //Toast.makeText(BarcodeActivity.this,"sss"+v.getId(),Toast.LENGTH_SHORT).show();
//                                return;
                            }
                        });
                        //imageView.setBackgroundColor(10);

                        //moveImage(imageView, 2);

                        //texteditlayout.addView(textView);


                        texteditlayout.addView(imageView);
                    }
                }
            }).show();

        }

    }
    private void FragmentGetBarcodeView(final String barcodeNumber)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = dbN.rawQuery("select _barcodenumber,_barcodename,_barcodetime,_barcodeinformation,_barcodeimage,_barcodebackgroundimage from barcodeview where _barcodenumber='"+barcodeNumber+"';", null);

//                HashMap<String, String> map = new HashMap<String, String>();
//                ArrayList<Map<String,Object>> barcodeImageList=new ArrayList<Map<String,Object>>();
//                //ArrayList<Map<String,Object>> barcodeInformationList=new ArrayList<Map<String,Object>>();
//                Map<String, Object> barcodeImageMap = new TreeMap<String, Object>(); //存储图片路径和转换成的 bitmap
//                Map<String, Object> barcodeInformationMap = new TreeMap<String, Object>(); //存储图片路径和转换成的 bitmap
                Gson gson = new Gson();

                while(cursor.moveToNext()){
                    //遍历出表名
//                    //viewInformationList.clear();
//                    barcodeInformationListNow=new ArrayList();
//
//                    String barcodenumber = cursor.getString(0);
//                    String barcodename=cursor.getString(1);
//                    String barcodetime=cursor.getString(2);
//                    String barcodeinformation=cursor.getString(3);
//                    //cursor.getInt(cursor.getColumnIndex("_id"));
////                Type type = new TypeToken<ArrayList<String>>() {}.getType();
////
////                viewInformationList = gson.fromJson(barcodeinformation, type);
//
//                    byte[] photoByte=cursor.getBlob(4);
//                    Bitmap bitmap= BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

                    byte[] photoByteB;
                    //Bitmap bitmapB=null;
                    if(cursor.getBlob(5)!=null) {
                        photoByteB = cursor.getBlob(5);
                        backgroundBitmap = BitmapFactory.decodeByteArray(photoByteB, 0, photoByteB.length);
                    }

                }
                Bundle bundle=new Bundle();
                bundle.putString("BarcodeNumber",barcodeNumber);

                Message message=new Message();
                message.setData(bundle);
                message.what=5;
                nHandler.sendMessage(message);
                //nHandler.sendEmptyMessage(5);


            }
        });
        thread.start();
    }
    private void GetPhoneSize(final int widthX,final int hightX)
    {
        WindowManager wm = (WindowManager) BarcodeActivity.this.getSystemService(BarcodeActivity.this.WINDOW_SERVICE);
        //获取屏幕的高和宽，以决定pdf的高和宽

        float width = wm.getDefaultDisplay().getWidth();
        PhoneHeightH = wm.getDefaultDisplay().getHeight()/2.5;//3
        PhoneHeight=PhoneHeightH;
        PhoneWidth=(int) width;



        //int widhtX=40;
        //int widthY=30;

        Log.d("Size", width+"   "+PhoneHeightH+"   "+PhoneWidth);


        //获取layout的尺寸=========================
        texteditlayout.post(new Runnable() {
            @Override
            public void run() {
//                //int heightY = texteditlayout.getHeight() ;
//                int xWidth=texteditlayout.getWidth();
//                int i=xWidth/8;
//                PhoneWidth=i*8;
//                //
                PhoneWidth = texteditlayout.getWidth();
                Log.d("SizeX", PhoneWidth+"   "+PhoneHeight);
                SetTextLayoutSize(PhoneWidth,widthX,hightX);
                //SetTextLayoutSize(PhoneWidth,(int)PhoneHeight,PhoneHeightS);
            }
        });

        //====================================
//        //获取layout的尺寸====缺点就是会多次调用==========================
//        ViewTreeObserver vto = texteditlayout.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//
//                 int heightY = texteditlayout.getMeasuredHeight();
//                 int widthY=texteditlayout.getMeasuredWidth();
//                Log.d("SizeY", widthY+"   "+heightY);
//                return  true;
//
//            }
//        });
//        //=================================================================

    }
    private void SetTextLayoutSize(int width,int widthX,int heightX)
    {

        //width textlayout的宽度
        //widthX 需要设置的纸张的宽度
        //heightX 需要设置的纸张的高度

        double sizePercent=1;
        sizePercent=1.0*width/widthX;
        PhoneHeight=sizePercent*heightX;
        if(PhoneHeight>PhoneHeightH)
        {
            PhoneHeightS=(int)PhoneHeightH;
        }
        else
        {
            PhoneHeightS=(int)PhoneHeight;
        }


        ViewGroup.LayoutParams params2=scrollViewBarcode.getLayoutParams();
        params2.height=PhoneHeightS;
        params2.width=width;
        scrollViewBarcode.setLayoutParams(params2);
//        ViewGroup.LayoutParams params3=(ViewGroup.LayoutParams)LinearLayoutBarcode.getLayoutParams();
////        FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)PhoneHeight);
//        params3.height=(int)PhoneHeight;
//
//        params3.width=width;
//
////        params3.width=width;
////        params3.height=(int)PhoneHeight;
//        LinearLayoutBarcode.setLayoutParams(params3);

        ViewGroup.LayoutParams params1=(ViewGroup.LayoutParams)texteditlayout.getLayoutParams();

//        FrameLayout.LayoutParams params3 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)PhoneHeight);

        params1.height=(int)PhoneHeight;

        params1.width=width;

        scrollview_TouchEnd=(int)PhoneHeight;
//        params3.width=width;
//        params3.height=(int)PhoneHeight;
        texteditlayout.setLayoutParams(params1);



    }

    //=====================================================

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
    private Bitmap CutBitmap(Bitmap img,int widthC,int heightC)
    {
        Matrix matrix1 = new Matrix();
        Bitmap  x = Bitmap.createBitmap(img, 0, 0, widthC, heightC, matrix1, true);
        //Bitmap  x=Bitmap.createBitmap (img, 0, 10, img.getWidth(), img.getHeight()-30, matrix1)
        return  x;
    }
    //=============================================================================
    private   Bitmap ChangeImagePDF(Bitmap img,int PrintCopies,int iCut,int addressFile)
    {
//        boolean PrintHalftoneNow=PrintHalftone;
//        String PrintHalftoneSNow=PrintHalftoneS;
//        if(PrintHalftoneNow)
//        {
//            if(addressFile==1)//文档
//            {
//                //灰度打印
//            }else//图片
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//        }
//        else
//        {
//
//
//            if(PrintHalftoneSNow.equals(PrintHalftone_String[0]))
//            {
//                img = DitherToBitmap(img);//抖动
//            }
//            else if(PrintHalftoneSNow.equals(PrintHalftone_String[1]))
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//            else
//            {
//                //灰度打印
//            }
//
//        }
//
//
//
//        if(doc_Flag)
//            img=DocGryBitmap(img,iCut,0);
//
//





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
        Log.e("height", "千"+qw+"百"+bw+"十"+sw+"个"+gw);
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
        //Log.e(TAG1, "千"+Wqw+"百"+Wbw+"十"+Wsw+"个"+Wgw);

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
        try {
            if (socket1 != null)
                Log.e("socket1", "null is now");
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
            //Log.e(TAG1,"s");
        }
        return result;
    }
    //=============================================================================
    private   Bitmap ChangeImagePDF_ESC(Bitmap img,int PrintCopies,int iCut,int addressFile)
    {
//        boolean PrintHalftoneNow=PrintHalftone;
//        String PrintHalftoneSNow=PrintHalftoneS;
//        if(PrintHalftoneNow)
//        {
//            if(addressFile==1)//文档
//            {
//                //灰度打印
//            }else//图片
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//        }
//        else
//        {
//
//
//            if(PrintHalftoneSNow.equals(PrintHalftone_String[0]))
//            {
//                img = DitherToBitmap(img);//抖动
//            }
//            else if(PrintHalftoneSNow.equals(PrintHalftone_String[1]))
//            {
//                img=convertGreyImgByFloyd(img);//误差扩散
//            }
//            else
//            {
//                //灰度打印
//            }
//
//        }
//
//
//
//        if(doc_Flag)
//            img=DocGryBitmap(img,iCut,0);
//
//


// var bufView = new ArrayBuffer(canvasWidth/8*canvasHeight+12+10);
//=======================================


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
//        bufImage[0]=0x1d;
//        bufImage[1]=0x0e;
//        //打印速度
//        bufImage[2]=0x1c
//        bufImage[3]=0x60
//        bufImage[4]=0x4D
//        bufImage[5]=0x53
//        bufImage[6]=speedSet
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
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x50;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x52;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x20;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x30;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);
//
//        StartInt=0x46;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x4F;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x52;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x4D;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);
//
//        StartInt=0x50;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x52;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x49;//CG
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x4E;
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x54;//CG
//        Gray_Arraylist.add((byte)StartInt);
//        StartInt=0x0A;
//        Gray_Arraylist.add((byte)StartInt);
//        bufImage[j++]=0x1c;
//        bufImage[j++]=0x5e;
        StartInt=0x1c;
        Gray_Arraylist.add((byte)StartInt);
        StartInt=0x5e;
        Gray_Arraylist.add((byte)StartInt);
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        //result=result;
        try {
            if (socket1 != null)
                Log.e("socket1", "null is now");
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
            //Log.e(TAG1,"s");
        }
        return result;
    }
    private Bitmap LayoutToBitmap() {

        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）

        //获取缓存

//        texteditlayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        texteditlayout.layout(0, 0, texteditlayout.getMeasuredWidth(), texteditlayout.getMeasuredHeight());
//        texteditlayout.buildDrawingCache();
//        texteditlayout.setDrawingCacheEnabled(true);
//        Bitmap drawingCache = texteditlayout.getDrawingCache();



//        texteditlayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        texteditlayout.layout(0, 0, texteditlayout.getMeasuredWidth(), texteditlayout.getMeasuredHeight());
//        texteditlayout.buildDrawingCache();
//        texteditlayout.setDrawingCacheEnabled(true);
//        Bitmap drawingCache = texteditlayout.getDrawingCache();
        int xxx=texteditlayout.getWidth();
        int yyy=texteditlayout.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(texteditlayout.getWidth(), texteditlayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);


        if (Build.VERSION.SDK_INT >= 11) {
            texteditlayout.measure(View.MeasureSpec.makeMeasureSpec(texteditlayout.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(texteditlayout.getHeight(), View.MeasureSpec.EXACTLY));
            texteditlayout.layout((int) texteditlayout.getX(), (int) texteditlayout.getY(), (int) texteditlayout.getX() + texteditlayout.getMeasuredWidth(), (int) texteditlayout.getY() + texteditlayout.getMeasuredHeight());
        } else {
            texteditlayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            texteditlayout.layout(0, 0, texteditlayout.getMeasuredWidth(), texteditlayout.getMeasuredHeight());
        }
        texteditlayout.draw(canvas);

//        Bitmap  bitmapX = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
//        //创建Canvas，并传入Bitmap.
//        Canvas canvas = new Canvas(bitmapX);
//
//        //View把内容绘制到canvas上，同时保存在bitmap.
//        webView.draw(canvas);
//        //return bitmap


        //拷贝图片(这里就是我们需要的截图内容啦)
        //Bitmap newBitmap = Bitmap.createBitmap(drawingCache);
        return newBitmap;








    }


    //===========================================
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
    //从assets中读取图片
    private Bitmap loadImage(String path) {
        Bitmap image = null;
        InputStream in = null;
        try {
            in = mAssetManager.open(path);
            image = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return image;
    }

    //这个方法是根据index 格式化先生的文字,需要先 implements NumberPicker.Formatter

    public String format(int value) {
        return pickerString[value];
    }

    /**
     * 通过反射改变分割线颜色,
     */
    private void setPickerDividerColor(NumberPicker mNumberPicker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try{
                    pf.set(mNumberPicker,new ColorDrawable((int)R.color.huise));
                    //ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#000000"));
                    //pf.set(mNumberPicker,Color.parseColor("#000000"));
                    //pf.set(mNumberPicker,colorDrawable);
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                }catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void BarcodeCopiesMakeAgain(int barcodeFlagC) {
        if (barcodeFlagC > 0) {
            for (int i = 0; i < texteditlayout.getChildCount(); i++) {

                View child = texteditlayout.getChildAt(i);
                if (child instanceof ImageView) {
                    bitmap = ((BitmapDrawable) ((ImageView) child).getDrawable()).getBitmap();
                    int idX = child.getId();
                    if (qrcodeData[idX].length() > 1 && qrcodeData[idX].substring(1, 2).equals("1")) {
                        ImageView imageView = findViewById(idX);


                        String xx = qrcodeData[idX];//2000+
                        String[] xyz = xx.split("[+]");
                        int l1 = xyz[0].length() + 1;
                        String s1 = xx.substring(l1, xx.length());
                        String s2 = "";
                        int s4 = Integer.parseInt(s1);
                        int xy = Integer.parseInt(xyz[0].substring(3, xyz[0].length()));

                        if (xy > 0) {
                            s2 = xyz[0].substring(0, 1).toString() + "1" + "1" + xy + "+";
                            s4 = s4 + xy;
                        } else {
                            String s3 = String.valueOf(xy);
                            s3 = s3.substring(1, s3.length());
                            s2 = xyz[0].substring(0, 1).toString() + "1" + "0" + s3 + "+";
                            s4 = s4 - xy;
                            if (s4 < 0) {
                                s4 = 0;
                            }
                        }
                        s1 = String.valueOf(s4);
                        qrcodeData[getById] = s2 + s1;
                        //==========================

                        int dd=child.getWidth();
                        int cc=child.getHeight();
                        if (qrcodeData[idX].substring(0, 1).equals("2")) {


                            makeQrCodeC(s1.toString(), child.getWidth(), child.getHeight());

                            //qrImage=creatBarcode(getApplicationContext(), "123456", 200, 100, true);
                        } else if (qrcodeData[idX].substring(0, 1).equals("1")) {
                            //qrImage = encodeAsBitmap(s.toString(), BarcodeFormat.CODE_128, 350, 150);

                            qrImage = creatBarcode(getApplicationContext(), s1.toString(), child.getWidth(), child.getHeight()/2, true);

                        }

                        //makeQrCode(s.toString());

                        imageView.setImageBitmap(qrImage);


                    }
                } else if (child instanceof TextView) {


////                float c=child.getLeft();
////                float d=child.getTop();
////                float e=child.getRight();
////                float f=child.getBottom();
////                float g=child.getHeight();
////                float h=child.getWidth();
//                           String viewText=((TextView) child).getText().toString();
//                           float k=((TextView) child).getTextSize();
//
//                           int a =child.getLeft();
//                           int b=child.getTop();
////                float c=child.getRight();
////                float d=child.getBottom();
//                           int e=child.getHeight();
//                           int f=child.getWidth();
//                           int g=Integer.parseInt(barcodeWidth);
//                           int h=Integer.parseInt(barcodeHeight);
//                           int c=g-a-f;//c=g-a-e;
//                           int d=h-b-e;//d=h-b-f;
//                           viewInformationList.add(String.valueOf(a));
//                           viewInformationList.add(String.valueOf(b));
//                           viewInformationList.add(String.valueOf(c));
//                           viewInformationList.add(String.valueOf(d));
//                           viewInformationList.add(String.valueOf(e));
//                           viewInformationList.add(String.valueOf(f));
//                           viewInformationList.add(String.valueOf(k));
//                           String viewInformation= gson.toJson(viewInformationList);
//
//
//                           //Log.d("NowViewSizeT", c+" "+d+"  "+e+"  "+f+"  "+g+"   "+h+"  "+a+"   "+b+"  ");
//
//                           String sql = "insert into viewinformation (_barcodenumbers,_viewflag,_viewinformation,_viewtext) values ('" + barcodeNumber +
//                                   "','" + "2" + "','" + viewInformation + "','" + viewText + "')";
//                           dbN.execSQL(sql);


                }
            }
        }
    }
    private void BarcodePrintCopies(final int barcodeFlagF)
    {



        if(backgroundBitmap==null)
            ;
        else
            texteditlayout.setBackground(null);
        texteditlayout.setBackgroundColor(Color.WHITE);
        texteditlayout.setDrawingCacheEnabled(true);
        texteditlayout.buildDrawingCache();



        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）

        //获取缓存

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Bitmap bmpC=LayoutToBitmap();
                final Bitmap bmp=CutBitmap(bmpC,bmpC.getWidth(),scrollview_TouchEnd);
                int bitmapWidth=bmp.getWidth();
                int bitmapHeight=bmp.getHeight();
                bitmapWidth=texteditlayout.getWidth();
                bitmapHeight=texteditlayout.getHeight();
                texteditlayout.setDrawingCacheEnabled(false);
                texteditlayout.buildDrawingCache(false);
                if(backgroundBitmap!=null)
                    texteditlayout.setBackground(new BitmapDrawable(getResources(),backgroundBitmap));

                if (isBtConDeviceByMac(BlueToothMainActivity.BlueMac)) {

                } else {
                    BlueToothMainActivity.BlueState = false;
                    BlueToothMainActivity.BlueName = "";
                    BlueToothMainActivity.BlueMac = "";
                    Toast.makeText(BarcodeActivity.this, "Please connect the device...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bmp == null) {
                    Toast.makeText(BarcodeActivity.this, "Please select a file to print", Toast.LENGTH_SHORT).show();
                }


                if (BlueToothMainActivity.socketall != null && BlueToothMainActivity.BlueState && bmp != null) {
//                            final String PageStart = PageInputStart.getText().toString().replaceAll(" ", "");
//                            final String PageEnd = PageInputEnd.getText().toString().replace(" ", "");
//                            final String PageCopies = PageInputCopies.getText().toString().replace(" ", "");
//                            final String PageWidth = PageInputWidth.getText().toString().replace(" ", "");
//                            final String PageHeight = PageInputHeight.getText().toString().replace(" ", "");


                    if(barcodeFlagF>0)
                    {
                        BarcodeCopiesMakeAgain(barcodeFlagF);
                    }

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //GetPDFImage(PageStart,PageEnd);
                            int rotateNow=0;
                            if(BarcodeFangXiang.equals(Barcode_HengXiang))
                            {
                                rotateNow=0;

                            }
                            else
                            {
                                rotateNow=90;
                            }
                            Bitmap bitmap=zoomBitmap(bmp,barcodeSizeWidth*8,barcodeSizeHeight*8,rotateNow);

                            if(BlueToothName_Flag==1)
                            {
                                ChangeImagePDF(bitmap,1,1,1);
                            }
                            else
                            {
                                ChangeImagePDF_ESC(bitmap,1,1,1);
                            }

//                                                if (TextHouZhui.equals("pdf"))
//                                                    GetPDFImage(PageStart, PageEnd);
//                                                else {
//                                                    GetBMPImage(PageStart, PageEnd, bitmapX);
//                                                }
//
                        }
                    });
                    thread.start();





                } else
                    Toast.makeText(BarcodeActivity.this, "Please connect the device", Toast.LENGTH_SHORT).show();


            }
        }, 100);
    }
    private void SearchView(Bitmap barcodeBitmap,String barcodeName,String barcodeNumber,String barcodeTime){
//        long timecurrentTimeMillis = System.currentTimeMillis();
//         barcodeNumber=String.valueOf(timecurrentTimeMillis);
         //barcodeName="name is empty";


//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");// HH:mm:ss
//
//         barcodeTime = simpleDateFormat.format(timecurrentTimeMillis);



//        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
//        TextBoadrDateSB.setText(simpleDateFormat.format(date));

//        long timeGetTime =new Date().getTime();
//
//        long timeSeconds = System.currentTimeMillis();
//
//        long timeMillis = Calendar.getInstance().getTimeInMillis();
//
//        Log.d("test", "  当前时间戳1->:"+timecurrentTimeMillis);
        barcodeInformationList.clear();
        String barcodeWidth=String.valueOf(texteditlayout.getWidth());
        String barcodeHeight=String.valueOf(texteditlayout.getHeight());
        String Width=String.valueOf(barcodeSizeWidth);
        String Height=String.valueOf(barcodeSizeHeight);
        barcodeInformationList.add(barcodeWidth);
        barcodeInformationList.add(barcodeHeight);
        barcodeInformationList.add(Width);
        barcodeInformationList.add(Height);
        Gson gson = new Gson();

        String barcodeInformation= gson.toJson(barcodeInformationList);

        System.out.println("inputString= " + barcodeInformation);

        InputViewInformation(barcodeNumber,barcodeName,barcodeTime,barcodeInformation,barcodeBitmap,backgroundBitmap);
        for (int i = 0; i < texteditlayout.getChildCount(); i++) {
            viewInformationList.clear();
            View child = texteditlayout.getChildAt(i);
            if(child instanceof ImageView){
//                        //==========imageview转bitmap======================
//                        child.setDrawingCacheEnabled(true);
//
//                        Bitmap bitmap = child.getDrawingCache();
//
//                        child.setDrawingCacheEnabled(false);

//                        bitmap = Bitmap.createBitmap(child.getWidth(), child.getHeight(), Bitmap.Config.ARGB_8888);
//                        Canvas canvas = new Canvas(bitmap);
//                        canvas.drawColor(Color.BLACK);
//                        child.draw(canvas);
//                        //=================================================
                bitmap =((BitmapDrawable) ((ImageView) child).getDrawable()).getBitmap();

                //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                int a =child.getLeft();
                int b=child.getTop();
//                float c=child.getRight();
//                float d=child.getBottom();
                int e=child.getHeight();
                int f=child.getWidth();
                int g=Integer.parseInt(barcodeWidth);
                int h=Integer.parseInt(barcodeHeight);
                int c=g-a-f;
                int d=h-b-e;
                viewInformationList.add(String.valueOf(a));
                viewInformationList.add(String.valueOf(b));
                viewInformationList.add(String.valueOf(c));
                viewInformationList.add(String.valueOf(d));
                viewInformationList.add(String.valueOf(e));
                viewInformationList.add(String.valueOf(f));

                String viewInformation= gson.toJson(viewInformationList);
                ContentValues viewBitmapValues = new ContentValues();

                final ByteArrayOutputStream os = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                viewBitmapValues.put("_barcodenumbers", barcodeNumber);
                viewBitmapValues.put("_viewflag", "1");
                viewBitmapValues.put("_viewinformation", viewInformation);
                viewBitmapValues.put("_viewtext", "115200");
                viewBitmapValues.put("_viewimage", os.toByteArray());
                dbN.insert("viewinformation", null, viewBitmapValues);

//                String sql = "insert into viewinformation (_barcodenumbers,_viewflag,_viewinformation,_viewtext,_viewimage) values ('" + barcodeNumber +
//                        "','" + "1" + "','" + viewInformation + "','" + "" + "','" + viewBitmapValues + "')";
//                dbN.execSQL(sql);



//                screenHeight-top-nowGetHeight;
//               _barcodenumbers varchar(50) not null,_viewflag varchar(50) not null,_viewinformation varchar(100) not null,_viewtext varchar(100) not null,_viewimage BLOB

               // Log.d("NowViewSizeB", a+"  "+b+"  "+c+" "+d+"  "+e+"  "+f+"  ");

            }
            else if(child instanceof TextView)
            {


//                float c=child.getLeft();
//                float d=child.getTop();
//                float e=child.getRight();
//                float f=child.getBottom();
//                float g=child.getHeight();
//                float h=child.getWidth();
                String viewText=((TextView) child).getText().toString();
                float k=((TextView) child).getTextSize();

                int a =child.getLeft();
                int b=child.getTop();
//                float c=child.getRight();
//                float d=child.getBottom();
                int e=child.getHeight();
                int f=child.getWidth();
                int g=Integer.parseInt(barcodeWidth);
                int h=Integer.parseInt(barcodeHeight);
                int c=g-a-f;//c=g-a-e;
                int d=h-b-e;//d=h-b-f;
                viewInformationList.add(String.valueOf(a));
                viewInformationList.add(String.valueOf(b));
                viewInformationList.add(String.valueOf(c));
                viewInformationList.add(String.valueOf(d));
                viewInformationList.add(String.valueOf(e));
                viewInformationList.add(String.valueOf(f));
                viewInformationList.add(String.valueOf(k));
                String viewInformation= gson.toJson(viewInformationList);


                //Log.d("NowViewSizeT", c+" "+d+"  "+e+"  "+f+"  "+g+"   "+h+"  "+a+"   "+b+"  ");

                String sql = "insert into viewinformation (_barcodenumbers,_viewflag,_viewinformation,_viewtext) values ('" + barcodeNumber +
                        "','" + "2" + "','" + viewInformation + "','" + viewText + "')";
                dbN.execSQL(sql);




            }


        }
        nHandler.sendEmptyMessage(4);
        //=====================
    }
    private void ConnectDB(){
        viewInformationList=new ArrayList<>();
        barcodeInformationList=new ArrayList<>();
        SQLiteHelper helper = new SQLiteHelper(BarcodeActivity.this,"historybarcodedata.db",null,1);
        dbN = helper.getWritableDatabase();
    }
    private void InputViewInformation(String barcodeNumber,String barcodeName,String barcodeTime,String barcodeInformation,Bitmap barcodeImage,Bitmap brcodeBackgroundImage)
    {
        ContentValues barcodeBitmapValues = new ContentValues();

        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        barcodeImage.compress(Bitmap.CompressFormat.PNG, 100, os);
        barcodeBitmapValues.put("_barcodenumber", barcodeNumber);
        barcodeBitmapValues.put("_barcodename", barcodeName);
        barcodeBitmapValues.put("_barcodetime", barcodeTime);
        barcodeBitmapValues.put("_barcodeinformation", barcodeInformation);
        barcodeBitmapValues.put("_barcodeimage", os.toByteArray());
        //barcodeBitmapValues.put("_barcodebackgroundimage", null);


        if(brcodeBackgroundImage!=null)
        {




            //ContentValues barcodeBitmapValuesB = new ContentValues();

            final ByteArrayOutputStream osB = new ByteArrayOutputStream();

            brcodeBackgroundImage.compress(Bitmap.CompressFormat.PNG, 100, osB);
            barcodeBitmapValues.put("_barcodebackgroundimage", osB.toByteArray());
//            byte[] barcodeBitmapValuesB=osB.toByteArray();
//            //barcodeBitmapValuesB.put("_barcodebackgroundimage", osB.toByteArray());
//            byte[] img = os.toByteArray();


//        String sql = "insert into barcodeview (_barcodenumber,_barcodename,_barcodetime,_barcodeinformation,_barcodeimage,_barcodebackgroundimage) values ('" + barcodeNumber +
//                "','" + barcodeName + "','" + barcodeTime + "','" + barcodeInformation + "','" + barcodeBitmapValues + "','" + barcodeBitmapValuesB + "')";
//        dbN.execSQL(sql);
            dbN.insert("barcodeview", null, barcodeBitmapValues);
        }else
        {
//            String sql = "insert into barcodeview (_barcodenumber,_barcodename,_barcodetime,_barcodeinformation,_barcodeimage,_barcodebackgroundimage) values ('" + barcodeNumber +
//                    "','" + barcodeName + "','" + barcodeTime + "','" + barcodeInformation + "','" + barcodeBitmapValues + "','" + null + "')";
//            dbN.execSQL(sql);
            dbN.insert("barcodeview", null, barcodeBitmapValues);
        }

        //Toast.makeText(getApplicationContext(), "新增成功",Toast.LENGTH_LONG).show();
    }
    /**
     * 过反射改变文字的颜色
     * @param numberPicker
     * @param color
     * @return
     */
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setTextColor", e);
                }
            }
        }
        return false;
    }
    class  MyDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}