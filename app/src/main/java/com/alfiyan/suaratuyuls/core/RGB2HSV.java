package com.alfiyan.suaratuyuls.core;

import android.graphics.Color;

/**
 * Created by Alfiyan on 09-Jul-15.
 */
public class RGB2HSV {
    public static double[] convert(int w) {
        double[] x = prosenRGB(w);
        double Cmax = max(x[0], x[1], x[2]);
        double Cmin = min(x[0], x[1], x[2]);
        double d = Cmax - Cmin;

        double h = 0, s = 0;

        if (d == 0) {
            h = 0;
        } else if (Cmax == x[0]) {
            h = 60 * (((x[1] - x[2]) / d) % 6);
        } else if (Cmax == x[1]) {
            h = 60 * (((x[2] - x[0]) / d) + 2);
        } else if (Cmax == x[2]) {
            h = 60 * (((x[0] - x[1]) / d) + 4);
        }

        if (Cmax == 0) {
            s = 0;
        } else {
            s = d / Cmax;
        }

        double v = Cmax;

        double[] hsv = {h, s, v};
        return hsv;
    }

    public static int convertS(int w) {
        double[] x = prosenRGB(w);
        double Cmax = max(x[0], x[1], x[2]);
        double Cmin = min(x[0], x[1], x[2]);
        double d = Cmax - Cmin;

        double h = 0;

        if (d == 0) {
            h = 0;
        } else if (Cmax == x[0]) {
            h = 60 * (((x[1] - x[2]) / d) % 6);
        } else if (Cmax == x[1]) {
            h = 60 * (((x[2] - x[0]) / d) + 2);
        } else if (Cmax == x[2]) {
            h = 60 * (((x[0] - x[1]) / d) + 4);
        }

        return (int)h;
    }

    private static double[] prosenRGB(int w) {
        double[] warna = {(double) Color.red(w) / 255, (double) Color.green(w) / 255, (double) Color.blue(w) / 255};
        return warna;
    }

    private static double max(double r, double g, double b) {
        double max = r;
        if (max < g) {
            max = g;
        }
        if (max < b) {
            max = b;
        }
        return max;
    }

    private static double min(double r, double g, double b) {
        double min = r;
        if (min > g) {
            min = g;
        }
        if (min > b) {
            min = b;
        }
        return min;
    }

}
