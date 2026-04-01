package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.Stack;

/**
 * 本工具类提供了各种进制之间的转换
 * String可以转换为char[],通过hashmap对应,能过将char[]转换为int
 * int 转 String 同理
 */

public class TypeConversion {
    //二进制到十六进制字符串

    /**
     * 能够把底层发送的数据转为16进制字符串
     * @param bytes
     * @return String
     */
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }


    /**
     * 十进制转二进制
     * @param num
     * @return String
     */
    public static String ToBinary(int num){
        String a = "";  //用字符串拼接
        while (num != 0) {  //利用十进制转二进制除2法
            a = num % 2 + a;
            num = num / 2;
        }
        return a;
    }


    /**
     *  十进制转八进制
     * @param n
     * @return int
     */
    public static int DexToOct(int n){
        int x;
        Stack<Integer> stack = new Stack<>();
        while(n>0){
            x=n%8;
            stack.push(x);
            n/=8;
        }
        int num = 0;
        while(!stack.empty()){
            num = stack.pop();
        }
        return num;
    }


    /**
     *将数字转化为十六进制
     * @param num
     * @return String
     */
    public static String toHex(int num) {
        if (num == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i --) {
            int val = (num >> (4 * i)) & 0xf;
            if (sb.length() > 0 || val > 0) {
                char digit = val < 10 ? (char) ('0' + val) : (char) ('a' + val - 10);
                sb.append(digit);
            }
        }
        return sb.toString();
    }


    /**
     * 二进制转十进制
     * @param n
     * @return int
     */
    public static int BinaryToOct(int n){
        int decimal=0,p=0;
        while(n!=0)
        {
            decimal+=((n%10)*Math.pow(2,p));
            n=n/10;
            p++;
        }
        return decimal;
    }


    /**
     * 2进制字符串转16进制字符串
     * @param binary
     * @return String
     */
    public static String binaryStringToHexString(String binary) {
        Integer temp = Integer.valueOf(binary, 2);
        String result = Integer.toHexString(temp);
        return result;
    }


    /**
     * 16进制字符串转10进制
     * @param hex
     * @return Integer
     */
    public static Integer hexStringToDecimal(String hex) {
        Integer result = Integer.valueOf(hex, 16);
        return result;
    }


    /**
     * 16进制字符串转2进制字符串
     * @param hex
     * @return String
     * 注意10不是16进制
     */
    public static String hexStringToBinaryString(String hex) {
        Integer temp = Integer.valueOf(hex, 16);
        String result = Integer.toBinaryString(temp);
        return result;
    }

}
