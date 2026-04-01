package com.tencent.yolov5ncnn.library;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.tencent.yolov5ncnn.Marking.Classifier;
import com.tencent.yolov5ncnn.Marking.DetectorFactory;
import com.tencent.yolov5ncnn.Marking.YoloV5Classifier;

import java.io.IOException;
import java.util.List;

public class RecognizeModel {
    private final AssetManager assertManaer;

    private YoloV5Classifier detector;

    public RecognizeModel(AssetManager manager) {
        this.assertManaer = manager;
    }
    public Bitmap imageScale(Bitmap bitmap, int new_w, int new_h) throws Exception{
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) new_w) / src_w;
        float scale_h = ((float) new_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w,scale_h);
        Bitmap re_bmp = Bitmap.createBitmap(bitmap,0,0,src_w,src_h,matrix,true);
        return re_bmp;
    }

    public List<Classifier.Recognition> MarkingJudge(Bitmap bitmap) throws Exception {
        bitmap = imageScale(bitmap,320,320);
        try {
            detector = DetectorFactory.getDetector(this.assertManaer,"marking.tflite");
            detector.useCPU();
            detector.setNumThreads(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开始识别
        return detector.recognizeImage(bitmap);
    }

    public List<Classifier.Recognition> TrafficJudge(Bitmap bitmap) throws Exception {
        bitmap = imageScale(bitmap,320,320);
        try {
            detector = DetectorFactory.getDetector(this.assertManaer,"traffic.tflite");
            detector.useGpu();
            detector.setNumThreads(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开始识别
        return detector.recognizeImage(bitmap);
    }
}

