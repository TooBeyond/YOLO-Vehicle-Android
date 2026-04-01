package com.tencent.yolov5ncnn.tool;

//排序
public class SortUtil {

    //int[]升序排列
    public static int[] ascInt(int[] value) {
        int t;
        for(int i=value.length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(value[j+1]<value[j]){
                    t=value[j];
                    value[j]=value[j+1];
                    value[j+1]=t;
                }
            }
        }
        return value;

    }

    //int[]降序排列
    public static int[] descInt(int[] value) {
        int t;
        for(int i=value.length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(value[j+1]>value[j]){
                    t=value[j];
                    value[j]=value[j+1];
                    value[j+1]=t;
                }
            }
        }
        return value;

    }

    //double[]升序排列
    public static double[] ascDouble(double[] value) {
        double t;
        for(int i=value.length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(value[j+1]<value[j]){
                    t=value[j];
                    value[j]=value[j+1];
                    value[j+1]=t;
                }
            }
        }
        return value;

    }

    //double[]降序排列
    public static double[] descDouble(double[] value) {
        double t;
        for(int i=value.length-1;i>0;i--){
            for(int j=0;j<i;j++){
                if(value[j+1]>value[j]){
                    t=value[j];
                    value[j]=value[j+1];
                    value[j+1]=t;
                }
            }
        }
        return value;
    }
}
