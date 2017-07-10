package com.xhg.test.image.strategies;

/**
 * Super class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 *
 * At the beginning of this project, I simply wanted to define this super class as an interface,
 * but when the project got bigger, some problems had been exposed, like multithreading and
 * default-method missing. So I finally decided to define this as an abstract class.
 *
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-08.
 */

public abstract class ColorStrategy {
    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return R values of pixel
     */
    protected abstract int getRed(int x, int y);

    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return G values of pixel
     */
    protected abstract int getGreen(int x, int y);

    /**
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return B values of pixel
     */
    protected abstract int getBlue(int x, int y);

    /**
     * Simple default method to invoke the three method above.
     * Override this method when needed to simplify your strategy.
     *
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return RGB values of pixel
     */
    public int getRGB(int x, int y) {
        int rgb = 0;
        rgb |= (getRed(x, y) % 256) << 16;
        rgb |= (getGreen(x, y) % 256) << 8;
        rgb |= (getBlue(x, y) % 256);
        return rgb;
    }
}
