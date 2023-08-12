package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MakeBarcodeAdapter extends RecyclerView.Adapter <MakeBarcodeAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    public MakeBarcodeAdapter(Context context, OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
    }

    @Override
    public MakeBarcodeAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_makebarcode_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MakeBarcodeAdapter.LinearViewHolder holder, final int position) {
        holder.textView.setText("Grid");


        switch (position) {
            case 0:holder.textView.setText("文本");
                holder.imageView.setImageResource(R.drawable.zgtext);
                break;
            case 1:holder.textView.setText("图片");
                holder.imageView.setImageResource(R.drawable.zgtubiao);
                break;
            case 2:holder.textView.setText("日期");
                holder.imageView.setImageResource(R.drawable.zgdate);
                break;
            case 3:holder.textView.setText("时间");
                holder.imageView.setImageResource(R.drawable.zgtime);
                break;
            case 4:holder.textView.setText("图标");
                holder.imageView.setImageResource(R.drawable.zgsymbol);
                break;
            case 5:holder.textView.setText("条形码");
                holder.imageView.setImageResource(R.drawable.zgbarcode);
                break;
            case 6:holder.textView.setText("二维码");
                holder.imageView.setImageResource(R.drawable.zgqrcode);
                break;
            case 7:holder.textView.setText("涂鸦");
                holder.imageView.setImageResource(R.drawable.zggraffiti);
                break;
            case 8:holder.textView.setText("边框");
                holder.imageView.setImageResource(R.drawable.zgframe);
                break;
            case 9:holder.textView.setText("扫描");
                holder.imageView.setImageResource(R.drawable.zgscanner);
                break;

            default:holder.textView.setText("");
                holder.imageView.setImageBitmap(null);
                break;

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
        return 10;
    }
    //用LinearViewHolder实现Adapter 继承自 RecyclerView.ViewHolder
    class LinearViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.zgbarcodeText);
            imageView=itemView.findViewById(R.id.zgBarcodeImage);


        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
}
