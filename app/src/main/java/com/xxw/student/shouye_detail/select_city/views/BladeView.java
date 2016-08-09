package com.xxw.student.shouye_detail.select_city.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.LogUtils;


/**
 * 右边子母选择器
 */
public class BladeView extends View {
	//触摸事件监听器
	private OnItemClickListener mOnItemClickListener;
	//26个字母
	String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K",
			"L", "M", "N", "P", "Q", "R", "S", "T", "W", "X",
			"Y", "Z" };
	//选中索引进行初始化
	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false;
	private PopupWindow mPopupWindow;//以悬浮的形式显示选中的字母
	private TextView mPopupText;
	private int mCharHeight = 15;

	public BladeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mCharHeight = context.getResources().getDimensionPixelSize(
				R.dimen.blade_view_text_size);
	}

	public BladeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCharHeight = context.getResources().getDimensionPixelSize(
				R.dimen.blade_view_text_size);
	}

	public BladeView(Context context) {
		super(context);
		mCharHeight = context.getResources().getDimensionPixelSize(
				R.dimen.blade_view_text_size);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor("#4d000000"));
		} else {
			canvas.drawColor(Color.parseColor("#00000000"));
		}

		//获取对应的高度
		int height = getHeight();
		//获取对应的宽度
		int width = getWidth();
		//获取每个字母的高度
		int singleHeight = height / b.length;

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.parseColor("#36ab60"));
			// paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(mCharHeight);
			paint.setFakeBoldText(true);
			paint.setAntiAlias(true);
			//如果是被选中的字母,则在屏幕的位置居中显示
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
			}
			//x的坐标为当前view的宽度的1/2减去字母宽度的1/2
			//点击的时候显示选了什么字母
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			//y的坐标为i+1个字母高度
			float yPos = singleHeight * i + singleHeight;
			//将字母画出来
			canvas.drawText(b[i], xPos, yPos, paint);
			//重置画笔
			paint.reset();
		}

	}

	@Override
	protected Parcelable onSaveInstanceState() {
		dismissPopup();
		return super.onSaveInstanceState();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if (oldChoose != c) {
					if (c > 0 && c < b.length) {
						performItemClicked(c);
						choose = c;
						invalidate();
					}
				}

				break;
			case MotionEvent.ACTION_MOVE:
				if (oldChoose != c) {
					if (c > 0 && c < b.length) {
						performItemClicked(c);
						choose = c;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
				choose = -1;
				dismissPopup();
				invalidate();
				break;
		}
		return true;
	}
//浮动显示的小方块部分的绘制
	private void showPopup(int item) {
		if (mPopupWindow == null) {
			mPopupText = new TextView(getContext());
			mPopupText
					.setBackgroundResource(R.drawable.ic_contacts_index_backgroud_sprd);
			mPopupText.setTextColor(Color.parseColor("#36ab60"));
			mPopupText.setHeight(150);
			mPopupText.setWidth(150);

			mPopupText.setTextSize(30);
			mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL);
			mPopupWindow = new PopupWindow(mPopupText,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}

		String text = "";
		if (item == 0) {
			text = "#";
		} else {
			text = b[item-1];
		}
		mPopupText.setText(text);
		if (mPopupWindow.isShowing()) {
			mPopupWindow.update();//更新显示内容
		} else {
			mPopupWindow.showAtLocation(getRootView(),
					Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		}
	}

	private void dismissPopup() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	private void performItemClicked(int item) {
		if (mOnItemClickListener != null) {
			mOnItemClickListener.onItemClick(b[item]);
			//点击时候隐藏上面的部分

			LogUtils.v("performitemclicked"+"this is " + b[item]);
			showPopup(item);
		}
	}

	public interface OnItemClickListener {
		void onItemClick(String s);
	}

}
