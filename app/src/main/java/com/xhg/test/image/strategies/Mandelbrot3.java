package com.xhg.test.image.strategies;

/**
 * 分形图形3
 *
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-10.
 */

public class Mandelbrot3 extends ColorStrategy {

    @Override
    public int getRed(int x, int y) {
        double a = 0, b = 0, d, n = 0;
        for (; a * a + (d = b * b) < 4 && n++ < 8192; b = 2 * a * b + y / 5e4 + .06, a = a * a - d + x / 5e4 + .34)
            ;
        return (int) (n / 4);
    }

    @Override
    public int getGreen(int x, int y) {
        return 2 * getRed(x, y);
    }

    @Override
    public int getBlue(int x, int y) {
        return 4 * getRed(x, y);
    }

    @Override
    public int getRGB(int x, int y) {
        int rgb = 0;
        int r = getRed(x, y);
        rgb |= r % 256 << 16;
        rgb |= r * 2 % 256 << 8;
        rgb |= r * 4 % 256;
        return rgb;
    }
}
