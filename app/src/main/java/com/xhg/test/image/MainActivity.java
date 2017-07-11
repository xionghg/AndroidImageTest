package com.xhg.test.image;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xhg.test.image.adapter.MainRecyclerAdapter;
import com.xhg.test.image.model.StrategyModel;
import com.xhg.test.image.strategies.ColorStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author xionghg
 * @email xiong9394@gmail.com
 * @created 2017-07-04.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TestBitmap-Main";

    private Button mToButton;
    private RecyclerView mRecyclerView;
    private List<ColorStrategy> mStrategies;
    private MainRecyclerAdapter mRecyclerAdapter;
    private StrategyModel mStrategyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();

        mToButton = (Button) findViewById(R.id.to_button);
        mToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BigImageActivity.class);
                intent.putExtra("strategy_index", 2);
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerAdapter = new MainRecyclerAdapter(MainActivity.this, mStrategies);
        //设置Adapter
        mRecyclerView.setAdapter(mRecyclerAdapter);
        //设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter.setOnItemClickListener(new MainRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: position=" + position);
                Intent intent = new Intent(MainActivity.this, BigImageActivity.class);
                intent.putExtra("strategy_index", position);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
    }

    private void initData() {
        mStrategyModel = StrategyModel.getInstance();
        mStrategies = Arrays.asList(mStrategyModel.getStrategies());
    }
}
