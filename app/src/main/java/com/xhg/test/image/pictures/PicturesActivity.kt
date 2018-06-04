package com.xhg.test.image.pictures

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast

import com.xhg.test.image.R
import com.xhg.test.image.data.source.PicturesRepository
import com.xhg.test.image.utils.ActivityUtils

/**
 * @author xionghg
 * @created 2017-07-04.
 */

class PicturesActivity : AppCompatActivity() {

    private var picturesPresenter: PicturesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the toolbar.
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            //设置图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_32dp)
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        var picturesFragment = supportFragmentManager.findFragmentById(R.id.main_content_frame)
        if (picturesFragment == null) {
            picturesFragment = PicturesFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, picturesFragment, R.id.main_content_frame)
        }

        // Create the repository
        val repository = PicturesRepository.providePicturesRepository(applicationContext)

        picturesPresenter = PicturesPresenter(repository, picturesFragment as PicturesFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> Toast.makeText(this, "Home function is coming soon", Toast.LENGTH_SHORT).show()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
