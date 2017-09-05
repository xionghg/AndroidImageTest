package com.xhg.test.image.data;

import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.strategies.Mandelbrot1;
import com.xhg.test.image.strategies.Mandelbrot2;
import com.xhg.test.image.strategies.Mandelbrot3;
import com.xhg.test.image.strategies.RandomPainter;
import com.xhg.test.image.strategies.TableCloths;

/**
 * @author xionghg
 * @created 17-7-11.
 */

public class StrategyFactory {

    private ColorStrategy[] mStrategies;

    private static StrategyFactory INSTANCE;

    public static StrategyFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (StrategyFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StrategyFactory();
                }
            }
        }
        return INSTANCE;
    }

    private StrategyFactory() {
        init();
    }

    private void init() {
        mStrategies = new ColorStrategy[]{new RandomPainter(), new Mandelbrot2(), new Mandelbrot3()
                , new TableCloths(), new Mandelbrot1(), new Mandelbrot1()
                , new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot2()
                , new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot1()};
    }

    public ColorStrategy getStrategy(int index) {
        return mStrategies[index];
    }

    public ColorStrategy[] getStrategies() {
        return mStrategies;
    }
}
