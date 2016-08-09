package com.xxw.student.fragment.wode_fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.edit_frag.gzjl_frag;
import com.xxw.student.fragment.wode_fragment.edit_frag.hjjl_frag;
import com.xxw.student.fragment.wode_fragment.edit_frag.jnjj_frag;
import com.xxw.student.fragment.wode_fragment.edit_frag.jybj_frag;
import com.xxw.student.fragment.wode_fragment.edit_frag.qwgz_frag;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.ValidateHelper;
import com.xxw.student.utils.getHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 我的-我的简历
 * Created by xxw on 2016/4/9.
 */
public class wodejianliFragment extends BaseFragment implements View.OnClickListener{
    private View rootView;
    private TextView jybj_edit,qwgz_edit,jnjj_edit,gzjl_edit,hjjl_edit;
    private HashMap<String,String> map;
    private static JSONObject json;
    public static JSONArray listEdu,listExperience,listRewarded,listSkill,listJob;
    private TextView username,gender_age_education,phone_email;
    private ListView jybj_list,qwgz_list,jnjj_list,gzjl_list,hjjl_list;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.wode_content_wodejianli,container,false);
        init();
        getJianli();
        return rootView;
    }

    private ImageView back;
    private LinearLayout root_jianli;
    private GestureDetector mGestureDetector;
    public void init(){
        back = (ImageView)rootView.findViewById(R.id.backTowode_wodejianli);
        jybj_edit = (TextView) rootView.findViewById(R.id.jybj_edit);
        qwgz_edit = (TextView) rootView.findViewById(R.id.qwgz_edit);
        jnjj_edit = (TextView) rootView.findViewById(R.id.jnjj_edit);
        gzjl_edit = (TextView) rootView.findViewById(R.id.gzjl_edit);
        hjjl_edit = (TextView) rootView.findViewById(R.id.hjjl_edit);
        username  = (TextView) rootView.findViewById(R.id.username);
        gender_age_education  = (TextView) rootView.findViewById(R.id.gender_age_education);
        phone_email  = (TextView) rootView.findViewById(R.id.phone_email);


        jybj_edit.setOnClickListener(this);
        qwgz_edit.setOnClickListener(this);
        jnjj_edit.setOnClickListener(this);
        gzjl_edit.setOnClickListener(this);
        hjjl_edit.setOnClickListener(this);

        back.setOnClickListener(this);

        root_jianli = (LinearLayout)rootView.findViewById(R.id.root_jianli);
        root_jianli.setOnTouchListener(this);
        root_jianli.setLongClickable(true);

        getUserInfo();
        mGestureDetector  = new GestureDetector((GestureDetector.OnGestureListener)this);
    }

    private void getUserInfo(){
        SharedPreferences preferences = getActivity().getSharedPreferences("userInfo",
                Activity.MODE_PRIVATE);

            username.setText(ValidateHelper.isEmptyString(preferences.getString("realname", "")) ? "用户名" : preferences.getString("realname", ""));

        String gender = preferences.getString("gender", "");
        String birth = preferences.getString("birth", "");

        String education = preferences.getString("education", "");

        String phone = preferences.getString("phone", "");
        String email = preferences.getString("email", "");


            String txt1="",txt2="";

            ArrayList<String> arr1 = new ArrayList<String>();
            ArrayList<String> arr2 = new ArrayList<String>();

            if(!ValidateHelper.isEmptyString(gender))
                arr1.add(gender);
            if(!ValidateHelper.isEmptyString(birth)){
                int age = Constant.calculateAge(birth);//计算出来的年龄
                arr1.add(age+"");
            }
            if(!ValidateHelper.isEmptyString(education))
                arr1.add(education);


            if(ValidateHelper.isEmptyString(phone))
                arr2.add(phone);
            if(ValidateHelper.isEmptyString(email))
                arr2.add(email);


            //组成字符串

            if(arr1.size()>1) {
                txt1 = arr1.get(0);
                for (int i = 1; i <= arr1.size() - 1; i++) {
                    txt1 += " | " + arr1.get(i);
                }
            }else if(arr1.size()==1){
                txt1 =arr1.get(0);
            }

            if(arr2.size()>1) {
                txt2 = arr2.get(0);
                for (int i = 1; i <= arr2.size() - 1; i++) {
                    txt2 += " | " + arr2.get(i);
                }
            }else if(arr2.size()==1){
                txt2 = arr2.get(0);
            }

            gender_age_education.setText(txt1);
            phone_email.setText(txt2);


    }


    //加载简历所有的内容

    public void getJianli() {
        map = new HashMap<String,String>();
        map.put("token", MainActivity.token);
        String url = Constant.getUrl()+"app/user/loadResume.htmls";
        try {
            HttpThread ht = new HttpThread(url, map) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("-------jsonstr------"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                        json = obj.getJSONObject("object");
                                        listExperience= json.getJSONArray("listExperience");
                                        listSkill= json.getJSONArray("listSkill");
                                        listRewarded= json.getJSONArray("listRewarded");
                                        listJob= json.getJSONArray("listJob");
                                        listEdu= json.getJSONArray("listEdu");

                                        showResume();//展示简历,把获取到的内容分配到每一个ListView内部
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
    public boolean onTouch(View v,MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }

    //把简历的内容展现在页面的每一部分的Listview中间
    private void showResume() {
        TextView jybj_none = (TextView) rootView.findViewById(R.id.jybj_none);
        TextView qwgz_none = (TextView) rootView.findViewById(R.id.qwgz_none);
        TextView jnjj_none = (TextView) rootView.findViewById(R.id.jnjj_none);
        TextView gzjl_none = (TextView) rootView.findViewById(R.id.gzjl_none);
        TextView hjjl_none = (TextView) rootView.findViewById(R.id.hjjl_none);

        try{
            if(listEdu.length()!=0){
                jybj_none.setVisibility(View.GONE);
                TextView tv1 = (TextView) rootView.findViewById(R.id.jybj_line1);
                TextView tv2 = (TextView) rootView.findViewById(R.id.jybj_line2);

                JSONObject json = (JSONObject) listEdu.get(0);
                tv1.setText(json.get("timeBegin").toString()+" "+json.get("timeEnd").toString());
                tv2.setText(json.get("education").toString()+" "+json.get("school").toString()+" "+json.get("majorIn").toString());
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
            }
            if(listJob.length()!=0){
                qwgz_none.setVisibility(View.GONE);
                TextView tv1 = (TextView) rootView.findViewById(R.id.qwgz_line1);
                TextView tv2 = (TextView) rootView.findViewById(R.id.qwgz_line2);

                JSONObject json = (JSONObject) listJob.get(0);
                tv1.setText(json.get("city").toString()+" "+json.get("position").toString());
                tv1.setVisibility(View.VISIBLE);
                if(listJob.length()>1){
                    json = (JSONObject) listJob.get(1);
                    tv2.setText(json.get("city").toString()+" "+json.get("position").toString());
                    tv2.setVisibility(View.VISIBLE);
                }
            }
            if(listSkill.length()!=0){
                jnjj_none.setVisibility(View.GONE);
                TextView tv1 = (TextView) rootView.findViewById(R.id.jnjj_line1);
                TextView tv2 = (TextView) rootView.findViewById(R.id.jnjj_line2);

                JSONObject json = (JSONObject) listSkill.get(0);
                Constant.setSkill_list();
                tv1.setText(json.get("skillName").toString() + " " + Constant.skill_list.get(Integer.parseInt(json.get("skillLevel").toString()) - 1));
                tv1.setVisibility(View.VISIBLE);
                if(listSkill.length()>1){
                    json = (JSONObject) listSkill.get(1);
                    tv2.setText(json.get("skillName").toString() + " " + Constant.skill_list.get(Integer.parseInt(json.get("skillLevel").toString()) - 1));
                    tv2.setVisibility(View.VISIBLE);
                }
            }
            if(listExperience.length()!=0){
                gzjl_none.setVisibility(View.GONE);
                TextView tv1 = (TextView) rootView.findViewById(R.id.gzjl_line1);
                TextView tv2 = (TextView) rootView.findViewById(R.id.gzjl_line2);

                JSONObject json = (JSONObject) listExperience.get(0);
                tv1.setText(json.get("workDateBegin").toString()+" "+json.get("workDateEnd").toString());
                tv2.setText(json.get("position").toString()+" "+json.get("companyName").toString());
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
            }
            if(listRewarded.length()!=0){
                hjjl_none.setVisibility(View.GONE);
                TextView tv1 = (TextView) rootView.findViewById(R.id.hjjl_line1);
                TextView tv2 = (TextView) rootView.findViewById(R.id.hjjl_line2);

                JSONObject json = (JSONObject) listRewarded.get(0);
                tv1.setText(json.get("rewardedName").toString());
                tv1.setVisibility(View.VISIBLE);
                if(listRewarded.length()>1){
                    tv1.setText(json.get("rewardedName").toString());
                    tv1.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.jybj_edit:
                toActivity(0);
                break;
            case R.id.qwgz_edit:
                toActivity(1);
                break;
            case R.id.jnjj_edit:
                toActivity(2);
                break;
            case R.id.gzjl_edit:
                toActivity(3);
                break;
            case R.id.hjjl_edit:
                toActivity(4);
                break;
            case R.id.backTowode_wodejianli:
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                ft.replace(R.id.fillContent, new Fragment());/*把当前的fragment替换fillcontentid的部分*/
                ft.commit();
                break;
        }

    }

    public void toActivity(int i){
        Intent intent = new Intent();
        switch (i){
            case 0:
                intent.setClass(rootView.getContext(), jybj_frag.class);
                break;
            case 1:
                intent.setClass(rootView.getContext(), qwgz_frag.class);
                break;
            case 2:
                intent.setClass(rootView.getContext(), jnjj_frag.class);
                break;
            case 3:
                intent.setClass(rootView.getContext(), gzjl_frag.class);
                break;
            case 4:
                intent.setClass(rootView.getContext(), hjjl_frag.class);
                break;
        }
        startActivity(intent);
    }

    //页面重新恢复活跃状态的时候调用这个方法
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.v("onresume");
        getJianli();
    }
}
