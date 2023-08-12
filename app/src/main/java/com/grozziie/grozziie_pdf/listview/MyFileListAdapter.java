package com.grozziie.grozziie_pdf.listview;;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.grozziie.grozziie_pdf.R;

import java.util.List;

public class MyFileListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;//布局装载对象
    private String mfileAddress;
    private List<String> mlist;
    private List<String> mdate;
    //context：要是用当前的Adapter的界面对象
    public MyFileListAdapter(Context context, String fileAddress){
        this.mContext=context;
        mLayoutInflater= LayoutInflater.from(context);
        mfileAddress=fileAddress;

    }
    public void setMyFileListAdapter(List<String> list, List<String> date){
        mlist=list;
        mdate=date;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(mlist==null)
            return 0;
            else
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    static  class  ViewHolder{
        public ImageView imageView;
        public TextView tvTile,tvTime,tvContent;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView=mLayoutInflater.inflate(R.layout.layout_listview_item,null);
            holder =new ViewHolder();
            holder.imageView=convertView.findViewById(R.id.imV_List);
            holder.tvTile=convertView.findViewById(R.id.tV_Title);
            holder.tvTime=convertView.findViewById(R.id.tV_Date);
            holder.tvContent=convertView.findViewById(R.id.tV_Content);
            convertView.setTag(holder);
        }
        else{
                holder= (ViewHolder) convertView.getTag();

        }
//        String xxx="111";
//        File file = new File(mfileAddress);
//        if(null==file || !file.exists()){
//
//        }
//        else {
//            File[] files = file.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                 xxx = files[i].getName();
//
//            }
//        }
        String fileName= mlist.get(position);
        String HouZhui=fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();

        if(HouZhui.equals("pdf"))
        {
            holder.imageView.setImageResource(R.drawable.pdf64);
            holder.tvTime.setText(mdate.get(position));
            holder.tvContent.setText("");
            holder.tvTile.setText(mlist.get(position));
        }


//        else if(HouZhui.equals("doc")||HouZhui.equals("docx"))
//        {
//            holder.imageView.setImageResource(R.drawable.word64);
//        }
//        else if(HouZhui.equals("txt"))
//        {
//            holder.imageView.setImageResource(R.drawable.txt64);
//        }
//        else if(HouZhui.equals("xls")||HouZhui.equals("xlsx"))
//        {
//            holder.imageView.setImageResource(R.drawable.xls64);
//        }
//        else
//        {
//            holder.imageView.setImageResource(R.drawable.unknown64);
//        }

//        holder.tvTime.setText(mdate.get(position));
//        holder.tvContent.setText("");



        return convertView;
    }
}
