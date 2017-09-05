package com.xhg.test.image.strategies;

import java.util.LinkedList;
import java.util.List;

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
//        if (c[i][j] == 0) {
//            if (random(999) == 0) {
//                return c[i][j] = random(256);
//            } else {
//                return c[i][j] = getColor(c, (i + random(2)) % WIDTH, (j + random(2)) % HEIGHT);
//            }
//        } else {
//            return c[i][j];
//        }
        // code above can easily lead to a stack overflow.
        if (c[i][j] == 0) {
            List<Point> cache = new LinkedList<>();
            int ii = i;
            int ij = j;
            char result = 0;
            while (random(999) != 0 && (result = c[ii][ij]) == 0) {
                cache.add(new Point(ii, ij));
                ii = (ii + random(2)) % WIDTH;
                ij = (ij + random(2)) % HEIGHT;
            }
            cache.add(new Point(ii, ij));

            if (result == 0) {
                result = random(256);
            }
            for (Point p : cache) {
                c[p.x][p.y] = result;
            }
        }
        return c[i][j];
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
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
