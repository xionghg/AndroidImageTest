package com.xhg.test.image.strategies;

/**
 * Created by xionghg on 17-7-4.
 */

/**
 * 分形图形1
 */
public class Mandelbrot1 implements ColorStrategy {

    @Override
    public int getRed(int x, int y) {
        float m = 0, n = 0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (m * m - n * n + (x - 768.0) / 512);
            n = (float) (2 * m * n + (y - 512.0) / 512);
            m = a;
            if (m * m + n * n > 4)
                break;
        }
        return (int) (Math.log(k) * 47);
    }

    @Override
    public int getGreen(int x, int y) {
        float m = 0, n = 0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (m * m - n * n + (x - 768.0) / 512);
            n = (float) (2 * m * n + (y - 512.0) / 512);
            m = a;
            if (m * m + n * n > 4)
                break;
        }
        return (int) (Math.log(k) * 47);
    }

    @Override
    public int getBlue(int x, int y) {
        float m = 0, n = 0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (m * m - n * n + (x - 768.0) / 512);
            n = (float) (2 * m * n + (y - 512.0) / 512);
            m = a;
            if (m * m + n * n > 4)
                break;
        }
        return 128 - (int) (Math.log(k) * 23);
    }
}
