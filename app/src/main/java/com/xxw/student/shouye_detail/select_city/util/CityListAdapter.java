package com.xxw.student.shouye_detail.select_city.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.shouye_detail.select_city.views.PinnedHeaderListView;

import java.util.List;

/**
 * 城市适配器
 */
public class CityListAdapter extends BaseAdapter implements PinnedHeaderListView.PinnedHeaderAdapter,SectionIndexer {

	private Context mContext;
	private List<City> mList;//城市列表

	public CityListAdapter(Context context, List<City> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.city_select_item, null);
			holder = new ItemHolder();
			holder.groupTextView = (TextView) convertView
					.findViewById(R.id.group_title);
			holder.cityTextView = (TextView) convertView
					.findViewById(R.id.column_title);
			convertView.setTag(holder);//缓存起来方便多次使用
		} else {
			holder = (ItemHolder) convertView.getTag();//如果已经被创建了，那么直接获取这个标签
		}

		City city = mList.get(position);

		// 获取第position个元素的首字母的ASCII码值
		int section = getSectionForPosition(position);

		//如果是刚好是该列表的第一个元素

		if(position == getPositionForSection(section)){
			holder.groupTextView.setVisibility(View.VISIBLE);
			holder.groupTextView.setText(String.valueOf(city.getPy().toUpperCase().charAt(0)));
		}else{
			holder.groupTextView.setVisibility(View.GONE);//gone会把控件的位置都空出来，完全隐藏
		}
		holder.cityTextView.setText(city.getName());
		return convertView;
	}

	/**
	 * 分组名   groupTextView
	 * 城市名	cityTextView
	 */
	private class ItemHolder {
		TextView groupTextView;
		TextView cityTextView;
	}

	@Override
	public int getPinnedHeaderState(int position) {
		int realPosition = position;
		if (realPosition < 0 || position >= getCount()) {
			return PINNED_HEADER_GONE;//0
		}
		int section = getSectionForPosition(realPosition);
		int nextSectionPosition = getPositionForSection(section + 1);
		if (nextSectionPosition != -1
				&& realPosition == nextSectionPosition - 1) {
			return PINNED_HEADER_PUSHED_UP;//2
		}
		return PINNED_HEADER_VISIBLE;//1
	}

	@Override
	public void configurePinnedHeader(View header, int position, int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据首字母的ascii值来获取在该ListView中第一次出现该首字母的位置，
	 * 例如：从上面的效果图1中，如果section是66，也就是‘B’的ascii值，那么该方法返回的position就是2
	 *
	 */
	@Override
	public int getPositionForSection(int section) {

		for(int i=0; i<getCount(); i++){
			String py = mList.get(i).getPy();
			char firstChar = py.toUpperCase().charAt(0);//大写的第一个字符
			if(firstChar == section){
				return i;
			}
		}
		return -1;
	}

	/**
	 * 根据ListView的position来获取该位置上面的name的首字母char的ascii值，例如：
	 * 如果该position上面的name是阿妹，首字母就是A，那么此方法返回的就是'A'字母的ascii值，也就是65， 'B'是66，依次类推
	 */
	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).getPy().toUpperCase().charAt(0);
	}

}
