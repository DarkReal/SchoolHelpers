package com.xxw.student.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xxw.student.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 预览图片
 * Created by DarkReal on 2016/7/11.
 */
public class CachePic extends Activity {

    //获取传过来的图片的名称，对应到存储空间里面读取

    private File path,myfiles;
    private Button upload;
    private String TAG = "CachePic";
    private JSONObject json;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_cachepic);
        Intent intent = getIntent();
        final String picname = intent.getStringExtra("picname");
        path = Environment.getExternalStorageDirectory() ; //获得SDCard目录
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        Bitmap bmpDefaultPic=null;
        ImageView iv = (ImageView)findViewById(R.id.pic);
        if(bmpDefaultPic==null) {
            bmpDefaultPic = BitmapFactory.decodeFile(path + "/Images/" + picname, null);

            ByteArrayOutputStream baos = null ;
            try{
                baos = new ByteArrayOutputStream();
                bmpDefaultPic.compress(Bitmap.CompressFormat.JPEG, 30, baos);

            }finally{
                try {
                    if(baos != null)
                        baos.close() ;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //图片压缩
        bmpDefaultPic = CompressImage.compressImage(bmpDefaultPic);
        iv.setImageBitmap(bmpDefaultPic);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传图片到服务器
               /* myfiles = new File(Environment.getExternalStorageDirectory(),picname);
                String url = Constant.getUrl()+"upload/uploadFiles.htmls";
                try {
                    HttpClientUtil ht = new HttpClientUtil();
                    ht.postRequest(url, new File[]{myfiles},"image");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                /*不能在主线程里面进行网络访问*/
                new Thread(){
                    @Override
                    public void run()
                    {
                        String requestURL = Constant.getUrl()+"common/uploadFiles.htmls";
                        String filepath = Environment.getExternalStorageDirectory()+ "/Images/" + picname;
                        LogUtils.v(filepath);
                        File myfiles = new File(filepath);
                        LogUtils.v("file exists:" + myfiles.exists());
                        if (myfiles.exists()) {
                            Map params = new HashMap<>();
                            params.put("type", "image");
                            //...如果有其他参数添加到这里

                            final String request = UploadUtils.uploadFile(myfiles, requestURL, params, picname);
                            try {
                                json = new JSONObject(request);
                                if (!json.get("code").toString().equals("10000")) {
                                    //成功上传，返回页面
//                                    Toast.makeText(CachePic.this, json.get("message").toString(), Toast.LENGTH_LONG).show();
                                    LogUtils.v(json.get("message").toString());
                                } else {
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LogUtils.v("upload" + request);
                        }
                    }
                }.start();

            }
        });
    }

}
