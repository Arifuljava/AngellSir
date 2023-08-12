package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmojiAdapter extends RecyclerView.Adapter <EmojiAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    private  int emojiType;
    private  int emojiSum;
    public EmojiAdapter(Context context,int emojiType,int emojiSum, OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
        this.emojiSum=emojiSum;
        this.emojiType=emojiType;

    }

    @Override
    public EmojiAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_emmojigrid_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiAdapter.LinearViewHolder holder, final int position) {

        int emojiText=emojiType;
        String emojiString = getEmojiStringByUnicode(emojiText+position);
        holder.textView.setText(emojiString);
        holder.textView.setTextSize(30);
        holder.textView.setTextColor(Color.BLACK);
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
        return emojiSum;
    }
    //用LinearViewHolder实现Adapter 继承自 RecyclerView.ViewHolder
    class LinearViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.emojiText);




        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
    private String getEmojiStringByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
