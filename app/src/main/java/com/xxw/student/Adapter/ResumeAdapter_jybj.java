package com.xxw.student.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
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
public class ResumeAdapter_jybj extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        教育背景
        TextView education;//学历
        TextView timeBegin;//开始时间
        TextView timeEnd;//终止时间
        TextView school;//学校
        TextView majorIn;//专业
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
    private String TAG = "ResumeAdapter_jybj";
    private static View mConvertView;
    private static boolean needtoshow = false;//默认不展示
    private Message msg;


    public ResumeAdapter_jybj(Context context, Activity activity, List<HashMap<String, String>> list,
                              int layoutID, String flag[], int ItemIDs[]) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
        this.flag = flag;
        this.ItemIDs = ItemIDs;
        this.activity = activity;
        this.mcontext = context;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        mConvertView = convertView;
        TextViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            holder = new TextViewHolder();

            convertView = mInflater.inflate(layoutID, null);
            // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件

            holder.education = (TextView) convertView.findViewById(R.id.education);
            holder.majorIn = (TextView) convertView.findViewById(R.id.majorIn);
            holder.school = (TextView) convertView.findViewById(R.id.school);
            holder.timeBegin = (TextView) convertView.findViewById(R.id.timeBegin);
            holder.timeEnd = (TextView) convertView.findViewById(R.id.timeEnd);
            holder.eachid = (TextView) convertView.findViewById(R.id.jybj_list_id);
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
        }
//            教育经历
        final TextViewHolder finalHolder = holder;
        holder.education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.setXueli_list();
                optionPicker = new OptionPicker(activity, Constant.xueli_list);
                optionPicker.show();
                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        Toast.makeText(mcontext, Constant.xueli_list.get(position) + "in_ResumeAdpater", Toast.LENGTH_SHORT).show();
                        TextView textView = finalHolder.education;
                        if(!finalHolder.education.getText().toString().equals(Constant.xueli_list.get(position).toString())){
                            LogUtils.v("原值：" + finalHolder.education.getText().toString() + "修改后的值" + Constant.xueli_list.get(position));
                            finalHolder.education.setText(Constant.xueli_list.get(position));
                            currentItem = finalHolder.eachid.getText().toString();
                            LogUtils.v(currentItem+"education");
                            currentItemInt = getNumber(currentItem);
                            list.get(currentItemInt).put("education", Constant.xueli_list.get(position));
                            changed(-1);
                        }
                    }
                });
            }
        });

        holder.timeBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePicker(activity, DatePicker.YEAR_MONTH);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        Toast.makeText(mcontext, year + "-" + month, Toast.LENGTH_SHORT).show();
                        if (!finalHolder.timeBegin.getText().toString().equals(year + "-" + month)) {
                            LogUtils.v("原值：" + finalHolder.timeBegin.getText().toString() + "修改后的值" + year + "-" + month);
                            finalHolder.timeBegin.setText(year + "-" + month);
                            currentItem = finalHolder.eachid.getText().toString();
                            LogUtils.v(currentItem + "timeBegin");
                            currentItemInt = getNumber(currentItem);
                            list.get(currentItemInt).put("timeBegin", year + "-" + month);
                            changed(-1);
                        }
                    }
                });
            }
        });

        holder.timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePicker(activity, DatePicker.YEAR_MONTH);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        Toast.makeText(mcontext, year + "-" + month, Toast.LENGTH_SHORT).show();
                        if (!finalHolder.timeEnd.getText().toString().equals(year + "-" + month)) {
                            LogUtils.v("原值：" + finalHolder.timeEnd.getText().toString() + "修改后的值" + year + "-" + month);
                            finalHolder.timeEnd.setText(year + "-" + month);
                            currentItem = finalHolder.eachid.getText().toString();
                            LogUtils.v(currentItem + "timeEnd");
                            currentItemInt = getNumber(currentItem);
                            list.get(currentItemInt).put("timeEnd", year + "-" + month);
                            changed(-1);
                        }
                    }
                });
            }
        });

        holder.school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View viewinflator = inflater.inflate(R.layout.custom_alertdialog_edit, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tv = (TextView) viewinflator.findViewById(R.id.dialog_title);
                final EditText et = (EditText) viewinflator.findViewById(R.id.content);
                TextView submit = (TextView) viewinflator.findViewById(R.id.dialog_submit);
                TextView cancel = (TextView) viewinflator.findViewById(R.id.dialog_cancel);
                et.setText(finalHolder.school.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!finalHolder.school.getText().toString().equals(et.getText().toString())) {
                            LogUtils.v("原值：" + finalHolder.school.getText().toString() + "修改后的值" + et.getText().toString());
                            finalHolder.school.setText(et.getText().toString());
                            currentItem = finalHolder.eachid.getText().toString();
                            LogUtils.v(currentItem + "school");
                            currentItemInt = getNumber(currentItem);
                            list.get(currentItemInt).put("school", et.getText().toString());
                            changed(-1);
                        }
                        alertDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("学校名称");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(400, 220);

            }
        });

        holder.majorIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View viewinflator = inflater.inflate(R.layout.custom_alertdialog_edit, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tv = (TextView) viewinflator.findViewById(R.id.dialog_title);
                final EditText et = (EditText) viewinflator.findViewById(R.id.content);
                TextView submit = (TextView) viewinflator.findViewById(R.id.dialog_submit);
                TextView cancel = (TextView) viewinflator.findViewById(R.id.dialog_cancel);
                et.setText(finalHolder.majorIn.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!finalHolder.majorIn.getText().toString().equals(et.getText().toString())) {
                            //如果修改了值，就做一个标记
                            LogUtils.v("原值：" + finalHolder.majorIn.getText().toString() + "修改后的值" + et.getText().toString());
                            finalHolder.majorIn.setText(et.getText().toString());
                            currentItem = finalHolder.eachid.getText().toString();
                            currentItemInt = getNumber(currentItem);
                            list.get(currentItemInt).put("majorIn", et.getText().toString());
                            //发送消息
                            changed(-1);
                        }
                        alertDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("专业名称");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(400, 220);

            }
        });

        //删除简历条目
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


        return convertView;
    }
    //删除条目
    private void delete(String ids) {
        HashMap<String,String> deletemap = new HashMap<String,String>();
        deletemap.put("token", MainActivity.token);
        deletemap.put("id", ids);
        deletemap.put("index", "4");
        LogUtils.v(deletemap.values().toString());
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
                                Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
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
        LogUtils.v("currentItemInt"+currentItemInt);
        hashMap = new HashMap<String,String>();

        //如果id有值，就存进去，如果没有，就不要，在教育背景页面只需要判断id是否有值就可以了

        if((list.get(currentItemInt).get("id").toString()!="0")){
            hashMap.put("id",list.get(currentItemInt).get("id").toString());
        }

        LogUtils.v("getHashMap-current_id"+currentItem);

        hashMap.put("school", list.get(currentItemInt).get("school").toString());
        hashMap.put("majorIn",list.get(currentItemInt).get("majorIn").toString());
        hashMap.put("education", list.get(currentItemInt).get("education").toString());
        hashMap.put("timeBegin", list.get(currentItemInt).get("timeBegin").toString());
        hashMap.put("timeEnd", list.get(currentItemInt).get("timeEnd").toString());

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


}
