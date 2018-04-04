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
    private static final String TAG = "BitmapGenerator";
    private final AtomicInteger mCurrentPoint = new AtomicInteger(0);
    private Disposable mDisposable;
    /**
     * 颜色数组，占用内存大，使用后回收
     */
    private int[] mColorArray;
    /**
     * 生成器状态
     */
    private volatile Status mStatus = Status.PENDING;
    private GeneratorParameter p;
    private long startTime;

    private BitmapGenerator(GeneratorParameter parameter) {
        setParameter(parameter);
    }

    public static BitmapGenerator with(GeneratorParameter parameter) {
        return new BitmapGenerator(parameter);
    }

    public GeneratorParameter getParameter() {
        return p;
    }

    public void setParameter(GeneratorParameter parameter) {
        p = Objects.requireNonNull(parameter, "parameter can't be null");
    }

    public void cancel() {
        if (mStatus != Status.RUNNING) {
            Log.w(TAG, "cancel: not running, no need to cancel");
            return;
        }
        Log.d(TAG, "cancel is called");
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = null;
        }
        p.getCallback().onProgressUpdate(0);
        p.getStrategy().recycle(); // release after finished
        mColorArray = null;
        p.getCallback().onCanceled();
        mStatus = Status.PENDING;
    }

    // 开始并行执行
    public void startInParallel() {
        startGeneratePixel(true);
    }

    // 开始串行执行
    public void startInSerial() {
        startGeneratePixel(false);
    }

    private void startGeneratePixel(boolean inParallel) {
        if (mStatus == Status.RUNNING) {
            Log.w(TAG, "startGeneratePixel: is running now, return");
            return;
        }
        Log.d(TAG, "startGeneratePixel: cpu_count=" + CPU_COUNT + ", inParallel=" + inParallel);
        p.checkParameter();
        mStatus = Status.RUNNING;
        // 开始之后才分配内存
        mColorArray = new int[p.getTotal()];
        p.getCallback().onStart();

        startTime = System.currentTimeMillis();
        mCurrentPoint.set(0);
        if (inParallel) {
            ParallelFlowable<Integer> pFlowable = ParallelFlowable.from(Flowable.range(0, p.getHeight()), 2);
            mDisposable = pFlowable
                    .runOn(Schedulers.computation())
                    .map(this::getPixelOnLine)
                    .filter(progress -> progress > 0)
                    .sequential()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateProgress,
                            e -> Log.e(TAG, "startGeneratePixel: ", e),
                            this::generatePixelFinished);
        } else {
            mDisposable = Flowable.range(0, p.getHeight())
                    .map(this::getPixelOnLine)
                    .filter(progress -> progress > 0)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateProgress,
                            e -> Log.e(TAG, "startGeneratePixel: ", e),
                            this::generatePixelFinished);
        }
    }

    // return progress, -1 if not yet
    private int getPixelOnLine(int line) {
        for (int i = 0; i < p.getWidth(); i++) {
            mColorArray[line * p.getWidth() + i] = p.getStrategy().getRGB(i, line);
        }

        final int pre = mCurrentPoint.getAndIncrement();
        final int now = pre + 1;
        int preProgress = pre * 100 / p.getHeight();
        int nowProgress = now * 100 / p.getHeight();
        if (nowProgress > preProgress) {
            Log.d(TAG, "getPixelOnLine: progress=" + nowProgress);
            return nowProgress;
        }
        return -1;
    }

    private void updateProgress(int progress) {
        Log.d(TAG, "updateProgress: " + progress);
        p.getCallback().onProgressUpdate(progress);
    }

    private void generatePixelFinished() {
        long cost = System.currentTimeMillis() - startTime;
        Log.d(TAG, "generate pixel finished, cost: " + cost + "ms");
        mStatus = Status.FINISHED;
        Bitmap bitmap = Bitmap.createBitmap(mColorArray, p.getWidth(), p.getHeight(), Bitmap.Config.ARGB_8888);
        p.getStrategy().recycle(); // release after finished
        mColorArray = null;
        mDisposable = null;
        p.getCallback().onBitmapCreated(bitmap);
    }

    /**
     * Indicates the current status of the generator.
     */
    public enum Status {
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
}
