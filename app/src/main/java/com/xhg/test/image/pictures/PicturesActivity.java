package com.xhg.test.image.pictures;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.xhg.test.image.R;
import com.xhg.test.image.data.source.PicturesRepository;
import com.xhg.test.image.utils.ActivityUtils;

/**
 * @author xionghg
 * @created 2017-07-04.
 */

public class PicturesActivity extends AppCompatActivity {

    private PicturesPresenter mPicturesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_32dp);
            //显示导航按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PicturesFragment picturesFragment =
                (PicturesFragment) getSupportFragmentManager().findFragmentById(R.id.main_content_frame);
        if (picturesFragment == null) {
            picturesFragment = PicturesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), picturesFragment, R.id.main_content_frame);
        }

        // Create the repository
        PicturesRepository repository = PicturesRepository.providePicturesRepository(getApplicationContext());

        mPicturesPresenter = new PicturesPresenter(repository, picturesFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home function is coming soon", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
