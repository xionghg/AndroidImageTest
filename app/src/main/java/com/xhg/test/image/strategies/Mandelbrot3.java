package com.xhg.test.image.strategies;

/**
 * Created by xionghg on 17-7-4.
 */

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分形图形2
 */
public class Mandelbrot3 implements ColorStrategy {

    private int[] mCache = new int[3];

    public AtomicInteger rcount = new AtomicInteger(0);
    public AtomicInteger gcount = new AtomicInteger(0);
    @Override
    public int getRed(int x, int y) {
        double a = 0, b = 0, d, n = 0;
        for (; a * a + (d = b * b) < 4 && n++ < 8192; b = 2 * a * b + y / 5e4 + .06, a = a * a - d + x / 5e4 + .34)
            ;
        mCache[0] = x;
        mCache[1] = y;
        mCache[2] = (int) (n / 4);
        rcount.getAndIncrement();
        return (int) (n / 4);
    }

    @Override
    public int getGreen(int x, int y) {
        if (mCache[0] == x && mCache[1] == y) {
//            Log.d("xhg", "getGreen: " + count.getAndIncrement());
            gcount.getAndIncrement();
            return 2 * mCache[2];
        }
        return 2 * getRed(x, y);
    }

    @Override
    public int getBlue(int x, int y) {
        if (mCache[0] == x && mCache[1] == y) {
            return 4 * mCache[2];
        }
        return 4 * getRed(x, y);
    }
}
