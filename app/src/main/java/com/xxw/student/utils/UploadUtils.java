package com.xxw.student.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/** * Created by atwal on 2016/3/11.
 * 文件上传工具类
 * 已实现（图片上传）
 * */
public class UploadUtils {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; //超时时间
    private static final String CHARSET = "utf-8";
    private static final String BOUNDARY = UUID.randomUUID().toString(); //边界标识随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; //内容类型

     /** * 上传文件
      * @param file 文件
      * @param RequestURL post地址
      * @param params 除文件外其他参数
      * @return
      * */

     public static String uploadFile(File file, String RequestURL, Map params, String filename) {
         String result = null;
         try {
             URL url = new URL(RequestURL);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setReadTimeout(TIME_OUT);
             conn.setConnectTimeout(TIME_OUT);
             conn.setDoInput(true);
             conn.setDoOutput(true);
             conn.setUseCaches(false);
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Charset", CHARSET);
             conn.setRequestProperty("connection", "keep-alive");
             conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
             conn.setRequestProperty("Accept-Charset", CHARSET);

             DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
             StringBuffer sb = new StringBuffer();
             sb.append(getRequestData(params));
             if (file != null) {
                 sb.append(PREFIX);
                 sb.append(BOUNDARY);
                 sb.append(LINE_END);
                 sb.append("Content-Disposition: form-data; name = \"myfiles\"; filename=\"" + filename + "\" " + LINE_END);
                 sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                 sb.append(LINE_END);
             }
             dos.write(sb.toString().getBytes());
             if (file != null) {
                 InputStream is = new FileInputStream(file);
                 byte[] bytes = new byte[1024];
                 int len = 0;
                 while ((len = is.read(bytes)) != -1) {
                     dos.write(bytes, 0, len);
                 }
                 is.close();
                 dos.write(LINE_END.getBytes());
                 byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                 dos.write(end_data);
             }
             dos.flush();

             int res = conn.getResponseCode();
             LogUtils.v("response code:" + res);
             if (res == 200) {
                 LogUtils.v("request success");

                 //字节流接收，编码问题需要单独处理
//                 InputStream input = conn.getInputStream();
//                 BufferedReader in = new BufferedReader(new InputStreamReader(input, "utf-8"));
//                 StringBuffer sb1 = new StringBuffer();
//                 int ss;
//                 while ((ss = in.read()) != -1) {
//                     sb1.append((char) ss);
//                 }
                 //字符流接收，就不用考虑编码问题了
                 InputStreamReader input = new InputStreamReader(conn.getInputStream());
                 StringBuffer sb1 = new StringBuffer();
                 int ss;
                 while ((ss = input.read()) != -1) {
                     sb1.append((char) ss);
                 }
                 result = sb1.toString();
                 LogUtils.v("result : " + result);
             }
             else {
                 LogUtils.v("request error");
             }
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return result;
     }

    /**对post参数进行编码处理
     * @param params post参数
     * @return
     * */
    private static StringBuffer getRequestData(Map<String,String> params) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String,String> entry : params.entrySet()) {
                stringBuffer.append(PREFIX);
                stringBuffer.append(BOUNDARY);
                stringBuffer.append(LINE_END);
                stringBuffer.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                stringBuffer.append(LINE_END);
                stringBuffer.append(URLEncoder.encode(entry.getValue(), CHARSET));
                stringBuffer.append(LINE_END);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
}
