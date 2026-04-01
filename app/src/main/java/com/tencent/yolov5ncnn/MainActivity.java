package com.tencent.yolov5ncnn;
import static com.tencent.yolov5ncnn.AlgorithmUtil.FHT.Hash;
import static com.tencent.yolov5ncnn.AlgorithmUtil.FHT.kasa;
import static com.tencent.yolov5ncnn.AlgorithmUtil.LprUtil.lprHandle;
import static com.tencent.yolov5ncnn.AlgorithmUtil.QRUtil.getQrCodePro;
import static com.tencent.yolov5ncnn.AlgorithmUtil.QR_choose.choose;
import static com.tencent.yolov5ncnn.AlgorithmUtil.TypeConversion.BinaryToHexString;
import static com.tencent.yolov5ncnn.Suffix.suffix.Exp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.ai.edge.core.util.Util;
import com.bkrcl.control_car_video.camerautil.CameraCommandUtil;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.tencent.yolov5ncnn.AlgorithmUtil.FHT;
import com.tencent.yolov5ncnn.AlgorithmUtil.RFIDUtil;
import com.tencent.yolov5ncnn.Marking.Classifier;
import com.tencent.yolov5ncnn.Suffix.GarageHeight;
import com.tencent.yolov5ncnn.Suffix.suffix;
import com.tencent.yolov5ncnn.library.command;
import com.tencent.yolov5ncnn.Suffix.StringCal;
import com.tencent.yolov5ncnn.library.Parameter;
import com.tencent.yolov5ncnn.library.RGBLuminanceSource;
import com.tencent.yolov5ncnn.library.RecognizeLPR;
import com.tencent.yolov5ncnn.init.SearchService;
import com.tencent.yolov5ncnn.init.SocketService;
import com.tencent.yolov5ncnn.library.RecognizeModel;
import com.tencent.yolov5ncnn.library.RecognizeShape;
import com.tencent.yolov5ncnn.library.Recognize_lamp;
import com.tencent.yolov5ncnn.tool.BitmapUtil;
import com.tencent.yolov5ncnn.tool.ColorGain;
import com.tencent.yolov5ncnn.tool.Rect;
import com.tencent.yolov5ncnn.tool.StringChange;
import com.tencent.yolov5ncnn.tool.TextCharacter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zeng_Zhitao
 * xiwangnimenzaicituifanzhezuosishan
 */

public class MainActivity extends Activity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    public static final String A_S = "com.a_s";
    //标志物
    public static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.4f;
    private static final int SELECT_IMAGE = 1;
    private ImageView imageView;
    //控件
    Button bt_Lpr,bt_Marker,bt_Traffic,bt_Qr,bt_Graphical,bt_Start,btn_start;
    Button btn_camera_high,btn_camera_low;
    ImageView im_Show;
    ListView list_Tips;
    Button btn_qr_one,btn_qr_two,btn_skip;
    //Socket
    SocketService socketService = new SocketService();
    public static byte[] sendBuffer = new byte[8];
    public static byte[] receiveBuffer = new byte[8];
    public static byte[] receiveOther = new byte[8];
    public static byte[] RFID1 = new byte[10];
    public static byte[] RFID2 = new byte[10];
    //适配器：用于List View和数据的连通
    ArrayList<String> dataHisData = new ArrayList<String>();
    ArrayAdapter<String> adapterHisData = null;
    //图像处理
    Bitmap LPRBitmap = null;
    Bitmap bitmap = null;
    Bitmap yourSelectedImage = null;
    private Bitmap bip = null;
    private Bitmap bit = null;
    //实例化二维码库类
    QRCodeReader reader = new QRCodeReader();
    //多个二维码
    QRCodeMultiReader qrCodeMultiReader = new QRCodeMultiReader();
    String QR_result;
    int qr_nun = 0;
    //车牌识别
    private final PlateRecognition plr = new PlateRecognition();
    //FHT
    //存储RFID内容
    public static char[] RFID = new char[6];
    //摄像头
    CameraCommandUtil cameraCommandUtil;
    String cameraIP;
    BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            cameraIP = arg1.getStringExtra("IP");
        }
    };
    int redRect=0,redTri=0,redCircle=0,resDiamond=0,redPentagon=0,       //红色
            yellowRect=0,yellowTri=0,yellowCircle=0,yellowDiamond=0,yellowPentagon=0, //黄色
            greenRect=0,greenTri=0,greenCircle=0,greenDiamond=0,greenPentagon=0,    //绿色
            blueRect =0,blueTri=0,blueCircle=0,blueDiamond=0,bluePentagon=0,      //蓝色
            purpleRect=0,purpleTri=0,purpleCircle=0,purpleDiamond=0,purplePentagon=0,   //紫色or品色
            blackRect=0,blackTri=0,blackCircle=0,blackDiamond=0,blackPentagon=0;    //黑色
    private static String rfidStr1;
    private static String rfidStr2;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //申请权限
        initPermission();
        //控件初始化
        initView();
        //车牌初始化
        initLPR();
        //开启Socket服务
        Intent intent = new Intent(MainActivity.this, SocketService.class);
        startService(intent);
        //EventBus注册
        EventBus.getDefault().register(this);
        //初始化摄像头
        camera_Init();
        //接收小车消息,进行处理
        try {
            onGetMessage(socketService.getReceiveBuffer());
        } catch (InterruptedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static char garageA ='0';//获取A车库高度
    public static char garageB ='0';//获取B车库高度
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onGetMessage(final byte[] bytes) throws InterruptedException, UnsupportedEncodingException {
        receiveBuffer = bytes;
        //包头包尾
        if ((receiveBuffer[0] == (byte) 0xff) && (receiveBuffer[7] == (byte) 0xf0)) {
            //对收到的数据进行处理
            if(receiveBuffer[1] == (byte) 0xA1){
                final GarageHeight H = new GarageHeight();
                //获取A车库
                if(receiveBuffer[2] == (byte) 0x01){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("二维码结果: "+QR_result);
                            MainActivity.garageA = H.judge(bytes);
                            new StringCal().putGarageA(garageA);
                            System.out.println("A车库的高度"+garageA);
                        }
                    }).start();
                }else if(receiveBuffer[2] == (byte) 0x02){
                    //获取B车库
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.garageB = H.judge(bytes);
                            new StringCal().putGarageB(garageB);
                            System.out.println("B车库的高度"+garageB);
                        }
                    }).start();
                }else if(receiveBuffer[2] == (byte) 0x04){
                    //接收RFID内容
                    if(receiveBuffer[3] == (byte) 0xA1){
                        //接收RFID 第一位
                        RFID[0] = (char)receiveBuffer[4];
                        RFID[1] = (char)receiveBuffer[5];
                        RFID[2] = (char)receiveBuffer[6];
                    }else if(receiveBuffer[3] == (byte) 0xA2){
                        //接收RFID 第二位
                        RFID[3] = (char)receiveBuffer[4];
                        RFID[4] = (char)receiveBuffer[5];
                        RFID[5] = (char)receiveBuffer[6];
                        for(int i=0;i<RFID.length;i++)
                            System.out.println(i+" "+RFID[i]);
                        String RFID_str = new String(RFID);
                        System.out.println(RFID_str);
                        Thread.sleep(800);
                        //处理RFID
                        char[] data;
                        data = RFIDUtil.StringTO(RFID_str);
                        listAdd("RFID " + String.valueOf(data));
                       sendBuffer= new command().RFID_send(data);
                       showByte(sendBuffer);
                    }
                }
                else if(receiveBuffer[2] == (byte) 0x05){
                    //对获取到的值,进行运算
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendBuffer = new StringCal().Calculation(QR_result);
                            listAdd("计算结果已发送:");
                            showByte(sendBuffer);
                        }
                    }).start();
                }
            }
            /*-----------------------------------------分隔符----------------------------------------------*/

            if (receiveBuffer[1] == (byte) 0xA0) {       //对图像进行处理
                getBitmap();
                if (receiveBuffer[2] == (byte) 0x02) {
                    // 二维码
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始二维码识别");
                            QR_result = QRCode();//单个二维码识别
                            if (QR_result.isEmpty()){
                                Log.i(TAG, "二维码识别结果错误：");
                                return;
                            }
                            int Res = new suffix().Express(QR_result);
                                sendBuffer[1] = (byte)0x02;
                                sendBuffer[2] = (byte)0x00;
                                sendBuffer[3] = (byte)0x00;
                                sendBuffer[4] = (byte)0x00;
                                sendBuffer[5] = (byte)0x00;
                                sendBuffer[6] = (byte)0x00;
                                socketService.send(sendBuffer);
                            showByte(sendBuffer);
                        }
                    }).start();
                } else if (receiveBuffer[2] == (byte) 0x03) {
                    // 车牌识别
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始车牌识别");
                            String LPR = LPR();
                            char[] chs = lprHandle(LPR);
                            sendBuffer = new command().LicensePlate(chs);
                            showByte(sendBuffer);
                        }
                    }).start();
                } else if(receiveBuffer[2] == (byte) 0x07){
                    //红绿灯识别 A
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始A红绿灯识别(opencv)");
                            int traffic_res = 0;
                            try {
                                traffic_res = traffic_CV();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            sendBuffer = new command().TrafficLightsA(traffic_res);
                            showByte(sendBuffer);
                        }
                    }).start();
                }else if(receiveBuffer[2] == (byte) 0x05){
                    //红绿灯识别 B
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始B红绿灯识别(YoloV5)");
                            int traffic_res = 0;
                            traffic_res = traffic_YoloV5();
                            sendBuffer = new command().TrafficLights(traffic_res);
                            showByte(sendBuffer);
                        }
                    }).start();
                } else if (receiveBuffer[2] == (byte) 0x06) {
                    //交通标志物
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始交通标志物识别");
                            int Maker_result = marker();
                            //indexMarker 1:禁止前进   2:禁止通行   3:掉头   4:前进   5:左转   6:右转
                            if (Maker_result == 7){
                                sendBuffer[1] = (byte) 0x00;
                                sendBuffer[2] = (byte) 0x00;
                                sendBuffer[3] = (byte) 0x00;
                                sendBuffer[4] = (byte) 0x00;
                                sendBuffer[5] = (byte) 0x00;
                                sendBuffer[6] = (byte) 0x00;
                                socketService.send(sendBuffer);
                                listAdd("不是交通标志物");
                            }else{
                                byte b = 0x01;
                                if(Maker_result==4 || Maker_result==3)  b=0x01;
                                else if(Maker_result==5 || Maker_result==6)  b= 0x02;
                                else if(Maker_result==1 || Maker_result==2)  b= 0x03;
                                sendBuffer[1] = (byte) 0xBB;
                                sendBuffer[2] = b;
                                sendBuffer[3] = (byte) 0x00;
                                sendBuffer[4] = (byte) 0x00;
                                sendBuffer[5] = (byte) 0x00;
                                sendBuffer[6] = (byte) 0x00;
                                socketService.send(sendBuffer);
                                //sendBuffer = new command().TrafficSign(Maker_result);
                                listAdd("是交通标志物"+b);
                                showByte(sendBuffer);
                            }
                        }
                    }).start();
                }else if (receiveBuffer[2] == (byte) 0x04) {
//                    // 图形识别
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listAdd("开始图形识别");
                            shape();
                            sendBuffer[1] = (byte)0xee;
                            sendBuffer[2] = (byte) (redCircle+greenCircle+yellowCircle);
                            sendBuffer[3] = (byte) (redTri+greenTri+yellowTri);
                            sendBuffer[4] = (byte) 0x00;
                            sendBuffer[5] = (byte) 0x00;
                            sendBuffer[6] = (byte) 0x00;
                            for(int i =0;i<3;i++){//连续发三次
                                socketService.send(sendBuffer);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            listAdd("图形已识别");
                            showByte(sendBuffer);
                        }
                    }).start();
                }else if(receiveBuffer[2] == (byte) 0x09){
                    FHT fht = new FHT();
                    String s = FHT.Regax(QR_result);
                    char[] arr = kasa(s);
                    for(char ch : arr) System.out.println(ch);
                    sendBuffer[0] = (byte)0x55;
                    sendBuffer[1] = (byte)arr[1];
                    sendBuffer[2] = (byte)arr[2];
                    sendBuffer[3] = (byte)arr[3];
                    sendBuffer[4] = (byte)arr[4];
                    sendBuffer[5] = (byte)arr[5];
                    sendBuffer[6] = (byte)arr[6];
                    sendBuffer[7] = (byte)0xbb;
                }else if(receiveBuffer[2] == (byte) 0x10){  //发送RFID数据处理结果
                    listAdd("开始计算RFID");
                    int RFIDInt1 = suffix.Express(rfidStr1);
                    int RFIDInt2 = suffix.Express(rfidStr2);
                    int res = (RFIDInt1+RFIDInt2)%4;
                    listAdd("1:"+RFIDInt1+" 2:"+RFIDInt2+"计算结果: "+res);
                    sendBuffer[0] = (byte) 0x00;
                    sendBuffer[1] = (byte) res;
                    sendBuffer[2] = (byte) 0x00;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) 0x00;
                    socketService.send(sendBuffer);
                    showByte(sendBuffer);
                }
                else if(receiveBuffer[2] == (byte) 0x08){  //发送中文
                    //将字符串转为GB2312数组
                    String text = "张三你好";
                    byte[] arr = text.getBytes("GB2312");
                    if(receiveBuffer[3] == (byte) 0x01){
                        sendBuffer[0] = (byte)0x55;
                        sendBuffer[1] = (byte)0xa1;
                        sendBuffer[2] = (byte)arr[0];
                        sendBuffer[3] = (byte)arr[1];
                        sendBuffer[4] = (byte)arr[2];
                        sendBuffer[5] = (byte)arr[3];
                        sendBuffer[6] = (byte)0x00;
                        sendBuffer[7] = (byte)0xbb;
                        socketService.send(sendBuffer);
                        Log.e("第一次发送","  ");
                    }else if(receiveBuffer[3] == (byte) 0x02){
                        sendBuffer[0] = (byte)0x55;
                        sendBuffer[1] = (byte)0xa2;
                        sendBuffer[2] = (byte)arr[4];
                        sendBuffer[3] = (byte)arr[5];
                        sendBuffer[4] = (byte)arr[6];
                        sendBuffer[5] = (byte)arr[7];
                        sendBuffer[6] = (byte)0x00;
                        sendBuffer[7] = (byte)0xbb;
                        socketService.send(sendBuffer);
                        Log.e("第二次发送","  ");
                    }
                }
            }
        }
        else if((receiveBuffer[0] == (byte) 0x66) && (receiveBuffer[7] == (byte) 0xAA)){ //接收RFID
            if(receiveBuffer[1]==(byte) 0xA1){
                RFID1[0] = receiveBuffer[2];
                RFID1[1] = receiveBuffer[3];
                RFID1[2] = receiveBuffer[4];
                RFID1[3] = receiveBuffer[5];
                RFID1[4] = receiveBuffer[6];
            }else if(receiveBuffer[1]==(byte) 0xA2){
                RFID1[5] = receiveBuffer[2];
                RFID1[6] = receiveBuffer[3];
                RFID1[7] = receiveBuffer[4];
                RFID1[8] = receiveBuffer[5];
                RFID1[9] = receiveBuffer[6];
                rfidStr1 = new String(RFID1, StandardCharsets.UTF_8).trim();
                listAdd(rfidStr1);
            }else if(receiveBuffer[1]==(byte) 0xB1){
                RFID2[0] = receiveBuffer[2];
                RFID2[1] = receiveBuffer[3];
                RFID2[2] = receiveBuffer[4];
                RFID2[3] = receiveBuffer[5];
                RFID2[4] = receiveBuffer[6];
            }else if(receiveBuffer[1]==(byte) 0xB2){
                RFID2[5] = receiveBuffer[2];
                RFID2[6] = receiveBuffer[3];
                RFID2[7] = receiveBuffer[4];
                RFID2[8] = receiveBuffer[5];
                RFID2[9] = receiveBuffer[6];
                rfidStr2 = new String(RFID2, StandardCharsets.UTF_8).trim();
                listAdd(rfidStr2);
            }
        }
        else{
            for(int i=0;i<receiveBuffer.length;i++){receiveOther[i] = receiveBuffer[i];}
            String  res = BinaryToHexString(receiveOther);
            listAdd("其他消息"+res);
        }
    }


    @SuppressWarnings({"all"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Qr:
                //二维码
                Bitmap qr = BitmapFactory.decodeResource(this.getResources(),R.drawable.qr_test2);
                bitmap = qr;
                im_Show.setImageBitmap(bitmap);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //getBitmap();
                        String str = qRCode();
                        listAdd("二维码: "+str);
                    }
                }).start();
                break;
            case R.id.bt_Traffic:
                //红绿灯
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBitmap();
                        traffic_YoloV5();
                    }
                }).start();
                break;
            case R.id.bt_Lpr:
                //车牌
                Bitmap bt = BitmapFactory.decodeResource(this.getResources(),R.drawable.a1);
                Rect rect = new Rect();
                bitmap = bt;
                im_Show.setImageBitmap(bitmap);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        getBitmap();
                        String res= LPR();
                        listAdd(res);
                    }
                }).start();
                break;
            case R.id.bt_Graphical:
                //图形
                bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.img_2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        getBitmap();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                im_Show.setImageBitmap(bitmap);
                            }
                        });
                        TextCharacter textCharacter = new TextCharacter(bitmap);
                        String res = textCharacter.getText();
                        listAdd(res.toString());
                    }
                }).start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        getBitmap();
//                        shape();
//                    }
//                }).start();
                break;
            case R.id.bt_Marker:
                //交通标志物
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getBitmap();
                        try {
                            RecognizeModel judge = new RecognizeModel(getAssets());
                            List<Classifier.Recognition> res = judge.MarkingJudge(bitmap);
                            double d = res.get(0).getConfidence();  //置信度
                            listAdd(res.toString());
                            if(d <= 0.4 || res.size()>=2) {
                                listAdd("不是交通标志物");
                                return;
                            }
                            listAdd(res.get(0).getTitle());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.bt_Start:
                listAdd("比赛开始");
                break;
            case R.id.btn_Start:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendBuffer = new Parameter().parameter("开始");
                        showByte(sendBuffer);
                    }
                }).start();
                break;
            case R.id.btn_camera_high:
                /*** 更改协议,主要目的是* 选择内容*/
//                sendBuffer[0] = (byte)0x55;
//                sendBuffer[1] = (byte)0xaa;
//                sendBuffer[2] = (byte)0xf1;
//                sendBuffer[3] = (byte)0x00;
//                sendBuffer[4] = (byte)0x00;
//                sendBuffer[5] = (byte)0x00;
//                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                sendBuffer[7] = (byte)0xbb;
//                socketService.send(sendBuffer);
//                listAdd("已发送");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cameraCommandUtil.postHttp(cameraIP, 0, 1);  //上
                    }
                }).start();
                break;
            case R.id.btn_camera_low:
                /*** 更改协议,主要目的是* 选择内容*/
//                sendBuffer[0] = (byte)0x55;
//                sendBuffer[1] = (byte)0xaa;
//                sendBuffer[2] = (byte)0xf2;
//                sendBuffer[3] = (byte)0x00;
//                sendBuffer[4] = (byte)0x00;
//                sendBuffer[5] = (byte)0x00;
//                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                sendBuffer[7] = (byte)0xbb;
//                socketService.send(sendBuffer);
//                listAdd("已发送");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cameraCommandUtil.postHttp(cameraIP, 2, 1);  //下
                    }
                }).start();
                break;
            case R.id.btn_Qr_one:
                sendBuffer[0] = (byte)0x55;
                sendBuffer[1] = (byte)0xaa;
                sendBuffer[2] = (byte)0xf3;
                sendBuffer[3] = (byte)0x00;
                sendBuffer[4] = (byte)0x00;
                sendBuffer[5] = (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte)0xbb;
                socketService.send(sendBuffer);
                listAdd("已发送");
                break;
            case R.id.btn_QR_two:
                sendBuffer[0] = (byte)0x55;
                sendBuffer[1] = (byte)0xaa;
                sendBuffer[2] = (byte)0xf4;
                sendBuffer[3] = (byte)0x00;
                sendBuffer[4] = (byte)0x00;
                sendBuffer[5] = (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte)0xbb;
                socketService.send(sendBuffer);
                listAdd("已发送");
                break;
            case R.id.btn_skip:
                //跳转
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,MainAcitivity_parameter.class);
                startActivity(intent);
                break;
        }
    }

    //红绿灯识别(openCV)
    public int traffic_CV() throws InterruptedException {
        int indexTraffic;
        bip  = Recognize_lamp.convertToLight(bitmap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                im_Show.setImageBitmap(bip);
            }
        });
        Thread.sleep(1000);
        bit = Recognize_lamp.convertToBlack(bip);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                im_Show.setImageBitmap(bit);
            }
        });
        String result = Recognize_lamp.sort();
        System.out.println("红绿灯 "+result);
        listAdd("交通灯识别结果为:" + result);
        switch (result) {
            //indexTraffic 1:红   2:绿   3:黄
            case "红色":
                indexTraffic = 1;
                break;
            case "绿色":
                indexTraffic = 2;
                break;
            default://default、黄、其他都为黄
                indexTraffic = 3;
                break;
        }
        return indexTraffic;
    }

    //红绿灯识别(YoloV5)
    public int traffic_YoloV5() {
        try {
            int indexLamp;
            RecognizeModel judge = new RecognizeModel(getAssets());
            List<Classifier.Recognition> lamp_res = judge.TrafficJudge(bitmap);
            if(lamp_res.isEmpty()){return 4;}
            String lamp_result = lamp_res.get(0).getTitle();
            listAdd("红绿灯识别结果 "+lamp_result);
            switch (lamp_result) {
                case "red":
                    indexLamp = 1;
                    break;
                case "green":
                    indexLamp = 2;
                    break;
                default: // 黄灯
                    indexLamp = 3;
                    break;
            }
            return indexLamp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //车牌识别
    public String  LPR(){
        /**
         图片色彩增强
         //        ColorGain colorGain = new ColorGain();
         //        Mat des = new Mat();
         //        Mat src = new Mat();
         //        Utils.bitmapToMat(bitmap,src);
         //        des = colorGain.colorEnhancement(src,1);
         //        yourSelectedImage = colorGain.matToBitmap(des);
         */
        yourSelectedImage = bitmap;
        String LPRRes = "";
        try {
            YoloV5Ncnn.Obj[] objects = plr.detect(yourSelectedImage, false);
            String  color;
            String  lpr;
            for(int i=0;i<objects.length;i++){
                color = objects[i].color;
                lpr = objects[i].label;
                listAdd("第"+i+"个车牌"+color+lpr);
                    LPRRes = objects[i].label;
                    return LPRRes;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //这里可以直接改值
        return LPRRes;
    }
    //交通标志识别
    public int marker() {
        try {
            //indexMarker 1:禁止前进   2:禁止通行   3:掉头   4:前进   5:左转   6:右转
            int indexMarker;
            RecognizeModel judge = new RecognizeModel(getAssets());
            List<Classifier.Recognition> res = judge.MarkingJudge(bitmap);
            if(res.isEmpty()){
                listAdd("不是交通标志物");
                return 7;
            }
            double d = res.get(0).getConfidence();  //置信度
            listAdd(res.toString()+"交通标志物个数 "+ res.size());
            if(d <= 0.5 || res.size()>=2) {
                listAdd("不是交通标志物");
                return 7;
            }
            String result = res.get(0).getTitle();
            listAdd("交通标志识别结果 "+result);
                    switch (result) {
                        case "no_just_run":
                            indexMarker = 1;
                            break;
                        case "no_run":
                            indexMarker = 2;
                            break;
                        case "go_home":
                            indexMarker = 3;
                            break;
                        case "go_run":
                            indexMarker = 4;
                            break;
                        case "left":
                            indexMarker = 5;
                            break;
                        case "right":
                            indexMarker = 6;
                            break;
                        default: //不是交通标志物
                            indexMarker = 7;
                            break;
                    }
                    return indexMarker;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //图形识别
    public void shape(){
        int[] res;
        RecognizeShape recognizeShape = new RecognizeShape();
        res = recognizeShape.recognize(bitmap,12);                                        //红色
        redTri= res[0];
        redRect = res[1];
        resDiamond = res[2];
        redPentagon = res[3];
        redCircle = res[4];
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        res = recognizeShape.recognize(bitmap,1);                                       //黄色
        yellowTri= res[5];
        yellowRect = res[6];
        yellowDiamond = res[7];
        yellowPentagon = res[8];
        yellowCircle = res[9];
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        res = recognizeShape.recognize(bitmap,38);                                        //绿色
//        greenTri= res[10];
//        greenRect = res[11];
//        greenDiamond = res[12];
//        greenPentagon = res[13];
//        greenCircle = res[14];
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        res = recognizeShape.recognize(bitmap,9);                                         //蓝色
        blueTri= res[15];
        blueRect = res[16];
        blueDiamond = res[17];
        bluePentagon = res[18];
        blueCircle = res[19];
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        res = recognizeShape.recognize(bitmap,10);                                        //紫色
        purpleTri= res[20];
        purpleRect = res[21];
        purpleDiamond = res[22];
        purplePentagon = res[23];
        purpleCircle = res[24];
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        res = recognizeShape.recognize(bitmap,11);                                        //黑色
        blackTri= res[25];
        blackRect = res[26];
        blackDiamond = res[27];
        blackPentagon = res[28];
        blackCircle = res[29];
        System.out.println( "红色 三角 :"+ redTri+ "         " +"红色 矩形 :"+redRect+ "\n"
                +"红色 菱形 :"+resDiamond+"             " +"红色 五角星 :"+redPentagon+"\n"
                +"红色 圆形 :"+redCircle+"            " +"黄色 三角 :"+yellowTri+"\n"
                +"黄色 矩形 :"+yellowRect+"          " +"黄色 菱形 :"+yellowDiamond+"\n"
                +"黄色 五角星 :"+yellowPentagon+"            "+"黄色 圆形 :"+yellowCircle+"\n"
                +"绿色 三角 :"+greenTri+"            " +"绿色 矩形 :"+greenRect+"\n"
                +"绿色 菱形 :"+greenDiamond+"            " +"绿色 五角星 :"+greenPentagon+"\n"
                +"绿色 圆形 :"+greenCircle+"            " +"蓝色 三角 :"+blueTri+"\n"
                +"蓝色 矩形 :"+blueRect+"            " +"蓝色 菱形 :"+blueDiamond+"\n"
                +"蓝色 五角星 :"+bluePentagon+"            " +"蓝色 圆形 :"+blueCircle+"\n"
                +"紫色 三角 :"+purpleTri+"             "+"紫色 矩形 :"+purpleRect+"\n"
                +"紫色 菱形 :"+purpleDiamond+"            " +"紫色 五角星 :"+purplePentagon+"\n"
                +"紫色 圆形 :"+purpleCircle +"             "+"黑色 三角 :"+blackTri+"\n"
                +"黑色 菱形 :"+blackDiamond+"            " +"黑色 矩形 :"+blackRect+"\n"
                +"黑色 五角 :"+blackPentagon+"            " +"黑色 圆形 :"+ blackCircle);
    }

    //单二维码识别
    public String QRCode() {
        Result[] result;
        RGBLuminanceSource rSource = new RGBLuminanceSource(bitmap);
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rSource));// 图片预处理
            Map<DecodeHintType, String> hint = new HashMap<>();
            hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
            //多二维码
            result = qrCodeMultiReader.decodeMultiple(binaryBitmap,hint);
            String[] result_qr = new String[result.length];
            for(int i=0;i<result.length;i++){result_qr[i] = result[i].toString();}
            listAdd("二维码个数: "+qr_nun+" 二维码识别结果: " + result_qr[0]);
            return result_qr[0];
        } catch (Exception e) {
            e.printStackTrace();
            listAdd("二维码识别错误:");
            return "";
        }
    }


    //多二维码识别
    public String qRCode() {
        Result[] result;
        RGBLuminanceSource rSource = new RGBLuminanceSource(bitmap);
        try {
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rSource));// 图片预处理
            Map<DecodeHintType, String> hint = new HashMap<>();
            hint.put(DecodeHintType.CHARACTER_SET, "utf-8");
            //多二维码
            result = qrCodeMultiReader.decodeMultiple(binaryBitmap,hint);
//            result = reader.decode(binaryBitmap, hint);// 进行识别
            String[] result_qr = new String[result.length];
            for(int i=0;i<result.length;i++){result_qr[i] = result[i].toString();}
            System.out.println(qr_nun);
            if(qr_nun == 0){
                qr_nun = choose(result_qr);
            }
            listAdd("二维码长度"+ result_qr.length);
            //以下是用来手动选择二维码
            if(result_qr.length == 1){
                listAdd("二维码(1) :" + result_qr[0]);
                return result_qr[0];
            }
            if(result_qr.length == 2 && qr_nun == 0){
                listAdd("二维码(1) :" + result_qr[0] + "二维码(2) :" + result_qr[1]);
                return result_qr[0];
            }
            if(result_qr.length == 2 && qr_nun == 1){
                listAdd("二维码(1) :" + result_qr[0] + "二维码(2) :" + result_qr[1]);
                return result_qr[1];
            }
            //结束 可以修改
            listAdd("二维码识别结果: " + result_qr[qr_nun]);
            return result_qr[qr_nun];
        } catch (Exception e) {
            e.printStackTrace();
            listAdd("二维码识别错误:");
            return "";
        }
    }
    // 获取摄像头当前图片
    public void getBitmap() {
        bitmap = cameraCommandUtil.httpForImage(cameraIP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        im_Show.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
        dealPhoto(bitmap);
    }
    //处理并保存图像
    private File dealPhoto(Bitmap photo){
        FileOutputStream fileOutputStream = null;
        //图片的名称,已时间命名
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); //时间格式
        Date date = new Date(System.currentTimeMillis());   //当前时间
        String photoName = format.format(date);      //格式化名称
        //图片存放地址
        File saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); //保存到系统图库中
        File file = new File(saveDir,photoName+".jpg");  //在这个路径下生成这么一个文件生成这么一个文件
        try {
            fileOutputStream = new FileOutputStream(file);  //将本地图片读成流
            photo.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);   //保存图片到本地，100是压缩比率,表示100%压缩

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (photo != null && photo.isRecycled()){
                photo.recycle();   //释放内存
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    //往List_Tips添加内容
    private void listAdd(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataHisData.add(0, string);
                list_Tips.setAdapter(adapterHisData);
            }
        });
    }

    public void showByte(byte[] bytes) {
        String str = BinaryToHexString(bytes);
        listAdd("发送: "+str);
    }
    //摄像头初始化
    private void camera_Init() {
        // Broadcast注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(A_S);
        registerReceiver(myBroadcastReceiver, intentFilter);
        cameraCommandUtil = new CameraCommandUtil();
        search();
    }
    // 搜索摄像cameraIP进度条
    private void search() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SearchService.class);
        startService(intent);
    }
    //车牌初始化
    public void initLPR(){
        boolean ret_init = plr.init(getAssets());
        if (!ret_init) {
            Log.e("MainActivity", "plr Init failed");
        }
    }
    //opencv初始化
    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            System.out.println("opencv 加载失败");
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            System.out.println("opencv 加载成功");
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
    //openCV4Android 需要加载用到
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    //初始化控件
    public void initView() {
        bt_Lpr = (Button) findViewById(R.id.bt_Lpr);
        bt_Marker = (Button) findViewById(R.id.bt_Marker);
        bt_Traffic = (Button) findViewById(R.id.bt_Traffic);
        bt_Qr = (Button) findViewById(R.id.bt_Qr);
        bt_Graphical = (Button) findViewById(R.id.bt_Graphical);
        bt_Start = (Button) findViewById(R.id.bt_Start);
        im_Show = (ImageView) findViewById(R.id.im_Show);
        list_Tips = (ListView) findViewById(R.id.list_Tips);
        btn_camera_high = (Button) findViewById(R.id.btn_camera_high);
        btn_camera_low = (Button) findViewById(R.id.btn_camera_low);
        //路线开始
        btn_start = (Button) findViewById(R.id.btn_Start);
        btn_qr_one = (Button) findViewById(R.id.btn_Qr_one);
        btn_qr_two = (Button) findViewById(R.id.btn_QR_two);
        btn_skip = (Button) findViewById(R.id.btn_skip);

        adapterHisData = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataHisData);
        bt_Lpr.setOnClickListener(this);
        bt_Marker.setOnClickListener(this);
        bt_Traffic.setOnClickListener(this);
        bt_Qr.setOnClickListener(this);
        bt_Graphical.setOnClickListener(this);
        bt_Start.setOnClickListener(this);
        //路线开始
        btn_start.setOnClickListener(this);
        //摄像头高度
        btn_camera_high.setOnClickListener(this);
        btn_camera_low.setOnClickListener(this);
        //二维码选择
        btn_qr_one.setOnClickListener(this);
        btn_qr_two.setOnClickListener(this);
        btn_skip.setOnClickListener(this);

        sendBuffer[0] = (byte) 0xFF;
        sendBuffer[1] = (byte) 0xAA;
        sendBuffer[7] = (byte) 0xF0;
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
        };
        ArrayList<String> toApplyList = new ArrayList<String>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            try {
                if (requestCode == SELECT_IMAGE) {
                    LPRBitmap = new RecognizeLPR().decodeUri(selectedImage);
                    yourSelectedImage = LPRBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    imageView.setImageBitmap(LPRBitmap);
                }
            }
            catch (FileNotFoundException e) {
                Log.e("MainActivity", "FileNotFoundException");
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus注销
        EventBus.getDefault().unregister(this);
        //Broadcast注销
        unregisterReceiver(myBroadcastReceiver);
    }
}
