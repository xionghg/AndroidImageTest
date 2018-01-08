package com.xhg.test.image.strategies;

/**
 * Abstract class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 * <p>
 * Subclasses can only override the getRGB() methods to return a combine RGB value.
 *
 * @author xionghg
 * @created 2017-07-11.
 */

public abstract class CombinedRGBColorStrategy implements ColorStrategy {
    @Override
    public int getRed(int x, int y) {
        return 0;
    }

    @Override
    public int getGreen(int x, int y) {
        return 0;
    }

    @Override
    public int getBlue(int x, int y) {
        return 0;
    }

    @Override
    public abstract int getRGB(int x, int y);

    @Override
    public void init() {
    }

    @Override
    public void recycle() {
    }

    protected int WIDTH;
    protected int HEIGHT;

    @Override
    public void setWidthAndHeight(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        init();
    }

    protected int generateRGB(int r, int g, int b) {
        return (r % 256 << 16) | (g % 256 << 8) | (b % 256);
    }
}
