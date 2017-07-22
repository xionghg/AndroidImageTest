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

    private static final String TAG = "TestBitmap-Main";

    private Button mToButton;
    private RecyclerView mRecyclerView;
    private List<ColorStrategy> mStrategies;
    private PicturesAdapter mRecyclerAdapter;
    private StrategyFactory mStrategyModel;
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

//        mToButton = (Button) findViewById(R.id.to_button);
//        mToButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PicturesActivity.this, PictureDetailActivity.class);
//                intent.putExtra("strategy_index", 4);
//                startActivity(intent);
//            }
//        });

//        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
//        LinearLayoutManager layoutManager = new GridLayoutManager(PicturesActivity.this, 2, GridLayoutManager.VERTICAL, false);
//        //设置布局管理器
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerAdapter = new PicturesAdapter(PicturesActivity.this, mStrategies);
//        //设置Adapter
//        mRecyclerView.setAdapter(mRecyclerAdapter);
//        //设置增加或删除条目的动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerAdapter.setOnItemClickListener(new PicturesAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Log.d(TAG, "onItemClick: position=" + position);
//                Intent intent = new Intent(PicturesActivity.this, PictureDetailActivity.class);
//                intent.putExtra("strategy_index", position);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//            }
//        });
        PicturesFragment picturesFragment =
                (PicturesFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (picturesFragment == null) {
            picturesFragment = PicturesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), picturesFragment, R.id.content_frame);
        }

        // Create the presenter
        PicturesRepository repository = PicturesRepository.providePicturesRepository(getApplicationContext());
        PicturesLoader picturesLoader = new PicturesLoader(getApplicationContext(), repository);

        mPicturesPresenter = new PicturesPresenter(
                GeneratorManger.getInstance(),
                repository,
                picturesFragment
        );

    }

//    private void initData() {
//        mStrategyModel = StrategyModel.getInstance();
//        mStrategies = Arrays.asList(mStrategyModel.getStrategies());
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Home function is coming soon", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.settings:
//                Intent intent = new Intent(PicturesActivity.this, SettingsActivity.class);
//                startActivity(intent);
//                // Toast.makeText(this, "To be coming soon", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.help:
//                Toast.makeText(this, "To be coming soon", Toast.LENGTH_SHORT).show();
//                break;
            default:
                break;
        }
        return true;
    }
}
