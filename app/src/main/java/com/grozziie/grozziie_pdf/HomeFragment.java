package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.youth.banner.Banner;
//import com.youth.banner.BannerConfig;
//import com.youth.banner.loader.ImageLoader;


import java.io.File;
import java.util.ArrayList;
import java.util.List;



import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.grozziie.grozziie_pdf.BlueTooth.BlueToothMainActivity;

public class HomeFragment extends Fragment {
    private Button deviceButton;
    private Button barButton;
    private Button helpButton;
    private Button setButton;
    private ImageView personImage2;
    private ImageView personImage1;

    private ImageView shopImage;
    private ImageView homeDevice;
    private LinearLayout shopLayout;
    private LinearLayout barcodeLayout;
    private ImageView homeDeviceState;
   // List<Integer> imageUrlData;
   // List<String> contentData;
   // Banner myBanner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        personImage1=view.findViewById(R.id.personImage1);
        personImage2=view.findViewById(R.id.personImage2);
        shopLayout=view.findViewById(R.id.homeShoplayout);
        barcodeLayout=view.findViewById(R.id.homeMakeBarcodelayout);
        homeDevice=view.findViewById(R.id.homeDevice);
        homeDeviceState=view.findViewById(R.id.homeDeviceState);
        //return super.onCreateView(inflater, container, savedInstanceState);
        //myBanner = view.findViewById(R.id.banner);
        //initBanner();

        if(BlueToothMainActivity.socketall==null)
        {
            homeDeviceState.setVisibility(View.VISIBLE);
        }
        else
        {
            homeDeviceState.setVisibility(View.GONE);
        }
        personImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isExist("com.taobao.taobao")) {
                    try {
                        Toast.makeText(getContext(),"即将跳转淘宝界面",Toast.LENGTH_SHORT).show();
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "https://detail.m.tmall.com/item.htm?spm=a212k0.12153887.0.0.4d7c687dvGR1FM&id=566375303724&skuId=3760846131485";
                    toTaoBao(getContext(),url);
                }else{
                    //Toast.makeText(getContext(),"请先安装淘宝",Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://detail.m.tmall.com/item.htm?spm=a212k0.12153887.0.0.4d7c687dvGR1FM&id=566375303724&skuId=3760846131485");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }







            }
        });
        personImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExist("com.taobao.taobao")) {
                    try {
                        Toast.makeText(getContext(),"即将跳转淘宝界面",Toast.LENGTH_SHORT).show();
                        Thread.sleep(500);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = "https://detail.m.tmall.com/item.htm?spm=a212k0.12153887.0.0.4d7c687dvGR1FM&id=539736901373";
                    toTaoBao(getContext(),url);
                }else{
                    //Toast.makeText(getContext(),"请先安装淘宝",Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://detail.m.tmall.com/item.htm?spm=a212k0.12153887.0.0.4d7c687dvGR1FM&id=539736901373");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });
        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager MF = getActivity().getSupportFragmentManager();
                MF.beginTransaction().replace(R.id.goodsDisplay,new MarketFragment()).commitAllowingStateLoss();

            }
        });
        barcodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(getActivity(),BarcodeActivity.class);
                startActivity(intent);
            }
        });
        homeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(getActivity(), BlueToothMainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    public static boolean isExist(String packageName){
        return new File("/data/data/" + packageName).exists();
    }
    public static void toTaoBao(Context context,String url){
        //String url = "https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.1.16cb5f90P7idqA&id=548529765739&user_id=3023089296&cat_id=50104459&is_b=1&rn=e79e493995c01cbedb5f787d35e235f3";
        Intent intent = new Intent();
        intent.setAction("Android.intent.action.VIEW");
        Uri uri = Uri.parse(url); // 商品地址
        intent.setData(uri);
        intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非activity类中调用startactivity方法必须添加标签
        context.startActivity(intent);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
//
//    private void initBanner() {
//        imageUrlData = new ArrayList<>();
//        contentData = new ArrayList<>();
//        imageUrlData.add(R.drawable.home1);
//        imageUrlData.add(R.drawable.home2);
//        imageUrlData.add(R.drawable.home3);
//        imageUrlData.add(R.drawable.home4);
//        contentData.add("我是雷神");
//        contentData.add("我是小美");
//        contentData.add("我是洛基");
//        contentData.add("我是博士");
//        myBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        myBanner.setImageLoader(new MyLoader());
//        myBanner.setImages(imageUrlData);
//        myBanner.setBannerTitles(contentData);
//        //myBanner.setBannerAnimation(Transformer.Default);
//        //切换频率
//        myBanner.setDelayTime(2000);
//        //自动启动
//        myBanner.isAutoPlay(true);
//        //位置设置
//        myBanner.setIndicatorGravity(BannerConfig.CENTER);
//        //开始运行
//        myBanner.start();
//    }
//
//    private class MyLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            Glide.with(getActivity()).load(path).into(imageView);
//        }
//    }
}
//======================




//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        initBanner();
//        return view;
//    }





