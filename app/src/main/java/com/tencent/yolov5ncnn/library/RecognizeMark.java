package com.tencent.yolov5ncnn.library;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 本类用于easydl训练模型,区分左右
 */
public class RecognizeMark {
    public static final String TAG = "Mark";
    public static String getPicturePixel(Bitmap bitmap) {
        Bitmap resultBitmap;
        Rect rect = new Rect(100, 50, 300, 300);//裁剪
        Mat disMat = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap, disMat);
        Mat srcMat = new Mat(disMat, rect);
        Imgproc.cvtColor(srcMat, srcMat, Imgproc.COLOR_RGB2HSV);//转换成HSV
        Core.inRange(srcMat, new Scalar(110, 43, 46), new Scalar(124, 255, 255), srcMat);//提取蓝色
        //去杂
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(srcMat, srcMat, Imgproc.MORPH_OPEN, kernel);//开运算去杂
        Imgproc.morphologyEx(srcMat, srcMat, Imgproc.MORPH_CLOSE, kernel);//闭运算去杂
        resultBitmap = Bitmap.createBitmap(srcMat.width(), srcMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(srcMat, resultBitmap);//Mat转换成Bitmap
        //压缩
        Matrix matrix = new Matrix();
        matrix.setScale(0.2f, 0.2f);
        resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(),
        resultBitmap.getHeight(), matrix, true);
        Log.i(TAG, resultBitmap.getWidth() + "*" + resultBitmap.getHeight());
        //区分交通标志物左转和右转
        int white = 0;//白色像素点
        int lastWhite = 0;//上一列白色像素点
        int count = 0;
        for (int i = 0; i < resultBitmap.getWidth(); i++)//row 540
        {
            for (int j = 0; j < resultBitmap.getHeight(); j++)//col 960
            {
                if (resultBitmap.getPixel(i, j) == -1) {
                    white++;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //" pixel"+resultBitmap.getPixel(i,j));
                //resultBitmap.setPixel(j,i,Color.argb(255, 255, 0, 0));
            }
            if (white > 5) {
                count++;
            }
            if (count >= 8) {
                return "左转";
            }
            if ((lastWhite - white) > 5) {
                return "右转";
            }
            Log.i(TAG, "白色像素点 " + white);
            lastWhite = white;
            white = 0;
        }
        return "左转";
    }
}
