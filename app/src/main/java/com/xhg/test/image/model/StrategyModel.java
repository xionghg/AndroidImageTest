package com.xhg.test.image.model;

import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.strategies.Mandelbrot1;
import com.xhg.test.image.strategies.Mandelbrot2;
import com.xhg.test.image.strategies.Mandelbrot3;

/**
 * @author xionghg
 * @created 17-7-11.
 */

public class StrategyModel {

    private ColorStrategy[] mStrategies;

    private static StrategyModel sModel;

    public static StrategyModel getInstance() {
        if (sModel == null) {
            synchronized (StrategyModel.class) {
                if (sModel == null) {
                    sModel = new StrategyModel();
                }
            }
        }
        return sModel;
    }

    private StrategyModel() {
        init();
    }

    private void init() {
        mStrategies = new ColorStrategy[]{new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot3()
                , new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot3()
                , new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot3()
                , new Mandelbrot1(), new Mandelbrot2(), new Mandelbrot3()};
    }

    public ColorStrategy getStrategy(int index) {
        return mStrategies[index];
    }

    public ColorStrategy[] getStrategies() {
        return mStrategies;
    }
}
