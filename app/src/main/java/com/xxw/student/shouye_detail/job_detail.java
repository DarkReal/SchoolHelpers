package com.xxw.student.shouye_detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.LoginActivity;
import com.xxw.student.MainActivity;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.DateUtils;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.DrawableCenterTextView;
import com.xxw.student.view.MaterialDialog;
import com.xxw.student.view.My_Toast;
import com.xxw.student.view.sweetdialog.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class job_detail extends Activity implements View.OnTouchListener,View.OnClickListener{
	// 定义手势检测器实例
	private LinearLayout layout;
	private LinearLayout return_before;
	//手指向右滑动时的最小速度
	private static final int XSPEED_MIN = 200;
	//手指向右滑动时的最小距离
	private static final int XDISTANCE_MIN = 150;
	//记录手指按下时的横坐标。
	private float xDown;
	//记录手指移动时的横坐标。
	private float xMove;
	//用于计算手指滑动的速度。
	private VelocityTracker mVelocityTracker;
	private My_Toast my_toast;
	private DrawableCenterTextView up_resume;
	private DrawableCenterTextView collect;
	private String job_ids,company_ids;//job_id
	private JSONObject json;
	private TextView job_name,apply_time,job_offer,job_address,job_workrest,job_workdate,job_workhappy,job_plannum,job_todate;
	private ImageView company_pic;
	private AlertDialog alertDialog;
	private BitmapUtils bitmapUtils;
	private static String flag;//标志是否收藏  等于2的时候已收藏
	private TextView positionDescs,workRequireds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_job_detail);
		Bundle bundle=getIntent().getExtras();
		job_ids=bundle.getString("id");
		company_ids = company_detail.company_id;
		bitmapUtils = new BitmapUtils(job_detail.this);



		initData();
		getjob();
	}

	private void initData() {
		layout = (LinearLayout) findViewById(R.id.job_detail_content);
		return_before = (LinearLayout) findViewById(R.id.return_before);
		my_toast = My_Toast.createToastConfig();//初始化
		up_resume= (DrawableCenterTextView) findViewById(R.id.up_resume);
		collect= (DrawableCenterTextView) this.findViewById(R.id.collect);

		up_resume.setOnClickListener(this);
		collect.setOnClickListener(this);
		return_before.setOnClickListener(this);
		layout.setOnTouchListener(this);

		company_pic = (ImageView) findViewById(R.id.company_pic);
		job_name = (TextView) findViewById(R.id.job_name);
		apply_time = (TextView) findViewById(R.id.apply_time);
		job_offer = (TextView) findViewById(R.id.job_offer);
		job_address = (TextView) findViewById(R.id.job_address);
		job_workrest = (TextView) findViewById(R.id.job_workrest);
		job_workdate = (TextView) findViewById(R.id.job_workdate);
		job_workhappy = (TextView) findViewById(R.id.job_workhappy);
		job_plannum = (TextView) findViewById(R.id.job_plannum);
		job_todate = (TextView) findViewById(R.id.job_todate);

		positionDescs = (TextView) findViewById(R.id.positionDesc);
		workRequireds = (TextView) findViewById(R.id.workRequired);
		Constant.isActivity();//检测登录状态
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.return_before:
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				break;
			case R.id.up_resume:
//				/*my_toast.ToastShow(this, (ViewGroup) findViewById(R.id.job_detail_content), "成功投递");
//				up_resume.setText("已投递");*/
//
//				LayoutInflater inflater = LayoutInflater.from(job_detail.this);
//				View view = inflater.inflate(R.layout.custom_alertdialog, null);
//
//				AlertDialog.Builder builder = new AlertDialog.Builder(job_detail.this);
//				alertDialog = builder.create();
//				alertDialog.show();
//				alertDialog.getWindow().setContentView(view);
//				alertDialog.getWindow().setLayout(400, 200);
				upResume();
				break;
			case R.id.collect:
				if(collect.getText().toString().equals("已收藏")){
					collect("cancel");
				}else{
					collect("sure");
				}
				break;
		}
	}
	//投递简历
	private void upResume() {
		if(!Constant.isActivitied||MainActivity.token==""){
			new SweetAlertDialog(job_detail.this, SweetAlertDialog.ERROR_TYPE)
					.setTitleText("未登录")
					.setContentText("未登录的状态下不能投递简历，请重新登录!")
					.autodismiss(2000)
					.show();
		}
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("token", MainActivity.token);
		map.put("recruitId", job_ids);
		String url= Constant.getUrl()+"app/user/sendResume.htmls";
		try{
			HttpThread ht = new HttpThread(url,map){
				@Override
				public void getObj(final JSONObject obj) throws JSONException {
					if(obj!=null){
						final String message = obj.get("message").toString();
						LogUtils.v("message: "+obj.get("message").toString());
						LogUtils.v("obj"+obj.toString());

						getHandler.mHandler.post(new Runnable() {
							@Override
							public void run() {
								try {
									if(!obj.get("code").toString().equals("10000")){
										new MaterialDialog(job_detail.this)
												.setTitle("警告")
												.autodismiss(2000)
												.setMessage(message)
												.show();
										if (obj.get("code").toString().equals("10004")||obj.get("code").toString().equals("10005")){//还没有创建过简历
											//没有简历的情况下
//											LayoutInflater inflater = LayoutInflater.from(job_detail.this);
//											View view = inflater.inflate(R.layout.custom_alertdialog, null);
//											AlertDialog.Builder builder = new AlertDialog.Builder(job_detail.this);
//											alertDialog = builder.create();
//											alertDialog.show();
//											alertDialog.getWindow().setContentView(view);
//											alertDialog.getWindow().setLayout(400, 200);
//											TextView cancel = (TextView) view.findViewById(R.id.dialog_cancel);
//											TextView submit = (TextView) view.findViewById(R.id.dialog_submit);
//
//											cancel.setOnClickListener(new View.OnClickListener() {
//												@Override
//												public void onClick(View view) {
//													alertDialog.cancel();
//												}
//											});
//											//确认以后跳转页面去填写简历
//											submit.setOnClickListener(new View.OnClickListener() {
//												@Override
//												public void onClick(View view) {
//													Intent intent = new Intent();
//													intent.setClass(job_detail.this,MainActivity.class);
//													intent.putExtra("page","3-2");
//													startActivity(intent);
//													finish();
//												}
//											});
											new SweetAlertDialog(job_detail.this, SweetAlertDialog.ERROR_TYPE)
													.setTitleText("警告")
													.setContentText("您没有创建过简历，请前往简历填写")
													.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
														@Override
														public void onClick(SweetAlertDialog sDialog) {
															//转回登录页面
															Intent intent = new Intent();
															intent.setClass(job_detail.this,MainActivity.class);
															intent.putExtra("page","3-2");
															startActivity(intent);
															finish();
														}
													})
													.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
														@Override
														public void onClick(SweetAlertDialog sweetAlertDialog) {
															sweetAlertDialog.dismiss();
														}
													})
													.show();
										}
									}
									else {
										my_toast.ToastShow(job_detail.this, (ViewGroup) findViewById(R.id.job_detail_content), "成功投递");
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

	//收藏职位
	private void collect(final String option) {
		if(!Constant.isActivitied){
			new SweetAlertDialog(job_detail.this, SweetAlertDialog.ERROR_TYPE)
					.setTitleText("未登录")
					.setContentText("未登录的状态下不能收藏职位，请重新登录!")
					.autodismiss(2000)
					.show();
		}
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("token", MainActivity.token);
		map.put("recruitId", job_ids);
		map.put("option", option);
		String url= Constant.getUrl()+"app/user/collectRecruit.htmls";
		try{
			HttpThread ht = new HttpThread(url,map){
				@Override
				public void getObj(final JSONObject obj) throws JSONException {
					if(obj!=null){
						final String message = obj.get("message").toString();
						LogUtils.v("message: "+obj.get("message").toString());
						LogUtils.v("obj"+obj.toString());

						getHandler.mHandler.post(new Runnable() {
							@Override
							public void run() {
								try {
									if (!obj.get("code").toString().equals("10000"))
										new MaterialDialog(job_detail.this)
												.setTitle("警告")
												.autodismiss(2000)
												.setMessage(message)
												.show();
									else {
										if(option.equals("sure")){
											collect.setText("已收藏");
											my_toast.ToastShow(job_detail.this, (ViewGroup) findViewById(R.id.job_detail_content), message);
										}else{
											collect.setText("收藏");
											my_toast.ToastShow(job_detail.this, (ViewGroup) findViewById(R.id.job_detail_content), "取消收藏");
										}
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
//获取职位详情
	private void getjob() {
		String url= Constant.getUrl()+"app/company/getRecruitInfo.htmls";
		HashMap<String,String> map = new HashMap<String,String>();

		map.put("recruitId", job_ids);
		map.put("token", MainActivity.token);
		try{
			HttpThread ht = new HttpThread(url,map){
				@Override
				public void getObj(final JSONObject obj) throws JSONException {
					if(obj!=null){
						final String message = obj.get("message").toString();
						LogUtils.v("message: "+obj.get("message").toString());
						LogUtils.v("obj"+obj.toString());
						json = obj.getJSONObject("object");

						LogUtils.v("ja-职位详情"+json.toString());
						getHandler.mHandler.post(new Runnable() {
							@Override
							public void run() {
								try {
									if (!obj.get("code").toString().equals("10000"))
										new MaterialDialog(job_detail.this)
												.setTitle("警告")
												.autodismiss(2000)
												.setMessage(message)
												.show();
									else {
										JSONObject json = obj.getJSONObject("object");
										JSONObject jsons = json.getJSONObject("recruit");
										flag = json.getString("flag");
										//获取是否已经收藏的状态显示
										if(flag.equals("1")){
											collect.setText("收藏");
										}else if(flag.equals("2")){
											collect.setText("已收藏");
										}


										bitmapUtils.display(company_pic, Constant.getUrl() + "upload/media/images/" + jsons.get("pic").toString());
										job_name.setText(jsons.get("recruitName").toString());
										apply_time.setText(jsons.get("createTime").toString());
										job_offer.setText(jsons.get("moneyMonth").toString()+"/月");
										job_address.setText(jsons.get("workPlace").toString());
										job_workrest.setText(jsons.get("workRest").toString());
										job_workdate.setText(jsons.get("workDate").toString());
										job_workhappy.setText(jsons.get("workHappy").toString());
										job_plannum.setText(jsons.get("planNum").toString());
										job_todate.setText(DateUtils.TimeStamp2Date(jsons.get("toDate").toString(), DateUtils.DATE_FORMAT3));

										String positionDesc = jsons.get("positionDesc").toString();
										String workRequire = jsons.get("workRequire").toString();


										String[] arr = positionDesc.split(";");
										String[] arr2 = workRequire.split(";");

										LayoutInflater inflater = LayoutInflater.from(job_detail.this);
										//获取岗位描述字符串，分割后显示
										workRequireds.setText("");
										positionDescs.setText("");
										for(int i=0;i<arr.length;i++){
											positionDescs.append(arr[i] + "\n");
										}
										//获取工作要求字符串，分割后显示
										for(int i = 0; i<arr2.length;i++){
											workRequireds.append(arr2[i] + "\n");
										}

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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDown = event.getRawX();
				break;
			case MotionEvent.ACTION_MOVE:
				xMove = event.getRawX();
				//活动的距离
				int distanceX = (int) (xMove - xDown);
				//获取顺时速度
				int xSpeed = getScrollVelocity();
				//当滑动的距离大于我们设定的最小距离且滑动的瞬间速度大于我们设定的速度时，返回到上一个activity
				if(distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
					finish();
					overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				}
				break;
			case MotionEvent.ACTION_UP:
				recycleVelocityTracker();
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
	 * @param event
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 回收VelocityTracker对象。
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 获取手指在content界面滑动的速度。
	 * @return 滑动速度，以每秒钟移动了多少像素值为单位。
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

}
