package com.tencent.yolov5ncnn.library;

import static com.tencent.yolov5ncnn.AlgorithmUtil.TypeConversion.BinaryToHexString;

public class ZQZB {


//    //接收小车速度值
//                else if(receiveBuffer[2] == (byte) 0x03){
//        if(receiveBuffer[4] == (byte) 0x01){//左转速度
//            int speed = (char)receiveBuffer[3];
//            listAdd("小车当前左转速度"+speed);
//        }else if(receiveBuffer[4] == (byte) 0x02){//右转速度
//            int speed = (char)receiveBuffer[3];
//            listAdd("小车当前右转速度"+speed);
//        }else if(receiveBuffer[4] == (byte) 0x03){//循迹速度
//            int speed = (char)receiveBuffer[3];
//            listAdd("小车当前循迹速度"+speed);
//        }else if(receiveBuffer[4] == (byte) 0x04){//距离
//            int speed = (char)receiveBuffer[3];
//            listAdd("小车转弯前进距离"+(speed*10));
//        }
//    }
//





//    public static char Route_A[]= new char[5];//路线 横坐标
//    public static char Route_O[]= new char[5];//路线 纵坐标
//    public static char Abscissa ='0';//用来接收横坐标
//    public static char Ordinate ='0';//用来接收纵坐标

//    else if(receiveBuffer[2] == (byte) 0x09){
//        //主车路线
//        Route_A[0] = 'B';
//        Route_O[0] = '6';
//        Route_A[1] = 'D';
//        Route_O[1] = '6';
//        Route_A[2] = 'F';
//        Route_O[2] = '6';
//        Abscissa = (char)receiveBuffer[3];//接收 横 坐标
//        Ordinate = (char)receiveBuffer[4];//接收 纵 坐标
//        System.out.println("当前横坐标 "+Abscissa);
//        System.out.println("当前纵坐标 "+Ordinate);
//        String s=BinaryToHexString(receiveBuffer);
//        System.out.println("收到的数据:"+s);
//        for(int i=0;i < 3;i++){//横坐标遍历
//            if(Abscissa != Route_A[i]){ //将 当前 横坐标与 目标 横坐标进行比较
//                if((Abscissa - Route_A[i]) < 0){
//                    //用当前坐标(横)减去目标坐标(横),如果小于零,表示(横)右转
//                    if((Abscissa - Route_A[i]) == -1 || (Abscissa - Route_A[i]) ==-2 ){
//                        sendBuffer[0] = (byte) 0x55;
//                        sendBuffer[1] = (byte) 0xaa;
//                        sendBuffer[2] = (byte) 0x05;
//                        sendBuffer[3] = (byte) 0x00;
//                        sendBuffer[4] = (byte) 0x00;
//                        sendBuffer[5] = (byte) 0x00;
//                        sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                        sendBuffer[7] = (byte) 0xbb;
//                        socketService.send(sendBuffer);
//                        showByte(sendBuffer);
//                        listAdd("主车右转");
//                    }
//                    if(receiveBuffer[5] == 0x02){
//                        sendBuffer[0] = (byte) 0x55;
//                        sendBuffer[1] = (byte) 0xaa;
//                        sendBuffer[2] = (byte) 0x06;
//                        sendBuffer[3] = (byte) 0x00;
//                        sendBuffer[4] = (byte) 0x00;
//                        sendBuffer[5] = (byte) 0x00;
//                        sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                        sendBuffer[7] = (byte) 0xbb;
//                        socketService.send(sendBuffer);
//                        showByte(sendBuffer);
//                        listAdd("主车循迹");
//                    }
//                }else if((Abscissa - Route_A[i]) > 0){
//                    //用当前坐标(横)减去目标坐标(横),如果大于零,表示(横)左转
//                    if((Abscissa - Route_A[i]) == 1){
//                        sendBuffer[0] = (byte) 0x55;
//                        sendBuffer[1] = (byte) 0xaa;
//                        sendBuffer[2] = (byte) 0x04;
//                        sendBuffer[3] = (byte) 0x00;
//                        sendBuffer[4] = (byte) 0x00;
//                        sendBuffer[5] = (byte) 0x00;
//                        sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                        sendBuffer[7] = (byte) 0xbb;
//                        socketService.send(sendBuffer);
//                        showByte(sendBuffer);
//                        listAdd("主车左转");
//                    }
//                }
//            }else if(Ordinate != Route_O[i]){
//                if((Ordinate - Route_O[i]) < 0){
//                    //用当前坐标(纵)减去目标坐标(纵),如果小于零,表示(纵)后退
//                    if((Ordinate - Route_O[i]) == -1){
//                    }
//                }else if((Ordinate - Route_O[i]) > 0){
//                    //用当前坐标(纵)减去目标坐标(纵),如果大于零,表示(纵)前进
//                    if((Ordinate - Route_O[i]) == 1){
//                        sendBuffer[0] = (byte) 0x55;
//                        sendBuffer[1] = (byte) 0xaa;
//                        sendBuffer[2] = (byte) 0x06;
//                        sendBuffer[3] = (byte) 0x00;
//                        sendBuffer[4] = (byte) 0x00;
//                        sendBuffer[5] = (byte) 0x00;
//                        sendBuffer[6] = (byte) ((sendBuffer[2] + sendBuffer[3] + sendBuffer[4] + sendBuffer[5]) % 256);
//                        sendBuffer[7] = (byte) 0xbb;
//                        socketService.send(sendBuffer);
//                        showByte(sendBuffer);
//                        listAdd("主车前进");
//                    }
//                }
//            }
//        }
//
//    }





    //        imageView = (ImageView) findViewById(R.id.imageView);
//        Button buttonImage = (Button) findViewById(R.id.buttonImage);
//        buttonImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Intent i = new Intent(Intent.ACTION_PICK);
//                i.setType("image/*");
//                startActivityForResult(i, SELECT_IMAGE);
//            }
//        });
//
//        Button buttonDetect = (Button) findViewById(R.id.buttonDetect);
//        buttonDetect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                TextView t1 = (TextView) findViewById(R.id.lprtext);
//                String tt2 = "";
////                t1.setText(String.valueOf(tt2));
//                t1.setText(String.valueOf(tt2));
//                if (yourSelectedImage == null)
//                    return;
//                YoloV5Ncnn.Obj[] objects = plr.detect(yourSelectedImage, false);
//                new RecognizeLPR().showObjects(objects,bitmap);
//            }
//        });
//
//        Button buttonDetectGPU = (Button) findViewById(R.id.buttonDetectGPU);
//        buttonDetectGPU.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                TextView t1 = (TextView) findViewById(R.id.lprtext);
//                String tt2 = "";
////                t1.setText(String.valueOf(tt2));
//                t1.setText(String.valueOf(tt2));
//                if (yourSelectedImage == null)
//                    return;
//
//                YoloV5Ncnn.Obj[] objects = plr.detect(yourSelectedImage, true);
//                new RecognizeLPR().showObjects(objects,bitmap);
//            }
//        });








//    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 640;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
//                break;
//            }
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//
//        // Rotate according to EXIF
//        int rotate = 0;
//        try {
//            ExifInterface exif = new ExifInterface(getContentResolver().openInputStream(selectedImage));
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    rotate = 270;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    rotate = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    rotate = 90;
//                    break;
//            }
//        }
//        catch (IOException e) {
//            Log.e("MainActivity", "ExifInterface IOException");
//        }
//        Matrix matrix = new Matrix();
//        matrix.postRotate(rotate);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }





//在原图上显示
//    private void showObjects(YoloV5Ncnn.Obj[] objects) {
//        if (objects == null) {
//            im_Show.setImageBitmap(bitmap);
//            return;
//        }
//        // draw objects on bitmap
//        Bitmap rgba = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        final int[] colors = new int[] {
//                Color.rgb( 54,  67, 244),
//                Color.rgb( 99,  30, 233),
//                Color.rgb(176,  39, 156),
//                Color.rgb(183,  58, 103),
//                Color.rgb(181,  81,  63),
//                Color.rgb(243, 150,  33),
//                Color.rgb(244, 169,   3),
//                Color.rgb(212, 188,   0),
//                Color.rgb(136, 150,   0),
//                Color.rgb( 80, 175,  76),
//                Color.rgb( 74, 195, 139),
//                Color.rgb( 57, 220, 205),
//                Color.rgb( 59, 235, 255),
//                Color.rgb(  7, 193, 255),
//                Color.rgb(  0, 152, 255),
//                Color.rgb( 34,  87, 255),
//                Color.rgb( 72,  85, 121),
//                Color.rgb(158, 158, 158),
//                Color.rgb(139, 125,  96)
//        };
//        Canvas canvas = new Canvas(rgba);
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(4);
//        Paint textbgpaint = new Paint();
//        textbgpaint.setColor(Color.WHITE);
//        textbgpaint.setStyle(Paint.Style.FILL);
//        Paint textpaint = new Paint();
//        textpaint.setColor(Color.BLACK);
//        textpaint.setTextSize(26);
//        textpaint.setTextAlign(Paint.Align.LEFT);
//        for (int i = 0; i < objects.length; i++) {
//            paint.setColor(colors[i % 19]);
//            canvas.drawRect(objects[i].x, objects[i].y, objects[i].x + objects[i].w, objects[i].y + objects[i].h, paint);{
//                // draw filled text inside image
//                String text = objects[i].label + " " + objects[i].color + " = " + String.format("%.1f", objects[i].prob * 100) + "%";
//                float text_width = textpaint.measureText(text);
//                float text_height = - textpaint.ascent() + textpaint.descent();
//                float x = objects[i].x;
//                float y = objects[i].y - text_height;
//                if (y < 0)
//                    y = 0;
//                if (x + text_width > rgba.getWidth())
//                    x = rgba.getWidth() - text_width;
//                canvas.drawRect(x, y, x + text_width, y + text_height, textbgpaint);
//                canvas.drawText(text, x, y - textpaint.ascent(), textpaint);
//            }
//        }
//        imageView.setImageBitmap(rgba);
//    }
}
