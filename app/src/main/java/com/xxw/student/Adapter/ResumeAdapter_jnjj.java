package com.xxw.student.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.RatingBarView;
import com.xxw.student.view.framework.picker.DatePicker;
import com.xxw.student.view.framework.picker.OptionPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 简历的adpter
 * Created by DarkReal on 2016/7/24.
 */
public class ResumeAdapter_jnjj extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //      技能简介
        TextView jnjj_skill;
        TextView skilldegress;
        RatingBarView skill_rating;
        TextView eachid;//每一个的编号
        LinearLayout del_icon;//删除按钮
    }
    private LayoutInflater mInflater;
    private List<HashMap<String, String>> list;
    private int layoutID;
    private String flag[];
    private int ItemIDs[];
    private Activity activity;
    private OptionPicker optionPicker;
    private Context mcontext;
    private DatePicker datePicker;
    private static HashMap<String,String> hashMap;//每次单击某一项的时候就组成一个Map,方便存储
    public static String currentItem = "";//标记当前选中项
    private int currentItemInt = -1;
    private String TAG = "ResumeAdapter_jnjj";
    private static View mConvertView;
    private static boolean needtoshow = false;//默认不展示
    public static int storagecount=0;
    private MaterialDialog materialDialog;

    public ResumeAdapter_jnjj(Context context, Activity activity, List<HashMap<String, String>> list,
                              int layoutID, String flag[], int ItemIDs[]) {
        Log.i("TAG", "构造方法");
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        mConvertView = convertView;
        TextViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            holder = new TextViewHolder();

            convertView = mInflater.inflate(layoutID, null);
            // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件

            holder.jnjj_skill = (TextView) convertView.findViewById(R.id.jnjj_skill);
            holder.skilldegress = (TextView) convertView.findViewById(R.id.skilldegress);
            holder.skill_rating = (RatingBarView) convertView.findViewById(R.id.skill_ratingbar);
            holder.eachid = (TextView) convertView.findViewById(R.id.jnjj_list_id);
            holder.del_icon = (LinearLayout) convertView.findViewById(R.id.del_linear);
            convertView.setTag(holder);

        } else {// 如果之前已经显示过该页面，则用viewholder中的缓存直接刷屏

            holder = (TextViewHolder) convertView.getTag();
        }
        //开始的时候每一项的删除按钮都要隐藏掉
        if(needtoshow){
            LinearLayout del_layout = (LinearLayout) convertView.findViewById(R.id.del_linear);
            del_layout.setVisibility(View.VISIBLE);
        }else {
            LinearLayout del_layout = (LinearLayout) convertView.findViewById(R.id.del_linear);
            del_layout.setVisibility(View.GONE);
        }

        for (int i = 0; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
            RatingBarView ratingBar = (RatingBarView) convertView.findViewById(R.id.skill_ratingbar);
            ratingBar.setStar(Constant.getNoforSkill(list.get(position).get("skillLevel").toString())+1, false);
        }

        final TextViewHolder finalHolder = holder;
        holder.jnjj_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText contentView = new EditText(mcontext);
                contentView.setText(finalHolder.jnjj_skill.getText().toString());
                contentView.setTextColor(Color.GRAY);
                contentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (b) {
                            contentView.setText("");
                        }
                    }
                });
                materialDialog.setContentView(contentView);
                materialDialog.setTitle("技能名称")
                        .setPositiveButton("确定", new View.OnClickListener() {
                            //单击确认之后发送请求
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                                if (!finalHolder.jnjj_skill.getText().toString().equals(contentView.getText().toString())) {
                                    LogUtils.v("原值：" + finalHolder.jnjj_skill.getText().toString() + "修改后的值" + contentView.getText().toString());
                                    finalHolder.jnjj_skill.setText(contentView.getText().toString());
                                    currentItem = finalHolder.eachid.getText().toString();
                                    LogUtils.v(currentItem + "jnjj_skill");
                                    currentItemInt = getNumber(currentItem);
                                    list.get(currentItemInt).put("skillName", contentView.getText().toString());
                                    changed(-1);
                                }
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();
            }
        });
        //删除事件
        holder.del_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View viewinflator = inflater.inflate(R.layout.custom_alertdialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tv = (TextView) viewinflator.findViewById(R.id.title);
                TextView submit = (TextView) viewinflator.findViewById(R.id.dialog_submit);
                TextView cancel = (TextView) viewinflator.findViewById(R.id.dialog_cancel);

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentItem = finalHolder.eachid.getText().toString();

                        currentItemInt = getNumber(currentItem);
                        delete(currentItem);
                        //发送消息
                        alertDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("确定删除？");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(300, 220);
            }
        });

        holder.skill_rating.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                //Toast.makeText(mcontext, String.valueOf(RatingScore), Toast.LENGTH_SHORT).show();
                //检测数据对应修改
                Constant.setSkill_list();
                //设置文字对应显示
                finalHolder.skilldegress.setText(Constant.skill_list.get(RatingScore - 1));
                currentItem = finalHolder.eachid.getText().toString();
                currentItemInt = getNumber(currentItem);
                list.get(currentItemInt).put("skillLevel", Constant.skill_list.get(RatingScore-1));
                changedFilter(-1);
            }
        });
        return convertView;
    }
    //删除条目
    private void delete(String ids) {
        HashMap<String,String> deletemap = new HashMap<String,String>();
        deletemap.put("token", MainActivity.token);
        deletemap.put("id", ids);
        deletemap.put("index", "3");
        LogUtils.v("map"+deletemap.values().toString());
        String url = Constant.getUrl() + "app/user/deleteResumeItem.htmls";
        try {

            HttpThread ht = new HttpThread(url, deletemap) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("-------jsonstr------"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());
                        //只能在handler中更新视图
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new MaterialDialog(mcontext)
                                        .setTitle("警告")
                                        .autodismiss(2000)
                                        .setMessage(message)
                                        .show();
                                //更改文字提示
                                //更新list
                                list.remove(currentItem);
                                //发送删除请求
                                changed(1);
                                needtoshow = false;
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

    //获得所选id对应的序号
    private int getNumber(String ids) {
        for(int i = 0;i < list.size();i++){
            if(list.get(i).get("id").toString().equals(ids))
                return i;
        }
        return -1;
    }

    //返回数据的map集合
    public HashMap<String, String> getHashMap() {
        LogUtils.v("currentItemInt"+currentItemInt+"");
        hashMap = new HashMap<String,String>();

        //如果id有值，就存进去，如果没有，就不要，在教育背景页面只需要判断id是否有值就可以了

        if((list.get(currentItemInt).get("id").toString()!="0")){
            hashMap.put("id",list.get(currentItemInt).get("id").toString());
        }

        LogUtils.v("getHashMap-current_id"+Constant.getNoforSkill(list.get(currentItemInt).get("skillLevel").toString())+1+"");

        hashMap.put("skillName", list.get(currentItemInt).get("skillName").toString());
        hashMap.put("skillLevel", Constant.getNoforSkill(list.get(currentItemInt).get("skillLevel").toString())+1+"");

        return hashMap;
    }
    //改变了之后给主页面发消息，更改主页面的ui视图
    public void changed(int state){
        Message msg = new Message();
        msg.what = state;//-1表示有变动
        Commonhandler.comHandler.sendMessageDelayed(msg, 10);
    }
    public void changeState(boolean needtoshow){
        this.needtoshow = needtoshow;
    }

    public void setCurrentItemInt(int currentItemInt) {
        this.currentItemInt = currentItemInt;
    }

    public int getCurrentItemInt() {
        return currentItemInt;
    }

    public static boolean isNeedtoshow() {
        return needtoshow;
    }

    private void changedFilter(int count) {

        if(storagecount==0){
            changed(-1);
        }//只有第一次改变的时候会发送消息，之后都不会被发送
        storagecount++;
    }
}
