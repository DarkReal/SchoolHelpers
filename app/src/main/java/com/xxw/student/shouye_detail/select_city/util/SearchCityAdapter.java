package com.xxw.student.shouye_detail.select_city.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xxw.student.R;
import com.xxw.student.utils.LogUtils;

import java.util.ArrayList;

public class SearchCityAdapter extends BaseAdapter implements Filterable {

	private ArrayList<City> allCities;
	private ArrayList<City> resultCities;
	private Context mContext;

	public SearchCityAdapter(Context context, ArrayList<City> allCities) {
		this.mContext = context;
		this.allCities = allCities;
		//新建一个结果列表
		resultCities = new ArrayList<City>();
	}

	@Override
	public int getCount() {
		return resultCities.size();
	}

	@Override
	public Object getItem(int position) {
		return resultCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ItemHolder {
		TextView provinceTextView; //显示省
		TextView cityTextView; //显示城市
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemHolder itemHolder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.city_search_item, null);
			itemHolder = new ItemHolder();
			itemHolder.provinceTextView = (TextView) convertView
					.findViewById(R.id.search_province);
			itemHolder.cityTextView = (TextView) convertView
					.findViewById(R.id.column_title);

			convertView.setTag(itemHolder);
		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}

		City city = resultCities.get(position);
		itemHolder.provinceTextView.setText(city.getProvince());
		itemHolder.cityTextView.setText(city.getName());

		return convertView;
	}

	//过滤我的列表
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				resultCities = (ArrayList<City>) results.values;

				LogUtils.v("searchCityAdapter"+resultCities.size());
				
				if (results.count > 0) {
					notifyDataSetChanged();//不用刷新的更新listview
				} else {
					notifyDataSetInvalidated();//把之前的查询结果全部清除掉
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String str = constraint.toString().toLowerCase();
				FilterResults results = new FilterResults();
				
				ArrayList<City> cityList = new ArrayList<City>();
				
				if (null != allCities && allCities.size() > 0) {
					for (City c : allCities) {
						if (c.getPy().indexOf(str) > -1
								|| c.getPinyin().indexOf(str) > -1
								|| c.getName().indexOf(str) > -1) {
							cityList.add(c);
						}
					}
				}
				
				results.values = cityList;
				results.count = cityList.size();
				return results;
			}
		};
		return filter;
	}

}
