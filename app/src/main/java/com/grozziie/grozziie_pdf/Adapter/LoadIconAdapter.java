package com.grozziie.grozziie_pdf.Adapter;

import android.content.Context;
import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;




import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grozziie.grozziie_pdf.Image.ImageLoader;
import com.grozziie.grozziie_pdf.R;

public class LoadIconAdapter extends RecyclerView.Adapter <LoadIconAdapter.LinearViewHolder>{
    private Context  mContext;
    private OnItemClickListener mListener;
    private  int emojiType;
    private  int listSize;
    //private List<String> mList;
    private ArrayList<String> mList;
    private String mDirPath;
    private ImageLoader mImageLoader;
    private AssetManager mAssetManager;
    private String getAssetPath;
    public LoadIconAdapter(Context context,ArrayList<String> list, int listsize,AssetManager mAssetManager,OnItemClickListener Listener)
    {
        this.mContext=context;
        this.mListener=Listener;
        this.listSize=listsize;
        this.mAssetManager=mAssetManager;
        this.mList=list;
        mImageLoader = ImageLoader.getInstance(4 , ImageLoader.Type.LIFO);
        //this.mList=list;

    }
    public void refreshGetAssetsList(ArrayList<String> list, int listsize,AssetManager mAssetManager) {
        this.mList = list;
        this.listSize=listsize;
        //this.getAssetPath=getAssetPath;
        this.mAssetManager=mAssetManager;
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
    public LoadIconAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_loadimage_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadIconAdapter.LinearViewHolder holder, final int position) {

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
//        mImageLoader.loadImage(mList.get(position),
//                holder.imageView);

        holder.imageView.setImageBitmap(loadImage(mList.get(position)));
//        //==============================================================
//        try {
//
//            //mAssetManager =getAssets();
//            String[] mAssetsImageList;
//            mAssetsImageList=mAssetManager.list("images/0");
//
//                String imagePath = "images/0" + "/" + mAssetsImageList[position];
//
//                holder.imageView.setImageBitmap(loadImage(mList.get(position)));
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



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
    //从assets中读取图片
    private Bitmap loadImage(String path) {
        Bitmap image = null;
        InputStream in = null;
        try {
            in = mAssetManager.open(path);
            image = BitmapFactory.decodeStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return image;
    }
}
