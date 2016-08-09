package com.xxw.student.fragment.group;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.fragment.contentFragment_quanzi;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * 发布帖子
 * Created by DarkReal on 2016/4/16.
 */
public class add_invitation extends Activity {
    private contentFragment_quanzi fquanzi ;
    private ImageView wen_send ;
    private EditText wen_title,wen_content;
    private ImageView pic;
    private TextView close_this;
    private LayoutInflater inflater;
    private int width,height;
    private HashMap<String,String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_invitation);


        FragmentManager fm = this.getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
//        fquanzi = (contentFragment_quanzi)fm.findFragmentById(R.id.main_content);
//        FragmentTransaction ft = fm.beginTransaction();
        wen_title = (EditText)findViewById(R.id.wen_title);
        wen_content = (EditText)findViewById(R.id.wen_content);
        wen_send = (ImageView)findViewById(R.id.wen_send);
//        fquanzi.initData();

        wen_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*List<Map<String,String>> list = a.getWdSet();*/
                LogUtils.v("wen_send click!");
                map = new HashMap<String,String>();
                SharedPreferences preferences = getSharedPreferences("userInfo",
                        Activity.MODE_PRIVATE);

                String username = preferences.getString("username", "");

                if(!check()) {
                    Toast.makeText(add_invitation.this, "请检查内容是否填写完毕", Toast.LENGTH_SHORT);
                }else{
                    map.put("username", username);//用户名
                    map.put("tiezi_title", wen_title.getText().toString());
                    map.put("content", wen_content.getText().toString());
                    map.put("tiezi_fenlei", "默认分类");
                    map.put("likecount","0");
                    map.put("comment","0");

                    LogUtils.v("tiezi"+map.values().toString());
                    String url= Constant.getUrl()+"tiezi/add_invitation.htmls";
                    try{

                        HttpThread ht = new HttpThread(url,map){
                            @Override
                            public void getObj(final JSONObject obj) throws JSONException {
                                if(obj!=null){
                                    final String message = obj.get("message").toString();
                                    LogUtils.v("message: "+obj.get("message").toString());
                                    getHandler.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (obj.get("code").toString().equals("-1"))
                                                    Toast.makeText(add_invitation.this, message, Toast.LENGTH_SHORT).show();
                                                else {
                                                    Toast.makeText(add_invitation.this, message, Toast.LENGTH_SHORT).show();
                                                    new Handler().postDelayed(new Runnable() {
                                                        public void run() {
                                                            Intent intent = new Intent();
                                                            intent.setClass(add_invitation.this, MainActivity.class);
                                                            startActivity(intent);

                                                        }

                                                    }, 1500);
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
                /*list.add(map);*/
                /*a.setWdSet(list);*/
                //放到对应的帖子对象里面去
                //finish();

            }
        });


        inflater = getLayoutInflater();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;
        close_this = (TextView) this.findViewById(R.id.close_this);
        pic = (ImageView) this.findViewById(R.id.pic);
        close_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            private mypop pop;
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
                        Uri mUri = Uri.fromFile(
                                new File(Environment.getExternalStorageDirectory() + "/Images/",
                                        "tiezipic" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
                        intent.putExtra("return-data", true);


                        startActivityForResult(intent, 1);


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
                pop = new mypop(contentView, 500, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                View rootview = inflater.inflate(R.layout.add_invitation, null);

                //view.invalidate();
                pop.setBackgroundDrawable(new ColorDrawable(00000000));
                pop.setAnimationStyle(R.style.popup_anim);

                pop.showAtLocation(rootview, Gravity.NO_GRAVITY,(width-500)/2,(height-40)/2);
                lp.alpha=0.6f;
                getWindow().setAttributes(lp);
            }
        });

    }
    public class mypop extends PopupWindow {
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        public mypop(View contentView, int width, int height, boolean focusable) {
            super(contentView,width,height,focusable);

        }

        @Override
        public void dismiss() {
            super.dismiss();
            lp.alpha = 1f;
            getWindow().setAttributes(lp);
        }
    }

    public boolean check(){
        if(wen_title.getText().toString().equals(""))
            return false;
        if(wen_content.getText().toString().equals(""))
            return false;
        LogUtils.v("wen_title"+wen_title.getText().toString());
        LogUtils.v("wen_content"+wen_content.getText().toString());
        return true;
    }
}
