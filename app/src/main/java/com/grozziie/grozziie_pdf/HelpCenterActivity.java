package com.grozziie.grozziie_pdf;;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class HelpCenterActivity extends AppCompatActivity {
    private View backHelpCenterView;
    private Button PDFWebSite;
    private RelativeLayout PDFHelpCenterMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        PDFHelpCenterMain=findViewById(R.id.PDFHelpCenterMain);
        backHelpCenterView=findViewById(R.id.backHelpCenterView);
        PDFWebSite=findViewById(R.id.PDFWebSite);
        getSystemLocale(this);
        backHelpCenterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        PDFWebSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //方式一：代码实现跳转
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.zjweiting.com/downloaden.htm");//此处填链接
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }
    private void getSystemLocale(Context context) {
        Locale locale;
        //7.0有多语言设置获取顶部的语言
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        String languageS=locale.getLanguage().toLowerCase().toString();
        String countryS=locale.getCountry().toLowerCase().toString();
        //languageS=locale.getLanguage().toString();
        if(languageS.equals("th_th")||languageS.equals("th"))
        {
            PDFHelpCenterMain.setBackgroundResource(R.drawable.pdfhelpcenterthai);
            PDFWebSite.setBackgroundResource(R.drawable.pdfwebsitethai);
        }
        else
        {
            PDFHelpCenterMain.setBackgroundResource(R.drawable.pdfhelpcenter);
            PDFWebSite.setBackgroundResource(R.drawable.pdfwebsite);
        }
        //return locale;
    }
}
