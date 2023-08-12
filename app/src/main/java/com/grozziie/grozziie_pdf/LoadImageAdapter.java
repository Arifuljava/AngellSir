package com.grozziie.grozziie_pdf;;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grozziie.grozziie_pdf.Image.ImageLoader;

public class LoadImageAdapter extends RecyclerView.Adapter <LoadImageAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    private  int emojiType;
    private  int listSize;
    //private List<String> mList;
    private ArrayList<String> mList;
    private String mDirPath;
    private ImageLoader mImageLoader;
    public LoadImageAdapter(Context context,  int emojiSum, OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
        this.listSize=emojiSum;
        this.emojiType=emojiType;
        mImageLoader = ImageLoader.getInstance(4 , ImageLoader.Type.LIFO);
        //this.mList=list;

    }
    public void refreshPathList(ArrayList<String> list, int listsize) {
        mList = list;
        listSize=listsize;
        //mDirPath=filePath;
        notifyDataSetChanged();
    }
//    public void refreshPathList(ArrayList<String> list, int listsize) {
//        mList = list;
//        listSize=listsize;
//        notifyDataSetChanged();
//        mImageLoader = ImageLoader.getInstance(4 , Type.LIFO);
//        //mDirPath=filePath;
//    }
    @Override
    public LoadImageAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_loadimage_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadImageAdapter.LinearViewHolder holder, final int position) {

//        int emojiText=emojiType;
//        String emojiString = getEmojiStringByUnicode(emojiText+position);
//        holder.textView.setText(emojiString);
//        holder.textView.setTextSize(30);
//        holder.textView.setTextColor(Color.BLACK);

        //Bitmap bitmap= BitmapFactory.decodeFile(mList.get(position));
        //holder.imageView.setImageBitmap(bitmap);

        //holder.imageView.setImageURI(Uri.fromFile(new File(mList.get(position))));

        //holder.imageView.setImageResource(R.drawable.xiaoai);
        //使用Imageloader去加载图片
        mImageLoader.loadImage(mList.get(position),
                holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
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
        private ImageView imageView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.loadImagePicture);




        }
    }
    public interface OnItemClickListener{
        void  onClick(int pos);
    }
    private String getEmojiStringByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
}
