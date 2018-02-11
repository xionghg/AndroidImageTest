package com.xhg.test.image.strategies;

/**
 * 分形图形2
 *
 * @author xionghg
 * @created 2017-07-04.
 */

public class Mandelbrot2 extends CombinedRGBColorStrategy {

    private double getN(int x, int y) {
        double a = 0, b = 0, c, d, n = 0;
        while ((c = a * a) + (d = b * b) < 4 && n++ < 880) {
            b = 2 * a * b + y * 8e-9 - .645411;
            a = c - d + x * 8e-9 + .356888;
        }
        return n;
    }

    @Override
    public int getRGB(int x, int y) {
        double n = getN(x, y);
        int r = (int) (255 * Math.pow((n - 80) / 800, 3.));
        int g = (int) (255 * Math.pow((n - 80) / 800, .7));
        int b = (int) (255 * Math.pow((n - 80) / 800, .5));
        return generateRGB(r, g, b);
    }

    @Override
    public String getDescription() {
        return "Mandelbrot2";
    }
}
