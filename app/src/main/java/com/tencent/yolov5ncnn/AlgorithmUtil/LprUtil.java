package com.tencent.yolov5ncnn.AlgorithmUtil;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LprUtil {
    //车牌识别结果处理
    public static char[] lprHandle(String string) {
        Log.i("车牌",string);
        char[] lastResult = {'1','1','1','1','1','1'};//默认值
        String rgex = ",";//车牌识别可能会出现两个结果，利用正则表达式分割
        String str=string;
        Pattern pattern = Pattern.compile(rgex);
        String[] result = pattern.split(str);//将分割出的片段赋值到result数组中
        //第一遍遍历寻找英文数字长度为6的车牌结果
        for(int i=0;i<result.length;i++) {
            System.out.println("车牌 result["+i+"] = "+result[i]);
            //取出英文数字字符
            result[i] = Regax(result[i]);
            System.out.println(result[i]);
            //判断最后长度是否为6
            if(result[i].length()==6) {
                System.out.println("格式正确");
                return result[i].toCharArray();//转换成char类型
            }
        }
        //第一遍寻找失败，因为识别结果可能存在最后识别加1的情况或最前面加字母的情况
        for(int i=0;i<result.length;i++) {
            //判断最后长度是否为6
            if(result[i].length()==7) {
                if(result[i].endsWith("1")) {
                    //去除最后一个字符
                    result[i] = result[i].substring(0, result[i].length()-1);
                }else {
                    //去除最前一个字符
                    result[i] = result[i].substring(1,result[i].length());
                }
                return result[i].toCharArray();//转换成char类型
            }
        }
        //第二遍寻找失败，返回识别后最后6个值
        for(int i=0;i<result.length;i++) {
            if(result[i].length()>7) {
                result[i] = result[i].substring(result[i].length() - 6);
                return result[i].toCharArray();//转换成char类型
            }

        }
        return lastResult;
    }


    //取出英文数字字符
    public static String Regax(String string) {
        StringBuilder result = new StringBuilder();
        Pattern p = Pattern.compile("\\w+");
        Matcher m = p.matcher(string);
        while (m.find()) {
            result.append(m.group());
        }
        return result.toString();
    }
}
