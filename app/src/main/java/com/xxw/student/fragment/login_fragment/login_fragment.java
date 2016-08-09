package com.xxw.student.fragment.login_fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xxw.student.MainActivity;
import com.xxw.student.PageModel.AppUser;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.Digests;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 登录用fragment
 * Created by DarkReal on 2016/5/5.
 */
public class login_fragment extends Fragment {
    private View view;
    private EditText phone, password;
    private Button login_right;
    private String phonestr, passstr;
    private HashMap<String, String> map;
    private String ToastText = "";
    private Toast simpletoast;
    private JSONObject user;//user的全局变量
    private String localaddress;
    private String TAG;
    protected LocationManager locationManager;
    private static Double Latitude;
    private static Double Longitude;
    private static String locationstr;
    private Location locl;
    private String locationProvider;
    private String token;
    public static AppUser appuser;
    private JSONObject userjson;//全局的userjson
    private static SharedPreferences sharedPreference;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login, container, false);
        TAG = "login_fragment";
        init();
        sharedPreference = view.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        login_right = (Button) view.findViewById(R.id.login_right);
        login_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastText = "";
                check();
                if (!ToastText.equals("")) {
                    simpletoast = Toast.makeText(view.getContext(), ToastText, Toast.LENGTH_SHORT);
                    simpletoast.show();
                } else {
                    //发送请求过去
                    map = new HashMap<String, String>();

                    //经纬度字符串
                    localaddress = getLocal();
                    Toast.makeText(view.getContext(), localaddress, Toast.LENGTH_LONG).show();
                    LogUtils.v("local" + localaddress);
                    getCity();//载入城市信息
//
//                    获取经纬度
//                  map.put("type", "0");
//                  map.put("param", localaddress);

                    //获取登录
                    map.put("phone",phonestr);
                    map.put("password",passstr);
                    map.put("loc",localaddress);
                    LogUtils.v(map.values().toString());
                    String url = Constant.getUrl() + "app/user/login.htmls";

                    try {

//                        HttpClientUtil ht = new HttpClientUtil();
//                        ht.postRequest(url, map);

                            HttpThread ht = new HttpThread(url,map){


                            @Override
                            public void getObj(final JSONObject obj){
                                try {
                                if (obj != null) {
                                    final String message;

                                        message = obj.get("message").toString();

                                    LogUtils.v("-------jsonstr------" + obj.toString());
                                    LogUtils.v("message: " + obj.get("message").toString());

                                    getHandler.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (!obj.get("code").toString().equals("10000")) {
                                                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    JSONObject json = obj.getJSONObject("object");
                                                    token = json.get("token").toString();

                                                    userjson = json.getJSONObject("user");
                                                    //登陆成功---载入城市信息
                                                    LogUtils.v(userjson.toString());
                                                    //转PageModel

                                                    new Handler().postDelayed(new Runnable() {
                                                        public void run() {
                                                            Intent intent = new Intent();
                                                            intent.setClass(view.getContext(), MainActivity.class);
                                                            //保存登录状态

                                                            saveUser();
                                                            //发送user信息，存到MainActivity中作为全局变量
                                                            startActivity(intent);
                                                            main_login main_login = (com.xxw.student.fragment.login_fragment.main_login) getActivity();
                                                            main_login.finish();
                                                        }

                                                    }, 1000);

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
            }
        });
        return view;
    }
    //根据经纬度取得城市
    private void getCity() {
        LogUtils.v("getCity");
        map.put("type", "0");
        map.put("param", localaddress);

        LogUtils.v(map.values().toString());
        String url = Constant.getUrl() + "common/getAddress.htmls";

        try {

            HttpThread ht = new HttpThread(url,map){
                @Override
                public void getObj(final JSONObject obj) throws JSONException {

                    if (obj != null) {
                        final String message;

                        message = obj.get("message").toString();
                        LogUtils.v("-------jsonstr------" + obj.toString());
                        LogUtils.v("message: " + obj.get("message").toString());

                        getHandler.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (!obj.get("code").toString().equals("10002")) {
                                        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        final JSONObject json = (JSONObject) obj.get("object");
                                        MainActivity.city = json.get("region").toString();

                                        LogUtils.v(MainActivity.city);
                                        saveCity();
                                        Thread.sleep(1000);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
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

    private void saveCity() {
        final SharedPreferences.Editor editor = sharedPreference.edit();//获取编辑器
        try{
            editor.putString("currcity", MainActivity.city);
            LogUtils.v("editor"+editor.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    //存储登陆返回回来的用户信息
    //写入sharedpreference
    private void saveUser() {

            final SharedPreferences.Editor editor = sharedPreference.edit();//获取编辑器
            try {
                editor.putString("id", userjson.getString("id"));
                editor.putString("realname", userjson.getString("realName"));
                editor.putString("gender", userjson.getString("gender"));
                editor.putString("birth", userjson.getString("birth"));
                editor.putString("city", userjson.getString("city"));
                editor.putString("education", userjson.getString("education"));
                editor.putString("univercity", userjson.getString("univercity"));
                editor.putString("majorIn", userjson.getString("majorIn"));
                editor.putString("phone", userjson.getString("phone"));
                editor.putString("email", userjson.getString("email"));
                editor.putString("token", Digests.decrypt(token));
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
    }

    private String getLocal() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(view.getContext(), "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return "null";
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "未获取权限";
        }
        //不是一次就能成功的
        locl = locationManager.getLastKnownLocation(locationProvider);
        if(locl!=null){
            //不为空,显示地理位置经纬度
//            locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            locationManager.requestLocationUpdates("gps", 60000, 1, locationListener);
            Latitude = locl.getLatitude();
            Longitude = locl.getLongitude();
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
        locationstr = Latitude + "-" + Longitude;
        return locationstr;
    }
    //监听位置变化
    LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            Latitude = locl.getLatitude();
            Longitude = locl.getLongitude();
        }
    };

    public void init(){
        phone = (EditText) view.findViewById(R.id.phone);
        password = (EditText) view.findViewById(R.id.password);

    }
    public void check(){
        phonestr = phone.getText().toString().trim().toLowerCase();
        passstr = password.getText().toString().trim().toLowerCase();
        if (phonestr.equals(""))
            ToastText = "请填写手机号码";
        else if (passstr.equals(""))
            ToastText = "请填写密码";
        }

}
