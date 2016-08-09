package com.xxw.student.shouye_detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.shouye_detail.select_city.util.City;
import com.xxw.student.shouye_detail.select_city.util.CityDB;
import com.xxw.student.shouye_detail.select_city.util.CityListAdapter;
import com.xxw.student.shouye_detail.select_city.util.PinyinComparator;
import com.xxw.student.shouye_detail.select_city.util.SearchCityAdapter;
import com.xxw.student.shouye_detail.select_city.views.BladeView;
import com.xxw.student.shouye_detail.select_city.views.PinnedHeaderListView;
import com.xxw.student.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 首页--选择城市
 */
public class city_select extends Activity implements OnClickListener {

    private LinearLayout city_linear;
    private TextView titleTextView,backTxt;

//    private Button cancelSearchBtn;
    private EditText searchEditText;
    private ImageButton clearSearchBtn;

    private PinnedHeaderListView mListView;
    private ListView mSearchListView;

    private FrameLayout allCityLayoutContainer;
    private FrameLayout searchCityLayoutContainer;

    private String searchKeywords;

    private CityDB cityDB;
    private ArrayList<City> mCityList;
    private CityListAdapter mainListViewAdapter;
    private SearchCityAdapter searchCityAdapter;

    private TextView current_city;
    private TextView hot_city_1,hot_city_2,hot_city_3,hot_city_4,hot_city_5,hot_city_6,hot_city_7,hot_city_8,hot_city_9,hot_city_10,hot_city_11,hot_city_12;



    private InputMethodManager mInputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.city_biz_plugin_weather_select);
        initCityClick();
        initData();
        setupViews();
    }

    private void initCityClick() {

        current_city = (TextView) this.findViewById(R.id.current_city);
        hot_city_1 = (TextView) this.findViewById(R.id.hot_city_1);
        hot_city_2 = (TextView) this.findViewById(R.id.hot_city_2);
        hot_city_3 = (TextView) this.findViewById(R.id.hot_city_3);
        hot_city_4 = (TextView) this.findViewById(R.id.hot_city_4);
        hot_city_5 = (TextView) this.findViewById(R.id.hot_city_5);
        hot_city_6 = (TextView) this.findViewById(R.id.hot_city_6);
        hot_city_7 = (TextView) this.findViewById(R.id.hot_city_7);
        hot_city_8 = (TextView) this.findViewById(R.id.hot_city_8);
        hot_city_9 = (TextView) this.findViewById(R.id.hot_city_9);
        hot_city_10 = (TextView) this.findViewById(R.id.hot_city_10);
        hot_city_11 = (TextView) this.findViewById(R.id.hot_city_11);
        hot_city_12 = (TextView) this.findViewById(R.id.hot_city_12);




        current_city.setOnClickListener(this);
        hot_city_1.setOnClickListener(this);
        hot_city_2.setOnClickListener(this);
        hot_city_3.setOnClickListener(this);
        hot_city_4.setOnClickListener(this);
        hot_city_5.setOnClickListener(this);
        hot_city_6.setOnClickListener(this);
        hot_city_7.setOnClickListener(this);
        hot_city_8.setOnClickListener(this);
        hot_city_9.setOnClickListener(this);
        hot_city_10.setOnClickListener(this);
        hot_city_11.setOnClickListener(this);
        hot_city_12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.current_city:
            case R.id.hot_city_1:
            case R.id.hot_city_2:
            case R.id.hot_city_3:
            case R.id.hot_city_4:
            case R.id.hot_city_5:
            case R.id.hot_city_6:
            case R.id.hot_city_7:
            case R.id.hot_city_8:
            case R.id.hot_city_9:
            case R.id.hot_city_10:
            case R.id.hot_city_11:
            case R.id.hot_city_12:
                TextView tv = (TextView) v;
                LogUtils.v("city_list" + tv.getText().toString());
                changeCity(tv.getText().toString());
                break;

        }
    }



    private void initData() {
        cityDB = openCityDB();
        mCityList = cityDB.getAllCity();
        Intent intent = getIntent();
        if(intent.getStringExtra("city")!=null){
            String city_name = intent.getStringExtra("city");
            LogUtils.v("city" + city_name + "is");
            current_city.setText(city_name);
        }
        PinyinComparator comparator = new PinyinComparator();
        Collections.sort(mCityList, comparator);

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // ****************************************************
        StringBuilder sb = new StringBuilder();
        City city = mCityList.get(0);
        sb.append(city.getName() + "\n");
        sb.append(city.getNumber() + "\n");
        sb.append(city.getPinyin() + "\n");
        sb.append(city.getProvince() + "\n");
        sb.append(city.getPy() + "\n");
        /*LogUtils.v(sb.toString());*/
        // ****************************************************
        mainListViewAdapter = new CityListAdapter(this, mCityList);

    }

    private void setupViews() {
        titleTextView = (TextView) findViewById(R.id.title_name);
        backTxt = (TextView) findViewById(R.id.title_back);

        city_linear= (LinearLayout) findViewById(R.id.city_linear);
        titleTextView.setText("选择城市");
        backTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 结束当前activity，返回到首页
                finish();
            }
        });

        View search_layout = findViewById(R.id.search_container);
        /*cancelSearchBtn = (Button) search_layout
                .findViewById(R.id.btn_cancel_search);*/
        searchEditText = (EditText) search_layout
                .findViewById(R.id.search_edit);
        clearSearchBtn = (ImageButton) search_layout
                .findViewById(R.id.ib_clear_text);

        // 实时监听搜索框中的输入文字
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                searchKeywords = s.toString();
                LogUtils.v("words:" + searchKeywords);
                if ((searchKeywords == null) || (searchKeywords.equals(""))) {
                    city_linear.setVisibility(View.VISIBLE);
                    LogUtils.v("searchKeywords" + "null");
                } else {
                    city_linear.setVisibility(View.GONE);
                    LogUtils.v(searchKeywords);
                    LogUtils.v("" + mCityList.size());
                    searchCityAdapter = new SearchCityAdapter(city_select.this,
                            mCityList);
                    mSearchListView.setAdapter(searchCityAdapter);
                    mSearchListView.setTextFilterEnabled(true);
                    mSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //每一个搜索结果的点击事件
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            City results_city = (City) searchCityAdapter.getItem(position);
                            LogUtils.v(results_city.getName());
                            changeCity(results_city.getName());
                        }
                    });
                    if (TextUtils.isEmpty(searchKeywords)) {
                        allCityLayoutContainer.setVisibility(View.VISIBLE);
                        searchCityLayoutContainer.setVisibility(View.GONE);
                        clearSearchBtn.setVisibility(View.GONE);
                    } else {
                        allCityLayoutContainer.setVisibility(View.GONE);
                        searchCityLayoutContainer.setVisibility(View.VISIBLE);
                        mSearchListView.setVisibility(View.VISIBLE);
                        clearSearchBtn.setVisibility(View.VISIBLE);
                        searchCityAdapter.getFilter().filter(searchKeywords);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearSearchBtn.setVisibility(View.VISIBLE);
            }
        });

        // 监听搜索清除按钮是否被点击，如被点击，则清楚搜索内容。
        clearSearchBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                searchKeywords = "";
                searchEditText.setText("");
                mInputMethodManager.hideSoftInputFromWindow(
                        searchEditText.getWindowToken(), 0);
                // clearSearchBtn.setVisibility(View.INVISIBLE);
            }
        });

        mListView = (PinnedHeaderListView) findViewById(R.id.citys_list);
        mListView.setAdapter(mainListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City results_city = (City) mCityList.get(position);
                LogUtils.v(results_city.getName());
                changeCity(results_city.getName());
            }
        });

        View emptyView = findViewById(R.id.citys_list_empty);
        emptyView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);

        BladeView bladeView = (BladeView) findViewById(R.id.citys_bladeview);
        bladeView.setOnItemClickListener(new BladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                city_linear.setVisibility(View.GONE);//这里可以加一个动画
                int position = mainListViewAdapter.getPositionForSection(s
                        .charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        mSearchListView = (ListView) findViewById(R.id.search_list);
        allCityLayoutContainer = (FrameLayout) findViewById(R.id.city_content_container);
        searchCityLayoutContainer = (FrameLayout) findViewById(R.id.search_content_container);
    }


    private CityDB openCityDB() {
        String path = "/data/data/com.xxw.student/"+CityDB.DATABASE_NAME;
       /* LogUtils.v(path);*/

        File dbFile = new File(path);
        if (!dbFile.exists()) {

            // 将asserts目录下的city.db文件拷贝到path路径下面去
            try {
                dbFile.createNewFile();
                InputStream is = getResources().getAssets().open(CityDB.DATABASE_NAME);
                FileOutputStream fos = new FileOutputStream(dbFile);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    // 将buffer数组中的第0个开始的len个字节写入到文件输入流中
                    fos.write(buffer, 0, len);
                    // flush()强行缓冲区内容输出否则直到缓冲区满才会一次性内容输出
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new CityDB(this, path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void changeCity(String cityName){


        Intent intent = new Intent();
        intent.setClass(city_select.this,MainActivity.class);
        intent.putExtra("current_city", cityName);
        startActivity(intent);
        current_city.setText(cityName);
    }
}
