package com.xhg.test.image.strategies;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.xhg.test.image.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 位图生成器，根据颜色生成策略生成相应的位图，可重复使用
 *
 * @author xionghg
 * @created 2017-07-04.
 */

public class BitmapGenerator {
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final String TAG = "BitmapGenerator";
    private static final AtomicInteger sIndex = new AtomicInteger(0);
    private final AtomicInteger mCurrentPoint = new AtomicInteger(0);
    private MyAsyncTask mMyAsyncTask;
    /**
     * 颜色数组，占用内存大，使用后回收
     */
    private int[] mColorArray;
    /**
     * 生成器状态
     */
    private volatile Status mStatus = Status.PENDING;
    private GeneratorParameter p;

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
        if (mMyAsyncTask != null) {
            mMyAsyncTask.cancel(false);
            mMyAsyncTask = null;
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
        mMyAsyncTask = new MyAsyncTask(inParallel, 0, p.getHeight());
        mMyAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // TODO: 使用rxjava，但效率貌似不高
//        mCurrentPoint.set(0);
//        Flowable.range(0, total)
//                .parallel()
//                .runOn(Schedulers.computation())
//                .map(this::getPixelOnPosition)
//                .filter(progress -> progress > 0)
//                .sequential()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::updateProgress,
//                        e -> Log.e(TAG, "startGeneratePixel: ", e),
//                        this::generatePixelFinished);
    }

    // return progress, -1 if not yet
    private int getPixelOnPosition(int pos) {
        final int x = pos % p.getWidth();
        final int y = pos / p.getWidth();
        // mColorArray[p.y * p.getWidth() + p.x] = mStrategy.getRGB(p.x, p.y);
        mColorArray[pos] = p.getStrategy().getRGB(x, y);

        final int pre = mCurrentPoint.getAndIncrement();
        final int now = pre + 1;
        int preProgress = pre * 100 / p.getTotal();
        int nowProgress = now * 100 / p.getTotal();
        if (nowProgress > preProgress) {
            Log.d(TAG, "getPixelOnPosition: progress=" + nowProgress);
            return nowProgress;
        }
        return -1;
    }

    private void updateProgress(int progress) {
        Log.d(TAG, "updateProgress: " + progress);
        p.getCallback().onProgressUpdate(progress);
    }

    private void generatePixelFinished() {
        Log.d(TAG, "generate pixel finished");
        mStatus = Status.FINISHED;
        Bitmap bitmap = Bitmap.createBitmap(mColorArray, p.getWidth(), p.getHeight(), Bitmap.Config.ARGB_8888);
        p.getStrategy().recycle(); // release after finished
        mColorArray = null;
        p.getCallback().onBitmapCreated(bitmap);
    }

    /**
     * Indicates the current status of the generator.
     */
    public enum Status {
        PENDING, RUNNING, FINISHED
    }

    /**
     * 直接使用ColorGenerator，则使用此回调
     */
    @FunctionalInterface
    public interface Callback {
        /**
         * Called before generate colors, add this interface because we always want to do the
         * same things before startGeneratePixel generate colors, like set Button to disable.
         */
        default void onStart() {
        }

        /**
         * Called when progress updated..
         */
        default void onProgressUpdate(int progress) {
        }

        /**
         * Called when colors has been created.
         */
        void onBitmapCreated(Bitmap bitmap);

        /**
         * Called when errors happened.
         */
        default void onCanceled() {
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

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Boolean> {
        final AtomicInteger mCurrentPoint = new AtomicInteger(0);
        String mName = "AsyncTask";
        boolean mInParallel;
        int mStartHeight;
        int mEndHeight;
        int mTotalPoint;
        int mLastProgress = 0;

        public MyAsyncTask(boolean inParallel, int start, int end) {
            super();
            mInParallel = inParallel;
            mStartHeight = start;
            mEndHeight = end;
            mTotalPoint = (end - start) * p.getWidth();
            mName = "MyAsyncTask@" + sIndex.incrementAndGet() + "-" + (inParallel ? "1" : "0");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.d(TAG, mName + " doInBackground begin");
            // 单线程7.869s; 2线程4.001s; 3线程4.294s  3664 4405 4815;
            // 6线程1.773s  1761 1871 1688; 7线程1.763s

            List<Point> points = new ArrayList<>(mTotalPoint);
            for (int y = mStartHeight; y < mEndHeight; y++) {
                for (int x = 0; x < p.getWidth(); x++) {
                    points.add(new Point(x, y));
                }
            }
            if (mInParallel) {
                points.parallelStream().forEach(this::getPixelOnPoint);
            } else {
                points.forEach(this::getPixelOnPoint);
            }

            Log.d(TAG, mName + " doInBackground end");
            return true;
        }

        private void getPixelOnPoint(Point point) {
            if (isCancelled()) {
                return;
            }
            mColorArray[point.y * p.getWidth() + point.x] = p.getStrategy().getRGB(point.x, point.y);

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
            generatePixelFinished();
        }
    }
}
