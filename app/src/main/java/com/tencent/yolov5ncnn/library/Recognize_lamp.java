package com.tencent.yolov5ncnn.library;

import android.graphics.Bitmap;
import android.util.Log;

public class Recognize_lamp {
    public static int[] colorNum = new int[3];
    /*
     * 第一步,像素处理背景变为黑色
     * */
    public static Bitmap convertToLight(Bitmap bit){
        int width = bit.getWidth();
        int height = bit.getHeight();
        int [] pixels = new int[width * height];
        bit.getPixels(pixels,0,width,0,0,width,height);//获取图像的每一个像素点
        int [] p1 = new int[bit.getWidth() * bit.getHeight()];
        for(int y = 0;y<height;y++){
            int offset = y * width;
            for(int x = 0;x < width;x++){
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int bright = (int)(0.3*r + 0.59*g + 0.11*b);
                if(bright<330/2){//阈值,小于阈值的像素直接置为黑色,256/2
                    p1[offset +x] = 0xff000000;
                }
                else {
                    p1[offset + x] = pixel;
                }
            }
        }
        Bitmap result = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        //把颜色值赋给新建的图片
        result.setPixels(p1,0,width,0,0,width,height);
        return result;
    }

    public static Bitmap convertToBlack(Bitmap bip){
        int width = bip.getWidth();
        int height = bip.getHeight();
        int []pixels = new int[width * height];
        bip.getPixels(pixels,0,width,0,0,width,height);
        int []p1 = new int[bip.getHeight() * bip.getWidth()];
        for(int y = 0;y < height;y++){
            int offset = y * width;
            for(int x = 0; x < width; x++){
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                p1[offset+x]=pixel;
                if(r > 240 && b <220 && g<220){
                    colorNum[0]++;                //红色
                } else if(r < 220 && b < 220 && g > 240){
                    colorNum[1]++;                //绿色
                }if(r > 240 && g > 240 && b < 220){
                    colorNum[2]++;               //黄色
                }
            }
        }
        Bitmap result = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        result.setPixels(p1,0,width,0,0,width,height);
        return result;
    }

    /*
     * 排序
     * */
    public static String sort() {
        Log.e("TAG", "colorNum[0]" + colorNum[0] + ",colorNum[1]" + colorNum[1] + ",colorNum[2]" + colorNum[2]);
        String result = (colorNum[0] > colorNum[1] && colorNum[0] > colorNum[2]) ? "红色" :
                (colorNum[1] > colorNum[0] && colorNum[1] > colorNum[2]) ? "绿色" : "黄色";
        for (int i = 0; i < 3; i++) {
            colorNum[i] = 0;
        }
        return result;
    }
}
