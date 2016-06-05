package com.alfiyan.suaratuyuls.core;

import android.util.Log;

/**
 * Created by Alfiyan on 09-Jul-15.
 */
public class Tuyul {
    public static String determine(int[][] best, int c){
        String col1=null, col2=null;
        int max=0,min=360;

        for(int i=0;i<c;i++){
            if(best[i][1] > max){
                max = best[i][1];
            }
            if(best[i][1] < min){
                min = best[i][1];
            }
        }
        col1 = toColor(min);
        /*
        if(max-min < 30){
            col2 = col1;
        }else{
            col2 = toColor(max);
        }*/
        col2 = toColor(max);
        /*
        for(int i=0;i<5;i++){
            for(int j = i+1; j < 5; j++){
                if(j == i || j >=5){
                    break;
                }
                if(Math.abs(best[j][1]-best[i][1]) > 40){
                    col1 = toColor(best[j][1]);
                    col2 = toColor(best[i][1]);
                    //return col1+" "+col2;
                }else{
                    col1 = toColor(best[j][1]);
                    if(col1.equals(col2)){
                        continue;
                    }
                    col2 = toColor(best[i][1]);
                }
            }
        }*/
        return col1+" "+col2;
    }

    public static int determine(int[][] best, int c, boolean gabut){
        String warna = determine(best, c);
        int nom = 0;

        if(warna.equals("Merah Kuning") || warna.equals("Merah Hijau")){
            nom = 1000;
        }else if(warna.equals("Putih Putih")){
            nom = 2000;
        }else if(warna.equals("Merah Kuning") || warna.equals("Kuning Kuning") || warna.equals("Orange Kuning") || warna.equals("Orange Orange")){
            nom = 5000;
        }else if(warna.equals("Merah Ungu") || warna.equals("Merah Merah")){
            nom = 10000;
        }else if(warna.equals("Hijau Biru")){
            nom = 20000;
        }else if(warna.equals("Biru Biru")){
            nom = 50000;
        }else if(warna.equals("Ungu Merah") || warna.equals("Ungu Ungu")){
            nom = 100000;
        }

        return nom;
    }

    private static String toColor(int c){
        String warna = null;
        Log.d("Color", c+"");

        //using if else
        /*
        if(c > 340 || c <= 25){
            warna = "Merah";
        }else if(c <= 40){
            warna = "Orange";
        }else if(c <= 85){
            warna = "Kuning";
        }else if(c <= 155){
            warna = "Hijau";
        }else if(c <= 265){
            warna = "Biru";
        }else if(c <= 340){
            warna = "Ungu";
        }*/

        //nearest neighbor
        int[] color = {0,30,60,120,180,240,300,330,360};
        int nearest = 0, minLength = 361;
        for(int i=0;i<color.length;i++){
            if(Math.abs(color[i] - c) < minLength){
                minLength = Math.abs(color[i] - c);
                nearest = color[i];
            }
        }

        Log.i("Color-Nearest",nearest+"");

        if(nearest == 0 || nearest == 360){
            warna = "Merah";
        }else if(nearest == 30){
            warna = "Orange";
        }else if(nearest == 60){
            warna = "Kuning";
        }else if(nearest == 120){
            warna = "Hijau";
        }else if(nearest == 180 || nearest == 240){
            warna = "Biru";
        }else if(nearest == 300 || nearest == 330){
            warna = "Ungu";
        }

        return warna;
    }
}
