package com.xxw.student.view.deletescroller;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.xxw.student.R;
import com.xxw.student.utils.Constant;

public class DeleteAdapter extends BaseAdapter {

	class ViewHolder {
		ImageView lxr_pic;//联系人头像
		TextView lxr_name;//联系人姓名
		TextView lxr_sign;//联系人签名
		Button btnDelete;
		Button btnblacklist;
	}



	public static ListItemDelete itemDelete = null;
	private List<HashMap<String, String>> list;
	private LayoutInflater mInflater;
	private Context context;
	private String flag[];
	private int ItemIDs[];
	private int layoutID;
	private Context mcontext;
	private BitmapUtils bitmapUtils;

	public DeleteAdapter(Context context, List<HashMap<String,String>> listDatas,int layoutID, String flag[], int ItemIDs[] ) {
		mInflater = LayoutInflater.from(context);
		this.list = listDatas;
		this.context = context;
		this.flag = flag;
		this.ItemIDs = ItemIDs;
		this.mcontext = context;
		this.bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_delete, null);
			holder.lxr_pic = (ImageView) convertView.findViewById(R.id.lxr_pic);
			holder.lxr_name = (TextView) convertView.findViewById(R.id.lxr_name);
			holder.lxr_sign = (TextView) convertView.findViewById(R.id.lxr_sign);

			holder.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
			holder.btnblacklist = (Button) convertView.findViewById(R.id.btnblacklist);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo("删除");
			}
		});
		holder.lxr_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showInfo("点击了数据： " + list.get(position));
			}
		});
		holder.btnblacklist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo("拉黑");
			}
		});

		for (int i = 0; i < flag.length; i++) {//备注1
			TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);
			tv.setText((String) list.get(position).get(flag[i]));
		}
		bitmapUtils.display(holder.lxr_pic, Constant.getUrl() + "upload/media/images/" + list.get(position).get("userHeadPic"));


		return convertView;
	}



	private Toast mToast;

	public void showInfo(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public static void ItemDeleteReset() {
		if (itemDelete != null) {
			itemDelete.reSet();
		}
	}
}
