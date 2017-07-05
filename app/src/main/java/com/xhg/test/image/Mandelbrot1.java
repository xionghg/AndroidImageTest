package com.xhg.test.image;

/**
 * Created by xionghg on 17-7-4.
 */

/**
 * 分形图形1
 */
public class Mandelbrot1 implements ColorHolder.ColorStrategy {

    @Override
    public int getRed(int i, int j) {
        float x=0,y=0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (x*x - y*y + (i-768.0)/512);
            y = (float) (2*x*y + (j-512.0)/512);
            x = a;
            if (x*x + y*y > 4)
                break;
        }
        return (int) (Math.log(k)*47);
    }

    @Override
    public int getGreen(int i, int j) {
        float x=0,y=0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (x*x - y*y + (i-768.0)/512);
            y = (float) (2*x*y + (j-512.0)/512);
            x = a;
            if (x*x + y*y > 4)
                break;
        }
        return (int) (Math.log(k)*47);
    }

    @Override
    public int getBlue(int i, int j) {
        float x=0,y=0;
        int k;
        for (k = 0; k++ < 256; ) {
            float a = (float) (x*x - y*y + (i-768.0)/512);
            y = (float) (2*x*y + (j-512.0)/512);
            x = a;
            if (x*x + y*y > 4)
                break;
        }
        return 128 - (int) (Math.log(k)*23);
    }
}
