package com.tencent.yolov5ncnn.AlgorithmUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class qr_chinese {
    //二维码识别 汉字
    public static int QR_GetChinese(String str) {
        String chinese[] = {"富强民主","你好"};//可以随时更新词库
        int i = 0;
        String string = null;
        String content = str;
        String[] res = new String[str.length()];
        String regStr = "[\u0391-\uffe5]+";  //中文十六进制范围匹配
        Pattern compile = Pattern.compile(regStr);
        Matcher matcher = compile.matcher(content);
        while (matcher.find()) {
            res[i] = matcher.group();//将找到的放入res数组中
            i++;
        }
        String result = null;
        StringBuffer s = new StringBuffer();
        for (int j = 0; j < res.length; j++) {
            if (res[j] != null)
                s.append(res[j]);     //将前面找到的String[]拼接成StringBuffer
            result = s.toString();
        }
        System.out.println("除杂后的中文 "+result);
        for(int j =0;j < chinese.length;j++){  //进行循环匹配
            System.out.println(chinese[j]);
            if (result != null) {
                if (result.equals(chinese[j])) {
                    return j + 1;//匹配正确
                }
            }
        }
        return 0;//匹配错误
    }
}
