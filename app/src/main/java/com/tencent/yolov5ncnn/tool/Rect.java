package com.tencent.yolov5ncnn.tool;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * 对TFT进行裁剪,效果还不错
 */
public class Rect {

    private Bitmap maxRectBitmap;
    public Bitmap RectBitmap(Bitmap bt){
        Mat image = new Mat();
        Utils.bitmapToMat(bt,image);
        if (!image.empty()) {
            // 转换图像为HSV颜色空间
            Mat hsvImage = new Mat();
            Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

            // 定义高亮部分的阈值范围
            Scalar lowerThreshold = new Scalar(0, 0, 200);
            Scalar upperThreshold = new Scalar(255, 255, 255);

            // 创建掩码
            Mat mask = new Mat();
            Core.inRange(hsvImage, lowerThreshold, upperThreshold, mask);

            // 将掩码应用于原始图像
            Mat result = new Mat();
            Core.bitwise_and(image, image, result, mask);

            // 转换图像为灰度
            Mat grayImage = new Mat();
            Imgproc.cvtColor(result, grayImage, Imgproc.COLOR_BGR2GRAY);

            // 二值化图像
            Mat binaryImage = new Mat();
            Imgproc.threshold(grayImage, binaryImage, 1, 255, Imgproc.THRESH_BINARY);

            // 查找连通组件
            Mat labels = new Mat();
            Mat stats = new Mat();
            Mat centroids = new Mat();
            int numLabels = Imgproc.connectedComponentsWithStats(binaryImage, labels, stats, centroids, 8);

            // 初始化最大矩形框的信息
            int maxArea = 0;
            org.opencv.core.Rect maxRect = null;

            // 遍历每个连通组件，获取最大的矩形框
            for (int i = 1; i < numLabels; i++) {
                int x = (int) stats.get(i, Imgproc.CC_STAT_LEFT)[0];
                int y = (int) stats.get(i, Imgproc.CC_STAT_TOP)[0];
                int w = (int) stats.get(i, Imgproc.CC_STAT_WIDTH)[0];
                int h = (int) stats.get(i, Imgproc.CC_STAT_HEIGHT)[0];
                int area = (int) stats.get(i, Imgproc.CC_STAT_AREA)[0];

                if (area > maxArea) {
                    maxArea = area;
                    maxRect = new org.opencv.core.Rect(x, y, w, h);
                }
            }

            // 映射最大矩形框到原图
            Mat maxRectMapped = null;
            if (maxRect != null) {
                maxRectMapped = new Mat(image, maxRect);
            }

            // 将Mat转换为Bitmap
            Bitmap originalBitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image, originalBitmap);

            maxRectBitmap = null;
            if (maxRectMapped != null) {
                maxRectBitmap = Bitmap.createBitmap(maxRectMapped.cols(), maxRectMapped.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(maxRectMapped, maxRectBitmap);
            }
            Log.e("运行完了","ddddddddddddddddddd");
        }
        return maxRectBitmap;
    }

}
