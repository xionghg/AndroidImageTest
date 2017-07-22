package com.xhg.test.image.strategies;

import android.os.Bundle;

import com.xhg.test.image.data.Picture;

import java.util.ArrayList;
import java.util.List;

import static com.xhg.test.image.utils.CheckUtils.checkNotNull;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-20.
 */

public class GeneratorManger {

    private static GeneratorManger INSTANCE;

    private List<ColorGenerator> mWorkingGenerators;

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

        ColorGenerator onCreateGenerator(int index);
        
        void onGenerateFinished(ColorGenerator generator, Picture data);
    }

    public void initGenerator(GeneratorCallback callback) {
        mGeneratorCallback = checkNotNull(callback);
        int size = mGeneratorCallback.setGeneratorSize();
        for (int i = 0; i < size; i++) {
            ColorGenerator generator = mGeneratorCallback.onCreateGenerator(i);
            mWorkingGenerators.add(generator);
        }
    }

    public void start() {
        for (ColorGenerator generator : mWorkingGenerators) {
            if (mWorkingGenerators.size() > ColorGenerator.CPU_COUNT) {
                generator.setRealParallelCount(2);
            }
            generator.startInParallel();
        }
    }

    public boolean isWorking() {
        return mWorkingGenerators!= null && mWorkingGenerators.size() > 0;
    }

}
