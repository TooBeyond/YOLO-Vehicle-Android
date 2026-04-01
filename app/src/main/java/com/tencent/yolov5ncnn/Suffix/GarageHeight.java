package com.tencent.yolov5ncnn.Suffix;

public class GarageHeight {
    //判断小车的回传值
    public static char judge(byte []bytes){
        switch (bytes[4]){
            case 0x01 :
                return '1';
            case 0x02 :
                return '2';
            case 0x03 :
                return '3';
            case 0x04 :
                return '4';
            default:
                return '0';
        }
    }

}
