package com.xxw.student.shouye_detail.select_city.util;

import java.io.Serializable;


/**
 * 城市对象
 */
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	private String province; //省份，例如山东
	private String name; //城市名，例如北京
	private String number; //城市号码，例如101010100
	private String pinyin; //城市名称的拼音，例如beijing
	private String py; //城市名称拼音的缩写，例如bj

	public City() {
	}

	public City(String province, String city, String number, String allPY,
				String allFristPY) {
		super();
		this.province = province;
		this.name = city;
		this.number = number;
		this.pinyin = allPY;
		this.py = allFristPY;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	@Override
	public String toString() {
		return "City [province=" + province + ", name=" + name + ", number="
				+ number + ", pinyin=" + pinyin + ", py=" + py + "]";
	}

}
