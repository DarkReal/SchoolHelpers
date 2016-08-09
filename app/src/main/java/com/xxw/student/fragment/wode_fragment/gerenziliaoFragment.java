package com.xxw.student.fragment.wode_fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.framework.picker.DatePicker;
import com.xxw.student.view.framework.picker.OptionPicker;

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
        root_gerenziliao = (RelativeLayout)rootView.findViewById(R.id.root_gerenziliao);
        root_gerenziliao.setOnTouchListener(this);
        root_gerenziliao.setLongClickable(true);


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
                                    if (!obj.get("code").toString().equals("10000"))
                                        Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
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
                                            case "3":
                                                education.setText(edu_pos);
                                                editor.putString("education",edu_pos);
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

