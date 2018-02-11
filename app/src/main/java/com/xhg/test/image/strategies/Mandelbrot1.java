package com.xhg.test.image.strategies;

/**
 * 分形图形1
 *
 * @author xionghg
 * @created 2017-07-04.
 */

public class Mandelbrot1 extends CombinedRGBColorStrategy {

    private int getK(int x, int y) {
        float m = 0, n = 0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (m * m - n * n + (x - 768.0) / 512);
            n = (float) (2 * m * n + (y - 512.0) / 512);
            m = a;
            if (m * m + n * n > 4)
                break;
        }
        return k;
    }

    @Override
    public int getRGB(int x, int y) {
        int k = getK(x, y);
        int r = (int) (Math.log(k) * 47);
        int b = 128 - (int) (Math.log(k) * 23);
        return generateRGB(r, r, b);
    }

    @Override
    public String getDescription() {
        return "Mandelbrot1";
    }
}
