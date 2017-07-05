package com.xhg.test.image;

import android.graphics.Bitmap;

/**
 * Created by xionghg on 17-7-4.
 */

public class ColorHolder {

    public int height;
    public int width;
    public int[] colorArray;
    public ColorStrategy strategy;
    public int alpha = 255 << 24;

    public ColorHolder(ColorStrategy strategy) {
        this(720, 720, strategy);
    }

    public ColorHolder(int width, int height, ColorStrategy strategy) {
        if (width <= 0 || height <= 0 || strategy == null) {
            throw new IllegalArgumentException();
        }
        this.width = width;
        this.height = height;
        this.strategy = strategy;
        colorArray = new int[width * height];
        createColors();
    }

    public Bitmap createBitmap() {
        return Bitmap.createBitmap(colorArray, width, height, Bitmap.Config.ARGB_8888);
    }

    public void createColors() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = strategy.getRed(i, j) % 256;
                int g = strategy.getGreen(i, j) % 256;
                int b = strategy.getBlue(i, j) % 256;
                colorArray[i * width + j] = alpha | r | g | b;
//                    if (i+j < width) {
//                        colorArray[i * width + j] = 0xffff0000;
//                    } else {
//                        colorArray[i * width + j] = 0xff00ff00;
//                    }
//                    if (i<256) {
//                        Log.d(TAG, String.format("createColors: a=%d, r=%d, g=%d, b=%d", a, r, g, b));
//                    }
            }
        }
    }

    public interface ColorStrategy {
        int getRed(int i, int j);

        int getGreen(int i, int j);

        int getBlue(int i, int j);
    }
}
