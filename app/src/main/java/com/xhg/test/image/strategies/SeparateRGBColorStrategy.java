package com.xhg.test.image.strategies;

/**
 * Abstract class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 *
 * Subclasses can only override three separate getX() methods.
 *
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-08.
 */

public abstract class SeparateRGBColorStrategy implements ColorStrategy{

    public abstract int getRed(int x, int y);

    public abstract int getGreen(int x, int y);

    public abstract int getBlue(int x, int y);

    /**
     * Simple default method to invoke the three method above.
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

    @Override
    public void init() {
    }

    @Override
    public void recycle() {
    }

    private int WIDTH;
    private int HEIGHT;

    @Override
    public void setWidthAndHeight(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        init();
    }
}
