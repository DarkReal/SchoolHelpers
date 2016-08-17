package com.xxw.student.fragment.group;

import android.app.Activity;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.List;

/**
 * 发布帖子
 * Created by DarkReal on 2016/4/16.
 */
public class add_invitation extends Activity {
    private contentFragment_quanzi fquanzi ;
    private Button wen_send ;
    private EditText wen_title,wen_content;
    private ImageView pic;
    private TextView close_this;
    private LayoutInflater inflater;
    private HashMap<String,String> map;
    private LinearLayout select_group;
    private LayoutInflater mInflater;
    private List<String> lists;//帖子分类
    private TextView all_group;
    //自定义适配器
    private MyAdapter mAdapter;
    //PopupWindow
    private my_pop pop;
    //是否显示PopupWindow，默认不显示
    private boolean isPopShow = false;
    private View view;
    private int width,height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_invitation);

        //获取屏幕的宽度和高度
        inflater = getLayoutInflater();
        //根视图
        final View rootview = inflater.inflate(R.layout.add_invitation, null);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;     // 屏幕宽度（像素）
        height = metric.heightPixels;

        mAdapter = new MyAdapter();

        Constant.setGroup_type();
        lists = Constant.getGroup_type();
        all_group = (TextView) findViewById(R.id.all_group);

        FragmentManager fm = this.getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        wen_title = (EditText)findViewById(R.id.wen_title);
        wen_content = (EditText)findViewById(R.id.wen_content);
        wen_send = (Button)findViewById(R.id.wen_send);

        select_group=(LinearLayout)findViewById(R.id.select_group);
        //弹出选择分类
        select_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LogUtils.v("click!!");
                // TODO Auto-generated method stub
                if(pop == null){//popupwindow展示出来的时候
                    //初始化透明度
                    LogUtils.v("popcreate");
                    //初始化popupwindow
                    ListView listView = new ListView(add_invitation.this);
                    listView.setCacheColorHint(0xFFFFFFFF);
                    listView.setAdapter(mAdapter);
                    pop = new my_pop(listView, select_group.getWidth()+70, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    pop.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
                    pop.setFocusable(true);
                    isPopShow = true;

                }
                if(isPopShow){
                    //展示出来的时候，再点击，那么
                    LogUtils.v("pop出来啦");
                    //进行高度的测量
                    pop.showUp(select_group);
                    isPopShow = false;
                    //点其他地方pop消失
                    rootview.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (pop.isShowing())
                            {
                                LogUtils.v("-------------------onTouch------------");
                                pop.dismiss();
                            }
                            return false;
                        }
                    });
                }
            }
        });

        mInflater = LayoutInflater.from(add_invitation.this);
        wen_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtils.v("wen_send click!");
                map = new HashMap<String,String>();
                SharedPreferences preferences = getSharedPreferences("userInfo",
                        Activity.MODE_PRIVATE);

                String username = preferences.getString("username", "");

                if(!check()) {
                    Toast.makeText(add_invitation.this, "请检查内容是否填写完毕", Toast.LENGTH_SHORT);
                }else{
                    map.put("token", MainActivity.token);//用户名
                    map.put("title", wen_title.getText().toString());
                    map.put("context", wen_content.getText().toString());
                    map.put("circleType", Constant.getCurrentGroup(all_group.getText().toString())+"");

                    LogUtils.v("tiezi"+map.values().toString());
                    String url= Constant.getUrl()+"app/circle/createTips.htmls";
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
                                                if (!obj.get("code").toString().equals("10000"))
                                                    Toast.makeText(add_invitation.this, message, Toast.LENGTH_SHORT).show();
                                                else {
                                                    Toast.makeText(add_invitation.this, message, Toast.LENGTH_SHORT).show();
                                                    new Handler().postDelayed(new Runnable() {
                                                        public void run() {
                                                            Intent intent = new Intent();
                                                            intent.setClass(add_invitation.this, MainActivity.class);
                                                            intent.putExtra("page","1");
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
            }
        });
        close_this = (TextView) this.findViewById(R.id.close_this);
        pic = (ImageView) this.findViewById(R.id.pic);
        close_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消事件
                finish();
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


                //view.invalidate();
                pop.setBackgroundDrawable(new ColorDrawable(00000000));
                pop.setAnimationStyle(R.style.popup_anim);

                pop.showAtLocation(rootview, Gravity.NO_GRAVITY,(width-500)/2,(height-40)/2);
                lp.alpha=0.6f;
                getWindow().setAttributes(lp);
            }
        });

    }
    //选择图片
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

    //弹出分类
    private class my_pop extends PopupWindow {
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        public my_pop(ListView listView, int i, int wrapContent, boolean b) {
            super(listView,i,wrapContent,b);
        }
        public my_pop(View contentView, int width, int height, boolean focusable) {
            super(contentView,width,height,focusable);
        }

        @Override
        public void dismiss() {
            super.dismiss();
            pop = null;
        }
        //在其上方显示
        public void showUp(View v) {
            //获取需要在其上方显示的控件的位置信息
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            //在控件上方显示
            showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - 40 / 2, location[1] - 220);
        }
    }
    /**
     * 自定义Adapter
     * @author liuyazhuang
     *
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View views = mInflater.inflate(R.layout.select_group_adapter,null);

            TextView select_group_content = (TextView) views.findViewById(R.id.select_group_content);
            select_group_content.setText(lists.get(position));
            select_group_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    all_group.setText(lists.get(position));
                    pop.dismiss();
                    isPopShow = false;
//                    点击了之后记得调成false，下次就可以打开了
                }
            });
            return views;
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
