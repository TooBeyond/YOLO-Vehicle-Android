package com.tencent.yolov5ncnn.Suffix;
import static com.tencent.yolov5ncnn.AlgorithmUtil.TypeConversion.BinaryToHexString;

import com.tencent.yolov5ncnn.init.SocketService;

public class StringCal {
    char garageA ='0';//获取A车库高度
    char garageB ='0';//获取B车库高度
    //Socket
    SocketService socketService = new SocketService();
    public static byte[] sendBuffer = new byte[8];
    //字符串计算
    public byte[] Calculation(String QR_result){
        suffix cal = new suffix();
        int ExpResult = cal.Exp(QR_result,garageA,garageB);
        System.out.println("计算结果"+ExpResult);
        sendBuffer[0] = (byte) 0x55;
        sendBuffer[1] = (byte) 0x00;
        sendBuffer[2] = (byte) 0x00;
        sendBuffer[3] = (byte) 0x00;
        sendBuffer[4] = (byte) 0x00;
        sendBuffer[5] = (byte) 0x00;
        sendBuffer[6] = (byte) ExpResult;
        sendBuffer[7] = (byte) 0xbb;
        socketService.send(sendBuffer);
        return sendBuffer;
    }
    public void putGarageA(char garageA){
        this.garageA = garageA;
    }
    public void putGarageB(char garageB){
        this.garageB = garageB;
    }
}
