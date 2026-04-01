package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RFIDUtil {
    public static char[] StringTO(String str){
        char[] chars = new char[6];
        int l=0;
        for(int i=0;i<6;i++){
            if((str.charAt(i) >= 'A' && str.charAt(i)<='Z') || (str.charAt(i) >='0' && str.charAt(i)<= '9' )){
                l++;
            }
        }
        if(l>=6){
            char[] arr = new char[6];
            for(int i=0;i<str.length();i++){
                if((str.charAt(i) >= 'A' && str.charAt(i)<='Z') || (str.charAt(i) >='0' && str.charAt(i)<= '9' )){
                    arr[i] = (char) (str.charAt(i)+2);
                }
            }
            return arr;
        }
        return chars;
    }
}
