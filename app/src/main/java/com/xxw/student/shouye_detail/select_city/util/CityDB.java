package com.xxw.student.shouye_detail.select_city.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xxw.student.utils.LogUtils;

import java.util.ArrayList;

/**
 * 数据库文件已经存在，名称叫做city.db，放在assets目录下
 * 读取数据库文件中的所有内容，并且进行修改
 */
public class CityDB{

	//定义数据库database的名称
	public static final String DATABASE_NAME = "city.db";
	//定义city.db数据库中的表名称
	private static final String TABLE_NAME = "city";
	//声明SQLiteDatabase类型的变量sqliteDB
	private SQLiteDatabase sqliteDB;

	public CityDB(Context context, String path) {
		sqliteDB = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	//查询数据库是否打开
	public boolean  isOpen(){
		return sqliteDB != null && sqliteDB.isOpen();
	}


	//关闭数据库
	public void close(){
		if(sqliteDB != null && sqliteDB.isOpen()){
			sqliteDB.close();
		}
	}

	//从数据库中读取所有的城市信息，返回装载所有city的List。
	public ArrayList<City> getAllCity(){
		ArrayList<City> cityList = new ArrayList<City>();
		Cursor cursor = sqliteDB.rawQuery("select * from " + TABLE_NAME, null);//从city表中找到所有的元素
		while(cursor.moveToNext()){
			String province = cursor.getString(cursor.getColumnIndex("province"));
			String city = cursor.getString(cursor.getColumnIndex("name"));
			String number = cursor.getString(cursor.getColumnIndex("number"));
			String allPY = cursor.getString(cursor.getColumnIndex("pinyin"));
			String allFirstPY = cursor.getString(cursor.getColumnIndex("py"));

			//编程时发现数据库此项信息不准备，故判断修改之。
			if("筠连".equals(city)){
				LogUtils.v("citydb" + allPY + "  ####### " + allFirstPY);

				allPY = "JunLian";
				allFirstPY = "jl";

				continue;
			}

			City item = new City(province, city, number, allPY, allFirstPY);
			cityList.add(item);
		}

		return cityList;
	}


}
