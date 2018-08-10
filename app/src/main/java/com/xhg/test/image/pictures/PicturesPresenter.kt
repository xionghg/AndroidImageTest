package com.xhg.test.image.pictures

import com.xhg.test.image.data.Picture
import com.xhg.test.image.data.source.PicturesRepository
import com.xhg.test.image.strategies.BitmapGenerator
import com.xhg.test.image.utils.Log
import java.util.*


/**
 * @author xionghg
 * @created 17-7-20.
 */

class PicturesPresenter(private val picturesRepository: PicturesRepository,
                        private val picturesView: PicturesContract.View) : PicturesContract.Presenter {

    private lateinit var currentPictures: List<Picture>
    private val bitmapGenerators = ArrayList<BitmapGenerator>()

    private var isFirstLoad: Boolean = false

    init {
        picturesView.setPresenter(this)
    }

    override fun start() {
        currentPictures = picturesRepository.pictures
        picturesView.showPictures(currentPictures)
        //startGenerateColor();
    }

    override fun startGenerateColor() {
        if (bitmapGenerators.isEmpty()) {
            for (i in 0..4/*currentPictures.size()*/) {
                Log.e(TAG, "start generator$i")
                val generator = BitmapGenerator.with { bitmap ->
                    currentPictures[i].bitmap = bitmap
                    picturesView.showPictureUpdate(i, currentPictures[i])
                }
                        .setWidth(256)
                        .setHeight(256)
                        .setStrategy(currentPictures[i].strategy)

                bitmapGenerators.add(generator)
            }
        }

        bitmapGenerators.forEach { it.startInParallel() }
    }

    override fun cancelCurrentOperation() {
        Log.d(TAG, "cancelCurrentOperation: ")
        bitmapGenerators.forEach { it.cancel() }
    }

    override fun result(requestCode: Int, resultCode: Int) {}

    private fun showFilteredPictures() {
        val picturesToDisplay = ArrayList<Picture>()

        currentPictures.filter { true }    // TODO: add some filters
                .forEach { picturesToDisplay.add(it) }

        processPictures(picturesToDisplay)
    }

    override fun loadPictures(forceUpdate: Boolean) {
        if (forceUpdate || isFirstLoad) {
            isFirstLoad = false
            //picturesRepository.refreshPictures()
        } else {
            showFilteredPictures()
            picturesView.setLoadingIndicator(false)
        }
    }

    private fun processPictures(pictures: List<Picture>) {
        if (pictures.isEmpty()) {
            // Show a message indicating there are no pictures.
            processEmptyPictures()
        } else {
            // Show the list of pictures
            picturesView.showPictures(pictures)
        }
    }

    private fun processEmptyPictures() {
        picturesView.showNoPictures()
    }

    override fun openPictureDetails(requestedPicture: Picture) {
        Objects.requireNonNull(requestedPicture, "requestedPicture cannot be null!")
        picturesView.showPictureDetailsUi(requestedPicture.id)
    }

    companion object {
        private const val TAG = "PicturesPresenter"
    }
}
