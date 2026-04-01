package com.tencent.yolov5ncnn.tool;

/**
 * 测试题,字符串除杂 字符串倒置
 */
public class StringChange {
    public static int StringToInt(String str){
        int res = 0;
        int count =0;
        char[] ch = str.toCharArray();
        char[] arr = new char[6];
        for(int i=0;i<ch.length;i++ ){
            if(ch[i] >= '0'&&ch[i] <= '9'){
                arr[count] = ch[i];
                count++;
            }
        }
        char[] result = new char[6];
        int k=5;
        for(int i=0;i<arr.length;i++){
            result[k]=arr[i];
            k--;
        }
        System.out.println();
        res = CharToInt(result[5]);
        return res;
    }
    public static int CharToInt(char ch){
        switch (ch){
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            default:
                return 0;

        }
    }
}
