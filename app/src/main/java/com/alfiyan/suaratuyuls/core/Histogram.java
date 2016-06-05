package com.alfiyan.suaratuyuls.core;

import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by Alfiyan on 09-Jul-15.
 */
public class Histogram {
    public int[] h = new int[361];

    public Histogram(Bitmap data){
        for(int i=0;i < 361; i++){
           h[i] = 0;
        }

        for(int x = 0; x < data.getWidth();x++){
            for(int y = 0; y < data.getHeight(); y++){
                int w = data.getPixel(x,y);
                int hue = RGB2HSV.convertS(w);
                if(hue < 0){
                    hue = 360+hue;
                }
                h[hue] = h[hue]+1;
            }
        }
    }

    public int[][] getTop(int c){
        int[][] top = new int[c][2];

        int[] datamod = this.h;
        for(int j = 0; j < c; j++){
            int maxval = 0;
            int maxidx = 0;
            for(int i = 0; i < datamod.length; i++){
                if(maxval < datamod[i]){
                    maxval = datamod[i];
                    maxidx = i;
                }
            }
            top[j][0] = maxval;
            top[j][1] = maxidx;

            datamod[maxidx] = 0;
        }

        Arrays.sort(top, new java.util.Comparator<int[]>(){
            @Override
            public int compare(int[] ints, int[] t1) {
                return Integer.compare(ints[1],t1[1]);
            }
        });

        return top;
    }
}
