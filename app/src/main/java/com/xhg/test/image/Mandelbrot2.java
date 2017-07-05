package com.xhg.test.image;

/**
 * Created by xionghg on 17-7-4.
 */

/**
 * 分形图形2
 */
public class Mandelbrot2 implements ColorHolder.ColorStrategy {

    @Override
    public int getRed(int i, int j) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + j * 8e-9 - .645411;
            a = c - d + i * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, 3.));
    }

    @Override
    public int getGreen(int i, int j) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + j * 8e-9 - .645411;
            a = c - d + i * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, .7));
    }

    @Override
    public int getBlue(int i, int j) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + j * 8e-9 - .645411;
            a = c - d + i * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, .5));
    }
}
