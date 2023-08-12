package com.grozziie.grozziie_pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.grozziie.grozziie_pdf.BlueTooth.BlueToothMainActivity;

public class PDFSetActivity extends AppCompatActivity {
    private Button meDevice;
    private Button meSet;
    private ImageView backPDFSet;
    private View backPDFSetView;
    private Button meHelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfset);
        meDevice=findViewById(R.id.meDevice);
        meSet=findViewById(R.id.meSet);
        backPDFSet=findViewById(R.id.backPDFSet);
        backPDFSetView=findViewById(R.id.backPDFSetView);
        meHelp=findViewById(R.id.meHelp);
        meDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PDFSetActivity.this, BlueToothMainActivity.class);
                intent.putExtra("setWhat",3);
                startActivity(intent);
            }
        });
        meSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PDFSetActivity.this, PDFSetFunctionActivity.class);
//                intent.putExtra("setWhat",3);
                startActivity(intent);
            }
        });
        meHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PDFSetActivity.this, HelpCenterActivity.class);
//                intent.putExtra("setWhat",3);
                startActivity(intent);
            }
        });
        backPDFSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backPDFSetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
