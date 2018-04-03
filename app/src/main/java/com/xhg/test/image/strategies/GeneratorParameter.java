package com.xhg.test.image.strategies;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.Objects;

public class GeneratorParameter {
    private static final int OPAQUE = 0xff000000;  // 完全不透明

    @IntRange(from = 1, to = 10000)
    private int mHeight = 1024;
    @IntRange(from = 1, to = 10000)
    private int mWidth = 1024;

    private int mTotal;

    private int mAlpha = OPAQUE;
    /**
     * 颜色生成策略
     */
    @NonNull
    private ColorStrategy mStrategy;

    /**
     * 回调
     */
    @NonNull
    private BitmapGenerator.Callback mCallback;

    private BitmapGenerator mGenerator;

    public GeneratorParameter() {
    }

    public GeneratorParameter(BitmapGenerator bitmapGenerator) {
        mGenerator = bitmapGenerator;
    }

    public void checkParameter() {
        mTotal = mWidth * mHeight;
        checkNumber(mTotal, 1, Integer.MAX_VALUE);
        Objects.requireNonNull(mCallback);
        Objects.requireNonNull(mStrategy);
        mStrategy.setParameters(mAlpha, mWidth, mHeight);   //init() will be called in this method
    }

    // 检查数据是否合理，闭区间
    private static int checkNumber(int number, int left, int right) {
        if (number < left || number > right) {
            throw new IllegalArgumentException("params not right, should between " + left +
                    " and " + right + ", actually is " + number);
        }
        return number;
    }

    // get and set begin
    public int getHeight() {
        return mHeight;
    }

    public GeneratorParameter setHeight(int height) {
        mHeight = checkNumber(height, 1, 10000);
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public GeneratorParameter setWidth(int width) {
        mWidth = checkNumber(width, 1, 10000);
        return this;
    }

    public int getTotal() {
        return mTotal;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public GeneratorParameter setStrategy(ColorStrategy strategy) {
        mStrategy = strategy;
        return this;
    }

    public BitmapGenerator.Callback getCallback() {
        return mCallback;
    }

    public GeneratorParameter setCallback(BitmapGenerator.Callback callback) {
        mCallback = Objects.requireNonNull(callback);
        return this;
    }

    public GeneratorParameter setAlpha(int alpha) {
        mAlpha = checkNumber(alpha, 1, 255) << 24;
        return this;
    }
    // get and set end

}
