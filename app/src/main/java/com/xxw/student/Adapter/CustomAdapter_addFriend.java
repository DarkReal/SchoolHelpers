package com.xxw.student.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 添加朋友的adapter
 * Created by DarkReal on 2016/7/24.
 */
public class CustomAdapter_addFriend extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        教育背景
        ImageView image;
        TextView name;
        TextView college;
        Button add_friend;
        TextView friend_id;
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
    private MaterialDialog materialDialog;

    private BitmapUtils bitmapUtils;

    public CustomAdapter_addFriend(Context context, Activity activity, List<HashMap<String, String>> list,
                                   int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;
        this.bitmapUtils = new BitmapUtils(this.mcontext);
        this.materialDialog = new MaterialDialog(mcontext);

    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int arg0) {
        return arg0;
    }
    @Override
    public long getItemId(int arg0) {
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
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.college = (TextView) convertView.findViewById(R.id.college);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.add_friend = (Button) convertView.findViewById(R.id.add_friend);
            holder.friend_id = (TextView) convertView.findViewById(R.id.friend_id);
            convertView.setTag(holder);

        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏
            holder = (TextViewHolder) convertView.getTag();
        }
        for (int i = 0; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
        }
        bitmapUtils.display(holder.image, Constant.getUrl() + "upload/media/images/" + list.get(position).get("image"));


        //添加好友的点击操作
        final TextViewHolder finalHolder = holder;
        finalHolder.add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //只要点击了，就插入这条记录
                Message message = new Message();
                message.what = -11;
                Commonhandler.comHandler.sendMessage(message);


                final EditText contentView = new EditText(mcontext);
                contentView.setText("说一句话吧");
                contentView.setTextColor(Color.GRAY);
                contentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(b){
                            contentView.setText("");
                        }
                    }
                });
                materialDialog.setContentView(contentView);
                materialDialog.setTitle("打招呼内容（可不填）")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            //单击确认之后发送请求
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("token", MainActivity.token);
                                map.put("id", finalHolder.friend_id.getText().toString());
                                map.put("context", contentView.getText().toString());

                                LogUtils.v(map.toString());
                                String url = Constant.getUrl() + "app/user/applyBulidUser.htmls";
                                try {

                                    HttpThread ht = new HttpThread(url, map) {
                                        @Override
                                        public void getObj(final JSONObject obj) throws JSONException {

                                            if (obj != null) {
                                                final String message;

                                                message = obj.get("message").toString();
                                                LogUtils.v("-------jsonstr------" + obj.toString());
                                                LogUtils.v("message: " + obj.get("message").toString());

                                                getHandler.mHandler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            if (!obj.get("code").toString().equals("10000")) {
                                                                Toast.makeText(mcontext,message,Toast.LENGTH_LONG).show();
                                                            } else {
                                                                new MaterialDialog(mcontext).setTitle("提示")
                                                                        .autodismiss(2000)
                                                                        .setMessage("请求发送成功")
                                                                        .show();
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

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();

            }
        });

        return convertView;
    }
}
