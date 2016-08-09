package com.xxw.student.shouye_detail.select_city.util;

import java.util.Comparator;

/**
 * 拼音比较，主要应用在搜索的哪里，还有列表展示的那里
 */
public class PinyinComparator implements Comparator<City> {

	@Override
	public int compare(City lhs, City rhs) {
		
		return lhs.getPinyin().compareTo(rhs.getPinyin());
	}

}
