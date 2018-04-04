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

    protected int WIDTH;
    protected int HEIGHT;
    protected int ALPHA;

    @Override
    public void setParameters(int alpha, int width, int height) {
        ALPHA = alpha;
        WIDTH = width;
        HEIGHT = height;
    }

    @Override
    public void recycle() {
    }

    protected final int generateRGB(int r, int g, int b) {
        return ALPHA | (r % 256 << 16) | (g % 256 << 8) | (b % 256);
    }

    @Override
    public String getDescription() {
        return "CombinedRGBColorStrategy";
    }
}
