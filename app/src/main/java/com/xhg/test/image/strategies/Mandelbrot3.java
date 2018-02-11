package com.xhg.test.image.strategies;

/**
 * 分形图形3
 *
 * @author xionghg
 * @created 2017-07-10.
 */

public class Mandelbrot3 extends CombinedRGBColorStrategy {

    @Override
    public int getRed(int x, int y) {
        double a = 0, b = 0, d, n = 0;
        for (; a * a + (d = b * b) < 4 && n++ < 8192; b = 2 * a * b + y / 5e4 + .06, a = a * a - d + x / 5e4 + .34)
            ;
        return (int) (n / 4);
    }

    @Override
    public int getRGB(int x, int y) {
        int r = getRed(x, y);
        int g = 2 * r;
        int b = 4 * r;
        return generateRGB(r, g, b);
    }

    @Override
    public String getDescription() {
        return "Mandelbrot3";
    }
}
