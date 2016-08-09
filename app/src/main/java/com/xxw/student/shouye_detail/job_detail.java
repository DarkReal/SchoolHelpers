package com.xxw.student.shouye_detail;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xxw.student.R;
import com.xxw.student.utils.Constant;
import com.xxw.student.utils.HttpThread;
import com.xxw.student.utils.LogUtils;
import com.xxw.student.utils.getHandler;
import com.xxw.student.view.DrawableCenterTextView;
import com.xxw.student.view.My_Toast;

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
	private TextView job_name,apply_time,job_offer,location;
	private AlertDialog alertDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_job_detail);
		Bundle bundle=getIntent().getExtras();
		job_ids=bundle.getString("id");
		company_ids = company_detail.company_id;

		job_name = (TextView)findViewById(R.id.job_name);
		location = (TextView)findViewById(R.id.location);
		apply_time = (TextView) findViewById(R.id.apply_time);
		job_offer = (TextView) findViewById(R.id.job_offer);

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.return_before:
				finish();
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				break;
			case R.id.up_resume:
				/*my_toast.ToastShow(this, (ViewGroup) findViewById(R.id.job_detail_content), "成功投递");
				up_resume.setText("已投递");*/

				LayoutInflater inflater = LayoutInflater.from(job_detail.this);
				View view = inflater.inflate(R.layout.custom_alertdialog, null);

				AlertDialog.Builder builder = new AlertDialog.Builder(job_detail.this);
				alertDialog = builder.create();
				alertDialog.show();
				alertDialog.getWindow().setContentView(view);
				alertDialog.getWindow().setLayout(400, 200);

				break;
			case R.id.collect:
				my_toast.ToastShow(this, (ViewGroup) findViewById(R.id.job_detail_content), "收藏成功");
				collect.setClickable(false);
				collect.setText("已收藏");
				collect();
				break;
		}
	}

	private void collect() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", job_ids);
		map.put("company_id",company_ids);
		String url= Constant.getUrl()+"collect/collectjob.htmls";
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
									if (obj.get("code").toString().equals("-1"))
										Toast.makeText(job_detail.this, message, Toast.LENGTH_SHORT).show();
									else {
										Toast.makeText(job_detail.this, message, Toast.LENGTH_SHORT).show();
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

	private void getjob() {
		String url= Constant.getUrl()+"job/getjob.htmls";
		HashMap<String,String> map = new HashMap<String,String>();

		map.put("id", job_ids);
		map.put("company_id",company_ids);
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
									if (obj.get("code").toString().equals("-1"))
										Toast.makeText(job_detail.this, message, Toast.LENGTH_SHORT).show();
									else {
										job_name.setText(obj.get("job_name").toString());
										location.setText(obj.get("location").toString());
										apply_time.setText(obj.get("fabu_time").toString());
										job_offer.setText(obj.get("salary").toString());
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
