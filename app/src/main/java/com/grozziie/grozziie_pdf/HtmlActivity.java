package com.grozziie.grozziie_pdf;;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HtmlActivity extends AppCompatActivity {
    private WebView DisplayHtmlView;
    protected final int PERMISSION_REQUEST = 42;
    private Button HtmlOpenButton;
    private Button HtmltoPDF;
    private  int htmlFlag = 0;
    private EditText InputHtmlText;
    private Handler handler;
    private UpdateTimer updateTimer;
    private Bitmap[] bitmaps;
    private ProgressBar htmlProgressBar;
    private View backHtmlView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        DisplayHtmlView=findViewById(R.id.DisplayHtmlView);
        HtmlOpenButton=findViewById(R.id.HtmlOpenButton);
        InputHtmlText=findViewById(R.id.InputHtmlText);
        HtmltoPDF=findViewById(R.id.HtmltoPDF);
        htmlProgressBar=findViewById(R.id.htmlProgressBar);
        backHtmlView=findViewById(R.id.backHtmlView);
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
        htlmLoad();
        //===================
//        setUseWideViewPort(true);  //将图片调整到适合webview的大小
//        setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//
//        setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
//        setBuiltInZoomControls(true); //设置内置的缩放控件。
////若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//
//        setDisplayZoomControls(false); //隐藏原生的缩放控件
//
//        setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
//        supportMultipleWindows();  //多窗口
//        setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
//        setAllowFileAccess(true);  //设置可以访问文件
//        setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
//        setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        setLoadsImagesAutomatically(true);  //支持自动加载图片
//        setDefaultTextEncodingName("utf-8");//设置编码格式
        //=====================
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        HtmlOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htlmLoad();
            }
        });
        backHtmlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HtmltoPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap[] bitmapsNow ;
                bitmaps=HtmlToBMP();
               // bitmaps=bitmapsNow;
                handler =new Handler(Looper.getMainLooper());
                Intent intent = new Intent(HtmlActivity.this, ViewFlipperOnTouchActivity.class);
                //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET); /* launch as a new document */
//                Bundle bundle=new Bundle();
//
//                bundle.putParcelable("bitmapX", bitmap);
//                intent.putExtra("bundle", bundle);
                intent.putExtra("intentFlag",2);
                startActivity(intent);

                updateTimer=new UpdateTimer();
                handler.postDelayed(updateTimer,500);


            }
        });
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


    }
    private void downloadySystem(String url, String contentDisposition, String mimeType) {
        //指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //分许媒体扫描，根据下到的文件类剂被加入相册、音乐等姐体库
        request.allowScanningByMediaScanner();
        //设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置通知栏的标题，如里不语警，默认伸用文件么
        request.setDescription("下载成功");
        //介许在计费流量下下载
        request.setAllowedOverMetered(false);
        //允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型WIFI 和 手机网络流量
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);// 设置下载文件保存的路径和文件名
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        String name = null;
        try {
            name = URLDecoder.decode(fileName, "utf-8");
        }//这里需要转码获取到中文格式的文件名
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置存储路径
        request.setDestinationInExternalFilesDir(this,"file_download/",name);
        final DownloadManager downloadManager= (DownloadManager)this.getSystemService(DOWNLOAD_SERVICE);
        // 下载任务唯一ID
        long downloadId = downloadManager.enqueue(request);
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
            int height = DisplayHtmlView.getHeight();
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
    private Bitmap newBitmap(List<Bitmap> cacheBitmaps) {
        //重测高度  防止高度测算不对
        int height = 0;
        for (Bitmap bitmap : cacheBitmaps) {
            height += bitmap.getHeight();
        }
        Bitmap bitmap1 = cacheBitmaps.get(0);
        int width = bitmap1.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);

        int topHeight = bitmap1.getHeight();
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于两张图片高度总和
        canvas.drawBitmap(bitmap1, 0, 0, null);
        for (int i = 1; i < cacheBitmaps.size(); i++) {
            Bitmap bitmap2 = cacheBitmaps.get(i);
            canvas.drawBitmap(bitmap2, 0, topHeight, null);
            topHeight += bitmap2.getHeight();
        }
        return bitmap;
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        getState();
//    }
@Override
protected void onDestroy() {

    super.onDestroy();
    EventBus.getDefault().unregister(this);
}
    private void htlmLoad() {
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

                    htmlProgressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    try
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager) HtmlActivity.this
                                .getSystemService(HtmlActivity.this.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(
                                HtmlActivity.this.getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e)
                    {
                    }

                }
                else
                {
                    htmlProgressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    htmlProgressBar.setProgress(progress);//设置进度值
                }
            }
        });
        // HtmlWebView.loadUrl"");
        //HtmlWebView.loadUrl("http://www.annalytics.co.uk/android/pdf/2017/04/06/Save-PDF-From-An-Android-WebView/");
        String htmlUrl=InputHtmlText.getText().toString();
        //String xxx=htmlUrl.toLowerCase().substring(0,3);
        if(!htmlUrl.toLowerCase().substring(0,4).equals("http"))
        {
            htmlUrl="https://"+htmlUrl;
            InputHtmlText.setText(htmlUrl);
        }
        DisplayHtmlView.loadUrl(InputHtmlText.getText().toString());//"Http://wap.baidu.com"
    }
}
