package com.xhg.test.image.strategies;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-16.
 */

public class RandomPainter extends CombinedRGBColorStrategy {

    private static int DIM = 1024;
    char[][] r = new char[DIM][DIM];
    char[][] g = new char[DIM][DIM];
    char[][] b = new char[DIM][DIM];

    private char random(int n) {
        return (char) (DIM * Math.random() % n);
    }

    private char getColor(char[][] c, int i, int j) {
        if (c[i][j] == 0) {
            if (random(999) == 0) {
                return c[i][j] = random(256);
            } else {
                return c[i][j] = getColor(c, (i + random(2)) % DIM, (j + random(2)) % DIM);
            }
        } else {
            return c[i][j];
        }
    }

    @Override
    public int getRGB(int x, int y) {
        int red = (int) getColor(r, x, y) % 256 << 16;
        int green = (int) getColor(g, x, y) % 256 << 8;
        int blue = (int) getColor(b, x, y) % 256;
        return red | green | blue;
    }
}
