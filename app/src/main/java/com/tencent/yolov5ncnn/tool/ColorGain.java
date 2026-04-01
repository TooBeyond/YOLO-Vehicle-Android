package com.tencent.yolov5ncnn.tool;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * 此方法用于基础图片色彩增强,版本为1.0
 * 运行时间过久,不是万不得已,不建议使用
 */
public class ColorGain {
    public Mat colorEnhancement(Mat src, int filter) {
        Mat origImage = new Mat();
        src.copyTo(origImage);
        Mat simg = new Mat();

        if (origImage.channels() != 1) {
            Imgproc.cvtColor(origImage, simg, Imgproc.COLOR_BGR2GRAY);
        } else {
            return src;
        }

        long N = simg.rows() * simg.cols();
        int[] histoB = new int[256];
        int[] histoG = new int[256];
        int[] histoR = new int[256];

        for (int i = 0; i < 256; i++) {
            histoB[i] = 0;
            histoG[i] = 0;
            histoR[i] = 0;
        }

        for (int i = 0; i < simg.rows(); i++) {
            for (int j = 0; j < simg.cols(); j++) {
                double[] intensity = origImage.get(i, j);

                histoB[(int) intensity[0]]++;
                histoG[(int) intensity[1]]++;
                histoR[(int) intensity[2]]++;
            }
        }

        for (int i = 1; i < 256; i++) {
            histoB[i] = histoB[i] + filter * histoB[i - 1];
            histoG[i] = histoG[i] + filter * histoG[i - 1];
            histoR[i] = histoR[i] + filter * histoR[i - 1];
        }

        int vminB = 0;
        int vminG = 0;
        int vminR = 0;
        int s1 = 3;
        int s2 = 3;

        while (histoB[vminB + 1] <= N * s1 / 100) {
            vminB = vminB + 1;
        }
        while (histoG[vminG + 1] <= N * s1 / 100) {
            vminG = vminG + 1;
        }
        while (histoR[vminR + 1] <= N * s1 / 100) {
            vminR = vminR + 1;
        }

        int vmaxB = 255 - 1;
        int vmaxG = 255 - 1;
        int vmaxR = 255 - 1;

        while (histoB[vmaxB - 1] > (N - ((N / 100) * s2))) {
            vmaxB = vmaxB - 1;
        }
        if (vmaxB < 255 - 1) {
            vmaxB = vmaxB + 1;
        }
        while (histoG[vmaxG - 1] > (N - ((N / 100) * s2))) {
            vmaxG = vmaxG - 1;
        }
        if (vmaxG < 255 - 1) {
            vmaxG = vmaxG + 1;
        }
        while (histoR[vmaxR - 1] > (N - ((N / 100) * s2))) {
            vmaxR = vmaxR - 1;
        }
        if (vmaxR < 255 - 1) {
            vmaxR = vmaxR + 1;
        }

        for (int i = 0; i < simg.rows(); i++) {
            for (int j = 0; j < simg.cols(); j++) {
                double[] intensity = origImage.get(i, j);

                if (intensity[0] < vminB) {
                    intensity[0] = vminB;
                }
                if (intensity[0] > vmaxB) {
                    intensity[0] = vmaxB;
                }

                if (intensity[1] < vminG) {
                    intensity[1] = vminG;
                }
                if (intensity[1] > vmaxG) {
                    intensity[1] = vmaxG;
                }

                if (intensity[2] < vminR) {
                    intensity[2] = vminR;
                }
                if (intensity[2] > vmaxR) {
                    intensity[2] = vmaxR;
                }

                origImage.put(i, j, intensity);
            }
        }

        for (int i = 0; i < simg.rows(); i++) {
            for (int j = 0; j < simg.cols(); j++) {
                double[] intensity = origImage.get(i, j);
                intensity[0] = (intensity[0] - vminB) * 255 / (vmaxB - vminB);
                intensity[1] = (intensity[1] - vminG) * 255 / (vmaxG - vminG);
                intensity[2] = (intensity[2] - vminR) * 255 / (vmaxR - vminR);
                origImage.put(i, j, intensity);
            }
        }

        Mat blurred = new Mat();
        double sigma = 1;
        double threshold = 50;
        double amount = 1;
        Imgproc.GaussianBlur(origImage, blurred, new Size(), sigma, sigma);
        Mat lowContrastMask = new Mat();
        Core.absdiff(origImage, blurred, lowContrastMask);
        Core.inRange(lowContrastMask, new Scalar(0), new Scalar(threshold), lowContrastMask);
        Core.addWeighted(origImage, 1 + amount, blurred, -amount, 0, blurred);
        origImage.copyTo(blurred, lowContrastMask);
        Mat dst = new Mat();
        dst = blurred.clone();
        return dst;
    }

    // 将Mat转换为Bitmap
    public Bitmap matToBitmap(Mat mat) {
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }
}
