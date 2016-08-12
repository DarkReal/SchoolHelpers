package com.xxw.student.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.GoodView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 公司评论的adapter
 * Created by DarkReal on 2016/7/24.
 */
public class ResumeAdapter_comcom extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        教育背景
        TextView username;
        TextView comment_time;
        TextView comment_value;
        ImageView touxiang;
        ImageView company_like_pic;
        TextView dz_count;
        String ispressed;//判断是否被选中了
        String eachid;
    }
    private LayoutInflater mInflater;
    private List<HashMap<String, String>> list;
    private int layoutID;
    private String flag[];
    private int ItemIDs[];
    private Activity activity;
    private Context mcontext;
    public static String currentItem = "";//标记当前选中项
    private int currentItemInt = -1;
    private static View mConvertView;
    private static boolean needtoshow = false;//默认不展示

    private BitmapUtils bitmapUtils;

    public ResumeAdapter_comcom(Context context, Activity activity, List<HashMap<String, String>> list,
                                int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;
        this.bitmapUtils = new BitmapUtils(this.mcontext);
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
            holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
            holder.comment_value = (TextView) convertView.findViewById(R.id.comment_value);
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.dz_count = (TextView) convertView.findViewById(R.id.dz_count);

            holder.touxiang = (ImageView) convertView.findViewById(R.id.touxiang);
            holder.company_like_pic = (ImageView) convertView.findViewById(R.id.company_like_pic);
            holder.ispressed = list.get(position).get("comment_like_pic").toString();
            convertView.setTag(holder);

        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (TextViewHolder) convertView.getTag();
        }
        for (int i = 0; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
        }
        bitmapUtils.display(holder.touxiang, Constant.getUrl() + "upload/media/images/" + list.get(position).get("touxiang"));

        LogUtils.v(position+"----"+holder.ispressed.toString());
        if(holder.ispressed.toString().equals("1")){
            holder.company_like_pic.setImageResource(R.drawable.like_pressed);
        }else{
            holder.company_like_pic.setImageResource(R.drawable.like_unclick);
        }

        holder.eachid = list.get(position).get("eachid").toString();


        if(Integer.parseInt(list.get(position).get("dz_count")) > 0){//点赞数不为0的时候,显示点赞数
            holder.dz_count.setVisibility(View.VISIBLE);
        }else{
            holder.dz_count.setVisibility(View.GONE);
        }

        //点赞的动画效果以及触发的事件

        final TextViewHolder finalHolder = holder;
        holder.company_like_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (finalHolder.ispressed) {
                    case "1":
                        finalHolder.company_like_pic.setImageResource(R.drawable.like_unclick);
                        String count = finalHolder.dz_count.getText().toString();
                        if(finalHolder.dz_count.getText().toString()=="1"){
                            finalHolder.dz_count.setText((Integer.parseInt(count) - 1) + "");
                            finalHolder.dz_count.setVisibility(View.GONE);
                        }else{
                            finalHolder.dz_count.setText((Integer.parseInt(count) - 1) + "");
                        }
                        finalHolder.ispressed ="0";
                        likeEvent(finalHolder);
                        break;
                    case "0"://点赞
                        finalHolder.company_like_pic.setImageResource(R.drawable.like_pressed);
                        finalHolder.dz_count.setText((Integer.parseInt(finalHolder.dz_count.getText().toString()) + 1) + "");
                        finalHolder.dz_count.setVisibility(View.VISIBLE);
                        //点赞部分
                        GoodView goodView = new GoodView(mcontext);
                        finalHolder.company_like_pic.setImageResource(R.drawable.like_pressed);
                        goodView.setImage(activity.getResources().getDrawable(R.drawable.like_pressed));
                        goodView.show(finalHolder.company_like_pic);

                        finalHolder.ispressed ="1";
                        likeEvent(finalHolder);
                        break;
                }
            }
        });

        return convertView;

    }

    private void likeEvent(TextViewHolder finalHolder) {
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("token", MainActivity.token);
        map.put("id",finalHolder.eachid);
        LogUtils.v(map.toString());
        String url = Constant.getUrl()+"app/company/admireCompanyCom.htmls";
        try{
            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if(obj!=null){
                        final String message = obj.get("message").toString();
                        LogUtils.v(obj.get("message").toString());
                        LogUtils.v("obj" + obj.toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (obj.get("code").toString().equals("-1"))
                                        Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
                                    else {
                                        //更新帖子列表显示内容
                                        Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            };
            ht.start();

        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}
