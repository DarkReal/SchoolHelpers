package com.xxw.student.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 自定义的http公共访问类，无框架,数据map传递，httpurlconnection
 * Created by DarkReal on 2016/6/24.
 */
public abstract class HttpThread extends Thread {
    private String url;
    private HashMap<String,String> map = new HashMap<String,String>() ;
    public static JSONObject obj;
    private String Sessionid;
    public HttpThread(String url,HashMap<String,String> map){
        this.url = url;
        this.map = map;
    }
    public HttpThread(String url){
        this.url = url;
    }

//    private void doGet(){
//        try {
//            //遍历hashmap里面的值组合成一个字符
//            String urlstr=url+"?";
//            Iterator iter = map.entrySet().iterator();
//            if(iter.hasNext()){
//                Map.Entry entry = (Map.Entry) iter.next();
//                Object key = entry.getKey();
//                Object val = entry.getValue();
//                urlstr+=key+"="+val;
//            }
//            while(iter.hasNext()){
//                Map.Entry entry = (Map.Entry) iter.next();
//                Object key = entry.getKey();
//                Object val = entry.getValue();
//                urlstr+="&"+key+"="+val;
//            }
//            LogUtils.v(urlstr);
//            URL httpURL = new URL(urlstr);
//            HttpURLConnection conn = (HttpURLConnection) httpURL.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setReadTimeout(5000);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String str;
//            StringBuffer sb = new StringBuffer();
//            while((str=reader.readLine())!=null){
//                sb.append(str);
//            }
//            System.out.println("result:" + sb.toString());
//            conn.disconnect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void doPost() {
        LogUtils.v("in post function");
        try {
            URL httpurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", " application/json");
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.connect();

            OutputStream out = conn.getOutputStream();
            //map不为空的时候组合参数传过去
            if(map.size()!=0) {
                LogUtils.v(map.toString());
                Iterator iter = map.entrySet().iterator();
                String content = "";
                if (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    content += key + "=" + val;
                }
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    content += "&" + key + "=" + val;
                }
                LogUtils.v(content);
                out.write(content.getBytes());
            }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str="";
                while((str=reader.readLine())!=null){
                    sb.append(str);
                }
                obj= new JSONObject(sb.toString());
                getObj(obj);
            LogUtils.v("json.string" + obj.toString());
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //回调
    public abstract void getObj(JSONObject obj) throws org.json.JSONException;

    /*public String getSessionId(){
        try {
            HttpURLConnection conn= (HttpURLConnection) httpurl.openConnection();
            // 取得sessionid.
            String cookieval = conn.getHeaderField("Set-Cookie");
            LogUtils.v("getSessionId中的cookieval"+cookieval);
            if(cookieval != null) {
                Sessionid = cookieval.substring(0, cookieval.indexOf(";"));
            }
            //App.sessionId=sessionid;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return Sessionid;
    }*/
    /*public synchronized  void createSession() {

        Long sessionCreateTime=0L;
        if(null==Sessionid ||"".equals(Sessionid)){
            Sessionid=getSessionId();
            System.out.println("session创建！");
            sessionCreateTime=new Date().getTime();
        }

        //20分钟后更换session
        long nowTime=new Date().getTime();
        int min=(int)((nowTime-sessionCreateTime)/(1000*60));
        if(min>=15){
            Sessionid=getSessionId();
            System.out.println("session重新创建！");
            sessionCreateTime=new Date().getTime();
        }else{
            sessionCreateTime=new Date().getTime();
        }

    }*/
    @Override
    public void run() {
        //doGet();
        LogUtils.v("-----in this function<--from run");
        doPost();
        LogUtils.v("-----after this function<--from run");
    }


}
