package com.grozziie.grozziie_pdf;;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aspose.words.Document;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {
    private static final int PERMISSION_GRANTED = 0;
    private String path="";
    private static final String TAG1 = "dos_S";
    protected final int PERMISSION_REQUEST = 42;
    private final String storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator;
    private final String outputPDF = storageDir + "Angenl_PDF.pdf";
    private ProgressDialog progressDialog=null;
    private  int doc_S=0;
    private WebView DisplayHtmlView;
    private  int htmlFlag = 0;
    private int htmlFlag_X=0;
    private Bitmap[] bitmaps;
    private Handler handler;
    private UpdateTimer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStorageManagerPermission();
        checkPermission();
        setContentView(R.layout.activity_start);
        DisplayHtmlView=findViewById(R.id.DisplayHtmlViewS);

//        private void handleImage(){
//            Intent intent=getIntent();
//            String action=intent.getAction();
//            String type=intent.getType();
//            if(action.equals(Intent.ACTION_SEND)&&type.equals("image/*")){
//                Uri uri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
//                //接收多张图片
//　　　　　　　//ArrayList<Uri> uris=intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//                if(uri!=null ){
//                    try {
//                        FileInputStream fileInputStream=new FileInputStream(uri.getPath());
//                        Bitmap bitmap= BitmapFactory.decodeStream(fileInputStream);
//                        imageView.setImageBitmap(bitmap);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }



        Intent intent = getIntent();
         Uri  uri = null;
        String action=intent.getAction();
        String type =intent.getType();



        if(action.equals(Intent.ACTION_SEND)){
            if(type.equals("text/plain"))
            {
                htmlFlag_X=0;
                String httpUrl1=intent.getStringExtra(Intent.EXTRA_TEXT);
                //String httpUrl1=httpUrl.substring(0,httpUrl.indexOf("http"));
                String httpUrl=httpUrl1.substring(httpUrl1.indexOf("http"));

                DisplayHtmlView.enableSlowWholeDocumentDraw();
                WebSettings settings= DisplayHtmlView.getSettings();
                //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                settings.setUseWideViewPort(true);
                //setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                //settings.setAllowFileAccess(true);

                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setLoadWithOverviewMode(true);
                settings.setJavaScriptEnabled(true);
                settings.setLoadsImagesAutomatically(true);  //支持自动加载图片
                settings.setDefaultTextEncodingName("utf-8");//设置编码格式

                htlmLoad(httpUrl);

                //DisplayHtmlView.loadUrl(httpUrl.toString());//"Http://wap.baidu.com"
                DisplayHtmlView.setDownloadListener(new DownloadListener() {
                    @Override
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimeType, long contentLength) {
                        //downloadySystem(url, contentDisposition, mimeType);
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    }
                });
                return;
            }
            else
            {
                uri=intent.getParcelableExtra(Intent.EXTRA_STREAM);
            }

        }
        else
        {
            uri = intent.getData();
        }




        if(uri!=null)



            path=getRealPathFromURIFile(this,uri);//即为这个文件的路径名了

        if(path!="")
        {

            //====================================
            final Uri uri1 = uri;

            //path=getRealPathFromURIFile(this,uri1);




            File file = new File(path);


            String fileNmae = file.getName();
            String TextHouZhui = fileNmae.substring(fileNmae.lastIndexOf(".") + 1).toLowerCase();
            if(TextHouZhui.equals("doc") ||TextHouZhui.equals("docx")) {
                progressDialog = ProgressDialog.show(this, null, "Please wait...", true, false);
                doc_S=1;
                //hmessage.sendEmptyMessage(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

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
                            path = outputPDF;
                            //progressDialog.dismiss();
                            // Looper.loop();
                            //Intent intent = new Intent(StartActivity.this, ViewFlipperOnTouchActivity.class);
                            Intent intent=new Intent(StartActivity.this, ViewFlipperOnTouchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            intent.putExtra("intentFlag", 3);
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
            else
            {
                doc_S=0;
//                Intent intent1=new Intent(StartActivity.this, ViewFlipperOnTouchActivity.class);
//                intent1.putExtra("getPath", String.valueOf(path));
//                startActivity(intent1);


                Intent intent1=new Intent(StartActivity.this, ViewFlipperOnTouchActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("getPath", String.valueOf(path));
                startActivity(intent1);


            }
            //====================================








        }
        else
        {Integer time = 3000;    //设置等待时间，单位为毫秒
            Handler handler = new Handler();
            //当计时结束时，跳转至主界面
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //startActivity(new Intent(StartActivity.this, AngenlActivity.class));

                    startActivity(new Intent(StartActivity.this, SelectFileTextActivity.class));
                    StartActivity.this.finish();
                }
            }, time);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(doc_S==1)
//        {
//            Log.e(TAG1, "Start doc_S");
//            //hmessage.sendEmptyMessage(1);
//            Looper.prepare();
//            dialog();
//            Looper.loop();
//
//        }

    }
    //=======================语言设置==================================
    @Override
    protected void attachBaseContext(Context newBase) {
        //super.attachBaseContext(newBase);
        super.attachBaseContext(attachBaseContext_S(newBase));
    }
//==========================================================================
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog!=null)
        {
            progressDialog.dismiss();
        }
        EventBus.getDefault().unregister(this);
    }

    Handler hmessage = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //dialog();
                //Toast.makeText(DialogActivity.this, "333", Toast.LENGTH_LONG).show();
            }
            else if(msg.what==2)
            {
                if (progressDialog!=null){
                    progressDialog.dismiss();

                }
                //progressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    };
    //=======================语言设置==================================
    private  Context attachBaseContext_S(Context context) {
        //不同版本设置方式不一样
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createResources(context);
        } else {
            updateResources(context);
            return context;
        }
    }
    @TargetApi(Build.VERSION_CODES.N)
    private  Context createResources(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        //获取语言设置，一般用户设置的语言优先级更高，如果用户没有设置，则获取系统语言
        Locale targetLocale = getSystemLocale(context);
        configuration.setLocale(targetLocale);
        resources.updateConfiguration(configuration, dm);
        //创建配置
        return context.createConfigurationContext(configuration);
    }

    private   void updateResources(Context pContext) {
        Locale targetLocale = getSystemLocale(pContext);
        Configuration configuration = pContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = pContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //更新配置
        resources.updateConfiguration(configuration, dm);
    }
    private   Locale getSystemLocale(Context context) {
        Locale locale;
        //7.0有多语言设置获取顶部的语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }
//=============================================================================

    private void dialog()
    {

        progressDialog.setMessage("Please wait");
        progressDialog.show();
    }
    private String getRealPathFromURIFile(Context context, Uri contentURI) {
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
        }


        return result;
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
    private static String getDataColumnOther(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
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
        return "";
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());

    }
    /**
     * Android 10 以上适配
     * @param context
     * @param uri
     * @return
     */


    private static String uriToFileApiQ(Context context, Uri uri) {
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
                    try {
                        InputStream is = contentResolver.openInputStream(uri);
                        File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
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

    class UpdateTimer implements Runnable {
        public void run()
        {
            EventBus.getDefault().post(new BitmapEventBus(bitmaps));

            handler=null;
            finish();
//            if(handler!=null)
//                handler.postDelayed(this,UPDATE_EVERY);
        }
    }
    private void htlmLoad(String httpUrl) {
        DisplayHtmlView.setWebViewClient(new WebViewClient() {
            int running = 0;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                running++;
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                htmlFlag = 0;
                if (--running == 0) {
                    htmlFlag = 9;





//                    //HtmlInput.setText("Http://wap.baidu.com");
//                    handlerHtml = new Handler();
//                    updateTimer = new UpdateTimer();
//                    handlerHtml.postDelayed(updateTimer, 500);

                }

            }

            @Override
            public void onPageStarted(WebView view, String s, Bitmap bitmap) {
                super.onPageStarted(view, s, bitmap);
                running = Math.max(running, 1);
            }

        });
        DisplayHtmlView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {

                    //htmlProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    if(htmlFlag_X==0)
                    {
                        final Bitmap[] bitmapsNow ;
                        bitmaps=HtmlToBMP();
                        // bitmaps=bitmapsNow;
                        htmlFlag_X=1;
                        handler =new Handler(Looper.getMainLooper());
                        Intent intent = new Intent(StartActivity.this, ViewFlipperOnTouchActivity.class);
                        intent.putExtra("intentFlag",2);
                        startActivity(intent);

                        updateTimer=new UpdateTimer();
                        handler.postDelayed(updateTimer,1000);
                    }




                    try
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager) StartActivity.this
                                .getSystemService(StartActivity.this.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(
                                StartActivity.this.getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e)
                    {
                    }

                }
                else
                {
                    //htmlProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    //htmlProgressBar.setProgress(progress);//设置进度值
                }
            }
        });
        // HtmlWebView.loadUrl"");
        //HtmlWebView.loadUrl("http://www.annalytics.co.uk/android/pdf/2017/04/06/Save-PDF-From-An-Android-WebView/");
//        String htmlUrl=httpUrl;
//        //String xxx=htmlUrl.toLowerCase().substring(0,3);
//        if(!htmlUrl.toLowerCase().substring(0,4).equals("http"))
//        {
//            htmlUrl="https://"+htmlUrl;
//            InputHtmlText.setText(htmlUrl);
//        }
        DisplayHtmlView.loadUrl(httpUrl.toString());//"Http://wap.baidu.com"
    }
    private Bitmap[] HtmlToBMP()
    {
        try {

//        Picture picture = HtmlWebView.capturePicture();
//        Bitmap b = Bitmap.createBitmap(
//                picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        picture.draw(c);
//        Bitmap x=b;
//                } catch (Exception e) {
//            e.printStackTrace();
//            //Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
//        }

            // WebView webView = HtmlWebView.getCurWebView();//此步骤为获取WebView，即需要截图的WebView，此步骤可忽略，将WebView设为自己的即可。。
            //获取网页在手机上真实的高度
            int contentHeight = (int) (DisplayHtmlView.getContentHeight() * DisplayHtmlView.getScale());
            //获取Webview控件的高度
            int height =Resources.getSystem().getDisplayMetrics().heightPixels; //DisplayHtmlView.getHeight();
            //计算需要滚动次数
            int totalScrollCount = (int) (contentHeight / height);
            //剩余高度
            int surplusScrollHeight = contentHeight - (totalScrollCount * height);
            //1:打开缓存开关

            DisplayHtmlView.setDrawingCacheEnabled(true);

            DisplayHtmlView.buildDrawingCache(true);


            //存储图片容器
            List<Bitmap> cacheBitmaps = new ArrayList<>();
            for (int i = 0; i < totalScrollCount; i++) {
                DisplayHtmlView.setScrollY(i * height);
                cacheBitmaps.add(getScreenshot(DisplayHtmlView));
            }
            //如果不能整除,需要额外滚动1次
            if (surplusScrollHeight > 0) {
                DisplayHtmlView.setScrollY(totalScrollCount * height);
                Bitmap bitmap = getScreenshot(DisplayHtmlView);
                //截取最后剩下的图片 不需要全部
                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, (int) (bitmap.getHeight() - surplusScrollHeight), bitmap.getWidth(), (int) surplusScrollHeight);


                Bitmap bitmapX = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
                Canvas canvas = new Canvas(bitmapX);
                canvas.drawColor(Color.WHITE);

                //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
                canvas.drawBitmap(bitmap2, 0, 0, null);




                cacheBitmaps.add(bitmapX);
            }
//            //图片拼接-这就是最终的截屏图片
//            Bitmap bigBitmap = newBitmap(cacheBitmaps);
//
//            return  bigBitmap;

            Bitmap[] bitmaps=new Bitmap[cacheBitmaps.size()];
            for (int i = 0; i < cacheBitmaps.size(); i++) {
                bitmaps[i] = cacheBitmaps.get(i);
//                canvas.drawBitmap(bitmap2, 0, topHeight, null);
//                topHeight += bitmap2.getHeight();
            }
            return  bitmaps;
        }
        catch (Exception e) {

            e.printStackTrace();
            return  null;
            //Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }









//        Bitmap bitmap = Bitmap.createBitmap(webviewBMP.getWidth(), webviewBMP.getHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(bitmap);
//
//        webviewBMP.draw(canvas);

        //return bitmap;


        //      File file = new File("/sdcard/" + "page.jpg");
//        if(file.exists()){
//            file.delete();
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file.getAbsoluteFile());
//            if (fos != null) {
//                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//                fos.close();
//                Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
//        }
    }
    private Bitmap getScreenshot(WebView webView) {
        webView.buildDrawingCache();
        webView.setDrawingCacheEnabled(true);
        //强制绘制缓存（必须在setDrawingCacheEnabled(true)之后才能调用，否者需要手动调用destroyDrawingCache()清楚缓存）

        //获取缓存
        Bitmap drawingCache = webView.getDrawingCache();


//        Bitmap  bitmapX = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
//        //创建Canvas，并传入Bitmap.
//        Canvas canvas = new Canvas(bitmapX);
//
//        //View把内容绘制到canvas上，同时保存在bitmap.
//        webView.draw(canvas);
//        //return bitmap


        //拷贝图片(这里就是我们需要的截图内容啦)
        Bitmap newBitmap = Bitmap.createBitmap(drawingCache);
        return newBitmap;








    }
    private void checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23  && Build.VERSION.SDK_INT<30) {
            if (StartActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (StartActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            //Log.i("cbs", "isGranted == " + isGranted);
            if (!isGranted) {
                ((Activity) StartActivity.this).requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }
    }
    private void checkStorageManagerPermission() {
        /**
         * 用户权限安卓10以上需要
         **/
        //PackageManager.PERMISSION_GRANTED=PERMISSION_GRANTED
        // 获得授权
       // public static final int PERMISSION_GRANTED = 0;
        // 未获授权
        //public static final int PERMISSION_DENIED = -1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] strings = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(this, strings, 18);
            }
        }
//=============获取位置权限=========================
//由于页面需要自动跳转，所以不能在这个位置获取======
//        if(!isLocServiceEnable(this))
//        {
//            openGPSSettings();
//            isflase();
//        }
//===================================================
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


    //===============================================================


    /**
     * 检测GPS、位置权限是否开启
     */
//    private Boolean checkGPSContacts(){
//        LocationManager lm = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
//        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (ok) {//开了定位服务
//            if (Build.VERSION.SDK_INT >= 23) { //判断是否为android6.0系统版本，如果是，需要动态添加权限
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PERMISSION_GRANTED) {// 没有权限，申请权限。
//                    ActivityCompat.requestPermissions(this, LOCATIONGPS,
//                            25);
//                } else {
//                    getLocation();//getLocation为定位方法
//                }
//            } else {
//                getLocation();//getLocation为定位方法
//            }
//            return true;
//        }
//        return false;
//    }


//=================动态获取位置权限方法一：=============================================
private void getLocation(Context context)
{
    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    if (!isGpsAble(locationManager)) {
        Toast.makeText(StartActivity.this, "请打开GPS", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(StartActivity.this, "已开启定位权限", Toast.LENGTH_LONG).show();
        }

//    if (ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
//        //开启定位权限,200是标识码
//        ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//    } else {
//        //startLocation();
//        Toast.makeText(StartActivity.this, "已开启定位权限", Toast.LENGTH_LONG).show();
//    }
}
    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }
    private void openGPS2() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 29);
    }
//=========================================================================================================

//====================动态获取位置权限方法二：===========================
    private   boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    //=================================================================
    private void isflase(){
        openGPSSettings();
        //用户手动开启
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 21);



    }


    private void openGPSSettings() {
//获取GPS现在的状态(打开或是关闭状态)

        boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER);

        if (gpsEnabled)

        {
//关闭GPS

            //Settings.Secure.setLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER, false );

        } else

        {
//打开GPS

            Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, true);

        }
    }

//===========================================================================================



}
