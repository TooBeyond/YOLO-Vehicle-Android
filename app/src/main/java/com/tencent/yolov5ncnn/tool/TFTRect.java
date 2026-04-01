package com.tencent.yolov5ncnn.tool;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class TFTRect {
    public static Bitmap RectBitMap(Mat srcMat){
        Mat destMat = new Mat();
        Imgproc.cvtColor(srcMat,destMat,Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(destMat,destMat,180,255,Imgproc.THRESH_BINARY);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(1,1));//运算核 替换像素值的范围
        Imgproc.morphologyEx(destMat,destMat,Imgproc.MORPH_CLOSE,kernel);
        Rect bound = Imgproc.boundingRect(destMat);
        List<MatOfPoint> count = new ArrayList<>();
        Imgproc.findContours(destMat,count,new Mat(),Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Mat result = null;
        for(MatOfPoint val : count) {
            Rect rect = Imgproc.boundingRect(val);
            if(Imgproc.contourArea(val) < 5000){
                continue;
            }
            int x = rect.x;
            int y = rect.y;
            int w = rect.width;
            int h = rect.height;
            Rect targetRect = new Rect(x, y, w, h);
            result = new Mat(srcMat, targetRect);
        }
        Bitmap res = null;
        if (result != null) {
            res = Bitmap.createBitmap(result.width(),result.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(result,res);
            return res;
        }else {
            res = Bitmap.createBitmap(srcMat.width(),srcMat.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(srcMat,res);
            return res;
        }
    }
}
