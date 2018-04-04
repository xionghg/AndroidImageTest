package com.xhg.test.image.strategies;

import com.xhg.test.image.data.Picture;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author xionghg
 * @created 2017-07-20.
 */

public class GeneratorManger {

    private static GeneratorManger INSTANCE;

    private List<BitmapGenerator> mWorkingGenerators;

    public static GeneratorManger getInstance() {
        if (INSTANCE == null) {
            synchronized (GeneratorManger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GeneratorManger();
                }
            }
        }
        return INSTANCE;
    }

    private GeneratorManger() {
        mWorkingGenerators = new ArrayList<>();
    }

    private GeneratorCallback mGeneratorCallback;

    //使用GeneratorManger，则使用此回调
    public interface GeneratorCallback {

        int setGeneratorSize();

        BitmapGenerator onCreateGenerator(int index);

        void onGenerateFinished(BitmapGenerator generator, Picture data);
    }

    public void initGenerator(GeneratorCallback callback) {
        mGeneratorCallback = requireNonNull(callback);
        int size = mGeneratorCallback.setGeneratorSize();
        for (int i = 0; i < size; i++) {
            BitmapGenerator generator = mGeneratorCallback.onCreateGenerator(i);
            mWorkingGenerators.add(generator);
        }
    }

    public void start() {
        for (BitmapGenerator generator : mWorkingGenerators) {
            if (mWorkingGenerators.size() > BitmapGenerator.CPU_COUNT) {
                generator.startInSequential();
            } else {
                generator.startInParallel();
            }
        }
    }

    public boolean isWorking() {
        return mWorkingGenerators != null && mWorkingGenerators.size() > 0;
    }

}
