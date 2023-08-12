package com.grozziie.grozziie_pdf;;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AngenlActivity extends AppCompatActivity {
    private MeFragment meFragment;
    private MarketFragment marketFragment;
    private HomeFragment homeFragment;
    private ImageView MarketFragmentDisplay;
    private ImageView MeFragmentDisplay;
    private LinearLayout BarDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angenl);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.bacgroundColor));



        MarketFragmentDisplay=findViewById(R.id.goodsMarketMain);
        MeFragmentDisplay=findViewById(R.id.goodsMeMain);
        BarDisplay=findViewById(R.id.L2);//goodsBarcodeMain
        MarketFragmentDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marketFragment==null)
                    marketFragment=new MarketFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.goodsDisplay,marketFragment).commitAllowingStateLoss();
            }
        });
        MeFragmentDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(meFragment==null)
//                    meFragment=new MeFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.goodsDisplay,meFragment).commitAllowingStateLoss();
                Toast.makeText(AngenlActivity.this, "Under development", Toast.LENGTH_SHORT).show();

            }
        });
        BarDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent=new Intent(AngenlActivity.this,BarcodeActivity.class);
                startActivity(inent);
                //finish();
            }
        });
       // marketFragment=new MarketFragment();
       // getSupportFragmentManager().beginTransaction().add(R.id.goodsDisplay,marketFragment).commitAllowingStateLoss();

//        homeFragment=new HomeFragment();
//        getSupportFragmentManager().beginTransaction().add(R.id.goodsDisplay,homeFragment).commitAllowingStateLoss();

        marketFragment=new MarketFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.goodsDisplay,marketFragment).commitAllowingStateLoss();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v!=null&&((v instanceof EditText)||(v instanceof AppCompatEditText))){
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(AngenlActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }

        return super.onTouchEvent(event);
    }

}
