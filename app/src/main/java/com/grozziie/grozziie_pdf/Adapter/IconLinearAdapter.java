package com.grozziie.grozziie_pdf.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grozziie.grozziie_pdf.R;

import java.util.ArrayList;

public class IconLinearAdapter extends RecyclerView.Adapter <IconLinearAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    private ArrayList<String> mList;
    private String mDirPath;
    private  int listSize;
    private int textcolorFlag;
    public IconLinearAdapter(Context context,int incoTilteSize, OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
        this.listSize=incoTilteSize;
    }
    public void RefreshIconTitleList(ArrayList<String> list, int listsize,int textcolorFlag) {
        this.mList = list;
        this.listSize=listsize;
        this.textcolorFlag=textcolorFlag;
        //mDirPath=filePath;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public IconLinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_icontitle_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull IconLinearAdapter.LinearViewHolder holder, final int position) {

        holder.textView.setText(mList.get(position));
        if(position==textcolorFlag)
        {
            holder.textView.setTextColor(Color.BLUE);
        }
        else{
            holder.textView.setTextColor(Color.BLACK);
        }
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
        return listSize;
    }
    //用LinearViewHolder实现Adapter 继承自 RecyclerView.ViewHolder
    class LinearViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        //private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.IconTitleText);
            //imageView=itemView.findViewById(R.id.IconTitleImage);

        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
}
