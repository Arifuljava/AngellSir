package com.grozziie.grozziie_pdf;;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter <MarketAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    private int listsize;
    private List barcodeInformationList;
    private List informationList;
    private ArrayList<String> informationArrayList;

    public MarketAdapter(Context context, int listsize,OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
        this.listsize=listsize;
    }
    public void refreshBarcodeViewList(List list, int listsize) {
        this.barcodeInformationList= new ArrayList();
        this.informationList= new ArrayList();
        this.barcodeInformationList.addAll(list);
        this.listsize=listsize;

        //mDirPath=filePath;
        notifyDataSetChanged();
    }
    @Override
    public MarketAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_market_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.LinearViewHolder holder, final int position) {

       // holder.imageView.setImageBitmap((mList.get(position)));
//        barcodeInformationListNow.add(barcodenumber);
//        barcodeInformationListNow.add(barcodename);
//        barcodeInformationListNow.add(barcodetime);
//        barcodeInformationListNow.add(barcodeinformation);
//        barcodeInformationListNow.add(bitmap);
//        barcodeInformationListNow.add(bitmapB);
        informationList.clear();
        informationList.addAll((List)barcodeInformationList.get(position));
        Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
        informationArrayList=new ArrayList<String>();
        informationArrayList = gson.fromJson((informationList.get(3).toString()), type);
        String barcodeSize=informationArrayList.get(2)+"*"+informationArrayList.get(3)+"mm";
        holder.textViewTitle.setText(informationList.get(1).toString());
        holder.textViewSize.setText(barcodeSize);
        holder.textViewTime.setText(informationList.get(2).toString());
        holder.imageView.setImageBitmap((Bitmap)informationList.get(4));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,"click..."+position,Toast.LENGTH_SHORT).show();  用于adapter内部调用click事件
                mListener.onClick(position);//调用接口用于外部调用click事件
            }
        });
    }

    @Override
    public int getItemCount() {
        return listsize;
    }
    //用LinearViewHolder实现Adapter 继承自 RecyclerView.ViewHolder
    class LinearViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewTitle;
        private TextView textViewSize;
        private TextView textViewTime;
        private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle=itemView.findViewById(R.id.HistoryLabelTitle);
            textViewSize=itemView.findViewById(R.id.HistoryLabelSize);
            textViewTime=itemView.findViewById(R.id.HistoryLabelTime);
            imageView=itemView.findViewById(R.id.HistoryLabelImage);






        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
}
