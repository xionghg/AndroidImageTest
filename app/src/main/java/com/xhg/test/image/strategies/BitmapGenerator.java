package com.xhg.test.image.strategies;

import android.graphics.Bitmap;

import com.xhg.test.image.utils.Log;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.schedulers.Schedulers;

/**
 * 位图生成器，根据颜色生成策略生成相应的位图，可重复使用
 *
 * @author xionghg
 * @created 2017-07-04.
 */

public class BitmapGenerator {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final AtomicInteger sIndex = new AtomicInteger(0);
    private final AtomicInteger mCurrentPoint = new AtomicInteger(0);
    private String mTag;
    private volatile Status mStatus = Status.PENDING;
    private long mStartTime;
    private Disposable mDisposable;

    private int mAlpha = 0xff000000;  // 不透明度，默认完全不透明
    private int mHeight = 1024;       // 高，默认1024
    private int mWidth = 1024;        // 宽，默认1024
    private ColorStrategy mStrategy;  // 颜色策略，不设置则使用默认全黑策略
    private int[] mColorArray;        // 颜色数组，占用内存大，使用后回收
    // 回调，不可为空
    private Callback mCallback;

    private BitmapGenerator(Callback callback) {
        mTag = "BitmapGenerator-" + sIndex.getAndIncrement();
        setCallback(callback);
    }

    public static BitmapGenerator with(Callback callback) {
        return new BitmapGenerator(callback);
    }

    public void checkParameter() {
        final int total = mWidth * mHeight;
        checkNumber(total, 1, Integer.MAX_VALUE);
        Objects.requireNonNull(mCallback);
        if (mStrategy == null) {
            mStrategy = new DefaultStrategy();
        }
        mStrategy.setParameters(mAlpha, mWidth, mHeight);   //init() will be called in this method
    }

    // get and set begin
    public int getHeight() {
        return mHeight;
    }

    public BitmapGenerator setHeight(int height) {
        mHeight = checkNumber(height, 1, 10000);
        return this;
    }

    public int getWidth() {
        return mWidth;
    }

    public BitmapGenerator setWidth(int width) {
        mWidth = checkNumber(width, 1, 10000);
        return this;
    }

    public ColorStrategy getStrategy() {
        return mStrategy;
    }

    public BitmapGenerator setStrategy(ColorStrategy strategy) {
        mStrategy = strategy;
        return this;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public BitmapGenerator setCallback(Callback callback) {
        mCallback = Objects.requireNonNull(callback);
        return this;
    }

    public BitmapGenerator setAlpha(int alpha) {
        mAlpha = checkNumber(alpha, 1, 255) << 24;
        return this;
    }
    // get and set end

    public void cancel() {
        if (mStatus != Status.RUNNING) {
            Log.w(mTag, "cancel: not running, no need to cancel");
            return;
        }
        Log.d(mTag, "cancel is called");
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mCallback.onProgressUpdate(0);
        mCallback.onCanceled();
        softReset();
        mStatus = Status.PENDING;
    }

    // 开始并行执行
    public void startInParallel() {
        startGeneratePixel(true);
    }

    // 开始串行执行
    public void startInSequential() {
        startGeneratePixel(false);
    }

    private void startGeneratePixel(boolean inParallel) {
        if (mStatus == Status.RUNNING) {
            Log.w(mTag, "startGeneratePixel: is running now, return");
            return;
        }
        mStatus = Status.RUNNING;
        Log.d(mTag, "startGeneratePixel: cpu_count=" + CPU_COUNT + ", inParallel=" + inParallel);
        checkParameter();
        mColorArray = new int[mWidth * mHeight];        // 开始之后才分配内存
        mCallback.onStart();

        mStartTime = System.currentTimeMillis();
        mCurrentPoint.set(0);
        Flowable<Integer> flowable;
        if (inParallel) {
            flowable = ParallelFlowable.from(Flowable.range(0, mHeight), 2)
                    .runOn(Schedulers.computation())
                    .map(this::getPixelOnLine)
                    .filter(progress -> progress > 0)
                    .sequential();
        } else {
            flowable = Flowable.range(0, mHeight)
                    .map(this::getPixelOnLine)
                    .filter(progress -> progress > 0)
                    .subscribeOn(Schedulers.computation());
        }
        mDisposable = flowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateProgress,
                        this::handleError,
                        this::generatePixelFinished);
    }

    // return progress, -1 if not yet
    private int getPixelOnLine(int line) {
        for (int i = 0; i < mWidth; i++) {
            mColorArray[line * mWidth + i] = mStrategy.getRGB(i, line);
        }
        final int pre = mCurrentPoint.getAndIncrement();
        int preProgress = pre * 100 / mHeight;
        int nowProgress = (pre + 1) * 100 / mHeight;
        if (nowProgress > preProgress) {
            Log.d(mTag, "getPixelOnLine: new progress=" + nowProgress);
            return nowProgress;
        }
        return -1;
    }

    private void updateProgress(int progress) {
        Log.d(mTag, "updateProgress: " + progress);
        mCallback.onProgressUpdate(progress);
    }

    private void handleError(Throwable e) {
        Log.e(mTag, "error happened when generate color: ", e);
    }

    private void generatePixelFinished() {
        long cost = System.currentTimeMillis() - mStartTime;
        Log.d(mTag, "generate pixel finished, cost: " + cost + "ms");
        Bitmap bitmap = Bitmap.createBitmap(mColorArray, mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCallback.onBitmapCreated(bitmap);
        softReset();
        mStatus = Status.FINISHED;
    }

    private void softReset() {
        mStrategy.recycle(); // release after finished
        mColorArray = null;
        mDisposable = null;
    }

    // 检查数据是否合理，闭区间
    private int checkNumber(int number, int left, int right) {
        if (number < left || number > right) {
            throw new IllegalArgumentException("params not right, should between " + left +
                    " and " + right + ", actually is " + number);
        }
        return number;
    }

    /**
     * Indicates the current status of the generator.
     */
    private enum Status {
        PENDING, RUNNING, FINISHED
    }

    @FunctionalInterface
    public interface Callback {
        /**
         * Called before generate colors, add this interface because we always want to do the
         * same things before startGeneratePixel generate colors, like set Button to disable.
         */
        default void onStart() {
        }

        default void onProgressUpdate(int progress) {
        }

        void onBitmapCreated(Bitmap bitmap);

        default void onCanceled() {
        }
    }

    // A default strategy that return black color for all
    private static class DefaultStrategy extends CombinedRGBColorStrategy {
        @Override
        public int getRGB(int x, int y) {
            return 0;
        }
    }
}
