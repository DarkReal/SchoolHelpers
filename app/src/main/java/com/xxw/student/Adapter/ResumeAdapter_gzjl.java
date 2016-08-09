package com.xxw.student.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
public class ResumeAdapter_gzjl extends BaseAdapter {

    //使用viewHolder提高效率
    public class TextViewHolder {
        //        工作经历
        TextView gzjl_job;
        TextView gzjl_company;
        TextView gzjl_begin;
        TextView gzjl_end;
        EditText gzjl_job_experience;
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
    private static int curr;

    private static HashMap<String,String> hashMap;//每次单击某一项的时候就组成一个Map,方便存储
    public static String currentItem = "";//标记当前选中项
    private int currentItemInt = -1;
    private static View mConvertView;
    private static boolean needtoshow = false;//默认不展示


    public ResumeAdapter_gzjl(Context context, Activity activity, List<HashMap<String, String>> list,
                              int layoutID, String flag[], int ItemIDs[]) {
        LogUtils.i("构造方法");
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

    private int count=0;
    public static int countEdit = 0;//计数记edit改变了几次
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LogUtils.v("执行了几次"+count++);

        mConvertView = convertView;
        TextViewHolder holder = null;
        if (convertView == null) {// 如果是第一次显示该页面(要记得保存到viewholder中供下次直接从缓存中调用)

            holder = new TextViewHolder();

            convertView = mInflater.inflate(layoutID, null);
            // 以下为保存这一屏的内容，供下次回到这一屏的时候直接refresh，而不用重读布局文件

            holder.gzjl_job = (TextView) convertView.findViewById(R.id.job);
            holder.gzjl_company = (TextView) convertView.findViewById(R.id.company);
            holder.gzjl_begin = (TextView) convertView.findViewById(R.id.gz_timeBegin);
            holder.gzjl_end = (TextView) convertView.findViewById(R.id.gz_timeEnd);
            holder.gzjl_job_experience = (EditText) convertView.findViewById(R.id.job_experience);
            holder.eachid = (TextView) convertView.findViewById(R.id.gzjl_list_id);
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

        //初始化数据

        for (int i = 1; i < flag.length; i++) {//备注1
            TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
            tv.setText((String) list.get(position).get(flag[i]));
        }

//            注册所有的点击事件

        EditText ed = (EditText) convertView.findViewById(ItemIDs[0]);
        ed.setText((String) list.get(position).get(flag[0]));
        //gzjl_textarea.add(ed);

        final TextViewHolder finalHolder = holder;
        finalHolder.gzjl_job_experience.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged-->"
                        + finalHolder.gzjl_job_experience.getText().toString() + "<--");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged-->"
                        + finalHolder.gzjl_job_experience.getText().toString() + "<--");
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged-->"
                        + finalHolder.gzjl_job_experience.getText().toString() + "<--");

                currentItem = finalHolder.eachid.getText().toString();
                LogUtils.v(currentItem + "gzjl_job");
                currentItemInt = getNumber(currentItem);
                LogUtils.v(currentItemInt + "currentItemInt");
                list.get(currentItemInt).put("workContext", finalHolder.gzjl_job_experience.getText().toString());
                changededittext(-1);

            }
        });

        holder.gzjl_job.setOnClickListener(new View.OnClickListener()

                                           {
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
                                                   et.setText(finalHolder.gzjl_job.getText().toString());

                                                   //提交内容,更改对应的文本框
                                                   submit.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if (!finalHolder.gzjl_job.getText().toString().equals(et.getText().toString())) {
                                                               LogUtils.v("原值：" + finalHolder.gzjl_job.getText().toString() + "修改后的值" + et.getText().toString());
                                                               finalHolder.gzjl_job.setText(et.getText().toString());
                                                               currentItem = finalHolder.eachid.getText().toString();
                                                               LogUtils.v(currentItem + "gzjl_job");
                                                               currentItemInt = getNumber(currentItem);

                                                               list.get(currentItemInt).put("position", et.getText().toString());

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

                                                   tv.setText("职位");
                                                   alertDialog.getWindow().setContentView(viewinflator);
                                                   alertDialog.getWindow().setLayout(400, 220);
                                               }
                                           }
        );


        holder.gzjl_company.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick (View v){
                                                       LayoutInflater inflater = LayoutInflater.from(mcontext);
                                                       View viewinflator = inflater.inflate(R.layout.custom_alertdialog_edit, null);

                                                       AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                                                       final AlertDialog alertDialog = builder.create();
                                                       alertDialog.show();

                                                       TextView tv = (TextView) viewinflator.findViewById(R.id.dialog_title);
                                                       final EditText et = (EditText) viewinflator.findViewById(R.id.content);
                                                       TextView submit = (TextView) viewinflator.findViewById(R.id.dialog_submit);
                                                       TextView cancel = (TextView) viewinflator.findViewById(R.id.dialog_cancel);
                                                       et.setText(finalHolder.gzjl_company.getText().toString());

                                                       //提交内容,更改对应的文本框
                                                       submit.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               if (!finalHolder.gzjl_company.getText().toString().equals(et.getText().toString())) {
                                                                   LogUtils.v("原值：" + finalHolder.gzjl_company.getText().toString() + "修改后的值" + et.getText().toString());
                                                                   finalHolder.gzjl_company.setText(et.getText().toString());
                                                                   currentItem = finalHolder.eachid.getText().toString();
                                                                   LogUtils.v(currentItem + "gzjl_company");
                                                                   currentItemInt = getNumber(currentItem);


                                                                   list.get(currentItemInt).put("companyName", et.getText().toString());

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

                                                       tv.setText("公司名称");
                                                       alertDialog.getWindow().setContentView(viewinflator);
                                                       alertDialog.getWindow().setLayout(400, 220);
                                                   }
                                               }

        );

        holder.gzjl_begin.setOnClickListener(new View.OnClickListener()

                                             {
                                                 @Override
                                                 public void onClick (View v){
                                                     datePicker = new DatePicker(activity, DatePicker.YEAR_MONTH);
                                                     datePicker.show();

                                                     datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                                                         @Override
                                                         public void onDatePicked(String year, String month) {
                                                             Toast.makeText(mcontext, year + "-" + month, Toast.LENGTH_SHORT).show();
                                                             if (!finalHolder.gzjl_begin.getText().toString().equals(year + "-" + month)) {
                                                                 LogUtils.v("原值：" + finalHolder.gzjl_end.getText().toString() + "修改后的值" + year + "-" + month);
                                                                 finalHolder.gzjl_begin.setText(year + "-" + month);
                                                                 currentItem = finalHolder.eachid.getText().toString();
                                                                 LogUtils.v(currentItem + "gzjl_begin");
                                                                 currentItemInt = getNumber(currentItem);
                                                                 list.get(currentItemInt).put("workDateBegin", year + "-" + month);
                                                                 changed(-1);
                                                             }
                                                         }
                                                     });
                                                 }
                                             }

        );

        holder.gzjl_end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                datePicker = new DatePicker(activity, DatePicker.YEAR_MONTH);
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(String year, String month) {
                        Toast.makeText(mcontext, year + "-" + month, Toast.LENGTH_SHORT).show();
                        if (!finalHolder.gzjl_end.getText().toString().equals(year + "-" + month)) {
                            LogUtils.v("原值：" + finalHolder.gzjl_end.getText().toString() + "修改后的值" + year + "-" + month);
                            finalHolder.gzjl_end.setText(year + "-" + month);
                            currentItem = finalHolder.eachid.getText().toString();
                            LogUtils.v(currentItem + "gzjl_begin");
                            currentItemInt = getNumber(currentItem);

                            list.get(currentItemInt).put("workDateEnd", year + "-" + month);
                            changededittext(-1);
                        }
                    }
                });
            }
        });
        //删除
        holder.del_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View viewinflator = inflater.inflate(R.layout.custom_alertdialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tv = (TextView) viewinflator.findViewById(R.id.title);
                tv.setText("确定删除？");
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

                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(300, 220);
            }
        });
        //初始化EditText的监听事件
        //init();
        return convertView;
    }

    private void changededittext(int count) {

        if(countEdit==0){
            changed(-1);
        }//只有第一次改变的时候会发送消息，之后都不会被发送
        countEdit++;
    }

    public static void setCountEdit(int countEdit) {
        ResumeAdapter_gzjl.countEdit = countEdit;
    }
    //删除条目

    private void delete(String ids) {
        HashMap<String,String> deletemap = new HashMap<String,String>();
        deletemap.put("token", MainActivity.token);
        deletemap.put("id", ids);
        deletemap.put("index", "1");
        Log.v("map", deletemap.values().toString());
        String url = Constant.getUrl() + "app/user/deleteResumeItem.htmls";
        try {

            HttpThread ht = new HttpThread(url, deletemap) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        Log.v("-------jsonstr------", obj.toString());
                        Log.v("message: ", obj.get("message").toString());
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

        hashMap.put("workContext", list.get(currentItemInt).get("workContext").toString());
        hashMap.put("position",list.get(currentItemInt).get("position").toString());
        hashMap.put("companyName", list.get(currentItemInt).get("companyName").toString());
        hashMap.put("workDateBegin", list.get(currentItemInt).get("workDateBegin").toString());
        hashMap.put("workDateEnd", list.get(currentItemInt).get("workDateEnd").toString());

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
