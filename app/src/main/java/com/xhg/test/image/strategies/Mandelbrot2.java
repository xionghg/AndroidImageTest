package com.xhg.test.image.strategies;

/**
 * 分形图形2
 *
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-04.
 */

public class Mandelbrot2 extends ColorStrategy {

    @Override
    public int getRed(int x, int y) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + y * 8e-9 - .645411;
            a = c - d + x * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, 3.));
    }

    @Override
    public int getGreen(int x, int y) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + y * 8e-9 - .645411;
            a = c - d + x * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, .7));
    }

    @Override
    public int getBlue(int x, int y) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + y * 8e-9 - .645411;
            a = c - d + x * 8e-9 + .356888;
        }
        return (int) (255 * Math.pow((n - 80) / 800, .5));
    }
}
