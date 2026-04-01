package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QRUtil {
    public static char[] getQrCodePro(String string) {
        char[] lastResult = {'2','2','2','2','2','2'};//默认值
        int j = 0;
        String str=string;
        String rgex = ",";
        Pattern pattern = Pattern.compile(rgex);
        String[] result = pattern.split(str);//将分割出的片段赋值到result数组中
        int[] ret = new int[6];
        String res = string;
        for(int i=0;i<result.length;i++) {
            System.out.println("result["+i+"] = "+result[i]);
            result[i] = Regax(result[i]);//取字符
            System.out.println(result[i]);
            //判断最后长度是否为6
            //if(result[i].length()==10) {
                return result[i].toCharArray();   //转换成char类型
            //}
        }
        return lastResult;
    }

    public static String Regax(String string) {
        StringBuilder result = new StringBuilder();
//        Pattern p = Pattern.compile("([0-9])|([A-Z])");
        Pattern p = Pattern.compile("([0-9])");
        Matcher m = p.matcher(string);
        while (m.find()) {
            result.append(m.group());
        }
        return result.toString();
    }
}
