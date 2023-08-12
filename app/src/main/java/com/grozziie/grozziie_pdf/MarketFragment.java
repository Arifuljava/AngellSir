package com.grozziie.grozziie_pdf;;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grozziie.grozziie_pdf.SqliteHistorySave.SQLiteHelper;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MarketFragment extends Fragment {
    private MarketAdapter marketadapter;
    private RecyclerView MarketRecycle;
    private Activity  MarketActivity;
    private EditText MarketText;
    private ImageView BackHome;
    private  HomeFragment homeFragment;
    private RelativeLayout marketBackground;
    private ImageView marketDeviceState;
    private SQLiteDatabase dbN=null;
    private List barcodeInformationList;
    private List viewInformationList;
    private List barcodeInformationListNow;
    private  List viewInformationListNow;
    //private MarketFragment.CallBackValue callBackValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_market,container,false);
        return  view;

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MarketRecycle=view.findViewById(R.id.marketRecycle);
        MarketText=view.findViewById(R.id.ET_1);
        BackHome=view.findViewById(R.id.backLast);
        marketDeviceState=view.findViewById(R.id.marketDeviceState);
        marketBackground=view.findViewById(R.id.marketBackground);
        //隐藏打印机链接状态==========================================
//        if(BlueToothMainActivity.socketall==null)
//        {
//            marketDeviceState.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            marketDeviceState.setVisibility(View.GONE);
//        }
        //===================================================================
        ConnectDB();
        MarketRecycle.setLayoutManager(new GridLayoutManager(MarketActivity,2));
        marketadapter=new MarketAdapter(MarketActivity,0, new MarketAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {

                // holder.imageView.setImageBitmap((mList.get(position)));
//        barcodeInformationListNow.add(barcodenumber);
//        barcodeInformationListNow.add(barcodename);
//        barcodeInformationListNow.add(barcodetime);
//        barcodeInformationListNow.add(barcodeinformation);
//        barcodeInformationListNow.add(bitmap);
//        barcodeInformationListNow.add(bitmapB);
                viewInformationListNow=new ArrayList();
                viewInformationListNow=(List)barcodeInformationList.get(pos);

                //callBackValue.SendMessageValue(viewInformationListNow);



                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList informationArrayList=new ArrayList<String>();
                informationArrayList = gson.fromJson((viewInformationListNow.get(3).toString()), type);
                //String barcodeSize=informationArrayList.get(2)+"*"+informationArrayList.get(3)+"mm";
                int barcodeWidth=Integer.parseInt(informationArrayList.get(2).toString());
                int barcodeHeight=Integer.parseInt(informationArrayList.get(3).toString());
//
//                String barcodeNumber=viewInformationListNow.get(0).toString();
//                Toast.makeText(MarketActivity,"ClickYour"+pos,Toast.LENGTH_SHORT).show();
                hintKeyboard(getActivity());
                Bundle bundle=new Bundle();
                bundle.putString("BarcodeNumber",viewInformationListNow.get(0).toString());
                bundle.putInt("IntentFlag",1);
                bundle.putInt("BarcodeWidth",barcodeWidth);
                bundle.putInt("BarcodeHeight",barcodeHeight);
                bundle.putString("BarcodeName",viewInformationListNow.get(1).toString());

                Intent intent =new Intent();
                //intent.putExtra("IntentFlag",1);
                intent.putExtras(bundle);

                intent.setClass(getActivity(),BarcodeActivity.class);
                startActivity(intent);
//                getActivity().finish();
            }
        });
        MarketRecycle.setAdapter(marketadapter);
//        MarketRecycle.setAdapter(new MarketAdapter(MarketActivity,0, new MarketAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                Toast.makeText(MarketActivity,"ClickYour"+pos,Toast.LENGTH_SHORT).show();
//                hintKeyboard(getActivity());
//            }
//        }));
        BackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    FragmentManager MF = getActivity().getSupportFragmentManager();
//                    MF.beginTransaction().replace(R.id.goodsDisplay,new HomeFragment()).commitAllowingStateLoss();


//                if(homeFragment==null)
//                    homeFragment=new HomeFragment();
//               getSupportFragmentManager().beginTransaction().replace(R.id.goodsDisplay,homeFragment).commitAllowingStateLoss();
                if(getActivity() != null) {
                    getActivity().finish();
                }

            }
        });
        setHintKeyboardView(MarketRecycle);
        //setHintKeyboardView(view);
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(getActivity().getCurrentFocus()!=null && getActivity().getCurrentFocus().getWindowToken()!=null){
                        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                return false;
            }
        });


    }
    /**
     * 设置点击软键盘之外区域，软键盘消失
     *
     * @param view
     */
    public void setHintKeyboardView(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hintKeyboard(getActivity());
                    return false;
                }
            });
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    hintKeyboard(getActivity());
//                    //return false;
//                }
//            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setHintKeyboardView(innerView);
            }
        }
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        //如果是点击事件，获取点击的view，并判断是否要收起键盘
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            //获取目前得到焦点的view
//            View v = getCurrentFocus();
//            //判断是否要收起并进行处理
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//            }
//        }
//        //这个是activity的事件分发，一定要有，不然就不会有任何的点击事件了
//        return super.dispatchTouchEvent(ev);
//    }
//
//    //判断是否要收起键盘
//    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
//        //如果目前得到焦点的这个view是editText的话进行判断点击的位置
//        if (v instanceof EditText) {
//            int[] l = {0, 0};
//            v.getLocationInWindow(l);
//            int left = l[0],
//                    top = l[1],
//                    bottom = top + v.getHeight(),
//                    right = left + v.getWidth();
//            // 点击EditText的事件，忽略它。
//            return !(event.getX() > left) || !(event.getX() < right)
//                    || !(event.getY() > top) || !(event.getY() < bottom);
//        }
//        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上
//        return false;
//    }
//
//    //隐藏软键盘并让editText失去焦点
//    private void hideKeyboard(IBinder token) {
//        MarketText.clearFocus();
//        if (token != null) {
//            //这里先获取InputMethodManager再调用他的方法来关闭软键盘
//            //InputMethodManager就是一个管理窗口输入的manager
//            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (im != null) {
//                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//    }
public static void hintKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm.isActive() && activity.getCurrentFocus() != null) {
        if (activity.getCurrentFocus().getWindowToken() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
    private Handler nHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {

            String path;
            if(msg.what==1) {
                marketadapter.refreshBarcodeViewList(barcodeInformationList,barcodeInformationList.size());
            }

        };
    };
private void HistoryDisplayInformation()
{
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            viewInformationList.clear();
            Cursor cursor = dbN.rawQuery("select _barcodenumber,_barcodename,_barcodetime,_barcodeinformation,_barcodeimage,_barcodebackgroundimage from barcodeview where _id!='';", null);

            HashMap<String, String> map = new HashMap<String, String>();
            ArrayList<Map<String,Object>> barcodeImageList=new ArrayList<Map<String,Object>>();
            //ArrayList<Map<String,Object>> barcodeInformationList=new ArrayList<Map<String,Object>>();
            Map<String, Object> barcodeImageMap = new TreeMap<String, Object>(); //存储图片路径和转换成的 bitmap
            Map<String, Object> barcodeInformationMap = new TreeMap<String, Object>(); //存储图片路径和转换成的 bitmap
            Gson gson = new Gson();

            while(cursor.moveToNext()){
                //遍历出表名
                //viewInformationList.clear();
                barcodeInformationListNow=new ArrayList();

                String barcodenumber = cursor.getString(0);
                String barcodename=cursor.getString(1);
                String barcodetime=cursor.getString(2);
                String barcodeinformation=cursor.getString(3);
                //cursor.getInt(cursor.getColumnIndex("_id"));
//                Type type = new TypeToken<ArrayList<String>>() {}.getType();
//
//                viewInformationList = gson.fromJson(barcodeinformation, type);

                byte[] photoByte=cursor.getBlob(4);
                Bitmap bitmap= BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

                byte[] photoByteB;
                Bitmap bitmapB=null;
                if(cursor.getBlob(5)!=null) {
                    photoByteB = cursor.getBlob(5);
                     bitmapB = BitmapFactory.decodeByteArray(photoByteB, 0, photoByteB.length);
                }



                barcodeInformationListNow.add(barcodenumber);
                barcodeInformationListNow.add(barcodename);
                barcodeInformationListNow.add(barcodetime);
                barcodeInformationListNow.add(barcodeinformation);
                barcodeInformationListNow.add(bitmap);
                barcodeInformationListNow.add(bitmapB);

//                viewInformationList.add(barcodenumber);
//                viewInformationList.add(barcodename);
//                viewInformationList.add(barcodetime);
//                viewInformationList.add(barcodeinformation);
//                barcodeInformationMap.put("barcodeImage",bitmap);
//                barcodeInformationMap.put("backgroundImage",bitmapB);
//                barcodeInformationMap.put("barcodeInformation",viewInformationList);
//                barcodeInformationList.add(barcodeInformationMap);
                barcodeInformationList.add(barcodeInformationListNow);
            }
            if(cursor.getCount()>0)
                nHandler.sendEmptyMessage(1);
            else
            {
                Intent intent =new Intent();
                intent.setClass(getActivity(),BarcodeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
                //marketBackground.setBackgroundResource(R.drawable.pdfmain);


        }
    });
    thread.start();
}
private void ConnectDB(){
    viewInformationList=new ArrayList<>();
    barcodeInformationList=new ArrayList<>();

    SQLiteHelper helper = new SQLiteHelper(MarketActivity,"historybarcodedata.db",null,1);
    dbN = helper.getWritableDatabase();
    HistoryDisplayInformation();
}
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MarketActivity= (Activity) context;

        //callBackValue = (CallBackValue) getActivity();//回调接口
    }

//    //定义一个回调接口
//    public interface CallBackValue{
//        public void SendMessageValue(List listFragment);
//    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

}

