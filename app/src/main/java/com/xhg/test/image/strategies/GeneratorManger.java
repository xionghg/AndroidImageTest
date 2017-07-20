package com.xhg.test.image.strategies;

import android.os.Bundle;

import com.xhg.test.image.data.Picture;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-20.
 */

public class GeneratorManger {

    private GeneratorCallback mGeneratorCallback;

    public interface GeneratorCallback {

        void onGenerateStart(int size);

        ColorGenerator onCreateGenerator(int index, Bundle args);
        
        void onGenerateFinished(ColorGenerator generator, Picture data);
    }

    public GeneratorManger() {

    }

    public void initGenerator(GeneratorCallback callback) {
        mGeneratorCallback = callback;
        start();
    }

    private void start() {

    }

}
