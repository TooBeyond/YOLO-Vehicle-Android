package com.tencent.yolov5ncnn.library;

import android.util.Log;

import com.tencent.yolov5ncnn.init.SocketService;
/*
    用来给小车发送数据
 */
public class command {
    //Socket
    SocketService socketService = new SocketService();
    public static byte[] sendBuffer = new byte[8];
    //车牌发送值
    public byte[] LicensePlate(char[] data){
        sendBuffer = new byte[8];
        if(data[0]== '1' && data[1]== '1' && data[2]== '1' && data[3]== '1' && data[4]== '1' && data[5]== '1'){
            sendBuffer[0] = (byte) 0xFF;
            sendBuffer[7] = (byte) 0xF0;
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
            sendBuffer[3] = (byte) data[2];
            sendBuffer[4] = (byte) data[3];
            sendBuffer[5] = (byte) data[4];
            sendBuffer[6] = (byte) data[5];
        }else {
                sendBuffer[0] = (byte) 0xff;
                sendBuffer[7] = (byte) 0xf0;
                Log.e("TAG","车牌信息已发送");
                sendBuffer[1] = (byte) 0xA1;
                sendBuffer[2] = (byte) data[0];
                sendBuffer[3] = (byte) data[1];
                sendBuffer[4] = (byte) data[2];
                sendBuffer[5] = (byte) data[3];
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                socketService.send(sendBuffer);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendBuffer[1] = (byte) 0xA2;
                sendBuffer[2] = (byte) data[4];
                sendBuffer[3] = (byte) data[5];
                sendBuffer[4] = (byte) 0x00;
                sendBuffer[5] = (byte) 0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                socketService.send(sendBuffer);
        }
        return sendBuffer;
    }

    //二维码发送
    public byte[] Qr_send(int[] data){
        sendBuffer = new byte[8];
        sendBuffer[0] = (byte) 0xFF;
        sendBuffer[7] = (byte) 0xF0;
        if(data == null || data.length == 0) {
            socketService.send(sendBuffer);
            return sendBuffer;
        }else if(data.length == 1){
            sendBuffer[1] = (byte) data[0];
        }else if(data.length == 2){
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
        }else if(data.length == 3){
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
            sendBuffer[3] = (byte) data[2];
            System.out.println(1111);
        }else if(data.length == 4){
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
            sendBuffer[3] = (byte) data[2];
            sendBuffer[4] = (byte) data[3];
        }else if(data.length == 5){
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
            sendBuffer[3] = (byte) data[2];
            sendBuffer[4] = (byte) data[3];
            sendBuffer[5] = (byte) data[4];
        }else if(data.length == 6){
            sendBuffer[1] = (byte) data[0];
            sendBuffer[2] = (byte) data[1];
            sendBuffer[3] = (byte) data[2];
            sendBuffer[4] = (byte) data[3];
            sendBuffer[5] = (byte) data[4];
            sendBuffer[6] = (byte) data[5];
        }
        socketService.send(sendBuffer);
        return sendBuffer;
    }



    //RFID数值发送
    public byte[] RFID_send(char[] data){
        if(data.length==6){
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)data[0];
            sendBuffer[2] = (byte)data[1];
            sendBuffer[3] = (byte)data[2];
            sendBuffer[4] = (byte)data[3];
            sendBuffer[5] = (byte)data[4];
            sendBuffer[6] = (byte)data[5];
            sendBuffer[7] = (byte)0xbb;
            for(int i=0;i<2;i++)
            socketService.send(sendBuffer);
        }
        return sendBuffer;
    }


    //红绿灯发送值 A
    public byte[] TrafficLightsA(int traffic_res){
        if(traffic_res==1){
            //红灯
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0e;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x01;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }else if(traffic_res==2){
            //绿灯
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0e;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x02;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }else if(traffic_res==3){
            //黄灯,以及其他特殊情况
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0e;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x03;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }
        return sendBuffer;
    }


    //红绿灯发送值 B
    public byte[] TrafficLights(int traffic_res){
        if(traffic_res==1){
            //红灯
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0f;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x01;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }else if(traffic_res==2){
            //绿灯
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0f;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x02;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }else if(traffic_res==3){
            //黄灯,以及其他特殊情况
            sendBuffer[0] = (byte)0x55;
            sendBuffer[1] = (byte)0x0f;
            sendBuffer[2] = (byte)0x02;
            sendBuffer[3] = (byte)0x03;
            sendBuffer[4] = (byte)0x00;
            sendBuffer[5] = (byte)0x00;
            sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
            sendBuffer[7] = (byte)0xbb;
            socketService.send(sendBuffer);
        }
        return sendBuffer;
    }
    //交通标志物发送值
    public byte[] TrafficSign(int Maker_result){
        sendBuffer = new byte[8];
        switch (Maker_result) {
            case 1://禁止前进
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b; //tft_A
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x05;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;
            case 2://禁止通行
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b;
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x06;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;
            case 3://掉头
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b;
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x04;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;
            case 4://前进
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b;
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x01;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;
            case 5://左转
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b;
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x02;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;

            case 6:           //default、右转、其他都为右
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x0b;
                sendBuffer[2]= (byte)0x60;
                sendBuffer[3]= (byte)0x03;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
                sendBuffer[7]= (byte)0xbb;
                break;
            case 7:           //不是交通标志物
                sendBuffer[0]= (byte)0x55;
                sendBuffer[1]= (byte)0x00;
                sendBuffer[2]= (byte)0x00;
                sendBuffer[3]= (byte)0x00;
                sendBuffer[4]= (byte)0x00;
                sendBuffer[5]= (byte)0x00;
                sendBuffer[6]= (byte)0x00;
                sendBuffer[7]= (byte)0xbb;
            default:
                break;
        }
        socketService.send(sendBuffer);
        return sendBuffer;
    }
}
