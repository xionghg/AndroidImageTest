package com.xhg.test.image.strategies;

/**
 * Created by daxiong on 2017-07-08.
 */

/**
 * Interface definition for a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 */
public interface ColorStrategy {
    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return R values of pixel
     */
    int getRed(int x, int y);

    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return G values of pixel
     */
    int getGreen(int x, int y);

    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return B values of pixel
     */
    int getBlue(int x, int y);
}
