package com.xhg.test.image.strategies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.xhg.test.image.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 位图生成器，根据颜色生成策略生成相应的位图，只供单次使用
 *
 * @author xionghg
 * @created 2017-07-04.
 */

public class BitmapGenerator {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final String TAG = "BitmapGenerator";
    private static final int OPAQUE = 0xff000000;  // 完全不透明

    private String mName;
    @IntRange(from = 1, to = 10000)
    private int mHeight;
    @IntRange(from = 1, to = 10000)
    private int mWidth;
    @IntRange(from = 1, to = 255)
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
    private Callback mCallback;
    /**
     * 颜色数组，占用内存大，使用后回收
     */
    private int[] mColorArray;
    /**
     * 生成器状态
     */
    private volatile Status mStatus = Status.PENDING;

    private BitmapGenerator(Builder builder) {
        mName = builder.name;
        mCallback = Objects.requireNonNull(builder.callback);
        setWidth(builder.width);
        setHeight(builder.height);
        mStrategy = Objects.requireNonNull(builder.colorStrategy);
    }

    private Bitmap createBitmap() {
        if (mStatus != Status.FINISHED) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mColorArray, mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mColorArray = null;
        return bitmap;
    }

    private void updateProgress(int progress) {
        mCallback.onProgressUpdate(progress);
    }

    private void generatePixelFinished() {
        Log.d(TAG, "generate pixel finished");
        mStatus = Status.FINISHED;
        mStrategy.recycle(); // release after finished
        mCallback.onBitmapCreated(createBitmap());
    }

    private void startGeneratePixel(boolean inParallel) {
        Log.d(TAG, "startGeneratePixel: cpu_count=" + CPU_COUNT + ", inParallel=" + inParallel);
        mStatus = Status.RUNNING;
        // 开始之后才分配内存
        mColorArray = new int[mWidth * mHeight];
        mStrategy.setWidthAndHeight(mWidth, mHeight);   //init() will be called in this method

        mCallback.onStart();
        new MyAsyncTask(inParallel, 0, mHeight).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // 开始并行执行
    public void startInParallel() {
        startGeneratePixel(mHeight > 300 || mColorArray.length > 100000);
    }

    // 开始串行执行
    public void startInSerial() {
        startGeneratePixel(false);
    }

    // 检查数据是否合理，闭区间
    private static int checkNumber(int number, int left, int right) {
        if (number < left || number > right) {
            throw new IllegalArgumentException("params not right, should between " + left +
                    " and " + right + ", actually is " + number);
        }
        return number;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = checkNumber(height, 1, 10000);
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = checkNumber(width, 1, 10000);
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public void setStrategy(ColorStrategy strategy) {
        mStrategy = strategy;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = Objects.requireNonNull(callback);
    }

    public void setAlpha(int alpha) {
        mAlpha = checkNumber(alpha, 1, 255);
    }
    // get and set end

    /**
     * Indicates the current status of the generator.
     */
    public enum Status {
        PENDING, RUNNING, FINISHED
    }

    /**
     * 直接使用ColorGenerator，则使用此回调
     */
    public interface Callback {
        /**
         * Called before generate colors, add this interface because we always want to do the
         * same things before startGeneratePixel generate colors, like set Button to disable.
         */
        void onStart();

        /**
         * Called when progress updated..
         */
        void onProgressUpdate(int progress);

        /**
         * Called when colors has been created.
         */
        void onBitmapCreated(Bitmap bitmap);

        /**
         * Called when errors happened.
         */
        void onError(String errorMsg);
    }

    /**
     * An implementation of {@link BitmapGenerator.Callback} that has empty method bodies and
     * default return values.
     */
    public static abstract class SimpleCallback implements Callback {
        @Override
        public void onStart() {
        }

        @Override
        public void onProgressUpdate(int progress) {
        }

        @Override
        public abstract void onBitmapCreated(Bitmap bitmap);

        @Override
        public void onError(String errorMsg) {
        }
    }

    public static class Builder {
        private int width = 1024;
        private int height = 1024;
        private String name = null;
        private ColorStrategy colorStrategy;
        private Callback callback;

        public Builder() {
        }

        public Builder setCallBack(Callback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setColorStrategy(ColorStrategy colorStrategy) {
            this.colorStrategy = colorStrategy;
            return this;
        }

        public BitmapGenerator build() { // 构建，返回一个新对象
            return new BitmapGenerator(this);
        }

        public void start() { // 构建，返回一个新对象
            build().startInParallel();
        }
    }

    private static final AtomicInteger sIndex = new AtomicInteger(0);

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        String mName = "AsyncTask";
        boolean mInParallel;
        int mStartHeight;
        int mEndHeight;
        int mTotalPoint;
        int mLastProgress = 0;
        final AtomicInteger mCurrentPoint = new AtomicInteger(0);

        public MyAsyncTask(boolean inParallel, int start, int end) {
            super();
            mInParallel = inParallel;
            mStartHeight = start;
            mEndHeight = end;
            mTotalPoint = (end - start) * mWidth;
            mName = "MyAsyncTask@" + sIndex.incrementAndGet() + "-" + (inParallel ? "1" : "0");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.d(TAG, mName + " doInBackground");
            // 单线程7.869s; 2线程4.001s; 3线程4.294s  3664 4405 4815;
            // 6线程1.773s  1761 1871 1688; 7线程1.763s

            List<Point> points = new ArrayList<>(mTotalPoint);
            for (int y = mStartHeight; y < mEndHeight; y++) {
                for (int x = 0; x < mWidth; x++) {
                    points.add(new Point(x, y));
                }
            }
            if (mInParallel) {
                points.parallelStream().forEach(this::getPixelOnPoint);
            } else {
                points.forEach(this::getPixelOnPoint);
            }
            return true;
        }

        private void getPixelOnPoint(Point p) {
            int rgb = mStrategy.getRGB(p.x, p.y);
            mColorArray[p.y * mWidth + p.x] = mAlpha | rgb;

            int currentProgress = mCurrentPoint.incrementAndGet() * 100 / mTotalPoint;
            if (mTotalPoint <= 100) {
                publishProgress(currentProgress);
            } else {
                if (currentProgress > mLastProgress) {
                    mLastProgress = currentProgress;
                    publishProgress(mLastProgress);
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            updateProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, mName + " onPostExecute");
            generatePixelFinished();
        }
    }

    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
