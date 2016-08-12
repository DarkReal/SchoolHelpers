package com.xxw.student.fragment;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.Commonhandler;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子--头部
 * Created by xxw on 2016/3/27.
 */
public class titleFragment_quanzi extends Fragment {

    private View view;
    private LinearLayout select_group;
    private TextView all_group;
    private ImageView select_group_btn;
    private LinearLayout search_icon;
    //构造用到的集合
    private List<String> lists;
    //布局加载器
    private LayoutInflater mInflater;
    //自定义适配器
    private MyAdapter mAdapter;
    //PopupWindow
    private my_pop pop;
    //是否显示PopupWindow，默认不显示
    private boolean isPopShow = false;


    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

        view = inflater.inflate(R.layout.main_title_quanzi,container,false);
        search_icon = (LinearLayout) view.findViewById(R.id.search_icon);

        Constant.setGroup_type();
        lists = Constant.getGroup_type();
        all_group = (TextView) view.findViewById(R.id.all_group);
        select_group_btn = (ImageView) view.findViewById(R.id.select_group_btn);
        select_group=(LinearLayout)view.findViewById(R.id.select_group);

        mInflater = LayoutInflater.from(view.getContext());

        mAdapter = new MyAdapter();
        select_group.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                if(pop == null){//popupwindow展示出来的时候
                    //初始化透明度
                    LogUtils.v("popcreate");
                    //初始化popupwindow
                    ListView listView = new ListView(view.getContext());
                    listView.setCacheColorHint(0xFFFFFFFF);
                    listView.setAdapter(mAdapter);
                    pop = new my_pop(listView, select_group.getWidth()+70, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                    pop.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

                    isPopShow = true;

                }
                if(isPopShow){
                    //展示出来的时候，再点击，那么
                    LogUtils.v("pop出来啦");

                    pop.showAsDropDown(all_group, 0, 0);

                    lp.alpha = 0.6f;
                    getActivity().getWindow().setAttributes(lp);

                    isPopShow = false;
                    view.setOnTouchListener(new View.OnTouchListener() {
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

        search_icon.setOnClickListener(new View.OnClickListener() {
            private my_pop pop2;
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            @Override
            public void onClick(View v) {
                View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.main_title_quanzi_search, null);
                TextView all_group_search = (TextView) contentView.findViewById(R.id.all_group_search);

                all_group_search.setText(all_group.getText().toString());

                pop2 = new my_pop(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                View rootview = LayoutInflater.from(view.getContext()).inflate(R.layout.main_title_quanzi, null);

                //view.invalidate();
                pop2.setBackgroundDrawable(new ColorDrawable(00000000));
                pop2.setAnimationStyle(R.style.popup_anim);
                pop2.showAtLocation(rootview, Gravity.TOP,0,45);
                lp.alpha=0.6f;
                getActivity().getWindow().setAttributes(lp);
            }
        });

        return view;
    }

    /**
     * 自定义popupwindow
     */
    private class my_pop extends PopupWindow {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

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
            lp.alpha = 1f;
            getActivity().getWindow().setAttributes(lp);
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

                    Message msg = new Message();
                    msg.what = position;
                    Commonhandler.comHandler.sendMessageDelayed(msg, 10);

                    pop.dismiss();
                    isPopShow = false;
                    LogUtils.v("pop消失了!!!!!!");

                }
            });
            return views;
        }

    }

}
