package com.tencent.yolov5ncnn.AlgorithmUtil;

/**
 * 本工具类能够对一些数据类型进行转换
 * 1.String 是由char[] 组成的,所以 String可以很轻松地转换为char类型
 * 2.在char[]转换为String时,推荐使用 new String(char[])(构造)
 * 3.String 可以转换为 byte(字节)类型,不过在此之前需要先将String转为char类型,
 * 但是char转换为byte时,输出会是ascll值(通常需要加强制转换)
 * 4.无论是什么字符，在计算机中，其实也是以数字,所以，可以直接将一个（不超过char的范围的）数字赋值给一个char变量(int转char)
 * 5.学会巧用String.valueOf(a)函数
 *
 */
public class TypeChange {
    /**
     * 字符串转char[]
     * @param str
     * @return char[]
     */
    public static char[] StringToArr(String str){
        return str.toCharArray();
    }


    /**
     * String转ascll
     * @param s
     * @return int[]
     */
    public static int[] StringToAscll(String s){
        int[] arr = new int[s.length()];
        for(int i=0;i<arr.length;i++){
            arr[i] = s.charAt(i);
        }
        return arr;
    }

    /**
     * ascll转char
     * @param num
     * @return
     */
    public static char IntToChar(int num){
        return (char)num;
    }

    /**
     * String 转 byte[]
     * @param str
     * @return byte[]
     */
    public static byte[] StringToByte(String str){
        char[] chars = str.toCharArray();
        byte[] by = new byte[str.length()];
        for(char ch:chars) System.out.println(ch);
        for(int i=0;i<chars.length;i++){
            by[i] = (byte)chars[i];
        }
        return by;
    }


    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }


    /**
     * String 反转
     * @param str
     * @return String
     */
    public static String  StringSwap(String str){
        String[]s=str.split("");
        StringBuilder stringBuilder=new StringBuilder();
        for (int i=s.length-1;i>=0;i--){
            stringBuilder.append(s[i]);
            if (i!=0){
                stringBuilder.append(" "); //字符串拼接
            }
        }
        String string = stringBuilder.toString();
        return string;
    }



    /**
     * byte[]转int
     * @param bytes 需要转换成int的数组
     * @return int值
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }


    /**
     * byte[]转String
     * @param bytes
     * @return String
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1) {
                // 得到的一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


    /**
     * byte[] 转 String[]
     * @param bytes
     * @return String[]
     */
    public static String[] bytesToStrings(byte[] bytes) {
        String[] strings = new String[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            String temp = Integer.toHexString(bytes[i] & 0xff);
            if (temp.length() == 1) {
                strings[i] = '0' + temp;
            } else {
                strings[i] = temp;
            }
        }
        return strings;
    }
}
