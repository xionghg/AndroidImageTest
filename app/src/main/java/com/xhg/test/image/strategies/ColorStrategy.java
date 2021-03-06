package com.xhg.test.image.strategies;

/**
 * Super class as a color strategy to be invoked.
 * Return RGB values for a specified pixel.
 * <p>
 * At the beginning of this project, I simply wanted to define this super class as an interface,
 * but when the project got bigger, some problems had been exposed, like multithreading and
 * default-method missing. So I decided to define this as an abstract class.
 * <p>
 * After that, I also found that calculation for R,G,b values in some strategy are separate but
 * some other's are not, So finally I made this interface and defined two different abstract classes
 * to implements it.
 *
 * @author xionghg
 * @created 2017-07-08.
 */

public interface ColorStrategy {

    /**
     * Simple default method to invoke the three method above.
     * Override this method when needed to simplify your strategy.
     *
     * @param x The position in horizontal
     * @param y The position in vertical
     * @return RGB values of pixel
     */
    int getRGB(int x, int y);

    /**
     * Set max width and height if needed.
     *
     * @param alpha
     * @param width
     * @param height
     */
    void setParameters(int alpha, int width, int height);

    /**
     * Called after strategy finish.
     * Override this method if some release operations are needed.
     */
    void recycle();

    /**
     * @return strategy description
     */
    String getDescription();

}
