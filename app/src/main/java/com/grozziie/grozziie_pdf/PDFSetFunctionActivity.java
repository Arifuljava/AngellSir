package com.grozziie.grozziie_pdf;;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class PDFSetFunctionActivity extends AppCompatActivity {
    private Spinner SpinnerSpeed;
    private Spinner SpinnerDensity;
    private int PrintSpeed_int=2;
    private int PrintDensity_int=9;
    private int PrintContrast_int=60;
    private int PrintHalftoneNumber_int=50;
    private int PrintHalftone_int=0;
    private String PrintHalftone_string="";
    private boolean PrintHalftone_bollean=true;
    private boolean PrinttCheckBarcodeProcess_bollean=false;
    private boolean PrintCutEmptyProcess_bollean=false;
    private ImageView backPDFFunction;
    private View backPDFFunctionView;
    private TextView ContrastNumberInput;
    private TextView DitherNumberInput;
    private String[] pickerContrastString=new String[120];
    private String[] pickerDitherString=new String[100];
    private PopupWindow popupWindow;
    private int timeFlag;
    private int ditherFlag;
    private CheckBox CheckHalftone;
    private CheckBox CheckBarcodeProcess;
    private CheckBox CheckCutEmptyProcess;
    //private Switch HalftoneFunction;
    private RadioGroup RadioGroupHalftone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfset_function);
        SpinnerSpeed=findViewById(R.id.SpinnerSpeed);
        SpinnerDensity=findViewById(R.id.SpinnerDensity);
        backPDFFunction=findViewById(R.id.backPDFFunction);
        backPDFFunctionView=findViewById(R.id.backPDFFunctionView);
        ContrastNumberInput=findViewById(R.id.ContrastNumberInput);
        //HalftoneFunction=findViewById(R.id.HalftoneFunction);
        RadioGroupHalftone=findViewById(R.id.RadioGroupHalftone);
        DitherNumberInput=findViewById(R.id.DitherNumberInput);
        CheckHalftone=findViewById(R.id.CheckHalftone);
        CheckBarcodeProcess=findViewById(R.id.CheckBarcodeProcess);
        CheckCutEmptyProcess=findViewById(R.id.CheckCutEmptyProcess);
        getState();
        final String[]PrintSpeed=getResources().getStringArray(R.array.spinnerspeed);//建立数据源
        final String[]PrintDensity=getResources().getStringArray(R.array.spinnerdensity);
       // ArrayAdapter<String>adapter= new ArrayAdapter<String>(this,android.R.layout.speedlayout,PrintSpeed);//建立Adapter并且绑定数据源
        ArrayAdapter<String> adapterSpeed = new ArrayAdapter<String>(this.getApplicationContext(),R.layout.speedlistlayout , PrintSpeed);
        adapterSpeed.setDropDownViewResource(R.layout.speedlayout);
//第一个参数表示在哪个Activity上显示，第二个参数是系统下拉框的样式，第三个参数是数组。

        for(int i=0;i<120;i++)
        {
            pickerContrastString[i]=Integer.toString(i+1);
            if(i<100)
            pickerDitherString[i]= Integer.toString(i+1);
        }

        SpinnerSpeed.setAdapter(adapterSpeed);//绑定Adapter到控件
        SpinnerSpeed.setSelection(PrintSpeed_int);
        ArrayAdapter<String>adapterDensity=new ArrayAdapter<String>(this,R.layout.speedlistlayout,PrintDensity);
        adapterDensity.setDropDownViewResource(R.layout.speedlayout);
        SpinnerDensity.setAdapter(adapterDensity);

        SpinnerDensity.setSelection(PrintDensity_int);
        ContrastNumberInput.setText(PrintContrast_int+"");
        DitherNumberInput.setText(PrintHalftoneNumber_int+"");
        //HalftoneFunction.setChecked(PrintHalftone_bollean);
        CheckHalftone.setChecked(PrintHalftone_bollean);
        CheckBarcodeProcess.setChecked(PrinttCheckBarcodeProcess_bollean);
        CheckCutEmptyProcess.setChecked(PrintCutEmptyProcess_bollean);
        RadioGroupHalftone.check(PrintHalftone_int);
        CheckHalftone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrintHalftone_bollean=isChecked;
                saveState();
            }
        });
        CheckBarcodeProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrinttCheckBarcodeProcess_bollean=isChecked;
                saveState();
            }
        });
        CheckCutEmptyProcess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrintCutEmptyProcess_bollean=isChecked;
                saveState();
            }
        });
        RadioGroupHalftone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton=findViewById(checkedId);
                PrintHalftone_int=checkedId;
                PrintHalftone_string=radioButton.getText().toString();
                saveState();
               //Toast.makeText(PDFSetFunctionActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        SpinnerSpeed.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrintSpeed_int=Integer.parseInt(PrintSpeed[position])-1;

                saveState();
                //Toast.makeText(PDFSetFunctionActivity.this, PrintSpeed[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ContrastNumberInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.popupwindow_huidu, null);
                final TextView button = view.findViewById(R.id.ContrastButton);
                timeFlag=PrintContrast_int;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrintContrast_int=timeFlag;
                        ContrastNumberInput.setText(PrintContrast_int+"");
                        popupWindow.dismiss();
                        saveState();
                    }
                });
                final com.shawnlin.numberpicker.NumberPicker mNumberPicker=view.findViewById(R.id.makeContrastNumberPicker);

                mNumberPicker.setDisplayedValues(pickerContrastString);//设置需要显示的数组
                mNumberPicker.setMinValue(0);
                mNumberPicker.setWrapSelectorWheel(false);
                mNumberPicker.setMaxValue(pickerContrastString.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法
                mNumberPicker.setValue(timeFlag-1);

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
                        timeFlag=newVal+1;
                    }
                });
                makePopupWiondo(view);
            }
        });


        DitherNumberInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.popupwindow_huidu, null);
                final TextView button = view.findViewById(R.id.ContrastButton);
                ditherFlag=PrintHalftoneNumber_int;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrintHalftoneNumber_int=ditherFlag;
                        DitherNumberInput.setText(PrintHalftoneNumber_int+"");
                        popupWindow.dismiss();
                        saveState();
                    }
                });
                final com.shawnlin.numberpicker.NumberPicker mNumberPicker=view.findViewById(R.id.makeContrastNumberPicker);

                mNumberPicker.setDisplayedValues(pickerDitherString);//设置需要显示的数组
                mNumberPicker.setMinValue(0);
                mNumberPicker.setWrapSelectorWheel(false);
                mNumberPicker.setMaxValue(pickerDitherString.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法
                mNumberPicker.setValue(ditherFlag-1);

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
                        ditherFlag=newVal+1;
                    }
                });
                makePopupWiondo(view);
            }
        });


//        HalftoneFunction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//
//            public void onCheckedChanged(CompoundButton buttonView,
//
//                                         boolean isChecked) {
//
//// TODO Auto-generated method stub
//
//                if (isChecked) {
//
//                    PrintHalftone_bollean=true;
//
//                } else {
//
//                    PrintHalftone_bollean=false;
//
//                }
//                saveState();
//            }
//
//        });
        backPDFFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backPDFFunctionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SpinnerDensity.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PrintDensity_int=Integer.parseInt(PrintDensity[position])-1;
                saveState();
                //Toast.makeText(PDFSetFunctionActivity.this, PrintDensity[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //init();
    }
    public void makePopupWiondo(View view) {
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.anim_bottom_pop);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    private void saveState()//存储数据到手机中
    {
        SharedPreferences sharedPref=this.getSharedPreferences("PDFPrintSet3693", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putInt("PrintSpeed",PrintSpeed_int);
        editor.putInt("PrintDensity",PrintDensity_int);
        editor.putInt("PrintContrast",PrintContrast_int);
        editor.putBoolean("PrintHalftone",PrintHalftone_bollean);
        editor.putInt("PrintHalftoneInt",PrintHalftone_int);
        editor.putString("PrintHalftoneString",PrintHalftone_string);
        editor.putInt("PrintHalftoneNumber_int",PrintHalftoneNumber_int);
        editor.putBoolean("PrintBarcodeProcess",PrinttCheckBarcodeProcess_bollean);
        editor.putBoolean("PrintCutEmptyProcess",PrintCutEmptyProcess_bollean);


        editor.commit();
    }
    private void getState()
    {
        SharedPreferences sp=getSharedPreferences("PDFPrintSet3693", Context.MODE_PRIVATE);
        PrintSpeed_int=sp.getInt("PrintSpeed",2);
        PrintDensity_int=sp.getInt("PrintDensity",9);
        PrintContrast_int=sp.getInt("PrintContrast",60);
        PrintHalftone_bollean=sp.getBoolean("PrintHalftone",true);
        PrintHalftone_int=sp.getInt("PrintHalftoneInt",R.id.RadioDither);
        PrintHalftone_string=sp.getString("PrintHalftoneString","Dither");
        PrintHalftoneNumber_int=sp.getInt("PrintHalftoneNumber_int",50);
        PrinttCheckBarcodeProcess_bollean=sp.getBoolean("PrintBarcodeProcess",false);
        PrintCutEmptyProcess_bollean=sp.getBoolean("PrintCutEmptyProcess",false);
    }
//    private void init()
//    {
//        String[] mProvinces={"1","2","3","4","5"};
//        ArrayList arrayList=new ArrayList<String>();
//        for(int i=0;i<mProvinces.length;i++)
//        {
//            arrayList.add(mProvinces[i]);
//        }
//        //这里使用自带的arrayadapter
//        ArrayAdapter arrayAdapter=new
//                ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,arrayList);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        SpinnerSpeed.setAdapter(arrayAdapter);
//
//    }
}
