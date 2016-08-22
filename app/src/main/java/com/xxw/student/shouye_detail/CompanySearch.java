package com.xxw.student.shouye_detail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.Adapter.CustomAdapter_addFriend;
import com.xxw.student.Adapter.CustomAdapter_companyList;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.search_history.MyListView;
import com.xxw.student.view.search_history.RecordSQLiteOpenHelper;
import com.xxw.student.view.search_history.RecordSQLiteOpenHelper2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 公司的查询页面
 * Created by DarkReal on 2016/8/19.
 */
public class CompanySearch extends Activity{

    private ImageView backToCompany;
    private EditText search_company;
    private ListView company_search;
    private TextView noneWord;
    private MyListView history;
    private TextView clear_history;
    private LinearLayout search_history;
    private RelativeLayout search_layout;
    private JSONArray ja_company;
    private ArrayList<HashMap<String,String>> companyList;
    /*数据库变量*/
    private RecordSQLiteOpenHelper2 helper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private CustomAdapter_companyList customAdapter_companyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_company);
        helper = new RecordSQLiteOpenHelper2(CompanySearch.this);
        init();    
    }

    //初始化
    private void init() {
        backToCompany = (ImageView) findViewById(R.id.backToCompany);
        search_company = (EditText) findViewById(R.id.search_company);
        company_search = (ListView) findViewById(R.id.company_search);
        noneWord = (TextView) findViewById(R.id.noneWord);
        history = (MyListView) findViewById(R.id.history);
        clear_history = (TextView) findViewById(R.id.clear_history);
        search_history = (LinearLayout) findViewById(R.id.search_history);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);

        backToCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        search_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged-->"
                        + search_company.getText().toString() + "<--");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged-->"
                        + search_company.getText().toString() + "<--");
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged-->"
                        + search_company.getText().toString() + "<--");
                //每次改变值的时候都去发送一次请求,时刻动态监听
                if (search_company.getText().toString().equals("")) {
                    search_history.setVisibility(View.VISIBLE);
                    search_layout.setVisibility(View.GONE);
                    queryData();
                } else {
                    search_history.setVisibility(View.GONE);
                    search_layout.setVisibility(View.VISIBLE);
                    getCompanyList(search_company.getText().toString());
                }
            }
        });
        //开始不显示搜索结果之后显示
        search_layout.setVisibility(View.GONE);
        clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData();
            }
        });
        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                search_company.setText(name);
            }
        });
        //开始展现所有历史记录
        queryData();
    }

    //查询公司获取查询列表
    private void getCompanyList(String param) {

//        kProgressHUD.show();

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("companyName",param);

        LogUtils.v(map.toString());
        String url = Constant.getUrl() + "app/company/searchCompany.htmls";
        try {

            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {

                    if (obj != null) {
                        final String message;

                        message = obj.get("message").toString();
                        LogUtils.v("-------jsonstr------" + obj.toString());
                        LogUtils.v("message: " + obj.get("message").toString());
//                        kProgressHUD.dismiss();//读完接口之后消失
                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10000")) {
                                        new MaterialDialog(CompanySearch.this)
                                                .setTitle("警告")
                                                .autodismiss(2000)
                                                .setMessage(message)
                                                .show();
                                        noneWord.setVisibility(View.VISIBLE);
                                        noneWord.setText("无查询结果");
                                        company_search.setVisibility(View.GONE);
                                    }else {
                                        //查询成功
                                        ja_company = obj.getJSONArray("object");
                                        if(ja_company.length()==0){
                                            noneWord.setVisibility(View.VISIBLE);
                                            noneWord.setText("无查询结果");
                                        }else{
                                            noneWord.setVisibility(View.GONE);
                                            company_search.setVisibility(View.VISIBLE);
                                        }
                                        initAdapter();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //初始化构造器
    private void initAdapter() {
        companyList = new ArrayList<HashMap<String, String>>();
        try {
            for (int i = 0; i < ja_company.length(); i++) {
                JSONObject jsonObject = (JSONObject) ja_company.get(i);
                HashMap<String,String> company_map = new HashMap<String, String>();
                company_map.put("companyName",jsonObject.get("companyName").toString());
                company_map.put("companyDesc",jsonObject.get("companyDesc").toString());
                company_map.put("companyId",jsonObject.get("id").toString());
                company_map.put("count_job","共有n在招职位");
                company_map.put("company_city",jsonObject.get("city").toString());
                company_map.put("companyPic",jsonObject.get("companyPic").toString());
                companyList.add(company_map);
                LogUtils.v("companyList" + companyList.toString());
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        customAdapter_companyList = new CustomAdapter_companyList(CompanySearch.this, this, companyList, R.layout.company_list_ever, new String[]{"companyName","companyDesc", "companyId", "count_job","company_city"},
                new int[]{R.id.company_name, R.id.company_desc, R.id.company_id, R.id.count_job,R.id.company_city});
        company_search.setAdapter(customAdapter_companyList);

        company_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //插入搜索记录
                boolean hasData = hasData(search_company.getText().toString().trim());
                if (!hasData) {
                    insertData(search_company.getText().toString().trim());
                    queryData();
                }

                Intent intent = new Intent();
                intent.setClass(view.getContext(), company_detail.class);
                Bundle bundle = new Bundle();
                //获得单击部分的隐藏起来的company_id的text的值
                TextView tv = (TextView) view.findViewById(R.id.company_id);
                String ids = tv.getText().toString();
                bundle.putString("id", ids);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    /*数据库语句*/
    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into recordscom(name) values ('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData() {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from recordscom order by id desc ", null);
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(CompanySearch.this, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        history.setAdapter(adapter);
        adapter.notifyDataSetChanged();


//        while(cursor.moveToNext())
//        {
//            int nameColumnIndex = cursor.getColumnIndex("name");
//            String strValue=cursor.getString(nameColumnIndex);
//            LogUtils.v("cursor:"+strValue);
//        }
    }
    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from recordscom where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from recordscom");
        db.close();
    }
    
}
