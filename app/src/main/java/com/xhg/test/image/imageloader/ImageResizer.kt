@file:JvmName("ImageResizer")

package com.xhg.test.image.imageloader

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.xhg.test.image.utils.Log

import java.io.FileDescriptor

/**
 * @author xionghg
 * @created 17-8-3.
 */

private const val TAG = "ImageResizer"

fun decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                    reqWidth: Int, reqHeight: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

fun decodeSampledBitmapFromPath(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(path, options)
}

fun decodeSampledBitmapFromFileDescriptor(fd: FileDescriptor,
                                          reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFileDescriptor(fd, null, options)

    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFileDescriptor(fd, null, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    if (reqWidth == 0 || reqHeight == 0) {
        return 1
    }

    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    Log.d(TAG, "origin, w=$width, h=$height")
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps
        // both height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    Log.d(TAG, "sampleSize: $inSampleSize")
    return inSampleSize
}
