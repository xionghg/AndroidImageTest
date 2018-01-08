package com.xhg.test.image.strategies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xionghg
 * @created 2017-07-04.
 */

public class ColorGenerator {

    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final String TAG = "ColorHolder";
    private static final int PARALLEL_COUNT = Math.max(2, Math.min(CPU_COUNT - 1, 8));
    private static AtomicInteger sId = new AtomicInteger(1);
    private final AtomicInteger resultCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private String mName;
    private int mHeight;
    private int mWidth;
    private int mAlpha = 0xff << 24;
    private int mRealParallelCount = PARALLEL_COUNT;
    private ColorStrategy mStrategy;
    private Callback mCallback;
    private int[] mColorArray;
    private int[] mProgress;
    private volatile Status mStatus = Status.PENDING;

    public void setRealParallelCount(int realParallelCount) {
        mRealParallelCount = realParallelCount;
    }

    private ColorGenerator(Builder builder) {
        setName(builder.name);
        setCallback(builder.callback);
        setWidthAndHeight(builder.width, builder.height);
        setStrategy(builder.colorStrategy);
    }

    public void setWidthAndHeight(int width, int height) {
        if (numberWrong(width) || numberWrong(height)) {
            throw new IllegalArgumentException("params not right");
        }
        this.mWidth = width;
        this.mHeight = height;
        mColorArray = new int[width * height];
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    private boolean numberWrong(int number) {
        return (number <= 0 || number > 10000);
    }

    /**
     * Create a bitmap using the color array created.
     *
     * @return Null if mStatus is not Status.FINISHED, always invoke this method in CallBack.
     */
    public Bitmap createBitmap() {
        if (mStatus != Status.FINISHED) {
            return null;
        }
        return Bitmap.createBitmap(mColorArray, mWidth, mHeight, Bitmap.Config.ARGB_8888);
    }

    private void updateProgress(int index, int progress) {
        if (mCallback != null) {
            mProgress[index] = progress;
            int current = 0;
            int total = 0;
            for (int p : mProgress) {
                current += p;
                total += 100;
            }
            mCallback.onProgressUpdate(current * 100 / total);
        }
    }

    private void createColorFinish() {
        Log.d(TAG, "create color finish: ");
        mStatus = Status.FINISHED;
        mStrategy.recycle(); // release after finished
        if (mCallback != null) {
            if (successCount.get() == mProgress.length) {
                mCallback.onProgressUpdate(100);
                mCallback.onColorsCreated(createBitmap());
            } else {
                mCallback.onProgressUpdate(0);
                mCallback.onError();
            }
        }
        resultCount.set(0);
        successCount.set(0);
    }

    private void startCreateColor(boolean inParallel) {
        Log.d(TAG, "create color startCreateColor: cpu_count=" + CPU_COUNT);
        mStatus = Status.RUNNING;

        if (mStrategy == null) {
            // Initialize a strategy if not specified
            mStrategy = new Mandelbrot1();
        }
        mStrategy.setWidthAndHeight(mWidth, mHeight);   //init() will be called in this method

        if (mCallback != null) {
            mCallback.onStart();
        }

        if (inParallel) {
            mProgress = new int[mRealParallelCount];
            int begin;
            int end = 0;
            for (int i = 1; i <= mRealParallelCount; i++) {
                begin = end;
                end = mHeight * i / mRealParallelCount;
                // Log.d(TAG, "startCreateColor in parallel, begin=" + begin + " end=" + end);
                new MyAsyncTask(i - 1, "AsyncTask#" + i).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, begin, end);
            }
        } else {
            mProgress = new int[1];
            // Log.d(TAG, "startCreateColor in serial, begin=0 end=" + mHeight);
            new MyAsyncTask(0, "AsyncTask#Single").execute(0, mHeight);
        }
    }

    public void startInParallel() {
        startCreateColor(mHeight > 300 || mColorArray.length > 100000);
    }

    public void startInSerial() {
        startCreateColor(false);
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public ColorGenerator setStrategy(ColorStrategy strategy) {
        mStrategy = strategy;
        return this;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public ColorGenerator setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    /**
     * Cause mColorArray is generated by ColorStrategy, so there is no
     * setColorArray() method, use setStrategy() instead.
     *
     * @return mColorArray
     */
    public int[] getColorArray() {
        return mColorArray;
    }

    /**
     * Set the alpha value for the Bitmap if you want
     */
    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    /**
     * Indicates the current status of the holder.
     */
    public enum Status {
        PENDING, RUNNING, FINISHED,
    }

    /**
     * 直接使用ColorGenerator，则使用此回调
     */
    public interface Callback {
        /**
         * Called before generate colors, add this interface because we always want to do the
         * same things before startCreateColor generate colors, like set Button to disable.
         */
        void onStart();

        /**
         * Called when progress updated..
         */
        void onProgressUpdate(int progress);

        /**
         * Called when colors has been created.
         */
        void onColorsCreated(Bitmap bitmap);

        /**
         * Called when errors happened.
         */
        void onError();
    }

    public static class Builder {
        private int width = 1024;
        private int height = 1024;
        private String name = null;
        private ColorStrategy colorStrategy;
        private Callback callback;

        public Builder(Callback callback) {
            this.callback = callback;
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

        public ColorGenerator build() { // 构建，返回一个新对象
            return new ColorGenerator(this);
        }

        public void start() { // 构建，返回一个新对象
            build().startInParallel();
        }
    }

    /**
     * An implementation of {@link ColorGenerator.Callback} that has empty method bodies and
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
        public abstract void onColorsCreated(Bitmap bitmap);

        @Override
        public void onError() {
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        private int mIndex = 0;
        private String mName = "AsyncTask";

        public MyAsyncTask(int index, String name) {
            super();
            mIndex = index;
            mName = name;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.v(TAG, mName + " doInBackground:");
            if (params.length < 2) {
                return false;
            }
            int start = params[0];
            int end = params[1];
            //单线程7.869s
            //2线程4.001s
            //3线程4.294s  3664 4405 4815
            //6线程1.773s  1761 1871 1688
            //7线程1.763s
            for (int j = start; j < end; j++) {
                for (int i = 0; i < mWidth; i++) {
                    int rgb = mStrategy.getRGB(i, j);
                    mColorArray[j * mWidth + i] = mAlpha | rgb;
                }
                publishProgress(j, start, end);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int total = values[2] - values[1];
            int current = values[0] - values[1] + 1;
            updateProgress(mIndex, current * 100 / total);
        }

        @Override
        protected void onPostExecute(Boolean execRight) {
            Log.d(TAG, mName + " onPostExecute called");
            if (execRight) {
                successCount.getAndIncrement();
            }
            if (resultCount.incrementAndGet() == mProgress.length) {
                createColorFinish();
            }
        }
    }
}
