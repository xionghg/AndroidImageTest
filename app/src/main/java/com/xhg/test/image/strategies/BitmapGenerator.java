package com.xhg.test.image.strategies;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.xhg.test.image.utils.Log;

import java.util.Locale;
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
    private static final int PARALLEL_COUNT = Math.max(2, Math.min(CPU_COUNT - 1, 8));
    private static final int OPAQUE = 0xff000000;  // 完全不透明

    private final AtomicInteger resultCount = new AtomicInteger(0);
    private final AtomicInteger successCount = new AtomicInteger(0);
    private String mName;
    private int mHeight;
    private int mWidth;
    /**
     * 位图alpha值, 初始为完全不透明
     */
    private int mAlpha = OPAQUE;
    /**
     * 并行线程数, 可设置为1~20, 过大或过小都会影响效率
     */
    private int mRealParallelCount = PARALLEL_COUNT;
    /**
     * 颜色生成策略
     */
    private ColorStrategy mStrategy;
    /**
     * 回调，必需非空
     */
    private Callback mCallback;
    /**
     * 颜色数组，占用内存大，使用后回收
     */
    private int[] mColorArray;
    /**
     * 回调数组
     */
    private int[] mProgress;
    /**
     * 生成器状态
     */
    private volatile Status mStatus = Status.PENDING;

    private BitmapGenerator(Builder builder) {
        setName(builder.name);
        setCallback(builder.callback);
        setWidth(builder.width);
        setHeight(builder.height);
        setStrategy(builder.colorStrategy);
    }

    private Bitmap createBitmap() {
        if (mStatus != Status.FINISHED) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mColorArray, mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mColorArray = null;
        return bitmap;
    }

    private void updateProgress(int index, int progress) {
        mProgress[index] = progress;
        int current = 0;
        int total = 0;
        for (int p : mProgress) {
            current += p;
            total += 100;
        }
        mCallback.onProgressUpdate(current * 100 / total);
    }

    private void createColorFinish() {
        Log.d(TAG, "create color finish: ");
        mStatus = Status.FINISHED;
        mStrategy.recycle(); // release after finished

        if (successCount.get() == mProgress.length) {
            mCallback.onProgressUpdate(100);
            mCallback.onBitmapCreated(createBitmap());
        } else {
            mCallback.onProgressUpdate(0);
            mCallback.onError("finish error: wrong count");
        }
        resultCount.set(0);
        successCount.set(0);
    }

    private void startCreateColor(boolean inParallel) {
        Log.d(TAG, "create color startCreateColor: cpu_count=" + CPU_COUNT);
        mStatus = Status.RUNNING;
        // 开始之后才分配内存
        mColorArray = new int[mWidth * mHeight];

        // Initialize a strategy if not specified
        if (mStrategy == null) {
            mStrategy = new Mandelbrot1();
        }
        mStrategy.setWidthAndHeight(mWidth, mHeight);   //init() will be called in this method

        mCallback.onStart();

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

    // 开始并行执行
    public void startInParallel() {
        startCreateColor(mHeight > 300 || mColorArray.length > 100000);
    }

    // 开始串行执行
    public void startInSerial() {
        startCreateColor(false);
    }

    /**
     * 检查数据是否合理，闭区间
     */
    private static int checkNumber(int number, int left, int right) {
        if (number < left || number > right) {
            throw new IllegalArgumentException(String.format(Locale.US, "params not right, should between %d and %d, actually is %d",
                    left, right, number));
        }
        return number;
    }

    // get and set begin
    public void setRealParallelCount(int realParallelCount) {
        mRealParallelCount = checkNumber(realParallelCount, 1, 20);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
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

    public BitmapGenerator setCallback(Callback callback) {
        mCallback = Objects.requireNonNull(callback);
        return this;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
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

        public BitmapGenerator build() { // 构建，返回一个新对象
            return new BitmapGenerator(this);
        }

        public void start() { // 构建，返回一个新对象
            build().startInParallel();
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
        protected void onPostExecute(Boolean success) {
            Log.d(TAG, mName + " onPostExecute called");
            if (success) {
                successCount.getAndIncrement();
            }
            if (resultCount.incrementAndGet() == mProgress.length) {
                createColorFinish();
            }
        }
    }
}
