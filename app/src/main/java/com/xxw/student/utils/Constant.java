package com.xxw.student.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * http公共访问url的统一获取
 * Created by DarkReal on 2016/6/25.
 */

public class Constant {

//    private static final String url="http://192.168.18.147:8080/xybserver/";
    private static final String url="http://192.168.11.59:8080/SchoolHelper/";
    private static final String localurl="http://127.0.0.1/SchoolHelper";//本地服务器地址(取文件用)


    public static String getLocalurl() {
        return localurl;
    }
    public static String getUrl() {
        return url;
    }
    public static ArrayList<String> gender_list;
    public static ArrayList<String> xueli_list;
    public static ArrayList<String> skill_list;
    public static ArrayList<String> workDatePerWeek;

    public static ArrayList<String> getSkill_list() {
        return skill_list;
    }

    public static void setSkill_list() {
        skill_list = new ArrayList<String>();
        skill_list.add("了解");
        skill_list.add("掌握");
        skill_list.add("熟练");
        skill_list.add("精通");
    }
    //性别PickerView初始化
    public static void setGender_list() {
        gender_list = new ArrayList<String>();
        gender_list.add("男");
        gender_list.add("女");
    }

    public static ArrayList<String> getGender_list() {
        return gender_list;
    }

    //学历选择PickerView初始化
    public static void setXueli_list() {
        xueli_list = new ArrayList<String>();
        xueli_list.add("专科");
        xueli_list.add("本科");
        xueli_list.add("硕士研究生");
        xueli_list.add("博士研究生");
        xueli_list.add("其他");
    }

    public static ArrayList<String> getXueli_list() {
        return xueli_list;
    }

    public static ArrayList<String> getWorkDatePerWeek() {
        return workDatePerWeek;
    }

    public static void setWorkDatePerWeek() {
        workDatePerWeek = new ArrayList<String>();
        workDatePerWeek.add("1");
        workDatePerWeek.add("2");
        workDatePerWeek.add("3");
        workDatePerWeek.add("4");
        workDatePerWeek.add("5");
        workDatePerWeek.add("6");
        workDatePerWeek.add("7");
    }
//    获取技能数组里面的次序
    public static int getNoforSkill(String value){
       return skill_list.indexOf(value);
    }

//    获取年龄
//    根据birth计算年龄
    public static int calculateAge(String Birth){
        String year = Birth.substring(0,4);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");//可以方便地修改日期格式
        String hehe = dateFormat.format( now );
        System.out.println(hehe);
        String nowYear = hehe.substring(0,4);
        Log.v("year", (Integer.parseInt(nowYear) - Integer.parseInt(year)) + "");
        return (Integer.parseInt(nowYear)- Integer.parseInt(year));
    }

}
