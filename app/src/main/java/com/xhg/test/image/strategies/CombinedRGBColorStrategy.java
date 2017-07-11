package com.xhg.test.image.strategies;

/**
 * Abstract class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 *
 * Subclasses can only override the getRGB() methods to return a combine RGB value.
 *
 * @author xionghg
 * @email xiong9394@gmail.com
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
}
