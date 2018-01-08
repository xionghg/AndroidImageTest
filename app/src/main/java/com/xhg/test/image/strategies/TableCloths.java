package com.xhg.test.image.strategies;

/**
 * @author xionghg
 * @created 2017-07-16.
 */

public class TableCloths extends CombinedRGBColorStrategy {
    private static final int DIM = 1024;

    @Override
    public int getRGB(int x, int y) {
        double[] params = getParams(x, y);
        int r = ((int) params[0] % 2 + (int) params[1] % 2) * 127;
        int g = ((int) (5 * params[0]) % 2 + (int) (5 * params[1]) % 2) * 127;
        int b = ((int) (29 * params[0]) % 2 + (int) (29 * params[1]) % 2) * 127;
        return generateRGB(r, g, b);
    }

    private double[] getParams(int x, int y) {
        double h = 3. / (y + 99);
        double i = (y + Math.sin((x * x + (y - 700) * (y - 700) * 5) / 100. / DIM) * 35) * h;
        double j = (x + DIM) * h + i;
        double k = (DIM * 2 - x) * h + i;
        return new double[]{j, k};
    }
}
