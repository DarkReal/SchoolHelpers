package com.xxw.student.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;

import java.util.HashMap;
import java.util.List;

/**
 * 公司评论的adapter
 * Created by DarkReal on 2016/7/24.
 */
public class ResumeAdapter_group extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        教育背景
        TextView text_id;
        ImageView group_list_pic;
        TextView group_list_name;
        TextView group_list_title;
        TextView group_list_content;
        TextView group_list_like;
        TextView group_list_comment;
    }
    private LayoutInflater mInflater;
    private List<HashMap<String, String>> list;
    private int layoutID;
    private String flag[];
    private int ItemIDs[];
    private Activity activity;
    private Context mcontext;
    private static View mConvertView;

    private BitmapUtils bitmapUtils;

    public ResumeAdapter_group(Context context, Activity activity, List<HashMap<String, String>> list,
                               int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;
        this.bitmapUtils = new BitmapUtils(mcontext);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        mConvertView = convertView;
        TextViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            holder = new TextViewHolder();

            convertView = mInflater.inflate(layoutID, null);
            // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件
            holder.text_id = (TextView) convertView.findViewById(R.id.text_id);
            holder.group_list_pic = (ImageView) convertView.findViewById(R.id.group_list_pic);
            holder.group_list_name = (TextView) convertView.findViewById(R.id.group_list_name);
            holder.group_list_title = (TextView) convertView.findViewById(R.id.group_list_title);
            holder.group_list_content = (TextView) convertView.findViewById(R.id.group_list_content);
            holder.group_list_like = (TextView) convertView.findViewById(R.id.group_list_like);
            holder.group_list_comment = (TextView) convertView.findViewById(R.id.group_list_comment);
            convertView.setTag(holder);

        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (TextViewHolder) convertView.getTag();
        }
        for (int i = 0; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
        }
        bitmapUtils.display(holder.group_list_pic, Constant.getUrl() + "upload/media/images/" + list.get(position).get("group_list_pic").toString());

        return convertView;
    }

}
