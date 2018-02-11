package com.xhg.test.image.strategies;

/**
 * Abstract class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 * <p>
 * Subclasses can only override three separate getX() methods.
 *
 * @author xionghg
 * @created 2017-07-08.
 */

public abstract class SeparateRGBColorStrategy implements ColorStrategy {

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
        return ALPHA | (getRed(x, y) % 256 << 16) | (getGreen(x, y) % 256 << 8) | (getBlue(x, y) % 256);
    }

    @Override
    public void init() {
    }

    @Override
    public void recycle() {
    }

    protected int WIDTH;
    protected int HEIGHT;
    protected int ALPHA;

    @Override
    public void setParameters(int alpha, int width, int height) {
        ALPHA = alpha;
        WIDTH = width;
        HEIGHT = height;
        init();
    }

    @Override
    public String getDescription() {
        return "SeparateRGBColorStrategy";
    }
}
