package com.tencent.yolov5ncnn.library;

import com.tencent.yolov5ncnn.MainActivity;
import com.tencent.yolov5ncnn.init.SocketService;

public class Parameter {
    //Socket
    SocketService socketService = new SocketService();
    public static byte[] sendBuffer = new byte[8];
    //小车调参
    public byte[] parameter(String str){
        switch (str){
            case "开始":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xff;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                break;
            case "距离-5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xBA;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("距离-5已发送");
                break;
            case "距离+5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xBB;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("距离+5已发送");
                break;
            case "循迹-5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xAB;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("循迹速度-5已发送");
                break;
            case "循迹+5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xAA;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("循迹速度+5已发送");
                break;
            case "右转-5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xB1;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("右转速度-5已发送");
                break;
            case "右转+5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xB0;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("右转速度+5已发送");
                break;
            case "左转-5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xC1;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("左转速度-5已发送");
                break;
            case "左转+5":
                sendBuffer[0] = (byte) 0x55;
                sendBuffer[1] = (byte) 0xaa;
                sendBuffer[2] = (byte) 0xC0;
                sendBuffer[3] = (byte) 0x00;
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7] = (byte) 0xbb;
                socketService.send(sendBuffer);
                System.out.println("左转速度+5已发送");
                break;
        }
        return sendBuffer;
    }
}
