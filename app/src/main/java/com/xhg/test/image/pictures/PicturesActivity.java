package com.xhg.test.image.pictures;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.xhg.test.image.data.source.PicturesLoader;
import com.xhg.test.image.data.source.PicturesRepository;
import com.xhg.test.image.R;
import com.xhg.test.image.data.StrategyFactory;
import com.xhg.test.image.strategies.ColorStrategy;
import com.xhg.test.image.strategies.GeneratorManger;
import com.xhg.test.image.utils.ActivityUtils;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
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
        //设置图标
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_32dp);
        //显示导航按钮
        actionBar.setDisplayHomeAsUpEnabled(true);


        PicturesFragment picturesFragment =
                (PicturesFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (picturesFragment == null) {
            picturesFragment = PicturesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), picturesFragment, R.id.content_frame);
        }

        // Create the presenter
        PicturesRepository repository = PicturesRepository.providePicturesRepository(getApplicationContext());

        mPicturesPresenter = new PicturesPresenter(
                repository,
                picturesFragment
        );
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
        return true;
    }
}
