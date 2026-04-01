package com.tencent.yolov5ncnn.library;

import android.graphics.Bitmap;

import com.tencent.yolov5ncnn.tool.TFTRect;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecognizeShape {
    //浅蓝0、//黄色1、//品红2、//浅红色3、//蓝色4、//青色5、// 深红色6、//黑色7
    //暗 S、V=214,211     亮 S、V=176,160
    //浅蓝0、//黄色1、//品红2、//浅红色3、//蓝色4、//青色5、// 深红色6、//黑色7      车牌蓝底9  车牌绿底10
    public static double[][] HSV_VALUE_LOW = {
            {13,176,160},//浅蓝0  12,214,211
            {67, 176,160},//黄色1
            {130, 176,160},//品红2  暗：100, 176,160   亮：130,176,160
            {126,176,160},//浅红色3
            {0, 176,160},//蓝色4
            {30, 176,160},//青色5   35
            {103,176,160},// 深红色6
            {0,0,0},//黑色7   暗：0,187,0   亮：0,0,0
            {0,0,192},//标准蓝8
            {100,90,90}, //9 蓝色
            {115,90,90}, //10 紫色
            {0,0,0}, //11 黑色
            {160,90,90} //12 红色
    };

    public static double[][] HSV_VALUE_HIGH = {
            {30,255,255},//浅蓝0
            {111, 255,255},//黄色1
            {241, 255, 255.0},//品红2
            {150,255, 255},//浅红色3
            {12, 255, 255},//蓝色4
            {70, 255.0, 255},//青色5   90
            {150,255,255},// 深红色6
            {255,255,150},//黑色7   暗：28,255,184    亮：255,255,150
            {45,238,255},//标准蓝8
            {115,255,255}, //9 蓝色
            {130,255,255}, //10 紫色
            {180,255,90}, //11 黑色
            {179,255,255} //12 红色
    };
    public int[] recognize(Bitmap bitmap,int H){
        Mat srcMat,destMat,hsvMat;
        Bitmap resultBitmap;
        List<MatOfPoint> contours = new ArrayList<>();//存储轮廓的值
        int contoursCount; //保存轮廓的数量
        srcMat = new Mat();
        Utils.bitmapToMat(bitmap,srcMat);
        int[] arr=new int[30];
        destMat = new Mat();
        Bitmap b = new TFTRect().RectBitMap(srcMat);
        Utils.bitmapToMat(b,destMat);
        resultBitmap = Bitmap.createBitmap(destMat.width(),destMat.height(),Bitmap.Config.ARGB_8888);
        Imgproc.cvtColor(destMat,destMat,Imgproc.COLOR_BGR2RGB);//这是一个颜色转换 R-B
        //1.对图像二值化处理
        hsvMat = new Mat();
        Imgproc.cvtColor(destMat,hsvMat,Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat,new Scalar(HSV_VALUE_LOW[H]),new Scalar(HSV_VALUE_HIGH[H]),hsvMat);//h 160-179 红色
        //优化处理: 先进行开运算,在进行闭运算
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));//运算核 替换像素值的范围
        Imgproc.erode(hsvMat,hsvMat,kernel);
        Imgproc.erode(hsvMat,hsvMat,kernel);
        Imgproc.morphologyEx(hsvMat,hsvMat,Imgproc.MORPH_OPEN,kernel);//进行开运算
        Imgproc.morphologyEx(hsvMat,hsvMat,Imgproc.MORPH_CLOSE,kernel);//进行闭运算
        //2.轮廓绘制
        Mat outMat = new Mat();
        Imgproc.findContours(hsvMat,contours,outMat,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);//包括查找模式和标识符
        contoursCount = contours.size();
        System.out.println("轮廓数量: "+contoursCount);
        //将轮廓绘制出来,我们不在二值图上面绘制,因为效果不好呈现,选择在原图上面绘制
        Imgproc.drawContours(destMat,contours,-1,new Scalar(0,0,255),4);
        //3.图形判定
        MatOfPoint2f contour2f;
        MatOfPoint2f approxCurve;
        double epsilon;
        for(int i=0;i<contoursCount;i++){          //对每一个轮廓分别进行判断
            contour2f = new MatOfPoint2f(contours.get(i).toArray());
            epsilon = 0.04 * Imgproc.arcLength(contour2f,true);//多边形 顶点的阈值
            approxCurve = new MatOfPoint2f();
            Imgproc.approxPolyDP(contour2f,approxCurve,epsilon,true);//多边形拟核
            System.out.println("顶点: "+approxCurve.rows());
            if(H == 12){                                                                    //红色
                if(approxCurve.rows() == 3){
                    arr[0]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);  //改一下格式
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){//菱形的外接矩形是其真实面积的1.3倍以上
                        arr[2]++;
                    }else {
                        arr[1]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[3]++;
                }else if(approxCurve.rows() >=5){
                    arr[4]++;
                }
            }else if(H == 22){                                                              //黄色
                if(approxCurve.rows() == 3){
                    arr[5]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){
                        arr[7]++;
                    }else {
                        arr[6]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[8]++;
                }else if(approxCurve.rows() >=6){
                    arr[9]++;
                }
            }else if(H == 38){                                                              //绿色
                if(approxCurve.rows() == 3){
                    arr[10]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){
                        arr[12]++;
                    }else {
                        arr[11]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[13]++;
                }else if(approxCurve.rows() >=6){
                    arr[14]++;
                }
            }else if(H == 9){                                                              //蓝色
                if(approxCurve.rows() == 3){
                    arr[15]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){
                        arr[17]++;
                    }else {
                        arr[16]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[18]++;
                }else if(approxCurve.rows() >=6){
                    arr[19]++;
                }
            }else if(H == 10){                                                             //紫色
                if(approxCurve.rows() == 3){
                    arr[20]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){
                        arr[22]++;
                    }else {
                        arr[21]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[23]++;
                }else if(approxCurve.rows() >=6){
                    arr[24]++;
                }
            }else if(H == 11){                                                             //黑色
                if(approxCurve.rows() == 3){
                    arr[25]++;
                }else if(approxCurve.rows() == 4){
                    RotatedRect rectMap = Imgproc.minAreaRect(approxCurve);
                    //外接矩形面积
                    double CirRect_area = (rectMap.size.height) * (rectMap.size.width);
                    destMat.convertTo(destMat, CvType.CV_32F);
                    System.out.println("像素 "+destMat.size());
                    Rect r = Imgproc.boundingRect(contours.get(0));
                    //实际面积
                    double Real_Area = Math.abs(Imgproc.contourArea(contours.get(0)));
                    System.out.println("外接矩形的面积"+CirRect_area);
                    System.out.println("面积"+Real_Area);
                    if(CirRect_area > (1.3*Real_Area)){
                        arr[27]++;
                    }else {
                        arr[26]++;
                    }
                }else if(approxCurve.rows() == 10){
                    arr[28]++;
                }else if(approxCurve.rows() >=6){
                    arr[29]++;
                }
            }
        }
        return arr;
    }
    public static Mat bitmapToMap(Bitmap bitmap) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        return mat;
    }
}
