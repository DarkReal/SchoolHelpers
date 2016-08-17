package com.xxw.student.fragment.wode_fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.LoginActivity;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.framework.picker.DatePicker;
import com.xxw.student.view.framework.picker.OptionPicker;
import com.xxw.student.view.sweetdialog.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 我的--个人资料
 * Created by xxw on 2016/4/9.
 * update by darkreal on 2016/7/18
 */
public class gerenziliaoFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private String mobile;

    private OptionPicker optionPicker;
    private DatePicker datePicker;
    private String TAG = "--------gerenziliaoFragment--------";
    private String url = Constant.getUrl() + "app/user/updateUserInfo.htmls";

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_gerenziliao,container,false);

        setDefault();
        updateView();
        return rootView;
    }

    private void checktj() {
        realnamestr = realname.getText().toString().toLowerCase();
        genderstr=gender.getText().toString().toLowerCase();
        birthstr=birth.getText().toString().toLowerCase();
        citystr=city.getText().toString().toLowerCase();
        educationstr=education.getText().toString().toLowerCase();
        univercitystr=univercity.getText().toString().toLowerCase();
        majorInstr=majorIn.getText().toString().toLowerCase();
        phonestr=phone.getText().toString().toLowerCase();
        emailstr=email.getText().toString().toLowerCase();

        //性别选择
        map = new HashMap<String,String>();
        map.put("username",realnamestr);
        map.put("sex",genderstr);
        map.put("birthday",birthstr);
        map.put("city",citystr);
        map.put("xueli",educationstr);
        map.put("school",univercitystr);
        map.put("major",majorInstr);
        map.put("mobile",phonestr);
        map.put("email", emailstr);
        LogUtils.v(map.toString());
    }

    private GestureDetector mGestureDetector;
    private EditText realname,gender,birth,city,education,univercity,majorIn,phone,email;
    private String realnamestr,genderstr,birthstr,citystr,educationstr,univercitystr,majorInstr,phonestr,emailstr;
    private ImageView back;
    private RelativeLayout root_gerenziliao;
    private HashMap<String,String> map;
    private String brith,gender_pos,edu_pos;


    public void setDefault(){
        realname = (EditText)rootView.findViewById(R.id.realname);
        gender = (EditText) rootView.findViewById(R.id.sex);
        birth = (EditText) rootView.findViewById(R.id.birth);
        city = (EditText) rootView.findViewById(R.id.location);
        education = (EditText) rootView.findViewById(R.id.xueli);
        univercity = (EditText) rootView.findViewById(R.id.byxx);
        majorIn = (EditText) rootView.findViewById(R.id.major);
        phone = (EditText)rootView.findViewById(R.id.telnumber);
        email = (EditText)rootView.findViewById(R.id.email);

        gender.setOnClickListener(this);
        birth.setOnClickListener(this);
        education.setOnClickListener(this);
        back = (ImageView)rootView.findViewById(R.id.backTowode_gerenziliao);
        back.setOnClickListener(this);
        city.setOnClickListener(this);
        root_gerenziliao = (RelativeLayout)rootView.findViewById(R.id.root_gerenziliao);
        root_gerenziliao.setOnTouchListener(this);
        root_gerenziliao.setLongClickable(true);

        univercity.setOnClickListener(this);
        majorIn.setOnClickListener(this);
        phone.setOnClickListener(this);
        email.setOnClickListener(this);

        mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener) this);
        super.setmGestureDetector(mGestureDetector);

    }

    private void updateView() {

        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);


        if((realnamestr = preferences.getString("realname","")).equals("null")){
            realnamestr="未填写";
        }
        if((genderstr=preferences.getString("gender", "")).equals("null")){
            genderstr="未填写";
        }
        if((birthstr=preferences.getString("birth", "")).equals("null")){
            birthstr="未填写";
        }
        if((citystr=preferences.getString("city", "")).equals("null")){
            citystr="未填写";
        }
        if((educationstr=preferences.getString("education", "")).equals("null")){
            educationstr="未填写";
        }
        if((univercitystr=preferences.getString("univercity", "")).equals("null")){
            univercitystr="未填写";
        }
        if((majorInstr=preferences.getString("majorIn", "")).equals("null")){
            majorInstr="未填写";
        }
        if((phonestr=preferences.getString("phone", "")).equals("null")){
            phonestr="未填写";
        }
        if((emailstr=preferences.getString("email","")).equals("null")){
            emailstr="未填写";
        }

        Toast.makeText(rootView.getContext(), "当前用户:" + phonestr, Toast.LENGTH_SHORT).show();

        realname.setText(realnamestr);
        gender.setText(genderstr);
        birth.setText(birthstr);
        city.setText(citystr);
        education.setText(educationstr);
        univercity.setText(univercitystr);
        majorIn.setText(majorInstr);
        phone.setText(phonestr);
        email.setText(emailstr);

    }

    //弹出pickerview来供选择
    @Override
    public void onClick(View v) {
        map = new HashMap<String,String>();//初始化

        map.put("token", MainActivity.token);
        map.put("phone", MainActivity.phone);

        LayoutInflater inflater = LayoutInflater.from(rootView.getContext());
        View viewinflator = inflater.inflate(R.layout.custom_alertdialog_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        final AlertDialog alertDialog = builder.create();
        TextView tv = (TextView) viewinflator.findViewById(R.id.dialog_title);
        final EditText et = (EditText) viewinflator.findViewById(R.id.content);
        TextView submit = (TextView) viewinflator.findViewById(R.id.dialog_submit);
        TextView cancel = (TextView) viewinflator.findViewById(R.id.dialog_cancel);


        switch (v.getId()) {
            //修改性别
            case R.id.sex:
                Constant.setGender_list();//初始化性别列表
                optionPicker = new OptionPicker(getActivity(), Constant.getGender_list());
                optionPicker.show();
                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(final int position, String option) {

                        gender_pos = Constant.getGender_list().get(position);
                        /*性别*/
                        map.put("index", "0");
                        map.put("gender", gender_pos);
                        LogUtils.v(map.values().toString());
                        update();
                    }
                });
                break;
            //修改出生日期
            case R.id.birth:
                datePicker = new DatePicker(getActivity());
                datePicker.show();

                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        brith = year + "-" + month + "-" + day;
                        Toast.makeText(rootView.getContext(), brith, Toast.LENGTH_SHORT).show();
                        //更新出生日期
                        map.put("index", "1");
                        map.put("birth", brith);
                        LogUtils.v(map.values().toString());
                    }
                });
                datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        update();
                    }
                });
                break;
            case R.id.xueli:
                //初始化
                Constant.setXueli_list();
                optionPicker = new OptionPicker(getActivity(), Constant.xueli_list);
                optionPicker.show();
                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        Toast.makeText(rootView.getContext(), Constant.xueli_list.get(position), Toast.LENGTH_SHORT).show();
                        edu_pos = Constant.xueli_list.get(position);
                        /*性别*/
                        map.put("index", "3");
                        map.put("education", edu_pos);
                        LogUtils.v(map.values().toString());
                        update();
                    }
                });
                break;
            case R.id.backTowode_gerenziliao:
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                ft.replace(R.id.fillContent, new Fragment());
                ft.commit();
                break;
            case R.id.location:

                alertDialog.show();


                et.setText(city.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        city.setText(et.getText().toString());
                        alertDialog.cancel();
                        map.put("index","2");
                        map.put("city",city.getText().toString());
                        update();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("城市名称");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(400, 220);
                break;

            case R.id.byxx:
                alertDialog.show();
                et.setText(univercity.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        univercity.setText(et.getText().toString());
                        alertDialog.cancel();
                        map.put("index","4");
                        map.put("university",univercity.getText().toString());
                        update();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("毕业院校");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(400, 220);
                break;
            case R.id.major:
                alertDialog.show();
                et.setText(majorIn.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        majorIn.setText(et.getText().toString());
                        alertDialog.cancel();
                        map.put("index","5");
                        map.put("majorIn",majorIn.getText().toString());
                        update();
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
                break;
//            case R.id.telnumber:
//                alertDialog.show();
//                et.setText(phone.getText().toString());
//
//                //提交内容,更改对应的文本框
//                submit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        phone.setText(et.getText().toString());
//                        alertDialog.cancel();
//                        map.put("index","4");
//                        map.put("city",phone.getText().toString());
//                        update();
//                    }
//                });
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.cancel();
//                    }
//                });
//
//                tv.setText("电话号码");
//                alertDialog.getWindow().setContentView(viewinflator);
//                alertDialog.getWindow().setLayout(400, 220);
//                break;
            case R.id.email:
                alertDialog.show();
                et.setText(email.getText().toString());

                //提交内容,更改对应的文本框
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email.setText(et.getText().toString());
                        alertDialog.cancel();
                        map.put("index", "6");
                        map.put("email",email.getText().toString());
                        update();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

                tv.setText("邮箱");
                alertDialog.getWindow().setContentView(viewinflator);
                alertDialog.getWindow().setLayout(400, 220);
                break;

        }

    }

    private void update() {
        SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            HttpThread ht = new HttpThread(url, map) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("-------jsonstr------" + obj.toString());
                        LogUtils.v("message: " + obj.get("message").toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000")){
                                        switch (obj.get("code").toString()){
                                            case "10006":
                                                Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                                new SweetAlertDialog(rootView.getContext(), SweetAlertDialog.ERROR_TYPE)
                                                        .setTitleText("警告")
                                                        .setContentText("性别不能二次修改!")
                                                        .show();
                                                break;
                                        }
                                    }else {
                                        Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();

                                        switch (map.get("index").toString()) {
                                            case "0"://性别
                                                gender.setText(gender_pos);
                                                editor.putString("gender", gender_pos);
                                                break;

                                            case "1"://出生年月
                                                birth.setText(brith);
                                                editor.putString("birth", brith);
                                                break;

                                            case "2":
                                                editor.putString("city",city.getText().toString());
                                                break;

                                            case "3":
                                                education.setText(edu_pos);
                                                editor.putString("education", edu_pos);
                                                break;

                                            case "4":
                                                editor.putString("university",univercity.getText().toString());
                                                break;

                                            case "5":
                                                editor.putString("majorIn",majorIn.getText().toString());
                                                break;

                                            case "6":
                                                editor.putString("email",email.getText().toString());
                                                break;
                                        }
                                        editor.commit();
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
        }
    }
}

