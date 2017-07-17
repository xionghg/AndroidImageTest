package com.xhg.test.image.strategies;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-16.
 */

public class RandomPainter extends CombinedRGBColorStrategy {

    private char[][] r;
    private char[][] g;
    private char[][] b;

    private char random(int n) {
        return (char) (1024 * Math.random() % n);
    }

    private char getColor(char[][] c, int i, int j) {
        if (c[i][j] == 0) {
            if (random(999) == 0) {
                return c[i][j] = random(256);
            } else {
                return c[i][j] = getColor(c, (i + random(2)) % WIDTH, (j + random(2)) % HEIGHT);
            }
        } else {
            return c[i][j];
        }
    }

    @Override
    public void init() {
        r = new char[WIDTH][HEIGHT];
        g = new char[WIDTH][HEIGHT];
        b = new char[WIDTH][HEIGHT];
    }

    @Override
    public int getRGB(int x, int y) {
        int red = (int) getColor(r, x, y);
        int green = (int) getColor(g, x, y);
        int blue = (int) getColor(b, x, y);
        return generateRGB(red, green, blue);
    }

    @Override
    public void recycle() {
        r = null;
        g = null;
        b = null;
    }
}
