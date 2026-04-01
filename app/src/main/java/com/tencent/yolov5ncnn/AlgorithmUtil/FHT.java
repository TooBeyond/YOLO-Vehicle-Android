package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第一次比赛第三模块烽火台解密
 * 凯撒加密
 */

public class FHT {
    public static int Hash(char ch){
        switch (ch){
            case 'A': return Integer.parseInt("01101100");
            case 'B': return Integer.parseInt("10001101");
            case 'C': return Integer.parseInt("10011110");
            case 'D': return Integer.parseInt("01011111");
            case 'E': return Integer.parseInt("01101010");
            case 'F': return Integer.parseInt("01111011");
            case 'G': return Integer.parseInt("01101011");
            case 'H': return Integer.parseInt("10011101");
            case 'I': return Integer.parseInt("01011110");
            case 'J': return Integer.parseInt("01101111");
            case 'K': return Integer.parseInt("01111010");
            case 'L': return Integer.parseInt("10001011");
            case 'M': return Integer.parseInt("10011100");
            case 'N': return Integer.parseInt("01011101");
            case 'O': return Integer.parseInt("10011111");
            case 'P': return Integer.parseInt("01111111");
            case 'Q': return Integer.parseInt("10001010");
            case 'R': return Integer.parseInt("10011011");
            case 'S': return Integer.parseInt("01011100");
            case 'T': return Integer.parseInt("01101101");
            case 'U': return Integer.parseInt("01111110");
            case 'V': return Integer.parseInt("10001111");
            case 'W': return Integer.parseInt("10011010");
            case 'X': return Integer.parseInt("01011011");
            case 'Y': return Integer.parseInt("10001100");
            case 'Z': return Integer.parseInt("01111101");
        }
        return -1;
    }

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

    public static char[] kasa(String str){
        char[] string = str.toCharArray();
        char[] ch = new char[6];
        for(int i=0;i<string.length;i++){
            if(string[i] == 'Y')  string[i] = '?';
            if(string[i] == 'Z')  string[i] = '>';
            if(string[i]>='>' && string[i]<='X'){
                ch[i] = (char) (str.charAt(i)+2);
            }
        }
        for(int i=0;i<ch.length;i++){
            if(ch[i] == '[') ch[i] = 'A';
            if(ch[i] == '\\') ch[i] ='B';
        }
        return ch;
    }

    public static String Regax(String string) {
        StringBuilder result = new StringBuilder();
        Pattern p = Pattern.compile("([A-Z])");
        Matcher m = p.matcher(string);
        while (m.find()) {
            result.append(m.group());
        }
        return result.toString();
    }
}
