package com.xxw.student.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.MainActivity;
import com.xxw.student.PageModel.AppCompany;
import com.xxw.student.R;
import com.xxw.student.utils.CompressImage;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.pullrefresh_view;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * Created by xxw on 2016/3/27.
 */

public class contentFragment_shouye extends Fragment implements GestureDetector.OnGestureListener,View.OnTouchListener, pullrefresh_view.OnHeaderRefreshListener {

    /**
     * 轮播图
     */
    private View view;
    private ViewPager viewPager;
    private static ArrayList<ImageView> images;
    private List<View> dots;
    private LinearLayout search_icon;
    private LinearLayout content_list;
    private GestureDetector gestureDetector;
    private pullrefresh_view mPullToRefreshView;
    private JSONArray ja;
    private JSONObject json;
    private BitmapUtils bitmapUtils;
    private JSONArray companyListja;
    private List<AppCompany> comList;

    //存放图片的id
    private static ArrayList<String> imagesStr;
    private ImageHandler handler = new ImageHandler(new WeakReference<contentFragment_shouye>(this));


    private int oldPosition=0;
    /**
     * 公司列表
     */
    private String[] companyPic;

    private String[] companyName;
    private String[] companyDesc;


    private String[] location = new String[]{
            "杭州",
            "北京",
            "上海"
    };
    private int ids[] = new int[]{1,2,3};//所有公司的列表

    private SimpleAdapter simple_adapter;
    private ListView company_list;
    private List<Map<String, Object>> dataList;

    public View onCreateView(LayoutInflater inflater, final ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_content_shouye, container, false);
        company_list= (ListView) view.findViewById(R.id.company_list);
        //初始化
        mPullToRefreshView = (pullrefresh_view) view.findViewById(R.id.main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.vp);
        search_icon = (LinearLayout) view.findViewById(R.id.search_icon);
        content_list = (LinearLayout) view.findViewById(R.id.content_layout_shouye);
        gestureDetector = new GestureDetector(view.getContext(),this);
        bitmapUtils = new BitmapUtils(view.getContext());
        imagesStr = new ArrayList<String>();

        dots = new ArrayList<View>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));

        images = new ArrayList<ImageView>();

        getIndexImage();//获取图片资源    并配置适配器
//        getCompanyInfo();//从数据库中读取公司信息
        getCompanyList();


        //下面列表的添加
//        dataList=new ArrayList<Map<String, Object>>();
//        for(int i=0;i<company_name.length;i++){
//            Map<String, Object> company_list = new HashMap<String, Object>();
//            company_list.put("company_name",company_name[i]);
//            company_list.put("company_pic",company_pic[i]);
//            company_list.put("job",job[i]);
//            company_list.put("location",location[i]);
//            company_list.put("id",ids[i]);
//            dataList.add(company_list);
//        }
//        simple_adapter=new SimpleAdapter(this.getActivity(), dataList,R.layout.company_list_ever, new String[] {"company_name","company_pic","job","location","id"},
//                new int[] {R.id.company_name,R.id.company_pic,R.id.job,R.id.location,R.id.company_id});
//        company_list.setAdapter(simple_adapter);
//        company_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent();
//                intent.setClass(view.getContext(), company_detail.class);
//                Bundle bundle = new Bundle();
//                //获得单击部分的隐藏起来的company_id的text的值
//                TextView tv = (TextView) view.findViewById(R.id.company_id);
//                String ids = tv.getText().toString();
//                bundle.putString("id", ids);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//


        return view;
    }

    private void getCompanyList() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("city", MainActivity.city);
        map.put("pageNow","0");
        String url = Constant.getUrl() + "app/company/getCompanyList.htmls";
        try {
            HttpThread ht = new HttpThread(url,map) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {

                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("--jsonstr--"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if (!obj.get("code").toString().equals("10000"))
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    else {
                                        companyListja = (JSONArray) obj.get("object");
                                        filltheList();//填充companyList列表
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
    //初始化公司列表
    private void filltheList() {
//        try {
//            for(int i=0 ;i < companyListja.length(); i++){
//                comList.add((AppCompany) companyListja.get(i));
//            }
//            LogUtils.v(comList.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        try {
            for (int i = 0; i < companyListja.length(); i++) {
                JSONObject jsonObject = (JSONObject) companyListja.get(i);


            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    //获取首页图片
    private void getIndexImage() {
        String url = Constant.getUrl() + "app/company/getIndexPics.htmls";
        //先获取首页的三张图片的名字
        try {
            HttpThread ht = new HttpThread(url) {
                @Override
                public void getObj(final JSONObject obj) throws JSONException {
                    if (obj != null) {
                        final String message = obj.get("message").toString();
                        LogUtils.v("--jsonstr--"+obj.toString());
                        LogUtils.v("message: "+obj.get("message").toString());
                        try {
                            if (!obj.get("code").toString().equals("10000"))
                                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                            else {
                                ja = (JSONArray) obj.get("object");
                                json = (JSONObject) ja.get(0);

                                imagesStr.add(json.getString("pic1").toString());
                                imagesStr.add(json.getString("pic2").toString());
                                imagesStr.add(json.getString("pic3").toString());

                                LogUtils.v("--shouye--"+imagesStr.toString());


                                LogUtils.v("before_set_image"+imagesStr.size()+"");
                                viewPager.setAdapter(new ImageAdapter(imagesStr));
                                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                    //配合Adapter的currentItem字段进行设置。
                                    @Override
                                    public void onPageSelected(int arg0) {

                                        dots.get(arg0 % imagesStr.size()).setBackgroundResource(R.drawable.dot_focused);
                                        dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
                                        oldPosition = arg0 % imagesStr.size();
                                        handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, arg0, 0));
                                    }

                                    @Override
                                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                                    }

                                    //覆写该方法实现轮播效果的暂停和恢复
                                    @Override
                                    public void onPageScrollStateChanged(int arg0) {
                                        switch (arg0) {
                                            case ViewPager.SCROLL_STATE_DRAGGING:
                                                handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                                                break;
                                            case ViewPager.SCROLL_STATE_IDLE:
                                                handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                });

                                viewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
                                //初始化点的颜色情况
                                dots.get(0).setBackgroundResource(R.drawable.dot_focused);

                                //开始轮播效果
                                handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            ht.start();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Bitmap get3Pic(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            LogUtils.v(url);
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            bitmap = CompressImage.compressImage(bitmap);//压缩
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //onGestureListener的方法实现
    @Override
    public boolean onDown(MotionEvent e) {
        LogUtils.v("Fragment onTouch start");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtils.v("Fragment onTouch start");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtils.v("Fragment onTouch start");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        LogUtils.v("Fragment onTouch start");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtils.v("Fragment onTouch start");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.v("Fragment onTouch start");
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtils.v("Fragment onTouch start");
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 下拉刷新
     * @param view
     */
    @Override
    public void onHeaderRefresh(pullrefresh_view view) {

        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                Date date = new Date();
                DateFormat format=new SimpleDateFormat("MM-dd HH:mm");
                String time=format.format(date);
                mPullToRefreshView.onHeaderRefreshComplete("上次更新于: "+time);
            }
        }, 1000);


    }
    //图片轮播
    private static class ImageHandler extends Handler {
        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE  = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT   = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT  = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED  = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;

        //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
        private WeakReference<contentFragment_shouye> weakReference;
        private int currentItem = 0;

        protected ImageHandler(WeakReference<contentFragment_shouye> wk){
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //LogUtils.v("receive message" + msg.what);
            contentFragment_shouye frag = weakReference.get();
            if (frag==null){
                //Activity已经回收，无需再处理UI了
                return ;
            }
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (currentItem!=0 &&frag.handler.hasMessages(MSG_UPDATE_IMAGE)){
                frag.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    frag.viewPager.setCurrentItem(currentItem);
                    //准备下次播放
                    frag.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    frag.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;
            }
        }
    }
    //轮播图片的adapter
    private class ImageAdapter extends PagerAdapter {

        private ArrayList<String> viewlist;

        public ImageAdapter(ArrayList<String> viewlist) {
            this.viewlist = viewlist;
        }

        @Override
        public int getCount() {
            //设置成最大，使用户看不到边界
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            //Warning：不要在这里调用removeView
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //对ViewPager页号求模取出View列表中要显示的项

            position %= viewlist.size();
            if (position<0){
                position = viewlist.size()+position;
            }
            ImageView imageView = new ImageView(view.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //bitmap做好了二级缓存
            bitmapUtils.display(imageView, Constant.getUrl() + "upload/media/images/" + viewlist.get(position).toString());

//            LogUtils.v("--ImageAdapter--"+Constant.getUrl() + "upload/media/images/" + viewlist.get(position).toString());
//            LogUtils.d("log in this");
//            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
//            ViewParent vp =view.getParent();
//            if (vp!=null){
//                ViewGroup parent = (ViewGroup)vp;
//                parent.removeView(view);
//            }
            container.addView(imageView);
            //add listeners here if necessary
            return imageView;
        }
    }

}


