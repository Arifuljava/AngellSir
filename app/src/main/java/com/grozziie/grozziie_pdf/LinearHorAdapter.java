package com.grozziie.grozziie_pdf;;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LinearHorAdapter extends RecyclerView.Adapter <LinearHorAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    public LinearHorAdapter(Context context, OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
    }
    @NonNull
    @Override
    public LinearHorAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_linerhor_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearHorAdapter.LinearViewHolder holder, final int position) {
        switch (position) {
            case 0:holder.textView.setText("文本");
                    holder.imageView.setImageResource(R.drawable.inputwords);
                    break;
            case 1:holder.textView.setText("图标");
                    holder.imageView.setImageResource(R.drawable.inputicon);
                    break;
            case 2:holder.textView.setText("涂鸦");
                    holder.imageView.setImageResource(R.drawable.inputtuya);
                    break;
            case 3:holder.textView.setText("条形码");
                    holder.imageView.setImageResource(R.drawable.inputbarcode);
                    break;
            case 4:holder.textView.setText("二维码");
                    holder.imageView.setImageResource(R.drawable.inputqrcode);
                    break;
                    default:holder.textView.setText("");
                    holder.imageView.setImageBitmap(null);
                    break;

        }

        //holder.textView.setText("一维码");


        //holder.imageView.setImageBitmap(bitmap);


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
        return 5;
    }
    //用LinearViewHolder实现Adapter 继承自 RecyclerView.ViewHolder
    class LinearViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.editBarcodeText);
            imageView=itemView.findViewById(R.id.editBarcodeImage);
        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
}
