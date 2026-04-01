package com.tencent.yolov5ncnn.tool;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions;
import com.tencent.yolov5ncnn.MainActivity;
import com.tencent.yolov5ncnn.init.SocketService;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 文本识别
 */
public class TextCharacter {
    Bitmap bmp;
    TextRecognizer rzer;
    String result ="";
    SocketService socketService = new SocketService();
    public static int num =0;
    public TextCharacter(Bitmap bmp) {
        this.bmp = bmp;
        rzer = TextRecognition.getClient(new ChineseTextRecognizerOptions.Builder().build());
    }

    public String getText() {
        InputImage image = InputImage.fromBitmap(bmp,0);
        Task<Text> res = rzer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(text);
//                        byte[] msg = new byte[8];
//                        for(int i=0;i<sb.length()&&i<6;i++){
//                            msg[i+1] = (byte)sb.charAt(i);
//                        }
                        String result = sb.toString();
                        System.out.print("文字识别结果: "+result);
                        Log.e("  ","识别成功");
                       // for (byte b : msg) System.out.println(b);//中文怎么传给底层
                      //  socketService.send(msg);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return "success";
    }
}
