/**
 * 
 */
package com.xxw.student.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.util.Map;

/**
 * xutils http公共访问类
 * @author liujian
 * 
 */
public class HttpClientUtil {


	/**
	 * post 请求参数
	 * 
	 * @param url
	 *            请求路径
	 * @param datas
	 *            请求参数
	 * @throws Exception
	 */
	public void postRequest(final String url, Map<String, String> datas)
			throws Exception {
		RequestParams params = new RequestParams();
		if (datas != null) {
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
		}
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		params.setContentType("applicatin/json");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);

		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest" + "conn..." + url);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("success:"+responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("error:"+error.getExceptionCode());
			}
		});

	}

	/*
	 * post 请求参数
	 * 
	 * @param url 请求路径
	 * 
	 * @param datas 请求参数
	 * 
	 * @files files 请求参数
	 * 
	 * @throws Exception
	 */
	public void postRequest(final String url, Map<String, String> datas,
			String uuid, File[] files) throws Exception {
		int imgn = 0;
		RequestParams params = new RequestParams();
		if (datas != null) {
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
		}
		if (files != null) {
			for (File file : files) {
				params.addBodyParameter("imgfiles" + imgn, file);
				imgn++;
			}
		} else {

		}
		params.setHeader("uuid", uuid);
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest" + "conn..." + url);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// System.out.println(responseInfo.result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error.getExceptionCode());

			}
		});

	}
	//只上传图片文件
	public void postRequest(final String url, File[] myfiles,String type) throws Exception {
		int imgn = 0;
		RequestParams params = new RequestParams();
		if (myfiles != null) {
			for (File file : myfiles) {
				params.addBodyParameter("myfiles", file);
			}
		} else {

		}
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		params.setContentType("multipart/form-data");

		HttpUtils http = new HttpUtils();

		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest" + "conn..." + url);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// System.out.println(responseInfo.result);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error.getExceptionCode());

			}
		});

	}
//	public void postRequest1(final String url, Map<String, Object> datas)
//			throws Exception { 
//		int imgn = 0;
//		RequestParams params = new RequestParams();
//		if (datas != null) {
//			for (String key : datas.keySet()) {
//				params.addBodyParameter(key, (String) datas.get(key));
//			}
//		}
//		
//			
//				params.addBodyParameter("imgfiles", (String) datas.get(key));
//			
//		params.setHeader("User-Agent",
//				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
//		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 1);
//		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
//
//			@Override
//			public void onStart() {
//				LogUtils.v("postRequest"+"conn..." + url);
//			}
//
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				// System.out.println(responseInfo.result);
//				onGetResponseData.OnGetData(responseInfo.result);
//			}
//
//			@Override
//			public void onFailure(HttpException error, String msg) {
//				System.out.println(error.getExceptionCode());
//				onGetResponseData.OnGetData(null);
//			}
//		});
//
//	}

	/**
	 * post 请求参数
	 * 
	 * @param url
	 *            请求路径
	 * @param datas
	 *            请求参数
	 * @throws Exception
	 */
	public void postRequest(final String url, Map<String, String> datas,
			String uuid) throws Exception {
		RequestParams params = new RequestParams();
		if (datas != null) {
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
		}
		params.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");

		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");

		params.setHeader("uuid", uuid);
		// params.setHeader("Content-Type", "text/html;charset=utf-8");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest" + "conn..." + url);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// System.out.println(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(error.getExceptionCode());
			}
		});

	}

	/**
	 * get 请求方式
	 * 
	 * @param url
	 *            请求路径
	 * @param datas
	 *            请求参数
	 * @throws Exception
	 */
	public void getRequest(String url, Map<String, String> datas, String uuid)
			throws Exception {
		RequestParams params = new RequestParams();
		if (datas != null) {
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
		}
		params.setHeader("uuid", uuid);
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest" + "conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.v("postRequest onSuccess...");
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.v("postRequest onFailure...");
			}
		});

	}

	public void getRequest(String url, Map<String, String> datas)
			throws Exception {
		RequestParams params = new RequestParams();
		if (datas != null) {
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
		}
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.v("postRequest onSuccess...");
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.v("postRequest onFailure...");
			}
		});

	}

	/**
	 * 表单提交
	 * 
	 * @param url
	 *            路径
	 * @param datas
	 *            请求参数
	 * @param files
	 *            请求文件
	 * @return result 请求参数
	 * @throws Exception
	 */
	public void postRequestByXUtils(String url, Map<String, String> datas,
			Map<String, String> files) throws Exception {
		// final String result = null;
		RequestParams params = null;
		if (datas == null || files == null) {

		} else {
			params = new RequestParams();
			for (String key : datas.keySet()) {
				params.addBodyParameter(key, datas.get(key));
			}
			for (String key : files.keySet()) {
				if ("".equals(files.get(key)) || files.get(key) == null)
					continue;
				params.addBodyParameter(key, new File(files.get(key)));
			}
		}

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 1);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});

	}

	/**
	 * POST json提交
	 * 
	 * @param url
	 *            请求路径
	 * @param j
	 *            参数
	 * @throws Exception
	 */
	/*public void postJsonByXUtils(String url, KeJson j) throws Exception {
		// KeJson j = KeJson.newKeJson();
		// j.put("userid", "10000");
		// j.put("password", "123456");

		String str = j.toJson();

		RequestParams params = null;
		params = new RequestParams();
		params.setBodyEntity(new StringEntity(str, "utf-8"));
		params.setHeader("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:0.9.4)");
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				LogUtils.v("postRequest conn...");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.v("postRequest success...");
				onGetResponseData.OnGetData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.v("postRequest onFailure...");
				LogUtils.v("postRequest msg");
				onGetResponseData.OnGetData(null);
			}
		});

	}

	*//**
	 * post 上传Json
	 * 
	 * @param path
	 * @param j
	 * @return
	 *//*
	protected String doPost(String path, KeJson j) {

		String urlAddress = path;
		// KeJson j = KeJson.newKeJson();
		// j.put("userid", "锟斤拷锟斤拷");
		// j.put("password", "123456");

		String str = j.toJson();

		try {
			URL url = new URL(urlAddress);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");
			connection.setRequestProperty("Content-Length",
					str.getBytes().length + "");
			connection.connect();

			connection.getOutputStream().write(str.getBytes());

			if (connection.getResponseCode() == 200) {
				int count = 0;

				InputStream in = new BufferedInputStream(
						connection.getInputStream());

				ByteArrayBuffer buffer = new ByteArrayBuffer(4096);

				while ((count = in.read()) != -1) {
					buffer.append(count);
				}

				String Data = EncodingUtils.getString(buffer.toByteArray(),
						"utf-8");

				return Data;
			} else {
				return connection.getResponseCode() + "";
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "error";
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
	}*/
}
