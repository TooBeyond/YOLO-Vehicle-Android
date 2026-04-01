package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.ArrayList;

public class QR_choose {
    public static int choose(String[] str){
        ArrayList<Character> characters = new ArrayList<>();
        for(int i=0;i<str.length;i++){
            char[] arr = str[i].toCharArray();
            for(int j=0;j<arr.length;j++){
                //匹配二维码中的内容
                if((arr[j]>'0' && arr[j]<'9') || (arr[j]>'a' && arr[j]<'z')){
                    characters.add(arr[j]);
                }
            }
            if(characters.size() > 1) return i;
        }
        return 0;
    }

/**
 * 此方法运用到了ascll除杂,从而做到对二维码的选择
 * 另外,ArrayList的拼接可以用于参考
  */

//    public static String choose(String[] str){
//        ArrayList<Character> characters = new ArrayList<>();
//        for(int i=0;i<str.length;i++){
//            char[] arr = str[i].toCharArray();
//            for(int j=0;j<arr.length;j++){
//                if((arr[j]>'0' && arr[j]<'9') || (arr[j]>'a' && arr[j]<'z')){
//                    characters.add(arr[j]);
//                }
//            }
//            if(characters.size() > 0){
//                StringBuffer sf = new StringBuffer();
//                String s = String.valueOf(characters);
//                for(int j=0; j<s.length();j++){
//                    if(s.charAt(j) == '[' || s.charAt(j) == ']' || s.charAt(j) == ',' || s.charAt(j) == ' ') continue;
//                    sf.append(s.charAt(j));
//                }
//                return sf.toString();
//            }
//        }
//        return "";
//    }
}
