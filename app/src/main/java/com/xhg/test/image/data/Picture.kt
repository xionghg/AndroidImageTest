package com.xhg.test.image.data

import android.graphics.Bitmap

import com.xhg.test.image.strategies.ColorStrategy

/**
 * @author xionghg
 * @created 2017-07-19.
 */

class Picture(val id: Int, val strategy: ColorStrategy) {

    var title: String
        private set

    var bitmap: Bitmap? = null

    init {
        title = "Picture$id"
    }

    fun recycle() {
        bitmap = null
    }
}
