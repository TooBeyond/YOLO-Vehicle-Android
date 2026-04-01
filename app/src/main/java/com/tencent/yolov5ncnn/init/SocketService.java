package com.tencent.yolov5ncnn.init;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketService extends Service {
    private final static String TAG = "SocketService";

    /*socket*/
    public static Socket socket;
    public static OutputStream outputStream;
    public static InputStream inputStream;
    byte[] receiveBuffer = new byte[8];
    public SocketService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*初始化socket*/
        initSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    /*初始化socket*/
    public void initSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                  //  socket = new Socket("192.168.26.254", 60000);
                //    socket = new Socket("192.168.53.254", 60000);
                   socket = new Socket("192.168.58.254", 60000);

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    //isConnected()只会判断首次是否连接，中途连接断开其值还是true
                    if (socket.isConnected()) {
                        Log.i(TAG, "run: socket已连接");
                        //开启接收线程
                        startReceiveThread();
                    }
                } catch (IOException e) {
                    Handler handlerThree = new Handler(Looper.getMainLooper());
                    handlerThree.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "socket连接失败", Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.i(TAG, "run: socket连接失败");
                }

            }
        }).start();
    }

    //接收数据
    private void startReceiveThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int len;
                while (socket != null && !socket.isClosed()) {//判断是否有连接
                    try {
                        if ((len = inputStream.read(receiveBuffer)) > 0) {
                            EventBus.getDefault().post(receiveBuffer);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public byte [] getReceiveBuffer(){
        return receiveBuffer;
    }

    /*发送数据*/
    public void send(final byte[] buffer) {
        if (socket != null && socket.isConnected()) {
            /*发送指令*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (outputStream != null) {
                            outputStream.write(buffer);
                            outputStream.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Log.i(TAG, "sendOrder: socket连接错误,请重试");
        }
    }

    //关闭
    public void releaseSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
            socket = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseSocket();
    }
}


