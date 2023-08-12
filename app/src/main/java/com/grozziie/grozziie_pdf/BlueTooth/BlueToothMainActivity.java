package com.grozziie.grozziie_pdf.BlueTooth;;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.grozziie.grozziie_pdf.BlueTooth.adapter.BleAdapter;
import com.grozziie.grozziie_pdf.R;
import com.tbruyelle.rxpermissions2.RxPermissions;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import de.greenrobot.event.EventBus;


public class BlueToothMainActivity extends AppCompatActivity {
    //    private static final String TAG ="ble_tag" ;
//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothManager mBluetoothManager;
//    ListView bleListView;
//    private TextView Display1;
//    private TextView Display2;
//    private TextView Display3;
//    private TextView Display4;
//    private  ImageView Search;
    private  String DiZhi="B0:55:08:55:9F:AC";
    private int i=0;
//    private BleAdapter mAdapter;
//    private List<BluetoothDevice> mDatas=new ArrayList<BluetoothDevice>();
//
//    private List<Integer> mRssis;

    public static BluetoothSocket socketall=null;
    public static BluetoothServerSocket meServerSocketall;
    //public static Boolean BlueToothFlagState=false;
    private int ABCDefg=0;
    private static final String TAG ="ble_tag" ;

    ProgressBar pbSearchBle;
    ImageView ivSerBleStatus;
    TextView tvSerBleStatus;
    TextView tvSerBindStatus;
    ListView bleListView;
    public static boolean BlueState=false;
    public static String BlueMac="";
    public static String BlueName="";
    private LinearLayout operaView;
    private Button btnWrite;
    private Button btnRead;
    private EditText etWriteContent;
    private TextView tvResponse;
    private List<BluetoothDevice> mDatas;
    private List<Integer> mRssis;
    private BleAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private boolean isScaning=false;
    private boolean isConnecting=false;
    private BluetoothGatt mBluetoothGatt;
    private Button BlueSend;
    public  static final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private   BluetoothSocket socket=null;

    private Button BlueSend7;
    private  Button BlueEsc;
    protected Handler handler;
    protected  UpdateTimer updateTimer;
    private Handler mHandler = new Handler();
    private int setWhat=0;
    private ImageView backMain;
    private View backMainView;

    //private L
    //private BleAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_device);
//        Search=findViewById(R.id.b1);
//       // Display1=(TextView) findViewById(R.id.T1);
//        Display1=findViewById(R.id.T1);
//        Display2=findViewById(R.id.T2);
//        Display3=findViewById(R.id.T3);
//        Display4=findViewById(R.id.T4);
        pbSearchBle=findViewById(R.id.progress_ser_bluetooth);
        ivSerBleStatus=findViewById(R.id.iv_ser_ble_status);
        tvSerBindStatus=findViewById(R.id.tv_ser_bind_status);
        tvSerBleStatus=findViewById(R.id.tv_ser_ble_status);
        bleListView=findViewById(R.id.ble_list_view);
        operaView=findViewById(R.id.opera_view);
        btnWrite=findViewById(R.id.btnWrite);
        btnRead=findViewById(R.id.btnRead);
        etWriteContent=findViewById(R.id.et_write);
        tvResponse=findViewById(R.id.tv_response);
        BlueSend=findViewById(R.id.BlueConnect);
        BlueSend7=findViewById(R.id.BlueSend7);
        backMain=findViewById(R.id.backMain);
        backMainView=findViewById(R.id.backMainView);
        //BlueEsc=findViewById(R.id.BlueEsc);
        getLocation(this);
        Intent intent1=getIntent();//(intent.getStringExtra("sss"));
        setWhat=intent1.getIntExtra("setWhat",0);

        initData();
        intView();
        //stateBlueTooth();
        ThreadStateBlueTooth();
        //EventBus.getDefault().register(this);


        mBluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter=mBluetoothManager.getAdapter();//耿大爷获取蓝牙适配器
        if (mBluetoothAdapter==null||!mBluetoothAdapter.isEnabled())//耿大爷判断是否支持或者判断蓝牙是否开启
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//耿大爷打开蓝牙
            startActivityForResult(intent, 0);
        }
        if(BlueName.length()!=0)
        {
            //tvSerBindStatus.setText("Connected "+BlueName);//已连接
            tvSerBindStatus.setText(R.string.bluetoothmain_connect+BlueName);
        }
    }
    private void initData() {
        mDatas=new ArrayList<>();
        mRssis=new ArrayList<>();
        mAdapter=new BleAdapter(BlueToothMainActivity.this,mDatas,mRssis);
        bleListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
//    public void setSocket (BluetoothSocket socket1)
//    {
//        this.socket=socket1;
//    }
//
//    public BluetoothSocket getSocket() {
//        return socket;
//    }

    private void intView(){
        ivSerBleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                PackageManager pm = BlueToothMainActivity.this.getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_PC))
                {
                    //mity.setText("Chrome OS");
                    //Toast.makeText(this, "Chrome OS", Toast.LENGTH_SHORT).show();
                    checkPermissions();//耿大爷android 6.0 并且targetSdkVersion>23需要再次获取位置权限
                }
                else{
                    //mity.setText("Android");
                    //Toast.makeText(this, "Android", Toast.LENGTH_SHORT).show();
                    //=========屏蔽获取蓝牙位置===============================================================================================
                    if (ContextCompat.checkSelfPermission(BlueToothMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
                        //开启定位权限,200是标识码
                        ActivityCompat.requestPermissions(BlueToothMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
                    } else {
                        //startLocation();
//                ActivityCompat.requestPermissions(BlueToothMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//                Toast.makeText(BlueToothMainActivity.this, "已开启定位权限", Toast.LENGTH_LONG).show();
                    }
                    LocationManager locationManager = (LocationManager) BlueToothMainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                    boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!isGpsAble(locationManager)||!network) {
                        //Toast.makeText(BlueToothMainActivity.this, "Bluetooth can be used after GPS is turned on", Toast.LENGTH_SHORT).show();
                        Toast.makeText(BlueToothMainActivity.this, getString(R.string.bluetoothmain_opengps)+"", Toast.LENGTH_SHORT).show();
                        openGPS2();
                    }
                    else{
                        checkPermissions();//耿大爷android 6.0 并且targetSdkVersion>23需要再次获取位置权限
                    }
                }


//                Configuration config = BlueToothMainActivity.this.getResources().getConfiguration();
//
//                // 检查设备的配置信息中是否包含chromeos属性
//                if(config.uiMode == Configuration.UI_MODE_TYPE_NORMAL
//                        && config.touchscreen == Configuration.TOUCHSCREEN_FINGER
//                        && BlueToothMainActivity.this.getPackageManager().hasSystemFeature("org.chromium.arc.device_management"))
//                {
//                    checkPermissions();//耿大爷android 6.0 并且targetSdkVersion>23需要再次获取位置权限
//                }
//                else
//                {
//                    //=========屏蔽获取蓝牙位置===============================================================================================
//                    if (ContextCompat.checkSelfPermission(BlueToothMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
//                        //开启定位权限,200是标识码
//                        ActivityCompat.requestPermissions(BlueToothMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//                    } else {
//                        //startLocation();
////                ActivityCompat.requestPermissions(BlueToothMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
////                Toast.makeText(BlueToothMainActivity.this, "已开启定位权限", Toast.LENGTH_LONG).show();
//                    }
//                    LocationManager locationManager = (LocationManager) BlueToothMainActivity.this.getSystemService(Context.LOCATION_SERVICE);
//                    boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//                    if (!isGpsAble(locationManager)||!network) {
//                        //Toast.makeText(BlueToothMainActivity.this, "Bluetooth can be used after GPS is turned on", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(BlueToothMainActivity.this, getString(R.string.bluetoothmain_opengps)+"", Toast.LENGTH_SHORT).show();
//                        openGPS2();
//                    }
//                    else{
//                        checkPermissions();//耿大爷android 6.0 并且targetSdkVersion>23需要再次获取位置权限
//                    }
//                }


                //checkPermissions();



//========================================================================================================
                //scanDevice();//耿大爷targetSdkVersion<23直接调用查询

                /*
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Display1.setText("停止搜索");
                    }
                },5000);*/

            }
        });
//        BlueEsc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String res = "";
//
//
////                Intent intent=new Intent();
////
////                intent.setClass(MainActivity.this,SendPictureActivity.class);
////                Bundle bundle=new Bundle();
////                bundle.putString("sss",socket.toString());
////                intent.putExtras(bundle);
//
//                //setSocket(socket);
//
////                Intent intent1=new Intent();
////                intent1.putExtra("sssss",socket);
//
//                //BluetoothConnect sss =new BluetoothConnect();
//
//                //sss.setSocket(socket);
//                try{
//
//                    // 读取raw文件夹中的txt文件,将它放入输入流中
//                    InputStream in = getResources().openRawResource(R.raw.esc);
//                    // 读取assets文件夹中的txt文件,将它放入输入流中
//                    //InputStream in = getResources().getAssets().open("a.prn");
//                    // 获得输入流的长度
//                    int length = in.available();
//                    // 创建字节输入
//                    byte[] buffer = new byte[length];
//                    // 放入字节输入中
//                    in.read(buffer);
////            // 获得编码格式
////            String type = codetype(buffer);
////            // 设置编码格式读取TXT
////            res = EncodingUtils.getString(buffer, type);
////            // 关闭输入流
////
////            tvtext.setText(res);
//
//                    in.close();
//
//
//
//
//                    //byte[] XmsgBuffer=new byte[]{0x70,0x72,0x69,0x6E,0x74,0x0A};
//                    OutputStream os1 = socket.getOutputStream();
//                    os1.write(buffer);
//                    //os1.write(XmsgBuffer);
//                    return ;
//
//                }
//                catch (IOException e)
//                {
//                    Log.e(TAG,"s");
//                }
//
//                if(socket==null)
//                    Log.e(TAG,"null is socket 1");
//                else
//                    Log.e(TAG, socket.toString());
//
//                Intent intent=new Intent(MainActivity.this, SendPictureActivity.class);
//                intent.putExtra("s", String.valueOf(socket));
//                startActivity(intent);
//                //startActivity(new Intent(MainActivity.this,SendPictureActivity.class));
//                handler =new Handler();
//                updateTimer=new UpdateTimer();
//                handler.postDelayed(updateTimer,500);
//
////                try{
////                    String String_S="ABCDEFG";
////
////                    byte[] msgBuffer = String_S.getBytes();
////
//////                    XmsgBuffer[0]=0x1B;
//////                    XmsgBuffer[1]=0x4A;
//////                    XmsgBuffer[2]=0x30;
//////                    XmsgBuffer[3]=0x0D;
//////                    XmsgBuffer[4]=0x0A;
//////                    msgBuffer[0] = 0x1B;
//
//////                    msgBuffer[1] = 0x4A;
//////                    msgBuffer[2] = 0x10;
////                    OutputStream os = socket.getOutputStream();
////                    os.write(msgBuffer);
////                }
////                catch (IOException e)
////                {
////                    Log.e(TAG,"s");
////                }
//            }
//
//        });
        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BlueSend7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                String res = "";
//
//
////                Intent intent=new Intent();
////
////                intent.setClass(MainActivity.this,SendPictureActivity.class);
////                Bundle bundle=new Bundle();
////                bundle.putString("sss",socket.toString());
////                intent.putExtras(bundle);
//
//                //setSocket(socket);
//
////                Intent intent1=new Intent();
////                intent1.putExtra("sssss",socket);
//
//                //BluetoothConnect sss =new BluetoothConnect();
//
//                //sss.setSocket(socket);
//                try{
//
//                    // 读取raw文件夹中的txt文件,将它放入输入流中
//                    InputStream in = getResources().openRawResource(R.raw.cpcl);
//                    // 读取assets文件夹中的txt文件,将它放入输入流中
//                    //InputStream in = getResources().getAssets().open("a.prn");
//                    // 获得输入流的长度
//                    int length = in.available();
//                    // 创建字节输入
//                    byte[] buffer = new byte[length];
//                    // 放入字节输入中
//                    in.read(buffer);
////            // 获得编码格式
////            String type = codetype(buffer);
////            // 设置编码格式读取TXT
////            res = EncodingUtils.getString(buffer, type);
////            // 关闭输入流
////
////            tvtext.setText(res);
//
//                    in.close();
//
//
//
//
//                    //byte[] XmsgBuffer=new byte[]{0x70,0x72,0x69,0x6E,0x74,0x0A};
//                    OutputStream os1 = socket.getOutputStream();
//                    os1.write(buffer);
//                    //os1.write(XmsgBuffer);
//                    return ;
//
//                }
//                catch (IOException e)
//                {
//                    Log.e(TAG,"s");
//                }
//
//                if(socket==null)
//                    Log.e(TAG,"null is socket 1");
//                else
//                    Log.e(TAG, socket.toString());
//
//                Intent intent=new Intent(BlueToothMainActivity.this, SendPictureActivity.class);
//                intent.putExtra("s", String.valueOf(socket));
//                startActivity(intent);
//                //startActivity(new Intent(MainActivity.this,SendPictureActivity.class));
//                handler =new Handler();
//                updateTimer=new UpdateTimer();
//                handler.postDelayed(updateTimer,500);
//
////                try{
////                    String String_S="ABCDEFG";
////
////                    byte[] msgBuffer = String_S.getBytes();
////
//////                    XmsgBuffer[0]=0x1B;
//////                    XmsgBuffer[1]=0x4A;
//////                    XmsgBuffer[2]=0x30;
//////                    XmsgBuffer[3]=0x0D;
//////                    XmsgBuffer[4]=0x0A;
//////                    msgBuffer[0] = 0x1B;
//
//////                    msgBuffer[1] = 0x4A;
//////                    msgBuffer[2] = 0x10;
////                    OutputStream os = socket.getOutputStream();
////                    os.write(msgBuffer);
////                }
////                catch (IOException e)
////                {
////                    Log.e(TAG,"s");
////                }
            }
        });
        BlueSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // byte[] msgBuffer = message.getBytes();
                //              byte[] XmsgBuffer=new byte[]{0x70,0x72,0x69,0x6E,0x74,0x0A};
//                    XmsgBuffer[0]=0x1B;
//                    XmsgBuffer[1]=0x4A;
//                    XmsgBuffer[2]=0x30;
//                    XmsgBuffer[3]=0x0D;
//                    XmsgBuffer[4]=0x0A;
//                    msgBuffer[0] = 0x1B;
//                    msgBuffer[1] = 0x4A;
//                    msgBuffer[2] = 0x10;



                try {


                    ArrayList<Byte> Gray_Arraylist;
                    Gray_Arraylist=new ArrayList<Byte>();
                    Byte[]Gray_Send;





                    //List<byte> list=new ArrayList<byte>();
                    int Gray_i=0;
                    int alpha = 0xFF << 24;
                    int []i_G=new int[7];
                    int Send_Gray=0x00;
//        String Start_S="@<"+hexStr2Str("0D0A0000")+"#<";
////        String String_S="";
////
////        String End_S=">#@>";
                    int StartInt=0;
                    char  StartWords='@';



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
                    for(int xp=1;xp<2;xp++)//120
                    {
                        StartWords = '#';
                        //StartInt=Integer.getInteger(StartWords);
                        Gray_Arraylist.add((byte) StartWords);
                        Gray_i++;

                        StartWords = '<';
                        //StartInt=Integer.getInteger(StartWords);
                        Gray_Arraylist.add((byte) StartWords);
                        Gray_i++;


                        for(int xpj=1;xpj<145;xpj++)
                        {
                            StartInt = 0x80;
                            Gray_Arraylist.add((byte) StartInt);
                            Gray_i++;

                            StartInt = 0x01;
                            Gray_Arraylist.add((byte) StartInt);
                            Gray_i++;
                            StartInt = 0x83;
                            Gray_Arraylist.add((byte) StartInt);
                            Gray_i++;

                            StartInt = 0x01;
                            Gray_Arraylist.add((byte) StartInt);
                            Gray_i++;
                        }
                        StartWords = '#';
                        //StartInt=Integer.getInteger(StartWords);
                        Gray_Arraylist.add((byte) StartWords);
                        Gray_i++;

                        StartWords = '>';
                        //StartInt=Integer.getInteger(StartWords);
                        Gray_Arraylist.add((byte) StartWords);
                        Gray_i++;
                    }




//                for(int xp=1;xp<50;xp++)
//                {
//
//                    StartWords = '#';
//                    //StartInt=Integer.getInteger(StartWords);
//                    Gray_Arraylist.add((byte) StartWords);
//                    Gray_i++;
//
//                    StartWords = '<';
//                    //StartInt=Integer.getInteger(StartWords);
//                    Gray_Arraylist.add((byte) StartWords);
//                    Gray_i++;
//
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x80;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                //100
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x80;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                //200
//
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x80;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//                //300
//
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//
//                    StartInt = 0x80;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//                //400
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//
//                    StartInt = 0x80;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x31;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//                //500
//
//                    StartInt = 0x85;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//                    StartInt = 0x4B;
//                    Gray_Arraylist.add((byte) StartInt);
//                    Gray_i++;
//
//
//                    StartWords = '#';
//                    //StartInt=Integer.getInteger(StartWords);
//                    Gray_Arraylist.add((byte) StartWords);
//                    Gray_i++;
//
//                    StartWords = '>';
//                    //StartInt=Integer.getInteger(StartWords);
//                    Gray_Arraylist.add((byte) StartWords);
//                    Gray_i++;
//
//                }
                    StartWords = '@';
                    //StartInt=Integer.getInteger(StartWords);
                    Gray_Arraylist.add((byte) StartWords);
                    Gray_i++;

                    StartWords = '>';
                    //StartInt=Integer.getInteger(StartWords);
                    Gray_Arraylist.add((byte) StartWords);
                    Gray_i++;


                    byte[] sss=new byte[Gray_Arraylist.size()];
                    Gray_Send=new Byte[Gray_Arraylist.size()];
                    Gray_Arraylist.toArray(Gray_Send);
                    for(int xx=0;xx<Gray_Send.length;xx++){
                        sss[xx]=Gray_Send[xx];
                    }
                    OutputStream os = socket.getOutputStream();
                    byte[] sssy=new byte[1];
                    sssy[0]=0x01;
                    //sssy[1]=0x02;
                    os.write(sss);

                    //====================================================
//                    String message = "";//hexStr2Str("7072696E740A");
//                    String Start_S="@<"+hexStr2Str("0D0A0000")+"#<";
//                    String String_S="";
//
//                    String End_S=">#@>";
//                    for(int i=1;i<217;i++)
//                    {
//                        String_S+="A";
//                    }
//                    for(int i=1;i<217;i++)
//                    {
//                        String_S+="B";
//                    }
//                    for(int i=1;i<217;i++)
//                    {
//                        String_S+="C";
//                    }
//                    for(int i=1;i<217;i++)
//                    {
//                        String_S+="D";
//                    }
//                    message=Start_S+String_S+End_S;
//                    byte[] msgBuffer = message.getBytes();
//                    byte[] XmsgBuffer=new byte[]{0x70,0x72,0x69,0x6E,0x74,0x0A};
////                    XmsgBuffer[0]=0x1B;
////                    XmsgBuffer[1]=0x4A;
////                    XmsgBuffer[2]=0x30;
////                    XmsgBuffer[3]=0x0D;
////                    XmsgBuffer[4]=0x0A;
////                    msgBuffer[0] = 0x1B;
////                    msgBuffer[1] = 0x4A;
////                    msgBuffer[2] = 0x10;
//                    OutputStream os = socket.getOutputStream();
//                    os.write(XmsgBuffer);
                    //=====================================================================================
                }
                catch (IOException e)
                {
                    Log.e(TAG,"s");
                }
            }
        });
        bleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(ListViewActivity.this,"你点击了"+position,Toast.LENGTH_SHORT).show();

                Log.e(TAG,"4连接中"+BlueMac);
                Log.e(TAG,"5"+BlueState);

                mBluetoothAdapter.cancelDiscovery();
                isScaning=false;
                pbSearchBle.setVisibility(View.GONE);
//                tvSerBindStatus.setText("Search completed");//搜索已停止
//
//                tvSerBindStatus.setText("Connecting devices");//连接中

                tvSerBindStatus.setText(R.string.bluetoothmain_complete);//搜索已停止

                tvSerBindStatus.setText(R.string.bluetoothmain_connecting);//连接中
                TextView xx = view.findViewById(R.id.introduce);
                xx.getText();
                if(!BlueState) {


                    //tvSerBindStatus.setText("Connecting devices");//连接中
                    tvSerBindStatus.setText(R.string.bluetoothmain_connecting);//连接中
                    //unregisterReceiver(bluetoothReceiver);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(xx.getText().toString());
                    try {
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        meServerSocketall = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
                                "angenlblue", MY_UUID);
                        Log.e(TAG, xx.getText().toString());
                        socket.connect();

////=================================增加
//                        Method m = null;
//                        try {
//                            m = device.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
//                            socket = (BluetoothSocket) m.invoke(device,1);
//
//                            socket.connect();
//                        }
//                        catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                        catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                        catch (NoSuchMethodException e) {
//                            e.printStackTrace();
//                        }
////=============================================================================


                    } catch (IOException e) {
                        Log.e(TAG, "e");
                    }
                    Log.e(TAG,"6"+BlueState);
                    //tvSerBindStatus.setText("Devices not connected");//没连接设备
                    tvSerBindStatus.setText(R.string.bluetoothmain_devicenotconnect);//没连接设备
                    //stateBlueTooth();

                }

                else if(BlueState&& !BlueMac.equals(xx.getText().toString()))
                {
                    BlueState=false;
                    BlueMac="";
                    BlueName="";
                    try {
                        if(socketall.isConnected())
                            socketall.close();
                            socketall=null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //tvSerBindStatus.setText("Connecting devices");//连接中
                    tvSerBindStatus.setText(R.string.bluetoothmain_connecting);//连接中
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(xx.getText().toString());
                    try {
                        socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                        meServerSocketall = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
                                "angenlblue", MY_UUID);
                        socket.connect();

//                        //=================================增加
//
//
//
//
//                        try {
//                            Method m = null;
//                            m = device.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
//                            socket = (BluetoothSocket) m.invoke(device,1);
//
//                            socket.connect();
//                        }
//                        catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        }
//                        catch (InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                        catch (NoSuchMethodException e) {
//                            e.printStackTrace();
//                        }
////=============================================================================

                        Log.e(TAG,"6.5"+xx.getText().toString());

                    } catch (IOException e) {
                        Log.e(TAG, "e");
                    }
                    //tvSerBindStatus.setText("Devices not connected");//没连接设备
                    tvSerBindStatus.setText("R.string.bluetoothmain_devicenotconnect");//没连接设备
                    Log.e(TAG,"7"+BlueState+xx.getText().toString());
                }
                else
                {
                    //Toast.makeText(BlueToothMainActivity.this, "已连接"+BlueName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(BlueToothMainActivity.this, getString(R.string.bluetoothmain_connect)+BlueName, Toast.LENGTH_SHORT).show();
                    //tvSerBindStatus.setText("Connected "+BlueName);//已连接
                    tvSerBindStatus.setText(R.string.bluetoothmain_connect+BlueName);//已连接

                    Log.e(TAG,"8"+BlueState+xx.getText().toString());
                    Log.e(TAG,"9已连接");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Display1.setText("停止搜索");
                            if(socket==null)
                            {
                                //Intent intent=new Intent(BlueToothMainActivity.this, AngenlActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                            else{
                                socketall=socket;

                                //Intent intent=new Intent(BlueToothMainActivity.this, AngenlActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                        }
                    },1000);
                }
            }
        });

    }
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    private void checkPermissions() {
        RxPermissions rxPermissions = new RxPermissions(BlueToothMainActivity.this);
        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new io.reactivex.functions.Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // 用户已经同意该权限
                            //scanDevice();//BLE蓝牙搜索
                            serchBlueTooth();
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //ToastUtils.showLong("用户开启权限后才能使用");
                            //Toast.makeText(BlueToothMainActivity.this,"The user can only use it after opening the permission",Toast.LENGTH_SHORT).show();
                            Toast.makeText(BlueToothMainActivity.this,getString(R.string.bluetoothmain_openpermission)+"",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    private  void serchBlueTooth()
    {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) //不在可被搜索的范围
        {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);//设置本机蓝牙在300秒内可见
            startActivity(discoverableIntent);
        }
        if (mBluetoothAdapter.isDiscovering()) {
            //判断蓝牙是否正在扫描，如果是调用取消扫描方法；如果不是，则开始扫描
            mBluetoothAdapter.cancelDiscovery();
            isScaning=false;
            pbSearchBle.setVisibility(View.GONE);
            //tvSerBindStatus.setText("Search completed");//搜索已停止
            tvSerBindStatus.setText(R.string.bluetoothmain_complete);//搜索已停止
            Log.e(TAG,"3"+BlueState);
            //stateBlueTooth();
            if(BlueState==true)
                //tvSerBindStatus.setText("Connected "+BlueName);//已连接
                tvSerBindStatus.setText(R.string.bluetoothmain_connect+BlueName);//已连接

            else
                //tvSerBindStatus.setText("Devices not connected");//没连接设备
                tvSerBindStatus.setText(R.string.bluetoothmain_devicenotconnect);//没连接设备
            //onDestroy();
        } else {
            mBluetoothAdapter.startDiscovery();
            isScaning=true;
            pbSearchBle.setVisibility(View.VISIBLE);
            //tvSerBindStatus.setText("Searching devices");//正在搜索
            tvSerBindStatus.setText(R.string.bluetoothmain_search);//正在搜索
            Log.e(TAG,"1正在搜索");
            Log.e(TAG,"2"+BlueState);
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//注册广播接收信号
            registerReceiver(bluetoothReceiver, intentFilter);//用BroadcastReceiver 来取得结果
        }
        //onDestroy();


    }
//    protected  void onDestroy(){
//        super.onDestroy();
//        unregisterReceiver(bluetoothReceiver);
//
//    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //int blueState=intent.getIntExtra(mBluetoothAdapter.EXTRA_STATE,0);
            //String aaa;
            // aaa=Integer.toString(blueState);
            // Log.e(TAG,aaa);
            // if(blueState==mBluetoothAdapter.STATE_TURNING_ON)
            //{tvSerBindStatus.setText("已打开");
            //     Log.e(TAG,"已打开AAAAAAAAAAAAAAAAAAAAAAAA");
            // }

            // else
            //{
            Log.e(TAG,"BBBBBBBBBBBBBBB");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG,"设备名："+device.getName()+"\n" +"设备地址："+device.getAddress() + "\n");//将搜索到的蓝牙名称和地址添加到列表。
                if (!mDatas.contains(device)){
                    mDatas.add(device);
                    mRssis.add(i++);
                    mAdapter.notifyDataSetChanged();
                }
            }
            //}
        }
    };
    private void ThreadStateBlueTooth()
    {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stateBlueTooth();
            }
        }, 500);

    }
    private void  stateBlueTooth()
    {
        //IntentFilter intentFilter1= new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);//注册广播连接状态
        IntentFilter intentFilter1= new IntentFilter();
        intentFilter1.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter1.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter1.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter1.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);

        registerReceiver(bluetoothState, intentFilter1);//用BroadcastReceiver 来取得结果
//        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);//注册广播接收信号
//        registerReceiver(bluetoothState, intentFilter1);//用BroadcastReceiver 来取得结果
//
//IntentFilter intentFilter1= new IntentFilter();
//        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);//注册广播接收信号
//        registerReceiver(bluetoothState, intentFilter);//用BroadcastReceiver 来取得结果



        Log.e(TAG,"hahahah");
    }


//    public class MyBroadcast extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//    }

    private final BroadcastReceiver bluetoothState = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice device =intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            Log.e(TAG,"16"+action);

            if(action!=null)
                switch (action)
                {
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        int blueState=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,0);
                        switch (blueState)
                        {
                            case BluetoothAdapter.STATE_ON:
                                BlueState=false;
                                BlueMac="";
                                BlueName="";
                                Log.e(TAG,"10蓝牙已经打开了");
                                //tvSerBindStatus.setText("ON");//已打开
                                tvSerBindStatus.setText(R.string.bluetoothmain_on);//已打开
                                break;
                            case BluetoothAdapter.STATE_OFF:
                                Log.e(TAG,"11蓝牙已经关闭了");
                                BlueState=false;
                                BlueMac="";
                                BlueName="";
                                //tvSerBindStatus.setText("OFF");//已关闭
                                tvSerBindStatus.setText(R.string.bluetoothmain_off);//已关闭
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTED:
                                Log.e(TAG,"17"+action);
                                BlueState=false;
                                BlueMac="";
                                BlueName="";
                                //tvSerBindStatus.setText("Disconnected");//已断开
                                tvSerBindStatus.setText(R.string.bluetoothmain_disconnect);//已断开
                                break;
                            case BluetoothAdapter.STATE_DISCONNECTING:
                                Log.e(TAG,"18"+action);
                                BlueState=false;
                                BlueMac="";
                                BlueName="";
                                //tvSerBindStatus.setText("Disconnected");//已断开
                                tvSerBindStatus.setText(R.string.bluetoothmain_disconnect);//已断开
                                break;
                        }
                        Log.e(TAG,"12-----------");
                        break;
                    case BluetoothDevice.ACTION_ACL_CONNECTED:

                        Log.e(TAG,"13已连接");
                        BlueMac=device.getAddress().toString();
                        BlueName=device.getName().toString();

                        //tvSerBindStatus.setText("Connected "+ device.getName().toString());//已连接
                        tvSerBindStatus.setText(R.string.bluetoothmain_connect+ device.getName().toString());//已连接
                        BlueState=true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Display1.setText("停止搜索");
                                if(socket==null)
                                {
                                    //Intent intent=new Intent(BlueToothMainActivity.this, AngenlActivity.class);
                                    //startActivity(intent);
                                    finish();
                                }
                                else{
                                    socketall=socket;

                                    //Intent intent=new Intent(BlueToothMainActivity.this, AngenlActivity.class);
                                    //startActivity(intent);
                                    finish();
                                }
                            }
                        },1000);
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        Log.e(TAG,"14已经断开");
                        BlueState=false;
                        BlueMac="";
                        BlueName="";
                        //tvSerBindStatus.setText("Begin to connect");//请连接
                        tvSerBindStatus.setText(R.string.bluetoothmain_beginconnect);//请连接
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                        Log.e(TAG,"19已经断开");
                        BlueState=false;
                        BlueMac="";
                        BlueName="";
                        //tvSerBindStatus.setText("Begin to connect");//请连接
                        tvSerBindStatus.setText(R.string.bluetoothmain_beginconnect);//请连接
                        break;

                }
            else
            {
                Log.e(TAG,"15null");
            }
            //int blueState=intent.getIntExtra(mBluetoothAdapter.EXTRA_STATE,0);
            //String aaa;
            // aaa=Integer.toString(blueState);
            // Log.e(TAG,aaa);
            // if(blueState==mBluetoothAdapter.STATE_TURNING_ON)
            //{tvSerBindStatus.setText("已打开");
            //     Log.e(TAG,"已打开AAAAAAAAAAAAAAAAAAAAAAAA");
            // }

            // else
            //{
//            Log.e(TAG,"BBBBBBBBBBBBBBB");
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Log.e(TAG,"设备名："+device.getName()+"\n" +"设备地址："+device.getAddress() + "\n");//将搜索到的蓝牙名称和地址添加到列表。
//                if (!mDatas.contains(device)){
//                    mDatas.add(device);
//                    mRssis.add(i++);
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
            //}
        }
    };

    private void scanDevice(){
        //tvSerBindStatus.setText("Searching devices");//正在搜索
        tvSerBindStatus.setText(R.string.bluetoothmain_search);//正在搜索
        //isScaning=true;
        //pbSearchBle.setVisibility(View.VISIBLE);
        Log.e(TAG, "run: 搜索...");
        mBluetoothAdapter.startLeScan(scanCallback);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //结束扫描
                mBluetoothAdapter.stopLeScan(scanCallback);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //isScaning=false;
                        //pbSearchBle.setVisibility(View.GONE);
                        //Display1.setText("搜索已结束");
                    }
                });
            }
        },5000);
    }
    BluetoothAdapter.LeScanCallback scanCallback=new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e(TAG, device.getAddress());
            // Display3.setText(device.toString());



            BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            String getName=mBluetoothAdapter.getName();
            Log.e(TAG, "qqq"+getName);
            tvSerBindStatus.setText(device.getAddress());

            tvSerBindStatus.setText(device.getName());
            //Log.e(TAG, Display4.toString());
            //if (!mDatas.contains(device)){
            String  LanYa ="2A:7B:8D:1F:7B:F3";
            String  LanYa1="2A:7B:8D:1F:7B:F3";
            //Display1.setText(LanYa);
            // String  DiZhi="DC:0D:30:43:DD:4B";
            String  DiZhi1="DC:0D:30:42:A6:3D";
            String  DiZhi2="47:E6:B6:8A:06:31";
            String  DiZhi3="11:17:B8:B9:78:D0";
            if (DiZhi.equals(device.getAddress().toString())){
                //mDatas.add(device);
                tvSerBindStatus.setText("1123");

                // Display2.setText(device.getAddress());
                //Display2.setText(device.getBondState());
                // mRssis.add(rssi);
                //mAdapter.notifyDataSetChanged();
            }
            if(LanYa1.equals(device.getAddress().toString())) {
                tvSerBindStatus.setText("5566");
                //Display2.setText(device.getName().toString());
            }
            if(LanYa1==device.getAddress()) {
                tvSerBindStatus.setText(device.getName());
            }
            if(LanYa1==device.getAddress()) {
                tvSerBindStatus.setText(device.getName());
            }

        }
    };
    class UpdateTimer implements Runnable{
        public void run()
        {
            EventBus.getDefault().post(new FirstEvent(socket));
            handler=null;
//            if(handler!=null)
//                handler.postDelayed(this,UPDATE_EVERY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//    //保持cpu一直运行，不管屏幕是否黑屏
//    //wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
//    PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
//
//        wakeLock.acquire();
}

    @Override
    protected void onDestroy()
    {
        //setSocket(socket);
        super.onDestroy();
        //setSocket(socket);
        if(socket==null)
        {
            Log.e(TAG, "null is sockey2");
        }
        else
            Log.e(TAG, "66666");
        unregisterReceiver(bluetoothState);

    }
    private void getLocation(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!isGpsAble(locationManager)) {
            //Toast.makeText(BlueToothMainActivity.this, "Bluetooth can be used after GPS is turned on", Toast.LENGTH_SHORT).show();
            Toast.makeText(BlueToothMainActivity.this, getString(R.string.bluetoothmain_opengps)+"", Toast.LENGTH_SHORT).show();
            openGPS2();
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, strings, 18);
        }else {
            //startLocation();
            //Toast.makeText(BlueToothMainActivity.this, "已开启定位权限", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }
    private void openGPS2() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 100);
    }


}
