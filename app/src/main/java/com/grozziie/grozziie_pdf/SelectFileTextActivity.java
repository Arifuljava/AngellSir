package com.grozziie.grozziie_pdf;;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.aspose.words.Document;
import com.grozziie.grozziie_pdf.listview.MyFileListAdapter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class SelectFileTextActivity extends AppCompatActivity {
    protected final int PERMISSION_REQUEST = 42;
    private Button SelectWeixin;
    private Button SelectQQ;
    private Button BarcodeEditButton;
    private Button SelectOther;
    private Button FileWeixinDBtn;
    private Button FileWeixinSBtn;
    private Button FileImageBtnX;
    private Button FileHtmlBtn;
    private ImageButton ShopeeButton;
    private ImageButton LazadaButton;
    private ImageView PDFImageTop;
    private String path;
    private Uri document = null;
    private ListView fileListview;
    private List<String> list;
    private List<String> date;
    private MyFileListAdapter myFileListAdapter;
    private final String mformatType="yyyy/MM/dd HH:mm:ss";
    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    private final String outputPDF = storageDir + "Angenl_PDF.pdf";
    private String fileAddress="";
    private int add_Flag=0;
    private ProgressDialog progressDialog=null;
    private final int mDuraction = 2000; // 两次返回键之间的时间差
    private long mLastTime = 0; // 最后一次按back键的时刻
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file_text);
        list = new ArrayList<>();
        date=new ArrayList<>();
        SelectWeixin=findViewById(R.id.FileWeixinBtn);
        SelectQQ=findViewById(R.id.FileQQBtn);
        SelectOther=findViewById(R.id.FileOtherBtn);
        ShopeeButton=findViewById(R.id.ShopeeButton);
        LazadaButton=findViewById(R.id.LazadaButton);
        PDFImageTop=findViewById(R.id.PDFImageTop);
        //fileListview=findViewById(R.id.fileListview);
        //FileWeixinDBtn=findViewById(R.id.FileWeixinDBtn);
        BarcodeEditButton=findViewById(R.id.BarcodeEditButton);
        FileWeixinSBtn=findViewById(R.id.FileWeixinSBtn);
        FileImageBtnX=findViewById(R.id.FileImageBtnX);
        FileHtmlBtn=findViewById(R.id.FileHtmlBtn);
        progressDialog=new ProgressDialog(SelectFileTextActivity.this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        checkStorageManagerPermission();
        checkPermission();
        // apply the license if you have the Aspose.Words license...
        //applyLicense();
        BarcodeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFileTextActivity.this, AngenlActivity.class);//BarcodeActivity
                //intent.putExtra("setWhat",1);
                startActivity(intent);
            }
        });
        SelectQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SelectFileTextActivity.this, BlueToothMainActivity.class);
//                intent.putExtra("setWhat",1);
//                startActivity(intent);
                Intent intent = new Intent(SelectFileTextActivity.this, PDFSetActivity.class);
                //intent.putExtra("setWhat",1);
                startActivity(intent);

            }
        });

        LazadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://www.lazada.com.my/shop/shinprinter");//此处填链接
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        ShopeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://shopee.com.my/grozziie_printer?categoryId=100644&itemId=18702252991");//此处填链接
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        FileWeixinSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
// TODO: handle exception
                    //SelectFileTextActivity().showToastLong("检查到您⼿机没有安装微信，请安装后使⽤该功能");
                    //Toast.makeText(SelectFileTextActivity.this,"Please install wechat first", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwechat)+"", Toast.LENGTH_SHORT).show();

                }
            }
        });
        FileHtmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectFileTextActivity.this, HtmlActivity.class);
                //intent.putExtra("setWhat",1);
                startActivity(intent);
            }
        });
        FileImageBtnX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.addCategory(Intent.CATEGORY_OPENABLE);

                intent.setType("image/*");

                startActivityForResult(intent, 1);
            }
        });

//        FileWeixinDBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sss=Environment.getExternalStorageDirectory()+"";
//                sss=getResources().getConfiguration().locale.getCountry();
//                String Filepath= Environment.getExternalStorageDirectory()+"/Download/WeChat/";
//
//                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
//                    if(getResources().getConfiguration().locale.getCountry().equals("CN"))
//                    {
//                        Filepath= Environment.getExternalStorageDirectory()+"/Download/WeiXin/";
//                    }
//                    else
//                    {
//                        Filepath= Environment.getExternalStorageDirectory()+"/Download/WeChat/";
//                    }
//                }
//                else
//                {
//                     Filepath= Environment.getExternalStorageDirectory()+"/Android/data/com.tencent.mm/MicroMsg/Download";
//                }
//
//
//
//
//                //String xxxx=Environment.getExternalStorageDirectory().getAbsolutePath();
//                ;
//                fileAddress=Filepath;
//                //ListViewActivity listViewActivity=new ListViewActivity();
//                myFileListAdapter=new MyFileListAdapter(SelectFileTextActivity.this,Filepath);
//                // fileListview.setAdapter(new MyFileListAdapter(SelectFileTextActivity.this,Filepath));
//
//                fileListview.setAdapter(myFileListAdapter);
//                list.clear();
//                File file = new File(Filepath);
//                if(null==file || !file.exists()){
//                    return;
//                }
//                File[] files =file.listFiles();
//                for(int i=0;i<files.length;i++)
//                {
//                    if(!files[i].isDirectory()) {
//                        String xxx = files[i].getName().toString();
//                        String HouZhui=xxx.substring(xxx.lastIndexOf(".")+1).toLowerCase();
//
//                        if(HouZhui.equals("pdf")) {
//                            list.add(xxx);
//                            long time = files[i].lastModified();
//                            xxx = Long.toString(time);
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTimeInMillis(time);
//                            //xxx=cal.getTime().toString();
//                            SimpleDateFormat formatter = new SimpleDateFormat(mformatType);
//                            xxx = formatter.format(cal.getTime());
//                            date.add(xxx);
//                        }
//                    }
//
//                }
//
//                //file
//
//
//                myFileListAdapter.setMyFileListAdapter(list,date);
//            }
//        });
        SelectWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsAPP2(SelectFileTextActivity.this);

//                if (Build.VERSION.SDK_INT >= 30) {
//
//
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                        intent.setData(Uri.parse("package:" + "com.whatsapp"));
//                        startActivity(intent);
//
//                }
                if(isAppInstall(SelectFileTextActivity.this,"com.whatsapp"))
                {
                    try{
                        SelectFileTextActivity.this.startActivity(getAppOpenIntentByPackageName(SelectFileTextActivity.this,"com.whatsapp"));
                    } catch (Exception e) { //  没有安装WhatsApp

                        //Toast.makeText(SelectFileTextActivity.this,"Please authorize to read the app list", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_applist)+"", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
                }

//                //chatInWhatsApp(SelectFileTextActivity.this);
//                if(add_Flag==0)
//                openWhatApp(SelectFileTextActivity.this);
//                else if(add_Flag==1)
//                    chatInWhatsApp(SelectFileTextActivity.this);
//                else if(add_Flag==2)
//                    openWhatsAPP2(SelectFileTextActivity.this);
//                else if(add_Flag==3)
//                    ;
//                add_Flag++;
//                if(add_Flag>3)
//                    add_Flag=0;


//                //String Filepath="/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download";
//                //String Filepath="/storage/emulated/0/Download";
//                String sss=Environment.getExternalStorageDirectory()+"";
//                //String xxxx=Environment.getExternalStorageDirectory().getAbsolutePath();
//                String Filepath= Environment.getExternalStorageDirectory()+"/Android/data/com.tencent.mm/MicroMsg/Download";
//
//
//                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
//                    if(getResources().getConfiguration().locale.getCountry().equals("CN"))
//                    {
//                        Filepath= Environment.getExternalStorageDirectory()+"/Download/WeiXin/";
//                    }
//                    else
//                    {
//                        Filepath= Environment.getExternalStorageDirectory()+"/Download/WeChat/";
//                    }
//                }
//                else
//                {
//                    Filepath= Environment.getExternalStorageDirectory()+"/Android/data/com.tencent.mm/MicroMsg/Download";
//                }
//                fileAddress=Filepath;
//                //ListViewActivity listViewActivity=new ListViewActivity();
//                myFileListAdapter=new MyFileListAdapter(SelectFileTextActivity.this,Filepath);
//               // fileListview.setAdapter(new MyFileListAdapter(SelectFileTextActivity.this,Filepath));
//
//                fileListview.setAdapter(myFileListAdapter);
//                list.clear();
//                File file = new File(Filepath);
//                if(null==file || !file.exists()){
//                    return;
//                }
//                File[] files =file.listFiles();
//                for(int i=0;i<files.length;i++)
//                {
//                            if(!files[i].isDirectory()) {
//                                String xxx = files[i].getName().toString();
//                                String HouZhui=xxx.substring(xxx.lastIndexOf(".")+1).toLowerCase();
//
//                                if(HouZhui.equals("pdf")) {
//                                    list.add(xxx);
//                                    long time = files[i].lastModified();
//                                    xxx = Long.toString(time);
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.setTimeInMillis(time);
//                                    //xxx=cal.getTime().toString();
//                                    SimpleDateFormat formatter = new SimpleDateFormat(mformatType);
//                                    xxx = formatter.format(cal.getTime());
//                                    date.add(xxx);
//                                }
//                            }
//
//                }
//
//                //file
//
//
//                myFileListAdapter.setMyFileListAdapter(list,date);

            }
        });
        SelectOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

               // Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                if (mimeTypes != null) {
//                    intent.putExtra(Intent.EXTA_MIME_TYPES, mimeTypes);
//                }
                final String DOC = "application/msword";
                final String XLS = "application/vnd.ms-excel";
                final String PPT = "application/vnd.ms-powerpoint";
                final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                final String XLS1 = "application/x-excel";
                final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
                final String PDF = "application/pdf";
                String[] mimeTypes = {DOC, DOCX, PDF, PPT, PPTX, XLS, XLS1, XLSX};
                //String[] mimeTypes = {PDF};
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
               //Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:Download/WeiXin/");
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                //intent.setType("image/*");
                //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
                startActivityForResult(intent, 1);


            }
        });
//        fileListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Toast.makeText(ListViewActivity.this,"你点击了"+position,Toast.LENGTH_SHORT).show();
//
//
//
//
//
//                TextView fileTextAddress=view.findViewById(R.id.tV_Title);
//                String pathX=fileAddress+"/"+fileTextAddress.getText().toString();
//
//                File file = new File(pathX);
//                if(null==file || !file.exists()){
//                    return;
//                }
//                else{
//                    Intent intent=new Intent(SelectFileTextActivity.this, ViewFlipperOnTouchActivity.class);
//                    intent.putExtra("getPath", String.valueOf(pathX));
//                    startActivity(intent);
//                }
////                OkHttp sss = new OkHttp();
////                sss.uploadFile(file, new SetActivity.VolleyCallback() {
////                    @Override
////                    public void onSuccess(String result) {
////
////                    }
////                });
//                Toast.makeText(SelectFileTextActivity.this,"Loading...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
        }
    public void applyLicense()
    {
        // set license
        //License lic= new License();
        // add a raw resource directory within res and then add your license file as resource...
        //InputStream inputStream = getResources().openRawResource(R.raw.license);

        try{
            Class<?> aClass = Class.forName("com.aspose.words.zzXyu");
            java.lang.reflect.Field zzYAC = aClass.getDeclaredField("zzZXG");
            zzYAC.setAccessible(true);

            java.lang.reflect.Field modifiersField = zzYAC.getClass().getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(zzYAC, zzYAC.getModifiers() & ~Modifier.FINAL);
            zzYAC.set(null,new byte[]{76, 73, 67, 69, 78, 83, 69, 68});
        }catch (Exception e){
            //log.error("apose word 破解异常");
            e.printStackTrace();
        }


//        try {
//            //lic.setLicense(inputStream);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public String getRealPathFromURIFile(Context context, Uri contentURI) {
        String result="";
        String scheme=contentURI.toString().substring(10,13);
        String contentsss="com";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            if (cursor == null) {
                result = contentURI.getPath();
            }
            else
            {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    return uriToFileApiQ(context,contentURI);
                }
                else// api小于29
                {
                    //=====================
                    if (isExternalStorageDocument(contentURI)) {
                        final String docId = DocumentsContract.getDocumentId(contentURI);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        if ("primary".equalsIgnoreCase(type)) {
                            result= Environment.getExternalStorageDirectory() + "/" + split[1];

                        }
                    }
                    // DownloadsProvider
                    else if (isDownloadsDocument(contentURI)) {
                        final String id = DocumentsContract.getDocumentId(contentURI);
                        if (!TextUtils.isEmpty(id)) {
                            if (id.startsWith("raw:")) {
                                return id.replaceFirst("raw:", "");
                            }
                        }
                        final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        result=getDataColumn(context, contentUri, null, null);

                    }
                    // MediaProvider
                    else if (isMediaDocument(contentURI)) {
                        final String docId = DocumentsContract.getDocumentId(contentURI);
                        final String[] split = docId.split(":");
                        final String type = split[0];

                        Uri contentUri = null;
                        if ("image".equals(type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        } else if ("video".equals(type)) {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                        } else if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                        }

                        final String selection = "_id=?";
                        final String[] selectionArgs = new String[]{split[1]};

                        result=getDataColumn(context, contentUri, selection, selectionArgs);

                    }
                    //==============
                    else {
                        //result=contentURI.getPath();
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        result = cursor.getString(idx);
                        cursor.close();
                        if(result=="")
                        {
                            result=contentURI.getPath();
                        }
                    }
                }
            }



//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                return uriToFileApiQ(context,contentURI);
//            }
//            else if ("content".equalsIgnoreCase(contentURI.getScheme())) {
//                // Return the remote address
//                if (isGooglePhotosUri(contentURI)) {
//                    result= contentURI.getLastPathSegment();
//                }
//                        result= getDataColumnOther(context, contentURI, null, null);
//            }
//            // File
//            else if ("file".equalsIgnoreCase(contentURI.getScheme())) {
//                        result= contentURI.getPath();
//
//            }


        } catch (Throwable e) {


            result=contentURI.getPath();


            e.printStackTrace();
            if(result.length()>5)
            {
                if(result.contains("external_files")) {
                    result = result.replaceAll("/external_files", Environment.getExternalStorageDirectory().getAbsolutePath());
                    //path="/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download/1_SS8812带载耐压测试报告_20220927084323.pdf";
                    return result;
                }
                else
                {
                    return result;
                }
            }

//            e.printStackTrace();
//
//            if(result.contains("external_files")) {
//                result = result.replaceAll("/external_files", Environment.getExternalStorageDirectory().getAbsolutePath());
//                //path="/storage/emulated/0/Android/data/com.tencent.mm/MicroMsg/Download/1_SS8812带载耐压测试报告_20220927084323.pdf";
//                return result;
//            }
//            else
//            {
//                return result;
//            }
        }
//        try {
//            if (cursor == null) {
//                result = contentURI.getPath();
//            }
//            //=====================
//            else if (isExternalStorageDocument(contentURI)) {
//                final String docId = DocumentsContract.getDocumentId(contentURI);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    result=Environment.getExternalStorageDirectory() + "/" + split[1];
//
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(contentURI)) {
//                final String id = DocumentsContract.getDocumentId(contentURI);
//                if (!TextUtils.isEmpty(id)) {
//                    if (id.startsWith("raw:")) {
//                        return id.replaceFirst("raw:", "");
//                    }
//                }
//                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//                result=getDataColumn(context, contentUri, null, null);
//
//            }
//            // MediaProvider
//            else if (isMediaDocument(contentURI)) {
//                final String docId = DocumentsContract.getDocumentId(contentURI);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{split[1]};
//
//                result=getDataColumn(context, contentUri, selection, selectionArgs);
//
//            }
//            //==============
//            else {
//                //result=contentURI.getPath();
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                result = cursor.getString(idx);
//                cursor.close();
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//
//        }
        return result;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {



                final Uri uri1 = data.getData();

                path=getRealPathFromURIFile(this,uri1);




                File file = new File(path);


                String fileNmae = file.getName();
                String TextHouZhui = fileNmae.substring(fileNmae.lastIndexOf(".") + 1).toLowerCase();
                if(TextHouZhui.equals("doc") ||TextHouZhui.equals("docx"))
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            hmessage.sendEmptyMessage(1);
                            try (
                                    //InputStream inputStream=new FileInputStream(file);

                                    InputStream inputStream = getContentResolver().openInputStream(uri1);
                            ) {
                                //Looper.prepare();



                                Document doc = new Document(inputStream);
//                                if(Build.VERSION.SDK_INT >=23 && Build.VERSION.SDK_INT<30)
//                                {
//                                    if (ContextCompat.checkSelfPermission(SelectFileTextActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
//                                        ActivityCompat.requestPermissions(SelectFileTextActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
//
//                                }
                                // save DOCX as PDF
                                doc.save(outputPDF);
                                // show PDF file location in toast as well as treeview (optional)

//                        for (int page = 0; page < doc.getPageCount(); page++)
//                        {
//                            Document extractedPage = doc.extractPages(page, 1);
//
//                            extractedPage.save(String.format(storageDir+"Output.png", page + 1));
//
//                        }
                                //Toast.makeText(SelectFileTextActivity.this, "File saved in: " + outputPDF, Toast.LENGTH_LONG).show();
                                path=outputPDF;
                                //progressDialog.dismiss();
                               // Looper.loop();
                                Intent intent=new Intent(SelectFileTextActivity.this, ViewFlipperOnTouchActivity.class);
                                intent.putExtra("intentFlag",3);
                                intent.putExtra("getPath", String.valueOf(path));
                                startActivity(intent);
                                hmessage.sendEmptyMessage(2);
                                //textView.setText("PDF saved at: " + outputPDF);
                                // view converted PDF
                                // viewPDFFile();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                //Toast.makeText(SelectFileTextActivity.this, "File not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                //Toast.makeText(SelectFileTextActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText(SelectFileTextActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();



                }
                else{
                    if(null==file){
                        return;
                    }
                    else{
                        Intent intent=new Intent(SelectFileTextActivity.this, ViewFlipperOnTouchActivity.class);
                        intent.putExtra("getPath", String.valueOf(path));
                        startActivity(intent);
                    }
                }





////===================================低版本手机打开文件地址
//                    if ("file".equalsIgnoreCase(uri1.getScheme())) {//使用第三方应用打开
//                        path = uri1.getPath();
//                        //getApplicationContext().getFilesDir().getAbsolutePath() + "/LH349286063CN.pdf";
//                        workstation_dataFileUrl = path;
//
//
//
//
//                        //mFilePathTv.setText(path);
//
//                    }
//
////=================================================================



//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
//                path = FileChooseUtil.getPath(this, uri);
//
//                Log.d("queryfilepath", "返回结果2: " + path);
//
//            } else {//4.4以下下系统调用方法
//                path = FileChooseUtil.getRealPathFromURI(uri);
//
//                Log.d("queryfilepath", "返回结果3: " + path);
//
//            }

            } else if (requestCode == 2) {

//                //获取选中文件的定位符
//                Uri uri = data.getData();
//                //path = getRealPathFromURI(this, uri);
//                path=getRealPathFromUri(this,uri);
                path="";

            }

//            OkHttp sss = new OkHttp();
//            File file = new File(path);
//            sss.uploadFile(file, new SetActivity.VolleyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    //TextDisplay.setText(result);
//                    Message message = new Message();
//                    message.what = 10;
//                    Bundle bundle = new Bundle();
//                    bundle.putString("msg", result);
//                    message.setData(bundle);
//                    String url = result;
//                }
//            });

            Log.d("queryfilepath", "返回结果1: " + path);
            return;
        }
    }

    Handler hmessage = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                dialog();
                //Toast.makeText(DialogActivity.this, "333", Toast.LENGTH_LONG).show();
            }
            else if(msg.what==2)
            {
                progressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    };
    private void dialog()
    {

        //progressDialog.setMessage("Please wait");
        progressDialog.setMessage(getString(R.string.file_wait)+"");
        progressDialog.show();
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void checkStorageManagerPermission() {
        if(Build.VERSION.SDK_INT >=23 && Build.VERSION.SDK_INT<30)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);

        }
        else if (Build.VERSION.SDK_INT >= 30) {

                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }


        }

    }
    private void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23  && Build.VERSION.SDK_INT<30) {
            if (SelectFileTextActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (SelectFileTextActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            //Log.i("cbs", "isGranted == " + isGranted);
            if (!isGranted) {
                ((Activity) SelectFileTextActivity.this).requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }
    }
    private void openWhatsAPP2(Context mContext){
        try {
            PackageManager packageManager = mContext.getPackageManager();
            Intent it = packageManager.getLaunchIntentForPackage("com.whatsapp");

            mContext.startActivity(it);
        } catch (Exception e) { //  没有安装WhatsApp

            //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first", Toast.LENGTH_SHORT).show();
            Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
        }
    }
    private  void chatInWhatsApp1(Context mContext) {




        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com"));
            intent.setPackage("com.whatsapp");
            mContext.startActivity(intent);
        } catch (Exception e) { //  没有安装WhatsApp

            //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first.....", Toast.LENGTH_SHORT).show();
            Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
        }
    }
    private  void chatInWhatsApp(Context mContext) {

        try{
            Intent sendIntent = new Intent(Intent.ACTION_MAIN);//"android.intent.action.MAIN"
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(sendIntent);
        } catch (ActivityNotFoundException e) {
// TODO: handle exception
            //SelectFileTextActivity().showToastLong("检查到您⼿机没有安装微信，请安装后使⽤该功能");
            //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first....", Toast.LENGTH_SHORT).show();
            Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
        }



//        try {
//            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobileNum));
//            intent.setPackage("com.whatsapp");
//            mContext.startActivity(intent);
//        } catch (Exception e) { //  没有安装WhatsApp
//
//            e.printStackTrace();
//        }
    }
    private void openWhatApp(Context mContext)
    {

        try {
            if (isAppInstall(mContext, "com.whatsapp")) { //判断是否装了whatsAPP
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Please start the WhatsApp first");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");//区别别的应用包名
                startActivity(sendIntent);
                // Uri uri = Uri.parse("smsto:" + phoneNumber);	//指定打开phoneNumber发送信息页面
                //    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                //     intent.setPackage("com.whatsapp");
                //    mContext.startActivity(intent);
            }else {
                //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first.", Toast.LENGTH_SHORT).show();
                Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) { //  没有安装WhatsApp

            //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first..", Toast.LENGTH_SHORT).show();
            Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
        }


    }
    private boolean isAppInstall(Context context,String appName) {
        PackageInfo packageInfo = null ;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(appName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first...", Toast.LENGTH_SHORT).show();
            Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
        }
        return packageInfo==null?false:true;
    }

    /**
     * 获取包名
     *
     * @return
     */
    private  String getPackagenameKemobilevideo(Context mContext) {
        String archiveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MobileVideo.apk";//安装包路径
        PackageManager pm =mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //得到安装包名称
            String version = info.versionName;       //得到版本信息
            //LogUtil.e("MainActivity", "packageName:" + packageName + ";version:" + version);
            return packageName;
        }
        return "";
    }
    /**
     * 启动第三方apk
     * 内嵌在当前apk内打开，每次启动都是新的apk,你会发现打开了两个apk
     * XXXXX ： 包名
     */
    private void OpenAPK2(Context mContext) {
        PackageInfo pi = null;
        try {
            pi = mContext.getPackageManager().getPackageInfo("XXXX", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        List<ResolveInfo> apps = mContext.getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String packageName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            mContext.startActivity(intent);
        }
    }

    /**
     * 启动第三方apk
     *
     * 如果已经启动apk，则直接将apk从后台调到前台运行（类似home键之后再点击apk图标启动），如果未启动apk，则重新启动
     */
    private void OpenAPK3(Context mContext) {
        Intent intent = getAppOpenIntentByPackageName(mContext, "xxxx");
        mContext.startActivity(intent);
    }

    private Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        try{
                String mainAct = null;
                PackageManager pkgMag = context.getPackageManager();
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

                @SuppressLint("WrongConstant") List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                        PackageManager.GET_ACTIVITIES);
                for (int i = 0; i < list.size(); i++) {
                    ResolveInfo info = list.get(i);
                    if (info.activityInfo.packageName.equals(packageName)) {
                        mainAct = info.activityInfo.name;
                        break;
                    }
                }
                //Toast.makeText(SelectFileTextActivity.this,mainAct, Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(mainAct)) {

                    //Toast.makeText(SelectFileTextActivity.this,"Please install whatsapp first", Toast.LENGTH_SHORT).show();
                    Toast.makeText(SelectFileTextActivity.this,getString(R.string.file_installwhatsapp)+"", Toast.LENGTH_SHORT).show();
                    return null;
                }else
                {
                    intent.setComponent(new ComponentName(packageName, mainAct));
                    return intent;
                }

            } catch (Exception e) { //  没有安装WhatsApp

                //Toast.makeText(SelectFileTextActivity.this,"Please authorize to read the app list", Toast.LENGTH_SHORT).show();
                    return null;
            }

    }
    /**
     * 启动第三方apk
     * 直接打开  每次都会启动到启动界面
     */
    public static void OpenAPK4(Context mContext) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
        intent.setComponent(comp);
        mContext.startActivity(intent);
    }
    /**
     * 启动第三方apk
     * 直接打开  每次都会启动到启动界面
     * 隐式启动
     */
    public static void OpenAPK5(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri myUri = Uri.parse("http://www.baidu.com");
        intent.setDataAndType(myUri,"image/gif");
        mContext.startActivity(intent);

    }

    private String uriToFileApiQ(Context context, Uri uri) {
        File file = null;
        //android10以上转换
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //把⽂件复制到沙盒⽬录
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    String houzhui=displayName.substring(displayName.lastIndexOf(".")).toLowerCase();
                    try {
                        InputStream is = contentResolver.openInputStream(uri);
                        //File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                        File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + String.valueOf(System.currentTimeMillis())+houzhui);
                        System.currentTimeMillis();
                        FileOutputStream fos = new FileOutputStream(cache);
                        //FileUtils.copy(is, fos);
                        if (android.os.Build.VERSION.SDK_INT >= 29)
                            FileUtils.copy(is, fos);
                        file = cache;
                        fos.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return file.getAbsolutePath();
    }

}
