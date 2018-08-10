package com.xhg.test.image.data

import com.xhg.test.image.strategies.ColorStrategy
import com.xhg.test.image.strategies.Mandelbrot1
import com.xhg.test.image.strategies.Mandelbrot2
import com.xhg.test.image.strategies.Mandelbrot3
import com.xhg.test.image.strategies.RandomPainter
import com.xhg.test.image.strategies.TableCloths

/**
 * @author xionghg
 * @created 18-5-10.
 */

object StrategyFactory {

    val strategies: Array<ColorStrategy> = arrayOf(Mandelbrot1(), Mandelbrot2(), Mandelbrot3(),
            TableCloths(), RandomPainter(), Mandelbrot1(), Mandelbrot2(), Mandelbrot3(),
            RandomPainter(), TableCloths())

    fun getStrategy(index: Int): ColorStrategy {
        return strategies[index]
    }
}
