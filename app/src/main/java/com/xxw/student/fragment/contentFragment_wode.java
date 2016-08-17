package com.xxw.student.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.LoginActivity;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.wode_fragment.gerenziliaoFragment;
import com.xxw.student.fragment.wode_fragment.tongzhiFragment;
import com.xxw.student.fragment.wode_fragment.wodeFragment;
import com.xxw.student.fragment.wode_fragment.wodehuifuFragment;
import com.xxw.student.fragment.wode_fragment.wodejianliFragment;
import com.xxw.student.fragment.wode_fragment.wodejiluFragment;
import com.xxw.student.fragment.wode_fragment.wodeshoucangFragment;
import com.xxw.student.utils.CachePic;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.CircleTextImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * 我的
 * Created by xxw on 2016/3/27.
 */
public class contentFragment_wode extends Fragment implements View.OnClickListener,Animation.AnimationListener {
    private View rootView;
    private TextView username;
    private int width,height;
    private Context context;
    private BitmapUtils bitmapUtils;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.main_content_wode, container, false);
        context = rootView.getContext();
        bitmapUtils = new BitmapUtils(context);

        setDefault();
        username = (TextView) rootView.findViewById(R.id.username);
        username.setText(MainActivity.phone);

        if((MainActivity.topage!="")&&(MainActivity.topage!=null)){
            changeFragment(Integer.parseInt(MainActivity.topage));
            MainActivity.topage="";/*用完了记得要清空*/
        }
        return rootView;

    }

    private Fragment childFragment[] = new Fragment[7];
    private LinearLayout gerenxinxi,gerenziliao,wodejianli,wodehuifu,wodeshoucang,wodejilu,tongzhizhongxin;
    private CircleTextImageView profile_image;
    private LayoutInflater inflater;
    private popup pop;
    private LinearLayout logout;
    private HashMap<String,String> map;
    public void setDefault(){
        inflater = getActivity().getLayoutInflater();
        gerenziliao = (LinearLayout)rootView.findViewById(R.id.gerenziliao);
        gerenxinxi = (LinearLayout)rootView.findViewById(R.id.gerenxinxi);
        wodejianli = (LinearLayout)rootView.findViewById(R.id.wodejianli);
        wodehuifu = (LinearLayout)rootView.findViewById(R.id.wodehuifu);
        wodeshoucang = (LinearLayout)rootView.findViewById(R.id.wodeshoucang);
        wodejilu = (LinearLayout)rootView.findViewById(R.id.wodejilu);
        tongzhizhongxin = (LinearLayout)rootView.findViewById(R.id.tongzhizhongxin);
        profile_image = (CircleTextImageView) rootView.findViewById(R.id.profile_image);
        logout = (LinearLayout) rootView.findViewById(R.id.logout);

        //加载头像
        bitmapUtils.display(profile_image,Constant.getUrl() + "upload/media/images/" + MainActivity.headpic);

        gerenziliao.setOnClickListener(this);
        gerenxinxi.setOnClickListener(this);
        wodejianli.setOnClickListener(this);
        wodehuifu.setOnClickListener(this);
        wodeshoucang.setOnClickListener(this);
        wodejilu.setOnClickListener(this);
        tongzhizhongxin.setOnClickListener(this);
        logout.setOnClickListener(this);

        childFragment[0] = new wodeFragment();
        childFragment[1] = new gerenziliaoFragment();
        childFragment[2] = new wodejianliFragment();
        childFragment[3] = new wodehuifuFragment();
        childFragment[4] = new wodeshoucangFragment();
        childFragment[5] = new wodejiluFragment();
        childFragment[6] = new tongzhiFragment();

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
        profile_image.setOnClickListener(new View.OnClickListener() {
            final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            @Override
            public void onClick(View v) {
                View contentView = inflater.inflate(R.layout.custom_get_pic, null);

                TableRow take_pic_row = (TableRow) contentView.findViewById(R.id.take_pic_row);
                TableRow get_pic_row = (TableRow) contentView.findViewById(R.id.get_pic_row);
                //拍摄
                take_pic_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        File file = new File(Environment.getExternalStorageDirectory() + "/Images");
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        MainActivity.headpic ="txpic" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                        Uri mUri = Uri.fromFile(
                                new File(Environment.getExternalStorageDirectory() + "/Images/",
                                        MainActivity.headpic));
                        //制定拍照后的存储路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                        intent.putExtra("return-data", true);
                        intent.putExtra("autofocus", true); // 自动对焦
                        intent.putExtra("fullScreen", false); // 全屏
                        intent.putExtra("showActionIcons", false);

                        startActivityForResult(intent, 1);
                        //跳转到确认图片那里
                    }
                });
                //获取相册
                get_pic_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");//相片类型
                        startActivityForResult(intent, 0);

                    }
                });
                pop = new popup(contentView, 500, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                View rootview = inflater.inflate(R.layout.add_invitation, null);

                //view.invalidate();
                pop.setBackgroundDrawable(new ColorDrawable(00000000));
                pop.setAnimationStyle(R.style.popup_anim);

                pop.showAtLocation(rootview, Gravity.NO_GRAVITY,(width-500)/2,(height-40)/2);
                lp.alpha=0.6f;
                getActivity().getWindow().setAttributes(lp);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gerenxinxi :
                changeFragment(0);
                break;
            case R.id.gerenziliao :
                //getStudentInfo();
                changeFragment(1);
                break;
            case R.id.wodejianli :
                changeFragment(2);
                break;
            case R.id.wodehuifu :
                changeFragment(3);
                break;
            case R.id.wodeshoucang :
                changeFragment(4);
                break;
            case R.id.wodejilu :
                changeFragment(5);
                break;
            case R.id.tongzhizhongxin :
                changeFragment(6);
                break;
            case R.id.logout:

                logout();
                break;
        }
    }
    //用户注销
    private void logout() {
        map = new HashMap<String,String>();
        map.put("phone", MainActivity.phone);
        map.put("token", MainActivity.token);
        LogUtils.v(map.values().toString());
        String url = Constant.getUrl() + "app/user/logOut.htmls";
        try {
            HttpThread ht = new HttpThread(url,map){

                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("---jsonstr----"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());
                        final String code = obj.getString("code").toString();
//                        Toast.makeText(rootView.getContext(), "code:"+obj.get("code").toString(), Toast.LENGTH_SHORT).show();

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!code.equals("10000")) {
                                    Toast.makeText(rootView.getContext(), message, Toast.LENGTH_SHORT).show();
                                    if(code.equals("10003")){
                                        updateStatus();
                                        Intent intent = new Intent();
                                        intent.setClass(rootView.getContext(), LoginActivity.class);
                                        //保存登录状态
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }
                                else {
                                    Toast.makeText(rootView.getContext(), "返回内容：" + message, Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            updateStatus();
                                            Intent intent = new Intent();
                                            intent.setClass(rootView.getContext(), LoginActivity.class);
                                            //保存登录状态
                                            startActivity(intent);
                                            getActivity().finish();
                                        }

                                    }, 1000);
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

    private void updateStatus() {

        SharedPreferences sharedPreferences = rootView.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        //根据mobile获取到username然后存到sharedpreference中
        try {
            editor.putString("phone", "");
            editor.putString("token", "");
            MainActivity.emptyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();

    }


    public void changeFragment(int index){
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fillContent, childFragment[index]);
        ft.commit();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    //自定义弹出框，选择图片
    public class popup extends PopupWindow {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

        public popup(View contentView, int width, int height, boolean focusable) {
            super(contentView,width,height,focusable);

        }

        @Override
        public void dismiss() {
            super.dismiss();
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://拍照
                if (resultCode == -1) {
                    LogUtils.v("requestCode: "+requestCode+" resultCode: "+resultCode+" intentdata: "+data);
                    LogUtils.v("拍照"+"照相完成");
                    Intent intent = new Intent();
                    intent.setClass(rootView.getContext(),CachePic.class);
                    intent.putExtra("picname", MainActivity.headpic);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }
}
