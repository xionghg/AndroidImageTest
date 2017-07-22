package com.xhg.test.image.strategies;

import android.os.Bundle;

import com.xhg.test.image.data.Picture;

import java.util.ArrayList;
import java.util.List;

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

    public interface GeneratorCallback {

        void onGenerateStart(int size);

        ColorGenerator onCreateGenerator(int index, Bundle args);
        
        void onGenerateFinished(ColorGenerator generator, Picture data);
    }

    public void initGenerator(GeneratorCallback callback) {
        mGeneratorCallback = callback;
        start();
    }

    public void start() {

    }

    public boolean isWorking() {
        return mWorkingGenerators!= null && mWorkingGenerators.size() > 0;
    }

}
