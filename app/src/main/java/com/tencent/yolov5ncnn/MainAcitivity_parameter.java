package com.tencent.yolov5ncnn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.tencent.yolov5ncnn.init.SocketService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * 小车调参界面
 */
public class MainAcitivity_parameter extends Activity implements View.OnClickListener {
    //Socket
    SocketService socketService;
    public static byte[] sendBuffer = new byte[8];
    public static byte[] receive = new byte[8];
    ArrayList<String> dataHisData = new ArrayList<String>();
    ArrayAdapter<String> adapterHisData = null;
    ListView list_test;
     Button test_go;
     Button test_back;
     Button test_left;
     Button test_right;
     Button test_xunji;
     Button test_delay_xunji;
     Button test_left_45;
     Button test_right_45;
     Button test_go_add_100;
     Button test_go_sub_100;
     Button test_back_add_100;
     Button test_back_sub_100;
     Button test_left_add_5;
     Button test_left_sub_5;
     Button test_right_add_5;
     Button test_right_sub_5;
     Button test_xunju_add_5;
     Button test_xunju_sub_5;
     Button test_guaiwan_add_10;
     Button test_guaiwan_sub_10;
     Button test_delay_xunji_add_100;
     Button test_delay_xunji_sub_100;
     Button test_left_45_add_50;
     Button test_left_45_sub_50;
     Button test_right_45_add_50;
     Button test_right_45_sub_50;
     Button btn_car;
     Button btn_bicycle;
     Button btn_motor;
     Button btn_big_car;
    private Button btn_wish;
    private EditText et_wish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_acitivity_parameter);
        InitView();
        //开启Socket服务
        socketService = new SocketService();
        Intent intent = new Intent(MainAcitivity_parameter.this, SocketService.class);
        startService(intent);
        //EventBus注册
        EventBus.getDefault().register(this);
        onGetMessage(socketService.getReceiveBuffer());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onGetMessage(final byte[] bytes) {
        receive = bytes;
        if ((receive[0] == (byte) 0xff) && (receive[7] == (byte) 0xf0)) {
            if(receive[1] == (byte)0xA1){
                if(receive[2] == (byte)0x03){
                    if(receive[4] == (byte) 0x01){               //左转速度
                        int speed = (char)receive[3];
                        listAdd("小车当前左转速度"+speed);
                    }
                    else if(receive[4] == (byte) 0x02){         //右转速度
                        int speed = (char)receive[3];
                        listAdd("小车当前右转速度"+speed);
                    }
                    else if(receive[4] == (byte) 0x03){         //循迹速度
                        int speed = (char)receive[3];
                        listAdd("小车当前循迹速度"+speed);
                    }
                    else if(receive[4] == (byte) 0x04){         //转弯角度
                        int speed = (char)receive[3];
                        listAdd("小车转弯角度"+(speed*10));
                    }
                    else if(receive[4] == (byte) 0x05){         //前进距离
                        int speed = (char)receive[3];
                        System.out.println(receive[3]);
                        listAdd("小车前进距离"+(speed*100));
                    }
                    else if(receive[4] == (byte) 0x06){         //后退距离
                        int speed = (char)receive[3];
                        listAdd("小车后退距离"+(speed*100));
                    }
                    else if(receive[4] == (byte) 0x07){         //delay循迹距离
                        int speed = (char)receive[3];
                        listAdd("小车delay循迹距离"+(speed*100));
                    }else if(receive[4] == (byte) 0x08){         //左转角度
                        int speed = (char)receive[3];
                        listAdd("小车左转角度"+(speed*10));
                    }
                    else if(receive[4] == (byte) 0x09){         //右转角度
                        int speed = (char)receive[3];
                        listAdd("小车右转角度"+(speed*10));
                    }
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus注销
        EventBus.getDefault().unregister(this);
    }

    @SuppressWarnings({"all"})
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_wish:
                    String str =et_wish.getText().toString();
                    listAdd(str);
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) str.charAt(0);
                    sendBuffer[2] = (byte) str.charAt(1);
                    sendBuffer[3] = (byte) str.charAt(2);
                    sendBuffer[4] = (byte) str.charAt(3);
                    sendBuffer[5] = (byte) str.charAt(4);
                    sendBuffer[6] = (byte) str.charAt(5);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_go:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x02;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    System.out.println(new String(sendBuffer));
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_back:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x03;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x04;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x05;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_xunji:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x06;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_delay_xunji:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x07;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left_45:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x08;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right_45:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0x09;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_go_add_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD2;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_go_sub_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD3;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_back_add_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD4;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_back_sub_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD5;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left_add_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xC0;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left_sub_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xC1;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right_add_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xB0;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right_sub_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xB1;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_xunju_add_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xAA;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_xunju_sub_5:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xAB;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_guaiwan_add_10:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xBB;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_guaiwan_sub_10:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xBA;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_delay_xunji_add_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD6;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_delay_xunji_sub_100:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD7;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left_45_add_50:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD8;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_left_45_sub_50:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xD9;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right_45_add_50:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xE1;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
                case R.id.test_right_45_sub_50:
                    sendBuffer[0] = (byte) 0x55;
                    sendBuffer[1] = (byte) 0xaa;
                    sendBuffer[2] = (byte) 0xE2;
                    sendBuffer[3] = (byte) 0x00;
                    sendBuffer[4] = (byte) 0x00;
                    sendBuffer[5] = (byte) 0x00;
                    sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                    sendBuffer[7] = (byte) 0xbb;
                    socketService.send(sendBuffer);
                    break;
            }
    }

    //往List_Tips添加内容
    private void listAdd(final String string) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataHisData.add(0, string);
                list_test.setAdapter(adapterHisData);
            }
        });
    }

    public void InitView(){
        test_go = findViewById(R.id.test_go);
        test_back = findViewById(R.id.test_back);
        test_left = findViewById(R.id.test_left);
        test_right = findViewById(R.id.test_right);
        test_xunji = findViewById(R.id.test_xunji);
        test_delay_xunji = findViewById(R.id.test_delay_xunji);
        test_left_45 = findViewById(R.id.test_left_45);
        test_right_45 = findViewById(R.id.test_right_45);
        test_go_add_100 = findViewById(R.id.test_go_add_100);
        test_go_sub_100 = findViewById(R.id.test_go_sub_100);
        test_back_add_100 = findViewById(R.id.test_back_add_100);
        test_back_sub_100 = findViewById(R.id.test_back_sub_100);
        test_left_add_5 = findViewById(R.id.test_left_add_5);
        test_left_sub_5 = findViewById(R.id.test_left_sub_5);
        test_right_add_5 = findViewById(R.id.test_right_add_5);
        test_right_sub_5 = findViewById(R.id.test_right_sub_5);
        test_xunju_add_5 = findViewById(R.id.test_xunju_add_5);
        test_xunju_sub_5 = findViewById(R.id.test_xunju_sub_5);
        test_guaiwan_add_10 = findViewById(R.id.test_guaiwan_add_10);
        test_guaiwan_sub_10 = findViewById(R.id.test_guaiwan_sub_10);
        test_delay_xunji_add_100 = findViewById(R.id.test_delay_xunji_add_100);
        test_delay_xunji_sub_100 = findViewById(R.id.test_delay_xunji_sub_100);
        test_left_45_add_50 = findViewById(R.id.test_left_45_add_50);
        test_left_45_sub_50 = findViewById(R.id.test_left_45_sub_50);
        test_right_45_add_50 = findViewById(R.id.test_right_45_add_50);
        test_right_45_sub_50 = findViewById(R.id.test_right_45_sub_50);
        btn_wish = findViewById(R.id.btn_wish);
        et_wish = findViewById(R.id.et_wish);

        btn_car = findViewById(R.id.btn_car);
        btn_bicycle = findViewById(R.id.btn_bicycle);
        btn_motor = findViewById(R.id.btn_motor);
        btn_big_car = findViewById(R.id.btn_big_car);
        list_test = findViewById(R.id.list_test);

        adapterHisData = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataHisData);
        test_go.setOnClickListener(this);
        test_back.setOnClickListener(this);
        test_left.setOnClickListener(this);
        test_right.setOnClickListener(this);
        test_xunji.setOnClickListener(this);
        test_delay_xunji.setOnClickListener(this);
        test_left_45.setOnClickListener(this);
        test_right_45.setOnClickListener(this);
        test_go_add_100.setOnClickListener(this);
        test_go_sub_100.setOnClickListener(this);
        test_back_add_100.setOnClickListener(this);
        test_back_sub_100.setOnClickListener(this);
        test_left_add_5.setOnClickListener(this);
        test_left_sub_5.setOnClickListener(this);
        test_right_add_5.setOnClickListener(this);
        test_right_sub_5.setOnClickListener(this);
        test_xunju_add_5.setOnClickListener(this);
        test_xunju_sub_5.setOnClickListener(this);
        test_guaiwan_add_10.setOnClickListener(this);
        test_guaiwan_sub_10.setOnClickListener(this);
        test_delay_xunji_add_100.setOnClickListener(this);
        test_delay_xunji_sub_100.setOnClickListener(this);
        test_left_45_add_50.setOnClickListener(this);
        test_left_45_sub_50.setOnClickListener(this);
        test_right_45_add_50.setOnClickListener(this);
        test_right_45_sub_50.setOnClickListener(this);
        btn_wish.setOnClickListener(this);

        btn_car.setOnClickListener(this);
        btn_motor.setOnClickListener(this);
        btn_big_car.setOnClickListener(this);
        btn_bicycle.setOnClickListener(this);
    }
}